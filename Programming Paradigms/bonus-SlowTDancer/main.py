import re
import functions

class iterator():
    def __init__(self, tokens, pos = 0):
        self.tokens = tokens
        self.pos = pos
    
    def next(self):
        self.pos += 1
        return self.tokens[self.pos - 1]
    
    def peek(self):
        if  len(self.tokens) <= self.pos:
            return None
        else:
            return self.tokens[self.pos]

def ccg(str):
    count = 0
    for ch in str:
        if ch == '(':
            count += 1
        if ch == ")":
            count -= 1
        if count < 0:
            return True
    if count != 0:
        return True
    return False

def tokenize(input):
    tokens = ["("]
    temp = re.compile(r"""[\s,]*(~@|[\[\]{}()'`~^@]|"(?:[\\].|[^\\"])*"?|;.*|[^\s\[\]{}()'"`@,;]+)""");
    tokens += [t for t in re.findall(temp, input) if t[0] != ';']
    tokens.append(")")
    return tokens

def read_list(it):
    ans = []
    it.next()
    token = it.peek()
    while token != ')':
        ans.append(build(it))
        token = it.peek()
    it.next()
    return ans

def read_atom(it):
     token = it.next()
     if re.match(r'^-?\d+(?:\.\d+)?$', token):
         return float(token)
     elif token == '#t':
         return True
     elif token == '#f':
         return False
     elif token == "nil":
         return None
     else:
        return str(token)

def build(it):
    curr = it.peek()
    if curr[0] == '(':
        return read_list(it)
    return read_atom(it)

def save(data):
    ans = ""
    check = False
    cnt = 0
    for ch in data:
        if ch == "'":
            ans += "'("
            check = True
        elif check and ch == '(' and cnt > 0:
            ans += "'("
            cnt += 1
        elif check and ch == '(':
            cnt += 1
        elif check and ch == ')':
            ans += ')'
            cnt -= 1
            if cnt == 0:
                check = False
        else:
            ans += str(ch)   
    return ans

def read(input):
    if ccg(input):
        print("invalid input!!! check parantheses")
        exit()
    tokens = tokenize(input)
    tree = build(iterator(tokens))
    return tree

def correct(data):
    ans = []
    index = 0
    while index < len(data):
        if isinstance(data[index], list):
            ans.append(correct(data[index]))
        else:
            if data[index] == "'":
                index += 1
                ans.append(['lwjahnbdarqszvnyajzv'] + correct(data[index]))
      
            else:
                ans.append(data[index])
        index += 1
    return ans

def execute_helper(lst, change, dic):
    i = 0
    temp = lst.copy()
    while i < len(temp):
        if isinstance(temp[i], list):
            temp[i] = execute_helper(temp[i], change, dic)
        elif dic.get(temp[i], 'samira') != 'samira':
            temp[i] = dic.get(temp[i], 'samira')
        i += 1
    return temp

def execute(fnc, args, ops, def_ops, def_cos):
    index = 0
    dic ={}
    fnc[0]
    while index < len(args):
        if not isinstance(fnc[0][index], list):
            dic[fnc[0][index]] = args[index]
        index += 1
    ans = execute_helper(fnc[1], fnc[0], dic)
    return eval_helper(ans, ops, def_ops, def_cos)

def eval_helper(lst, ops, def_ops, def_cos):
    if isinstance(lst, list):
        if len(lst) == 0:
            return []
        if (not isinstance(lst[0], list)) and def_cos.get(lst[0], "samira") != "samira":
            lst[0] = def_cos[lst[0]]
        if (not isinstance(lst[0], list)) and (ops.get(lst[0], "samira") == "samira" and def_ops.get(lst[0], "samira") == 'samira'):
            print(str(lst[0]) + " function doesn't exists!!!")
            exit()
        elif isinstance(lst[0], list) and lst[0][0] == 'lambda':
            args = lst[0][1:]
            if len(args) != 2:
                print("incorrect amount of arguments for lambda operation!!!")
                exit()
            arg = args[0]
            func = args[1]
            return execute([arg, func], lst[1:], ops, def_ops, def_cos)
        elif lst[0] == 'map':
            ans = ['lwjahnbdarqszvnyajzv']
            if len(lst[1:]) < 2:
                print("incorrect amount of arguments for map operation!!!")
                exit()
            func = lst[1]
            args = lst[2:]
            index = 0
            while index < len(args):
                temp = eval_helper(args[index], ops, def_ops, def_cos)
                if not isinstance(temp, list):
                    print("invalid arguments for map operation!!!")
                    exit()
                args[index] = temp[1:]
                index += 1
            index = 0
            sz = 1000000
            for elem in args:
                if sz > len(elem):
                    sz = len(elem)
            while index < sz:
                tmp = [func]
                for elem in args:
                    tmp.append(elem[index])
                ans.append(eval_helper(tmp, ops, def_ops, def_cos))
                index += 1
            return ans
        elif lst[0] == 'eval':
            curr = eval_helper(lst[1], ops, def_ops, def_cos)
            if isinstance(curr, list) and curr[0] == 'lwjahnbdarqszvnyajzv':
                curr = curr[1:]
            return eval_helper(curr, ops, def_ops, def_cos)
        elif lst[0] == 'apply':
            func = lst[1]
            args = lst[2]
            args = eval_helper(args, ops, def_ops, def_cos)
            args = args[1:]
            temp = []
            index = 0
            while index < len(args):
                if isinstance(args[index], list):
                    temp += [args[index]]
                else:
                    temp +=[args[index]]
                index += 1
            toDo = [func] + temp
            return eval_helper(toDo, ops, def_ops, def_cos)
        elif lst[0] == 'define':
            if len(lst[1:]) != 2:
                print("incorrect amount of arguments for define operation!!!")
                exit()
            if isinstance(lst[1], list):
                if len(lst[1]) == 0:
                    print("incorrect amount of arguments for define operation!!!")
                    exit()
                name = lst[1][0]
                args = lst[1][1:]
                func = lst[2]
                def_ops[name] = [args, func]
            else:
                def_cos[lst[1]] = eval_helper(lst[2], ops, def_ops, def_cos)
            return ""
        elif (not isinstance(lst[0], list)) and def_ops.get(lst[0], 'samira') != 'samira':
            curr = def_ops.get(lst[0], 'samira')
            args = lst[1:]
            return execute(curr, args, ops, def_ops, def_cos)
        elif lst[0] == 'and':
            args = lst[1:]
            for elem in args:
                elem = eval_helper(elem, ops, def_ops, def_cos)
                if not isinstance(elem, bool):
                    print("invalid arguments for and operation!!!")
                    exit()
                if not elem:
                    return False
            return True
        elif lst[0] == 'or':
            args = lst[1:]
            for elem in args:
                elem = eval_helper(elem, ops, def_ops, def_cos)
                if not isinstance(elem, bool):
                    print("invalid arguments for or operation!!!")
                    exit()
                if elem:
                    return True
            return False
        elif lst[0] == 'if':
            args = lst[1:]
            if len(args) != 3:
                print("incorrect amount of arguments for if operation!!!")
                exit()
            check = eval_helper(args[0], ops, def_ops, def_cos)
            if not isinstance(check, bool):
                print("invalid arguments for if operation!!!")
                exit()
            if check:
                return eval_helper(args[1], ops, def_ops, def_cos)
            return eval_helper(args[2], ops, def_ops, def_cos)
        elif lst[0] == "load":
            if len(lst) != 2:
                print("incorrect amount of arguments for load operation!!!")
                exit()
            file = open(lst[1], "r")
            data = ""
            for line in file.readlines():
                if line == "" or line == "\n": 
                    continue
                if line == "(exit)": 
                    break
                line = save(line)
                data += line
            s = correct(read(data))
            ans = eval(s, ops, def_ops, def_cos)
            return ""
        elif lst[0] == "display":
            ans = eval(lst[1:], ops, def_ops, def_cos)
            if len(ans) != 1:
                print("'display' wrong number of arguments")
                exit()
            toPrint = output(ans)
            if len(toPrint) > 0: 
                print(toPrint)
            return ""
        elif lst[0] == 'list':
            ans = ['lwjahnbdarqszvnyajzv']
            for elem in lst[1:]:
                ans.append(eval_helper(elem, ops, def_ops, def_cos))
            return ans  
        else:
            operator = lst[0]
            args = []
            index = 1
            while index < len(lst):
                if isinstance(lst[index], list) and len(lst[index]) > 0 and lst[index][0] == 'lwjahnbdarqszvnyajzv':
                    args.append(lst[index])
                    index += 1
                    continue
                args.append(eval_helper(lst[index], ops, def_ops, def_cos))
                index += 1
            return ops[operator](args)
    if (not isinstance(lst, list)) and def_cos.get(lst, "samira") != "samira":
        lst = def_cos[lst]
    return lst

def eval(data, ops, def_ops, def_cos):
    lst = []
    index = 0
    while index < len(data):
        lst.append(eval_helper(data[index], ops, def_ops, def_cos))
        index += 1
    return lst


def checker(data):
    ans = ""
    if len(data) == 0:
        ans = " "
    for elem in data:
        if isinstance(elem, list):
            ans += '('
            ans += checker(elem)
            ans = ans[: -1]
            ans += ') '
        elif elem == False and (not isinstance(elem, float)):
            ans += "#f "
        elif elem == True and (not isinstance(elem, float)):
            ans += "#t "
        elif elem == None:
            ans += "nil "
        else:
            ans += str(elem)
            ans += " "
    return ans

def output(ans):
    s = ""
    if len(ans) == 0:
        return ""
    if isinstance(ans[0], list) and len(ans[0]) > 0 and ans[0][0] == 'define':
        return ""
    for elem in ans:
        if isinstance(elem, bool):
            if elem:
                s += '#t '
            else:
                s += '#f '
        elif isinstance(elem, list):
            s += '('
            s += output(elem)
            s += ') '
        elif isinstance(elem, float) and elem % 1 == 0:
            s += str(int(elem))
            s += " "
        elif elem == None:
            s += "nil "
        else:
            if elem == 'lwjahnbdarqszvnyajzv':
                continue
            s += str(elem)
            s += " "
    s = s[:-1]
    return s
    

def main():
    ops = {
        '+' : (lambda lst: functions.sum(lst)),
        '-' : (lambda lst: functions.subtract(lst)),
        '*' : (lambda lst: functions.multiply(lst)),
        '/' : (lambda lst: functions.divide(lst)),
        'and' : (lambda lst: functions.AND(lst)),
        'or' : (lambda lst: functions.OR(lst)),
        'length' : (lambda lst: functions.SIZE(lst)),
        'not' : (lambda lst: functions.NOT(lst)),
        'min' : (lambda lst: functions.MIN(lst)),
        'max' : (lambda lst: functions.MAX(lst)),
        'if' : (lambda lst: functions.IF(lst)),
        'else' : True,
        'sqrt' : (lambda lst: functions.SQRT(lst)),
        'reverse' : (lambda lst: functions.rev(lst)),
        'pow' : (lambda lst: functions.POWER(lst)),
        'even?' : (lambda lst: functions.even(lst)),
        'odd?' : (lambda lst: functions.odd(lst)),
        'quotient' : (lambda lst: functions.quotient(lst)),
        'remainder' : (lambda lst: functions.remainder(lst)),
        'negative?' : (lambda lst: functions.negative(lst)),
        'positive?' : (lambda lst: functions.positive(lst)),
        'list?' : (lambda lst: functions.islist(lst)),
        'zero?' : (lambda lst: functions.zero(lst)),
        'equal?' : (lambda lst: functions.equal(lst)),
        'null?' : (lambda lst: functions.null(lst)),
        'apply' : (lambda lst: functions.apply(lst)),
        'eval' : (lambda lst: functions.eval(lst)),
        'cdr' : (lambda lst: functions.cdr(lst)),
        'car' : (lambda lst: functions.car(lst)),
        'cons' : (lambda lst: functions.cons(lst)),
        'append' : (lambda lst: functions.append(lst)),
        'map' : (),
        '=' : (lambda lst: functions.neq(lst)),
        '<' : (lambda lst: functions.nlt(lst)),
        '>' : (lambda lst: functions.ngt(lst)),
        '<=' : (lambda lst: functions.nle(lst)),
        '>=' : (lambda lst: functions.nge(lst)),
        'define' : (),
        "'": (),
        'lambda' : (),
        'lwjahnbdarqszvnyajzv' : (lambda lst: functions.quote(lst)),
        'list' : (lambda lst: functions.quote(lst)),
        'cadr' : (lambda lst: functions.cadr(lst)),
        'load' : (),
        'display' : ()
    }
    def_ops = {}
    def_cos = {}
    counter = 1
    while True:
        data = input("#|kawa:" + str(counter) + "|# ")
        if data == "(exit)":
            break
        data = save(data)
        s = correct(read(data))
        #print(checker(s))
        ans = eval(s, ops, def_ops, def_cos)
        toPrint = output(ans)
        if len(toPrint) > 0: 
            print(toPrint)
        counter += 1

if __name__ == '__main__':
    main()