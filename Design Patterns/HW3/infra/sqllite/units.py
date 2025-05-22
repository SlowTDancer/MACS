from dataclasses import dataclass
from sqlite3 import IntegrityError
from typing import List
from uuid import UUID

from core.errors import DoesNotExistError, ExistsError
from core.units import Unit
from infra.sqllite.database import SqliteDatabase


@dataclass
class UnitSqlite:
    database: SqliteDatabase

    def create(self, unit: Unit) -> None:
        try:
            query = "INSERT INTO units (id, name) VALUES (?, ?)"
            self.database.execute(query, (str(unit.id), unit.name))
        except IntegrityError:
            raise ExistsError

    def fetch(self, unit_id: UUID) -> Unit:
        query = "SELECT id, name FROM units WHERE id = ?"
        result = self.database.fetch_one(query, (str(unit_id),))

        if result:
            return Unit(result[1], UUID(result[0]))
        else:
            raise DoesNotExistError(f"Unit with id<{unit_id}> does not exist.")

    def fetch_all(self) -> List[Unit]:
        query = "SELECT id, name FROM units"
        results = self.database.fetch_all(query)

        return [Unit(row[1], UUID(row[0])) for row in results]
