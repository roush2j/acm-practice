import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ClassificationCmp {

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
        StringBuilder sb = new StringBuilder();
        for (int ch = System.in.read(); ch >= 0; ch = System.in.read()) {
            if (ch >= '0' && ch <= '9') sb.append((char) ch);
            else break;
        }
        final int N = Integer.parseInt(sb.toString());
        int[] a = parse(N, System.in);
        int[] b = parse(N, System.in);

        int[] nodet = new int[2 * N];
        for (int i = 0; i < 2 * N; i += 2) {
            nodet[i] = nodet[i + 1] = i;
        }
        int matchcnt = N;
        
        
        printTree(N, a);
        System.out.println();
        printTree(N, b);
    }
}
