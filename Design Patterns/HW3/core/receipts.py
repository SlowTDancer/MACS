from __future__ import annotations

from dataclasses import dataclass, field
from typing import Protocol
from uuid import UUID, uuid4

from core.products import Product


@dataclass
class ReceiptService:
    receipts: ReceiptRepository

    def create_receipt(self) -> Receipt:
        new_receipt = Receipt()
        self.receipts.create(new_receipt)
        return new_receipt

    def add_product_to_receipt(
        self, receipt_id: UUID, product: Product, quantity: int
    ) -> Receipt:
        receipt = self.receipts.read(receipt_id)
        receipt.add_product(product, quantity)
        self.receipts.update(receipt)
        return receipt

    def get_receipt(self, receipt_id: UUID) -> Receipt:
        return self.receipts.read(receipt_id)

    def close_receipt(self, receipt_id: UUID) -> None:
        receipt = self.receipts.read(receipt_id)
        receipt.close()
        self.receipts.update(receipt)

    def delete_receipt(self, receipt_id: UUID) -> None:
        self.receipts.delete(receipt_id)


class ReceiptRepository(Protocol):
    def create(self, receipt: Receipt) -> None:
        pass

    def read(self, receipt_id: UUID) -> Receipt:
        pass

    def read_all(self) -> list[Receipt]:
        pass

    def update(self, receipt: Receipt) -> None:
        pass

    def delete(self, receipt_id: UUID) -> None:
        pass


@dataclass
class ReceiptProduct:
    id: UUID
    quantity: int
    price: int
    total: int


@dataclass
class Receipt:
    status: str = "open"
    products: list[ReceiptProduct] = field(default_factory=list)
    total: int = field(default_factory=int)
    id: UUID = field(default_factory=uuid4)

    def add_product(self, product: Product, quantity: int) -> None:
        product_total = product.price * quantity
        self.total += product_total
        print(self.products, product.id)
        for receipt_product in self.products:
            if str(receipt_product.id) == str(product.id):
                print(quantity)
                receipt_product.quantity += quantity
                return

        product_total = product.price * quantity
        self.products.append(
            ReceiptProduct(product.id, quantity, product.price, product_total)
        )

    def close(self) -> None:
        self.status = "closed"
