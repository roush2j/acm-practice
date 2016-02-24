import java.util.Scanner;


public class Goldbach {
    
    public static final int MAX_INPUT = 32000;
    
    private static int[] composites = new int[(MAX_INPUT + 4) / 4];
    static {
        // sieve of eritosthenes
        composites[0] |= 0x3; // mark 0 and 1 as not prime
        for (int i = 2; i <= MAX_INPUT; i++) {
            if (!isPrime(i)) continue;
            for (int j = i*2; j <= MAX_INPUT; j += i) {
                composites[j >> 3] |= 1 << (j & 0x7);
            }
        }
    }
    
    /** Return true if an integer is prime. */
    public static boolean isPrime(int x) {
        assert x <= MAX_INPUT;
        return ((composites[x >> 3] >> (x & 0x7)) & 1) == 0;
    }
    

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        for (int n = s.nextInt(); n > 0; n--) {
            int x = s.nextInt();
            int[] matches = new int[x/2];
            int matchcnt = 0;
            for (int i = 2; i <= x/2; i++) {
                if (isPrime(i) && isPrime(x-i)) {
                    matches[matchcnt++] = i;
                }
            }
            
            System.out.format("%d has %d representation(s)\n", x, matchcnt);
            for (int i = 0; i < matchcnt; i++) {
                System.out.format("%d+%d\n", matches[i], x-matches[i]);
            }
            System.out.println();
        }
        s.close();
    }

}
