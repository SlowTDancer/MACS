from typing import Iterable


class Parser:
    def __init__(self, assembly: Iterable[str]) -> None:
        self.asm = list(assembly)
        self.pc = 0

    def has_more_lines(self) -> bool:
        return self.pc < len(self.asm)

    def advance(self) -> None:
        self.pc += 1

    def instruction_type(self) -> str:
        instruction = self.asm[self.pc]
        if instruction.startswith("@"):
            return "A_INSTRUCTION"
        elif "(" in instruction and ")" in instruction:
            return "L_INSTRUCTION"
        return "C_INSTRUCTION"

    def symbol(self) -> str:
        instruction, instruction_type = self.asm[self.pc], self.instruction_type()
        if instruction_type == "A_INSTRUCTION":
            return instruction[1:]
        elif instruction_type == "L_INSTRUCTION":
            return instruction[1:-1]
        return ""

    def dest(self) -> str:
        instruction, instruction_type = self.asm[self.pc], self.instruction_type()
        return (
            instruction.split("=")[0].strip()
            if instruction_type == "C_INSTRUCTION" and "=" in instruction
            else ""
        )

    def comp(self) -> str:
        instruction, instruction_type = self.asm[self.pc], self.instruction_type()
        if instruction_type == "C_INSTRUCTION":
            if "=" in instruction:
                return instruction.split("=")[1].split(";")[0].strip()
            elif ";" in instruction:
                return instruction.split(";")[0].strip()
            else:
                return instruction.strip()
        return ""

    def jump(self) -> str:
        instruction, instruction_type = self.asm[self.pc], self.instruction_type()
        return (
            instruction.split(";")[1].strip()
            if instruction_type == "C_INSTRUCTION" and ";" in instruction
            else ""
        )

    def reset(self) -> None:
        self.pc = 0

    def strip_lines(self) -> None:
        res = []
        for line in self.asm:
            if line.strip().startswith("//") or not line.strip():
                continue
            if "//" in line:
                line = line[: line.index("//")]
            line = line.strip()
            res.append(line)
        self.asm = res
