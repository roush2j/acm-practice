import java.util.*;

/**
 * This problem demonstrates basic input/output.
 * 
 * https://open.kattis.com/contests/rvgb3c/problems/bijele
 * 
 * @author jroush
 */
public class Bijele {

    public static void main(String[] args) {

        // We use the Scanner class for basic input
        // it handles *tokenization* - breaking up the input based on spaces
        // and *parsing* - converting numbers into java ints, floats, etc.
        Scanner s = new Scanner(System.in);

        // We read the number of kings we *have* with Scanner.nextInt()
        // and subtract from the number of kings we *need*
        // and print the result (the number of kings to add/remove)
        int kings_found = s.nextInt();
        int kings_added = 1 - kings_found;
        System.out.print(kings_added + " ");

        // The same process for queens, on a single line
        System.out.print((1 - s.nextInt()) + " ");

        // etc ...
        // note that we add a space after each output, 
        // and a newline after the last
        System.out.print((2 - s.nextInt()) + " ");
        System.out.print((2 - s.nextInt()) + " ");
        System.out.print((2 - s.nextInt()) + " ");
        System.out.print((8 - s.nextInt()) + "\n");

        s.close();
    }
}
