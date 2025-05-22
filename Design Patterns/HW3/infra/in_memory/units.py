from dataclasses import dataclass, field
from typing import Dict
from uuid import UUID

from core.errors import DoesNotExistError, ExistsError
from core.units import Unit


@dataclass
class UnitInMemory:
    units: Dict[UUID, Unit] = field(default_factory=dict)

    def create(self, unit: Unit) -> None:
        existing_unit = next(
            (u for u in self.units.values() if u.name == unit.name), None
        )
        if existing_unit:
            raise ExistsError
        self.units[unit.id] = unit

    def fetch(self, unit_id: UUID) -> Unit:
        try:
            return self.units[unit_id]
        except KeyError:
            raise DoesNotExistError(unit_id)

    def fetch_all(self) -> list[Unit]:
        return list(self.units.values())
