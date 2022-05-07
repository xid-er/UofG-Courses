import sys

def usage():
    print('Usage: <int> <int>')
    sys.exit(1)

val1 = 0
val2 = 0

if len(sys.argv) != 3:
    print('Wrong number of arguments')
    usage()
    
try:
    val1 = int(sys.argv[1])
    val2 = int(sys.argv[2])
except ValueError:
    print('Argument is not an int')
    usage()
    
print('Sum: ' + str(val1 + val2))