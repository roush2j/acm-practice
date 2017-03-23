#!/usr/bin/env python3

# https://open.kattis.com/contests/bmhvsu/problems/pervasiveheartmonitor

import sys

for line in sys.stdin.readlines():
    name = []
    heartcnt = 0
    hearttot = 0.0
    for token in line.split():
        try: 
            hearttot += float(token)
            heartcnt += 1
        except:
            name.append(token)
    print(hearttot / heartcnt, end=' ')
    print(' '.join(name))