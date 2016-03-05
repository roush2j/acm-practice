import java.util.*;

public class BishopMoves {

    public static String moveToString(int col, int row) {
        return String.format("%c %d ", (char) ('A' + col), row + 1);
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        for (int n = s.nextInt(); n > 0; n--) {
            int scol = s.next().toUpperCase().charAt(0) - 'A';
            int srow = s.nextInt() - 1;
            int fcol = s.next().toUpperCase().charAt(0) - 'A';
            int frow = s.nextInt() - 1;
            //
            boolean scolor = (scol + srow) % 2 == 0; // start on black
            boolean fcolor = (fcol + frow) % 2 == 0; // finish on black
            if (scolor != fcolor) System.out.println("Impossible");
            else {
                int mvcnt = 0;
                StringBuilder moves = new StringBuilder();
                moves.append(moveToString(scol, srow));
                // we can move to any reachable square with <= 3 moves
                // since any path is accepted, we will just hardcode an easy one
                // we move to diagonal, along diagonal, and then off diagonal
                int spcol, sprow, fpcol, fprow;
                if (scolor) {
                    spcol = sprow = (scol + srow) / 2;
                    fpcol = fprow = (fcol + frow) / 2;
                } else {
                    spcol = (scol + 7 - srow) / 2;
                    sprow = 7 - spcol;
                    fpcol = (fcol + 7 - frow) / 2;
                    fprow = 7 - fpcol;
                }
                if (fcol != scol || frow != srow) {
                    if (spcol != scol || sprow != srow) {
                        moves.append(moveToString(spcol, sprow));
                        mvcnt++;
                    }
                    if (fpcol != spcol || fprow != sprow) {
                        moves.append(moveToString(fpcol, fprow));
                        mvcnt++;
                    }
                    if (fcol != fpcol || frow != fprow) {
                        moves.append(moveToString(fcol, frow));
                        mvcnt++;
                    }
                }
                System.out.println(mvcnt + " " + moves.toString());
            }
        }
        s.close();
    }
}
