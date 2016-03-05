import java.util.*;

public class WeatherKiller {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        while (s.hasNext()) {
            int satc = s.nextInt(), stmc = s.nextInt();
            if (satc == 0 && stmc == 0) break;

            double[] satpos = new double[satc * 3];
            for (int i = 0; i < satc * 3; i += 3) {
                satpos[i] = s.nextDouble();     // x
                satpos[i + 1] = s.nextDouble(); // y
                satpos[i + 2] = s.nextDouble(); // z
            }

            int hitcnt = 0;
            for (int i = 0; i < stmc; i++) {
                double x = s.nextDouble();
                double y = s.nextDouble();
                double z = s.nextDouble();
                for (int j = 0; j < satc * 3; j += 3) {
                    double sx = satpos[j];
                    double sy = satpos[j + 1];
                    double sz = satpos[j + 2];
                    
                    // OK, so we know that the storm is *on the surface* and
                    // the satellite must be "above" the plane tangent at that
                    // point in order to hit the storm.
                    // Therefore, the vector from the storm to the satellite
                    // must have some non-negative radial component.
                    double dx = sx - x, dy = sy - y, dz = sz - z;
                    double dot = dx * x + dy * y + dz * z;
                    if (dot >= 0) {
                        hitcnt++;
                        break;
                    }
                }
            }

            System.out.println(hitcnt);
        }
        s.close();
    }
}
