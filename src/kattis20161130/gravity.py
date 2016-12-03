#!/usr/bin/env python3

# https://open.kattis.com/contests/kpwqcx/problems/gravity

import sys
    
# iterate over test cases
while True:
    rows, cols = (int(s) for s in sys.stdin.readline().split())
    if rows == 0 or cols == 0: break
    
    # read in the starting grid, count number of non-empty spaces
    mov = [[]]*rows
    movcnt = 0
    for r in range(rows):
        mov[r] = list(sys.stdin.readline())[0:cols]
        for letter in mov[r]: 
            if letter != ' ': movcnt += 1
    
    # initialize grid of stuck letters and queue of newly stuck letter positions
    stuck = [[' ']*cols for _ in range(rows)]
    newstuck = []
    def makestuck(r,c):
        global movcnt
        if mov[r][c] == ' ': return
        stuck[r][c] = mov[r][c]
        mov[r][c] = ' '
        movcnt -= 1
        newstuck.append((r,c))
          
    # iterate until all letters are stuck
    while movcnt > 0:
        
        # stick letters on the bottom row or which have stuck neighbors
        for r in range(rows):
            for c in range(cols): 
                if r == rows - 1: makestuck(r, c)
                elif stuck[r+1][c] != ' ': makestuck(r, c)
                elif c > 0 and stuck[r][c-1] != ' ': makestuck(r, c)
                elif c < cols-1 and stuck[r][c+1] != ' ': makestuck(r, c)
        
        # enumerate all newly-stuck letters in depth-first search
        #  and add unstuck neighbors to newly stuck queue
        while len(newstuck) > 0:
            r, c = newstuck.pop()
            if r > 0:        makestuck(r-1, c)
            if r < rows - 1: makestuck(r+1, c)
            if c > 0:        makestuck(r, c-1)
            if c < cols - 1: makestuck(r, c+1)
            
        # advance by 1s, so that all unstuck letters move down one row
        # we achieve this compactly by popping the last row and inserting at the top
        mov.insert(0, mov.pop())
      
    # print final result for this test case
    print('\n'.join(''.join(row) for row in stuck))
    print()