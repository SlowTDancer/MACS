from typing import Protocol

from pos.constants import NUM_SHIFTS
from pos.human_factory import IHumanFactory
from pos.printer import IPrinter
from pos.store import IStore


class IStoreSimulator(Protocol):
    def simulate_one_day(self) -> None:
        pass

    def _do_shift(self) -> None:
        pass

    def _serve_customer(self) -> None:
        pass

    def _check_report(self) -> bool:
        pass

    def _increment_customer_served(self) -> None:
        pass


class StoreSimulator:
    def __init__(
        self, store: IStore, human_factory: IHumanFactory, printer: IPrinter
    ) -> None:
        self._store = store
        self._human_factory = human_factory
        self._printer = printer
        self._customers_served = 0

    def simulate_one_day(self) -> None:
        for _ in range(NUM_SHIFTS):
            self._do_shift()

    def _do_shift(self) -> None:
        while True:
            self._serve_customer()
            self._increment_customer_served()
            if self._check_report():
                self._store.flush_in_memory_data()
                break

    def _serve_customer(self) -> None:
        cashier = self._store.get_cashier()
        customer = self._human_factory.create_customer(self._store.get_products())
        discounts = self._store.get_discounts()

        cashier.open_receipt()
        customer_products = customer.checkout()

        for product in customer_products:
            cashier.add_product_to_receipt(product)

        receipt = cashier.give_receipt()
        customer.see_receipt(receipt)
        self._printer.print_receipt(receipt, discounts)
        payment_type = customer.pay()

        for product in receipt:
            self._store.update_sold_items(product)

        self._store.update_revenue((payment_type, receipt.get_total_price(discounts)))
        cashier.close_receipt()

    def _check_report(self) -> bool:
        if self._customers_served % 20 == 0:
            self._store.get_store_manager().make_x_report(
                self._store.get_curr_sold_items(),
                self._store.get_curr_revenue(),
                self._printer,
            )
        if self._customers_served % 100 == 0:
            return self._store.get_cashier().make_z_report(
                self._store.get_store_manager()
            )
        return False

    def _increment_customer_served(self) -> None:
        self._customers_served += 1
