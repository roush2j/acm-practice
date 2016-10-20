#!/usr/bin/env python3
# author: Brian Bauman
import math
import itertools

class Crane:
	def __init__(self, x, y, r, l, w):
		self.x = x
		self.y = y
		self.r = r
		self.reachlen = 0
		self.reach(l,w)
	def reach(self, l, w):
		if (self.r >= math.sqrt(self.x**2 + (self.y - w/2.)**2)):
			self.reachlen = self.reachlen | 1
		if (self.r >= math.sqrt(self.x**2 + (self.y + w/2.)**2)):
			self.reachlen = self.reachlen | 2
		if (self.r >= math.sqrt((self.x-l/2)**2 + self.y**2)):
			self.reachlen = self.reachlen | 4
		if (self.r >= math.sqrt((self.x+l/2)**2 + self.y**2)):
			self.reachlen = self.reachlen | 8
	def __hash__(self):
		return self.reachlen
	def __eq__(self,o):
		return self.reachlen == o.reachlen
	def __str__(self):
		return "{:}:Crane at ({:},{:}) with reach {:}".format(self.reachlen,self.x,self.y,self.r)
	def __lt__(self,o):
		return self.reachlen < o.reachlen
	def __gt__(self,o):
		return self.reachlen > o.reachlen
		
l,w,n,r = [int(i) for i in input().split()]

cranes = set()

for i in range(n):
	x,y = [int(i) for i in input().split()]
	cranes.add(Crane(x,y,r,l,w))

chash = 0
stemp = 1
solution = None

while (stemp < 5):
	x = itertools.combinations(cranes, stemp)
	for comb in x:
		for crane in comb:
			chash = chash|crane.reachlen
		if (chash == 15):
			solution = stemp
			stemp = 5
			break
		else:
			chash = 0
	stemp = stemp + 1

if (solution == None):
	print("Impossible")
else:
	print("{:}".format(solution))

