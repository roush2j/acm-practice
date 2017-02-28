#!/usr/bin/env python3

# https://open.kattis.com/contests/kozet3/problems/nnnnn

import sys
import math

def f(x, lx):
    ll = math.log10(x)
    lll = math.log(ll) if ll > 1 else 0
    return math.floor(x - ll + lll + 0.5)

for i in range(17):
    N = 10**i
    for dn in [-3,-2,-1,0,1,2,3]:
        n = N + dn
        lenn = len(str(n))
        l = n * lenn
        lenl = len(str(l))
        logl = math.log10(l) if l > 1 else 0
        print('%20d (%3d)  %20d (%3d, %6.3f)' % (n, lenn, l, lenl, logl))
    