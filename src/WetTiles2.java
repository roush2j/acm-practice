import java.util.*;

/**
 * This problem is a classic path-finding (or "maze solving") algorithm on a
 * rectangular grid. We must find the minimum path length from each grid tile to
 * the nearest source ('leak'), and report how many tiles have a path length
 * less than some specified threshold.
 * 
 * This is a textbook use case for best-first search. However, there is an
 * easier way to code it using the relaxation method shown below. We store in
 * memory the minimum number of minutes each tile takes to become wet (or zero
 * if a tile is not wet). At first, only the leak tiles are marked and all other
 * tiles are zero. During every round of relaxation, we iterate through the
 * tiles looking for those that could become wet sooner than their current time
 * value. These tiles are updated. In the next round of relaxation the neighbors
 * of these tiles might also be updated, and then their neighbors, and so on. In
 * this way the 'wetness' spreads outward from the leaks. We stop when no more
 * tiles can be updated.
 * 
 * This method does strictly more work than the breadth-first search but it uses
 * less memory and may be more obvious to someone without a classical CS
 * education.
 * 
 * https://open.kattis.com/contests/mk2hhn/problems/wettiles
 * 
 * @author jroush
 */
public class WetTiles2 {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // Iterate over test cases
        while (true) {

            // read 5 parameters for this test case
            int width = s.nextInt();
            if (width < 0) break;   // width = -1 is used to mark end of input
            int height = s.nextInt();
            int maxTime = s.nextInt();
            int leakCnt = s.nextInt();
            int wallCnt = s.nextInt();

            // a map of the tiles in our 'house'
            // the tile at (x,y) is represented by tiles[y*width + x]
            // the value is the minute # at which the tile becomes wet
            // all tiles start at 0, meaning they are not wet
            int[] tiles = new int[width * height];

            // read leak positions and mark them
            for (int i = 0; i < leakCnt; i++) {
                int x = s.nextInt() - 1, y = s.nextInt() - 1;
                tiles[y * width + x] = 1; // all leaks become wet at minute 1
            }

            // read wall positions and mark them
            for (int i = 0; i < wallCnt; i++) {
                int x = s.nextInt() - 1, y = s.nextInt() - 1;
                int dx = s.nextInt() - 1 - x, dy = s.nextInt() - 1 - y;
                int incx = Integer.signum(dx), incy = Integer.signum(dy);
                int wallTileCnt = 1 + Math.max(Math.abs(dx), Math.abs(dy));
                for (int j = 0; j < wallTileCnt; j++) {
                    tiles[y * width + x] = -1; // walls marked as -1
                    x += incx;
                    y += incy;
                }
            }

            // solve the test case by relaxation
            int wetTiles = leakCnt, tilesUpdated = 0;
            do {
                // begin another round of the relaxation
                tilesUpdated = 0;
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {

                        int t = tiles[y * width + x];
                        if (t < 0) continue; // wall tiles cannot change

                        // find the minimum time at which 
                        // a neighboring tile becomes wet
                        int minTime = Integer.MAX_VALUE;
                        if (x > 0) {
                            int left = tiles[y * width + x - 1];
                            if (left > 0 && left < minTime) minTime = left;
                        }
                        if (x + 1 < width) {
                            int right = tiles[y * width + x + 1];
                            if (right > 0 && right < minTime) minTime = right;
                        }
                        if (y > 0) {
                            int up = tiles[(y - 1) * width + x];
                            if (up > 0 && up < minTime) minTime = up;
                        }
                        if (y + 1 < height) {
                            int down = tiles[(y + 1) * width + x];
                            if (down > 0 && down < minTime) minTime = down;
                        }

                        // make sure at least one neighbor is wet and
                        // wetness will has time to spread to this tile
                        if (minTime >= maxTime) continue;

                        if (t == 0) wetTiles++; // tile changed from dry -> wet
                        else if (t <= minTime + 1) continue; // tile wet earlier

                        // update tile
                        tiles[y * width + x] = minTime + 1;
                        tilesUpdated++;
                    }
                }
            } while (tilesUpdated > 0);

            // print results
            System.out.println(wetTiles);
        }

        s.close();
    }

}
