import java.util.*;

/**
 * This problem involves some interesting mathematics. The algorithm described
 * in the problem statement uses <em>Newton's Series</em>, which is similar to
 * Taylor's series but uses finite differences instead of derivatives. Like
 * Taylor's series, Newton's series is often used for building approximations of
 * functions, and it is particularly well-suited to the discrete, iterative
 * approximations used in many numerical simulations. 
 * <p>
 * Newton's series builds up a polynomial representation of a function by
 * sampling it at discrete points {@code x0, x0+dx, x0+2*dx, ...} :
 * 
 * <pre>
 *      F(x0 + m*dx) = 
 *         F(x0)
 *         + m * ( F(x0 + dx) - F(x0) )
 *         + (1/2)*m*(m-1) * ( F(x0 + 2*dx) - 2*F(x0 + dx) + F(x0) )
 *         + (1/6)*m*(m-1)*(m-2) * ( F(x0 + 3*dx) - 3*F(x0 + 2*dx) + 3*F(x0 + dx) - F(x0) )
 *         + ...
 *               
 *        = SUM from L = 0 to CUTOFF of 
 *          (m choose L) * (Lth forward difference of F(x0) in steps of dx)
 * </pre>
 * 
 * where:
 * 
 * <pre>
 *      Lth forward difference of F(x0) in steps of dx =
 *          SUM from y = 0 to L of 
 *              (-1)^(L-y) * (L choose y) * F(x0 + y*dx)
 * </pre>
 * 
 * If we take CUTOFF to be infinity, Newton's series is an infinite series which
 * perfectly represents our function. If we take CUTOFF to be N, then Newton's
 * series is an Nth order polynomial which passes through the points
 * {@code x0, x0+dx, x0+2*dx, ..., x0+N*dx} . With CUTOFF=1, for example, we
 * would obtain a straight line passing through F(x0) and F(xo + dx).
 * <p>
 * 
 * 
 * https://open.kattis.com/contests/rt95cv/problems/plot
 *
 * @author jroush
 */
public class PlottingPolynomials {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // read coefficients
        int N = s.nextInt();
        int[] a = new int[N + 1];
        for (int i = N; i >= 0; i--) {
            a[i] = s.nextInt();
        }
        s.close();

        // precompute the values of the polynomial p(0) ... p(N)
        int[] p = new int[N + 1];
        for (int x = 0; x <= N; x++) {
            int xi = 1;
            for (int i = 0; i < a.length; i++) {
                p[x] += a[i] * xi;
                xi *= x;
            }
        }

        // compute the coefficients C_0 ... C_N (the nth forward difference)
        int[] t = new int[N + 1];
        t[0] = 1;
        System.out.print(p[0]);
        for (int l = 1; l <= N; l++) {
            int C = 0;
            for (int x = l; x > 0; x--) {
                t[x] = t[x - 1] - t[x];
                C += t[x] * p[x];
            }
            t[0] = -t[0];
            C += t[0] * p[0];
            System.out.print(" " + C);
        }
    }
}
