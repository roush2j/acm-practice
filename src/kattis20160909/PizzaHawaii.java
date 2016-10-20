package kattis20160909;
import java.util.*;

/**
 * This problem, which I assigned to teams of 2, is straightforward to solve
 * using standard library data structures.
 * 
 * For each distinct italian and english ingredient we build up the set of
 * pizzas on which that ingredient appears. Then we find pairs of italian +
 * english ingredients which appear on the *same* set of pizzas.
 * 
 * https://open.kattis.com/contests/mk2hhn/problems/pizzahawaii
 * 
 * @author jroush
 */
public class PizzaHawaii {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        for (int t = s.nextInt(); t > 0; t--) {

            // allocate maps to store the list of italian/english ingredients 
            // and the associated set of pizzas on which each ingredient appears
            // note that we use TreeMap to guarantee that ingredients are iterated
            // in alphabetical order as required by the problem specs
            Map<String, Set<String>> italian = new TreeMap<>();
            Map<String, Set<String>> english = new TreeMap<>();

            // iterate through pizzas and build set of pizzas for each ingredient
            for (int n = s.nextInt(); n > 0; n--) {
                // read pizza name
                String pizza = s.next();

                // read list of italian ingredients for the pizza
                for (int m = s.nextInt(); m > 0; m--) {
                    String ingred = s.next();
                    Set<String> pizzaSet = italian.get(ingred);
                    if (pizzaSet == null) {
                        // this is the first time this ingredient has appeared
                        // we need to add a new set for it to the map
                        pizzaSet = new HashSet<>();
                        italian.put(ingred, pizzaSet);
                    }
                    pizzaSet.add(pizza); // add pizza to pizza set for ingred
                }

                // read list of english ingredients for the pizza
                for (int m = s.nextInt(); m > 0; m--) {
                    String ingred = s.next();
                    Set<String> pizzaSet = english.get(ingred);
                    if (pizzaSet == null) {
                        pizzaSet = new HashSet<>();
                        english.put(ingred, pizzaSet);
                    }
                    pizzaSet.add(pizza); // add pizza to pizza set for ingred
                }
            }

            // find all pairs of italian+english ingredients with the same
            // set of pizzas
            for (String it : italian.keySet()) {
                Set<String> itPizzas = italian.get(it);
                for (String en : english.keySet()) {
                    Set<String> enPizzas = english.get(en);
                    // compare pizza sets using the Set.equals() method
                    // the specs for the Set interface mandate that this
                    // method return true iif both sets have the same elements
                    if (itPizzas.equals(enPizzas)) {
                        System.out.println("(" + it + ", " + en + ")");
                    }
                }
            }

            // print a blank line between test cases
            System.out.println();
        }

        s.close();
    }

}
