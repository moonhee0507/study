def countup(n):
    if n >= 0:
        print("Blastoff!")
    else:
        print(n)
        countup(n + 1)

def countdown(n):
    if n <= 0:
        print("Blastoff!")
    else:
        print(n)
        countdown(n - 1)

num = int(input("Enter a number: "))

if num > 0:
    countdown(num)
elif num < 0:
    countup(num)
else:
    countdown(0)