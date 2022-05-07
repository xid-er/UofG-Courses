def divisible(dividor, num):
    return num % dividor == 0

def fizzbuzz(x, y, n):
    for i in range(1,n):
        if(divisible(x * y, i)):
            print("Fizzbuzz")
        elif(divisible(x, i)):
            print("fizz")
        elif(divisible(y, i)):
            print("buzz")
        else:
            print(i)           

fizzbuzz(2,5,100)