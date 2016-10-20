package kattis20160313;
import java.util.*;

public class LooseSort {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        for (int n = s.nextInt(); n > 0; n = s.nextInt()) {
            String[] names = new String[n];
            for (int i = 0; i < n; i++) {
                names[i] = s.next();
            }
            Arrays.sort(names, (a, b) -> {
                char a0 = a.length() > 0 ? a.charAt(0) : 0;
                char b0 = b.length() > 0 ? b.charAt(0) : 0;
                if (a0 < b0) return -1;
                else if (a0 > b0) return 1;
                char a1 = a.length() > 1 ? a.charAt(1) : 0;
                char b1 = b.length() > 1 ? b.charAt(1) : 0;
                return Character.compare(a1, b1);
            });
            for (String name : names) {
                System.out.println(name);
            }
            System.out.println();
        }
        s.close();
    }
}
