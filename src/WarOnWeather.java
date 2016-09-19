import java.util.*;

/**
 * This problem is solved with trigonometry or vector algebra.
 * 
 * This solution uses the following logic. Picture the earth as a sphere, with a
 * storm on its surface at the very top. We have a satellite somewhere in space
 * outside the sphere. In order for the satellite to see the storm, it must lie
 * *above* the plane tangent to the sphere at very top (where the storm is).
 * Therefore, in this picture the upward (i.e. radial) distance from the storm
 * to the satellite must be greater than zero.
 * 
 * https://open.kattis.com/contests/u2b58x/problems/waronweather
 *
 * @author jroush
 */
public class WarOnWeather {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // outer loop runs once per test case
        while (s.hasNext()) {

            // read in the number of satellites and storms
            int satc = s.nextInt(), stmc = s.nextInt();
            if (satc == 0 && stmc == 0) break; // 0,0 is a marker for end-of-input

            // read in satellite positions and store them for later
            double[] satpos = new double[satc * 3];
            for (int i = 0; i < satc * 3; i += 3) {
                satpos[i] = s.nextDouble();     // x
                satpos[i + 1] = s.nextDouble(); // y
                satpos[i + 2] = s.nextDouble(); // z
            }

            // read in the storm positions
            int hitcnt = 0;
            for (int i = 0; i < stmc; i++) {
                double stmX = s.nextDouble();
                double stmY = s.nextDouble();
                double stmZ = s.nextDouble();

                // check each satellite to see if it can hit this storm
                for (int j = 0; j < satc * 3; j += 3) {
                    double satX = satpos[j];
                    double satY = satpos[j + 1];
                    double satZ = satpos[j + 2];

                    // We calculate the radial distance from the storm to the
                    // satellite using vector algebra.
                    // If R is the vector from the center of the earth to the
                    // storm and and S is the vector for the satellite then:
                    //   (R/|R|) . (S-R) >= 0
                    // That is, the storm is visible to the satellite if the 
                    // vector dot product of R and (S-R) is greater than zero.
                    double dX = satX - stmX, dY = satY - stmY, dZ = satZ - stmZ;
                    double dot = stmX * dX + stmY * dY + stmZ * dZ;
                    if (dot >= 0) {
                        hitcnt++;
                        break; // stop looking through satellites after first hit
                    }
                }
            }

            System.out.println(hitcnt);
        }
        s.close();
    }
}
