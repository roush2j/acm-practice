import java.util.Scanner;


public class Rings {
    
    public static int gcd(int a, int b) {
        // cribbed from wikipedia
        // wikipedia.org/wiki/Greatest_common_divisor#Binary_method
        int d = 1;
        while (a % 2 == 0 && b % 2 == 0) {
            a /= 2;
            b /= 2;
            d *= 2;
        }
        while (a != b) {
            if (a % 2 == 0) a /= 2;
            else if (b % 2 == 0) b /= 2;
            else if (a > b) a = (a-b)/2;
            else b = (b-a)/2;
        }
        return a * d;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        int first = s.nextInt();
        while (--n > 0) {
            int x = s.nextInt();
            int gcd = gcd(first, x);
            System.out.format("%d/%d\n", first/gcd, x/gcd);
        }
        s.close();
    }

}
