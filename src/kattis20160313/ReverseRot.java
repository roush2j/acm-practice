package kattis20160313;
import java.util.Scanner;

public class ReverseRot {

    public static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_.";

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        for (int rot = s.nextInt(); rot > 0; rot = s.nextInt()) {
            String msg = s.next();
            StringBuilder sb = new StringBuilder();
            for (int i = msg.length() - 1; i >= 0; i--) {
                char c = msg.charAt(i);
                int idx = alphabet.indexOf(c);
                if (idx < 0)
                    throw new RuntimeException("Illegal character " + c);
                idx = (idx + rot) % alphabet.length();
                sb.append(alphabet.charAt(idx));
            }
            System.out.println(sb.toString());
        }
        s.close();
    }
}
