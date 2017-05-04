#!/usr/bin/env python3

# https://open.kattis.com/problems/brexit

import sys
import collections

ctycnt, paircnt, homei, leavei = (int(s) for s in sys.stdin.readline().split())

class Country:
    def __init__(self):
        self.curp = 0
        self.partners = []
        
    def __repr__(self):
        return ("%d/(" % self.curp) + ",".join(str(i+1) for i in self.partners) + ")"

countries = [Country() for _ in range(ctycnt)]    

for i in range(paircnt):
    ai, bi = (int(s) - 1 for s in sys.stdin.readline().split())
    ctya, ctyb = countries[ai], countries[bi]
    ctya.partners.append(bi)
    ctya.curp += 1
    ctyb.partners.append(ai)
    ctyb.curp += 1

def removeCountry(ci):
    cty = countries[ci]
    for pi in cty.partners:
        ctyp = countries[pi]
        if ctyp != None: ctyp.curp -= 1
    testqueue.extend(cty.partners)
    countries[ci] = None

testqueue = collections.deque()
removeCountry(leavei-1)
while len(testqueue) > 0:
    ci = testqueue.popleft()
    cty = countries[ci]
    if cty != None and len(cty.partners) >= cty.curp * 2:
        removeCountry(ci)
    
print("leave" if countries[homei-1] == None else "stay")


