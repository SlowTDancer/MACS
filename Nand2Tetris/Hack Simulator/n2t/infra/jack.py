from __future__ import annotations

import os
from dataclasses import dataclass, field

from n2t.core.compiler.tokenizer import Tokenizer
from n2t.infra.compilation_engine import CompilationEngine
from n2t.infra.symbol_table import SymbolTable
from n2t.infra.vm_writer import VMWriter


@dataclass
class JackProgram:  # TODO: your work for Projects 10 and 11 starts here
    input_path: str = field(default_factory=str)

    @classmethod
    def load_from(cls, file_or_directory_name: str) -> JackProgram:
        return cls(file_or_directory_name)

    def compile(self) -> None:
        if os.path.isdir(self.input_path):
            self.compile_directory()
        else:
            self.compile_file(self.input_path)

    @staticmethod
    def compile_file(file: str) -> None:
        out_file, _ = os.path.splitext(file)
        new_out_file = out_file + ".vm"
        tokenizer = Tokenizer(file)
        tokenizer.open_and_tokenize()
        vm_writer = VMWriter(open(new_out_file, "w"))
        symbol_table = SymbolTable()
        compilation_engine = CompilationEngine(tokenizer, vm_writer, symbol_table)
        compilation_engine.compile_class()

    def compile_directory(self) -> None:
        for root, dirs, files in os.walk(self.input_path):
            for file in files:
                if file.endswith(".jack"):
                    file_path = os.path.join(root, file)
                    self.compile_file(file_path)
