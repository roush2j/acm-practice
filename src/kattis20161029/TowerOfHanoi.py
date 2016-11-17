#!/usr/bin/env python3

# The Tower of Hanoi problem is to move a complete set of disks from source to 
# destination peg, with one spare peg in the middle, without any peg every having
# two disks in the 'wrong' order.
# The solution takes 2^N - 1 steps and goes like so:
#   1. Recursively move the top N-1 disks from the source peg to the *spare* peg.
#      This means the 'spare' temporarily becomes the 'destination' and vice-versa.
#      (2^(N-1) - 1 steps)
#   2. Move the bottom disk to the destination peg (1 step)
#   3. Recursively move the top N-1 disks from the spare peg to the destination peg.
#      This means the 'spare' temporarily becomes the 'source' and vice-versa.
#      (2^(N-1) - 1) steps)
# Note that the bottom disk in (2) doesn't interfere with recursive 
# movements (1) and (3) - in fact the bottom disk can be on any peg without
# interfering with the free movement of the disks above it.

import sys

# parse list of disks on each peg, one peg per line
# the first value for each peg is the number of disks; it is redundant and discarded
pegs = []
for pegidx in range(0,3):
    line = sys.stdin.readline()
    peg = [int(v) for i,v in enumerate(line.split()) if i > 0]
    pegs.append(peg)

# the number of pegs and the total number of steps to solve completely
diskcnt = sum([len(p) for p in pegs])
steprem = 2**diskcnt - 1
# initial bindings for source, spare, and destination pegs
src, spr, dst = pegs

# At any time the biggest disk must be the first disk on the source or destination peg.
# If it is on the source peg then we are still somewhere in the first (2^(N-1) - 1) steps.
# If it is on the destination peg then we are somewhere in the last (2^(N-1) - 1) steps.
# This is true recursively as we consider the smaller pegs.
# If at any point the biggest remaining disk is not on the 'source' or 'destination'
# peg then the tower state is not part of the optimal solution.
while diskcnt > 0:
    if len(src) > 0 and src[0] == diskcnt:
        diskcnt -= 1
        src.pop(0)
        spr, dst = dst, spr
    elif len(dst) > 0 and dst[0] == diskcnt:
        diskcnt -= 1
        steprem -= 2**diskcnt
        dst.pop(0)
        src, spr = spr, src
    else:
        break
        
if diskcnt == 0:
    print(steprem)
else:
    print('No')
        