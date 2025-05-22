from dataclasses import dataclass
from sqlite3 import IntegrityError
from uuid import UUID

from core.errors import DoesNotExistError, ExistsError
from core.products import Product
from infra.sqllite.database import SqliteDatabase


@dataclass
class ProductSqlite:
    database: SqliteDatabase

    def create(self, product: Product) -> None:
        try:
            query = "INSERT INTO products "
            query += "(id, unit_id, name, barcode, price) VALUES (?, ?, ?, ?, ?)"
            self.database.execute(
                query,
                (
                    str(product.id),
                    str(product.unit_id),
                    product.name,
                    product.barcode,
                    product.price,
                ),
            )
        except IntegrityError:
            raise ExistsError

    def fetch(self, product_id: UUID) -> Product:
        query = "SELECT id, unit_id, name, barcode, price FROM products WHERE id = ?"
        result = self.database.fetch_one(query, (str(product_id),))

        if result:
            return Product(
                UUID(result[1]), result[2], result[3], result[4], UUID(result[0])
            )
        else:
            raise DoesNotExistError(f"Product with id<{product_id}> does not exist.")

    def fetch_all(self) -> list[Product]:
        query = "SELECT id, unit_id, name, barcode, price FROM products"
        results = self.database.fetch_all(query)

        return [
            Product(UUID(row[1]), row[2], row[3], row[4], UUID(row[0]))
            for row in results
        ]

    def update(self, product: Product) -> None:
        query = "UPDATE products SET unit_id=?, name=?, barcode=?, price=? WHERE id=?"
        self.database.execute(
            query,
            (
                str(product.unit_id),
                product.name,
                product.barcode,
                product.price,
                str(product.id),
            ),
        )
