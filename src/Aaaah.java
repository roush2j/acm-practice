import java.util.*;

// Submissions to CMICH SP16 #2

public class Aaaah {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String cando = s.nextLine();
        String needed = s.nextLine();
        System.out.println(cando.length() >= needed.length() ? "go" : "no");
        s.close();
    }
}
