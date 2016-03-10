import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class SkiHill {

    public final int   N;

    public final int[] heights;

    public SkiHill(int N) {
        this.N = N;
        this.heights = new int[N + 1];
    }

    public void update(final int i, final int V) {
        heights[i] = V;
    }

    public long query(final int X, final long K) {
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
                long N = 10;
                long dx = N / (dd + 1);
                N = dx * (dd + 1) + 1;
                out.println(N + " 1");
                for (int i = 0; i < dx; i++) {
                    out.print(maxh);
                    out.print(' ');
                    for (int j = 0; j < dd; j++) {
                        out.print(j * maxh / dd);
                        out.print(' ');
                    }
                }
                out.println(maxh + " " + 0);
                out.println("1 1 " + (1L << 32 + 4));
                out.println();
                out.println("Cost: " + (maxh * (dd + 1) * dx));
                out.println("Fun: "
                        + (long) (maxh * maxh * (dx * (dd - 1.0) / dd + 1.0)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return;
        }

        Scanner s = new Scanner(System.in);
        final int N = s.nextInt(), Q = s.nextInt();
        SkiHill hill = new SkiHill(N);
        for (int i = 0; i <= N; i++) {
            hill.heights[i] = s.nextInt();
        }
        for (int q = 0; q < Q; q++) {
            final int query = s.nextInt();
            switch (query) {
            case 0:
                hill.update(s.nextInt(), s.nextInt());
                break;
            case 1:
                System.out.println(hill.query(s.nextInt(), s.nextLong()));
                break;
            default:
                throw new RuntimeException("Invalid query: " + query);
            }
        }
        s.close();
    }
}
