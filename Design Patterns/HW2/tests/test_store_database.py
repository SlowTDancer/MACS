from typing import Dict

from pos.constants import DATABASE_PATH
from pos.product import IProduct, Product
from pos.store_database import StoreDatabase


def test_utility() -> None:
    store_database = StoreDatabase(DATABASE_PATH)
    store_database.close_connection()


def test_get_product() -> None:
    store_database = StoreDatabase(DATABASE_PATH)

    product = store_database.get_product(1)
    assert product == Product(1, "Milk", 2.99, 1)

    store_database.clear()
    store_database.close_connection()


def test_get_all_products() -> None:
    store_database = StoreDatabase(DATABASE_PATH)

    products = store_database.get_all_products()
    assert 10 == len(products)

    store_database.close_connection()


def test_flush_in_memory_data() -> None:
    store_database = StoreDatabase(DATABASE_PATH)
    store_database.clear()

    sold_items: Dict[IProduct, int] = {
        Product(1, "Milk", 2.99, 1): 5,
        Product(10, "Water Pack", 0.49, 12): 3,
    }
    revenue = {"Cash": 100.0, "Card": 50.0}

    store_database.flush_in_memory_data(sold_items, revenue)

    sold_items_in_base = store_database.get_sold_items()
    revenue_in_base = store_database.get_revenue()

    assert sold_items == sold_items_in_base
    assert revenue_in_base == revenue

    store_database.clear()
    store_database.close_connection()


def test_clear() -> None:
    store_database = StoreDatabase(DATABASE_PATH)
    store_database.clear()

    sold_items: Dict[IProduct, int] = {
        Product(1, "Milk", 2.99, 1): 5,
        Product(10, "Water Pack", 0.49, 12): 3,
    }
    revenue = {"Cash": 100.0, "Card": 50.0}

    store_database.flush_in_memory_data(sold_items, revenue)

    store_database.clear()

    sold_items_in_base = store_database.get_sold_items()
    assert len(sold_items_in_base) == 0

    revenue_in_base = store_database.get_revenue()
    assert revenue_in_base["Cash"] == 0.0 and revenue_in_base["Card"] == 0.0

    store_database.close_connection()
