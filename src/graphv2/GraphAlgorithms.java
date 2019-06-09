package graphv2;

import java.util.*;

public class GraphAlgorithms {

    /**
     * Recursive function calculates all paths.
     *
     * @param graph       The graph.
     * @param source      The source vertex.
     * @param destination The destination vertex.
     * @param isVisited   The visited nodes map.
     * @param path        The path being calculated.
     * @param allPaths    All the paths already found.
     */
    private static void allPaths(Graph graph, Vertex source, Vertex destination, Map<Vertex, Boolean> isVisited,
                          Deque<Vertex> path, List<Deque<Vertex>> allPaths) {

        // Mark the current node as visited
        isVisited.replace(source, true);

        // Exit condition
        if (source.equals(destination)) {

            // Add path to all paths
            allPaths.add(new ArrayDeque(path));

            // if match found then no need to traverse more till depth
            isVisited.replace(source, false);
            return;
        }

        // Get adjacent vertices from current vertex
        ArrayList<Vertex> adj = (ArrayList<Vertex>) graph.getAdjacentVertices(source.getLabel());

        // Recursive call for all the vertices adjacent to current vertex
        for (Vertex v: adj) {

            // If this vertex has not been visited
            if (!isVisited.get(v)) {

                // Store current node in path[]
                path.add(v);
                allPaths(graph, v, destination, isVisited, path, allPaths);

                // Remove current node from paths
                path.removeLast();
            }
        }

        // Mark the current node as not visited
        isVisited.replace(source, false);
    }


    /**
     * Returns all the paths from a source vertex to another.
     *
     * @param graph The graph instance.
     * @param source The source vertex label.
     * @param destination The destination vertex label.
     *
     * @return ArrayList<Deque<Vertex>>
     */
    public static ArrayList<Deque<Vertex>> getAllPaths(Graph graph, String source, String destination)
    {
        ArrayList<Vertex> list = new ArrayList<>(graph.getVertices());

        // Create is visited with all false
        Map<Vertex, Boolean> isVisited = new HashMap<>();
        for (Vertex v : list) {
            isVisited.put(v, false);
        }

        // Get vertices
        Vertex sourcev = graph.getVertex(source);
        Vertex destinationv = graph.getVertex(destination);

        // Create base path
        Deque<Vertex> path = new ArrayDeque<>();
        path.add(sourcev);

        // Create list for all paths
        ArrayList<Deque<Vertex>> allPaths = new ArrayList<>();

        // Call recursive utility
        allPaths(graph, sourcev, destinationv, isVisited, path, allPaths);

        return allPaths;
    }
}
