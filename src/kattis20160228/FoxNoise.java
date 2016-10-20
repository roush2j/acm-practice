package kattis20160228;
import java.util.*;

public class FoxNoise {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        s.nextLine();
        while (n-- > 0) {
            String[] words = s.nextLine().split("\\s");
            Set<String> known = new HashSet<>();
            while (true) {
                String name = s.next();
                String verb = s.next();
                String sound = s.next();
                if (verb.equalsIgnoreCase("goes")) {
                    known.add(sound);
                } else {
                    s.nextLine();
                    break;
                }
            }
            for (String word : words) {
                if (!known.contains(word)) System.out.print(word + " ");
            }
            System.out.println();
        }
        s.close();
    }

}
