from typing import List

from pos.discount import Discount
from pos.product import IProduct, Product
from pos.receipt import Receipt, ReceiptBuilder


def test_receipt_builder_add_entry() -> None:
    receipt_builder = ReceiptBuilder()
    product = Product(1, "1", 1, 1)

    assert len(receipt_builder._products) == 0
    receipt_builder.add_entry(product)

    assert len(receipt_builder._products) == 1


def test_receipt_builder_get_receipt() -> None:
    receipt_builder = ReceiptBuilder()
    product = Product(1, "1", 1, 1)

    receipt_builder.add_entry(product)
    receipt = receipt_builder.get_receipt()

    assert isinstance(receipt, Receipt)
    assert next(iter(receipt)) == product


def test_receipt_builder_clear() -> None:
    receipt_builder = ReceiptBuilder()
    product = Product(1, "1", 1, 1)

    receipt_builder.add_entry(product)

    assert len(receipt_builder._products) == 1

    receipt_builder.clear()
    assert len(receipt_builder._products) == 0


def test_receipt_get_total_price() -> None:
    products: List[IProduct] = [Product(1, "1", 1, 1), Product(2, "1", 5.0, 1)]
    receipt = Receipt(products)
    discounts = Discount({})
    assert receipt.get_total_price(discounts) == 6.0


def test_receipt_iteration() -> None:
    products: List[IProduct] = [Product(1, "1", 1, 1)]
    receipt = Receipt(products)

    assert next(iter(receipt)) == Product(1, "1", 1, 1)
