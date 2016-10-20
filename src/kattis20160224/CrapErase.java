package kattis20160224;
import java.util.*;

// Submissions to CMICH SP16 #2

public class CrapErase {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        boolean nop = (n % 2) == 0;
        s.nextLine();
        String in = s.nextLine();
        String out = s.nextLine();
        for (int i = 0; i < in.length(); i++) {
            char cin = in.charAt(i);
            char cout = nop ? cin : cin == '0' ? '1' : '0';
            if (i >= out.length() || out.charAt(i) != cout) {
                System.out.println("Deletion failed");
                s.close();
                return;
            }
        }
        System.out.println("Deletion succeeded");
        s.close();
    }
}
