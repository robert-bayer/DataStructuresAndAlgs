import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.LinkedList;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Queue;
import java.util.PriorityQueue;


/**
 * Your implementation of various different graph algorithms.
 *
 * @author Robert Bayer
 * @version 1.0
 * @userid rbayer6
 * @GTID 903381275
 *
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class GraphAlgorithms {

    /**
     * Performs a breadth first search (bfs) on the input graph, starting at
     * the parameterized starting vertex.
     *
     * When exploring a vertex, explore in the order of neighbors returned by
     * the adjacency list. Failure to do so may cause you to lose points.
     *
     * You may import/use java.util.Set, java.util.List, java.util.Queue, and
     * any classes that implement the aforementioned interfaces, as long as they
     * are efficient.
     *
     * The only instance of java.util.Map that you may use is the
     * adjacency list from graph. DO NOT create new instances of Map
     * for BFS (storing the adjacency list in a variable is fine).
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @param <T>   the generic typing of the data
     * @param start the vertex to begin the bfs on
     * @param graph the graph to search through
     * @return list of vertices in visited order
     * @throws IllegalArgumentException if any input is null, or if start
     *                                  doesn't exist in the graph
     */
    public static <T> List<Vertex<T>> bfs(Vertex<T> start, Graph<T> graph) {
        if (start == null || graph == null) {
            throw new IllegalArgumentException("ERROR: Must provide both a start and a graph object");
        }
        Set<Vertex<T>> vertices = graph.getVertices();
        if (!vertices.contains(start)) {
            throw new IllegalArgumentException("ERROR: Must provide a valid start");
        }

        LinkedHashSet<Vertex<T>> visited = new LinkedHashSet<Vertex<T>>();
        ArrayList<Vertex<T>> returnList = new ArrayList<Vertex<T>>();
        Map<Vertex<T>, List<VertexDistance<T>>> adjacencies = graph.getAdjList();
        Queue<Vertex<T>> q = new LinkedList<>();
        visited.add(start);
        returnList.add(start);
        q.add(start);
        while (!q.isEmpty()) {
            Vertex<T> v = q.poll();
            for (VertexDistance<T> vtx : adjacencies.get(v)) {
                if (!visited.contains(vtx.getVertex())) {
                    visited.add(vtx.getVertex());
                    returnList.add(vtx.getVertex());
                    q.add(vtx.getVertex());
                }
            }
        }

        return returnList;
    }

    /**
     * Performs a depth first search (dfs) on the input graph, starting at
     * the parameterized starting vertex.
     *
     * When exploring a vertex, explore in the order of neighbors returned by
     * the adjacency list. Failure to do so may cause you to lose points.
     *
     * *NOTE* You MUST implement this method recursively, or else you will lose
     * all points for this method.
     *
     * You may import/use java.util.Set, java.util.List, and
     * any classes that implement the aforementioned interfaces, as long as they
     * are efficient.
     *
     * The only instance of java.util.Map that you may use is the
     * adjacency list from graph. DO NOT create new instances of Map
     * for DFS (storing the adjacency list in a variable is fine).
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @param <T>   the generic typing of the data
     * @param start the vertex to begin the dfs on
     * @param graph the graph to search through
     * @return list of vertices in visited order
     * @throws IllegalArgumentException if any input is null, or if start
     *                                  doesn't exist in the graph
     */
    public static <T> List<Vertex<T>> dfs(Vertex<T> start, Graph<T> graph) {
        if (start == null || graph == null) {
            throw new IllegalArgumentException("ERROR: Must provide both a start and a graph object");
        }
        Set<Vertex<T>> vertices = graph.getVertices();
        if (!vertices.contains(start)) {
            throw new IllegalArgumentException("ERROR: Must provide a valid start");
        }

        LinkedList<Vertex<T>> visited = new LinkedList<Vertex<T>>();
        ArrayList<Vertex<T>> returnList = new ArrayList<Vertex<T>>();
        Map<Vertex<T>, List<VertexDistance<T>>> adjacencies = graph.getAdjList();
        return dfsHelp(start, graph, visited, returnList, adjacencies);
    }

    /**
     * A recursive helper function for Depth First Search
     *
     * It takes in both the vertex and the graph along with the lists
     * required to store and compare data without redefining anything.
     *
     * @param start The index it is visiting now
     * @param graph The graph being traversed
     * @param visited The list containing all visited vertices
     * @param returnList The list containing all visited vertices in order
     * @param adjacencies A Map of lists of adjacent vertices to each vertex in
     *                    the graph.
     * @return The completed list of visited vertices
     * @param <T> The generic type of the class
     */
    private static <T> List<Vertex<T>> dfsHelp(Vertex<T> start, Graph<T> graph, LinkedList<Vertex<T>> visited,
                                               ArrayList<Vertex<T>> returnList, Map<Vertex<T>,
            List<VertexDistance<T>>> adjacencies) {
        visited.add(start);
        returnList.add(start);

        for (VertexDistance<T> vtx : adjacencies.get(start)) {
            if (!visited.contains(vtx.getVertex())) {
                dfsHelp(vtx.getVertex(), graph, visited, returnList, adjacencies);
            }
        }
        return returnList;

    }

    /**
     * Finds the single-source shortest distance between the start vertex and
     * all vertices given a weighted graph (you may assume non-negative edge
     * weights).
     *
     * Return a map of the shortest distances such that the key of each entry
     * is a node in the graph and the value for the key is the shortest distance
     * to that node from start, or Integer.MAX_VALUE (representing
     * infinity) if no path exists.
     *
     * You may import/use java.util.PriorityQueue,
     * java.util.Map, and java.util.Set and any class that
     * implements the aforementioned interfaces, as long as your use of it
     * is efficient as possible.
     *
     * You should implement the version of Dijkstra's where you use two
     * termination conditions in conjunction.
     *
     * 1) Check if all of the vertices have been visited.
     * 2) Check if the PQ is empty yet.
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @param <T>   the generic typing of the data
     * @param start the vertex to begin the Dijkstra's on (source)
     * @param graph the graph we are applying Dijkstra's to
     * @return a map of the shortest distances from start to every
     * other node in the graph
     * @throws IllegalArgumentException if any input is null, or if start
     *                                  doesn't exist in the graph.
     */
    public static <T> Map<Vertex<T>, Integer> dijkstras(Vertex<T> start, Graph<T> graph) {
        if (start == null || graph == null) {
            throw new IllegalArgumentException("ERROR: Must provide both a start and a graph object");
        }
        Set<Vertex<T>> vertices = graph.getVertices();
        if (!vertices.contains(start)) {
            throw new IllegalArgumentException("ERROR: Must provide a valid start");
        }

        LinkedHashSet<Vertex<T>> visited = new LinkedHashSet<Vertex<T>>();
        PriorityQueue<VertexDistance> pq = new PriorityQueue<VertexDistance>();
        LinkedHashMap<Vertex<T>, Integer> distanceMap = new LinkedHashMap<Vertex<T>, Integer>();

        Map<Vertex<T>, List<VertexDistance<T>>> adjacencies = graph.getAdjList();

        for (Vertex<T> vtx : vertices) {
            distanceMap.put(vtx, Integer.MAX_VALUE);
        }

        VertexDistance<T> visitedVertex = new VertexDistance<T>(start, 0);
        pq.add(visitedVertex);

        while (!pq.isEmpty() && visited.size() < vertices.size()) {
            VertexDistance<T> v = pq.poll();
            if (!visited.contains(v.getVertex())) {
                visited.add(v.getVertex());
                distanceMap.put(v.getVertex(), v.getDistance());

                if (adjacencies.get(v.getVertex()) != null) {
                    for (VertexDistance<T> vtx : adjacencies.get(v.getVertex())) {
                        if (!visited.contains(vtx.getVertex())) {
                            VertexDistance<T> addVertex = new VertexDistance<T>(vtx.getVertex(), vtx.getDistance()
                                    +
                                    v.getDistance());
                            pq.add(addVertex);
                        }
                    }
                }
            }
        }

        return distanceMap;
    }

    /**
     * Runs Kruskal's algorithm on the given graph and returns the Minimal
     * Spanning Tree (MST) in the form of a set of Edges. If the graph is
     * disconnected and therefore no valid MST exists, return null.
     *
     * You may assume that the passed in graph is undirected. In this framework,
     * this means that if (u, v, 3) is in the graph, then the opposite edge
     * (v, u, 3) will also be in the graph, though as a separate Edge object.
     *
     * The returned set of edges should form an undirected graph. This means
     * that every time you add an edge to your return set, you should add the
     * reverse edge to the set as well. This is for testing purposes. This
     * reverse edge does not need to be the one from the graph itself; you can
     * just make a new edge object representing the reverse edge.
     *
     * You may assume that there will only be one valid MST that can be formed.
     *
     * Kruskal's will also require you to use a Disjoint Set which has been
     * provided for you. A Disjoint Set will keep track of which vertices are
     * connected given the edges in your current MST, allowing you to easily
     * figure out whether adding an edge will create a cycle. Refer
     * to the DisjointSet and DisjointSetNode classes that
     * have been provided to you for more information.
     *
     * You should NOT allow self-loops or parallel edges into the MST.
     *
     * By using the Disjoint Set provided, you can avoid adding self-loops and
     * parallel edges into the MST.
     *
     * You may import/use java.util.PriorityQueue,
     * java.util.Set, and any class that implements the aforementioned
     * interfaces.
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @param <T>   the generic typing of the data
     * @param graph the graph we are applying Kruskals to
     * @return the MST of the graph or null if there is no valid MST
     * @throws IllegalArgumentException if any input is null
     */
    public static <T> Set<Edge<T>> kruskals(Graph<T> graph) {
        if (graph == null) {
            throw new IllegalArgumentException("ERROR: Must provide both a start and a graph object");
        }

        DisjointSet<T> ds = new DisjointSet<T>();
        LinkedHashSet<Edge<T>> mst = new LinkedHashSet<Edge<T>>();
        PriorityQueue<Edge<T>> pq = new PriorityQueue<Edge<T>>(graph.getEdges());

        Set<Vertex<T>> vertices = graph.getVertices();
        Set<Edge<T>> edges = graph.getEdges();
        if (vertices.size() == 0 || edges.size() == 0) {
            return mst;
        }

        while (!pq.isEmpty() && mst.size() < 2 * (graph.getVertices().size() - 1)) {
            Edge<T> edge = pq.poll();
            if (!ds.find(edge.getU()).equals(ds.find(edge.getV()))) {
                Edge<T> reverseEdge = new Edge<T>(edge.getV(), edge.getU(), edge.getWeight());
                ds.union(ds.find(edge.getU()), ds.find(edge.getV()));
                mst.add(edge);
                mst.add(reverseEdge);
            }
        }


        if (mst.size() == 2 * (graph.getVertices().size() - 1)) {
            return mst;
        } else {
            return null;
        }

    }
}
