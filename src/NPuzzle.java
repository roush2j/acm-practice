import java.util.*;

/**
 * This problem can be simplified by exploiting the close relationship between
 * piece names 'A', 'B', etc. and the desired positions:
 * 
 * <pre>
 *      A B C D       0  1  2  3      (0,0) (0,1) (0,2) (0,3)
 *      E F G H  =>   4  5  6  7  =>  (1,0) (1,1) (1,2) (1,3)
 *      I J K L       8  9 10 11      (2,0) (2,1) (2,2) (2,3)
 *      M N O .      12 13 14 15      (3,0) (3,1) (3,2) (3,3)
 * </pre>
 * 
 * The desired row and column can be calculated for each piece using character
 * arithmetic and modular division; we don't need a lookup table or some
 * complicated control logic.
 * 
 * https://open.kattis.com/contests/mk2hhn/problems/npuzzle
 * 
 * @author jroush
 */
public class NPuzzle {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // total distance of all tiles from their desired locations
        int dist = 0;

        // The outermost loop reads each row of the puzzle board
        for (int row = 0; row < 4; row++) {
            String line = s.nextLine();

            // The inner loop reads each tile (1 character) in the row
            for (int col = 0; col < 4; col++) {
                char piece = line.charAt(col);
                if (piece == '.') continue; // The blank piece '.' is not counted

                // Convert the piece letter into a  number for the desired position.
                // This is easy; java lets us find the difference between two
                // characters with subtraction:
                int pieceNum = piece - 'A';

                // Then convert the number into row and column indices
                int desRow = pieceNum / 4;
                int desCol = pieceNum % 4;

                // Then find the manhattan distance to the desired position from
                // the current row and column
                dist += Math.abs(desRow - row) + Math.abs(desCol - col);
            }
        }
        System.out.println(dist);

        s.close();
    }

}
