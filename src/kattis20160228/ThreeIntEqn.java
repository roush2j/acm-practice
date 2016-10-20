package kattis20160228;
import java.util.Scanner;

public class ThreeIntEqn {
    
    public static char test(double a, double b, double c) {
        if (a+b == c) return '+';
        else if (a-b == c) return '-';
        else if (a*b == c) return '*';
        else if (b != 0 && a/b == c) return '/';
        else return '\0';
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        double a = s.nextDouble(), b = s.nextDouble(), c = s.nextDouble();
        char op = test(a,b,c);
        if (op != '\0') {
            System.out.format("%.0f%c%.0f=%.0f\n", a, op, b, c);
        } else {
            op = test(b,c,a);
            if (op != '\0') System.out.format("%.0f=%.0f%c%.0f\n", a, b, op, c);
        }
        s.close();
    }

}
