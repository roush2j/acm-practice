import java.util.*;

/**
 * This is intended to be a warm-up problem. Unfortunately, the unclear wording
 * caused a lot of confusion.
 * 
 * https://open.kattis.com/contests/u2b58x/problems/tourdefrance
 * 
 * @author jroush
 */
public class TourDeFrance {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // Outer loop iterates once per test case
        while (true) {

            // read gear counts
            int frontCnt = s.nextInt();
            if (frontCnt == 0) break;   // zero is a marker for end-of-input
            int rearCnt = s.nextInt();

            // read the tooth count for each of the front gears
            int[] frontGears = new int[frontCnt];
            for (int i = 0; i < frontCnt; i++) {
                frontGears[i] = s.nextInt(); // store tooth count for later
            }

            // read the tooth count for each of the rear gears
            float ratios[] = new float[frontCnt * rearCnt];
            for (int i = 0; i < rearCnt; i++) {
                int rearGear = s.nextInt();
                // calculate the drive ratio from this rear gear to each
                // of the front gears and store it in an array
                for (int j = 0; j < frontCnt; j++) {
                    float ratio = rearGear / (float) frontGears[j];
                    ratios[i * frontCnt + j] = ratio;
                }
            }

            // sort the array of calculated drive ratios
            // this puts 'adjacent' (as defined by the problem statement)
            // drive ratios next to each other in the array
            Arrays.sort(ratios);
            float maxSpread = 0;
            for (int i = 1; i < frontCnt * rearCnt; i++) {
                // calculate the 'spread' (the quotient) of each pair of
                // adjacent drive ratios and track the highest spread
                float spread = ratios[i] / ratios[i - 1];
                if (spread > maxSpread) maxSpread = spread;
            }

            // print the highest spread
            System.out.format("%.2f\n", maxSpread);
        }

        s.close();
    }
}
