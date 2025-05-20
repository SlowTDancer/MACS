import re
from dataclasses import dataclass, field

from n2t.infra.constants import KEYWORDS, SYMBOLS


@dataclass
class Tokenizer:
    input_file: str = field(default_factory=str)
    tokens: list[str] = field(default_factory=list[str])
    token_counter: int = field(default_factory=int)
    token_regex = re.compile(
        r'"[^"\n]*"'
        r'|class|constructor|function|method|field|static|var|int|char|boolean|void|true|false|null|this|let|do|if'
        r'|else|while|return'
        r'|[{}()\[\].,;+\-*/&|<>=~]'
        r'|\d+'
        r'|\w+'
    )

    def open_and_tokenize(self) -> None:
        with open(self.input_file, 'r') as file:
            code = file.read()

        code = re.sub(r'/\*\*.*?\*/', '', code, flags=re.DOTALL)
        code = re.sub(r'//.*', '', code)
        code = re.sub(r'/\*.*?\*/', '', code, flags=re.DOTALL)

        self.tokens = self.token_regex.findall(code)
        self.tokens = [token for token in self.tokens if token.strip()]

    def has_more_tokens(self) -> bool:
        return self.token_counter < len(self.tokens)

    def advance(self) -> None:
        self.token_counter += 1

    def get_token(self) -> str | int:
        token_type = self.token_type()

        if token_type == 'KEYWORD':
            return self.keyword()
        elif token_type == 'SYMBOL':
            return self.symbol()
        elif token_type == 'IDENTIFIER':
            return self.identifier()
        elif token_type == 'INT_CONST':
            return self.int_val()

        return self.string_val()

    def token_type(self) -> str:
        curr_token = self.tokens[self.token_counter]

        if curr_token in KEYWORDS:
            return 'KEYWORD'
        elif curr_token in SYMBOLS:
            return 'SYMBOL'
        elif re.match(r'^\d+$', curr_token):
            return "INT_CONST"
        elif re.match(r'^".*"$', curr_token):
            return "STRING_CONST"

        return 'IDENTIFIER'

    def keyword(self) -> str:
        curr_token = self.tokens[self.token_counter]
        return curr_token

    def symbol(self) -> str:
        curr_token = self.tokens[self.token_counter]
        return curr_token

    def identifier(self) -> str:
        curr_token = self.tokens[self.token_counter]
        return curr_token

    def int_val(self) -> int:
        curr_token = int(self.tokens[self.token_counter])
        return curr_token

    def string_val(self) -> str:
        curr_token = self.tokens[self.token_counter]
        return curr_token[1:-1]
