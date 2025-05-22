from typing import Dict, List, Protocol, Tuple

from pos.cashier import ICashier
from pos.constants import PAYMENT_METHODS
from pos.discount import IDiscount
from pos.product import IProduct
from pos.store_database import IStoreDatabase
from pos.store_manager import IStoreManager


class IStore(Protocol):
    def get_store_manager(self) -> IStoreManager:
        pass

    def get_cashier(self) -> ICashier:
        pass

    def get_products(self) -> List[IProduct]:
        pass

    def get_discounts(self) -> IDiscount:
        pass

    def update_sold_items(self, product: IProduct) -> None:
        pass

    def get_sold_items(self) -> Dict[IProduct, int]:
        pass

    def update_revenue(self, payment: Tuple[str, float]) -> None:
        pass

    def get_revenue(self) -> Dict[str, float]:
        pass

    def get_curr_sold_items(self) -> Dict[IProduct, int]:
        pass

    def get_curr_revenue(self) -> Dict[str, float]:
        pass

    def flush_in_memory_data(self) -> None:
        pass


class Store:
    def __init__(
        self,
        store_manager: IStoreManager,
        cashier: ICashier,
        store_database: IStoreDatabase,
    ) -> None:
        self._store_manager = store_manager
        self._cashier = cashier
        self._store_database = store_database
        self._sold_items: Dict[IProduct, int] = {}
        self._revenue: Dict[str, float] = {}
        for payment_method in PAYMENT_METHODS:
            self._revenue[payment_method] = 0.0

    def get_store_manager(self) -> IStoreManager:
        return self._store_manager

    def get_cashier(self) -> ICashier:
        return self._cashier

    def get_products(self) -> List[IProduct]:
        return self._store_database.get_all_products()

    def get_discounts(self) -> IDiscount:
        return self._store_database.get_all_discounts()

    def update_sold_items(self, product: IProduct) -> None:
        if product in self._sold_items:
            self._sold_items[product] += 1
        else:
            self._sold_items[product] = 1

    def get_sold_items(self) -> Dict[IProduct, int]:
        return self._store_database.get_sold_items()

    def update_revenue(self, payment: Tuple[str, float]) -> None:
        payment_type, revenue = payment
        self._revenue[payment_type] += revenue

    def get_revenue(self) -> Dict[str, float]:
        return self._store_database.get_revenue()

    def get_curr_sold_items(self) -> Dict[IProduct, int]:
        return self._sold_items

    def get_curr_revenue(self) -> Dict[str, float]:
        return self._revenue

    def flush_in_memory_data(self) -> None:
        self._store_database.flush_in_memory_data(self._sold_items, self._revenue)
        self._sold_items = {}
        for payment_type, _ in self._revenue.items():
            self._revenue[payment_type] = 0.0
