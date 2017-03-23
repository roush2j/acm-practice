#!/usr/bin/env python3

# https://open.kattis.com/contests/bmhvsu/problems/sodasurpler

import sys

empty, found, cost = (int(x) for x in sys.stdin.readline().split())
total = empty + found
if total > 0: print((total - 1) // (cost - 1))
else: print(0)