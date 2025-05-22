from dataclasses import dataclass
from typing import Dict, Protocol, Tuple


class IDiscount(Protocol):
    def get_product_discount(self, product_id: int) -> float:
        pass


@dataclass
class Discount:
    _discounts_list: Dict[Tuple[int, ...], float]

    def get_product_discount(self, product_id: int) -> float:
        res = 1.0
        for p_ids, discount in self._discounts_list.items():
            if product_id in p_ids:
                res *= 1 - discount

        return round(1 - res, 2)
