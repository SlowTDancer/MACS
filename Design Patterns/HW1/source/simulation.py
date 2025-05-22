import random
from dataclasses import dataclass

from constants import (
    MAX_HEALTH,
    MAX_POWER,
    MIN_HEALTH,
    MIN_POWER,
    PREDATOR_MAX_STAMINA,
    PREDATOR_MIN_STAMINA,
    PREY_MAX_STAMINA,
    PREY_MIN_STAMINA,
)
from creature import Creature


@dataclass
class Simulator:
    _prey: Creature
    _predator: Creature

    def evolution_phase(self) -> tuple[str, str]:
        self._prey.evolve()
        self._predator.evolve()

        return "Prey: " + str(self._prey), "Predator: " + str(self._predator)

    def chase_phase(self) -> str:
        while True:
            if self._predator.is_exhausted():
                break
            self._predator.move()
            self._prey.move()
            if self._predator.get_location() >= self._prey.get_location():
                return ""
        return "Prey ran into infinity\n"

    def fighting_phase(self) -> str:
        while True:
            if self._predator.is_dead():
                break
            self._predator.attack(self._prey)
            if self._prey.is_dead():
                return "Some R-rated things have happened\n"
            self._prey.attack(self._predator)
        return "Prey ran into infinity\n"


def create_creature(
    starting_location: int,
    min_stamina: int,
    max_stamina: int,
) -> Creature:
    base_health = random.randint(MIN_HEALTH, MAX_HEALTH)
    base_stamina = random.randint(min_stamina, max_stamina)
    base_power = random.randint(MIN_POWER, MAX_POWER)
    return Creature(base_health, base_stamina, base_power, starting_location)


def main() -> None:
    for i in range(100):
        prey = create_creature(
            random.randint(0, 1000), PREY_MIN_STAMINA, PREY_MAX_STAMINA
        )
        predator = create_creature(0, PREDATOR_MIN_STAMINA, PREDATOR_MAX_STAMINA)
        simulator = Simulator(prey, predator)
        prey_str, predator_str = simulator.evolution_phase()
        print(prey_str)
        print(predator_str)
        chase_result = simulator.chase_phase()
        if chase_result == "":
            fight_result = simulator.fighting_phase()
            print(fight_result)
        else:
            print(chase_result)
        print("-" * 130)


if __name__ == "__main__":
    main()
