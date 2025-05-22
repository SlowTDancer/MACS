from typing import Protocol

from pos.product import IProduct
from pos.receipt import IReceipt, IReceiptBuilder, ReceiptBuilder
from pos.store_manager import IStoreManager


class ICashier(Protocol):
    def open_receipt(self) -> None:
        pass

    def add_product_to_receipt(self, product: IProduct) -> None:
        pass

    def give_receipt(self) -> IReceipt:
        pass

    def close_receipt(self) -> None:
        pass

    def make_z_report(self, store_manager: IStoreManager) -> bool:
        pass


class Cashier:
    _receipt_builder: IReceiptBuilder

    def open_receipt(self) -> None:
        self._receipt_builder = ReceiptBuilder()

    def add_product_to_receipt(self, product: IProduct) -> None:
        self._receipt_builder.add_entry(product)

    def give_receipt(self) -> IReceipt:
        return self._receipt_builder.get_receipt()

    def close_receipt(self) -> None:
        self._receipt_builder.clear()

    def make_z_report(self, store_manager: IStoreManager) -> bool:
        return store_manager.answer_question()
