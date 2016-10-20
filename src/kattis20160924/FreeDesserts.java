package kattis20160924;
import java.util.*;

public class FreeDesserts {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        long P = s.nextLong();
        s.close();

        DigitMap digits = new DigitMap();
        for (long x = P; x > 0; x /= 10) {
            int digit = (int) (x % 10);
            digits.mapDigit(x, digit, DigitMap.PRICE);
        }
        List<String> solutions = new ArrayList<>(5000);
        Map<Integer, Long> cache = new HashMap<>();
        long cnt = solve(100_000_000_000_000_000L, P, 0, 0, digits, solutions, cache);
        System.out.println(cnt);
        for (String sol : solutions) {
            System.out.println(sol);
        }
    }

    public static class DigitMap {

        public static final int FREE       = 0, PRICE = 1, BEV = 2, DISH = 3;

        private int             digitState = 0;

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

    public static long solve( //
            long mult,        // multiplier for this digit of the solution (power of 10)
            long pPrice,      // remaining price
            long pBev, long pDish, // partial values for beverage and dish
            DigitMap digits,  // map of digit states
            List<String> solutions,  // list of solutions
            Map<Integer, Long> cache  // cache of digitState -> solution count at level 9
    ) {
        // base case
        if (mult == 0) {
            if (pPrice > 0) return 0;
            else {
                if (solutions.size() < 5000) {
                    solutions.add(String.format("%d %d", pBev, pDish));
                }
                return 1;
            }
        }

        long solcnt = 0;
        int priced = (int) (pPrice / mult);
        if (mult == 100_000_000 && solutions.size() >= 5000) {
            Integer key = digits.digitState | (priced << 24);
            Long cnt = cache.get(key);
            if (cnt != null) return cnt;
        }
        int minbevd = Math.max(0, priced - 10);
        int maxbevd = Math.min(9, priced);
        if (pDish == 0) maxbevd = Math.min(9, (priced - 1) / 2);
        for (int bevd = minbevd; bevd <= maxbevd; bevd++) {

            if (!digits.mapDigit(pBev, bevd, DigitMap.BEV)) continue;
            pBev += mult * bevd;

            int dishd = priced - bevd;
            if (dishd >= 0 && dishd < 10
                    && digits.mapDigit(pDish, dishd, DigitMap.DISH)) {
                solcnt += solve(mult / 10, pPrice % mult, pBev, pDish + mult
                        * dishd, digits, solutions, cache);
                digits.unmapDigit(pDish, dishd);
            }

            dishd--;
            if (dishd >= 0 && dishd < 10
                    && digits.mapDigit(pDish, dishd, DigitMap.DISH)) {
                solcnt += solve(mult / 10, mult + pPrice % mult, pBev, pDish
                        + mult * dishd, digits, solutions, cache);
                digits.unmapDigit(pDish, dishd);
            }

            pBev -= mult * bevd;
            digits.unmapDigit(pBev, bevd);
        }
        if (mult == 100_000_000 && solutions.size() >= 5000) {
            Integer key = digits.digitState | (priced << 24);
            cache.put(key, solcnt);
        }
        return solcnt;
    }
}
