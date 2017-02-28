#!/usr/bin/env python3

# https://open.kattis.com/contests/annky6/problems/8queens

import sys

board = [['.'] * 8 for _ in range(8)]
def placeQueen(row, col):
    if board[row][col] != '.': return False
    for i in range(8):
        if board[row][i] == '*' or board[i][col] == '*': return False
        board[row][i] = '+'
        board[i][col] = '+'
        bc = col - row + i
        if bc >= 0 and bc < 8:
            if board[i][bc] == '*': return False
            board[i][bc] = '+'
        fc = col + row - i
        if fc >= 0 and fc < 8:
            if board[i][fc] == '*': return False
            board[i][fc] = '+'
    board[row][col] = '*'
    return True
    
def queenTest():
    queenCnt = 0
    for row in range(8):
        line = sys.stdin.readline()
        for col in range(8):
            if line[col] == '.': continue
            if not placeQueen(row, col): return False
            queenCnt += 1
    return queenCnt
    
print("valid" if queenTest() == 8 else "invalid")
        