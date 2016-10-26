package kattispersonal;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * https://open.kattis.com/problems/whowantstoliveforever
 *
 * @author jroush
 */
public class LiveForever {

    public static boolean readBit(int[] state, int i) {
        return (state[i >>> 5] & (1 << (i & 0x1F))) != 0;
    }

    public static boolean dies(int[] state, int len) {
        // for even lengths, only the all-zeros dead state DIES
        if ((len & 0x1) == 0) {
            for (int i = (len + Integer.SIZE - 1) / Integer.SIZE; i >= 0; i--) {
                if (state[i] != 0) return false;
            }
            return true; // all zeros
        }

        // otherwise, we find a,m such that len = a * 2^m - 1
        int a = len + 1, m = 0;
        while ((a & 0x1) == 0) {
            a >>= 1;
            m++;
        }

        // dies iif state is `a` mirrored subgroups of `2^m-1` bits,
        // separated by zeros
        int g = (1 << m);
        for (int i = 1; i < a; i++) {
            if (readBit(state, g * i - 1)) return false;
            for (int j = 0; j < g - 1; j++) {
                if (readBit(state, g * i + j) != readBit(state, g * i - j - 2))
                    return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        InputStream in = System.in;

        // iterate once per test case
        int testCnt = 0;
        for (int c = in.read(); c >= '0'; c = in.read()) {
            testCnt *= 10;
            testCnt += c - '0';
        }
        int[] state = new int[128];
        for (; testCnt > 0; testCnt--) {

            // read input line, one bit per character
            int idx = 0, bit = 1, val = 0;
            for (int c = in.read(); c >= '0'; c = in.read()) {
                if (c == '1') val |= bit;
                bit <<= 1;
                if (bit == 0) {
                    state[idx] = val;
                    if (++idx >= state.length)
                        state = Arrays.copyOf(state, state.length * 2);
                    bit = 1;
                    val = 0;
                }
            }
            state[idx] = val;
            int len = idx * Integer.SIZE + Integer.numberOfTrailingZeros(bit);

            // determine fate
            if (dies(state, len)) System.out.println("DIES");
            else System.out.println("LIVES");
        }
    }

}
