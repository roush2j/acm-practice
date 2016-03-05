import java.util.*;

public class CookieSplit {

    public static double segArea(double R, double r) {
        // formula for area of a segment of a circle
        // mathworld.wolfram.com/CircularSegment.html
        if (R <= 0) return 0;
        else if (r <= -R || r >= R) return Math.PI * R * R;
        return R * R * Math.acos(r / R) - r * Math.sqrt(R * R - r * r);
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        while (s.hasNext()) {
            double R = s.nextDouble(), x = s.nextDouble(), y = s.nextDouble();
            double r = Math.sqrt(x * x + y * y);
            if (R <= 0 || r >= R - 0.0005) System.out.println("miss");
            else {
                double a = segArea(R, r);
                double rem = Math.PI * R * R - a;
                System.out.format("%.6f %.6f\n", rem, a);
            }
        }
        s.close();
    }
}
