#!/usr/bin/env python3

# https://open.kattis.com/contests/bmhvsu/problems/kitten

import sys

start = int(sys.stdin.readline())
tree = [-1] * 101
while True:
    nodes = [int(ns) for ns in sys.stdin.readline().split()]
    parent = nodes[0]
    if parent < 0: break
    for n in nodes[1:]: tree[n] = parent

while start != -1:
    print(start, end=' ')
    start = tree[start]