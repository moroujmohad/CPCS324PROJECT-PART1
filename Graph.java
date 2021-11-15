// CPCS324 - PROJECT (PART 1)
// 11/15/2021

// Group members: 
// 1- Reem Abdulaziz Altamimi (1906580) - Leader
// 2- Nojood Othman Alghamdi (1906145)
// 3- Morouj Mohammed Hamdhy (1911029)

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;
import javafx.util.Pair;

/**
 *
 * @author moroujmohad
 * @author reemyziz
 * @author nojoodGMD
 *
 */
public class Graph {

    int nv, ne;
    LinkedList<Edge>[] adjList;

    /**
     * // initialize the graph with vertices and edges chosen the user
     *
     * @param nv
     * @param ne
     */
    Graph(int nv, int ne) {
        this.nv = nv;
        this.ne = ne;
        adjList = new LinkedList[nv];

        // new adjacencey list for each vertex        
        for (int i = 0; i < nv; i++) {
            adjList[i] = new LinkedList<>();
        }
    }

    /**
     * // this method generate a random undirected graph
     * 
     * @param graph
     * @return
     */
    public Graph make_graph(Graph graph) {
          // make a connected graph 
        // (minimum edges = nv -1)
        for (int i = 0; i < nv - 1; i++) {
            // i = current vertex, i + 1 = next vertix
            addEdge(i, i + 1);
        }

        // make random graph with remaining edges 
        // (remaining edges = ne - (nv -1))
        Random random = new Random(); // to pick a random vertix
        int srcVertex, desVertex;
        boolean isConnected;
        for (int i = 0; i < (ne - nv + 1); i++) {
            srcVertex = random.nextInt(nv); // pick random source of the edge
            desVertex = random.nextInt(nv); // pick random distenation of the edge

            // is there an edge between them?
            isConnected = isConnected(srcVertex, desVertex);
            if (srcVertex == desVertex || isConnected) {
                i--; // do not count this iteration
                continue; // generate another pairs
            }

            // no edge between these vertices
            // add an edge
            addEdge(srcVertex, desVertex);

        }

        return graph;
    }

    /**
     *
     * is there an edge between them? (used in make_graph method)
     *
     * @param srcVertex
     * @param desVertex
     * @return
     */
    public boolean isConnected(int srcVertex, int desVertex) {
        // iterate over the ajacency lists of the graph
        for (LinkedList<Edge> i : this.adjList) {
            for (Edge edge : i) {
                // for each edge in each adjacency list, check:
                if ((edge.srcVertex == srcVertex && edge.desVertex == desVertex)
                        || (edge.srcVertex == desVertex && edge.desVertex == srcVertex)) {
                    return true; // --> there is an edge and they are connected
                }
            }
        }

        return false; // no edges! 
    }

    /**
     * add undirected edge to both source and destination vertices (used in
     * make_graph method)
     *
     * @param srcVertex
     * @param desVertex
     */
    public void addEdge(int srcVertex, int desVertex) {
        Edge edge; // create an edge object
        // random weight (range of weights is from 1 to 20)
        // same weight for both directions
        int weight = (int) (Math.random() * 20) + 1;

        // intialize src to des edge, add it to the list
        edge = new Edge(srcVertex, desVertex, weight);
        adjList[srcVertex].add(edge);

        // intialize des to src edge, add it to the list
        edge = new Edge(desVertex, srcVertex, weight);
        adjList[desVertex].add(edge);

    }

    // -------------------------------------------------------------------------
    /**
     * Kruskal's Algorithm
     * 
     */
    public void kruskal() {

        // copy all edges to one linked list so that it can be ordered by weights
        // modified data type from ArrayList to LinkedList
        LinkedList<Edge>[] allEdges = adjList.clone();

        // create the priority queue, ordered using comparator
        PriorityQueue<Edge> pq = new PriorityQueue<>(ne, Comparator.comparingInt(o -> o.weight));

        //add all the edges to priority queue, and sort the edges on weights
        for (int i = 0; i < allEdges.length; i++) {
            for (int j = 0; j < allEdges[i].size(); j++) {
                pq.add(allEdges[i].get(j));
            }
        }

        // create the source []
        int[] source = new int[nv];

        // creating the new element with the source pointer to itself.
        for (int i = 0; i < nv; i++) {
            source[i] = i;
        }

        // initialize MST graph as alinked list
        LinkedList<Edge> mst = new LinkedList<>();

        // process vertices - 1 edges
        for (int edges = 0; edges < (nv - 1) && !pq.isEmpty(); edges++) {
            Edge edge = pq.remove();

            // check if adding this edge creates src cycle
            int src = findCycle(source, edge.srcVertex);
            int des = findCycle(source, edge.desVertex);

            if (src == des) {
                //ignore, will create cycle
                edges--; // don't count this edge

            } else {
                // add to MST graph
                mst.add(edge);
                union(source, src, des);
            }
        }

        // print the cost
        System.out.println(printMSTCost(mst));
    }

    /**
     *
     * chain of source pointers from x upwards through the tree until an element
     * is reached whose source is itself
     *
     *
     * @param source
     * @param vertex
     * @return
     */
    public int findCycle(int[] source, int vertex) {

        if (source[vertex] != vertex) {
            return findCycle(source, source[vertex]);
        }
        return vertex;
    }

    /**
     * make parent vertex as source of destination
     * 
     * @param parent
     * @param src
     * @param des
     */
    public void union(int[] parent, int src, int des) {
        int src_source = findCycle(parent, src);
        int des_source = findCycle(parent, des);

        
        parent[des_source] = src_source;
    }

    /**
     * sum all weights
     *
     * @param edgeList
     * @return
     */
    public String printMSTCost(LinkedList<Edge> edgeList) {
        int cost = 0; // intialize the cost by zero 

        for (int i = 0; i < edgeList.size(); i++) {
            cost += edgeList.get(i).weight;
        }

        return ("MST Cost = " + cost); // print result
    }

    // -------------------------------------------------------------------------
    /**
     * Prim's Algorithm based on Priprity Queue
     *
     */
    public void primPQ() {

        boolean[] mst = new boolean[nv];
        Set[] resultSet = new Set[nv];
        int[] key = new int[nv];  //keys used to store the key to know whether priority queue update is required

        // initialize all the keys to infinity and
        // initialize result for all the vertices
        for (int i = 0; i < nv; i++) {
            key[i] = Integer.MAX_VALUE; // ==> infinity
            resultSet[i] = new Set();
        }

        // initialize priority queue
        // override the comparator to do the sorting based keys
        PriorityQueue<Pair<Integer, Integer>> pq = new PriorityQueue<>(nv, (Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) -> {
            //sort using key values
            int key1 = p1.getKey();
            int key2 = p2.getKey();
            return key1 - key2;
        });

        // create the pair for for the first edges, 0 key 0 edges
        key[0] = 0;
        Pair<Integer, Integer> p0 = new Pair<>(key[0], 0);

        // add it to pq
        pq.offer(p0);
        resultSet[0] = new Set();
        resultSet[0].source = -1;

        // while priority queue is not empty
        while (!pq.isEmpty()) {
            // extract the min
            Pair<Integer, Integer> extractedPair = pq.poll();

            // extracted vertex
            int extractedVertex = extractedPair.getValue();
            mst[extractedVertex] = true;

            // iterate through all the adjacent vertices and update the keys
            LinkedList<Edge> list = adjList[extractedVertex];
            for (int i = 0; i < list.size(); i++) {
                Edge edge = list.get(i);

                // only if edge destination is not present in mst
                if (mst[edge.desVertex] == false) {
                    int destination = edge.desVertex;
                    int newKey = edge.weight;

                    // check if updated key < existing key, if yes, update if
                    if (key[destination] > newKey) {

                        //add it to the priority queue
                        Pair<Integer, Integer> p = new Pair<>(newKey, destination);
                        pq.offer(p);

                        //update the result for destination vertex
                        resultSet[destination].source = extractedVertex;
                        resultSet[destination].weight = newKey;

                        //update the key[]
                        key[destination] = newKey;
                    }
                }
            }
        }

        // print mst
        System.out.println(printMSTcost(resultSet));
    }

    /**
     *
     * @author moroujmohad
     * @author reemyziz
     * @author nojoodGMD
     *
     */
    public static class Set {

        int source;
        int weight;
    }

    // -------------------------------------------------------------------------
    /**
     *
     * Prim's Algorithm using Minheap
     *
     */
    public void primMH() {

        boolean[] inHeap = new boolean[nv];
        Set[] result = new Set[nv];

        // keys[] used to store the key to know whether min hea update is required
        int[] key = new int[nv];
        //create heapNode for all the vertices
        HeapNode[] heapNodes = new HeapNode[nv];
        for (int i = 0; i < nv; i++) {
            heapNodes[i] = new HeapNode();
            heapNodes[i].vertex = i;
            heapNodes[i].key = Integer.MAX_VALUE;
            result[i] = new Set();
            result[i].source = -1;
            inHeap[i] = true;
            key[i] = Integer.MAX_VALUE;
        }

        // decrease the key for the first edges
        heapNodes[0].key = 0;

        // add all the vertices to the key
        MinHeap minHeap = new MinHeap(nv);

        // add all the vertices to priority queue
        for (int i = 0; i < nv; i++) {
            minHeap.insert(heapNodes[i]);
        }

        // while minHeap is not empty
        while (!minHeap.isEmpty()) {

            // extract the min
            HeapNode extractedNode = minHeap.extractMin();

            // extracted vertex
            int extractedVertex = extractedNode.vertex;
            inHeap[extractedVertex] = false;

            // iterate through all the adjacent vertices
            LinkedList<Edge> list = adjList[extractedVertex];

            for (int i = 0; i < list.size(); i++) {
                Edge edge = list.get(i);

                //only if edge destination is present in heap
                if (inHeap[edge.desVertex]) {
                    int destination = edge.desVertex;
                    int newKey = edge.weight;

                    //check if updated key < existing key, if yes, update if
                    if (key[destination] > newKey) {
                        decreaseKey(minHeap, newKey, destination);
                        //update the source node for destination
                        result[destination].source = extractedVertex;
                        result[destination].weight = newKey;
                        key[destination] = newKey;
                    }
                }
            }
        }

        //print mst
        System.out.println(printMSTcost(result));
    }

    /**
     * update the value of edge key
     * 
     * @param minHeap
     * @param newKey
     * @param vertex
     */
    public void decreaseKey(MinHeap minHeap, int newKey, int vertex) {

        //get the edges which key's needs the decrease;
        int index = minHeap.indexes[vertex];

        //get the node and update its value
        HeapNode node = minHeap.mH[index];
        node.key = newKey;
        minHeap.bubbleUp(index);
    }

    /**
     * 
     * sum weights and print the total cost
     * 
     * @param resultSet
     * @return
     */
    public String printMSTcost(Set[] resultSet) {
        int total_min_weight = 0;
        for (int i = 1; i < nv; i++) {
            total_min_weight += resultSet[i].weight;
        }
        return ("Total cost: " + total_min_weight);
    }
}
