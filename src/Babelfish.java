import java.util.*;

public class Babelfish {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Map<String, String> dict = new HashMap<>();
        while (s.hasNextLine()) {
            String pair = s.nextLine();
            if (pair.isEmpty()) break;
            String[] spair = pair.split(" ");
            dict.put(spair[1], spair[0]);
        }
        while (s.hasNext()) {
            String word = s.next();
            String trans = dict.get(word);
            if (trans == null) System.out.println("eh");
            else System.out.println(trans);
        }
        s.close();
    }
}
