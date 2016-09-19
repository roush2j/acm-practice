import java.util.*;

/**
 * There are two notable things about this problem: (1) it has a very brief,
 * elegant solution, and (2) *ALL* of the exposition prior to the 'Task' section
 * is pointless and unhelpful.
 * 
 * The elegant solution to this problem - as opposed to just brute-force
 * counting up to K for each test case - involves a change of base. There are
 * seven digits in the illustrated font which map to other digits (or
 * themselves) when upside down. Each digit in a 'valid' number must be one of
 * these seven. The Kth valid number, therefore, is K written in base 7 using
 * the valid upside-down digits.
 * 
 * https://open.kattis.com/contests/u2b58x/problems/ignore
 * 
 * @author jroush
 */
public class Ignore {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // An array of characters that we will use to write in base 7
        // these are the seven upside-down digit characters that are still "valid"
        // 3, 4, and 7 are not valid and do not appear
        // 0, 1, 2, 5, and 8 are the same upside down and appear in the same order
        // 6 and 9 are reversed when upside down
        final char[] digitMap = new char[] { '0', '1', '2', '5', '9', '8', '6' };

        // outer loop runs once per test case
        while (s.hasNext()) {

            // we read in K and break it down into digits in base 7
            // the first digit is K mod 7, the second digit is (K/7) mod 7,
            // the third digit is (K/7^2) mod 7, and so on
            String upsidedown = "";
            for (int K = s.nextInt(); K > 0; K /= 7) {
                // then we find the appropriate character and append it to the string
                upsidedown += digitMap[K % 7];
            }

            // the resulting string is backwards - with the least significant
            // base-7 character first and the most significant character last
            // this is actually what we want, though, as the string is supposed
            // to be read upside-down.
            System.out.println(upsidedown);
        }

        s.close();
    }
}
