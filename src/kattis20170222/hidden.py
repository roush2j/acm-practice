#!/usr/bin/env python3

# https://open.kattis.com/contests/annky6/problems/hidden

import sys

password, msg = sys.stdin.readline().split()
for c in msg:
    if c in password:
        if password[0] == c: password = password[1:]
        else: break
    
print("PASS" if len(password) == 0 else "FAIL")