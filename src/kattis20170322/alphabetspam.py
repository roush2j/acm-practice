#!/usr/bin/env python3

# https://open.kattis.com/contests/sk8bsi/problems/alphabetspam

ws = 0
lw = 0
up = 0
sy = 0
for c in input():
    if c == '_': ws += 1
    elif c >= 'a' and c <= 'z': lw += 1
    elif c >= 'A' and c <= 'Z': up += 1
    else: sy += 1

tot = ws + lw + up + sy
print(ws/tot)
print(lw/tot)
print(up/tot)
print(sy/tot)