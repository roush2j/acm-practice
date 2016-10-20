package kattis20160902;
import java.util.*;

/**
 * This problem is an example of basic arithmetic. It is also an example of a
 * common theme in ICPC problems: knowledge of algorithms. To solve this problem
 * you must either know or be able to derive the forumla for the area of a
 * simple polygon: https://en.wikipedia.org/wiki/Area_formula#Polygon_formulas
 * 
 * https://open.kattis.com/contests/rvgb3c/problems/polygonarea
 * 
 * @author jroush
 */
public class PolygonArea {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // The outer loop runs once for each polygon in the input file
        // Each polygon begins with a number denoting how many vertices it has
        // We read that number, n, at the start of every loop iteration.
        // We are done when we find a "polygon" with zero vertices.
        for (int N = s.nextInt(); N > 0; N = s.nextInt()) {

            // We read and save the first vertex in the current polygon (x0,y0)
            int x0 = s.nextInt(), y0 = s.nextInt();

            // now we read the remaining vertices one at a time
            double a = 0;
            int xp = x0, yp = y0;
            for (int n = 1; n < N; n++) {
                int x = s.nextInt(), y = s.nextInt();
                // and sum up the cross products with the previous vertex
                a += xp * y - x * yp;
                xp = x;
                yp = y;
            }

            // the last step is to finish the sum by adding the cross product
            // of the last vertex with the first vertex, and then divide by 2
            a += xp * y0 - x0 * yp;
            a /= 2.0;

            // Now the magnitude of a is the area of the polygon, and the
            // sign represents the direction.
            // Note that we use PrintStream.format() to make sure that the
            // area is printed with exactly 1 decimal place as per the problem
            // requirements.
            if (a < 0) System.out.format("CW %.1f\n", -a);
            else System.out.format("CCW %.1f\n", a);
        }

        s.close();
    }
}
