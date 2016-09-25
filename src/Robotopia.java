import java.util.*;

public class Robotopia {

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);

        for (int N = s.nextInt(); N > 0; N--) {

            int l1 = s.nextInt(), a1 = s.nextInt();
            int l2 = s.nextInt(), a2 = s.nextInt();
            int lt = s.nextInt(), at = s.nextInt();

            /* 
             * We are attempting to solve the following set of linear equations
             *      l1*q1 + l2*q2 = lt
             *      a1*q1 + a2*q2 = at
             * which we can write this in matrix form:
             *    | l1  l2 |   | q1 | = | lt |
             *    | a1  a2 | * | q2 | = | at |
             * if we multiply both sides by the inverse matrix:
             *     | q1 |             | a2   -l2 |   | lt |
             *     | q2 | = (1/det) * | -a1   l1 | * | at |
             * where the determinant of the matrix is
             *       det  = l1 * a2 - a1 * l2
             * remember your linear algebra!
             * 
             * Note that we don't actually do any division - we want integer
             * solutions, and division requires floating point numbers -> too messy
             */
            int det = l1 * a2 - a1 * l2;
            int c1 = lt * a2 - at * l2;
            int c2 = -lt * a1 + at * l1;

            if (det == 0) {
                /*
                 * If the determinant of the matrix is zero, the matrix is
                 * singular, meaning that there is no unique pair of real
                 * numbers (q1,q2) that solve the equation.
                 * However, since we are interested only in strictly positive 
                 * integer solutions, we may yet have a unique answer.
                 */
                if (lt * a1 == l1 * at) {
                    /*
                     * Since det == 0, we know that l1/a1 == l2/a2.
                     * If lt/at is the same value then the two equations we started
                     * with are linear multiples of each other, and the problem
                     * is essentially reduced from two dimensions to one dimension.
                     * 
                     * In that case, we just search for a brute-force solution
                     * to the single equation 
                     *      l1*q1 + l2*q2 == lt
                     */
                    int solcnt = 0, solq1 = 0, solq2 = 0;
                    for (int q1 = lt / l1; q1 > 0; q1--) {
                        int q2l2 = lt - q1 * l1;
                        if (q2l2 >= l2 && q2l2 % l2 == 0) {
                            solcnt++;
                            solq1 = q1;
                            solq2 = q2l2 / l2;
                        }
                    }
                    if (solcnt == 1) System.out.format("%d %d\n", solq1, solq2);
                    else System.out.println("?"); // 1D equation has no unique solution
                } else System.out.println("?"); // problem is singular but not 1D

            } else if (c1 % det == 0 && c2 % det == 0) {
                /*
                 * If det != 0 and both c1 and c2 are evenly divisible by det
                 * then the problem has a unique 2D integer solution.
                 * We just need to make sure that both values are strictly positive.
                 */
                int q1 = c1 / det;
                int q2 = c2 / det;
                if (q1 > 0 && q2 > 0) System.out.format("%d %d\n", q1, q2);
                else System.out.println("?"); // one or both quantities are <= 0

            } else {
                /*
                 * If det != 0 and c1 or c2 is not evenly divisible by det,
                 * the problem has a unique but non-integer solution.
                 */
                System.out.println("?");
            }
        }
        s.close();

    }
}
