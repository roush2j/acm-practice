#!/usr/bin/env python3

import sys
import os.path
import io
import argparse
import glob
import subprocess

parser = argparse.ArgumentParser(description='Check candidate ICPC Solutions against test cases')
parser.add_argument('testdir', help='the directory containing the test inputs (*.in) and expected outputs (*.ans)')
parser.add_argument('solutioncmd', help='command to execute candidate solution, taking the input on stdin and producing output on stdout')
parser.add_argument('solutionargs',  nargs=argparse.REMAINDER, help='arguments to pass to the solution')
args = parser.parse_args()

if not os.path.exists(args.testdir):
    print('Error: test directory does not exist ' + args.testdir)
    sys.exit()
print('Reading tests from ' + args.testdir)
    
cmd = [args.solutioncmd] + args.solutionargs
print('Testing ' + ' '.join(cmd))
    
def cmpfiles(f1, f2):
    l1, l2 = f1.readline(), f2.readline()
    line = 1
    while l1 or l2:
        if len(l1) == 0 or len(l2) == 0 or l1.rstrip() != l2.rstrip():
            return (line, l1.rstrip(), l2.rstrip())
        l1, l2 = f1.readline(), f2.readline()
        line += 1
    return None

passed = True
for test in glob.glob(os.path.join(args.testdir, '*.in')):
    with open(test, 'r') as testin:
        with open(test.replace('.in','.ans'), 'r') as testans:
            proc = subprocess.Popen(cmd, stdin=testin, stdout=subprocess.PIPE, stderr=None)
            mismatch = cmpfiles(testans, io.TextIOWrapper(proc.stdout))
            if mismatch is not None:
                print('OUTPUT MISMATCH on line ' + str(mismatch[0]) + ' of test ' + test)
                print('  expected: ' + mismatch[1])
                print('   but got: ' + mismatch[2])
                passed = False
            if proc.wait() != 0:
                print('ERROR on ' + test)
                passed = False
                
print('Overall Results: ' + ('PASSED' if passed else 'FAILED'))
                