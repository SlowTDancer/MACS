import sqlite3
from sqlite3 import IntegrityError
from typing import Any, List, Tuple


class SqliteDatabase:
    def __init__(self, database_path: str) -> None:
        self.database_path = database_path

    def execute(self, query: str, params: Tuple[Any, ...] = ()) -> None:
        connection = sqlite3.connect(self.database_path, check_same_thread=False)
        cursor = connection.cursor()
        try:
            cursor.execute(query, params)
            connection.commit()
            connection.close()
        except IntegrityError:
            connection.close()
            raise IntegrityError

    def fetch_one(self, query: str, params: Tuple[Any, ...] = ()) -> Tuple[Any, ...]:
        connection = sqlite3.connect(self.database_path, check_same_thread=False)
        cursor = connection.cursor()
        cursor.execute(query, params)
        result: Tuple[Any, ...] = cursor.fetchone()
        connection.close()
        return result

    def fetch_all(
        self, query: str, params: Tuple[Any, ...] = ()
    ) -> List[Tuple[Any, ...]]:
        connection = sqlite3.connect(self.database_path, check_same_thread=False)
        cursor = connection.cursor()
        cursor.execute(query, params)
        results = cursor.fetchall()
        connection.close()
        return results

    def clear(self) -> None:
        connection = sqlite3.connect(self.database_path, check_same_thread=False)
        cursor = connection.cursor()
        cursor.execute("DELETE from units", ())
        cursor.execute("DELETE from sold_products", ())
        cursor.execute("DELETE from receipts", ())
        cursor.execute("DELETE from products", ())
        connection.commit()
        connection.close()
