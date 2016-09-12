import java.util.*;

/**
 * This problem requires tracking mutable state (the position of the ball) over
 * a series of input commands. The main challenge is to decide how to code the
 * transitions for each of the 9 combinations of (current position, command).
 * 
 * We could use a 9-way if statement or switch, or a lookup table with 9
 * entries. We use a hybrid: a 3-way switch and 3 lookup tables.
 * 
 * https://open.kattis.com/contests/mk2hhn/problems/trik
 * 
 * @author jroush
 */
public class Trik {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // Current ball position (1, 2, or 3).
        int ballPos = 1;

        // Lookup tables for the next position after applying a move to
        // the current position.
        final int[] moveA = new int[] { 2, 1, 3 }; // A swaps 1 <-> 2
        final int[] moveB = new int[] { 1, 3, 2 }; // B swaps 2 <-> 3
        final int[] moveC = new int[] { 3, 2, 1 }; // C swaps 1 <-> 3

        // The commands in this problem are just single characters with no
        // whitespace in-between, so Scanner won't split them up.
        // We read the whole command string at once and then iterate over the
        // characters using String.charAt().
        String commands = s.nextLine();
        for (int i = 0; i < commands.length(); i++) {
            char c = commands.charAt(i);
            switch (c) {
                case 'A':
                    ballPos = moveA[ballPos - 1];
                    break;
                case 'B':
                    ballPos = moveB[ballPos - 1];
                    break;
                case 'C':
                    ballPos = moveC[ballPos - 1];
                    break;
            }
        }
        System.out.println(ballPos);

        s.close();
    }

}
