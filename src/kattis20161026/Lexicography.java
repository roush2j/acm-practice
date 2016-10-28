package kattis20161026;

import java.util.Arrays;
import java.util.Scanner;

public class Lexicography {
    
    public static long permCount(char[] chars, int start, int len) {
        Arrays.sort(chars, start, start + len);
        long fact = 1, div = 1;
        char prev = '\0';
        for (int i = 0, run = 1; i < len; i++, run++) {
            if (chars[i + start] != prev) run = 1;
            prev = chars[i + start];
            fact *= (i + 1);
            div *= run;
        }
        return fact / div;
    }
    
    public static void solve(char[] chars, int start, int len, long rank) {
        Arrays.sort(chars, start, start + len);
        char prev = '\0';
        for (int i = 0; i < len; i++) {
            char c = chars[i + start];
            if (c != prev) {
                char[] tmp = Arrays.copyOfRange(chars, start, start + len);
                tmp[i] = chars[start];
                long cnt = permCount(tmp, 1, len - 1);
                if (cnt < rank) rank -= cnt;
                else {
                    chars[start + i] = chars[start];
                    chars[start] = c;
                    solve(chars, start + 1, len - 1, rank);
                    return;
                }
            }
            prev = chars[i + start];
        }
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        while (true) {
            String word = s.next();
            long rank = s.nextLong();
            if (word.equals("#") && rank == 0) break; // '#' denotes end of file
            
            char[] chars = word.toCharArray();
            solve(chars, 0, chars.length, rank);
            System.out.println(new String(chars));
        }
        s.close();
    }

}
