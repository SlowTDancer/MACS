from dataclasses import dataclass, field
from typing import Iterator, List, Protocol

from pos.discount import IDiscount
from pos.product import IProduct


class IReceipt(Protocol):
    def get_total_price(self, discounts: IDiscount) -> float:
        pass

    def __iter__(self) -> Iterator[IProduct]:
        pass


class IReceiptBuilder(Protocol):
    def add_entry(self, product: IProduct) -> None:
        pass

    def get_receipt(self) -> IReceipt:
        pass

    def clear(self) -> None:
        pass


@dataclass
class ReceiptBuilder:
    _products: List[IProduct] = field(default_factory=list)

    def add_entry(self, product: IProduct) -> None:
        self._products.append(product)

    def get_receipt(self) -> IReceipt:
        return Receipt(self._products)

    def clear(self) -> None:
        self._products.clear()


@dataclass
class Receipt:
    _products: List[IProduct] = field(default_factory=list[IProduct])

    def get_total_price(self, discounts: IDiscount) -> float:
        total_price = sum(
            product.calculate_price(discounts) for product in self._products
        )
        return round(total_price, 2)

    def __iter__(self) -> Iterator[IProduct]:
        return iter(self._products)
