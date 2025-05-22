import sqlite3
from typing import Dict, List, Protocol

from pos.constants import DATABASE_PATH
from pos.discount import Discount, IDiscount
from pos.product import IProduct, Product


class IStoreDatabase(Protocol):
    def get_product(self, product_id: int) -> IProduct | None:
        pass

    def get_all_products(self) -> List[IProduct]:
        pass

    def get_all_discounts(self) -> IDiscount:
        pass

    def get_sold_items(self) -> Dict[IProduct, int]:
        pass

    def get_revenue(self) -> Dict[str, float]:
        pass

    def flush_in_memory_data(
        self, sold_items: Dict[IProduct, int], revenue: Dict[str, float]
    ) -> None:
        pass

    def close_connection(self) -> None:
        pass


class StoreDatabase:
    def __init__(self, db_path: str = DATABASE_PATH) -> None:
        self._connection = sqlite3.connect(db_path)
        self._cursor = self._connection.cursor()

    def get_product(self, product_id: int) -> IProduct | None:
        self._cursor.execute(
            """SELECT name, price, unit FROM products WHERE id = ?""",
            (product_id,),
        )
        result = self._cursor.fetchone()
        if not result:
            return None
        name, price, unit = result
        product = Product(product_id, name, price, unit)
        return product

    def get_all_products(self) -> List[IProduct]:
        self._cursor.execute("SELECT * FROM products")
        results = self._cursor.fetchall()
        products: List[IProduct] = []
        for product_id, name, price, unit in results:
            product = Product(product_id, name, price, unit)
            products.append(product)
        return products

    def get_all_discounts(self) -> IDiscount:
        self._cursor.execute("SELECT applicable_product_ids, value FROM discounts")
        results = self._cursor.fetchall()

        discounts_dict = {}
        for applicable_product_ids, value in results:
            product_ids_tuple = tuple(map(int, applicable_product_ids.split(",")))
            discounts_dict[product_ids_tuple] = value

        return Discount(discounts_dict)

    def get_sold_items(self) -> Dict[IProduct, int]:
        self._cursor.execute("SELECT product_id, count FROM sold_products")
        results = self._cursor.fetchall()

        sold_items = {}
        for product_id, count in results:
            product = self.get_product(product_id)
            if product:
                sold_items[product] = count

        return sold_items

    def get_revenue(self) -> Dict[str, float]:
        self._cursor.execute("SELECT payment_type, revenue FROM all_time")
        results = self._cursor.fetchall()

        revenue = {}
        for payment_type, amount in results:
            revenue[payment_type] = amount

        return revenue

    def flush_in_memory_data(
        self, sold_items: Dict[IProduct, int], revenue: Dict[str, float]
    ) -> None:
        for product, count in sold_items.items():
            self._cursor.execute(
                """INSERT INTO sold_products (product_id, count)
                VALUES (?, ?)
                ON CONFLICT(product_id) DO UPDATE SET count = count + ?""",
                (product.get_id(), count, count),
            )

        for payment_type, rev in revenue.items():
            self._cursor.execute(
                """UPDATE all_time
                                            SET revenue = revenue + ?
                                            WHERE payment_type = ?""",
                (round(rev, 2), payment_type),
            )

        self._connection.commit()

    def clear(self) -> None:
        self._cursor.execute("DELETE FROM sold_products")
        self._connection.commit()
        self._cursor.execute("UPDATE all_time SET revenue = 0.0")
        self._connection.commit()

    def close_connection(self) -> None:
        if self._connection:
            self._connection.close()
