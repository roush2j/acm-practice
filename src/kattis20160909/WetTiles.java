package kattis20160909;
import java.util.*;

/**
 * This problem is a classic path-finding (or "maze solving") algorithm on a
 * rectangular grid. We must find the minimum path length from each grid tile to
 * the nearest source ('leak'), and report how many tiles have a path length
 * less than some specified threshold.
 * 
 * This is a textbook use case for best-first search such as Dijkstra's
 * Algorithm. We start with a FIFO queue of leak tiles a distance 1. All of the
 * non-wall neighbors of the leak tiles are added to the queue with distance 2.
 * Then all of the non-wall, not-yet-queued neighbors of those tiles are added,
 * and so on until we have reached the distance threshold.
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
            Queue<Integer> frontier = new ArrayDeque<Integer>();
            for (int i = 0; i < leakCnt; i++) {
                int x = s.nextInt() - 1, y = s.nextInt() - 1;
                int tile = y * width + x;
                tiles[tile] = TILE_WET; // all leaks become wet at minute 1
                frontier.add(tile); // add leaks to first tile set
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
            int wetTiles = 0;
            for (int t = 1; t <= maxTime && !frontier.isEmpty(); t++) {

                // iterate over tiles in current set (newly visited)
                for (int i = frontier.size(); i > 0; i--) {
                    // pop tile off of queue
                    wetTiles++;
                    int tile = frontier.remove();
                    int x = tile % width, y = tile / width;
                    // for each tile, add dry (unvisited) neighbors to queue
                    int left = y * width + x - 1, right = y * width + x + 1;
                    int up = (y - 1) * width + x, down = (y + 1) * width + x;
                    if (x > 0 && tiles[left] == TILE_DRY) {
                        tiles[left] = TILE_WET;
                        frontier.add(left);
                    }
                    if (x + 1 < width && tiles[right] == TILE_DRY) {
                        tiles[right] = TILE_WET;
                        frontier.add(right);
                    }
                    if (y > 0 && tiles[up] == TILE_DRY) {
                        tiles[up] = TILE_WET;
                        frontier.add(up);
                    }
                    if (y + 1 < height && tiles[down] == TILE_DRY) {
                        tiles[down] = TILE_WET;
                        frontier.add(down);
                    }
                }

            }

            // print results
            System.out.println(wetTiles);
        }

        s.close();
    }

}
