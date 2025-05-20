from __future__ import annotations

from dataclasses import dataclass
from typing import Iterable

from n2t.core.assembler.code import Code
from n2t.core.assembler.parser import Parser
from n2t.core.assembler.symbol_table import SymbolTable


@dataclass
class Assembler:
    @classmethod
    def create(cls) -> Assembler:
        return cls()

    def assemble(self, assembly: Iterable[str]) -> Iterable[str]:
        binary_code = []
        curr_addr, addr = 0, 0
        parser, code, symbol_table = Parser(assembly), Code(), SymbolTable()
        symbol_table.fill_predefined_symbols()
        parser.strip_lines()

        while parser.has_more_lines():
            instruction_type = parser.instruction_type()
            if instruction_type == "L_INSTRUCTION":
                symbol = parser.symbol()
                if not symbol_table.contains(symbol):
                    symbol_table.add_entry(symbol, curr_addr)
            else:
                curr_addr += 1
            parser.advance()

        parser.reset()
        curr_addr = 16
        while parser.has_more_lines():
            instruction_type = parser.instruction_type()
            if instruction_type == "A_INSTRUCTION":
                symbol = parser.symbol()
                if symbol.isdigit():
                    addr = int(symbol)
                elif symbol_table.contains(symbol):
                    addr = symbol_table.get_address(symbol)
                else:
                    addr = curr_addr
                    symbol_table.add_entry(symbol, addr)
                    curr_addr += 1
                binary_address = format(addr, "016b")
                binary_code.append(binary_address)
            elif instruction_type == "C_INSTRUCTION":
                dest, comp, jump = parser.dest(), parser.comp(), parser.jump()
                c_instruction = (
                    "111" + code.comp(comp) + code.dest(dest) + code.jump(jump)
                )
                binary_code.append(c_instruction)
            parser.advance()
        return binary_code
