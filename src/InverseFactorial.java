import java.io.IOException;

/**
 * https://open.kattis.com/problems/inversefactorial
 *
 * @author jroush
 */
public class InverseFactorial {

    // Stirling's famous approximation for the logarithm of a factorial: 
    //    ln(n!) ~= n * ln(n) - n + (1/2) * ln(2 * pi * n)
    public static double stirlingApprox(long n) {
        return (n + 0.5) * Math.log(n) - n + 0.5 * Math.log(Math.PI * 2);
    }

    public static void main(String[] args) throws IOException {

        // Read in the most significant 16 digits of the number
        // We can handle small inputs, where stirlings approx. does not hold
        byte[] prefixbuf = new byte[16];
        int prefixlen = System.in.read(prefixbuf);
        String prefixstr = new String(prefixbuf, 0, prefixlen);
        double prefix = Double.parseDouble(prefixstr);
        if (prefix <= 1) {
            System.out.println(1);
            return;
        } else if (prefix <= 2) {
            System.out.println(2);
            return;
        } else if (prefix <= 6) {
            System.out.println(3);
            return;
        }

        // For longer inputs, we want to calculate the log of the input value.  
        // We approximate this as the log of the first few digits + the length 
        // of the rest.  Note that we convert to natural log.
        double lnn = Math.log(prefix);
        byte[] scratch = new byte[10000];
        for (int c = 0; c >= 0; c = System.in.read(scratch)) {
            if (c > 0 && scratch[c - 1] == 10) c--; // remove trailing LF
            lnn += c * Math.log(10);
        }

        // We have a function (Stirling's Approx.) giving us ln(n!) for any n
        // This function is transcendental - I don't know how to find it's inverse.
        // Instead, we choose crude upper and lower bounds and then use a binary
        // search to tighten the bounds until we find l such that:
        //    Stirling(l) <= log(input) <= Stirling(l+1)
        long l = (long) Math.floor(lnn / Math.log(lnn));
        long h = (long) Math.ceil(lnn * Math.log(lnn));
        double stl = stirlingApprox(l), sth = stirlingApprox(h);
        while (l < h - 1) {
            // Since the factorial function grows so fast, we choose a midpoint
            // that varies exponentially from the lower bound to the upper bound.
            // This greatly improves the convergence rate.
            double r = (lnn - stl) / (sth - stl);
            double dm = Math.exp(r) * (h - l) / 10;
            long m = l + (long) Math.ceil(dm); // make sure m > l
            double stm = stirlingApprox(m);
            if (lnn < stm) {
                h = m;
                sth = stm;
            } else {
                l = m;
                stl = stm;
            }
        }

        System.out.println(l);
    }
}
