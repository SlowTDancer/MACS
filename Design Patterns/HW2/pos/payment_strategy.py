import random
from typing import List, Protocol


class IPaymentStrategy(Protocol):
    def choose_payment_strategy(self, payment_strategies: List[str]) -> str:
        pass


class RandomPaymentStrategy:
    def choose_payment_strategy(self, payment_strategies: List[str]) -> str:
        return random.choice(payment_strategies)
