import java.util.*;


public class DataStructureOracle {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        while (s.hasNextInt()) {
            Stack<Integer> stack = new Stack<>();
            Queue<Integer> queue = new ArrayDeque<>();
            PriorityQueue<Integer> pqueue = new PriorityQueue<>(Comparator.reverseOrder());
            boolean mstack = true, mqueue = true, mpqueue = true;
            for (int n = s.nextInt(); n > 0; n--) {
                int type = s.nextInt();
                int val = s.nextInt();
                switch (type) {
                case 1:
                    stack.push(val);
                    queue.add(val);
                    pqueue.add(val);
                    break;
                case 2:
                    mstack &= !stack.isEmpty() && stack.pop() == val;
                    mqueue &= !queue.isEmpty() && queue.poll() == val;
                    mpqueue &= !pqueue.isEmpty() && pqueue.poll() == val;
                    break;
                }
            }
            int total = (mstack ? 1 : 0) + (mqueue ? 1 : 0) + (mpqueue ? 1 : 0);
            if (total > 1) System.out.println("not sure");
            else if (mstack) System.out.println("stack");
            else if (mqueue) System.out.println("queue");
            else if (mpqueue) System.out.println("priority queue");
            else System.out.println("impossible");
        }
        s.close();
    }

}
