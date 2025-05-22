import random
from typing import List, Protocol

from pos.cashier import Cashier, ICashier
from pos.constants import PAYMENT_METHODS
from pos.customer import Customer, ICustomer, ShoppingCart
from pos.payment_strategy import RandomPaymentStrategy
from pos.product import IProduct
from pos.store_manager import IStoreManager, StoreManager


class IHumanFactory(Protocol):
    def create_store_manager(self) -> IStoreManager:
        pass

    def create_cashier(self) -> ICashier:
        pass

    def create_customer(self, product_list: List[IProduct]) -> ICustomer:
        pass


class HumanFactory:
    def create_store_manager(self) -> IStoreManager:
        store_manager = StoreManager()
        return store_manager

    def create_cashier(self) -> ICashier:
        cashier = Cashier()
        return cashier

    def create_customer(self, product_list: List[IProduct]) -> ICustomer:
        customers_products = random.sample(
            product_list, random.randint(1, len(product_list))
        )
        shopping_cart = ShoppingCart(customers_products)
        customer = Customer(shopping_cart, PAYMENT_METHODS, RandomPaymentStrategy())
        return customer
