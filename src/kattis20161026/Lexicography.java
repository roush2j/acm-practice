package kattis20161026;

import java.util.*;

/**
 * https://open.kattis.com/contests/e3emoi/problems/lexicography
 *
 * @author jroush
 */
public class Lexicography {

    /** Return an array of 26 counts, one for each latin character in the string */
    public static int[] charCnt(String s) {
        int[] cnt = new int[26];
        for (int i = 0; i < s.length(); i++) {
            char c = Character.toUpperCase(s.charAt(i));
            if (c < 'A' || c > 'Z')
                throw new RuntimeException("Invalid character " + c);
            cnt[c - 'A']++;
        }
        return cnt;
    }

    /** Calculate the number of unique anagrams given an array of character counts */
    public static long permCount(int[] charCnt) {
        long fact = 1, div = 1;
        for (int ic = 0, uc = 1; ic < charCnt.length; ic++) {
            for (int l = 1; l <= charCnt[ic]; l++, uc++) {
                fact *= uc; // compute the factorial of total number of characters
                div *= l;  // compute the product of the factorials of each character count
            }
        }
        return fact / div;
    }

    /** Find the nth unique anagram in lexographic order, given an array of character counts */
    public static String solve(int[] charCnt, long rank) {
        for (int ic = 0; ic < charCnt.length; ic++) {
            if (charCnt[ic] == 0) continue;
            char c = (char) ('A' + ic);
            charCnt[ic]--;
            long cnt = permCount(charCnt);
            if (cnt < rank) {
                rank -= cnt;
                charCnt[ic]++;
            } else {
                String s = solve(charCnt, rank);
                return c + s;
            }
        }
        return "";
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        while (true) {
            String word = s.next();
            long rank = s.nextLong();
            if (word.equals("#") && rank == 0) break; // '#' denotes end of file

            String ana = solve(charCnt(word), rank);
            System.out.println(ana);
        }
        s.close();
    }

}
