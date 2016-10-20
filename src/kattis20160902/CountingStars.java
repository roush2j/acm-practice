package kattis20160902;
import java.util.*;

/**
 * This problem requires two steps to solve: we must completely load each test
 * case and store it in memory, and then solve it using a recursive search
 * algorithm.
 * 
 * https://open.kattis.com/contests/rvgb3c/problems/countingstars
 * 
 * @author jroush
 */
public class CountingStars {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // The outer loop runs once for each star "image" in the file
        // We keep track of the number because it is required in our output
        for (int casenum = 1; s.hasNext(); casenum++) {

            // The test case begins with 2 numbers, the height and width 
            // of the current star "image".
            int height = s.nextInt(), width = s.nextInt();
            // make sure to consume the rest of the line on which
            // the height and width are given
            s.nextLine();

            // now we parse the star image itself and store it in a more 
            // accessible form: stars[r * width + c] will be true if the
            // character at line r and column c is part of a star 
            boolean[] stars = new boolean[width * height];
            for (int r = 0; r < height; r++) {
                // read the image one *line* at a time using Scanner.nextLine()
                String dataline = s.nextLine();
                // then iterate over the characters using String.charAt()
                for (int c = 0; c < width; c++) {
                    stars[r * width + c] = (dataline.charAt(c) == '-');
                }
            }

            // Now that we have the whole star image loaded, we iterate over it
            // looking for stars.  Whenever we find a "pixel" that is part of
            // a star, we remove the whole star from the image, add one to our
            // star count, and continue iterating.
            int starcnt = 0;
            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {
                    if (removeStar(stars, height, width, r, c)) starcnt++;
                }
            }
            System.out.println("Case " + casenum + ": " + starcnt);

        }
        s.close();
    }

    // This method removes a star "pixel" from the stars array.
    // It then recursively removes star pixels that neighbor the original, and
    // star pixels that neighbor the neighbors, and so on until the entire 
    // contiguous star has been removed.
    public static boolean removeStar(boolean[] stars, int h, int w, int r, int c) {
        if (!stars[r * w + c]) return false;
        stars[r * w + c] = false;
        if (c - 1 >= 0) removeStar(stars, h, w, r, c - 1);
        if (c + 1 < w) removeStar(stars, h, w, r, c + 1);
        if (r - 1 >= 0) removeStar(stars, h, w, r - 1, c);
        if (r + 1 < h) removeStar(stars, h, w, r + 1, c);
        return true;
    }
}
