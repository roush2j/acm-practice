package kattispersonal;
import java.io.*;

public class Deathstar {

    public static int nextInt(InputStream in) throws IOException {
        int b = in.read();
        while (b >= 0 && b < '0') b = in.read();
        int v = 0;
        while (b >= '0') {
            v = v * 10 + (b - '0');
            b = in.read();
        }
        return v;
    }

    public static void main(String[] args) throws IOException {
        InputStream s = new BufferedInputStream(System.in, 1024);
        int N = nextInt(s);
        for (int i = 0; i < N; i++) {
            int v = 0;
            for (int j = 0; j < N; j++) {
                v |= nextInt(s);
            }
            System.out.print(v);
            System.out.print(' ');
        }
    }
}
