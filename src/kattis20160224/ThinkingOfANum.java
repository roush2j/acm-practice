package kattis20160224;
import java.util.*;

// Submissions to CMICH SP16 #2

public class ThinkingOfANum {

    public static long gcd(long a, long b) {
        // Euclid's algorithm for Greatest Common Denominator
        // en.wikipedia.org/wiki/Euclidean_algorithm#Implementations
        while (b != 0) {
            long c = b;
            b = a % b;
            a = c;
        }
        return a;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        for (int clues = s.nextInt(); clues > 0; clues = s.nextInt()) {
            s.nextLine();
            long min = 0;
            long max = Long.MAX_VALUE;
            long factors = 1;
            while (clues-- > 0) {
                String verb = s.next();
                String preposition = s.next();
                int val = s.nextInt();
                switch (verb.charAt(0)) {
                case 'l':    // less than
                    if (val < max) max = val;
                    break;
                case 'g':    // greater than
                    if (val > min) min = val;
                    break;
                case 'd':   // divisible by
                    if (factors % val != 0) {
                        // lcm of factors, val
                        factors = (factors / gcd(factors, val)) * val;
                    }
                    break;
                default:
                    throw new RuntimeException("Bad input '" + verb + "'");
                }
            }
            if (max > Integer.MAX_VALUE) System.out.print("infinite");
            else {
                long fm = factors * ((min + factors) / factors);
                if (fm >= max) System.out.print("none");
                else for (long i = fm; i < max; i += factors) {
                    System.out.print(i + " ");
                }
            }
            System.out.println();
        }
        s.close();
    }
}
