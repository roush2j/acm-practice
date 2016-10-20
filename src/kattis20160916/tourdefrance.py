#!/usr/bin/env python3
# author: Brian Bauman

values = input()

while (len(values) > 1):
	f,r = [int(i) for i in values.split()]

	front = [int(i) for i in input().split()]
	rear = [int(i) for i in input().split()]

	values = input()

	d = []

	for fval in front:
		for rval in rear:
			d.append(fval/rval)

	maxSpread = 0

	d.sort()

	#find max delta
	for i in range(len(d)-1):
		if (d[i+1]/d[i] > maxSpread):
			maxSpread = d[i+1]/d[i]
	
	print("{:.2f}".format(maxSpread))
