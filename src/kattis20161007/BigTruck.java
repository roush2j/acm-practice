package kattis20161007;

import java.util.*;

/**
 * This is a classic shortest-path search algorithm. The only wrinkle is that
 * when there are multiple "shortest" paths we break the tie by choosing the one
 * which maximizes the number of items from visited nodes.
 * 
 * Because our graph has a single starting point and end point, is undirected
 * (roads always go in both directions), and is guaranteed to contain strictly
 * positive edge weights, we can use the very well-known Dijkstra's algorithm.
 * 
 * https://open.kattis.com/contests/rt95cv/problems/bigtruck
 *
 * @author jroush
 */
public class BigTruck {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // read number of locations and number of items at each location
        int N = s.nextInt();
        int[] items = new int[N];
        for (int i = 0; i < N; i++) {
            items[i] = s.nextInt();
        }

        // read number of roads and their endpoints
        int roadCnt = s.nextInt();
        int[] roads = new int[N * N]; // length of road, or 0 if no road
        for (int i = 0; i < roadCnt; i++) {
            int src = s.nextInt() - 1, dst = s.nextInt() - 1;
            roads[src * N + dst] = roads[dst * N + src] = s.nextInt();
        }

        // Find the best path using Dijkstra's algorithm
        // This amounts to a breadth-first search where we examine nodes in the
        // road graph from "closest/highest-item-count" to 
        // "farthest/lowest-item-count".
        final int[] shortestPath = new int[N];
        final int[] pathItems = new int[N];
        Queue<Integer> fringe = new PriorityQueue<>((a, b) -> {
            int csp = Integer.compare(shortestPath[a], shortestPath[b]);
            if (csp == 0) return -Integer.compare(pathItems[a], pathItems[b]);
            else return csp;
        });
        Arrays.fill(shortestPath, Integer.MAX_VALUE); // initially all distances are "infinite"
        shortestPath[0] = 0;        // distance to starting point is zero
        pathItems[0] = items[0];    // items from starting point
        fringe.add(0);
        while (!fringe.isEmpty()) {
            int src = fringe.remove();
            if (src == N - 1) break; // we have found best path to final destination
            for (int dst = 0; dst < N; dst++) {
                // check if there is a better path to dst via src
                int len = roads[src * N + dst];
                if (len == 0) continue; // no road
                int newpath = shortestPath[src] + len;
                if (newpath > shortestPath[dst]) {
                    continue; // road is longer than current best for this destination
                }
                int newcnt = pathItems[src] + items[dst];
                if (newpath == shortestPath[dst] && newcnt <= pathItems[dst]) {
                    continue; // road is same length and item count is not better
                }
                // path is better, update dst and add to queue
                shortestPath[dst] = newpath;
                pathItems[dst] = newcnt;
                fringe.add(dst);
            }
        }

        // print the path length and item count, or 'impossible' if the destination
        // was not reachable over the given roads
        if (shortestPath[N - 1] == Integer.MAX_VALUE) {
            System.out.println("impossible");
        } else {
            System.out.format("%d %d\n", shortestPath[N - 1], pathItems[N - 1]);
        }
        s.close();
    }

}
