import java.util.*;

public class AliceTravels {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        for (int T = s.nextInt(); T > 0; T--) {
            Set<String> cities = new HashSet<>();
            for (int trips = s.nextInt(); trips > 0; trips--) {
                cities.add(s.next());
            }
            System.out.println(cities.size());
        }
        s.close();
    }
}
