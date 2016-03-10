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

    public static void main(String[] args) {
        if (false) {
            try {
                PrintStream out = new PrintStream("SkiTests");
                final long maxh = 1000000, dd = 4;
                long N = 100000, Q = 10000;
                long dx = N / (dd + 1);
                N = dx * (dd + 1) + 1;
                out.println(N + " " + Q);
                for (int i = 0; i < dx; i++) {
                    out.print(maxh);
                    out.print(' ');
                    for (int j = 0; j < dd; j++) {
                        out.print(j * maxh / dd);
                        out.print(' ');
                    }
                }
                out.println(maxh + " " + 0);
                for (int q = 0; q < Q; q++) {
                    if (q % 16 > 0) {
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return;
        } else if (true) {
            try {
                System.setIn(new FileInputStream("SkiTests"));
                System.setOut(new PrintStream("SkiOutput"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        long starttime = System.nanoTime();

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
                long a = hill.oldQuery(X, K);
                long b = hill.query(X, K);
                System.out.println(b);
                if (a != b) System.err.println(a + " != " + b);
                break;
            }
            default:
                throw new RuntimeException("Invalid query: " + query);
            }
        }
        s.close();

        System.err.println((System.nanoTime() - starttime) / 1000000000.0);
    }
}
