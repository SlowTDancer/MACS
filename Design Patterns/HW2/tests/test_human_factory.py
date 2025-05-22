from typing import List

from pos.cashier import Cashier
from pos.customer import Customer
from pos.human_factory import HumanFactory
from pos.product import IProduct, Product
from pos.store_manager import StoreManager


def test_human_factory_create_store_manager() -> None:
    human_factory = HumanFactory()
    store_manager = human_factory.create_store_manager()
    assert isinstance(store_manager, StoreManager)


def test_human_factory_create_cashier() -> None:
    human_factory = HumanFactory()
    cashier = human_factory.create_cashier()
    assert isinstance(cashier, Cashier)


def test_human_factory_create_customer() -> None:
    human_factory = HumanFactory()
    product_list: List[IProduct] = [Product(1, "1", 1, 1)]
    customer = human_factory.create_customer(product_list)
    assert isinstance(customer, Customer)
