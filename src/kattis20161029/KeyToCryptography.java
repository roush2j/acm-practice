package kattis20161029;

import java.util.Scanner;

public class KeyToCryptography {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String cypher = s.next();
        String word = s.next();
        StringBuilder plain = new StringBuilder();
        for (int i = 0; i < Math.min(word.length(), cypher.length()); i++) {
            char c = cypher.charAt(i);
            int shifted = c - (word.charAt(i) - 'A');
            if (shifted < 'A') shifted += 26;
            plain.append((char) shifted);
        }
        for (int i = word.length(); i < cypher.length(); i++) {
            char c = cypher.charAt(i);
            int shifted = c - (plain.charAt(i - word.length()) - 'A');
            if (shifted < 'A') shifted += 26;
            plain.append((char) shifted);
        }
        System.out.println(plain);
        s.close();
    }

}
