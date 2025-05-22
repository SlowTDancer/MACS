import typer

from pos.cashier import Cashier
from pos.human_factory import HumanFactory
from pos.printer import Printer
from pos.store import Store
from pos.store_database import StoreDatabase
from pos.store_manager import StoreManager
from pos.store_simulator import StoreSimulator

cli = typer.Typer()


@cli.command("list")
def list_information() -> None:
    store_database = StoreDatabase("store.db")
    printer = Printer()
    products = store_database.get_all_products()
    discounts = store_database.get_all_discounts()
    printer.print_shop_list(products, discounts)


@cli.command("simulate")
def simulate_command() -> None:
    store_manager = StoreManager()
    cashier = Cashier()
    store_database = StoreDatabase("store.db")
    store = Store(store_manager, cashier, store_database)
    human_factory = HumanFactory()
    printer = Printer()
    store_simulator = StoreSimulator(store, human_factory, printer)
    store_simulator.simulate_one_day()


@cli.command("report")
def get_report_command() -> None:
    printer = Printer()
    store_database = StoreDatabase("store.db")
    sold_items = store_database.get_sold_items()
    revenue = store_database.get_revenue()
    printer.print_report(sold_items)
    printer.print_revenue(revenue)
