from pos.customer import Customer, ShoppingCart
from pos.payment_strategy import RandomPaymentStrategy
from pos.product import Product
from pos.receipt import Receipt


def test_shopping_cart() -> None:
    shopping_cart = ShoppingCart()
    product = Product(1, "1", 1.0, 1)
    shopping_cart.add_product(product)

    assert next(iter(shopping_cart)) == product


def test_customer_checkout() -> None:
    shopping_cart = ShoppingCart()
    customer = Customer(shopping_cart, ["Card", "Cash"], RandomPaymentStrategy())

    assert customer.checkout() == shopping_cart


def test_customer_see_receipt() -> None:
    receipt = Receipt([])
    shopping_cart = ShoppingCart()
    customer = Customer(shopping_cart, ["Card", "Cash"], RandomPaymentStrategy())

    customer.see_receipt(receipt)
    assert customer.show_receipt() == receipt


def test_customer_pay() -> None:
    shopping_cart = ShoppingCart()
    customer = Customer(shopping_cart, ["Card", "Cash"], RandomPaymentStrategy())

    payment_type = customer.pay()
    assert payment_type == "Card" or payment_type == "Cash"
