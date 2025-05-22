import random
from typing import Dict, Protocol

from pos.printer import IPrinter
from pos.product import IProduct


class IStoreManager(Protocol):
    def make_x_report(
        self,
        sold_items: Dict[IProduct, int],
        revenue: Dict[str, float],
        printer: IPrinter,
    ) -> None:
        pass

    def answer_question(self) -> bool:
        pass


class StoreManager:
    def make_x_report(
        self,
        sold_items: Dict[IProduct, int],
        revenue: Dict[str, float],
        printer: IPrinter,
    ) -> None:
        printer.print_report(sold_items)
        printer.print_revenue(revenue)

    def answer_question(self) -> bool:
        return random.choice([True, False])
