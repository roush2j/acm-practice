package kattis20160313;
import java.util.*;

public class MatrixInverse {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        for (int casenum = 1; s.hasNext(); casenum++) {
            long a = s.nextLong(), b = s.nextLong();
            long c = s.nextLong(), d = s.nextLong();
            long det = a * d - b * c;
            System.out.format("Case %d:\n", casenum);
            System.out.format("%d %d\n", d / det, -b / det);
            System.out.format("%d %d\n", -c / det, a / det);
        }
        s.close();
    }
}
