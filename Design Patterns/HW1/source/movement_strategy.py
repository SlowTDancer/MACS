from typing import Callable, List, Protocol

from movement import Crawl, Movement


class IMovementStrategy(Protocol):
    def move(
        self, stamina: int, movement_options: List[Callable[[int], Movement]]
    ) -> tuple[int, int]:
        pass


class CrawlingMovementStrategy:
    def move(
        self, stamina: int, movement_options: List[Callable[[int], Movement]]
    ) -> tuple[int, int]:
        movement = Crawl()

        return (
            stamina - movement(stamina).calculate_stamina(stamina),
            movement(stamina).calculate_speed(),
        )


class GreedyMovementStrategy:
    def move(
        self, stamina: int, movement_options: List[Callable[[int], Movement]]
    ) -> tuple[int, int]:
        possible_movements = [movement(stamina) for movement in movement_options]

        doable_movements = [
            movement
            for movement in possible_movements
            if movement.calculate_stamina(stamina) != stamina
        ]

        if doable_movements:
            best_movement = max(
                doable_movements, key=lambda movement: movement.calculate_speed()
            )

            stamina -= best_movement.calculate_stamina(stamina)
            speed = best_movement.calculate_speed()

            return stamina, speed
        else:
            return stamina, 0
