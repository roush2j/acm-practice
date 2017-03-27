#!/usr/bin/env python3

# https://open.kattis.com/contests/sk8bsi/problems/averageseasy

# This solution also works for the "hard" version
#   https://open.kattis.com/problems/averageshard

testcnt = int(input())
for testidx in range(testcnt):
    _ = input() # blank line
    
    # read in iqs, compute averages
    numcs, numec = (int(s) for s in input().split())
    avgcs, avgec = (0, 0)
    iqcs = []
    numiq = 0
    while numiq < numcs + numec:
        for iq in (int(s) for s in input().split()):
            if numiq < numcs:
                avgcs += iq
                iqcs.append(iq)
            else:
                avgec += iq
            numiq += 1
    
    # count cs students who meet criteria
    avgcs, avgec = (avgcs/numcs, avgec/numec)
    print( sum(1 for iq in iqcs if iq < avgcs and iq > avgec) )