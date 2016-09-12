import java.util.*;

/**
 * This problem is just basic geometry. The only potential difficulty is
 * understanding the wording (i.e. that C is the width of the *crust* and not
 * the inner radius).
 * 
 * https://open.kattis.com/contests/mk2hhn/problems/pizza2
 * 
 * @author jroush
 */
public class Pizza2 {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // parse the radius and crust size
        int R = s.nextInt();
        int C = s.nextInt();

        // find inner crust radius and compute fraction of cheese coverage
        double r = R - C;
        double f = (r * r) / (R * R);

        // output the result AS A PERCENTAGE with PrintStream.format()
        // this will automatically round the result to make it presentable
        System.out.format("%.6f", f * 100);

        s.close();
    }

}
