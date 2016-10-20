
TIME := $(shell date +%s)

bin/skihill: skihill.cpp
	g++ -Wall -O3 -DNDEBUG -std=c++11 -o ../bin/skihill skihill.cpp

test: ../bin/skihill
	cat testdata/SkiTest1 | java -cp ../bin SkiHill >testdata/SkiOutJava
	cat testdata/SkiTest1 | ../bin/skihill >testdata/SkiOutCpp
	diff -y --suppress-common-lines testdata/SkiOutJava testdata/SkiOutCpp
