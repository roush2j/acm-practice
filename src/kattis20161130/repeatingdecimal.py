#!/usr/bin/env python3

# https://open.kattis.com/contests/kpwqcx/problems/repeatingdecimal

import sys

# iterate over inputs
for line in sys.stdin.readlines():
    numerator, denominator, count = (int(s) for s in line.split())
    
    # explicit long division, one digit at a time
    out = "0."
    for i in range(0,count):
        numerator *= 10
        out += str(numerator // denominator)
        numerator = numerator % denominator
    print(out) 
