package kattis20161007;
import java.util.*;

/**
 * This problem involves some interesting mathematics. The algorithm described
 * in the problem statement uses <em>Newton's Series</em>, which is similar to
 * Taylor's series but uses finite differences instead of derivatives. Like
 * Taylor's series, Newton's series is often used for building approximations of
 * functions, and it is particularly well-suited to the discrete, iterative
 * approximations used in many numerical simulations.
 * <p>
 * In this problem, we are given a polynomial of degree N with coefficients
 * {@code a0, a1, a2, ... aN} such that
 * 
 * <pre>
 *    p(x) = SUM from i = 0 to N of ( ai * x^i )
 * </pre>
 * 
 * The problem posits an efficient algorithm for sampling this polynomial using
 * some mystery coefficients {@code C0, C1, C2, ... CN}:
 * 
 * <pre>
 *    p(0) = C0,         t10 = C1,         t20 = C2,         ...   tN0 = CN
 *    p(1) = p(0)+t10,   t11 = t10+t20,    t21 = t20+t30,    ...   tN1 = CN
 *    p(2) = p(1)+t11,   t12 = t11+t21,    t22 = t21+t31,    ...   tN2 = CN
 *        ...               ...               ...                     ...
 *  p(x+1) = p(x)+t1x, t1x+1 = t1x+t2x,  t2x+1 = t2x+t3x,    ... tNx+1 = CN
 * </pre>
 * 
 * If we work backwards to fill in all the variables {@code t} for the
 * appropriate combinations of constants C, we find:
 * 
 * <pre>
 *    p(0) = C0,         t10 = C1,         t20 = C2,         ...   tN0 = CN
 *    p(1) = C0+C1,      t11 = C1+C2,      t21 = C2+C3,      ...   tN1 = CN
 *    p(2) = C0+2*C1+C2, t12 = C1+2*C2+C3, t22 = C2+2*C3+C4, ...   tN2 = CN
 *        ...
 *    p(x) = SUM from L = 0 to x of ( (x choose L) * C_L )
 * </pre>
 * 
 * This is very similar to the form of Newton's series for general functions:
 * 
 * <pre>
 *      F(x0 + m*dx) = 
 *          SUM from L = 0 to N of 
 *              (m choose L) * (Lth forward difference of F(x0) in steps of dx)
 *      
 *      take x0 = 0, dx = 1, m = x, so that F(x0 + m*dx) == p(x)
 * </pre>
 *
 * Therefore the coefficients {@code C_L} that we are interested in are exactly
 * the "Lth forward differences":
 * 
 * <pre>
 *      Lth forward difference of F(x0) in steps of dx =
 *          SUM from y = 0 to L of 
 *              (-1)^(L-y) * (L choose y) * F(x0 + y*dx)
 * </pre>
 * 
 * and therefore:
 * 
 * <pre>
 *      C_L = SUM from y = 0 to L of  ( (-1)^(L-y) * (L choose y) * p(y) )
 * </pre>
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
        for (int y = 0; y <= N; y++) {
            int yi = 1;
            for (int i = 0; i < a.length; i++) {
                p[y] += a[i] * yi;
                yi *= y;
            }
        }

        // compute the coefficients C_0 ... C_N (the Lth forward difference)
        int[] choose = new int[N + 1];
        choose[0] = 1;
        System.out.print(p[0]); // first constant C0
        for (int L = 1; L <= N; L++) {
            int C = 0;
            for (int y = L; y > 0; y--) {
                // we calculate the values of (-1)^(L-y) 
                // from the previous values (-1)^(L-y-1) * (L-1 choose y)
                // we do this in-place, overwriting the old values,
                // which is why we iterate backwards
                choose[y] = choose[y - 1] - choose[y];
                C += choose[y] * p[y];
            }
            choose[0] = -choose[0]; // calculate new value of (-1)^L for y==0
            C += choose[0] * p[0];
            System.out.print(" " + C); // print C_L
        }
    }
}
