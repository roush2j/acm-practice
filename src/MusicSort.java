import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;


public class MusicSort {
    
    public static void printStrings(String[] strings) {
        for (String s : strings) {
            System.out.print(s);
            System.out.print(" ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // read attribute names
        Scanner s = new Scanner(System.in);
        String[] attr = s.nextLine().split("\\s+");
        
        // read song data
        int count = s.nextInt();
        s.nextLine();
        String[][] songs = new String[count][];
        for (int i = 0; i < count; i++) {
            songs[i] = s.nextLine().split("\\s+");
        }
        
        // sorting
        for (int c = s.nextInt(); c > 0; c--) {
            // sort by attribute
            String a = s.next();
            int _idx = 0;
            for (; _idx < attr.length; _idx++) 
                if (attr[_idx].equalsIgnoreCase(a)) break;
            final int idx = _idx;
            Arrays.sort(songs, (x,y) -> x[idx].compareTo(y[idx]));
            
            // print partial result
            printStrings(attr);
            for (String[] song : songs) printStrings(song);
            System.out.println();
        }
        s.close();
    }

}
