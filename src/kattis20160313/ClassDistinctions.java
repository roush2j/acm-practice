package kattis20160313;
import java.util.*;

public class ClassDistinctions {

    static class Person implements Comparable<Person> {
        String name;

        int    cls;

        @Override public int compareTo(Person o) {
            int cc = -Integer.compare(cls, o.cls);
            if (cc == 0) cc = name.compareTo(o.name);
            return cc;
        }
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        s.useDelimiter("[\\s:\\-]+");
        for (int t = s.nextInt(); t > 0; t--) {
            final int n = s.nextInt();
            Person[] people = new Person[n];
            for (int i = 0; i < n; i++) {
                people[i] = new Person();
                people[i].name = s.next();
                people[i].cls = 0x55555;
                for (String d = s.next(); !d.equals("class"); d = s.next()) {
                    people[i].cls >>>= 2;
                    if (d.equals("upper")) people[i].cls |= 0xC0000;
                    else if (d.equals("middle")) people[i].cls |= 0x40000;
                    else if (d.equals("lower")) people[i].cls |= 0x00000;
                    else throw new RuntimeException("Illegal Class " + d);
                }
            }
            Arrays.sort(people);
            for (Person p : people) {
                System.out.println(p.name);
            }
            System.out.println("==============================");
        }
        s.close();
    }
}
