package kattis20161029;

import java.util.Scanner;

public class VinDiagrams {

    static final int UNK = 0, A = 1, B = 2, Edge = 4, Empty = 8;

    public static void trace(byte[] grid, int row, int col, int idx) {
        int state = grid[idx];
        int r = idx / col, c = idx % col, i = idx;
        boolean dir = false;
        while (true) {
            int d = 0;
            for (; d < 2; d++, dir = !dir) {
                if (dir) {
                    if (r > 0 && (grid[i - col] & state) == Edge) {
                        r--;
                        i -= col;
                        break;
                    } else if (r < row - 1 && (grid[i + col] & state) == Edge) {
                        r++;
                        i += col;
                        break;
                    }
                } else {
                    if (c > 0 && (grid[i - 1] & state) == Edge) {
                        c--;
                        i -= 1;
                        break;
                    } else if (c < col - 1 && (grid[i + 1] & state) == Edge) {
                        c++;
                        i += 1;
                        break;
                    }
                }
            }
            if (d == 2) return; // no edge neighbors found to trace
            grid[i] |= state;
        }
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int row = s.nextInt(), col = s.nextInt();
        s.nextLine();
        byte[] grid = new byte[row * col];
        int astart = -1, bstart = -1;
        for (int r = 0; r < row; r++) {
            String line = s.nextLine();
            for (int c = 0; c < col; c++) {
                switch (line.charAt(c)) {
                case 'X':
                    grid[r * col + c] = Edge;
                    break;
                case 'A':
                    grid[r * col + c] = A | Edge;
                    astart = r * col + c;
                    break;
                case 'B':
                    grid[r * col + c] = B | Edge;
                    bstart = r * col + c;
                    break;
                default:
                    grid[r * col + c] = UNK;
                    break;
                }
            }
        }
        s.close();

        trace(grid, row, col, astart);
        trace(grid, row, col, bstart);

        int acnt = 0, bcnt = 0, abcnt = 0;
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                if ((grid[r * col + c] & Edge) > 0) {
                    continue;
                } 
                int up = 0, dn = 0, lf = 0, rt = 0;
                for (int rr = r - 1; rr >= 0; rr--) {
                    up ^= grid[rr * col + c];
                }
                for (int rr = r + 1; rr < row; rr++) {
                    dn ^= grid[rr * col + c];
                }
                for (int cc = c - 1; cc >= 0; cc--) {
                    lf ^= grid[r * col + cc];
                }
                for (int cc = c + 1; cc < col; cc++) {
                    rt ^= grid[r * col + cc];
                }
                int edges = up & dn & lf & rt & (A | B);
                if (edges == A) {
                    acnt++;
                } else if (edges == B) {
                    bcnt++;
                } else if (edges == (A | B)) {
                    abcnt++;
                }
            }
        }
        
        System.out.println(acnt + " " + bcnt + " " + abcnt);
    }
}
