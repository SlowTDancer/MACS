from pos.cashier import Cashier
from pos.discount import Discount
from pos.product import Product


def test_cashier_open_receipt() -> None:
    cashier = Cashier()
    assert not hasattr(cashier, "_receipt_builder")

    cashier.open_receipt()
    assert hasattr(cashier, "_receipt_builder")


def test_cashier_receipt() -> None:
    cashier = Cashier()
    cashier.open_receipt()
    product = Product(1, "1", 1, 1)

    cashier.add_product_to_receipt(product)
    assert next(iter(cashier.give_receipt())) == product


class Discounts:
    pass


def test_cashier_close_receipt() -> None:
    cashier = Cashier()
    cashier.open_receipt()
    product = Product(1, "1", 1, 1)
    cashier.add_product_to_receipt(product)
    cashier.close_receipt()

    assert cashier.give_receipt().get_total_price(Discount({})) == 0
