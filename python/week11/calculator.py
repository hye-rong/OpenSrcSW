class Stack:
    def __init__(self):
        self.BOX = []
    def push(self, item):
        self.BOX.append(item)
    def pop(self):
        return self.BOX.pop()
    def peek(self):
        n = len(self.BOX)
        tmp = self.BOX[n - 1]
        return tmp
    def isEmpty(self):
        return self.BOX==[]
    def printStack(self):
        print("-----스택상태-----")
        for i in self.BOX:
            print(i)
        print("-----------------")

class Calc:
    def __init__(self):
        self.num = Stack()
        self.op = Stack()
    def calc(self, a, b, p):
        if p == "*":
            return b * a
        elif p == "/":
            return b / a
        elif p == "+":
            return b + a
        elif p == "-":
            return b - a

    def play(self):
        i = input("계산식 입력:")
        l = i.split()
        for item in l:
            if item.isdigit():
                self.num.push(int(item))
            else:
                if self.op.isEmpty():
                    self.op.push(item)
                else:
                    while not self.op.isEmpty():

                        if self.op.peek() == "*" or self.op.peek() == "/":
                            self.num.push(self.calc(self.num.pop(), self.num.pop(), self.op.pop()))
                        else:
                            if item == "*" or item == "/":
                                break
                            else:
                                self.num.push(self.calc(self.num.pop(), self.num.pop(), self.op.pop()))
                    self.op.push(item)
        while not self.op.isEmpty():
            self.num.push(self.calc(self.num.pop(), self.num.pop(), self.op.pop()))
        print("---결과----")
        print(self.num.pop())





