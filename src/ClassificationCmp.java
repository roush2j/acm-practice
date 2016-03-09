import java.io.*;
import java.util.*;

public class ClassificationCmp {

    public static void randomTree(PrintStream out, Random rand, int[] vals,
            int start, int len) {
        if (len == 1) {
            out.print(vals[start]);
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

    /**
     * Parse a parenthesis- and comma-delimited tree structure with exactly
     * {@code N} leaves numbered {@code 1 .. N}.
     * 
     * @return An array where every 2 elements are a node, containing the index
     *         of its parent node and the number of descendant nodes. The layout
     *         is as follows:
     * 
     *         <pre>
     * 0 <= i < 2*N     leaf with value i/2 + 1, contains index of parent and (1)
     * 2*N == i         root node, contains -1 and (N)
     * 2*N + 2 <= i     intermediate node, contains index of parent and (x)
     * </pre>
     */
    public static int[] parse(final int N, InputStream in) throws IOException {
        final int logN = 32 - Integer.numberOfLeadingZeros(2 * N - 1);
        StringBuilder buf = new StringBuilder();
        int[] tree = new int[2 * N * logN];
        int treec = 2 * N;
        int root = -1;

        for (int ch = in.read(); ch >= 0; ch = in.read()) {
            switch (ch) {
            case ' ':
            case '\n':
            case '\r':
                break;  // skip whitespace
            case '(':
                if (treec == tree.length)
                    tree = Arrays.copyOf(tree, treec * 2);
                tree[treec++] = root;
                tree[treec++] = 0;
                root = treec - 2;
                break;
            case ')':
            case ',':
                if (buf.length() > 0) {
                    int val = Integer.parseInt(buf.toString());
                    tree[2 * val - 2] = root;
                    tree[2 * val - 1] = 1;
                    tree[root + 1]++;
                    buf.setLength(0);
                }
                if (ch == ')') {
                    int c = tree[root + 1];
                    root = tree[root];
                    if (root < 0) return Arrays.copyOf(tree, treec);
                    else tree[root + 1] += c;
                }
                break;
            default:
                buf.append((char) ch);
                break;
            }
        }
        throw new RuntimeException("EOF");
    }

    public static void printTree(int N, int[] tree) {
        int[] stack = new int[N];
        for (int i = 0; i < N; i++) {
            int stackc = 0;
            for (int node = 2 * i; node >= 0; node = tree[node]) {
                if (stackc == stack.length)
                    stack = Arrays.copyOf(stack, stackc * 2);
                stack[stackc++] = node;
            }
            while (--stackc >= 0) {
                System.out.print(stack[stackc] / 2 + 1);
                System.out.print("(" + tree[stack[stackc] + 1] + ")");
                System.out.print(' ');
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {

        // parse number of leaves
        StringBuilder sb = new StringBuilder();
        for (int ch = System.in.read(); ch >= 0; ch = System.in.read()) {
            if (ch >= '0' && ch <= '9') sb.append((char) ch);
            else break;
        }
        final int N = Integer.parseInt(sb.toString());

        // randomized inputs
        if (true) {
            PrintStream out = new PrintStream("Classification-in-" + N);
            out.println(N);
            randomInputLine(out, new Random(), N);
            randomInputLine(out, new Random(), N);
            return;
        }

        // parse trees
        int[] a = parse(N, System.in);
        int[] b = parse(N, System.in);

        //
        int[] nodemap = new int[2 * N];
        for (int i = 0; i < 2 * N; i += 2) {
            nodemap[i] = nodemap[i + 1] = i;
        }
        int matchingNodes = 0;

        // compare trees
        for (int baseleaf = 0; true; baseleaf += 2) {
            if (baseleaf >= 2 * N) baseleaf = 0;

            // check if all descendents have reached A-node
            int anode = nodemap[baseleaf];
            int acnt = 0;
            for (int l = 0; l < N * 2; l += 2) {
                if (nodemap[l] == anode) acnt++;
            }
            if (acnt != a[anode + 1]) continue;

            // search upward until we find a common ancestor in B for
            // all leaves that descend from the target A-node
            int bcnt = acnt;
            for (int l = baseleaf, ances = -1, match = 0; match < acnt; l += 2) {
                if (l >= 2 * N) l = 0;
                if (nodemap[l] != anode) continue;

                // search upwards from leaf in B to find deepest node that 
                // exceeds specified descendant count
                int bnode = nodemap[l + 1];
                for (; b[bnode + 1] < bcnt; bnode = b[bnode]) {}
                if (bnode == ances) match++;
                else {
                    if (match > 0 && b[bnode + 1] == bcnt) bnode = b[bnode];
                    ances = bnode;
                    bcnt = b[bnode + 1];
                    match = 1;
                }
                nodemap[l + 1] = bnode;
            }

            // check if common ancestor is exact match for target A-node
            if (bcnt == acnt) {
                matchingNodes++;
            }

            // advance A-node
            int parent = a[anode];
            for (int l = 0; l < N * 2; l += 2) {
                if (nodemap[l] == anode) nodemap[l] = parent;
            }
            if (acnt == N) break;
        }

        System.out.println(matchingNodes);
    }
}
