
import java.util.Scanner;

/**
 *
 * @author moroujmohad
 * @author reemyziz
 * @author nojoodGMD
 *
 */
public class CPCS324ProjectDEMO {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        try {

            // introduction
            System.out.println(" -------------------------- MST Algorithms Run-Time Comparison --------------------------- \n");
            System.out.println("This program will randomly generate undirected weighted"
                    + "\ngraph with a user defiend number of edges and vertices, then apply: "
                    + "\n1- Kruskal's algorithm"
                    + "\n2- Prim's algorithm - based on priority queue"
                    + "\n3- Prim's algorithm - based on min heap"
                    + "\n\nNOTE: - we do all three algorithms in a single run so that we can reduce number of runs needed."
                    + "\n      - weights are randomly assigned to the edges with values range between 1 and 20.\n");

            // to record the running time
            double startTime, endTime;

            // to read number of vertices and edges from the user
            Scanner input = new Scanner(System.in);
            System.out.print("> Enter number of vertices: ");
            int nv = input.nextInt();
            System.out.print("> Enter number of edges: ");
            int ne = input.nextInt();

            // generate a connected graph randomly
            Graph graph = new Graph(nv, ne); // create graph object
            graph = graph.make_graph(graph); // generate random graph

            System.out.println("> Graph is successfully generated.");
            System.out.println(" ----------------------------------------------------------------------------------------- \n");

            // Apply all algoritms and record the run time for each algorithm 
            // ---------------------------------------------------------------------
            startTime = System.currentTimeMillis();
            graph.kruskal();
            endTime = System.currentTimeMillis();

            System.out.println("Kruskal algorithm's run time =  " + (endTime - startTime) + " ms.\n");

            // ---------------------------------------------------------------------
            startTime = System.currentTimeMillis();
            graph.primPQ();
            endTime = System.currentTimeMillis();

            System.out.println("Run time of Prim's algorithm based on priority queue =  " + (endTime - startTime) + " ms.\n");

            // ---------------------------------------------------------------------
            startTime = System.currentTimeMillis();
            graph.primMH();
            endTime = System.currentTimeMillis();

            System.out.println("Run time of Prim's algorithm based on min heap =  " + (endTime - startTime) + " ms.\n");
            
                        System.out.println(" ========================================================================================= \n");

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

    }

}
