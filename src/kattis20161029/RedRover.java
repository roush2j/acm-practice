package kattis20161029;

import java.util.Arrays;
import java.util.Scanner;

public class RedRover {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String code = s.next();
        final int len = code.length();
        s.close();

        int[] max = new int[len * len];
        Arrays.fill(max, -1);
        for (int i = 0; i < len; i++) {
            for (int l = 2; l < len - i; l++) {
                for (int j = i + l; j <= len - l; j++) {
                    boolean match = true;
                    for (int k = 0; k < l && match; k++) {
                        if (code.charAt(i + k) != code.charAt(j + k))
                            match = false;
                    }
                    if (match) {
                        max[i * len + l] += l - 1;
                        j += l - 1;
                    }
                }
            }
        }
        
        int mm = 0;
        for (int i = 0; i < len; i++) {
            for (int l = 2; l < len - i; l++) {
                if (max[i * len + l] > mm) {
                    mm = max[i * len + l];
                }
            }
        }
        
        System.out.println(code.length() - mm);
    }

}
