from __future__ import annotations

from dataclasses import dataclass, field

from n2t.infra.code_writer import CodeWriter


@dataclass
class VmProgram:  # TODO: your work for Projects 7 and 8 starts here
    input_file: str = field(default_factory=str)

    @classmethod
    def load_from(cls, file_or_directory_name: str) -> VmProgram:
        return cls(file_or_directory_name)

    def translate(self) -> None:
        instructions = self.__open_and_strip()
        result_file_path = self.input_file[:-3]
        output_file = result_file_path.split("\\")[-1]
        code_writer = CodeWriter(instructions, output_file)
        res = code_writer.write()

        with open(f"{result_file_path}.asm", "w") as file:
            for line in res:
                file.write(line + "\n")

    def __open_and_strip(self) -> list[list[str]]:
        res = []
        with open(self.input_file, "r") as f:
            for line in f:
                if line.strip().startswith("//") or not line.strip():
                    continue
                if "//" in line:
                    line = line[: line.index("//")]
                line = line.strip()
                res.append(line.split())
        return res
