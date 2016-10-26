package kattispersonal;
import java.io.*;

public class LiveForever {
        
    private static final byte[] kernel;
    static {
        kernel = new byte[1 << (Byte.SIZE + 2)];
        for (int state = 0; state < kernel.length; state++) {
            int next = 0, hstate = state >>> 2;
            for (int i = 0, m = 1; i < Byte.SIZE; i++, m <<= 1) {
                next |= (state ^ hstate) & m;
            }
            kernel[state] = (byte) next;
        }
    }
    
    private static String lpad(int x, int len) {
        StringBuilder sb = new StringBuilder();
        String s = Integer.toBinaryString(x);
        for (int i = s.length(); i < len; i++) {
            sb.append(0);
        }
        sb.append(s);
        return sb.toString();
    }
    
    public static int sim(int state, int len) {
        int next = 0;
        next |= (kernel[(state << 1) & 0x3FF] & 0xFF);
        next |= (kernel[(state >>> 7) & 0x3FF] & 0xFF) << 8;
        next |= (kernel[(state >>> 15) & 0x3FF] & 0xFF) << 16;
        next |= (kernel[(state >>> 23) & 0x3FF] & 0xFF) << 24;
        return next & ((1 << len) - 1);
    }

    public static boolean fate(int state, int len, int[] cache) {
        while (state != 0) {
            int cidx = state >>> 5, cshift = state & 0x1F;
            int cachebit = (cache[cidx] >>> cshift) & 0x1;
            if (cachebit != 0) return true; // cycle detected
            cache[cidx] |= (1 << cshift);
            state = sim(state, len);
        }
        return false;
    }
    
    public static int countFates(int len) {
        boolean printDead = len < 4 || Integer.bitCount(len + 1) != 1;
        int count = 0;
        int[] cache = new int[1 << Math.max(0, len - 5)];
        for (int state = (1 << len) - 1; state != 0; state--) {
            Arrays.fill(cache, 0);
            if (fate(state, len, cache)) count++;
            else if (printDead) System.out.format("%2d   dead %32s\n", len, lpad(state, len));
        }
        if (printDead) System.out.format("%2d   dead %32s\n", len, lpad(0, len));
        return count;
    }

    public static void main(String[] args) {
        
        for (int len = 1; len < 30; len++) {
            int alive = countFates(len);
            int dead = (1 << len) - alive;
            System.out.format("%2d %8d %8d\n", len, dead, alive);
        }        
    }

}
