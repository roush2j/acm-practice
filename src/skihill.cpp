#include <iostream>
#include <cstdlib>
#include <algorithm>
#include <cmath>

int nextPow2(int n) {
    n--;
    n |= n >> 1;
    n |= n >> 2;
    n |= n >> 4;
    n |= n >> 8;
    n |= n >> 16;
    return n + 1;
}

class SkiHill {    
private:
    int     N;          // number of height ranges
    int*    heights;    // N+1 heights
    
    /*
        A compact fixed-size binary tree with power-of-two node count.  
        For each node index i, where 0 <= i < nodecnt:
            Root = 1
            Parent(i) = floor(i/2)
            Left-Child(i) = 2*i
            Right-Child(i) = 2*i + 1
        Leaves are continuous and start at index nodecnt/2
    */
    struct Node {
        long    cost;   // total cost of all descendants
        long    lfun;   // total fun from leftmost desc. to start of max fun
        long    mfun;   // maximum total fun of all left-descendants
        long    rfun;   // total fun from end of max fun to rightmost desc.
    };    
    int     nodecnt;
    Node*   data;
    
    inline Node* parent(const Node* node) const {
        return data + (node - data) / 2;
    }
    
    inline Node* leftchild(const Node* node) const {
        return data + 2 * (node - data);
    }
    
    inline Node* rightchild(const Node* node) const {
        return data + 2 * (node - data) + 1;
    }
    
    struct NodePrint { const SkiHill& hill; const Node& node; };
    NodePrint print(const Node& node) const { return NodePrint {*this,node}; }
    friend std::ostream& operator << (std::ostream&, const NodePrint&);
    friend std::ostream& operator << (std::ostream&, const SkiHill&);
    
    /* Update node after children have been modified */
    static void updateNode(Node* node, Node* lch, Node* rch) {
        node->cost = lch->cost + rch->cost;
        long cfun = lch->mfun + lch->rfun + rch->lfun + rch->mfun;
        if (cfun >= lch->mfun && cfun >= rch->mfun) {
            node->lfun = lch->lfun;
            node->mfun = cfun;
            node->rfun = rch->rfun;
        } else if (lch->mfun >= rch->mfun) {
            node->lfun = lch->lfun;
            node->mfun = lch->mfun;
            node->rfun = lch->rfun + rch->lfun + rch->mfun + rch->rfun;
        } else {
            node->lfun = lch->lfun + lch->mfun + lch->rfun + rch->lfun;
            node->mfun = rch->mfun;
            node->rfun = rch->rfun;
        }
    }
    
public:
    
    SkiHill(int n, std::istream& in) 
        : N(n), heights(new int[n+1]), nodecnt(nextPow2(n)*2), data(new Node[nodecnt])
    {
        data->cost = 0;
        data->lfun = 0;
        data->mfun = 0;
        data->rfun = 0;
        in >> *heights;
            
        int* height = heights + 1;
        Node* leaf = data + (nodecnt/2);
        for (int i = 0, hl = *heights, hr = 0; i < N; i++, hl = hr, height++, leaf++) {
            // read height
            in >> hr;
            *height = hr;
                        
            // initialize leaf node
            leaf->cost = (long)hl + (long)hr;
            long dh = (long)hl - (long)hr;
            if (dh >= 0) {
                leaf->lfun = 0;
                leaf->mfun = dh * dh;
                leaf->rfun = 0;
            } else {
                leaf->lfun = 0;
                leaf->mfun = 0;
                leaf->rfun = - dh * dh;
            }
            //std::cout << print(*leaf) << '\n';
            
            // init parent nodes when all descendants have been initialized
            Node* rch = leaf;
            for (int j = i; j % 2 == 1; j /= 2) {
                Node* par = parent(rch);
                updateNode(par, rch - 1, rch);
                //std::cout << print(*par) << '\n';
                rch = par;
            }
        }
        
        // initialize remaining empty leaves
        for (int i = N; i < nodecnt/2; i++, leaf++) {
            // initialize leaf node
            leaf->cost = 0;
            leaf->lfun = 0;
            leaf->mfun = 0;
            leaf->rfun = 0;
            
            // init parent nodes when all descendants have been initialized
            Node* rch = leaf;
            for (int j = i; j % 2 == 1; j /= 2) {
                Node* par = parent(rch);
                updateNode(par, rch - 1, rch);
                rch = par;
                //std::cout << print(*par) << '\n';
            }
        }
    }
    
    ~SkiHill() { 
        delete [] heights;
        delete [] data;
    }
    
    void update_(int i) {
        int hl = heights[i];
        int hr = heights[i + 1];
        Node* leaf = data + (nodecnt/2) + i;
        
        // update leaf node
        leaf->cost = (long)hl + (long)hr;
        long dh = (long)hl - (long)hr;
        if (dh >= 0) {
            leaf->lfun = 0;
            leaf->mfun = dh * dh;
            leaf->rfun = 0;
        } else {
            leaf->lfun = 0;
            leaf->mfun = 0;
            leaf->rfun = - dh * dh;
        }
        //std::cout << print(*leaf) << '\n';
        
        // update parent nodes
        for (Node* ch = leaf; ch != data + 1; ) {
            Node* par = parent(ch);
            Node* lch = leftchild(par);
            updateNode(par, lch, lch + 1);
            //std::cout << print(*par) << '\n';
            ch = par;
        }
    }
    
    void update(int idx, int V) {
        heights[idx] = V;
        if (idx > 0) update_(idx - 1);
        if (idx < N) update_(idx);
    }

    long query(int X, long K) {
        //std::cout << "Query X=" << X << ", K=" << K;
        Node q {-K,0,0,0};
        
        // add bonus for cabin at X
        for (Node* node = data + (nodecnt/2 + X); X != 0; X >>= 1) {
            if ((X & 1) == 1) {
                node--;
                q.cost -= node->cost;
            }
            node = parent(node);
        }
        //std::cout << "(" << q.cost << ")\n";
        
        // binary search for max fun
        Node* node = data + 1;
        if (q.cost + node->cost <= 0) return node->mfun;
        while ((node - data) < nodecnt) {
            if (q.cost + node->cost <= 0) {
                updateNode(&q, &q, node);
                node++;
            }
            node = leftchild(node);
        }        
        return q.mfun;
    }
};

std::ostream& operator << (std::ostream& out, const SkiHill::NodePrint& n) {
    out << '@' << (&n.node - n.hill.data);
    int i = (&n.node - n.hill.data) - (n.hill.nodecnt/2);
    if (i >= n.hill.N) {
        out << " Empty#" << i;
    } else if (i >= 0) {
        out << " Leaf#" << i;
        out << " h=[" << n.hill.heights[i];
        out << ',' << n.hill.heights[i + 1] << ']';
    }
    out << " cost=" << n.node.cost;
    out << " fun=[" << n.node.lfun << ',' << n.node.mfun << ',' << n.node.rfun << ']';
    return out;
}

std::ostream& operator << (std::ostream& out, const SkiHill& hill) {
    for (int i = 0; i < hill.nodecnt; i++) 
        std::cout << hill.print(hill.data[i]) << '\n';
    return out;
}

int main(int argc, char** args) {
    int N, Q;
    std::cin >> N >> Q;
    SkiHill hill(N, std::cin);
    for (int q = 0; q < Q; q++) {
        int query;
        std::cin >> query;
        switch (query) {
        case 0: {
            int i, V;
            std::cin >> i >> V;
            hill.update(i,V);
            break;
        }
        case 1: {
            int X;
            long K;
            std::cin >> X >> K;
            long result = hill.query(X, K);
            std::cout << result << std::endl;
            break;
        }
        default:
            return -query;
        }
    }
    return 0;
}