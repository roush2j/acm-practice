import java.io.*;
import java.util.*;

public class Xortris {

    public static void main(String[] args) throws FileNotFoundException {
        Xortris x33 = new Xortris(3, 3);
        System.out.println(x33.solvableGridCount());
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

    /** Size of grid */
    private final int    w, h;

    /** Solvability of individual grids, one bit per grid is ascending order */
    private final long[] grids;

    /** Number of solvable grids */
    private final long   gridCount;

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
        assert width * height <= 36;
        this.w = width;
        this.h = height;

        // masks for individual rows in the w x h grid
        final long r0Mask = (1 << width) - 1;
        final long r1Mask = r0Mask << width;
        final long r2Mask = r1Mask << width;
        final long r3Mask = r2Mask << width;
        final long r4Mask = r3Mask << width;
        final long r5Mask = r4Mask << width;
        final long r6Mask = r5Mask << width;
        final long r7Mask = r6Mask << width;
        // bitshifts for individual rows
        final long r1Shift = 1 * (8 - width);
        final long r2Shift = 2 * (8 - width);
        final long r3Shift = 3 * (8 - width);
        final long r4Shift = 4 * (8 - width);
        final long r5Shift = 5 * (8 - width);
        final long r6Shift = 6 * (8 - width);
        final long r7Shift = 7 * (8 - width);

        // masks for the full row/col-beyond-last for bounds checking
        final long lrowMask = height >= 8 ? 0 : 0xFFL << (8 * height);
        final long lcolMask = width >= 8 ? 0 : 0x0101010101010101L << width;

        // arrays to hold the status of each possible w x h grid
        // one bit per grid, in ascending order
        //      grids = is grid reachable from root
        //      visit = grid needs to be visited for possible children
        final int sz = width * height < 6 ? 1 : 1 << (width * height - 6);
        this.grids = new long[sz];
        final long[] visit = new long[sz];
        grids[0] = visit[0] = 0x1;

        // visit every reachable node in the move tree, 
        // we cycle through every bit in the visit array until it is empty 
        long gCount = 0, dgCount = 0;
        do {
            dgCount = 0;
            for (int idx = 0; idx < sz; idx++) {
                if (visit[idx] == 0) continue;
                for (long bit = 1L, bitidx = 0; bit != 0; bit <<= 1, bitidx++) {
                    if ((visit[idx] & bit) == 0) continue;

                    // visit a particular grid
                    // first, we decode the index into an actual grid bitmask
                    long grid = (long) idx << 6 | bitidx;
                    grid = (grid & r0Mask) | (grid & r1Mask) << r1Shift
                            | (grid & r2Mask) << r2Shift
                            | (grid & r3Mask) << r3Shift
                            | (grid & r4Mask) << r4Shift
                            | (grid & r5Mask) << r5Shift
                            | (grid & r6Mask) << r6Shift
                            | (grid & r7Mask) << r7Shift;

                    // then we try every possible move (every 
                    // translation/rotation/mirror of every tetramino that fits)
                    long moveCount = 0;
                    for (long tbase : tetros) {
                        for (; (tbase & lrowMask) == 0; tbase <<= 8) {
                            for (long t = tbase; (t & lcolMask) == 0; t <<= 1) {
                                // perform the "move"
                                long mg = grid ^ t;
                                // decode the resulting grid into bit indices
                                mg = (mg & r0Mask) | (mg >>> r1Shift) & r1Mask
                                        | (mg >>> r2Shift) & r2Mask
                                        | (mg >>> r3Shift) & r3Mask
                                        | (mg >>> r4Shift) & r4Mask
                                        | (mg >>> r5Shift) & r5Mask
                                        | (mg >>> r6Shift) & r6Mask
                                        | (mg >>> r7Shift) & r7Mask;
                                long mbit = 1L << (mg & 0x3F);
                                int midx = (int) (mg >>> 6);
                                if ((grids[midx] & mbit) == 0) {
                                    // move produces a newly reachable grid
                                    // mark it for future visits
                                    visit[midx] |= mbit;
                                    grids[midx] |= mbit;
                                    moveCount++;
                                }
                                if ((t & 0x8080808080808080L) != 0) break;
                            }
                            if ((tbase & 0xFF00000000000000L) != 0) break;
                        }
                    }
                    visit[idx] &= ~bit;
                    dgCount += moveCount;

                }
            }
            gCount += dgCount;
        } while (dgCount > 0);

        gridCount = gCount;
    }

    public long solvableGridCount() {
        return gridCount;
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
    private static final long[]   tetros;
    static {
        tetros = new long[TetrominoShapes.length];
        for (int i = 0; i < TetrominoShapes.length; i++) {
            tetros[i] = shapeToGrid(TetrominoShapes[i]);
        }
    }

    /** Translate a grid up/down and/or left/right. */
    public static long translate(long grid, int dx, int dy) {
        int shift = dx + (dy << 3);
        if (shift >= 0) return grid << shift;
        else return grid >>> -shift;
    }

    /** Decode an 8x8 grid from a string of up to 8 braille characters */
    public static long shapeToGrid(String shape) {
        long grid = 0;
        int idx = 0;
        for (int j = 0; j < 8; j += 4) {
            for (int i = 0; i < 8; i += 2) {
                if (idx >= shape.length()) return grid;
                int code = shape.charAt(idx++);
                while ((code & 0xFFFFFF00) != 0x2800) {
                    if (idx >= shape.length()) return grid;
                    code = shape.charAt(idx++);
                }
                int lowsix = code & 0x3F;
                int aligned = lowsix | lowsix << 7 | lowsix << 14;
                int shifted = aligned & 0x010101 | (aligned >>> 2) & 0x020202;
                shifted |= (code & 0xC0) << 18;
                grid |= (long) shifted << (i + (j << 3));
            }
        }
        return grid;
    }

    /** Return a string representation of an 8x8 grid using Braille characters */
    public static String gridToString(long grid) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ▁▁▁▁▁ \n");
        for (int j = 0; j < 8; j += 4) {
            sb.append('│');
            for (int i = 0; i < 8; i += 2) {
                int shifted = (int) (grid >>> (i + (j << 3)));
                int aligned = shifted & 0x010101 | (shifted & 0x020202) << 2;
                int lowsix = aligned | (aligned >>> 7) | (aligned >>> 14);
                int code = 0x2800 | lowsix & 0x3F | (shifted >>> 18) & 0xC0;
                sb.append((char) code);
            }
            sb.append("│\n");
        }
        sb.append(" ▔▔▔▔▔ ");
        return sb.toString();
    }
}
