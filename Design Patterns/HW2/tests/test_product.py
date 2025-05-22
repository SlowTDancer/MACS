from pos.discount import Discount
from pos.product import Product


def test_product_attributes() -> None:
    sample_product = Product(1, "Sample Product", 10.0, 2)
    assert sample_product.get_id() == 1
    assert sample_product.get_name() == "Sample Product"
    assert sample_product.get_price(Discount({(1,): 0.5})) == 5.0
    assert sample_product.calculate_price(Discount({(1,): 0.5})) == 10.0
    assert sample_product.get_unit() == 2


def test_product_equality() -> None:
    product1 = Product(1, "Product 1", 10.0)
    product2 = Product(1, "Product 2", 15.0)

    assert product1 == product2


def test_product_inequality() -> None:
    product1 = Product(1, "Product 1", 10.0)
    product3 = Product(3, "Product 3", 20.0)

    assert product1 != product3


def test_product_hashing() -> None:
    product1 = Product(1, "Product 1", 10.0)
    product2 = Product(1, "Product 2", 15.0)

    assert hash(product1) == hash(product2)


def test_product_hash_inequality() -> None:
    product1 = Product(1, "Product 1", 10.0)
    product3 = Product(3, "Product 3", 20.0)

    assert hash(product1) != hash(product3)
