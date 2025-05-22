from pos.cashier import Cashier
from pos.product import Product
from pos.store import Store
from pos.store_database import StoreDatabase
from pos.store_manager import StoreManager


def test_store_get_store_manager() -> None:
    store_manager = StoreManager()
    store = Store(store_manager, Cashier(), StoreDatabase())

    assert store.get_store_manager() == store_manager


def test_store_get_cashier() -> None:
    cashier = Cashier()
    store = Store(StoreManager(), cashier, StoreDatabase())

    assert store.get_cashier() == cashier


def test_store_get_products() -> None:
    store_database = StoreDatabase()
    store = Store(StoreManager(), Cashier(), store_database)

    assert len(store.get_products()) == 10
    store_database.close_connection()


def test_store_update_sold_items() -> None:
    product = Product(1, "1", 1, 1)
    store = Store(StoreManager(), Cashier(), StoreDatabase())

    store.update_sold_items(product)
    assert product in store._sold_items

    assert store._sold_items[product] == 1


def test_store_get_sold_items() -> None:
    product = Product(1, "1", 1, 1)
    store_database = StoreDatabase()
    store_database.clear()
    store = Store(StoreManager(), Cashier(), store_database)

    store.update_sold_items(product)
    store.flush_in_memory_data()

    assert len(store.get_sold_items()) == 1

    store_database.clear()
    store_database.close_connection()


def test_store_update_revenue() -> None:
    store_database = StoreDatabase()
    store = Store(StoreManager(), Cashier(), store_database)

    assert store._revenue == {"Card": 0.0, "Cash": 0.0}
    store.update_revenue(("Card", 20.0))
    assert store._revenue == {"Card": 20.0, "Cash": 0.0}
    store.flush_in_memory_data()

    store_database.clear()
    store_database.close_connection()


def test_store_get_revenue() -> None:
    store_database = StoreDatabase()
    store = Store(StoreManager(), Cashier(), store_database)

    store.update_revenue(("Card", 20.0))
    store.flush_in_memory_data()

    assert store.get_revenue() == {"Card": 20.0, "Cash": 0.0}

    store_database.clear()
    store_database.close_connection()
