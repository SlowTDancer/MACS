import random
from dataclasses import dataclass, field
from typing import Callable, List, Protocol

from constants import (
    CLAWS,
    MAXIMUM_LEG_COUNT,
    MAXIMUM_WING_COUNT,
    MINIMUM_LEG_COUNT,
    MINIMUM_WING_COUNT,
    REQUIRED_LEGS_TO_HOP,
    REQUIRED_LEGS_TO_RUN,
    REQUIRED_LEGS_TO_WALK,
    REQUIRED_WINGS_TO_FLY,
    TEETH_MODIFIERS,
)
from movement import Crawl, Fly, Hop, Movement, Run, Walk
from movement_strategy import GreedyMovementStrategy, IMovementStrategy


class ICreatureBody(Protocol):
    def get_power(self) -> int:
        pass

    def get_health(self) -> int:
        pass

    def get_stamina(self) -> int:
        pass

    def set_power(self, power: int) -> None:
        pass

    def set_stamina(self, stamina: int) -> None:
        pass

    def set_health(self, health: int) -> None:
        pass

    def __str__(self) -> str:
        pass


@dataclass
class CreatureBody:
    _health: int = field(default_factory=int)
    _stamina: int = field(default_factory=int)
    _power: int = field(default_factory=int)

    def get_power(self) -> int:
        return self._power

    def get_health(self) -> int:
        return self._health

    def get_stamina(self) -> int:
        return self._stamina

    def set_power(self, power: int) -> None:
        self._power = power

    def set_stamina(self, stamina: int) -> None:
        self._stamina = stamina

    def set_health(self, health: int) -> None:
        self._health = health

    def __str__(self) -> str:
        return f"health: {self._health}, stamina: {self._stamina}, power: {self._power}"


@dataclass
class CreatureBodyDecorator:
    body: ICreatureBody

    def get_power(self) -> int:
        return self.body.get_power()

    def get_health(self) -> int:
        return self.body.get_health()

    def get_stamina(self) -> int:
        return self.body.get_stamina()

    def set_power(self, power: int) -> None:
        self.body.set_power(power)

    def set_stamina(self, stamina: int) -> None:
        self.body.set_stamina(stamina)

    def set_health(self, health: int) -> None:
        self.body.set_health(health)

    def __str__(self) -> str:
        return self.body.__str__()


@dataclass
class CreatureBodyWithClaws(CreatureBodyDecorator):
    modifier: int

    def get_power(self) -> int:
        return super().get_power() * self.modifier

    def __str__(self) -> str:
        return f"{super().__str__()}, Claw Type: {CLAWS[self.modifier]}"


@dataclass
class CreatureBodyWithTeeth(CreatureBodyDecorator):
    modifier: int

    def get_power(self) -> int:
        return super().get_power() + self.modifier

    def __str__(self) -> str:
        return f"{super().__str__()}, Teeth Sharpness: {self.modifier}"


@dataclass
class CreatureBodyWithLegs(CreatureBodyDecorator):
    modifier: int

    def __str__(self) -> str:
        return f"{super().__str__()}, Legs: {self.modifier}"


@dataclass
class CreatureBodyWithWings(CreatureBodyDecorator):
    modifier: int

    def __str__(self) -> str:
        return f"{super().__str__()}, Wings: {self.modifier}"


class ICreature(Protocol):
    def evolve(self) -> None:
        pass

    def move(self) -> None:
        pass

    def is_dead(self) -> bool:
        pass

    def is_exhausted(self) -> bool:
        pass

    def attack(self, enemy: "ICreature") -> None:
        pass

    def take_damage(self, damage: int) -> None:
        pass

    def __str__(self) -> str:
        pass


class Creature:
    _body: ICreatureBody
    _location: int
    _movement_strategy: IMovementStrategy
    _movement_options: List[Callable[[int], Movement]]

    def __init__(
        self,
        health: int,
        stamina: int,
        power: int,
        location: int,
        movement_strategy: IMovementStrategy = GreedyMovementStrategy(),
    ) -> None:
        self._body = CreatureBody(health, stamina, power)
        self._location = location
        self._movement_strategy = movement_strategy
        self._movement_options = []

    def evolve(self) -> None:
        num_legs = random.randint(MINIMUM_LEG_COUNT, MAXIMUM_LEG_COUNT)
        self._body = CreatureBodyWithLegs(self._body, num_legs)
        self._movement_options.append(Crawl())
        if num_legs >= REQUIRED_LEGS_TO_HOP:
            self._movement_options.append(Hop())

        if num_legs >= REQUIRED_LEGS_TO_WALK:
            self._movement_options.append(Walk())

        if num_legs >= REQUIRED_LEGS_TO_RUN:
            self._movement_options.append(Run())

        num_wings = random.randint(MINIMUM_WING_COUNT, MAXIMUM_WING_COUNT)
        self._body = CreatureBodyWithWings(self._body, num_wings)
        if num_wings >= REQUIRED_WINGS_TO_FLY:
            self._movement_options.append(Fly())

        claw_type = random.choice(list(CLAWS.keys()))
        teeth_sharpness = random.choice(TEETH_MODIFIERS)
        self._body = CreatureBodyWithClaws(self._body, claw_type)
        self._body = CreatureBodyWithTeeth(self._body, teeth_sharpness)

    def move(self) -> None:
        stamina_cost, displacement = self._movement_strategy.move(
            self._body.get_stamina(), self._movement_options
        )
        self._body.set_stamina(self._body.get_stamina() - stamina_cost)
        self._location += displacement

    def is_dead(self) -> bool:
        return self._body.get_health() <= 0

    def is_exhausted(self) -> bool:
        return self._body.get_stamina() <= 0

    def attack(self, enemy: ICreature) -> None:
        enemy.take_damage(self._body.get_power())

    def take_damage(self, damage: int) -> None:
        self._body.set_health(self._body.get_health() - damage)

    def get_location(self) -> int:
        return self._location

    def __str__(self) -> str:
        return f"{self._body.__str__()}, Location: {str(self._location)}\n"
