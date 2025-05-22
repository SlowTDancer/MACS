from dataclasses import dataclass, field
from uuid import UUID

from core.errors import DoesNotExistError, ExistsError
from core.products import Product


@dataclass
class ProductInMemory:
    products: dict[UUID, Product] = field(default_factory=dict)

    def create(self, product: Product) -> None:
        existing_product = next(
            (p for p in self.products.values() if p.barcode == product.barcode), None
        )
        if existing_product:
            raise ExistsError
        self.products[product.id] = product

    def fetch(self, product_id: UUID) -> Product:
        try:
            return self.products[product_id]
        except KeyError:
            raise DoesNotExistError(product_id)

    def fetch_all(self) -> list[Product]:
        return list(self.products.values())

    def update(self, product: Product) -> None:
        if product.id not in self.products:
            raise DoesNotExistError
        self.products[product.id] = product
