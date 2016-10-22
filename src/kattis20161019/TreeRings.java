package kattis20161019;

import java.util.*;

/**
 * This problem is from last year's (2015 ICPC ECNA) Regional competition.
 * <p>
 * The problem is to map out the "rings" of a closed shape on a grid. More
 * precisely, given a grid that has been partitioned into "tree" and "non-tree"
 * squares, we must find the manhattan distance from every tree square to the
 * nearest non-tree square.
 * <p>
 * This is yet another a shortest-path search problem. We can solve it with a
 * classic breadth-first search; the only wrinkle is that we must know where to
 * start the search. For that we must identify all of the boundary tree squares
 * during our initial input-reading phase.
 * 
 * https://open.kattis.com/contests/c6mgpk/problems/rings2
 *
 * @author jroush
 */
public class TreeRings {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        final int INFINITY = Integer.MAX_VALUE;

        // read size of grid
        int height = s.nextInt(), width = s.nextInt();
        s.nextLine();

        // read grid state: the manhattan distance of each square 
        //  from the nearest non-tree square
        // we also identify the "fringe" - the tree squares bordering non-tree squares
        int[] grid = new int[width * height];
        Queue<Integer> fringe = new ArrayDeque<>(width * height);
        for (int row = 0; row < height; row++) {
            String line = s.nextLine();
            for (int col = 0; col < width; col++) {
                int idx = row * width + col;

                if (line.charAt(col) == 'T') {
                    // Initialize most 'tree' squares to "infinite" distance ...
                    grid[idx] = INFINITY;
                    // ... but initialize any tree square which borders the edge
                    // of the grid, or a known non-tree square, to 1
                    if (col == 0 || col == width - 1 || row == 0
                            || row == height - 1 //
                            || grid[idx - 1] == 0 || grid[idx - width] == 0 //
                    ) {
                        grid[idx] = 1;
                        fringe.add(idx);
                    }

                } else {
                    // when we read a non-tree square, check if it borders a 
                    // known tree square and initialize that to 1
                    if (col > 0 && grid[idx - 1] > 1) {
                        grid[idx - 1] = 1;
                        fringe.add(idx - 1);
                    }
                    if (row > 0 && grid[idx - width] > 1) {
                        grid[idx - width] = 1;
                        fringe.add(idx - width);
                    }
                }
            }
        }

        // now the grid contains 0 for non-tree squares, 1 for all fringe squares,
        // and "infinity" for all interior (non-fringe) tree squares.
        // we perform a breadth-first search to fill in the distance for the
        // interior tree squares
        int maxd = 0;
        while (!fringe.isEmpty()) {
            int idx = fringe.remove();
            int row = idx / width, col = idx % width;
            int d = grid[idx];
            // Check interior neighbors
            if (col > 0 && grid[idx - 1] == INFINITY) {
                grid[idx - 1] = d + 1;
                fringe.add(idx - 1);
            }
            if (col < width - 1 && grid[idx + 1] == INFINITY) {
                grid[idx + 1] = d + 1;
                fringe.add(idx + 1);
            }
            if (row > 0 && grid[idx - width] == INFINITY) {
                grid[idx - width] = d + 1;
                fringe.add(idx - width);
            }
            if (row < height - 1 && grid[idx + width] == INFINITY) {
                grid[idx + width] = d + 1;
                fringe.add(idx + width);
            }
            // keep track of maximum distance
            if (maxd < d) maxd = d;
        }

        // now we print the result
        // note that the format changes if the maximum distance/depth is >= 10
        int fmtlen = maxd < 10 ? 2 : 3;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int d = grid[row * width + col];
                String n = lpad(d == 0 ? "" : Integer.toString(d), '.', fmtlen);
                System.out.print(n);
            }
            System.out.println();
        }

        s.close();
    }

    public static String lpad(String s, char pad, int width) {
        for (int i = s.length(); i < width; i++)
            s = pad + s;
        return s;
    }
}
