import java.util.*;

public class FreeDesserts_BruteForce {

    public static Set<Long> digits(long x) {
        Set<Long> d = new HashSet<>();
        for (; x > 0; x /= 10) {
            d.add(x % 10);
        }
        return d;
    }

    public static <T> boolean intersect(Set<T> a, Set<T> b) {
        for (Object x : a) {
            if (b.contains(x)) return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        long P = s.nextLong();

        List<Long> solutions = new ArrayList<>();
        for (long bev = 1; bev < P; bev++) {
            long dish = P - bev;
            if (bev >= dish) continue;
            if (intersect(digits(dish), digits(P))) continue;
            if (intersect(digits(bev), digits(P))) continue;
            if (intersect(digits(bev), digits(dish))) continue;
            solutions.add(bev);
        }

        System.out.println(solutions.size());
        for (Long bev : solutions) {
            System.out.format("%d %d\n", bev, P - bev);
        }
    }
}
