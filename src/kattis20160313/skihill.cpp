#include <iostream>
#include <cstdlib>
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
        long    lfun;   // maximum total fun from leftmost desc.
        long    lpen;   // negative penalty between lfun and max fun
        long    mfun;   // maximum total fun of all descendants
        long    rpen;   // negative penalty between max fun and rfun
        long    rfun;   // maximum total fun from rightmost desc.
        
        void zeroInit() {
            cost = lfun = lpen = mfun = rpen = rfun = 0;
        }
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
    
    /* Update node after children have been modified */
    static void updateNode(Node* node, Node* lch, Node* rch) {
        long cost = lch->cost + rch->cost;        
        long f[10];
        f[0] = lch->lfun;   f[5] = rch->lfun;
        f[1] = lch->lpen;   f[6] = rch->lpen;
        f[2] = lch->mfun;   f[7] = rch->mfun;
        f[3] = lch->rpen;   f[8] = rch->rpen;
        f[4] = lch->rfun;   f[9] = rch->rfun; 
        node->zeroInit();
        
        node->cost = cost;
        
        long fun = 0;
        int lidx = 0, ridx = -1;
        for (int i = 0, start = 0; i < 10; i++) {
            fun += f[i];
            if (fun < 0) { fun = 0; start = i + 1; }
            else if (fun > node->mfun) { node->mfun = fun; lidx = start; ridx = i; }
        }
        
        fun = 0;
        for (int i = 0; i < lidx; i++) {
            fun += f[i];
            if (fun > node->lfun) { node->lfun = fun; }
        }
        node->lpen = fun - node->lfun;
        
        fun = 0;
        for (int i = 9; i > ridx; i--) {
            fun += f[i];
            if (fun > node->rfun) { node->rfun = fun; }
        }
        node->rpen = fun - node->rfun;
    }
    
public:
    
    SkiHill(int n, std::istream& in) 
        : N(n), heights(new int[n+1]), nodecnt(nextPow2(n)*2), data(new Node[nodecnt])
    {
        data->zeroInit();
        in >> *heights;
            
        int* height = heights + 1;
        Node* leaf = data + (nodecnt/2);
        for (int i = 0, hl = *heights, hr = 0; i < N; i++, hl = hr, height++, leaf++) {
            // read height
            in >> hr;
            *height = hr;
                        
            // initialize leaf node
            leaf->zeroInit();
            leaf->cost = (long)hl + (long)hr;
            long dh = (long)hl - (long)hr;
            if (dh >= 0) leaf->mfun = dh * dh;
            else leaf->rpen = - dh * dh;
            
            // init parent nodes when all descendants have been initialized
            Node* rch = leaf;
            for (int j = i; j % 2 == 1; j /= 2) {
                Node* par = parent(rch);
                updateNode(par, rch - 1, rch);
                rch = par;
            }
        }
        
        // initialize remaining empty leaves
        for (int i = N; i < nodecnt/2; i++, leaf++) {
            // initialize leaf node
            leaf->zeroInit();
            
            // init parent nodes when all descendants have been initialized
            Node* rch = leaf;
            for (int j = i; j % 2 == 1; j /= 2) {
                Node* par = parent(rch);
                updateNode(par, rch - 1, rch);
                rch = par;
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
            leaf->mfun = dh * dh;
            leaf->rpen = 0;
        } else {
            leaf->mfun = 0;
            leaf->rpen = - dh * dh;
        }
        
        // update parent nodes
        for (Node* ch = leaf; ch != data + 1; ) {
            Node* par = parent(ch);
            Node* lch = leftchild(par);
            updateNode(par, lch, lch + 1);
            ch = par;
        }
    }
    
    void update(int idx, int V) {
        heights[idx] = V;
        if (idx > 0) update_(idx - 1);
        if (idx < N) update_(idx);
    }

    long query(int X, long K) {
        Node q;
        q.zeroInit();
        q.cost = -K;
        
        // add bonus for cabin at X
        for (Node* node = data + (nodecnt/2 + X); X != 0; X >>= 1) {
            if ((X & 1) == 1) {
                node--;
                q.cost -= node->cost;
            }
            node = parent(node);
        }
        
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