from creature import Creature, CreatureBody
from movement import Crawl, Fly, Hop
from movement_strategy import CrawlingMovementStrategy, GreedyMovementStrategy
from simulation import Simulator


def test_health() -> None:
    base_health = 100
    body = CreatureBody(base_health, 1, 1)
    assert body.get_health() == base_health
    body.set_health(50)
    assert body.get_health() == 50


def test_stamina() -> None:
    base_stamina = 100
    body = CreatureBody(1, base_stamina, 1)
    assert body.get_stamina() == base_stamina
    body.set_stamina(50)
    assert body.get_stamina() == 50


def test_power() -> None:
    base_power = 100
    body = CreatureBody(1, 1, base_power)
    assert body.get_power() == base_power
    body.set_power(50)
    assert body.get_power() == 50


def test_location() -> None:
    starting_location = 0
    living_thing = Creature(1, 1, 1, starting_location)
    assert starting_location == living_thing.get_location()


def test_crawling_movement_strategy() -> None:
    possible_movement = [Crawl()]
    stamina = 100
    location = 0
    crawling_movement_strategy = CrawlingMovementStrategy()
    for i in range(100):
        stamina_cost, displacement = crawling_movement_strategy.move(
            stamina, possible_movement
        )
        stamina -= stamina_cost
        location += displacement
    assert stamina == 0
    assert location == 100


def test_greedy_movement_strategy() -> None:
    possible_movement = [Crawl(), Hop(), Fly()]
    stamina = 84
    location = 0
    greedy_movement_strategy = GreedyMovementStrategy()
    stamina_cost, displacement = greedy_movement_strategy.move(
        stamina, possible_movement
    )
    stamina -= stamina_cost
    location += displacement

    for i in range(30):
        stamina_cost, displacement = greedy_movement_strategy.move(
            stamina, possible_movement
        )
        stamina -= stamina_cost
        location += displacement

    for i in range(20):
        stamina_cost, displacement = greedy_movement_strategy.move(
            stamina, possible_movement
        )
        stamina -= stamina_cost
        location += displacement

    assert stamina == 0
    assert location == 118


def test_creature_body_str() -> None:
    body = CreatureBody(1, 1, 1)
    assert str(body) == "health: 1, stamina: 1, power: 1"


def test_evolution() -> None:
    body = CreatureBody(1, 1, 1)

    living_thing = Creature(1, 1, 1, 0)
    living_thing.evolve()

    dummy_str = ", Location: 0\n"

    assert len(str(body)) < len(str(living_thing)) - len(dummy_str)


def test_chase_success() -> None:
    prey = Creature(1, 1, 1, 30, CrawlingMovementStrategy())
    predator = Creature(100, 100, 1, 0, CrawlingMovementStrategy())
    simulator = Simulator(prey, predator)
    simulator.evolution_phase()
    chase_str = simulator.chase_phase()
    assert chase_str == ""


def test_chase_failed() -> None:
    prey = Creature(1, 100, 1, 30, CrawlingMovementStrategy())
    predator = Creature(100, 20, 1, 0, CrawlingMovementStrategy())
    simulator = Simulator(prey, predator)
    simulator.evolution_phase()
    chase_str = simulator.chase_phase()
    assert chase_str == "Prey ran into infinity\n"


def test_predator_wins_fight() -> None:
    prey = Creature(1, 100, 1, 0, CrawlingMovementStrategy())
    predator = Creature(100, 20, 30, 0, CrawlingMovementStrategy())
    simulator = Simulator(prey, predator)
    simulator.evolution_phase()
    fight_result = simulator.fighting_phase()
    assert fight_result == "Some R-rated things have happened\n"


def test_prey_wins_fight() -> None:
    prey = Creature(100, 100, 30, 0, CrawlingMovementStrategy())
    predator = Creature(1, 20, 1, 0, CrawlingMovementStrategy())
    simulator = Simulator(prey, predator)
    simulator.evolution_phase()
    fight_result = simulator.fighting_phase()
    assert fight_result == "Prey ran into infinity\n"
