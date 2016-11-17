package kattis20161029;

import java.io.*;
import java.util.*;

public class VinDiagrams {

    static final int A = 1, B = 2, Edge = 4;

    /** Returns the value at (r,c) from the grid, or 0 if out of bounds */
    public static int gridVal(int[][] grid, int r, int c) {
        if (r < 0 || r >= grid.length) return 0;
        if (c < 0 || c >= grid[0].length) return 0;
        return grid[r][c];
    }

    /** Pretty-print the grid for debugging */
    public static void dumpGrid(int[][] grid) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                System.out.print(Integer.toHexString(grid[r][c]));
            }
            System.out.println();
        }
    }

    /** 
     * Mark the edges and interior squares of a ring with the specified
     * bitmask value.  This given coordinates must be an unambiguous square
     * on the ring's edge and all edge squares must be already marked.
     */
    public static void traceAndFill(int[][] grid, int r0, int c0, int val) {
        final int R = grid.length, C = grid[0].length;

        // Step along outside edge of the ring.
        int r = r0, c = c0;         // current position
        int dr = 0, dc = 1;         // delta from last position
        int firstr = R, firstc = C; // upper-leftmost corner of ring edge
        do {
            // continue straight if possible
            // otherwise try turning CCW, then CW
            if ((gridVal(grid, r + dr, c + dc) & Edge) == 0) {
                int tmp = dc;
                dc = dr;
                dr = -tmp;
            }
            if ((gridVal(grid, r + dr, c + dc) & Edge) == 0) {
                dc = -dc;
                dr = -dr;
            }
            // mark edge square
            r += dr;
            c += dc;
            grid[r][c] |= val;
            // track upper-left most position in edge
            if (r < firstr || (r == firstr && c < firstc)) {
                firstr = r;
                firstc = c;
            }
        } while (r != r0 || c != c0); // terminate when we reach the starting square

        // Since the ring edge must not touch or intersect itself, we know that
        // every corner on the edge is kitty-corner to an interior square:
        //      ........ <- outside
        //      .AXXXXXX
        //      .X*..... <- inside
        // If 'A' is the upper-leftmost corner then the square down+right of it
        // marked '*' must be an interior square for all rings.
        // Credit to B. Bauman for this observation.

        // We start with this square and perform a depth-first search outwards to
        // mark all interior squares (since the interior is guaranteed to be
        // contiguous).
        int[] stack = new int[32];
        int stacktop = 0;
        stack[stacktop++] = firstr + 1;
        stack[stacktop++] = firstc + 1;
        while (stacktop > 0) {
            // pop square off of stack and mark it
            c = stack[--stacktop];
            r = stack[--stacktop];
            grid[r][c] |= val;
            // iterate through 4 immediate neighbors
            dr = 1;
            dc = 0;
            for (int dir = 0; dir < 4; dir++) {
                if ((gridVal(grid, r + dr, c + dc) & val) == 0) {
                    // push unmarked neighbor onto stack
                    // (note that the edges have already been marked)
                    if (stacktop + 2 >= stack.length)
                        stack = Arrays.copyOf(stack, stack.length * 2);
                    stack[stacktop++] = r + dr;
                    stack[stacktop++] = c + dc;
                }
                int tmp = dc;
                dc = dr;
                dr = -tmp;
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner s = new Scanner(System.in);
        
        // read the size of the grid
        int R = s.nextInt(), C = s.nextInt();
        int[][] grid = new int[R][C];
        s.nextLine();

        // read the input, mark the edges and labeled ring squares
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
                }
            }
        }

        // mark the edges and interior squares for each ring
        traceAndFill(grid, rA, cA, A);
        traceAndFill(grid, rB, cB, B);

        // count the interior squares
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
        System.out.format("%d %d %d\n", acnt, bcnt, icnt);
        s.close();
    }
}
