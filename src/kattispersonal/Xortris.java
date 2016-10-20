package kattispersonal;
import java.util.Scanner;

public class Xortris {

    /**
     * Checks if the grid can be reduced to empty by some combination of
     * translations/rotations/mirrorings of the 5 basic tetrominos.
     * 
     * @param grid The initial grid in row-major order, containing at least
     *            {@code width * height / 64} elements. One bit per square:
     *            1-bits are black squares and 0-bits are white squares.
     * @param width width of the grid
     * @param height height of the grid
     * 
     * @return true if the grid is solvable
     */
    public static boolean solvable(long[] grid, int width, int height) {
        int min = Math.min(width, height), max = Math.max(width, height);

        /* Empty grids have no solution */
        if (min == 0) return false;

        /*
         * After taking an embarrassingly long time messing around,
         * it seems my initial thoughts were right - all grids with
         * an even number of black squares *are* solvable, but only
         * if the grid is at least 3x2 or 2x3.
         * In other words, only the grids large enoug to accomodate
         * the "O", "L", and "Z" tetrominos.
         */
        if (min >= 2 && max >= 3) {
            long pop = 0;
            for (int i = 0; i < grid.length; i++)
                pop += Long.bitCount(grid[i]);
            return (pop % 2) == 0;
        }

        /*
         * 2x2 grids are a special case which can only accomodate the
         * "O" tetromino, and thus have only 2 possible solutions:
         * all 4 white or all 4 black.
         * 1x1,1x2,2x1,1x3,3x1 grids are too small to accomodate any
         * tetrominos and have only one solution: all white.
         */
        if (min * max <= 4) {
            return grid[0] == 0 || grid[0] == 0xF;
        }

        /*
         * I have not found a clever constant-time solution for 1xN grids.
         * Instead, I use a constant-time solution.  
         * We note that the left/top-most grid square can be touched affected 
         * by only one tetromino placement. The second left/top-most grid 
         * square can be touched by only two tetromino placements, and so on.
         * Each grid square is affected by at most 4 tetromino placements.
         * We can inductively calculate the necessary tetrominos to produce
         * the first N-3 grid squares.  The grid is solvable only if the last
         * 3 squares match the calculated placement.
         */
        assert (min == 1 && max > 4);
        boolean p1 = false, p2 = false, p3 = false;
        for (int idx = 0; idx <= width * height / 64; idx++) {
            for (long bit = 1L; bit != 0; bit <<= 1) {
                boolean square = (grid[idx] & bit) != 0;
                boolean p0 = p1;
                p1 = p2;
                p2 = p3;
                p3 = p0 ^ p1 ^ p2 ^ square;
            }
        }
        return !(p1 || p2 || p3);
    }

    public static void main(String[] args) {

        // parse grid
        Scanner s = new Scanner(System.in);
        int h = s.nextInt(), w = s.nextInt();
        long[] grid = new long[(w * h + 63) / 64];
        s.nextLine();
        for (int j = 0; j < h; j++) {
            String line = s.nextLine();
            for (int i = 0; i < w; i++) {
                if (line.charAt(i) == '.') continue; // white
                assert (line.charAt(i) == 'X'); // black
                int idx = j * w + i;
                grid[idx / 64] |= 1L << (idx & 0x3F);
            }
        }

        // print answer
        System.out.println(solvable(grid, w, h) ? "possible" : "impossible");
        s.close();
    }
}
