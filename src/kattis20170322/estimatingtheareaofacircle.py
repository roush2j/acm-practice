#!/usr/bin/env python3

# https://open.kattis.com/contests/sk8bsi/problems/estimatingtheareaofacircle

import sys
from math import pi

for line in sys.stdin:
    radius, ptcnt, incnt = (float(s) for s in line.split())
    if radius == 0 and ptcnt == 0 and incnt == 0: break
    area = pi * radius*radius
    est  =  4 * radius*radius * incnt/ptcnt
    print(area, est)

