package kattis20160224;
import java.util.*;

// Submissions to CMICH SP16 #2

public class Coldtemps {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int count = s.nextInt();
        int belowz = 0;
        for (; count > 0; count--) {
            int t = s.nextInt();
            if (t < 0) belowz++;
        }
        System.out.println(belowz);
        s.close();
    }
}
