import java.util.*;

/**
 * This problem is a classic path-finding (or "maze solving") algorithm on a
 * rectangular grid. We must find the minimum path length from each grid tile to
 * the nearest source ('leak'), and report how many tiles have a path length
 * less than some specified threshold.
 * 
 * This is a textbook use case for best-first search such as Dijkstra's
 * Algorithm. We start with a set (#1) of leak tiles. All of the non-wall
 * neighbors of the leak tiles are added to another set (#2). Then all of the
 * non-wall, not-yet-in-any-set neighbors of the #2 tiles are added to yet
 * another set (#3). Then the neighbors of the #3 tiles are added to #4, and so
 * on until we have a set for each # up to the given threshold.
 * 
 * https://open.kattis.com/contests/mk2hhn/problems/wettiles
 * 
 * @author jroush
 */
public class WetTiles {

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
            byte[] tiles = new byte[width * height];
            final int TILE_DRY = 0;
            final int TILE_WET = 1;
            final int TILE_WALL = 2;

            // read leak positions and mark them
            List<Integer> curset = new ArrayList<>();
            for (int i = 0; i < leakCnt; i++) {
                int x = s.nextInt() - 1, y = s.nextInt() - 1;
                int tile = y * width + x;
                tiles[tile] = TILE_WET; // all leaks become wet at minute 1
                curset.add(tile); // add leaks to first tile set
            }

            // read wall positions and mark them
            for (int i = 0; i < wallCnt; i++) {
                int x = s.nextInt() - 1, y = s.nextInt() - 1;
                int dx = s.nextInt() - 1 - x, dy = s.nextInt() - 1 - y;
                int incx = Integer.signum(dx), incy = Integer.signum(dy);
                int wallTileCnt = 1 + Math.max(Math.abs(dx), Math.abs(dy));
                for (int j = 0; j < wallTileCnt; j++) {
                    tiles[y * width + x] = TILE_WALL;
                    x += incx;
                    y += incy;
                }
            }

            // count all tiles at most maxTime units away from the nearest leak
            // using Dijkstra's algorithm
            int wetTiles = leakCnt;
            List<Integer> nextset = new ArrayList<>();
            for (int t = 2; t <= maxTime && !curset.isEmpty(); t++) {

                // iterate over tiles in current set (newly marked)
                for (int tile : curset) {
                    // for each tile, add unmarked neigbors to next set
                    int x = tile % width, y = tile / width;
                    int left = y * width + x - 1, right = y * width + x + 1;
                    int up = (y - 1) * width + x, down = (y + 1) * width + x;
                    if (x > 0 && tiles[left] == TILE_DRY) {
                        tiles[left] = TILE_WET;
                        nextset.add(left);
                    }
                    if (x + 1 < width && tiles[right] == TILE_DRY) {
                        tiles[right] = TILE_WET;
                        nextset.add(right);
                    }
                    if (y > 0 && tiles[up] == TILE_DRY) {
                        tiles[up] = TILE_WET;
                        nextset.add(up);
                    }
                    if (y + 1 < height && tiles[down] == TILE_DRY) {
                        tiles[down] = TILE_WET;
                        nextset.add(down);
                    }
                }
                wetTiles += nextset.size();

                // discard contents of current set and make the next set the current set
                List<Integer> _tmp = curset;
                curset = nextset;
                nextset = _tmp;
                nextset.clear();
            }

            // print results
            System.out.println(wetTiles);
        }

        s.close();
    }

}
