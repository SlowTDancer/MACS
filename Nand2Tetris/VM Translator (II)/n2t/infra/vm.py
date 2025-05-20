from __future__ import annotations

import os
from dataclasses import dataclass, field
from pathlib import Path

from n2t.infra.code_writer import CodeWriter


@dataclass
class VmProgram:  # TODO: your work for Projects 7 and 8 starts here
    input_path: str = field(default_factory=str)

    @classmethod
    def load_from(cls, file_or_directory_name: str) -> VmProgram:
        return cls(file_or_directory_name)

    def translate(self) -> None:
        output_file = Path(self.input_path)
        filename = self.input_path.split("\\")[-1]
        if os.path.isdir(self.input_path):
            if filename == "":
                filename = self.input_path.split("\\")[-2]
            res = self.translate_directory()
            output_file = output_file.joinpath(f"{filename}")
        else:
            res = self.translate_file(filename)

        output_file = output_file.with_suffix(".asm")
        with open(output_file, "w") as file:
            for line in res:
                file.write(line + "\n")

    def __open_and_strip(self, file: str) -> list[list[str]]:
        res = []
        with open(file, "r") as f:
            for line in f:
                if line.strip().startswith("//") or not line.strip():
                    continue
                if "//" in line:
                    line = line[: line.index("//")]
                line = line.strip()
                res.append(line.split())
        return res

    def translate_directory(self) -> list[str]:
        res = []
        sys_vm_file = os.path.join(self.input_path, "Sys.vm")
        if os.path.exists(sys_vm_file):
            res += ["@256", "D=A", "@0", "M=D"]
            temp_writer = CodeWriter([["call", "Sys.init", "0"]], sys_vm_file)
            res += temp_writer.write()
            with open(sys_vm_file, "r"):
                instructions = self.__open_and_strip(sys_vm_file)
                code_writer = CodeWriter(instructions, sys_vm_file)
                res += code_writer.write()
        for filename in os.listdir(self.input_path):
            if not filename.endswith(".vm") or filename == "Sys.vm":
                continue
            curr_filename = filename.split("\\")[-1].split(".")[0]
            with open(os.path.join(self.input_path, filename), "r"):
                instructions = self.__open_and_strip(
                    os.path.join(self.input_path, filename)
                )
                code_writer = CodeWriter(instructions, curr_filename)
                res += code_writer.write()
        return res

    def translate_file(self, file: str) -> list[str]:
        instructions = self.__open_and_strip(self.input_path)
        output_file = file.split("\\")[-1].split(".")[0]
        code_writer = CodeWriter(instructions, output_file)
        res = code_writer.write()

        return res
