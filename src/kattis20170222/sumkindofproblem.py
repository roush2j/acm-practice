#!/usr/bin/env python3

# https://open.kattis.com/contests/annky6/problems/sumkindofproblem

import sys

P = int(sys.stdin.readline().strip())
for line in sys.stdin.readlines():
    K, N = (int(s) for s in line.split())
    allsum = N * (N + 1) / 2
    oddsum = N * N
    evensum = N * (N + 1)
    print('%d %d %d %d' % (K, allsum, oddsum, evensum))