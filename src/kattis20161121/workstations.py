#!/usr/bin/env python3

# https://open.kattis.com/problems/workstations

# Credit for this algorithm goes to Joe Byrd and Richard Sivak

import sys
import heapq as hq

# read input
ucnt, timeout = map(int, sys.stdin.readline().split())
users = [()] * ucnt
for i in range(0,ucnt):
    start, time = map(int, sys.stdin.readline().split())
    users[i] = (start, start + time)

# sort by start time, with end time breaking ties
# sort in reversed order so we can pop off of end (which is faster)
users.sort(reverse=True) 

# greedy algorithm that assigns sessions to the oldest open slot
expired = 0     # number of sessions expired
sessions = []   # priority queue of open sessions in order of end time
while len(users) > 0:
    u = users.pop() # next session to start
    while len(sessions) > 0 and sessions[0] + timeout < u[0]:
        hq.heappop(sessions) # discard expired sessions
        expired += 1
    if len(sessions) > 0 and sessions[0] <= u[0]:
        hq.heapreplace(sessions, u[1]) # continue existing session
    else:
        hq.heappush(sessions, u[1]) # start new session
    
print(ucnt - expired - len(sessions))