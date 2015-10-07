import java.math.BigInteger;
import java.util.Scanner;

/**
 * For each line of input (should be an integer), prints a graphical
 * chart of bee population as a function of time.
 * <p>
 * We use BigIntegers to avoid precision issues; note though that the output
 * size is exponential in the input values, so large inputs (> ~20) will
 * generate outputs too large to represent in memory.
 * 
 * @author jroush
 */
public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        for (int y = s.nextInt(); y > 0; y = s.nextInt()) { // lol error checking
            BigInteger max = bee_pop(y--);
            bee_print(max, max);
            for (; y >= 0; y--) {
                bee_print(bee_pop(y), max);
            }
            System.out.println();
        }
        s.close();
    }
    
    public static BigInteger bee_pop(int year) {
        // closed form solution of the recurrence relation
        //   f(n) = 3*f(n-1) - 1 = (3^n - 1) / 2 with f(0) = 1
        return BigInteger.valueOf(3).pow(year).add(BigInteger.ONE).shiftRight(1);
    }

    public static void bee_print(BigInteger pop, BigInteger max) {
        // this is awful; manual line formatting is a pain in java
        for (BigInteger x = max.subtract(pop).shiftRight(1); 
                x.compareTo(BigInteger.ZERO) > 0; 
                x = x.subtract(BigInteger.ONE)) {
            System.out.print(' ');
        }
        for (BigInteger x = pop; 
                x.compareTo(BigInteger.ZERO) > 0; 
                x = x.subtract(BigInteger.ONE)) {
            System.out.print('B');
        }
        System.out.println();
    }
}
