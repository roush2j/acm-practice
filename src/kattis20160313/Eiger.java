package kattis20160313;
import java.util.*;
import java.util.regex.*;

public class Eiger {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Map<String, Integer> vals = new HashMap<>();
        Pattern evalp = Pattern.compile("([\\s\\w]+) ([<=>]) ([\\s\\w]+)");
        while (s.hasNext()) {
            String cmd = s.next();
            s.skip(" ");
            if (cmd.equalsIgnoreCase("define")) {
                int val = s.nextInt();
                s.skip(" ");
                String name = s.nextLine();
                vals.put(name, val);
            } else if (cmd.equalsIgnoreCase("eval")) {
                //
                String line = s.nextLine();
                Matcher r = evalp.matcher(line);
                r.matches();
                String a = r.group(1);
                char op = r.group(2).charAt(0);
                String b = r.group(3);
                //
                Integer va = vals.get(a);
                Integer vb = vals.get(b);
                if (va == null || vb == null) System.out.println("undefined");
                else {
                    switch (op) {
                    case '<':
                        System.out.println(va < vb);
                        break;
                    case '>':
                        System.out.println(va > vb);
                        break;
                    case '=':
                        System.out.println(va == vb);
                        break;
                    default:
                        throw new RuntimeException("Bad operator: " + op);
                    }
                }
            } else throw new RuntimeException("Bad command: " + cmd);
        }
        s.close();
    }
}
