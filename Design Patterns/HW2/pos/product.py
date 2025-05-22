from dataclasses import dataclass
from typing import Protocol

from pos.discount import IDiscount


class IProduct(Protocol):
    def get_id(self) -> int:
        pass

    def get_name(self) -> str:
        pass

    def get_unit(self) -> int:
        pass

    def get_price(self, discounts: IDiscount) -> float:
        pass

    def get_real_price(self) -> float:
        pass

    def calculate_price(self, discounts: IDiscount) -> float:
        pass

    def __eq__(self, other: object) -> bool:
        pass

    def __hash__(self) -> int:
        pass


@dataclass
class Product:
    _id: int
    _name: str
    _price: float
    _unit: int = 1

    def get_id(self) -> int:
        return self._id

    def get_name(self) -> str:
        return self._name

    def get_unit(self) -> int:
        return self._unit

    def get_price(self, discounts: IDiscount) -> float:
        return round(self._price * (1.0 - discounts.get_product_discount(self._id)), 2)

    def get_real_price(self) -> float:
        return self._price

    def calculate_price(self, discounts: IDiscount) -> float:
        return round(self.get_price(discounts) * self._unit, 2)

    def __eq__(self, other: object) -> bool:
        if not isinstance(other, Product):
            return NotImplemented
        return other.get_id() == self.get_id()

    def __hash__(self) -> int:
        return hash(self._id)
