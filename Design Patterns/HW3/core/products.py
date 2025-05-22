from __future__ import annotations

from dataclasses import dataclass, field
from typing import Protocol
from uuid import UUID, uuid4


@dataclass
class ProductService:
    products: ProductRepository

    def create_product(
        self, unit_id: UUID, name: str, barcode: str, price: int, product_id: UUID
    ) -> None:
        new_product = Product(unit_id, name, barcode, price, product_id)
        self.products.create(new_product)

    def get_product(self, product_id: UUID) -> Product:
        return self.products.fetch(product_id)

    def get_all_products(self) -> list[Product]:
        return self.products.fetch_all()

    def update_product_price(self, product_id: UUID, new_price: int) -> None:
        product = self.products.fetch(product_id)
        product.price = new_price
        self.products.update(product)


class ProductRepository(Protocol):
    def create(self, product: Product) -> None:
        pass

    def fetch(self, product_id: UUID) -> Product:
        pass

    def fetch_all(self) -> list[Product]:
        pass

    def update(self, product: Product) -> None:
        pass


@dataclass
class Product:
    unit_id: UUID
    name: str
    barcode: str
    price: int
    id: UUID = field(default_factory=uuid4)
