package kattis20160313;
import java.io.*;
import java.util.*;

public class SkiHill {

    public final int    N;

    public final int[]  heights;

    public final long[] costs;  // cumulative cost to ride back to start

    public final long[] funs;   // cumulative fun to ride from start

    public final long[] maxfuns;    // cumulative max fun to ride from start

    public SkiHill(int N, Scanner input) {
        this.N = N;
        this.heights = new int[N + 1];
        this.costs = new long[N + 1];
        this.funs = new long[N + 1];
        this.maxfuns = new long[N + 1];
        long hl = heights[0] = input.nextInt();
        long cost = 0, fun = 0, maxfun = 0;
        for (int i = 1; i <= N; i++) {
            int h = heights[i] = input.nextInt();
            cost += hl + h;
            costs[i] = cost;
            long dh = hl - h;
            fun += dh * dh * Long.signum(dh);
            if (fun < 0) fun = 0;
            else if (fun > maxfun) maxfun = fun;
            funs[i] = fun;
            maxfuns[i] = maxfun;
            hl = h;
        }
    }

    public final void update(final int idx, final int V) {
        heights[idx] = V;
        final int st = idx == 0 ? 0 : idx - 1;
        long hl = heights[st];
        long cost = costs[st], fun = funs[st], maxfun = maxfuns[st];
        for (int i = st + 1; i <= N; i++) {
            int h = heights[i];
            cost += hl + h;
            costs[i] = cost;
            long dh = hl - h;
            fun += dh * dh * Long.signum(dh);
            if (fun < 0) fun = 0;
            else if (fun > maxfun) maxfun = fun;
            funs[i] = fun;
            maxfuns[i] = maxfun;
            hl = h;
        }
    }

    public final long query(final int X, final long K) {
        int end = Arrays.binarySearch(costs, K + costs[X]);
        if (end < 0) end = -end - 2;
        return maxfuns[end];
    }

    public final long oldQuery(final int X, final long K) {
        long cost = 0, fun = 0, maxfun = 0;
        for (int i = 1; i <= N; i++) {
            if (i > X) {
                cost += heights[i - 1] + heights[i];
                if (cost > K) break;
            }
            final long dh = heights[i - 1] - heights[i];
            fun += dh * dh * Long.signum(dh);
            if (fun < 0) fun = 0;
            else if (fun > maxfun) maxfun = fun;
        }
        return maxfun;
    }

    public static void sawTest(PrintStream out, int N, int Q) {
        final int maxh = 1000000, dd = 4;
        int dx = N / (dd + 1);
        N = dx * (dd + 1) + 1;
        out.println(N + " " + Q);
        for (long i = 0; i < dx; i++) {
            out.print(maxh);
            out.print(' ');
            for (long j = 0; j < dd; j++) {
                out.print(j * maxh / dd);
                out.print(' ');
            }
        }
        out.println(maxh + " " + 0);
        for (long q = 0; q < Q; q++) {
            if (q % 2 > 0) {
                out.print("1 ");
                out.print(q * N / Q / 2);
                out.print(' ');
                out.println(q * maxh * (dd + 1) * dx / Q);
            } else {
                out.print("0 ");
                out.print(q * N / Q);
                out.print(' ');
                out.println(5);
            }
        }
    }

    public static void fuzzTest(PrintStream out, int N, int Q) {
        Random rand = new Random();
        final int hbound = 1000000 + 1;
        final long kbound = hbound * (long)N / 8;
        out.println(N + " " + Q);
        for (long i = 0; i <= N; i++) {
            out.print(rand.nextInt(hbound));
            out.print(' ');
        }
        out.println();
        for (long q = 0; q < Q; q++) {
            if (q % 2 > 0) {
                out.print("1 ");
                out.print(rand.nextInt(N + 1));
                out.print(' ');
                out.println((rand.nextLong() >>> 1) % kbound);
            } else {
                out.print("0 ");
                out.print(rand.nextInt(N + 1));
                out.print(' ');
                out.println(rand.nextInt(hbound));
            }
        }
    }

    public static void main(String[] args) {
        if (args.length > 1) {
            String test = args[0];
            int N = Integer.parseInt(args[1]);
            int Q = Integer.parseInt(args[2]);
            if (test.equalsIgnoreCase("sawtest")) {
                sawTest(System.out, N, Q);
            } else if (test.equalsIgnoreCase("fuzztest")) {
                fuzzTest(System.out, N, Q);
            } else throw new RuntimeException("Unknown test " + test);
            return;
        }

        Scanner s = new Scanner(System.in);
        final int N = s.nextInt(), Q = s.nextInt();
        SkiHill hill = new SkiHill(N, s);
        for (int q = 0; q < Q; q++) {
            final int query = s.nextInt();
            switch (query) {
            case 0:
                hill.update(s.nextInt(), s.nextInt());
                break;
            case 1: {
                int X = s.nextInt();
                long K = s.nextLong();
                //long a = hill.oldQuery(X, K);
                long b = hill.query(X, K);
                System.out.println(b);
                //if (a != b) System.err.println(a + " != " + b);
                break;
            }
            default:
                throw new RuntimeException("Invalid query: " + query);
            }
        }
        s.close();
    }
}
