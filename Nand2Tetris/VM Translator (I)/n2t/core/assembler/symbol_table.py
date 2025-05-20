from dataclasses import dataclass, field

from n2t.core.assembler.constants import PREDEFINED_SYMBOLS


@dataclass
class SymbolTable:
    __symbols: dict[str, int] = field(default_factory=dict[str, int])

    def add_entry(self, symbol: str, address: int) -> None:
        self.__symbols[symbol] = address

    def contains(self, symbol: str) -> bool:
        return symbol in self.__symbols

    def get_address(self, symbol: str) -> int:
        return self.__symbols[symbol]

    def fill_predefined_symbols(self) -> None:
        for symbol, address in PREDEFINED_SYMBOLS.items():
            self.add_entry(symbol, address)
