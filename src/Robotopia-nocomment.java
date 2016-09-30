import java.util.*;

public class Robotopia {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        for (int N = s.nextInt(); N > 0; N--) {
            int l1 = s.nextInt(), a1 = s.nextInt();
            int l2 = s.nextInt(), a2 = s.nextInt();
            int lt = s.nextInt(), at = s.nextInt();
            int det = l1 * a2 - a1 * l2;
            int c1 = lt * a2 - at * l2;
            int c2 = -lt * a1 + at * l1;
            int q1 = -1, q2 = -1;
            if (det == 0) {
                if (lt * a1 == l1 * at) {
                    int solcnt = 0;
                    for (int q = lt / l1; q > 0; q--) {
                        int q2l2 = lt - q * l1;
                        if (q2l2 >= l2 && q2l2 % l2 == 0) {
                            solcnt++;
                            q1 = q;
                            q2 = q2l2 / l2;
                        }
                    }
                    if (solcnt != 1) q1 = q2 = -1;
                }
            } else if (c1 % det == 0 && c2 % det == 0) {
                q1 = c1 / det;
                q2 = c2 / det;
                if (q1 <= 0 || q2 <= 0) q1 = q2 = -1;
            } 
            if (q1 > 0 && q2 > 0) System.out.format("%d %d\n", q1, q2);
            else System.out.println("?");
        }
        s.close();
    }
}
