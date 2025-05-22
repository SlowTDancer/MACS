from __future__ import annotations

from dataclasses import dataclass, field
from typing import Protocol
from uuid import UUID, uuid4


@dataclass
class UnitService:
    units: UnitRepository

    def create_unit(self, name: str) -> None:
        new_unit = Unit(name)
        self.units.create(new_unit)

    def get_unit(self, unit_id: UUID) -> Unit:
        return self.units.fetch(unit_id)

    def get_all_units(self) -> list[Unit]:
        return self.units.fetch_all()


class UnitRepository(Protocol):
    def create(self, unit: Unit) -> None:
        pass

    def fetch(self, unit_id: UUID) -> Unit:
        pass

    def fetch_all(self) -> list[Unit]:
        pass


@dataclass
class Unit:
    name: str
    id: UUID = field(default_factory=uuid4)
