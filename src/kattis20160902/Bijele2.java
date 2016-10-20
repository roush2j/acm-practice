package kattis20160902;
import java.util.*;

/**
 * A second, less repetitive solution for this problem using a loop.
 * 
 * https://open.kattis.com/contests/rvgb3c/problems/bijele
 * 
 * @author jroush
 */
public class Bijele2 {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // the number of each piece we want { 1 King, 1 Queen, etc...}
        int[] pieces = new int[] { 1, 1, 2, 2, 2, 8 };

        // for each number in our array ...
        for (int count : pieces) {
            // read how many of that piece we have
            int c = s.nextInt();
            // and print the difference with a trailing space
            System.out.print(count - c);
            System.out.print(" ");
        }

        System.out.println();
        s.close();
    }
}
