import getopt
import sys

def usage():
    print("Usage: [-a|--operand_a <int>] [-b|--operand_b <int>]")
    sys.exit(1)

val1 = 0
val2 = 0

try:
    opts, vals = getopt.getopt(sys.argv[1:], "a:b:", ["operand_a=", "operand_b="])
except getopt.GetoptError as err:
    print(str(err))
    usage()

if len(vals) > 0:
    print("Unknown options: " + str(vals))
    usage()

try:
    for opt, val in opts:
        if opt in ("-a", "--operand_a"):
            val1 = int(val)
        elif opt in ("-b", "--operand_b"):
            val2 = int(val)
except ValueError:
    usage()

print(str(val1 + val2))