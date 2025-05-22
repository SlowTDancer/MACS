from dataclasses import dataclass, field
from typing import Iterator, List, Optional, Protocol

from pos.payment_strategy import IPaymentStrategy
from pos.product import IProduct
from pos.receipt import IReceipt


class IShoppingCart(Protocol):
    def add_product(self, product: IProduct) -> None:
        pass

    def __iter__(self) -> Iterator[IProduct]:
        pass


@dataclass
class ShoppingCart:
    _products: List[IProduct] = field(default_factory=list)

    def add_product(self, product: IProduct) -> None:
        self._products.append(product)

    def __iter__(self) -> Iterator[IProduct]:
        return iter(self._products)


class ICustomer(Protocol):
    def checkout(self) -> IShoppingCart:
        pass

    def see_receipt(self, receipt: IReceipt) -> None:
        pass

    def pay(self) -> str:
        pass

    def show_receipt(self) -> IReceipt | None:
        pass


class Customer:
    def __init__(
        self,
        shopping_cart: IShoppingCart,
        payment_strategies: List[str],
        payment_strategy: IPaymentStrategy,
    ) -> None:
        self._shopping_cart = shopping_cart
        self._payment_strategies = payment_strategies
        self._payment_strategy = payment_strategy
        self._receipt: Optional[IReceipt] = None

    def checkout(self) -> IShoppingCart:
        return self._shopping_cart

    def see_receipt(self, receipt: IReceipt) -> None:
        self._receipt = receipt

    def pay(self) -> str:
        payment_strategy = self._payment_strategy.choose_payment_strategy(
            self._payment_strategies
        )
        print("Customer paid with " + payment_strategy)
        return payment_strategy

    def show_receipt(self) -> IReceipt | None:
        return self._receipt
