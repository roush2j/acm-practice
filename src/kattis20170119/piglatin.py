#!/usr/bin/env python3

# https://open.kattis.com/contests/kozet3/problems/piglatin

import sys
import re

vowels = re.compile('[aeiouy]') # precompiled regex for finding vowels
for line in sys.stdin.readlines():
    
    # iterate over each word in the phrase
    for word in line.split():
        # find first vowel (the problem guarantees there will be at least one)
        first = vowels.search(word).start()        
        # shuffle word and add suffix
        if first == 0: latin = word + 'yay'
        else: latin = word[first:] + word[:first] + 'ay'        
        # print pig latin word followed by a space
        print(latin, end=' ')
    print()