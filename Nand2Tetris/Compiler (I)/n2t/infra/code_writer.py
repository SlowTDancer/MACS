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

    def write_add(self) -> str:
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

    def write_sub(self) -> str:
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

    def write_neg(self) -> str:
        sp = str(SPECIAL_REGISTERS["sp"])
        return "@" + sp + "\n" + "A=M\n" + "A=A-1\n" + "M=-M\n"

    def write_eq(self) -> str:
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

    def write_gt(self) -> str:
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

    def write_lt(self) -> str:
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

    def write_and(self) -> str:
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

    def write_or(self) -> str:
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

    def write_not(self) -> str:
        sp = str(SPECIAL_REGISTERS["sp"])
        return "@" + sp + "\n" + "A=M\n" + "A=A-1\n" + "M=!M\n"

    def write_push(self) -> str:
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

    def write_pop(self) -> str:
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

    def write_label(self) -> str:
        label_name = self.instructions[self.instruction_counter][1]
        return f"({label_name})\n"

    def write_goto(self) -> str:
        label_name = self.instructions[self.instruction_counter][1]
        return f"@{label_name}\n" + "0;JMP\n"

    def write_if_goto(self) -> str:
        label_name = self.instructions[self.instruction_counter][1]
        return "@SP\n" + "M=M-1\n" + "A=M\n" + "D=M\n" + f"@{label_name}\n" + "D;JNE\n"

    def write_function(self) -> str:
        function_name = self.instructions[self.instruction_counter][1]
        n_vars = int(self.instructions[self.instruction_counter][2])

        res = f"({function_name})\n"
        for _ in range(n_vars):
            res += ("@0\n"
                    + "D=A\n"
                    + "@SP\n"
                    + "A=M\n"
                    + "M=D\n"
                    + "@SP\n"
                    + "M=M+1\n"
                    )

        return res

    def write_call(self) -> str:
        function_name = self.instructions[self.instruction_counter][1]
        n_args = self.instructions[self.instruction_counter][2]
        ret_addr_label = function_name + "$ret." + str(self.label_counter)
        self.label_counter += 1
        return (
            f"@{ret_addr_label}\n"
            + "D=A\n"
            + "@SP\n"
            + "A=M\n"
            + "M=D\n"
            + "@SP\n"
            + "M=M+1\n"
            + "@LCL\n"
            + "D=M\n"
            + "@SP\n"
            + "A=M\n"
            + "M=D\n"
            + "@SP\n"
            + "M=M+1\n"
            + "@ARG\n"
            + "D=M\n"
            + "@SP\n"
            + "A=M\n"
            + "M=D\n"
            + "@SP\n"
            + "M=M+1\n"
            + "@THIS\n"
            + "D=M\n"
            + "@SP\n"
            + "A=M\n"
            + "M=D\n"
            + "@SP\n"
            + "M=M+1\n"
            + "@THAT\n"
            + "D=M\n"
            + "@SP\n"
            + "A=M\n"
            + "M=D\n"
            + "@SP\n"
            + "M=M+1\n"
            + "@5\n"
            + "D=A\n"
            + f"@{n_args}\n"
            + "D=D+A\n"
            + "@SP\n"
            + "A=M\n"
            + "D=A-D\n"
            + "@ARG\n"
            + "M=D\n"
            + "@SP\n"
            + "D=M\n"
            + "@LCL\n"
            + "M=D\n"
            + f"@{function_name}\n"
            + "0;JMP\n"
            + f"({ret_addr_label})\n"
        )

    def write_return(self) -> str:
        return (
            "@LCL\n"
            + "D=M\n"
            + "@endFrame\n"
            + "M=D\n"
            + "@5\n"
            + "D=D-A\n"
            + "A=D\n"
            + "D=M\n"
            + "@retAddr\n"
            + "M=D\n"
            + "@SP\n"
            + "A=M\n"
            + "A=A-1\n"
            + "D=M\n"
            + "@SP\n"
            + "M=M-1\n"
            + "@ARG\n"
            + "A=M\n"
            + "M=D\n"
            + "@ARG\n"
            + "A=M\n"
            + "A=A+1\n"
            + "D=A\n"
            + "@SP\n"
            + "M=D\n"
            + "@endFrame\n"
            + "D=M\n"
            + "@1\n"
            + "D=D-A\n"
            + "A=D\n"
            + "D=M\n"
            + "@THAT\n"
            + "M=D\n"
            + "@endFrame\n"
            + "D=M\n"
            + "@2\n"
            + "D=D-A\n"
            + "A=D\n"
            + "D=M\n"
            + "@THIS\n"
            + "M=D\n"
            + "@endFrame\n"
            + "D=M\n"
            + "@3\n"
            + "D=D-A\n"
            + "A=D\n"
            + "D=M\n"
            + "@ARG\n"
            + "M=D\n"
            + "@endFrame\n"
            + "D=M\n"
            + "@4\n"
            + "D=D-A\n"
            + "A=D\n"
            + "D=M\n"
            + "@LCL\n"
            + "M=D\n"
            + "@retAddr\n"
            + "A=M\n"
            + "0;JMP\n"
        )


FUNCTION_TABLE: dict[str, Callable[[CodeWriter], str]] = {
    "ADD": CodeWriter.write_add,
    "SUB": CodeWriter.write_sub,
    "NEG": CodeWriter.write_neg,
    "EQ": CodeWriter.write_eq,
    "GT": CodeWriter.write_gt,
    "LT": CodeWriter.write_lt,
    "AND": CodeWriter.write_and,
    "OR": CodeWriter.write_or,
    "NOT": CodeWriter.write_not,
    "PUSH": CodeWriter.write_push,
    "POP": CodeWriter.write_pop,
    "LABEL": CodeWriter.write_label,
    "GOTO": CodeWriter.write_goto,
    "IF-GOTO": CodeWriter.write_if_goto,
    "FUNCTION": CodeWriter.write_function,
    "CALL": CodeWriter.write_call,
    "RETURN": CodeWriter.write_return,
}
