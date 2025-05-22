from dataclasses import dataclass
from uuid import UUID

from core.errors import ClosedError, DoesNotExistError
from core.receipts import Receipt, ReceiptProduct
from infra.sqllite.database import SqliteDatabase


@dataclass
class ReceiptSqlite:
    database: SqliteDatabase

    def create(self, receipt: Receipt) -> None:
        query = "INSERT INTO receipts (id, status, total) VALUES (?, ?, ?)"
        self.database.execute(query, (str(receipt.id), receipt.status, receipt.total))

    def _get_products(self, receipt_id: UUID) -> list[ReceiptProduct]:
        query = "SELECT product_id, quantity FROM sold_products where receipt_id = ?"
        result = self.database.fetch_all(query, (str(receipt_id),))
        items = []

        if not result:
            return []

        for product_id, quantity in result:
            query = "SELECT price FROM products WHERE id = ?"
            res = self.database.fetch_one(query, (str(product_id),))

            if res:
                items.append(
                    (ReceiptProduct(product_id, quantity, res[0], quantity * res[0]))
                )
            else:
                raise DoesNotExistError(
                    f"Product with id<{product_id}> does not exist."
                )
        return items

    def read(self, receipt_id: UUID) -> Receipt:
        query = "SELECT id, status, total FROM receipts WHERE id = ?"
        result = self.database.fetch_one(query, (str(receipt_id),))
        items = self._get_products(receipt_id)
        if result:
            return Receipt(result[1], items, result[2], UUID(result[0]))
        else:
            raise DoesNotExistError(f"Receipt with id<{receipt_id}> does not exist.")

    def read_all(self) -> list[Receipt]:
        query = "SELECT id, status, total FROM receipts"
        results = self.database.fetch_all(query)

        return [Receipt(row[1], [], row[2], UUID(row[0])) for row in results]

    def _add_products(self, receipt: Receipt) -> None:
        if len(receipt.products) == 0:
            return

        receipt_product = receipt.products[-1]
        query = "Select * from sold_products where receipt_id = ? and product_id = ?"
        res = self.database.fetch_one(query, (str(receipt.id), str(receipt_product.id)))

        if res:
            query = "UPDATE sold_products SET quantity=? "
            query += "where receipt_id = ? and product_id = ?"
            self.database.execute(
                query,
                (
                    receipt.products[-1].quantity,
                    str(receipt.id),
                    str(receipt_product.id),
                ),
            )
            return

        product_id, quantity = receipt_product.id, receipt_product.quantity
        query = "INSERT INTO sold_products (receipt_id, "
        query += "product_id, quantity) VALUES (?, ?, ?)"
        self.database.execute(query, (str(receipt.id), str(product_id), quantity))

    def update(self, receipt: Receipt) -> None:
        query = "UPDATE receipts SET status=?, total=? WHERE id=?"
        self.database.execute(query, (receipt.status, receipt.total, str(receipt.id)))
        self._add_products(receipt)

    def delete(self, receipt_id: UUID) -> None:
        receipt = self.read(receipt_id)
        if receipt.status == "closed":
            raise ClosedError

        query = "DELETE FROM receipts WHERE id=?"
        self.database.execute(query, (str(receipt_id),))
