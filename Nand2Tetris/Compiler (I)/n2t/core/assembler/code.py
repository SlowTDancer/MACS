from dataclasses import dataclass

from n2t.core.assembler.constants import COMP_TABLE, DEST_TABLE, JUMP_TABLE


@dataclass
class Code:
    dest_table = DEST_TABLE
    comp_table = COMP_TABLE
    jump_table = JUMP_TABLE

    def dest(self, dest: str) -> str:
        return self.dest_table[dest]

    def comp(self, comp: str) -> str:
        return self.comp_table[comp]

    def jump(self, jump: str) -> str:
        return self.jump_table[jump]
