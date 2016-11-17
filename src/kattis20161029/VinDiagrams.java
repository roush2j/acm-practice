package kattis20161029;

import java.io.*;
import java.util.*;

public class VinDiagrams {

    static final int A = 1, B = 2, Edge = 4;

    public static void dumpGrid(int[][] grid) {
        final int R = grid.length, C = grid[0].length;
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                System.out.print(Integer.toHexString(grid[r][c]));
            }
            System.out.println();
        }
    }

    public static void fillEdge(int[][] grid, int r0, int c0, int val) {
        final int R = grid.length, C = grid[0].length;
        int r = r0, c = c0;
        int dr = 1, dc = 0;
        do {
            for (int dir = 0; dir < 4; dir++) {
                int nr = r + dr, nc = c + dc;
                if (dir != 2 && nr >= 0 && nr < R && nc >= 0 && nc < C
                        && (grid[nr][nc] & Edge) != 0) {
                    grid[r][c] |= val;
                    r = nr;
                    c = nc;
                    break;
                } else {
                    int tmp = dc;
                    dc = dr;
                    dr = -tmp;
                }
            }
        } while (r != r0 || c != c0);
    }

    public static void fillInterior(int[][] grid, int r, int c, int val) {
        final int R = grid.length, C = grid[0].length;
        if (r < 0 || r >= R || c < 0 || c >= C || (grid[r][c] & val) != 0)
            return;
        grid[r][c] |= val;
        int dr = 1, dc = 0;
        for (int dir = 0; dir < 4; dir++) {
            fillInterior(grid, r + dr, c + dc, val);
            int tmp = dc;
            dc = dr;
            dr = -tmp;
        }
    }

    public static String solve(Scanner s) {
        int R = s.nextInt(), C = s.nextInt();
        s.nextLine();

        int[][] grid = new int[R][C];

        int rA = -1, cA = -1, rB = -1, cB = -1;
        for (int r = 0; r < R; r++) {
            String line = s.nextLine();
            for (int c = 0; c < C; c++) {
                switch (line.charAt(c)) {
                case 'X':
                    grid[r][c] = Edge;
                    break;
                case 'A':
                    grid[r][c] = A | Edge;
                    rA = r;
                    cA = c;
                    break;
                case 'B':
                    grid[r][c] = B | Edge;
                    rB = r;
                    cB = c;
                    break;
                default:
                }
            }
        }

        fillEdge(grid, rA, cA, A);
        fillEdge(grid, rB, cB, B);

        A: for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                if ((grid[r][c] & A) != 0) {
                    fillInterior(grid, r + 1, c + 1, A);
                    break A;
                }
            }
        }
        B: for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                if ((grid[r][c] & B) != 0) {
                    fillInterior(grid, r + 1, c + 1, B);
                    break B;
                }
            }
        }

        int acnt = 0, bcnt = 0, icnt = 0;
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                switch (grid[r][c]) {
                case A:
                    acnt++;
                    break;
                case B:
                    bcnt++;
                    break;
                case A | B:
                    icnt++;
                    break;
                }
            }
        }
        return String.format("%d %d %d", acnt, bcnt, icnt);
    }

    public static void bulkTest(String secretTestPath)
            throws FileNotFoundException {
        for (int i = 3; i <= 13; i++) {
            File fin = new File(String.format("%s/%03d.in", secretTestPath, i));
            Scanner s = new Scanner(fin);
            String sol = solve(s);
            s.close();
            File fans = new File(
                    String.format("%s/%03d.ans", secretTestPath, i));
            s = new Scanner(fans);
            String ans = s.nextLine().trim();
            if (sol.equals(ans)) {
                System.out.format("PASSED test case %d: %s\n", i, sol);
            } else {
                System.out.format("FAILED test case %d: %s != %s\n", i, sol,
                        ans);
            }
            s.close();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner s = new Scanner(System.in);
        System.out.println(solve(s));
        s.close();
    }
}
