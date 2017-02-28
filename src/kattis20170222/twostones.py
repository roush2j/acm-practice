#!/usr/bin/env python3

# https://open.kattis.com/contests/annky6/problems/twostones

import sys

line = sys.stdin.readline().strip()
cnt = int(line)
print('Bob' if cnt % 2 == 0 else 'Alice')