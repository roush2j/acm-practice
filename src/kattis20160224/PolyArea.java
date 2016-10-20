package kattis20160224;
import java.util.*;

// Submissions to CMICH SP16 #2

public class PolyArea {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        for (int verts = s.nextInt(); verts > 2; verts = s.nextInt()) {
            // Polygon area: en.wikipedia.org/wiki/Polygon#Area_and_centroid
            int x0 = s.nextInt(), y0 = s.nextInt();
            int xp = x0, yp = y0;
            long area = 0;
            while (--verts > 0) {
                int x = s.nextInt(), y = s.nextInt();
                area += xp * y - x * yp;
                xp = x;
                yp = y;
            }
            area += xp * y0 - x0 * yp;
            System.out.format("%s %.1f\n", area >= 0 ? "CCW" : "CW",
                    Math.abs(area) / 2.0);
        }
        s.close();
    }
}
