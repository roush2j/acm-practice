import java.util.*;

public class Eligibility {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        s.useDelimiter("[\\s/]+");
        for (int t = s.nextInt(); t > 0; t--) {
            String name = s.next();
            //
            int colyr = s.nextInt();
            int colmo = s.nextInt();
            int coldy = s.nextInt();
            //
            int birthyr = s.nextInt();
            int birthmo = s.nextInt();
            int birthdy = s.nextInt();
            //
            int courses = s.nextInt();
            
            String el = "";
            if (colyr >= 2010) el = "eligible";
            else if (birthyr >= 1991) el = "eligible";
            else if (courses > 40) el = "ineligible";
            else el = "coach petitions";
            System.out.println(name + " " + el);
        }
        s.close();
    }
}
