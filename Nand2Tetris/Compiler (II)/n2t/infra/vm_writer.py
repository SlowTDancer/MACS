from dataclasses import dataclass, field
from typing import TextIO

from n2t.infra.constants import OPERATOR_MAP


@dataclass
class VMWriter:
    output_file: TextIO = field(default_factory=TextIO)

    def write_push(self, segment: str, index: int) -> None:
        self.output_file.write("push " + segment + " " + str(index) + "\n")

    def write_pop(self, segment: str, index: int) -> None:
        self.output_file.write("pop " + segment + " " + str(index) + "\n")

    def write_arithmetic(self, command: str) -> None:
        self.output_file.write(OPERATOR_MAP[command] + "\n")

    def write_label(self, label: str) -> None:
        self.output_file.write("label " + label + "\n")

    def write_goto(self, label: str) -> None:
        self.output_file.write("goto " + label + "\n")

    def write_if(self, label: str) -> None:
        self.output_file.write("if-goto " + label + "\n")

    def write_call(self, name: str, n_locals: int) -> None:
        self.output_file.write("call " + name + " " + str(n_locals) + "\n")

    def write_function(self, name: str, n_args: int) -> None:
        self.output_file.write("function " + name + " " + str(n_args) + "\n")

    def write_return(self) -> None:
        self.output_file.write("return" + "\n")

    def close(self) -> None:
        self.output_file.close()
