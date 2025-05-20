from dataclasses import dataclass, field
from typing import TextIO

from n2t.core.compiler.tokenizer import Tokenizer
from n2t.infra.constants import OPERATORS, TOKEN_TYPES, UNARY_OPERATORS


@dataclass
class CompilationEngine:
    tokenizer: Tokenizer = field(default_factory=Tokenizer)
    output_file: TextIO = field(default_factory=TextIO)
    tab_counter: int = field(default_factory=int)

    def compile_class(self) -> None:
        self.write_tag("class", True)

        # write keyword class
        self.write()

        # write identifier class name
        self.tokenizer.advance()
        self.write()

        # write symbol {
        self.tokenizer.advance()
        self.write()

        # class_var_dec and subroutine
        self.tokenizer.advance()
        self.compile_class_var_dec()
        self.compile_subroutine()

        # write symbol }
        self.write()

        self.write_tag("class", False)

    def compile_class_var_dec(self) -> None:
        while self.tokenizer.get_token() in ["static", "field"]:
            self.write_tag("classVarDec", True)

            # write static/field
            self.write()

            # write keyword/identifier varType
            self.tokenizer.advance()
            self.write()

            # write identifier varName
            self.tokenizer.advance()
            self.write()

            # write all varNames
            self.tokenizer.advance()
            while self.tokenizer.get_token() == ",":
                self.write()
                self.tokenizer.advance()
                self.write()
                self.tokenizer.advance()

            # write symbol ;
            self.write()
            self.tokenizer.advance()

            self.write_tag("classVarDec", False)

    def compile_subroutine(self) -> None:
        while self.tokenizer.get_token() in ["constructor", "function", "method"]:
            self.write_tag("subroutineDec", True)

            # write keyword method/constructor/function
            self.write()

            # write keyword/identifier varType
            self.tokenizer.advance()
            self.write()

            # write identifier varName
            self.tokenizer.advance()
            self.write()

            # write symbol (
            self.tokenizer.advance()
            self.write()

            # write compile_parameter_list
            self.tokenizer.advance()
            self.compile_parameter_list()

            # write symbol )
            self.write()

            # write compile_subroutine_body
            self.tokenizer.advance()
            self.compile_subroutine_body()

            self.write_tag("subroutineDec", False)

    def compile_parameter_list(self) -> None:
        self.write_tag("parameterList", True)

        # write all varNames and varTypes
        while self.tokenizer.get_token() != ")":
            # write keyword/identifier varType
            self.write()

            self.tokenizer.advance()
            if self.tokenizer.get_token() == ",":
                self.write()
                self.tokenizer.advance()

        self.write_tag("parameterList", False)

    def compile_subroutine_body(self) -> None:
        self.write_tag("subroutineBody", True)

        # write {
        self.write()

        # write var_dec and statements
        self.tokenizer.advance()
        self.compile_var_dec()
        self.compile_statements()

        # write }
        self.write()
        self.tokenizer.advance()

        self.write_tag("subroutineBody", False)

    def compile_var_dec(self) -> None:
        while self.tokenizer.get_token() == 'var':
            self.write_tag("varDec", True)

            # write keyword var
            self.write()

            # write keyword/identifier varType
            self.tokenizer.advance()
            self.write()

            # write identifier varName
            self.tokenizer.advance()
            self.write()

            # write all varNames
            self.tokenizer.advance()
            while self.tokenizer.get_token() == ",":
                self.write()
                self.tokenizer.advance()
                self.write()
                self.tokenizer.advance()

            # write symbol ;
            self.write()
            self.tokenizer.advance()

            self.write_tag("varDec", False)

    def compile_statements(self) -> None:
        self.write_tag("statements", True)

        while self.tokenizer.token_type() == "KEYWORD":
            if self.tokenizer.get_token() == "let":
                self.compile_let()
            elif self.tokenizer.get_token() == "if":
                self.compile_if()
            elif self.tokenizer.get_token() == "while":
                self.compile_while()
            elif self.tokenizer.get_token() == "do":
                self.compile_do()
            elif self.tokenizer.get_token() == "return":
                self.compile_return()

        self.write_tag("statements", False)

    def compile_let(self) -> None:
        self.write_tag("letStatement", True)

        # write keyword let
        self.write()

        # write identifier
        self.tokenizer.advance()
        self.write()

        self.tokenizer.advance()
        if self.tokenizer.get_token() == "[":
            # write symbol [
            self.write()

            # write compile expression
            self.tokenizer.advance()
            self.compile_expression()

            # write symbol ]
            self.write()
            self.tokenizer.advance()

        # write symbol =
        self.write()

        # write compile expression
        self.tokenizer.advance()
        self.compile_expression()

        # write symbol ;
        self.write()
        self.tokenizer.advance()

        self.write_tag("letStatement", False)

    def compile_if(self) -> None:
        self.write_tag("ifStatement", True)

        # write keyword if
        self.write()

        # write symbol (
        self.tokenizer.advance()
        self.write()

        # write compile expression
        self.tokenizer.advance()
        self.compile_expression()

        # write symbol )
        self.write()

        # write symbol {
        self.tokenizer.advance()
        self.write()

        # write compile statements
        self.tokenizer.advance()
        self.compile_statements()

        # write symbol }
        self.write()

        self.tokenizer.advance()
        if self.tokenizer.get_token() == "else":
            # write keyword else
            self.write()

            # write symbol {
            self.tokenizer.advance()
            self.write()

            # write compile statements
            self.tokenizer.advance()
            self.compile_statements()

            # write symbol }
            self.write()
            self.tokenizer.advance()

        self.write_tag("ifStatement", False)

    def compile_while(self) -> None:
        self.write_tag("whileStatement", True)

        # write keyword while
        self.write()

        # write symbol (
        self.tokenizer.advance()
        self.write()

        # write compile expression
        self.tokenizer.advance()
        self.compile_expression()

        # write symbol )
        self.write()

        # write symbol {
        self.tokenizer.advance()
        self.write()

        # write compile statements
        self.tokenizer.advance()
        self.compile_statements()

        # write symbol }
        self.write()
        self.tokenizer.advance()

        self.write_tag("whileStatement", False)

    def compile_do(self) -> None:
        self.write_tag("doStatement", True)

        # write keyword do
        self.write()

        # write subroutine call
        self.tokenizer.advance()
        self.compile_subroutine_call()

        # write symbol ;
        self.write()
        self.tokenizer.advance()

        self.write_tag("doStatement", False)

    def compile_return(self) -> None:
        self.write_tag("returnStatement", True)

        # write keyword return
        self.write()

        self.tokenizer.advance()
        if self.tokenizer.get_token() != ";":
            self.compile_expression()

        # write symbol ;
        self.write()
        self.tokenizer.advance()

        self.write_tag("returnStatement", False)

    def compile_expression(self) -> None:
        self.write_tag("expression", True)

        self.compile_term()

        while self.tokenizer.get_token() in OPERATORS:
            # write symbol op
            self.write()

            self.tokenizer.advance()
            self.compile_term()

        self.write_tag("expression", False)

    def compile_term(self) -> None:
        self.write_tag("term", True)

        if self.tokenizer.get_token() == "(":
            # write symbol (
            self.write()

            # write compile expression
            self.tokenizer.advance()
            self.compile_expression()

            # wrote symbol )
            self.write()
            self.tokenizer.advance()

        elif self.tokenizer.token_type() == "IDENTIFIER":
            self.compile_subroutine_call()

        elif self.tokenizer.get_token() in UNARY_OPERATORS:
            self.write()
            self.tokenizer.advance()
            self.compile_term()
        else:
            self.write()
            self.tokenizer.advance()

        self.write_tag("term", False)

    def compile_expression_list(self) -> None:
        self.write_tag("expressionList", True)

        if self.tokenizer.get_token() != ")":
            self.compile_expression()
            while self.tokenizer.get_token() == ",":
                # write symbol ,
                self.write()

                # write compile expression
                self.tokenizer.advance()
                self.compile_expression()

        self.write_tag("expressionList", False)

    def write(self) -> None:
        tag = self.tokenizer.token_type()
        val = self.tokenizer.get_token()

        if tag in TOKEN_TYPES:
            tag = tag.lower()

        if tag == 'int_const':
            tag = 'integerConstant'
        elif tag == 'string_const':
            tag = 'stringConstant'

        if val == '<':
            val = '&lt;'
        elif val == '>':
            val = '&gt;'
        elif val == '&':
            val = '&amp;'

        res = self.tab_counter * "  " + f"<{tag}> {val} </{tag}>\n"
        self.output_file.write(res)

    def write_tag(self, s: str, opener: bool) -> None:
        res = self.tab_counter * "  "

        if opener:
            self.tab_counter += 1
            res += f"<{s}>"
        else:
            self.tab_counter -= 1
            res = res[:-2]
            res += f"</{s}>"

        res += '\n'
        self.output_file.write(res)

    def compile_subroutine_call(self) -> None:
        # write subroutine name
        self.write()

        self.tokenizer.advance()
        if self.tokenizer.get_token() == "[":
            # write symbol [
            self.write()

            self.tokenizer.advance()
            self.compile_expression()

            # write symbol ]
            self.write()
            self.tokenizer.advance()
        elif self.tokenizer.get_token() == ".":
            # write symbol .
            self.write()

            # write identifier subroutine name
            self.tokenizer.advance()
            self.write()

            # write symbol (
            self.tokenizer.advance()
            self.write()

            self.tokenizer.advance()
            self.compile_expression_list()

            # write symbol )
            self.write()
            self.tokenizer.advance()
        elif self.tokenizer.get_token() == "(":
            # write symbol (
            self.write()

            self.tokenizer.advance()
            self.compile_expression_list()

            # write symbol )
            self.write()
            self.tokenizer.advance()
