from __future__ import annotations

import json
from dataclasses import dataclass, field
from pathlib import Path

from n2t.core import Assembler, Disassembler
from n2t.core.simulator.simulator import Simulator
from n2t.infra.io import File


@dataclass
class SimulatorProgram:
    input_path: Path = field(default_factory=Path)
    cycles: int = field(default_factory=int)

    @classmethod
    def load_from(cls, input_path: str, cycles: int) -> SimulatorProgram:
        return cls(Path(input_path), cycles)

    def execute(self) -> None:
        assembler, disassembler = Assembler.create(), Disassembler.create()
        words = File(self.input_path).load()
        if self.input_path.suffix == ".asm":
            words = assembler.assemble(words)
        words = list(disassembler.disassemble(words))

        simulator = Simulator(words, self.cycles)
        simulated_ram = simulator.simulate()
        simulated_ram = {
            key: simulated_ram[key] for key in sorted(simulated_ram.keys())
        }
        self.write_in_file(simulated_ram)

    def write_in_file(self, simulated_ram: dict[int, int]) -> None:
        temporary_ram = {"RAM": simulated_ram}
        ram = json.dumps(temporary_ram, indent=2)
        output_file = Path(self.input_path)
        output_file = output_file.with_suffix(".json")
        output_file.write_text(ram)
