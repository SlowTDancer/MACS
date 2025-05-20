class Parser:
    @staticmethod
    def instruction_type(instruction: str) -> str:
        return "A_INSTRUCTION" if instruction.startswith("@") else "C_INSTRUCTION"

    def symbol(self, instruction: str) -> str:
        instruction_type = self.instruction_type(instruction)
        if instruction_type == "A_INSTRUCTION":
            return instruction[1:]
        return ""

    def dest(self, instruction: str) -> str:
        instruction_type = self.instruction_type(instruction)
        return (
            instruction.split("=")[0].strip()
            if instruction_type == "C_INSTRUCTION" and "=" in instruction
            else ""
        )

    def comp(self, instruction: str) -> str:
        instruction_type = self.instruction_type(instruction)
        if instruction_type == "C_INSTRUCTION":
            if "=" in instruction:
                return instruction.split("=")[1].split(";")[0].strip()
            elif ";" in instruction:
                return instruction.split(";")[0].strip()
            else:
                return instruction.strip()
        return ""

    def jump(self, instruction: str) -> str:
        instruction_type = self.instruction_type(instruction)
        return (
            instruction.split(";")[1].strip()
            if instruction_type == "C_INSTRUCTION" and ";" in instruction
            else ""
        )
