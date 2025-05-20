from dataclasses import dataclass, field

from n2t.core.compiler.tokenizer import Tokenizer
from n2t.infra.constants import OPERATORS, UNARY_OPERATORS
from n2t.infra.symbol_table import SymbolTable
from n2t.infra.vm_writer import VMWriter


@dataclass
class CompilationEngine:
    tokenizer: Tokenizer = field(default_factory=Tokenizer)
    vm_writer: VMWriter = field(default_factory=VMWriter)
    class_table: SymbolTable = field(default_factory=SymbolTable)
    method_table: SymbolTable = field(default_factory=SymbolTable)
    class_name: str = field(default_factory=str)
    counter: int = field(default_factory=int)

    def compile_class(self) -> None:
        self.tokenizer.advance()
        self.class_name = str(self.tokenizer.get_token())
        self.tokenizer.advance()
        self.tokenizer.advance()

        while True:
            token = self.tokenizer.get_token()

            if token == "}":
                break
            elif token == "field" or token == "static":
                self.compile_var_dec()
            else:
                self.compile_subroutine()
                self.tokenizer.rollback()

            if self.tokenizer.has_more_tokens():
                self.tokenizer.advance()
            else:
                break

    def compile_subroutine(self) -> None:
        self.method_table.reset()

        function_type = self.tokenizer.get_token()
        self.tokenizer.advance()
        self.tokenizer.advance()
        function_name = self.class_name + "." + str(self.tokenizer.get_token())
        self.tokenizer.advance()

        if function_type == "method":
            self.method_table.define("this", self.class_name, "argument")
        self.compile_parameter_list()
        self.tokenizer.advance()

        while self.tokenizer.get_token() == "var":
            self.compile_var_dec()
            self.tokenizer.advance()

        self.vm_writer.write_function(
            function_name, self.method_table.var_count("local")
        )

        if function_type == "constructor":
            self.vm_writer.write_push("constant", self.class_table.var_count("this"))
            self.vm_writer.write_call("Memory.alloc", 1)
            self.vm_writer.write_pop("pointer", 0)
        elif function_type == "method":
            self.vm_writer.write_push("argument", 0)
            self.vm_writer.write_pop("pointer", 0)

        self.compile_statements()

    def compile_parameter_list(self) -> None:
        self.tokenizer.advance()

        while True:
            token = self.tokenizer.get_token()
            self.tokenizer.advance()

            if token == ")":
                break
            elif token == ",":
                continue
            else:
                sym_type = token
                name = self.tokenizer.get_token()
                self.tokenizer.advance()
                self.method_table.define(name, sym_type, "argument")

    def compile_var_dec(self) -> None:
        symbol_kind = self.tokenizer.get_token()
        if symbol_kind == "field":
            symbol_kind = "this"
        elif symbol_kind == "var":
            symbol_kind = "local"

        self.tokenizer.advance()
        symbol_type = self.tokenizer.get_token()
        self.tokenizer.advance()

        while True:
            token = self.tokenizer.get_token()

            if token == ";":
                break
            elif token == ",":
                self.tokenizer.advance()
            else:
                if symbol_kind == "local":
                    self.method_table.define(token, symbol_type, symbol_kind)
                else:
                    self.class_table.define(token, symbol_type, symbol_kind)
                self.tokenizer.advance()

    def compile_statements(self) -> None:
        while True:
            token = self.tokenizer.get_token()

            if token == "}":
                self.tokenizer.advance()
                break
            else:
                self.compile_statement()

    def compile_let(self) -> None:
        lhs = []
        while self.tokenizer.get_token() != "=":
            lhs.append(str(self.tokenizer.get_token()))
            self.tokenizer.advance()
        self.tokenizer.advance()

        self.compile_expression(self.tokenizer)

        assert self.tokenizer.get_token() == ";"

        self.tokenizer.advance()

        lhs_iterator = Tokenizer(tokens=lhs)
        var_name = lhs_iterator.get_token()

        if var_name in self.method_table.symbols.keys():
            kind = self.method_table.kind_of(var_name)
            index = self.method_table.index_of(var_name)
        else:
            kind = self.class_table.kind_of(var_name)
            index = self.class_table.index_of(var_name)

        if lhs_iterator.has_more_tokens():
            lhs_iterator.advance()
            lhs_iterator.advance()
            self.vm_writer.write_push(kind, index)
            self.compile_expression(lhs_iterator)
            self.vm_writer.write_arithmetic("+")
            self.vm_writer.write_pop("pointer", 1)
            self.vm_writer.write_pop("that", 0)
        else:
            self.vm_writer.write_pop(kind, index)

    def compile_if(self) -> None:
        self.tokenizer.advance()
        self.compile_expression(self.tokenizer)
        self.vm_writer.write_arithmetic("~")

        assert self.tokenizer.get_token() == ")"

        self.tokenizer.advance()

        self.vm_writer.write_if("L" + str(self.counter))
        self.counter += 1
        l_counter = self.counter
        self.counter += 4
        self.tokenizer.advance()
        self.compile_statements()
        self.vm_writer.write_goto("L" + str(l_counter))

        self.vm_writer.write_label("L" + str(l_counter - 1))
        self.counter += 5
        if self.tokenizer.get_token() == "else":
            self.tokenizer.advance()
            self.tokenizer.advance()
            self.compile_statements()
        self.vm_writer.write_label("L" + str(l_counter))

    def compile_while(self) -> None:
        self.tokenizer.advance()

        self.vm_writer.write_label("L" + str(self.counter))
        self.counter += 1

        self.compile_expression(self.tokenizer)
        self.vm_writer.write_arithmetic("~")
        self.vm_writer.write_if("L" + str(self.counter))

        assert self.tokenizer.get_token() == ")"

        self.tokenizer.advance()
        self.tokenizer.advance()

        l_counter = self.counter
        self.counter += 4
        self.compile_statements()

        self.vm_writer.write_goto("L" + str(l_counter - 1))
        self.vm_writer.write_label("L" + str(l_counter))

    def compile_do(self) -> None:
        self.compile_expression(self.tokenizer)

        assert self.tokenizer.get_token() == ";"
        self.tokenizer.advance()

        self.vm_writer.write_pop("temp", 0)

    def compile_return(self) -> None:
        token = self.tokenizer.get_token()
        if token == ";":
            self.vm_writer.write_push("constant", 0)
            self.tokenizer.advance()
        elif token == "this":
            self.vm_writer.write_push("pointer", 0)
            self.tokenizer.advance()
            self.tokenizer.advance()
        else:
            self.compile_expression(self.tokenizer)

            assert self.tokenizer.get_token() == ";"

            self.tokenizer.advance()

        self.vm_writer.write_return()

    def compile_expression(self, tokenizer: Tokenizer) -> None:
        op: str = ""

        while True:
            token = tokenizer.get_token()

            if token in ["]", ")", ";", ","]:
                break

            if token in UNARY_OPERATORS:
                tokenizer.advance()
                self.compile_term(tokenizer)
                if token == "-":
                    self.vm_writer.write_arithmetic("--")
                else:
                    self.vm_writer.write_arithmetic("~")
            else:
                self.compile_term(tokenizer)

            if not op == "":
                self.vm_writer.write_arithmetic(op)

            token = tokenizer.get_token()
            if token in ["]", ")", ";", ","]:
                break

            op = str(tokenizer.get_token())
            tokenizer.advance()

    def compile_term(self, tokenizer: Tokenizer) -> None:
        if tokenizer.get_token() == "(":
            tokenizer.advance()
            self.compile_expression(tokenizer)

            if tokenizer.get_token() == "]":
                for _ in range(3):
                    tokenizer.rollback()
            assert tokenizer.get_token() == ")"

            tokenizer.advance()
            return
        if tokenizer.token_type() == "INT_CONST":
            self.vm_writer.write_push("constant", int(tokenizer.get_token()))
            tokenizer.advance()
            return
        if tokenizer.token_type() == "STRING_CONST":
            str_const = str(tokenizer.get_token())
            self.vm_writer.write_push("constant", len(str_const))
            self.vm_writer.write_call("String.new", 1)

            for i in range(0, len(str_const)):
                self.vm_writer.write_push("constant", ord(str_const[i]))
                self.vm_writer.write_call("String.appendChar", 2)

            tokenizer.advance()
            return
        if tokenizer.get_token() == "true":
            self.vm_writer.write_push("constant", 0)
            self.vm_writer.write_arithmetic("~")

            tokenizer.advance()
            return
        if tokenizer.get_token() == "false" or tokenizer.get_token() == "null":
            self.vm_writer.write_push("constant", 0)

            tokenizer.advance()
            return

        var_name = tokenizer.get_token()
        tokenizer.advance()
        if tokenizer.get_token() in OPERATORS or tokenizer.get_token() in [
            "]",
            ")",
            ";",
            ",",
        ]:
            if var_name == "this":
                self.vm_writer.write_push("pointer", 0)
                return
            if var_name in self.method_table.symbols.keys():
                kind = self.method_table.kind_of(var_name)
                index = self.method_table.index_of(var_name)
            else:
                kind = self.class_table.kind_of(var_name)
                index = self.class_table.index_of(var_name)
            self.vm_writer.write_push(kind, index)
            return
        if tokenizer.get_token() == "[":
            if var_name in self.method_table.symbols.keys():
                kind = self.method_table.kind_of(var_name)
                index = self.method_table.index_of(var_name)
            else:
                kind = self.class_table.kind_of(var_name)
                index = self.class_table.index_of(var_name)
            tokenizer.advance()
            self.compile_expression(tokenizer)
            self.vm_writer.write_push(kind, index)
            self.vm_writer.write_arithmetic("+")
            self.vm_writer.write_pop("pointer", 1)
            self.vm_writer.write_push("that", 0)

            assert tokenizer.get_token() == "]"

            tokenizer.advance()
            return
        if tokenizer.get_token() == "(":
            func_name = self.class_name + "." + str(var_name)
            tokenizer.advance()

            self.vm_writer.write_push("pointer", 0)
            self.compile_expression_list(tokenizer, func_name, 1)
            return
        if str(var_name)[0].isupper():
            func_name = str(var_name)
            func_name += str(tokenizer.get_token())
            tokenizer.advance()
            func_name += str(tokenizer.get_token())
            tokenizer.advance()
            tokenizer.advance()

            self.compile_expression_list(tokenizer, func_name, 0)
            return

        if var_name in self.method_table.symbols.keys():
            sym_type = self.method_table.type_of(var_name)
            kind = self.method_table.kind_of(var_name)
            index = self.method_table.index_of(var_name)
        else:
            sym_type = self.class_table.type_of(var_name)
            kind = self.class_table.kind_of(var_name)
            index = self.class_table.index_of(var_name)
        self.vm_writer.write_push(kind, index)
        func_name = sym_type
        func_name += str(tokenizer.get_token())
        tokenizer.advance()
        func_name += str(tokenizer.get_token())
        tokenizer.advance()
        tokenizer.advance()

        self.compile_expression_list(tokenizer, func_name, 1)
        return

    def compile_expression_list(
        self, tokenizer: Tokenizer, func_name: str, static: int
    ) -> None:
        n_args = static
        while True:
            token = tokenizer.get_token()

            if token == ")":
                tokenizer.advance()
                break
            elif token == ",":
                tokenizer.advance()
            else:
                n_args += 1
                self.compile_expression(tokenizer)

        self.vm_writer.write_call(func_name, n_args)

    def compile_statement(self) -> None:
        token = self.tokenizer.get_token()
        self.tokenizer.advance()

        if token == "let":
            self.compile_let()
        elif token == "if":
            self.compile_if()
        elif token == "while":
            self.compile_while()
        elif token == "do":
            self.compile_do()
        else:
            self.compile_return()
