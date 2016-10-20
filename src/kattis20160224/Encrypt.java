package kattis20160224;
import java.util.*;

// Submissions to CMICH SP16 #2

public class Encrypt {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int count = s.nextInt();
        s.nextLine();
        for (; count > 0; count--) {
            String msg = s.nextLine();
            int k = (int) Math.ceil(Math.sqrt(msg.length()));
            for (int x = 0; x < k; x++) {
                for (int y = k - 1; y >= 0; y--) {
                    int i = x + y*k;
                    if (i < msg.length()) System.out.print(msg.charAt(i));
                }
            }
            System.out.println();
        }
        s.close();
    }
}
