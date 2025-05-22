import os

from fastapi import FastAPI

from constants import DATABASE_PATH
from infra.fastapi.products import product_api
from infra.fastapi.receipts import receipt_api
from infra.fastapi.sales import sales_api
from infra.fastapi.units import unit_api
from infra.in_memory.products import ProductInMemory
from infra.in_memory.receipts import ReceiptInMemory
from infra.in_memory.units import UnitInMemory
from infra.sqllite.database import SqliteDatabase
from infra.sqllite.products import ProductSqlite
from infra.sqllite.receipts import ReceiptSqlite
from infra.sqllite.units import UnitSqlite


def init_app() -> FastAPI:
    app = FastAPI()
    app.include_router(unit_api)
    app.include_router(product_api)
    app.include_router(receipt_api)
    app.include_router(sales_api)
    os.environ["REPOSITORY_KIND"] = "sqlite"

    if os.getenv("REPOSITORY_KIND", "memory") == "sqlite":
        sqlite_database = SqliteDatabase(DATABASE_PATH)
        sqlite_database.clear()
        app.state.units = UnitSqlite(sqlite_database)
        app.state.products = ProductSqlite(sqlite_database)
        app.state.receipts = ReceiptSqlite(sqlite_database)
        sqlite_database.clear()
    else:
        app.state.units = UnitInMemory()
        app.state.products = ProductInMemory()
        app.state.receipts = ReceiptInMemory()
    return app
