from dataclasses import dataclass
from typing import Protocol


class IMovement(Protocol):
    def calculate_stamina(self, old_stamina: int) -> int:
        pass

    def calculate_speed(self) -> int:
        pass


@dataclass
class Movement:
    stamina: int = 0
    speed: int = 0

    def calculate_stamina(self, old_stamina: int) -> int:
        return old_stamina - self.stamina

    def calculate_speed(self) -> int:
        return self.speed


class Crawl:
    def __call__(self, stamina: int) -> Movement:
        if stamina > 0:
            return Movement(stamina=1, speed=1)
        return Movement()


class Hop:
    def __call__(self, stamina: int) -> Movement:
        if stamina > 20:
            return Movement(stamina=2, speed=3)
        return Movement()


class Walk:
    def __call__(self, stamina: int) -> Movement:
        if stamina > 40:
            return Movement(stamina=2, speed=4)
        return Movement()


class Run:
    def __call__(self, stamina: int) -> Movement:
        if stamina > 60:
            return Movement(stamina=4, speed=6)
        return Movement()


class Fly:
    def __call__(self, energy: int) -> Movement:
        if energy > 80:
            return Movement(stamina=4, speed=8)
        return Movement()
