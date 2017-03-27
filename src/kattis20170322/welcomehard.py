#!/usr/bin/env python3

# https://open.kattis.com/problems/welcomehard

# This is a variation on the longest-common-subsequence problem
# We cache the number of matches for each pair of offsets (off_query, off_base)
maxbaselen = 500
maxquerylen = 20
matchcnt = [ ([ 0 ] * maxbaselen) for _ in range(maxquerylen) ]

testcnt = int(input())
for testidx in range(testcnt):
    
    base = input()
    query = "welcome to code jam"
    countmod = 10000
    
    for iq in range(len(query)-1, -1, -1):
        mcnt = 0
        for ib in range(len(base)-1, -1, -1):
            if (query[iq] == base[ib]): 
                if iq == len(query)-1: mcnt += 1
                elif ib < len(base)-1: mcnt += matchcnt[iq+1][ib+1]
                mcnt = mcnt % countmod
            matchcnt[iq][ib] = mcnt
        
    print('Case #%d: %04d' % (testidx+1, matchcnt[0][0]))