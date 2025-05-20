from dataclasses import dataclass, field


@dataclass
class SymbolTable:
    symbols: dict[str, tuple[str, str, int]] = field(default_factory=dict)
    static_counter: int = field(default_factory=int)
    this_counter: int = field(default_factory=int)
    local_counter: int = field(default_factory=int)
    argument_counter: int = field(default_factory=int)

    def reset(self) -> None:
        self.symbols.clear()
        self.static_counter = 0
        self.this_counter = 0
        self.local_counter = 0
        self.argument_counter = 0

    def define(self, name: str | int, sym_type: str | int, kind: str | int) -> None:
        counter = 0
        kind = str(kind)
        if kind == "static":
            counter = self.static_counter
            self.static_counter += 1
        elif kind == "this":
            counter = self.this_counter
            self.this_counter += 1
        elif kind == "local":
            counter = self.local_counter
            self.local_counter += 1
        elif kind == "argument":
            counter = self.argument_counter
            self.argument_counter += 1

        entry = (str(sym_type), kind, counter)
        self.symbols[str(name)] = entry

    def var_count(self, kind: str) -> int:
        if kind == "static":
            return self.static_counter
        elif kind == "this":
            return self.this_counter
        elif kind == "local":
            return self.local_counter
        elif kind == "argument":
            return self.argument_counter
        return -1

    def type_of(self, name: str | int) -> str:
        return self.symbols[str(name)][0]

    def kind_of(self, name: str | int) -> str:
        return self.symbols[str(name)][1]

    def index_of(self, name: str | int) -> int:
        return self.symbols[str(name)][2]
