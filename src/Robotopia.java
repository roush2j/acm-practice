import java.util.*;

public class Robotopia {

    public static boolean compare(int sol1, int sol2) {
        boolean brutefnd = sol1 < Integer.MAX_VALUE - 10;
        boolean matfnd = sol2 < Integer.MAX_VALUE - 10;
        return matfnd == brutefnd && (!matfnd || sol2 == sol1);
    }

    public static int matSolve(int l1, int l2, int a1, int a2, int lt, int at) {
        /*  mat   | l1  l2 | * | q1 | = | lt |
         *        | a1  a2 |   | q2 | = | at |
         *
         *  inverse matrix * totals:
         *       det  = l1 * a2 - a1 * l2
         *     | q1 | =        1/           * | a2   -l2 |  * | lt |
         *     | q2 |   (l1 * a2 - a1 * l2)   | -a1   l1 |    | at |
         */
        int det = l1 * a2 - a1 * l2;
        int c1 = lt * a2 - at * l2;
        int c2 = -lt * a1 + at * l1;

        if (det == 0) {
            if (lt * a1 == l1 * at) {
                int solcnt = 0, solq1 = 0, solq2 = 0;
                for (int q1 = lt / l1; q1 > 0; q1--) {
                    int q2l2 = lt - q1 * l1;
                    if (q2l2 >= l2 && q2l2 % l2 == 0) {
                        solcnt++;
                        solq1 = q1;
                        solq2 = q2l2 / l2;
                    }
                }
                if (solcnt == 1) return (solq1 << 12) | solq2;
                else return Integer.MAX_VALUE - 1;
            } else return Integer.MAX_VALUE - 2; // singular & not matched
        } else if (c1 % det == 0 && c2 % det == 0) {
            int q1 = c1 / det;
            int q2 = c2 / det;
            if (q1 > 0 && q2 > 0) return (q1 << 12) | q2;
            else return Integer.MAX_VALUE - 3; // "non-positive";
        } else {
            return Integer.MAX_VALUE - 4; // "non-integer";
        }
    }

    public static int bruteforce(int l1, int l2, int a1, int a2, int lt, int at) {
        int solcount = 0, solq1 = 0, solq2 = 0;
        for (int q1 = 1; q1 < 10000; q1++) {
            for (int q2 = 1; q2 < 10000; q2++) {
                if (q1 * l1 + q2 * l2 == lt && q1 * a1 + q2 * a2 == at) {
                    solcount++;
                    solq1 = q1;
                    solq2 = q2;
                }
            }
        }
        if (solcount == 1) return (solq1 << 12) | solq2;
        else return Integer.MAX_VALUE - 1;
    }

    public static void test() {
        Random r = new Random(576897);
        for (long counter = 0; counter < 100000000; counter++) {

            int l1 = r.nextInt(49) + 1, a1 = r.nextInt(49) + 1;
            int l2 = r.nextInt(49) + 1, a2 = r.nextInt(49) + 1;
            int lt = r.nextInt(49) + 1, at = r.nextInt(49) + 1;

            int brutesol = bruteforce(l1, l2, a1, a2, lt, at);
            int matsol = matSolve(l1, l2, a1, a2, lt, at);
            if (!compare(brutesol, matsol)) {
                System.out.format("FAIL: %d,%d,%d,%d -> %d,%d\n", l1, a1, l2,
                        a2, lt, at);
            }

            if ((counter++ & 0xFFFFF) == 0) {
                System.out.println("Test " + counter);
            }
        }

    }

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);

        for (int N = s.nextInt(); N > 0; N--) {

            int l1 = s.nextInt(), a1 = s.nextInt();
            int l2 = s.nextInt(), a2 = s.nextInt();
            int lt = s.nextInt(), at = s.nextInt();

            ////            int brutesol = bruteforce(l1, l2, a1, a2, lt, at);
            //            int matsol = matSolve(l1, l2, a1, a2, lt, at);
            ////            if (!compare(brutesol, matsol)) {
            ////                System.out.format("FAIL: %d,%d,%d,%d -> %d,%d\n", l1, a1, l2,
            ////                        a2, lt, at);
            ////            }
            //
            //            if (matsol < Integer.MAX_VALUE - 10) {
            //                System.out.format("%d %d\n", (matsol >>> 12), (matsol & 0xFFF));
            //            } else System.out.println('?');

            /*  mat   | l1  l2 | * | q1 | = | lt |
             *        | a1  a2 |   | q2 | = | at |
             *
             *  inverse matrix * totals:
             *       det  = l1 * a2 - a1 * l2
             *     | q1 | =        1/           * | a2   -l2 |  * | lt |
             *     | q2 |   (l1 * a2 - a1 * l2)   | -a1   l1 |    | at |
             */
            int det = l1 * a2 - a1 * l2;
            int c1 = lt * a2 - at * l2;
            int c2 = -lt * a1 + at * l1;

            if (det == 0) {
                if (lt * a1 == l1 * at) {
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
                    else System.out.println("?");
                } else System.out.println("?");
            } else if (c1 % det == 0 && c2 % det == 0) {
                int q1 = c1 / det;
                int q2 = c2 / det;
                if (q1 > 0 && q2 > 0) System.out.format("%d %d\n", q1, q2);
                else System.out.println("?");
            } else {
                System.out.println("?");
            }
        }
        s.close();

    }
}
