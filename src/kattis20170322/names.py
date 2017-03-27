#!/usr/bin/env python3

# https://open.kattis.com/contests/sk8bsi/problems/names

# Note that we can only add to then *end* of the string, not insert in the middle.  
# Thus there can be at most one node at each level in the search tree that adds
# a character.
# hint: try xyzabcbzyx (should be 2)

def palModDist(s):
    '''Returns the number of characters that must be modified to make s a palindrome'''
    return sum(1 for i in range(len(s)//2) if s[i] != s[-i-1])

name = input()
mindist = len(name) // 2
for i in range(len(name)//2):
    mindist = min(mindist, i + palModDist(name[i:]))

print(mindist)

