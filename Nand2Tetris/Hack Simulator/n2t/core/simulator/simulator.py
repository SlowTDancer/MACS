from dataclasses import dataclass, field
from typing import Callable

from n2t.core.simulator.parser import Parser


@dataclass
class Simulator:
    words: list[str] = field(default_factory=list[str])
    cycles: int = field(default_factory=int)
    parser: Parser = field(default_factory=Parser)
    ram: dict[int, int] = field(default_factory=dict[int, int])
    regs: dict[str, int] = field(default_factory=dict[str, int])
    pc: int = field(default_factory=int)
    cycles_done: int = field(default_factory=int)
    dest: str = field(default_factory=str)
    comp: str = field(default_factory=str)
    jump: str = field(default_factory=str)

    def simulate(self) -> dict[int, int]:
        while self.pc < len(self.words) and self.cycles_done < self.cycles:
            instruction = self.words[self.pc]
            if self.parser.instruction_type(instruction) == "A_INSTRUCTION":
                self.regs["A"] = int(self.parser.symbol(instruction))
                self.pc += 1
            else:
                self.handle_c_instruction()
            self.cycles_done += 1
        return self.ram

    def handle_c_instruction(self) -> None:
        instruction = self.words[self.pc]
        self.dest, self.comp, self.jump = (
            self.parser.dest(instruction),
            self.parser.comp(instruction),
            self.parser.jump(instruction),
        )

        comp = self.eval_comp()
        self.do_dest(comp)
        self.do_jump(comp)

    def do_dest(self, comp: int) -> None:
        DEST_FUNCTIONS[self.dest](self, comp)

    def dest_m(self, comp: int) -> None:
        self.ram[self.regs["A"]] = comp

    def dest_d(self, comp: int) -> None:
        self.regs["D"] = comp

    def dest_md(self, comp: int) -> None:
        self.dest_m(comp)
        self.dest_d(comp)

    def dest_dm(self, comp: int) -> None:
        self.dest_d(comp)
        self.dest_m(comp)

    def dest_a(self, comp: int) -> None:
        self.regs["A"] = comp

    def dest_am(self, comp: int) -> None:
        self.dest_m(comp)
        self.dest_a(comp)

    def dest_ad(self, comp: int) -> None:
        self.dest_d(comp)
        self.dest_a(comp)

    def dest_adm(self, comp: int) -> None:
        self.dest_d(comp)
        self.dest_m(comp)
        self.dest_a(comp)

    def eval_comp(self) -> int:
        return COMP_FUNCTIONS[self.comp](self)

    def comp_constant(self) -> int:
        return int(self.comp)

    def comp_reg(self) -> int:
        return self.regs[self.comp]

    def comp_bitwise_not(self) -> int:
        return ~self.regs[self.comp[1]]

    def comp_negative(self) -> int:
        return -self.regs[self.comp[1]]

    def comp_increment(self) -> int:
        return self.my_plus(self.regs[self.comp[0]], 1)

    def comp_decrement(self) -> int:
        return self.my_plus(self.regs[self.comp[0]], -1)

    def comp_d_plus_a(self) -> int:
        return self.my_plus(self.regs["A"], self.regs["D"])

    def comp_d_minus_a(self) -> int:
        return self.my_plus(-self.regs["A"], self.regs["D"])

    def comp_a_minus_d(self) -> int:
        return self.my_plus(self.regs["A"], -self.regs["D"])

    def comp_bitwise_and(self) -> int:
        return self.regs["A"] & self.regs["D"]

    def comp_bitwise_or(self) -> int:
        return self.regs["A"] | self.regs["D"]

    def comp_read_from_memory(self) -> int:
        return self.ram[self.regs["A"]] if self.regs["A"] in self.ram else 0

    def comp_read_from_memory_not(self) -> int:
        return ~self.comp_read_from_memory()

    def comp_read_from_memory_negative(self) -> int:
        return -self.comp_read_from_memory()

    def comp_read_from_memory_increment(self) -> int:
        return self.my_plus(self.comp_read_from_memory(), 1)

    def comp_read_from_memory_decrement(self) -> int:
        return self.my_plus(self.comp_read_from_memory(), -1)

    def comp_read_from_memory_plus_d(self) -> int:
        return self.my_plus(self.comp_read_from_memory(), self.regs["D"])

    def comp_d_minus_read_from_memory(self) -> int:
        return self.my_plus(-self.comp_read_from_memory(), self.regs["D"])

    def comp_read_from_memory_minus_d(self) -> int:
        return self.my_plus(self.comp_read_from_memory(), -self.regs["D"])

    def comp_read_from_memory_and(self) -> int:
        return self.regs["D"] & self.comp_read_from_memory()

    def comp_read_from_memory_or(self) -> int:
        return self.regs["D"] | self.comp_read_from_memory()

    def dummy(self, comp: int) -> None:
        return

    def do_jump(self, comp: int) -> None:
        if self.jump == "JMP":
            self.jump_unconditional()
            return
        JUMP_FUNCTIONS[self.jump](self, comp)
        if self.jump == "":
            self.pc += 1

    def jump_unconditional(self) -> None:
        self.pc = self.regs["A"]

    def jump_gt(self, comp: int) -> None:
        if comp > 0:
            self.jump_unconditional()
        else:
            self.pc += 1

    def jump_eq(self, comp: int) -> None:
        if comp == 0:
            self.jump_unconditional()
        else:
            self.pc += 1

    def jump_ge(self, comp: int) -> None:
        if comp >= 0:
            self.jump_unconditional()
        else:
            self.pc += 1

    def jump_lt(self, comp: int) -> None:
        if comp < 0:
            self.jump_unconditional()
        else:
            self.pc += 1

    def jump_ne(self, comp: int) -> None:
        if comp != 0:
            self.jump_unconditional()
        else:
            self.pc += 1

    def jump_le(self, comp: int) -> None:
        if comp <= 0:
            self.jump_unconditional()
        else:
            self.pc += 1

    @staticmethod
    def my_plus(a: int, b: int) -> int:
        res = a + b
        res = res & 0xFFFF
        if res & 0x8000:
            res -= 0x10000
        return res


DEST_FUNCTIONS = {
    "": Simulator.dummy,
    "M": Simulator.dest_m,
    "D": Simulator.dest_d,
    "MD": Simulator.dest_md,
    "DM": Simulator.dest_dm,
    "A": Simulator.dest_a,
    "AM": Simulator.dest_am,
    "AD": Simulator.dest_ad,
    "ADM": Simulator.dest_adm,
}

COMP_FUNCTIONS: dict[str, Callable[[Simulator], int]] = {
    "0": Simulator.comp_constant,
    "1": Simulator.comp_constant,
    "-1": Simulator.comp_constant,
    "D": Simulator.comp_reg,
    "A": Simulator.comp_reg,
    "!D": Simulator.comp_bitwise_not,
    "!A": Simulator.comp_bitwise_not,
    "-D": Simulator.comp_negative,
    "-A": Simulator.comp_negative,
    "D+1": Simulator.comp_increment,
    "A+1": Simulator.comp_increment,
    "D-1": Simulator.comp_decrement,
    "A-1": Simulator.comp_decrement,
    "D+A": Simulator.comp_d_plus_a,
    "D-A": Simulator.comp_d_minus_a,
    "A-D": Simulator.comp_a_minus_d,
    "D&A": Simulator.comp_bitwise_and,
    "D|A": Simulator.comp_bitwise_or,
    "M": Simulator.comp_read_from_memory,
    "!M": Simulator.comp_read_from_memory_not,
    "-M": Simulator.comp_read_from_memory_negative,
    "M+1": Simulator.comp_read_from_memory_increment,
    "M-1": Simulator.comp_read_from_memory_decrement,
    "D+M": Simulator.comp_read_from_memory_plus_d,
    "D-M": Simulator.comp_d_minus_read_from_memory,
    "M-D": Simulator.comp_read_from_memory_minus_d,
    "D&M": Simulator.comp_read_from_memory_and,
    "D|M": Simulator.comp_read_from_memory_or,
}

JUMP_FUNCTIONS = {
    "": Simulator.dummy,
    "JGT": Simulator.jump_gt,
    "JEQ": Simulator.jump_eq,
    "JGE": Simulator.jump_ge,
    "JLT": Simulator.jump_lt,
    "JNE": Simulator.jump_ne,
    "JLE": Simulator.jump_le,
    "JMP": Simulator.dummy,
}
