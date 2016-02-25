import java.util.*;

// Submissions to CMICH SP16 #2

public class Towering {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        int[] heights = new int[6];
        for (int i = 0; i < heights.length; i++) {
            heights[i] = s.nextInt();
        }
        int h1 = s.nextInt();
        int h2 = s.nextInt();

        // there are some constraints we could solve, 
        // but brute force is quite fast enough
        Arrays.sort(heights);
        int[] ta = new int[3], tb = new int[3];
        LOOP: for (ta[0] = 0; ta[0] < heights.length - 2; ta[0]++) {
            for (ta[1] = ta[0] + 1; ta[1] < heights.length - 1; ta[1]++) {
                for (ta[2] = ta[1] + 1; ta[2] < heights.length; ta[2]++) {
                    int ha = 0, hb = 0;
                    for (int i = 0, j = 0, k = 0; k < heights.length; k++) {
                        if (i < ta.length && ta[i] == k) {
                            ha += heights[k];
                            i++;
                        } else {
                            hb += heights[k];
                            tb[j++] = k;
                        }
                    }
                    if (ha == h1 && hb == h2) break LOOP;
                    else if (hb == h1 && ha == h2) {
                        int[] temp = ta;
                        ta = tb;
                        tb = temp;
                        break LOOP;
                    } 
                }
            }
        }
        
        for (int i = ta.length - 1; i >= 0; i--) {
            System.out.print(heights[ta[i]] + " ");
        }
        for (int i = tb.length - 1; i >= 0; i--) {
            System.out.print(heights[tb[i]] + " ");
        }
        s.close();
    }
}
