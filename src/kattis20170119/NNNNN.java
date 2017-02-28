package kattis20170119;

import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;

public class NNNNN {

    private final int[] num;

    private final int   numLen;

    public NNNNN(InputStream in) throws IOException {
        InputStream buf = new BufferedInputStream(in, 1024);
        this.num = new int[112_000]; // big enough for 1M digits in groups of 9
        int idx = 0, s = 0, tmp = 0;
        for (int b = buf.read(); b >= '0' && b <= '9'; s++, b = buf.read()) {
            if (s == 9) {
                num[idx] = tmp;
                tmp = 0;
                idx++;
                s = 0;
            }
            tmp = 10 * tmp + (b - '0');
        }
        num[idx] = tmp;
        this.numLen = idx * 9 + s;
    }

    public int len() {
        return numLen;
    }

    public void print(PrintStream out) {
        final int maxidx = (numLen - 1) / 9;
        final int lastwidth = numLen - maxidx * 9;
        for (int i = 0; i < maxidx; i++) {
            out.format("%09d", num[i]);
        }
        out.format("%0" + lastwidth + "d", num[maxidx]);
    }

    public void mult(int mult) {
        final long m = mult;
        final int maxidx = (numLen - 1) / 9;
        final int lastwidth = numLen - maxidx * 9;
        long carry = 0;
        {
            long p = num[maxidx] * m;
            int pow = (int) Math.pow(10, lastwidth);
            carry = p / pow;
        }
        
        
        for (int i = 0; i < num.length; i++) {
            long p = num[i] * m + carry;
            carry = p / 1_000_000_000;
            num[i] = (int) (p % 1_000_000_000);
        }
    }

    public static BigInteger readBigInt() {
        Scanner s = new Scanner(System.in);

        String ns = s.nextLine();
        BigInteger N = new BigInteger(ns);
        long logn = ns.length();

        System.out.println(N);
        System.out.println(logn);

        s.close();

        return N;
    }

    public static void main(String[] args) throws IOException {
        try {
            System.setIn(new FileInputStream(
                    "src/kattis20170119/problemspecs/nnnnn/data/sample/003.ans"));
            System.setOut(new PrintStream("testout"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.setOut(new PrintStream(System.out, false));

        //        int[] N = new int[112_000]; // big enough for 1M digits in groups of 9
        //        int lenN = 0;
        NNNNN q = new NNNNN(System.in);
        System.out.println(q.len());
        q.print(System.out);
    }

}
