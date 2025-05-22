from __future__ import annotations

import uvicorn
from dotenv import load_dotenv
from typer import Typer

from constants import HOST, PORT
from runner.setup import init_app

cli = Typer(no_args_is_help=True, add_completion=False)


@cli.command()
def run(host: str = HOST, port: int = PORT) -> None:
    load_dotenv()

    uvicorn.run(host=host, port=port, app=init_app())
