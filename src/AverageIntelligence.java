import java.util.Scanner;

public class AverageIntelligence {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        for (int t = s.nextInt(); t > 0; t--) {
            s.nextLine();
            s.nextLine();
            int ncs = s.nextInt(), nec = s.nextInt();
            float avgcs = 0, avgec = 0;
            int[] csiqs = new int[ncs];
            for (int i = 0; i < ncs; i++) {
                csiqs[i] = s.nextInt();
                avgcs += csiqs[i];
            }
            avgcs /= ncs;
            for (int i = 0; i < nec; i++) avgec += s.nextInt();
            avgec /= nec;
            
            int joke = 0;
            for (int i = 0; i < ncs; i++) {
                if (csiqs[i] < avgcs && csiqs[i] > avgec) joke++;
            }
            System.out.println(joke);
        }
        s.close();
    }

}
