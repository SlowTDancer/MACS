import math

def sum(lst):
    ans = 0
    for elem in lst:
        if not isinstance(elem, float):
            print("invalid arguments for sum!!!")
            exit()
        ans += elem
    return ans

def subtract(lst):
    if len(lst) == 0:
        print("not enough argumets for subtraction!!!")
        exit()
    if not isinstance(lst[0], float):
            print("invalid arguments for subtraction!!!")
            exit()
    ans = lst[0]
    if len(lst) == 1:
        return -ans
    for elem in lst[1:]:
        if not isinstance(elem, float):
            print("invalid arguments for subtraction!!!")
            exit()
        ans -= elem
    return ans

def multiply(lst):
    ans = 1
    for elem in lst:
        if not isinstance(elem, float):
            print("invalid arguments for multiplication!!!")
            exit()
        ans *= elem
    return ans

def divide(lst):
    if len(lst) == 0:
        print("not enough arguments for division!!!")
        exit()
    if not isinstance(lst[0], float):
            print("invalid arguments for division!!!")
            exit()
    ans = lst[0]
    if len(lst) == 1:
        return 1/ans
    for elem in lst[1:]:
        if not isinstance(elem, float):
            print("invalid arguments for division!!!")
            exit()
        ans /= elem
    return ans

def AND(lst):
    ans = True
    if len(lst) == 0:
        return ans
    for elem in lst:
        if not isinstance(elem, bool):
            print("invalid arguments for and operation!!!")
            exit()
        ans = ans and elem
    return ans

def OR(lst):
    ans = False
    if len(lst) == 0:
        return ans
    for elem in lst:
        if not isinstance(elem, bool):
            print("invalid arguments for or operation!!!")
            exit()
        ans = ans or elem
    return ans

def NOT(lst):
    if len(lst) != 1:
        print("incorrect amount of arguments for not opeartion!!!")
        exit()
    if not isinstance(lst[0], bool):
        print("invalid arguments for not operation!!!")
        exit()
    return not lst[0]

def even(lst):
    if len(lst) != 1:
        print("incorrect amount of arguments for even? opeartion!!!")
        exit()
    if not isinstance(lst[0], float):
        print("invalid arguments for even? operation!!!")
        exit()
    return (lst[0] % 2 == 0)

def odd(lst):
    if len(lst) != 1:
        print("incorrect amount of arguments for odd? opeartion!!!")
        exit()
    if not isinstance(lst[0], float):
        print("invalid arguments for odd? operation!!!")
        exit()
    return (lst[0] % 2 == 1)

def POWER(lst):
    if len(lst) != 2:
        print("incorrect amount of arguments for expt opeartion!!!")
        exit()
    if (not isinstance(lst[0], float)) or (not isinstance(lst[1], float)):
        print("invalid arguments for expt operation")
        exit()
    return lst[0] ** lst[1]

def zero(lst):
    if len(lst) != 1:
        print("incorrect amount of arguments for zero? opeartion!!!")
        exit()
    if not isinstance(lst[0], float):
        print("invalid arguments for zero? operation!!!")
        exit()
    return (lst[0] == 0)

def equal(lst):
    if len(lst) != 2:
        print("incorrect amount of arguments for equal? opeartion!!!")
        exit()
    return lst[0] == lst[1]

def negative(lst):
    if len(lst) != 1:
        print("incorrect amount of arguments for negative? opeartion!!!")
        exit()
    if not isinstance(lst[0], float):
        print("invalid arguments for negative? operation!!!")
        exit()
    return lst[0] < 0

def positive(lst):
    if len(lst) != 1:
        print("incorrect amount of arguments for positive? opeartion!!!")
        exit()
    if not isinstance(lst[0], float):
        print("invalid arguments for positive? operation!!!")
        exit()
    return lst[0] > 0

def neq(lst):
    if len(lst) < 2:
        print("incorrect amount of arguments for = opeartion!!!")
        exit()
    if not isinstance(lst[0], float):
        print("invalid arguments for = operation!!!")
        exit()
    flag = lst[0]
    for elem in lst[1:]:
        if not isinstance(elem, float):
            print("invalid arguments for = operation!!!")
            exit()
        if elem != flag:
            return False
        flag = elem
    return True

def nlt(lst):
    if len(lst) < 2:
        print("incorrect amount of arguments for < opeartion!!!")
        exit()
    if not isinstance(lst[0], float):
        print("invalid arguments for < operation!!!")
        exit()
    flag = lst[0]
    for elem in lst[1:]:
        if not isinstance(elem, float):
            print("invalid arguments for < operation!!!")
            exit()
        if elem <= flag:
            return False
        flag = elem
    return True

def ngt(lst):
    if len(lst) < 2:
        print("incorrect amount of arguments for > opeartion!!!")
        exit()
    if not isinstance(lst[0], float):
        print("invalid arguments for > operation!!!")
        exit()
    flag = lst[0]
    for elem in lst[1:]:
        if not isinstance(elem, float):
            print("invalid arguments for > operation!!!")
            exit()
        if elem >= flag:
            return False
        flag = elem
    return True

def nle(lst):
    if len(lst) < 2:
        print("incorrect amount of arguments for <= opeartion!!!")
        exit()
    if not isinstance(lst[0], float):
        print("invalid arguments for <= operation!!!")
        exit()
    flag = lst[0]
    for elem in lst[1:]:
        if not isinstance(elem, float):
            print("invalid arguments for <= operation!!!")
            exit()
        if elem < flag:
            return False
        flag = elem
    return True

def nge(lst):
    if len(lst) < 2:
        print("incorrect amount of arguments for >= opeartion!!!")
        exit()
    if not isinstance(lst[0], float):
        print("invalid arguments for >= operation!!!")
        exit()
    flag = lst[0]
    for elem in lst[1:]:
        if not isinstance(elem, float):
            print("invalid arguments for >= operation!!!")
            exit()
        if elem > flag:
            return False
        flag = elem
    return True

def SQRT(lst):
    if len(lst) != 1:
        print("incorrect amount of arguments for sqrt opeartion!!!")
        exit()
    if not isinstance(lst[0], float):
        print("invalid arguments for sqrt operation!!!")
        exit()
    return math.sqrt(lst[0])

def MIN(lst):
    if len(lst) < 1:
        print("incorrect amount of arguments for min operation!!!")
        exit()
    if not isinstance(lst[0], float):
        print("invalid arguments for min operation!!!")
        exit()
    ans = lst[0]
    for elem in lst[1:]:
        if not isinstance(elem, float):
            print("invalid arguments for min operation!!!")
            exit()
        ans = min(ans, elem)
    return ans

def MAX(lst):
    if len(lst) < 1:
        print("incorrect amount of arguments for max operation!!!")
        exit()
    if not isinstance(lst[0], float):
        print("invalid arguments for max operation!!!")
        exit()
    ans = lst[0]
    for elem in lst[1:]:
        if not isinstance(elem, float):
            print("invalid arguments for max operation!!!")
            exit()
        ans = max(ans, elem)
    return ans

def quotient(lst):
    if len(lst) != 2:
        print("incorrect amount of arguments for quotient operation!!!")
        exit()
    if (not isinstance(lst[0], float)) or (not isinstance(lst[1], float)):
        print("invalid arguments for quotient operation!!!")
        exit()
    ans = lst[0] / lst[1]
    if ans < 0:
        return float(math.ceil(ans))
    return float(math.floor(ans))


def remainder(lst):
    if len(lst) != 2:
        print("incorrect amount of arguments for remainder operation!!!")
        exit()
    if (not isinstance(lst[0], float)) or (not isinstance(lst[1], float)):
        print("invalid arguments for remainder operation!!!")
        exit()
    ans = lst[0] % lst[1]
    if ans < 0:
        if lst[1] < 0:
            return -lst[1] + ans
        return lst[1] + ans
    return ans

def rev(lst):
    if not isinstance(lst[0], list):
        print("invalid arguments for reverse operation!!!")
        exit()
    temp = lst[0][1:]
    temp = temp[::-1]
    return ['lwjahnbdarqszvnyajzv'] + temp

def islist(lst):
    return isinstance(lst[0], list)

def cdr(lst):
    if not isinstance(lst[0], list):
        print("invalid arguments for cdr operation!!!")
        exit()
    if len(lst[0]) == 1:
        print("out of bound exception for cdr operation!!!")
    return [lst[0][0]] + lst[0][2:]

def car(lst):
    if not isinstance(lst[0], list):
        print("invalid arguments for car operation!!!")
        exit()
    if len(lst[0]) <= 1:
        print("null pointer exception for car operation!!!")
    return lst[0][1]

def cons(lst):
    if len(lst) != 2:
        print("incorrect ammount of argumerts for cons operation!!!")
        exit()
    if not isinstance(lst[1], list):
        print("can't cons without a list!!!")
        exit()
    return ['lwjahnbdarqszvnyajzv', lst[0]] + lst[1][1:]

def append(lst):
    if len(lst) < 1:
        print("incorrect ammount of argumerts for append operation!!!")
        exit()
    ans = ['lwjahnbdarqszvnyajzv']
    for elem in lst:
        if not isinstance(elem, list):
            print("can't append without a list!!!")
            exit()
        ans += elem[1:]
    return ans

def null(lst):
    if not isinstance(lst[0], list):
        return False
    return len(lst[0]) == 1

def IF(lst):
    if len(lst) != 1:
        print("incorrect amount of arguments for if operation!!!")
        exit()
    if not isinstance(lst[0], bool):
        print("invalid arguments for if operation!!!")
        exit()
    return lst[0]

def SIZE(lst):
    if not isinstance(lst, list):
        print("invalid arguments for length operation!!!")
        exit()
    return float(len(lst[0]) - 1)

def quote(lst):
    return ['lwjahnbdarqszvnyajzv'] + lst

def cadr(lst):
    if not isinstance(lst[0], list):
        print("invalid arguments for car operation!!!")
        exit()
    if len(lst[0]) <= 2:
        print("null pointer exception for car operation!!!")
    return lst[0][2]