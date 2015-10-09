import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        // first point, used as basis
        double[] p = new double[] { 
                s.nextDouble(), s.nextDouble(), s.nextDouble()
        };
        // iterate over pairs of successive vertices
        // compute cross product of vectors from basis to vertices
        // keep running sum of cross products
        // since polygon is planar, cross products are all parallel
        // summing them is equivalent to adding their (signed!) magnitudes
        double[] a = new double[3], b = new double[3]; 
        double[] x = new double[3];
        while (s.hasNext()) {
            b[0] = s.nextDouble() - p[0];
            b[1] = s.nextDouble() - p[1];
            b[2] = s.nextDouble() - p[2];
            x[0] += a[1]*b[2] - a[2]*b[1];
            x[1] += a[2]*b[0] - a[0]*b[2];
            x[2] += a[0]*b[1] - a[1]*b[0];
            double[] tmp = a;
            a = b;
            b = tmp;
        }
        // magnitude of result vector is 2*area of polygon
        double area = Math.sqrt(x[0]*x[0] + x[1]*x[1]+ x[2]*x[2]) / 2;
        System.out.format("%.3f\n", area);
        s.close();
    }
}
