#!/usr/bin/env python3

# https://open.kattis.com/contests/sk8bsi/problems/welcomeeasy

def matchcnt(base, query):
    if len(query) == 0: return 1
    elif len(base) == 0: return 0
    else:
        cnt = matchcnt(base[1:],query)
        if base[0] == query[0]: cnt += matchcnt(base[1:],query[1:])
        return cnt

testcnt = int(input())
for testidx in range(testcnt):
    base = input()
    query = "welcome to code jam"
    print('Case #%d: %04d' % (testidx+1, matchcnt(base, query) % 10000))