import java.util.Scanner;


public class TravelDistance {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        for (int n = s.nextInt(); n >= 0; n = s.nextInt()) {
            int dist = 0, time = 0;
            while (n-- > 0) {
                int speed = s.nextInt();
                int hr = s.nextInt();
                dist += speed * (hr - time); // guaranteed to be positive
                time = hr;
            }
            System.out.println(dist + " miles");
        }
        s.close();
    }

}
