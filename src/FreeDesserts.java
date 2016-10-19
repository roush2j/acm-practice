import java.util.*;

/**
 * This is a rather tough problem. It can be solved with a combination of
 * recursive constraint-based search and dynamic programming.
 * 
 * We are given an integer P with digits P0, P1, P2, ... Pn where
 * 
 * <pre>
 *   P = P0 + 10*P1 + 100*P2 + ... + 10^n * Pn
 * </pre>
 * 
 * We want to find all possible numbers B and D, such that
 * 
 * <pre>
 *   B + D = P
 *   B < D
 * </pre>
 * 
 * Further, the sets of digits {@code [B0,B1,B2,...]} and {@code [D0,D1,D2,...]}
 * and {@code [P0,P1,P2,...]} are disjoint, meaning that B, D, and P do not
 * share any digits.
 * 
 * Our search algorithm takes advantage of the fact that there are a limited
 * number of possibilities for {@code Bn} and {@code Dn}. For example, if
 * {@code P = 4114} (and thus {@code P3 = 4}), then there are only 5
 * possibilities for {@code B3} and {@code D3} that add up to 4:
 * 
 * <pre>
 *      B3 = 0, 1, 2, 3, 4
 *      D3 = 4, 3, 2, 1, 0
 *     Sum = 4, 4, 4, 4, 4
 * </pre>
 * 
 * Of these possibilities, four involve digits that already appear in {@code P}
 * and one involves assigning the same digit (2) to both {@code B} and {@code D}
 * . Therefore, we can discard all of these possibilities as invalid!
 * 
 * Of course, we must also be concerned with carries from adding up the lower
 * digits {@code B0-B2, D0-D2}. To account for carries we also consider the 4
 * possibilities that add up to 3:
 * 
 * <pre>
 *      B3 = 0, 1, 2, 3
 *      D3 = 3, 2, 1, 0
 *     Sum = 3, 3, 3, 3
 * </pre>
 * 
 * Again, two of these possibilities can immediately be discarded because they
 * involve the digit 1, which already appears in {@code P}. Further, we can
 * discard the assignment {@code B3 = 3} because that would inevitably result in
 * {@code B > D}, which is invalid. Therefore, we know for sure that
 * {@code B3 = 0, D3 = 3}, and thus {@code B = 0??? and D = 3???}.
 * 
 * Now we recursively consider {@code P2}, which is 1. Because we assumed a
 * carry ({@code B3 + D3 = P3 - 1}), we are looking for
 * {@code B2 + D2 = P2 + 10 = 11}. Of course we must account for the possibility
 * of another carry from lower digits, so we must also consider
 * {@code B2 + D2 = P2 + 10 - 1 = 10}. We can eliminate assignments that involve
 * 1 or 4 because they in {@code P}. We can also eliminate the assignment
 * {@code B2 = 3} because 3 now appears in {@code D}. This leaves 8 possible
 * assignments for {@code B2, D2}:
 * 
 * <pre>
 *      B2 =  2,  5,  6,  8,  9,  2,  7,  8
 *      D2 =  9,  6,  5,  3,  2,  8,  3,  2
 *     Sum = 11, 11, 11, 11, 11, 10, 10, 10
 * </pre>
 * 
 * For each of these possibilities we now recursively look at the assignments
 * for {@code B1,D1} and {@code B0,D0}. If we find a legal assignment for
 * {@code B0, D0} that does not assume a carry then we have a complete legal
 * value for {@code B, D}.
 * 
 * The last - and perhaps most important - part of a correct solution is dynamic
 * programming. The problem only requires us to print the first 5000 valid
 * solutions for {@code B, D}, but we must count <b>all</b> valid solutions.
 * Since P can be up to 18 digits long and our search tree has an average
 * branching factor of ~5, there may be trillions of leaves in a full search -
 * far too many to visit within the prescribed time limits.
 * 
 * Once we stop printing solutions, however, there are at most about 500,000
 * possible unique combinations of arguments to our search function (~4^9
 * possible digit assignments x 2 possibilities for assumed carry). That means
 * that for all digits after the ninth, the number of nodes at that depth in the
 * search tree will greatly exceed the number of unique combinations of search
 * arguments. Therefore that we will be <b>repeatedly calling the search
 * function with the same arguments</b>. This is a textbook case for dynamic
 * programming. We have an expensive sub-problem (the number of valid solutions
 * from the 9th digit onward, given a digit map and a carry state) which is
 * being solved repeatedly. We can <b>cache</b> the number of solutions the
 * first time we calculate it for a particular combination of arguments. This
 * cuts the runtime of the algorithm down by many orders of magnitude.
 * 
 * https://open.kattis.com/contests/rt95cv/problems/freedesserts
 *
 * @author jroush
 */
public class FreeDesserts {

    public static void main(String[] args) {

        // read in input - note that an 18 digit number *will* fit in a 
        // signed 64bit integer
        Scanner s = new Scanner(System.in);
        long P = s.nextLong();
        s.close();

        // Assign all digits in input to PRICE
        DigitMap digits = new DigitMap();
        for (long x = P; x > 0; x /= 10) {
            int digit = (int) (x % 10);
            digits.mapDigit(x, digit, DigitMap.PRICE);
        }

        // allocate solution list and cache
        List<String> solutions = new ArrayList<>(5000);
        Map<Integer, Long> cache = new HashMap<>();

        // recursively search for solutions
        long cnt = solve(100_000_000_000_000_000L, P, 0, 0, digits, solutions,
                cache);

        // print solution count and first 5000 solutions
        System.out.println(cnt);
        for (String sol : solutions) {
            System.out.println(sol);
        }
    }

    /** A simple class for tracking which digits are assigned to what. */
    public static class DigitMap {

        /** Symbolic constants. FREE means the digit is unassigned. */
        public static final int FREE       = 0, PRICE = 1, BEV = 2, DISH = 3;

        /**
         * We store digit assignments as a bitmask. Each of the digits 0-9 is
         * stored in the two bits {@code 2*d} and {@code 2*d+1}. This is very
         * helpful - it allows us to express each of the possible 4^10 digit
         * assignments as a unique integer. We use this as part of our key for
         * caching results.
         */
        private int             digitState = 0;

        /**
         * We keep track of how many times a digit has appeared in its assigned
         * number. This gets incremented in {@code mapDigit()} and decremented
         * in {@code unmapDigit()}. When it reaches zero, we know the digit is
         * FREE again.
         */
        private int[]           digitCount = new int[10];

        public boolean mapDigit(long pNum, int digit, int state) {
            if (pNum == 0 && digit == 0) return true; // do not map leading zeros
            int s = (digitState >> (2 * digit)) & 0x3;
            if (s == FREE) digitState |= state << (2 * digit);
            else if (s != state) return false;
            digitCount[digit]++;
            return true;
        }

        public void unmapDigit(long pNum, int digit) {
            if (pNum == 0 && digit == 0) return; // do not unmap leading zeros
            digitCount[digit]--;
            if (digitCount[digit] == 0) digitState &= ~(0x3 << (2 * digit));
        }

        public String toString() {
            StringBuilder sb = new StringBuilder('[');
            char names[] = new char[] { 'F', 'P', 'B', 'D' };
            for (int i = 0, s = digitState; i < 10; i++, s >>= 2) {
                if (i > 0) sb.append(", ");
                sb.append(i).append(":").append(names[s & 0x3]);
                sb.append('x').append(digitCount[i]);
            }
            return sb.append(']').toString();
        }
    }

    /** Our recursive search function. */
    public static long solve( //
            long mult,        // multiplier for this digit of the solution (power of 10)
            long pPrice,      // remaining price (P = pPrice + pBev + pDish)
            long pBev, long pDish, // partial values for beverage and dish
            DigitMap digits,  // map of digit states
            List<String> solutions,  // list of solutions (up to first 5000)
            Map<Integer, Long> cache  // cache of solution counts at level 9
    ) {
        // base case for recursive search
        if (mult == 0) {
            if (pPrice > 0) {
                // solution invalid - we assumed a carry but have nothing to carry from
                return 0;
            } else {
                // valid solution
                if (solutions.size() < 5000) {
                    solutions.add(String.format("%d %d", pBev, pDish));
                }
                return 1;
            }
        }

        // Compute the next digit (between 0-9) of P
        int priced = (int) (pPrice / mult);
        if (mult == 100_000_000 && solutions.size() >= 5000) {
            // after we have printed out the first 5000 solutions,
            // we start looking up solution counts in the cache to avoid
            // unnecessary repetition.
            // Our key is the current recursive state minus the partial solution
            // (so just the digit map and carry bit).
            // We only keep a cache at one level in the recursion - digit 9 -
            // we could keep caches at all levels, but that would cost a lot 
            // more space.
            Integer key = digits.digitState | (priced << 24);
            Long cnt = cache.get(key);
            if (cnt != null) return cnt;
        }

        // find the range of bev digits that could possibly add up to our price
        int minbevd = Math.max(0, priced - 10);
        int maxbevd = Math.min(9, priced);
        if (pDish == 0) {
            // make sure that the *first* non-zero digit of pDish is greater
            // than the matching digit in pBev.
            maxbevd = Math.min(9, (priced - 1) / 2);
        }

        // iterate through possible digits for beverage and dish
        long solcnt = 0;
        for (int bevd = minbevd; bevd <= maxbevd; bevd++) {

            // assign bevd to BEV
            if (!digits.mapDigit(pBev, bevd, DigitMap.BEV)) continue;
            pBev += mult * bevd;

            // assign dishd to DISH
            int dishd = priced - bevd;
            if (dishd >= 0 && dishd < 10
                    && digits.mapDigit(pDish, dishd, DigitMap.DISH)) {
                // recurse
                solcnt += solve(mult / 10, pPrice % mult, pBev, pDish + mult
                        * dishd, digits, solutions, cache);
                digits.unmapDigit(pDish, dishd); // unassign dishd from DISH
            }

            // assign dishd-1 to DISH
            dishd--;
            if (dishd >= 0 && dishd < 10
                    && digits.mapDigit(pDish, dishd, DigitMap.DISH)) {
                // recurse - note that we assume a carry from the next digit
                solcnt += solve(mult / 10, mult + pPrice % mult, pBev, pDish
                        + mult * dishd, digits, solutions, cache);
                digits.unmapDigit(pDish, dishd); // unassign dishd-1 from DISH
            }

            // unassign bevd from BEV
            pBev -= mult * bevd;
            digits.unmapDigit(pBev, bevd);
        }

        if (mult == 100_000_000) {
            // store the number of solutions in the cache for future reference
            Integer key = digits.digitState | (priced << 24);
            cache.put(key, solcnt);
        }
        return solcnt;
    }
}
