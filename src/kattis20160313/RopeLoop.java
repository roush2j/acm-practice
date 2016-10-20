package kattis20160313;
import java.util.*;

public class RopeLoop {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int[] reds = new int[1000], blues = new int[1000];
        final int n = s.nextInt();
        for (int nn = 0; nn < n; nn++) {
            int redc = 0, bluec = 0;
            for (int ropecnt = s.nextInt(); ropecnt > 0; ropecnt--) {
                String rope = s.next();
                String ropelen = rope.substring(0, rope.length() - 1);
                int len = Integer.parseInt(ropelen);
                switch (rope.charAt(rope.length() - 1)) {
                case 'R':
                    reds[redc++] = len;
                    break;
                case 'B':
                    blues[bluec++] = len;
                    break;
                default:
                    throw new RuntimeException();
                }
            }

            Arrays.sort(reds, 0, redc);
            Arrays.sort(blues, 0, bluec);
            int tlen = 0;
            for (int i = Math.min(redc, bluec); i > 0; i--) {
                tlen += reds[redc - i] + blues[bluec - i] - 2;
            }
            System.out.format("Case #%d: %d\n", nn + 1, tlen);
        }
        s.close();
    }
}
