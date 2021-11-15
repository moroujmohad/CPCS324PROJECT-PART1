/**
 *
 * @author moroujmohad
 * @author reemyziz
 * @author nojoodGMD
 * 
 */
public class MinHeap {

    int capacity;
    int currentSize;
    HeapNode[] mH;
    int[] indexes; //will be used to decrease the MinHeap

    /**
     *
     * @param capacity
     */
    public MinHeap(int capacity) {
        this.capacity = capacity;
        mH = new HeapNode[capacity + 1];
        indexes = new int[capacity];
        mH[0] = new HeapNode();
        mH[0].key = Integer.MIN_VALUE;
        mH[0].vertex = -1;
        currentSize = 0;
    }

    /**
     *  
     */
    public void display() {
        for (int i = 0; i <= currentSize; i++) {
            System.out.println(" " + mH[i].vertex + "   key   " + mH[i].key);
        }
        System.out.println("________________________");
    }

    /**
     *
     * @param x
     */
    public void insert(HeapNode x) {
        currentSize++;
        int idx = currentSize;
        mH[idx] = x;
        indexes[x.vertex] = idx;
        bubbleUp(idx);
    }

    /**
     *
     * @param pos
     */
    public void bubbleUp(int pos) {
        int parentIdx = pos / 2;
        int currentIdx = pos;
        while (currentIdx > 0 && mH[parentIdx].key > mH[currentIdx].key) {
            HeapNode currentNode = mH[currentIdx];
            HeapNode parentNode = mH[parentIdx];

            //swap the positions
            indexes[currentNode.vertex] = parentIdx;
            indexes[parentNode.vertex] = currentIdx;
            swap(currentIdx, parentIdx);
            currentIdx = parentIdx;
            parentIdx = parentIdx / 2;
        }
    }

    /**
     *
     * @return
     */
    public HeapNode extractMin() {
        HeapNode min = mH[1];
        HeapNode lastNode = mH[currentSize];
        
        // update the indexes[] and move the last node to the top
        indexes[lastNode.vertex] = 1;
        mH[1] = lastNode;
        mH[currentSize] = null;
        sinkDown(1);
        currentSize--;
        return min;
    }

    /**
     *
     * @param key
     */
    public void sinkDown(int key) {
        int smallest = key;
        int leftChildIdx = 2 * key;
        int rightChildIdx = 2 * key + 1;
        if (leftChildIdx < heapSize() && mH[smallest].key > mH[leftChildIdx].key) {
            smallest = leftChildIdx;
        }
        if (rightChildIdx < heapSize() && mH[smallest].key > mH[rightChildIdx].key) {
            smallest = rightChildIdx;
        }
        if (smallest != key) {

            HeapNode smallestNode = mH[smallest];
            HeapNode kNode = mH[key];

            //swap the positions
            indexes[smallestNode.vertex] = key;
            indexes[kNode.vertex] = smallest;
            swap(key, smallest);
            sinkDown(smallest);
        }
    }

    /**
     *
     * @param a
     * @param b
     */
    public void swap(int a, int b) {
        HeapNode temp = mH[a];
        mH[a] = mH[b];
        mH[b] = temp;
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return currentSize == 0;
    }

    /**
     *
     * @return
     */
    public int heapSize() {
        return currentSize;
    }
    
    
    
}
