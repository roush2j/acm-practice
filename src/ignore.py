#!/usr/bin/env python3
# author: Brian Bauman
import sys

arr = ['0','1','2','5','9','8','6']

for x in sys.stdin:
	x = int(x)
	output = ""
	mod7 = 0
	i = 1
	while (x > 0):
		mod7 = (x%7)
		x = int(x/7)
		output = output + arr[mod7]
	print("{:}".format(output))
