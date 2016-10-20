package kattis20161007;

import java.util.Scanner;

/**
 * This is an optimization problem: find the smallest set of pilots such that
 * their routes form a spanning tree over the set of cities. If each pilot could
 * have routes between multiple cities this problem would be significantly
 * harder. I believe in that case it could be solved with a variant of Prim's
 * algorithm.
 *
 * However, because each pilot has exactly one route/edge between a pair of
 * cities, and we are guaranteed that the city graph is fully connected, the
 * problem becomes trivial. With N cities, our minimum spanning tree will
 * contain exactly N-1 edges/pilots.
 * 
 * https://open.kattis.com/contests/rt95cv/problems/flyingsafely
 *
 * @author jroush
 */
public class FlyingSafely {

    public static void main(String[] argv) {
        Scanner s = new Scanner(System.in);
        for (int cases = s.nextInt(); cases > 0; cases--) {
            int cities = s.nextInt(), pilots = s.nextInt();
            for (int i = 0; i < pilots; i++) {
                // we can throw away the actual route information -
                // the city graph is guaranteed to be connected and we 
                // already know how many pilots we need.
                int src = s.nextInt(), dst = s.nextInt();
            }
            System.out.println(cities - 1);
        }
        s.close();
    }

}
