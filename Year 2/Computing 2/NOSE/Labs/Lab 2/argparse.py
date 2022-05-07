import argparse
import sys

val1 = 0
val2 = 0

ap = argparse.ArgumentParser()
ap.add_argument("-a", "--operand_a", type=int, default=val1)
ap.add_argument("-b", "--operand_b", type=int, default=val2)
args = vars(ap.parse_args())

print(str(int(args["operand_a"]) + int(args["operand_b"])))