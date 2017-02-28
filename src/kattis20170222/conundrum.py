#!/usr/bin/env python3

# https://open.kattis.com/contests/annky6/problems/conundrum

import sys

key = "PER"
line = sys.stdin.readline().strip()
repcnt = 0
for i,c in enumerate(line):
    if c != key[i % len(key)]: repcnt += 1
print(repcnt)