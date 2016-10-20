#!/usr/bin/env python3
# author: Brian Bauman
import math

class spaceObj:
	R = 20000 / math.pi
	def __init__(self, x, y, z):
		self.x = x
		self.y = y
		self.z = z
		self.rangeTo = 0.
		self.hit = False
	def distanceTo(self, o):
		dx = (self.x-o.x)**2
		dy = (self.y-o.y)**2
		dz = (self.z-o.z)**2
		return math.sqrt(dx+dy+dz)
	def vector(self):
		return math.sqrt(self.x**2+self.y**2+self.z**2)
	def firingRange(self):
		self.rangeTo = math.sqrt(self.vector()**2-self.R**2)

while (True):
	numSats,numTargets = [int(i) for i in input().split()]

	if (numSats == 0 and numTargets == 0):
		break

	potentialHits = 0

	sats = []
	targets = []

	for i in range(numSats):
		x,y,z = [float(j) for j in input().split()]
		sats.append(spaceObj(x,y,z))
	for i in range(numTargets):
                x,y,z = [float(j) for j in input().split()]
                targets.append(spaceObj(x,y,z))

	for sat in sats:
		sat.firingRange()
		for target in targets:
			if (sat.distanceTo(target) < sat.rangeTo and not target.hit):
				target.hit = True
				potentialHits = potentialHits + 1

	print("{:}".format(potentialHits))
