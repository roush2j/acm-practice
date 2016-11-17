package kattis20161029;

import java.util.*;

public class LostInTranslation {
    
    public static final int INFINITY = Integer.MAX_VALUE;

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int langcnt = s.nextInt() + 1, transcnt = s.nextInt();
        Map<String, Integer> langs = new HashMap<>(langcnt);
        langs.put("English", 0);
        for (int i = 1; i < langcnt; i++) {
            langs.put(s.next(), i);
        }
        int[] costs = new int[langcnt * langcnt];
        Arrays.fill(costs, INFINITY);
        for (int i = 0; i < transcnt; i++) {
            String sl1 = s.next(), sl2 = s.next();
            int l1 = langs.get(sl1), l2 = langs.get(sl2);
            costs[l1 * langcnt + l2] = costs[l2 * langcnt + l1] = s.nextInt();
        }
        s.close();
        
        final int[] mincost = new int[langcnt];
        final int[] minsteps = new int[langcnt];
        Arrays.fill(mincost, INFINITY);
        Arrays.fill(minsteps, INFINITY);
        minsteps[0] = mincost[0] = 0;
        PriorityQueue<Integer> fringe = new PriorityQueue<>((a,b) -> {
            int cmp = Integer.compare(minsteps[a], minsteps[b]);
            if (cmp != 0) return cmp;
            else return Integer.compare(mincost[a], mincost[b]);
        });
        fringe.add(0);
        while (!fringe.isEmpty()) {
            int idx = fringe.remove();
            for (int n = 0; n < langcnt; n++) {
                int ncost = costs[idx * langcnt + n];
                if (ncost == INFINITY) continue;
                int nsteps = minsteps[idx] + 1;
                if (nsteps > minsteps[n]) continue;
                if (nsteps == minsteps[n] && ncost >= mincost[n]) continue;
                minsteps[n] = nsteps;
                mincost[n] = ncost;
                fringe.add(n);
            }
        }
        
        int sum = 0;
        for (int i = 0; i < langcnt; i++) {
            if (mincost[i] == INFINITY || minsteps[i] == INFINITY) {
                System.out.println("Impossible");
                return;
            }
            sum += mincost[i];
        }
        System.out.println(sum);
    }

}
