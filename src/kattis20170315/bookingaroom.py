#!/usr/bin/env python3

# https://open.kattis.com/contests/bmhvsu/problems/bookingaroom

import sys

roomcnt, roomtaken = (int(x) for x in sys.stdin.readline().split())
rooms = [False] * roomcnt
for r in sys.stdin.readlines():
    rooms[int(r) - 1] = True
    
if roomtaken == roomcnt: 
    print("too late")
else:
    print([i+1 for i,taken in enumerate(rooms) if not taken][0])
