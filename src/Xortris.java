import java.io.*;
import java.util.*;

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

    public static void main(String[] args) throws FileNotFoundException {

        for (int w = 1; w <= 8; w++) {
            for (int h = 1; h <= 8 && h * w <= 20; h++) {
                new Xortris(w, h).test(System.out);
            }
        }
        if (true) return;

        Scanner s = new Scanner(System.in);
        int m = s.nextInt(), n = s.nextInt();
        int whites = 0, blacks = 0;
        s.nextLine();
        for (int i = 0; i < m; i++) {
            String line = s.nextLine();
            for (int j = 0; j < n; j++) {
                if (line.charAt(j) == 'X') blacks++;
                else whites++;
            }
        }

        // since each tetramino changes the color of exactly four
        // squares, it must change (whites-blacks) by a multiple of four.
        // hence, if the difference is not even then no combination of
        // tetraminoes can reduce the grid to all whites.
        System.out.println(blacks % 2 == 0 ? "possible" : "impossible");
        s.close();
    }

    //@formatter:off
    /**
     * Braille-string representations of all rotations + reflections of the 
     * 5 fundamental tetrominos.
     */
    private static final String[] TetrominoShapes = new String[] { 
        "⠛",
        "⡇","⠉⠉",
        "⠞","⠙⠂",
        "⠳","⠚⠁",
        "⠗","⠺","⠙⠁","⠚⠂",
        "⠧","⠹","⠋⠁","⠒⠃",
        "⠏","⠼","⠓⠂","⠉⠃"
    };
    //@formatter:on

    /**
     * Grids representing all rotations + reflections of the 5 fundamental
     * tetrominos.
     */
    private final long[]          tetros;

    /** Size of grid */
    public final int              w, h;

    /** Solvability of individual grids, one bit per grid is ascending order */
    private final long[]          grids;

    /** Number of solvable grids */
    public final long             gridCount;

    /**
     * Create a new instance of the Xortris game and find all solvable starting
     * grids.
     * 
     * @param width the width of the grid, in the range [1,8]
     * @param height the height of the grid, in the range [1,8]
     */
    public Xortris(final int width, final int height) {
        assert width >= 1 && width <= 8;
        assert height >= 1 && height <= 8;
        assert width * height <= 26;
        this.w = width;
        this.h = height;

        // build list of encoded tetrominos that fit at least once
        {
            long[] tets = new long[TetrominoShapes.length];
            int tetcnt = 0;
            for (int i = 0; i < TetrominoShapes.length; i++) {
                try {
                    tets[tetcnt] = shapeToGrid(TetrominoShapes[i]);
                    tetcnt++;
                } catch (Exception ex) {
                    // shape too large
                }
            }
            tetros = Arrays.copyOf(tets, tetcnt);
        }

        // masks for the w x h grid
        final long frowMask = (1 << w) - 1; // first row
        final long lrowMask = frowMask << (w * (h - 1)); // last row
        final long fcolMask; // first column
        {
            long m = 0;
            for (int i = 0; i < h; i++)
                m |= 1 << (i * w);
            fcolMask = m;
        }
        final long lcolMask = fcolMask << (w - 1); // last column

        // arrays to hold the status of each possible w x h grid
        // one bit per grid, in ascending order
        //      grids = is grid reachable from root
        //      visit = grid needs to be visited for possible children
        final int sz = Math.max(1, (1 << (w * h)) / 64);
        this.grids = new long[sz];
        final long[] visit = new long[sz];
        grids[0] = visit[0] = 0x1;

        // visit every reachable node in the move tree, 
        // we cycle through every bit in the visit array until it is empty 
        long gCount = 1, dgCount = 0;
        do {
            dgCount = 0;
            for (int idx = 0; idx < sz; idx++) {
                if (visit[idx] == 0) continue;
                for (long bit = 1L, bitidx = 0; bit != 0; bit <<= 1, bitidx++) {
                    if ((visit[idx] & bit) == 0) continue;

                    // visit a particular grid
                    long grid = (long) idx << 6 | bitidx;
                    // try every possible translation/rotation/mirror 
                    // of every tetramino that fits
                    long moveCount = 0;
                    for (long tbase : tetros) {
                        for (;; tbase <<= w) {
                            for (long t = tbase;; t <<= 1) {
                                // perform the "move"
                                long mg = grid ^ t;
                                long mbit = 1L << (mg & 0x3F);
                                int midx = (int) (mg >>> 6);
                                if ((grids[midx] & mbit) == 0) {
                                    // move produces a newly reachable grid
                                    // mark it for future visits
                                    visit[midx] |= mbit;
                                    grids[midx] |= mbit;
                                    moveCount++;
                                }
                                if ((t & lcolMask) != 0) break;
                            }
                            if ((tbase & lrowMask) != 0) break;
                        }
                    }
                    visit[idx] &= ~bit;
                    dgCount += moveCount;

                    if ((Long.bitCount(grid) & 1) == 1) {
                        System.out.println("WARNING: ODD GRID FOUND");
                        System.out.println(gridToString(grid));
                    }
                }
            }
            gCount += dgCount;
        } while (dgCount > 0);

        gridCount = gCount;
    }

    /** Return the next solvable grid from the puzzle */
    public long nextGrid(long prevGrid) {
        final long max = 1L << (w * h);
        while (++prevGrid < max) {
            long bit = 1L << (prevGrid & 0x3F);
            int idx = (int) (prevGrid >>> 6);
            if ((grids[idx] & bit) != 0) return prevGrid;
        }
        return 0;
    }

    /** Return true if the specified grid is solvable */
    public boolean hasGrid(long grid) {
        if (grid >= 1L << (w * h)) ;
        long bit = 1L << (grid & 0x3F);
        int idx = (int) (grid >>> 6);
        return (grids[idx] & bit) != 0;
    }

    /**
     * Decode a grid from a string of braille characters
     * 
     * @throws IllegalArgumentException if the shape string is not a legal grid
     */
    public long shapeToGrid(String shape) {
        long grid = 0;
        for (int i = 0, j = 0, idx = 0; idx < shape.length(); idx++) {

            // next character
            int code = shape.charAt(idx);
            if (code == '\n') {
                i = 0;
                j += 4;
            }
            if ((code & 0xFFFFFF00) != 0x2800) continue; // not braille

            // decode character
            long col0 = (code & 0x01) << 6;
            long col1 = (code & 0x08) << 4;
            col0 |= (code & 0x02) << (w + 5);
            col1 |= (code & 0x10) << (w + 3);
            col0 |= (code & 0x04) << (2 * w + 4);
            col1 |= (code & 0x20) << (2 * w + 2);
            col0 |= (code & 0x40) << (3 * w);
            col1 |= (code & 0x80) << (3 * w);

            // check grid bounds
            if ((i >= w && col0 != 0) || (i + 1 >= w && col1 != 0)) {
                throw new IllegalArgumentException("Shape width > " + w + " : "
                        + shape);
            } else if ((col0 | col1) >>> (6 + w * Math.max(0, h - j)) != 0) {
                throw new IllegalArgumentException("Shape height > " + h
                        + " : " + shape);
            }

            // set grid chunk
            grid |= ((col0 | col1) >>> 6) << (i + (j * w));
            i += 2;
        }
        return grid;
    }

    /** Return a string representation of a grid using Braille characters */
    public String gridToString(long grid) {
        final int colMask = (1 | 1 << w | 1 << (2 * w) | 1 << (3 * w)) << 6;
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < h; j += 4) {
            sb.append('│');
            for (int i = 0; i < w; i += 2) {
                // encode 8-bit chunk
                final long shifted = (grid >>> (i + (j * w))) << 6;
                final long col0 = shifted & colMask;
                final long col1 = (i + 1 < w) ? shifted & (colMask << 1) : 0;
                long code = 0x2800;
                code |= (col0 >>> 6) & 0x01;
                code |= (col1 >>> 4) & 0x08;
                code |= (col0 >>> (w + 5)) & 0x02;
                code |= (col1 >>> (w + 3)) & 0x10;
                code |= (col0 >>> (2 * w + 4)) & 0x04;
                code |= (col1 >>> (2 * w + 2)) & 0x20;
                code |= (col0 >>> (3 * w)) & 0x40;
                code |= (col1 >>> (3 * w)) & 0x80;
                sb.append((char) code);
            }
            sb.append("│\n");
        }
        return sb.toString();
    }

    /** Test every possible starting grid to verify correctness */
    void test(PrintStream out) {
        out.format("%d x %d -> %8d\n", w, h, gridCount);
        long[] buf = new long[1];
        long count = 0;
        for (long g = 0, gg = 0; g < 1L << (w * h); g++) {
            buf[0] = g;
            String err = "";
            if (g == gg) {
                if (!hasGrid(g)) err += ", not precomp";
                if (!solvable(buf, w, h)) err += ", not solvable";
                count++;
                gg = nextGrid(gg);
            } else {
                if (hasGrid(g)) err += ", was precomp";
                if (solvable(buf, w, h)) err += ", was solvable";
            }
            if (g != shapeToGrid(gridToString(g))) err += ", string mismatch";
            if (!err.isEmpty()) out.println(gridToString(g) + " ERR" + err);
        }
        if (count != gridCount) out.println(" COUNT ERR: " + count);
    }
}
