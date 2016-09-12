import java.util.*;

/**
 * This is a warm-up problem: basic input/output and looping.
 * 
 * https://open.kattis.com/contests/mk2hhn/problems/timeloop
 * 
 * @author jroush
 */
public class TimeLoop {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // We parse the 'magic' number N from the standard input
        int N = s.nextInt();

        // and use a loop to count from 1 to N
        for (int i = 1; i <= N; i++) {
            System.out.println(i + " Abracadabra");
        }

        s.close();
    }

}
