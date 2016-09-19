import java.util.*;

/**
 * This is an example of the Set Cover problem, which is quite well studied and
 * known to be NP-hard. Fortunately, we have a very small set to cover.
 * 
 * A naive brute-force solution is simply to iterate all O(2^N) possible crane
 * arrangements (where N is the number of cranes). Since there are only 4 walls
 * to move, we know that at most 4 cranes can be used. Therefore we need only
 * check arrangements with 1, 2, 3, or 4 cranes, with an overall runtime
 * complexity of O(N^4).
 * 
 * https://open.kattis.com/contests/u2b58x/problems/walls
 *
 * @author jroush
 */
public class Walls {

    public static float dist2(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2, dy = y1 - y2;
        return dx * dx + dy * dy;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // read the size of the house and the number and range of the cranes
        int length = s.nextInt(), width = s.nextInt();
        int craneCnt = s.nextInt(), craneRange = s.nextInt();

        // calculate the center coordinates of the 4 walls and the range squared
        // these are used repeatedly later on
        float centerX = length / 2F, centerY = width / 2F;
        int range2 = craneRange * craneRange;

        // read in the crane positions
        byte[] craneCovers = new byte[craneCnt];
        for (int i = 0; i < craneCnt; i++) {
            int x = s.nextInt(), y = s.nextInt();
            // we do not store the crane position - we don't need it
            // all we need to store is which of the four walls this crane can reach
            // we store it in a very compact form - one bit per wall
            if (dist2(x, y, centerX, 0) <= range2) craneCovers[i] |= 1;  // east wall
            if (dist2(x, y, -centerX, 0) <= range2) craneCovers[i] |= 2; // west wall
            if (dist2(x, y, 0, centerY) <= range2) craneCovers[i] |= 4;  // north wall
            if (dist2(x, y, 0, -centerY) <= range2) craneCovers[i] |= 8; // south wall
        }

        // We iterate through the O(N^4) possible arrangements with nested loops
        int bestCnt = 999;
        for (int cA = 0; cA < craneCnt; cA++) {

            // The outer loop checks to see if a single crane can cover all 4 walls
            int coverageA = craneCovers[cA];
            if (Integer.bitCount(coverageA) == 4) {
                bestCnt = 1;    // if we've found a solution with 1 crane
                break;          // we don't need to check any more arrangements
            }
            // if we've found a solution with 2 cranes
            // we don't need to look at any more 2-, 3-, or 4-crane arrangements
            if (bestCnt == 2) continue;

            // otherwise we look at all the 2-, 3-, and 4-crane arrangements
            // which start with this crane
            for (int cB = cA + 1; cB < craneCnt; cB++) {

                // the inner loops follow the same pattern
                int coverageB = coverageA | craneCovers[cB];
                if (Integer.bitCount(coverageB) == 4) {
                    bestCnt = 2;
                    break;
                }
                if (bestCnt == 3) continue;

                for (int cC = cB + 1; cC < craneCnt; cC++) {
                    int coverageC = coverageB | craneCovers[cC];
                    if (Integer.bitCount(coverageC) == 4) {
                        bestCnt = 3;
                        break;
                    }
                    if (bestCnt == 4) continue;

                    for (int cD = cC + 1; cD < craneCnt; cD++) {
                        int coverageD = coverageC | craneCovers[cD];
                        if (Integer.bitCount(coverageD) == 4) {
                            bestCnt = 4;
                            break;
                        }
                    }
                }
            }
        }

        // if we haven't found an arrangement with 4 or fewer cranes then
        // the job is impossible
        if (bestCnt <= 4) System.out.println(bestCnt);
        else System.out.println("Impossible");

        s.close();
    }
}
