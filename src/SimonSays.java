import java.util.*;

/**
 * This problem tests basic string manipulation.
 * 
 * https://open.kattis.com/contests/rvgb3c/problems/simonsays
 * 
 * @author jroush
 */
public class SimonSays {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // first, we read in the number of 'commands' in the game
        int N = s.nextInt();
        // and then make sure to consume the rest of the (now empty) first line
        // this is a common gotcha with the Scanner class!
        s.nextLine();

        // then we read each command using Scanner.nextLine()
        for (int n = 0; n < N; n++) {
            String cmd = s.nextLine();
            // use the String.startsWith() method to check if
            //  a string begins with a particular prefix
            if (cmd.startsWith("Simon says")) {
                // and if so, extract and print the rest of the command
                // (everything after "Simon says ") using String.substring()
                System.out.println(cmd.substring(11));
            }
        }

        s.close();
    }
}
