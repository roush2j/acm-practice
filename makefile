
TIME := $(shell date +%s)

bin/skihill: src/skihill.cpp
	g++ -Wall -O3 -DNDEBUG -std=c++11 -o bin/skihill src/skihill.cpp

test: bin/skihill
	cat SkiTest4 | java -cp bin SkiHill >SkiOutJava
	cat SkiTest4 | bin/skihill >SkiOutCpp
	diff -y --suppress-common-lines SkiOutJava SkiOutCpp
