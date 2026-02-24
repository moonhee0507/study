num1 = int(input("Enter the first number: "))
num2 = int(input("Enter the second number: "))

try:
    print("Result:", num1 / num2)
except ZeroDivisionError:
    print("Error: You attempted to divide by zero.")