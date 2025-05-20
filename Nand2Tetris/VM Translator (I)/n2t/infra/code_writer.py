from dataclasses import dataclass, field
from typing import Callable

from n2t.infra.constants import SPECIAL_REGISTERS


@dataclass
class CodeWriter:
    instructions: list[list[str]] = field(default_factory=list[list[str]])
    output_file: str = field(default_factory=str)
    label_counter: int = field(default_factory=int)
    instruction_counter: int = field(default_factory=int)

    def write(self) -> list[str]:
        res = []

        for instruction in self.instructions:
            res.append(FUNCTION_TABLE[instruction[0].upper()](self))
            self.instruction_counter += 1

        return res

    def add(self) -> str:
        sp = str(SPECIAL_REGISTERS["sp"])
        return (
            "@"
            + sp
            + "\n"
            + "A=M\n"
            + "A=A-1\n"
            + "D=M\n"
            + "A=A-1\n"
            + "D=D+M\n"
            + "M=D\n"
            + "@"
            + sp
            + "\n"
            + "M=M-1\n"
        )

    def sub(self) -> str:
        sp = str(SPECIAL_REGISTERS["sp"])
        return (
            "@"
            + sp
            + "\n"
            + "A=M\n"
            + "A=A-1\n"
            + "D=M\n"
            + "A=A-1\n"
            + "D=M-D\n"
            + "M=D\n"
            + "@"
            + sp
            + "\n"
            + "M=M-1\n"
        )

    def neg(self) -> str:
        sp = str(SPECIAL_REGISTERS["sp"])
        return "@" + sp + "\n" + "A=M\n" + "A=A-1\n" + "M=-M\n"

    def eq(self) -> str:
        sp = str(SPECIAL_REGISTERS["sp"])
        true_label = "TRUE_EQ_" + str(self.label_counter)
        end_label = "END_EQ_" + str(self.label_counter)
        self.label_counter += 1
        return (
            "@"
            + sp
            + "\n"
            + "A=M\n"
            + "A=A-1\n"
            + "D=M\n"
            + "A=A-1\n"
            + "D=M-D\n"
            + "@"
            + true_label
            + "\n"
            + "D;JEQ\n"
            + "D=0\n"
            + "@"
            + sp
            + "\n"
            + "A=M-1\n"
            + "A=A-1\n"
            + "M=D\n"
            + "@"
            + end_label
            + "\n"
            + "0;JMP\n"
            + "("
            + true_label
            + ")"
            + "\n"
            + "D=-1\n"
            + "@"
            + sp
            + "\n"
            + "A=M-1\n"
            + "A=A-1\n"
            + "M=D\n"
            + "("
            + end_label
            + ")"
            + "\n"
            + "@"
            + sp
            + "\n"
            + "M=M-1\n"
        )

    def gt(self) -> str:
        sp = str(SPECIAL_REGISTERS["sp"])
        true_label = "TRUE_EQ_" + str(self.label_counter)
        end_label = "END_EQ_" + str(self.label_counter)
        self.label_counter += 1
        return (
            "@"
            + sp
            + "\n"
            + "A=M\n"
            + "A=A-1\n"
            + "D=M\n"
            + "A=A-1\n"
            + "D=M-D\n"
            + "@"
            + true_label
            + "\n"
            + "D;JGT\n"
            + "D=0\n"
            + "@"
            + sp
            + "\n"
            + "A=M-1\n"
            + "A=A-1\n"
            + "M=D\n"
            + "@"
            + end_label
            + "\n"
            + "0;JMP\n"
            + "("
            + true_label
            + ")"
            + "\n"
            + "D=-1\n"
            + "@"
            + sp
            + "\n"
            + "A=M-1\n"
            + "A=A-1\n"
            + "M=D\n"
            + "("
            + end_label
            + ")"
            + "\n"
            + "@"
            + sp
            + "\n"
            + "M=M-1\n"
        )

    def lt(self) -> str:
        sp = str(SPECIAL_REGISTERS["sp"])
        true_label = "TRUE_EQ_" + str(self.label_counter)
        end_label = "END_EQ_" + str(self.label_counter)
        self.label_counter += 1
        return (
            "@"
            + sp
            + "\n"
            + "A=M\n"
            + "A=A-1\n"
            + "D=M\n"
            + "A=A-1\n"
            + "D=M-D\n"
            + "@"
            + true_label
            + "\n"
            + "D;JLT\n"
            + "D=0\n"
            + "@"
            + sp
            + "\n"
            + "A=M-1\n"
            + "A=A-1\n"
            + "M=D\n"
            + "@"
            + end_label
            + "\n"
            + "0;JMP\n"
            + "("
            + true_label
            + ")"
            + "\n"
            + "D=-1\n"
            + "@"
            + sp
            + "\n"
            + "A=M-1\n"
            + "A=A-1\n"
            + "M=D\n"
            + "("
            + end_label
            + ")"
            + "\n"
            + "@"
            + sp
            + "\n"
            + "M=M-1\n"
        )

    def vm_and(self) -> str:
        sp = str(SPECIAL_REGISTERS["sp"])
        return (
            "@"
            + sp
            + "\n"
            + "M=M-1\n"
            + "A=M\n"
            + "D=M\n"
            + "A=A-1\n"
            + "D=D&M\n"
            + "M=D\n"
        )

    def vm_or(self) -> str:
        sp = str(SPECIAL_REGISTERS["sp"])
        return (
            "@"
            + sp
            + "\n"
            + "M=M-1\n"
            + "A=M\n"
            + "D=M\n"
            + "A=A-1\n"
            + "D=D|M\n"
            + "M=D\n"
        )

    def vm_not(self) -> str:
        sp = str(SPECIAL_REGISTERS["sp"])
        return "@" + sp + "\n" + "A=M\n" + "A=A-1\n" + "M=!M\n"

    def push(self) -> str:
        instruction = self.instructions[self.instruction_counter]
        segment = instruction[1]
        val = instruction[2]
        sp = str(SPECIAL_REGISTERS["sp"])
        if segment == "constant":
            return (
                "@"
                + val
                + "\n"
                + "D=A\n"
                + "@"
                + sp
                + "\n"
                + "A=M\n"
                + "M=D\n"
                + "@"
                + sp
                + "\n"
                + "M=M+1\n"
            )
        if segment in ["local", "argument", "this", "that"]:
            segment_pointer_address = str(SPECIAL_REGISTERS[segment])
            return (
                "@"
                + segment_pointer_address
                + "\n"
                + "A=M\n"
                + "D=A\n"
                + "@"
                + val
                + "\n"
                + "D=D+A\n"
                + "A=D\n"
                + "D=M\n"
                + "@"
                + sp
                + "\n"
                + "A=M\n"
                + "M=D\n"
                + "@"
                + sp
                + "\n"
                + "M=M+1\n"
            )
        if segment == "temp":
            return (
                "@5\n"
                + "D=A\n"
                + "@"
                + val
                + "\n"
                + "D=D+A\n"
                + "A=D\n"
                + "D=M\n"
                + "@"
                + sp
                + "\n"
                + "A=M\n"
                + "M=D\n"
                + "@"
                + sp
                + "\n"
                + "M=M+1\n"
            )
        if segment == "static":
            return (
                "@"
                + self.output_file
                + "."
                + val
                + "\n"
                + "D=M\n"
                + "@"
                + sp
                + "\n"
                + "A=M\n"
                + "M=D\n"
                + "@"
                + sp
                + "\n"
                + "M=M+1\n"
            )
        if segment == "pointer":
            this_or_that = str(SPECIAL_REGISTERS["that"])
            if val == "0":
                this_or_that = str(SPECIAL_REGISTERS["this"])
            return (
                "@"
                + this_or_that
                + "\n"
                + "D=M\n"
                + "@"
                + sp
                + "\n"
                + "A=M\n"
                + "M=D\n"
                + "@"
                + sp
                + "\n"
                + "M=M+1\n"
            )
        return ""

    def pop(self) -> str:
        instruction = self.instructions[self.instruction_counter]
        segment = instruction[1]
        val = instruction[2]
        sp = str(SPECIAL_REGISTERS["sp"])
        if segment in ["local", "argument", "this", "that"]:
            segment_pointer_address = str(SPECIAL_REGISTERS[segment])
            return (
                "@"
                + segment_pointer_address
                + "\n"
                + "A=M\n"
                + "D=A\n"
                + "@"
                + val
                + "\n"
                + "D=D+A\n"
                + "@"
                + sp
                + "\n"
                + "A=M\n"
                + "M=D\n"
                + "@"
                + sp
                + "\n"
                + "M=M-1\n"
                + "@"
                + sp
                + "\n"
                + "A=M\n"
                + "D=M\n"
                + "@"
                + sp
                + "\n"
                + "A=M\n"
                + "A=A+1\n"
                + "A=M\n"
                + "M=D\n"
            )
        elif segment == "static":
            return (
                "@"
                + sp
                + "\n"
                + "M=M-1\n"
                + "@"
                + sp
                + "\n"
                + "A=M\n"
                + "D=M\n"
                + "@"
                + self.output_file
                + "."
                + val
                + "\n"
                + "M=D\n"
            )
        elif segment == "temp":
            return (
                "@5\n"
                + "D=A\n"
                + "@"
                + val
                + "\n"
                + "D=D+A\n"
                + "@"
                + sp
                + "\n"
                + "A=M\n"
                + "M=D\n"
                + "@"
                + sp
                + "\n"
                + "M=M-1\n"
                + "@"
                + sp
                + "\n"
                + "A=M\n"
                + "D=M\n"
                + "@"
                + sp
                + "\n"
                + "A=M\n"
                + "A=A+1\n"
                + "A=M\n"
                + "M=D\n"
            )
        elif segment == "pointer":
            this_or_that = str(SPECIAL_REGISTERS["that"])
            if val == "0":
                this_or_that = str(SPECIAL_REGISTERS["this"])
            return (
                "@"
                + sp
                + "\n"
                + "M=M-1\n"
                + "A=M\n"
                + "D=M\n"
                + "@"
                + this_or_that
                + "\n"
                + "M=D\n"
            )
        return ""


FUNCTION_TABLE: dict[str, Callable[[CodeWriter], str]] = {
    "ADD": CodeWriter.add,
    "SUB": CodeWriter.sub,
    "NEG": CodeWriter.neg,
    "EQ": CodeWriter.eq,
    "GT": CodeWriter.gt,
    "LT": CodeWriter.lt,
    "AND": CodeWriter.vm_and,
    "OR": CodeWriter.vm_or,
    "NOT": CodeWriter.vm_not,
    "PUSH": CodeWriter.push,
    "POP": CodeWriter.pop,
}
