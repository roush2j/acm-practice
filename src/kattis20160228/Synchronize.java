package kattis20160228;
import java.util.Arrays;
import java.util.Scanner;

public class Synchronize {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        for (int n = s.nextInt(); n > 0; n = s.nextInt()) {
            // first list
            int[] a = new int[n];
            Integer[] perm = new Integer[n];
            for (int i = 0; i < n; i++) {
                a[i] = s.nextInt();
                perm[i] = Integer.valueOf(i);
            }
            // get permuation of indices that sort the list
            Arrays.sort(perm, (x,y) -> Integer.compare(a[x], a[y]));
            // invert permutation mapping
            for (int i = 0; i < n; i++) a[perm[i]] = i;
            
            // second list
            int[] b = new int[n];
            for (int i = 0; i < n; i++) b[i] = s.nextInt();
            Arrays.sort(b);
            // print in reverse-permuted order
            for (int i = 0; i < n; i++) System.out.println(b[a[i]]);
            System.out.println();
        }
        s.close();
    }

}
