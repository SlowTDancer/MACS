SPECIAL_REGISTERS = {"sp": 0, "local": 1, "argument": 2, "this": 3, "that": 4}

KEYWORDS = [
    "class",
    "constructor",
    "function",
    "method",
    "field",
    "static",
    "var",
    "int",
    "char",
    "boolean",
    "void",
    "true",
    "false",
    "null",
    "this",
    "let",
    "do",
    "if",
    "else",
    "while",
    "return",
]

SYMBOLS = [
    "{",
    "}",
    "(",
    ")",
    "[",
    "]",
    ".",
    ",",
    ";",
    "+",
    "-",
    "*",
    "/",
    "&",
    "|",
    "<",
    ">",
    "=",
    "~",
]

TOKEN_TYPES = ["KEYWORD", "SYMBOL", "IDENTIFIER", "INT_CONST", "STRING_CONST"]

OPERATORS = ["+", "-", "*", "/", "&", "|", "<", ">", "="]

UNARY_OPERATORS = ["-", "~"]

OPERATOR_MAP = {
    "+": "add",
    "-": "sub",
    "--": "neg",
    "=": "eq",
    ">": "gt",
    "<": "lt",
    "&": "and",
    "|": "or",
    "~": "not",
    "*": "call Math.multiply 2",
    "/": "call Math.divide 2",
}
