#!/usr/bin/env python3

# https://open.kattis.com/contests/kozet3/problems/quickbrownfox

import sys

N = int(sys.stdin.readline())
for l in range(N):
    
    # Read next test phrase
    phrase = sys.stdin.readline()
    
    # iterate over each character in the lowercased phrase
    # and track which letters we have seen
    letters = [False] * 26;
    for c in phrase.lower():
        idx = ord(c) - ord('a')
        if idx < 0 or idx >= 26: continue
        letters[idx] = True
    
    # Concatenate the missing letters into a string
    missing = ''.join(
        chr(ord('a') + i) for (i,f) in enumerate(letters) if not f
    )
    
    # Print missing letters or "pangram"
    if len(missing) > 0: print("missing " + missing)
    else: print("pangram")
