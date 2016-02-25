import java.util.*;

// Submissions to CMICH SP16 #2

public class MultSumDigits {

    // find sum of digits in base 10
    public static int sumOfDigits(int n) {
        int sum = 0;
        for (; n > 0; n /= 10) {
            sum += n % 10;
        }
        return sum;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        for (int n = s.nextInt(); n > 0; n = s.nextInt()) {
            int nsum = sumOfDigits(n);
            // find p such that p*n has same sum of digits as n
            // p must be > 10 (constraint) and <= 100 (obvious upper limit)
            for (int p = 11; p <= 100; p++) {
                if (sumOfDigits(p * n) == nsum) {
                    System.out.println(p);
                    break;
                }
            }
        }
        s.close();
    }
}
