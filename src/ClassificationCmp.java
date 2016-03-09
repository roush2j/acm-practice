import java.io.*;
import java.util.*;

public class ClassificationCmp {

    public static void randomTree(PrintStream out, Random rand, int[] vals,
            int start, int len) {
        if (len == 1) {
            out.print(vals[start] + 1);
            return;
        }
        int children = 1 + Integer.numberOfLeadingZeros(rand.nextInt()) / 2;
        if (children == 1) {
            randomTree(out, rand, vals, start, len);
            return;
        } else if (children > len) children = len;
        float childpop = len / (float) children;
        int off = 0;
        out.append('(');
        for (int c = 1; c < children && off < len; c++) {
            int l = Math.round(childpop * (rand.nextFloat() + rand.nextFloat())
                    / 2.0F);
            if (l == 0) l = 1;
            randomTree(out, rand, vals, start + off, l);
            out.append(',');
            off += l;
        }
        if (off < len) {
            randomTree(out, rand, vals, start + off, len - off);
        }
        out.append(')');
    }

    public static void randomInputLine(PrintStream out, Random rand, int N) {
        int[] vals = new int[N];
        for (int i = 0; i < vals.length; i++)
            vals[i] = i;
        for (int i = vals.length - 1; i >= 0; i--) {
            int j = rand.nextInt(i + 1);
            int tmp = vals[i];
            vals[i] = vals[j];
            vals[j] = tmp;
        }
        randomTree(out, rand, vals, 0, N);
        out.println();
    }

    public static void orderedInputLine(PrintStream out, int N, boolean order) {
        if (order) {
            for (int i = 1; i < N; i++)
                out.append('(');
            out.append('1');
            for (int i = 1; i < N; i++) {
                out.append(',');
                out.print(i + 1);
                out.append(')');
            }
        } else {
            for (int i = N; i > 1; i--) {
                out.append('(');
                out.print(i);
                out.append(',');
            }
            out.append('1');
            for (int i = 1; i < N; i++)
                out.append(')');
        }
        out.println();
    }

    /**
     * Parsed tree structure. The first N nodes are the leaves in order of
     * value, the N+1 node is the root, and the remaining nodes follow.
     */
    public static class Tree {
        /** Number of Leaves in the tree */
        public final int   N;

        /** Index to the parent of the ith node */
        public final int[] parent;

        /** Number of leaves descending from the ith node */
        public final int[] descCnt;

        /** Index to the ith leaf node in depth-first visitation order */
        public final int[] dforder;

        /**
         * Parse a parenthesis- and comma-delimited tree structure with exactly
         * {@code N} leaves numbered {@code 1 .. N}.
         */
        public Tree(final int N, InputStream in) throws IOException {
            this.N = N;
            final int logN = 32 - Integer.numberOfLeadingZeros(2 * N - 1);
            StringBuilder buf = new StringBuilder();
            this.dforder = new int[N];
            int leafc = 0;
            int[] treepar = new int[N * logN];
            int[] treecnt = new int[N * logN];
            int treec = N;
            int root = -1;

            for (int ch = in.read(); ch >= 0; ch = in.read()) {
                switch (ch) {
                case ' ':
                case '\n':
                case '\r':
                    break;  // skip whitespace
                case '(':
                    if (treec == treepar.length) {
                        treepar = Arrays.copyOf(treepar, treec * 2);
                        treecnt = Arrays.copyOf(treecnt, treec * 2);
                    }
                    treepar[treec++] = root;
                    root = treec - 1;
                    break;
                case ')':
                case ',':
                    if (buf.length() > 0) {
                        int val = Integer.parseInt(buf.toString());
                        treepar[val - 1] = root;
                        treecnt[val - 1] = 1;
                        treecnt[root]++;
                        dforder[leafc++] = val - 1;
                        buf.setLength(0);
                    }
                    if (ch == ')') {
                        int c = treecnt[root];
                        root = treepar[root];
                        if (root < 0) {
                            this.parent = Arrays.copyOf(treepar, treec);
                            this.descCnt = Arrays.copyOf(treecnt, treec);
                            return;
                        } else treecnt[root] += c;
                    }
                    break;
                default:
                    buf.append((char) ch);
                    break;
                }
            }

            throw new RuntimeException("EOF");
        }

        /** Print the tree showing the descent of all nodes */
        public void print(PrintStream out) {
            int[] stack = new int[N];
            for (int i = 0; i < N; i++) {
                int stackc = 0;
                for (int node = dforder[i]; node >= 0; node = parent[node]) {
                    if (stackc == stack.length) {
                        stack = Arrays.copyOf(stack, stackc * 2);
                    }
                    stack[stackc++] = node;
                }
                while (--stackc >= 0) {
                    out.print(stack[stackc] + 1);
                    out.print("(" + descCnt[stack[stackc]] + ")");
                    out.print(' ');
                }
                out.println();
            }
        }
    }

    public static void main(String[] args) throws IOException {

        if (true) {
            System.setIn(new FileInputStream("Classification-in-10000b"));
        }

        // parse number of leaves
        StringBuilder sb = new StringBuilder();
        for (int ch = System.in.read(); ch >= 0; ch = System.in.read()) {
            if (ch >= '0' && ch <= '9') sb.append((char) ch);
            else break;
        }
        final int N = Integer.parseInt(sb.toString());

        // randomized inputs
        if (false) {
            PrintStream out = new PrintStream("Classification-out");
            out.println(N);
            randomInputLine(out, new Random(), N);
            randomInputLine(out, new Random(), N);
            return;
        } else if (false) {
            PrintStream out = new PrintStream("Classification-out");
            out.println(N);
            orderedInputLine(out, N, true);
            orderedInputLine(out, N, false);
            out.println(2 * N - 1);
            return;
        }

        // parse trees
        Tree a = new Tree(N, System.in);
        Tree b = new Tree(N, System.in);
//        System.out.println(N);
//        a.print(System.out);
//        System.out.println();
//        b.print(System.out);
//        System.out.println();

        //
        int[] anodes = Arrays.copyOf(a.dforder, N);
        int[] bnodes = Arrays.copyOf(a.dforder, N);
        int matchingNodes = 0;

        // compare trees
        for (int baseleaf = 0;;) {
            if (baseleaf >= N) baseleaf = 0;

            // find next node in A for which all descendents have been checked
            // this relies on the assumption that, for *every* node in A,
            // all descendant leaves are contiguous in our initial node ordering
            final int anode = anodes[baseleaf];
            final int acnt = a.descCnt[anode];
            final int endleaf = baseleaf + acnt;
            {
                int l = baseleaf + 1;
                for (; l < N && anodes[l] == anode; l++) {}
                if (l != endleaf) {
                    baseleaf = l;
                    continue;
                }
            }

            // search upward until we find a common ancestor in B for
            // all leaves that descend from the target A-node
            int bcnt = acnt;
            for (int l = baseleaf, ances = -1, match = 0; match < acnt; l++) {
                if (l >= endleaf) l = baseleaf;

                // search upwards from leaf in B to find deepest node that 
                // exceeds specified descendant count
                int bnode = bnodes[l];
                for (; b.descCnt[bnode] < bcnt; bnode = b.parent[bnode]) {}
                if (bnode == ances) match++;
                else {
                    if (match > 0 && b.descCnt[bnode] == bcnt)
                        bnode = b.parent[bnode];
                    ances = bnode;
                    bcnt = b.descCnt[bnode];
                    match = 1;
                }
                bnodes[l] = bnode;
            }

            // check if common ancestor is exact match for target A-node
            if (bcnt == acnt) {
                matchingNodes++;
//                System.out.print("{");
//                for (int l = baseleaf; l < endleaf; l++) {
//                    System.out.print(a.dforder[l] + 1);
//                    System.out.append(',');
//                }
//                System.out.println("}");
            }
            if (acnt == N) break;

            // advance A-node
            int parent = a.parent[anode];
            for (int l = baseleaf; l < endleaf; l++) {
                anodes[l] = parent;
            }
            baseleaf = endleaf;
        }

        System.out.println(matchingNodes);
        if (true) {
            sb = new StringBuilder();
            for (int ch = System.in.read(); ch >= 0; ch = System.in.read()) {
                if (ch >= '0' && ch <= '9') sb.append((char) ch);
            }
            if (sb.length() > 0) {
                final int expected = Integer.parseInt(sb.toString());
                System.out.println(" == " + expected + " : "
                        + (expected == matchingNodes));
            }
        }
    }
}
