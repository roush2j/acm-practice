#!/usr/bin/env python3

# https://open.kattis.com/contests/bmhvsu/problems/numbertree

import sys

line = sys.stdin.readline().split()
height = int(line[0])
path = '' if len(line) == 1 else line[1]
idx = 1
for h,c in enumerate(path):
    idx = idx << 1
    if c == 'R': idx = idx | 1

print(2**(height+1) - idx)
