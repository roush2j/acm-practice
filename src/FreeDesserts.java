import java.util.*;

public class FreeDesserts {

    public static Set<Integer> digits(int x) {
        Set<Integer> d = new HashSet<>();
        for (; x > 0; x /= 10) {
            d.add(x % 10);
        }
        return d;
    }

    public static boolean intersect(Set<Integer> a, Set<Integer> b) {
        for (Integer d : a) {
            if (b.contains(d)) return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int P = s.nextInt();

        List<Integer> solutions = new ArrayList<>();
        for (int bev = 1; bev < P; bev++) {
            int dish = P - bev;
            if (bev >= dish) continue;
            if (intersect(digits(dish), digits(P))) continue;
            if (intersect(digits(bev), digits(P))) continue;
            if (intersect(digits(bev), digits(dish))) continue;
            solutions.add(bev);
        }

        System.out.println(solutions.size());
        for (Integer bev : solutions) {
            System.out.format("%d %d\n", bev, P - bev);
        }
    }
}
