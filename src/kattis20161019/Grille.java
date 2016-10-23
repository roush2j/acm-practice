package kattis20161019;

import java.util.*;

/**
 * This problem is from last year's (2015 ICPC ECNA) Regional competition.
 * <p>
 * The decryption algorithm is the nearly the same as the encryption algorithm.
 * For each 90 degree rotation of the grille, visit the revealed slots in
 * left-to-right, top-to-bottom order and copy the next character from the
 * encrypted text into the revealed slot of the plain text.
 * <p>
 * Given the 0-based row and column indices of an open grille slot from the
 * upper-left corner, the row and column of the same open slot after a 90 degree
 * CW grille rotation is:
 * 
 * <pre>
 *   90deg CW row = col
 *   90deg CW col = size - row - 1
 * </pre>
 * 
 * There is one additional wrinkle with this problem: we must identify invalid
 * grilles. To be valid a grille must reveal each letter in the plain text at
 * least once. The problem description is not clear, however, on whether it is
 * valid for a grille to reveal a letter <em>more</em> than once. Both
 * interpretations make sense, but the official solutions assume that it is
 * <b>not</b> valid for a grille to reveal the same letter multiple times.
 * <p>
 * https://open.kattis.com/contests/c6mgpk/problems/grille
 *
 * @author jroush
 */
public class Grille {

    // Return the array index of the (row,col) slot after rot*90 deg. rotation
    public static int rotIdx(int row, int col, int size, int rot) {
        switch (rot & 3) {
        default:
            return row * size + col;
        case 1:
            return col * size + (size - row - 1);
        case 2:
            return (size - row - 1) * size + (size - col - 1);
        case 3:
            return (size - col - 1) * size + row;
        }
    }

    public static String solve(Scanner s) {
        // read grille size
        int size = s.nextInt();
        s.nextLine();

        // read grille: the rotation (0-4) on which a character is first revealed
        int[] grille = new int[size * size];
        Arrays.fill(grille, Integer.MAX_VALUE);
        int grillecnt = 0;
        for (int row = 0; row < size; row++) {
            String line = s.nextLine();
            for (int col = 0; col < size; col++) {
                if (line.charAt(col) != '.') continue;
                // fill in the grille for each 90 deg. rotation of this opening
                for (int rot = 0; rot < 4; rot++) {
                    int idx = rotIdx(row, col, size, rot);
                    if (grille[idx] == Integer.MAX_VALUE) {
                        grillecnt++;
                        grille[idx] = rot;
                    } else {
                        // collision of two openings
                        // this character would be revealed more than once.
                        return "invalid grille";
                    }
                }
            }
        }
        // make sure that grille has enough slots open
        if (grillecnt != grille.length) return "invalid grille";

        // read encrypted message
        String enc = s.next();
        int charsRead = 0;

        // decrypt message
        // iterate through the grille slots once for each rotation and fill in
        // characters in the order in which they were originally drawn from the
        // plain text.
        char[] msg = new char[size * size];
        for (int rot = 0; rot < 4; rot++) {
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    int idx = rotIdx(row, col, size, 0);
                    if (grille[idx] == rot) msg[idx] = enc.charAt(charsRead++);
                }
            }
        }
        return new String(msg);
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println(solve(s));
        s.close();
    }
}
