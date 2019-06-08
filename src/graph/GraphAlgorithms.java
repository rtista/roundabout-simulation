package graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class GraphAlgorithms {

    /**
     * Performs breadth-first search of a Graph starting in a Vertex
     *
     * @param g Graph instance
     * @param vInf information of the Vertex that will be the source of the
     * search
     * @return qbfs a queue with the vertices of breadth-first search
     */
    public static <V, E> Deque<V> BreadthFirstSearch(Graph<V, E> g, V vInf) {

        Vertex<V, E> vOrig = g.getVertex(vInf);

        if (vOrig == null) {
            return null;
        }

        Deque<V> qbfs = new LinkedList<>();
        Deque<Vertex<V, E>> qaux = new LinkedList<>();
        boolean[] visited = new boolean[g.numVertices()];  //default initializ.: false

        qbfs.add(vOrig.getValue());
        qaux.add(vOrig);
        int vKey = vOrig.getKey();
        visited[vKey] = true;

        while (!qaux.isEmpty()) {
            vOrig = qaux.remove();
            for (Edge<V, E> edge : g.outgoingEdges(vOrig)) {
                Vertex<V, E> vAdj = g.opposite(vOrig, edge);
                vKey = vAdj.getKey();
                if (!visited[vKey]) {
                    qbfs.add(vAdj.getValue());
                    qaux.add(vAdj);
                    visited[vKey] = true;
                }
            }
        }
        return qbfs;
    }

    /**
     * Performs depth-first search starting in a Vertex
     *
     * @param g Graph instance
     * @param vOrig Vertex of graph g that will be the source of the search
     * @param visited set of discovered vertices
     * @param qdfs queue with vertices of depth-first search
     */
    private static <V, E> void DepthFirstSearch(Graph<V, E> g, Vertex<V, E> vOrig, boolean[] visited, Deque<V> qdfs) {

        ArrayList<Vertex<V, E>> list = (ArrayList) g.vertices();
        visited[list.indexOf(vOrig)] = true;
        for (int i = 0; i < g.numVertices(); i++) {
            if (g.getEdge(vOrig, g.getVertex(i)) != null && visited[i] == false) {
                qdfs.add(g.getVertex(i).getValue());
                DepthFirstSearch(g, g.getVertex(i), visited, qdfs);
            }
        }
    }

    /**
     * @param g Graph instance
     * @param vInf information of the Vertex that will be the+ source of the
     * search
     * @return qdfs a queue with the vertices of depth-first search
     */
    public static <V, E> Deque<V> DepthFirstSearch(Graph<V, E> g, V vInf) {

        ArrayList<Vertex<V, E>> list = (ArrayList) g.vertices();
        int index = list.indexOf(g.getVertex(vInf));
        if (index == -1) {
            return null;
        }

        LinkedList<V> resultQueue = new LinkedList<V>();
        resultQueue.add(vInf);
        boolean[] knownVertices = new boolean[g.numVertices()];
        DepthFirstSearch(g, g.getVertex(vInf), knownVertices, resultQueue);
        return resultQueue;

    }

    /**
     * Method that returns true if a graph has cycles or false if it does not.
     * @param <V>
     * @param <E>
     * @param g
     * @param vOrig
     * @param visited
     * @return
     */
    public static <V, E> boolean hasCycles(Graph<V, E> g, Vertex<V, E> vOrig, boolean[] visited) {

        ArrayList<Vertex<V, E>> vertexList = (ArrayList) g.vertices();

        visited[vertexList.indexOf(vOrig)] = true;

        for (Edge<V, E> edge : g.outgoingEdges(vOrig)) {

            Vertex<V, E> vDest = edge.getDestination();

            if (visited[vertexList.indexOf(vDest)] == true) {

                return true;

            } else {

                if (hasCycles(g, vDest, visited)) {
                    return true;
                }
            }
        }

        visited[vertexList.indexOf(vOrig)] = false;
        return false;
    }

    /**
     * Returns all paths from vOrig to vDest
     *
     * @param g Graph instance
     * @param vOrig Vertex that will be the source of the path
     * @param vDest Vertex that will be the end of the path
     * @param visited set of discovered vertices
     * @param path stack with vertices of the current path (the path is in
     * reverse order)
     * @param paths ArrayList with all the paths (in correct order)
     */
    private static <V, E> void allPaths(Graph<V, E> g, Vertex<V, E> vOrig, Vertex<V, E> vDest,
                                        boolean[] visited, Deque<V> path, ArrayList<Deque<V>> paths) {

        ArrayList<Vertex<V, E>> vertexList = (ArrayList) g.vertices();

        visited[vertexList.indexOf(vOrig)] = true;

        path.add(vOrig.getValue());

        for (Edge<V, E> edge : g.outgoingEdges(vOrig)) {

            Vertex<V, E> v2 = edge.getDestination();

            if (v2.equals(vDest)) {

                path.add(v2.getValue());
                paths.add(new ArrayDeque(path));
                path.removeLast();

            } else {

                if (visited[vertexList.indexOf(v2)] != true) {

                    allPaths(g, v2, vDest, visited, path, paths);

                }
            }

        }

        visited[vertexList.indexOf(vOrig)] = false;
        path.removeLast();
    }

    /**
     * @param <V>
     * @param <E>
     * @param g Graph instance
     * @param voInf information of the Vertex origin
     * @param vdInf information of the Vertex destination
     * @return paths ArrayList with all paths from voInf to vdInf
     */
    public static <V, E> ArrayList<Deque<V>> allPaths(Graph<V, E> g, V voInf, V vdInf) {

        if (g.getVertex(vdInf) == null) {
            return null;
        }
        if (g.getVertex(voInf) == null) {
            return null;
        }

        boolean knownVertices[] = new boolean[g.numVertices()];
        Deque<V> auxStack = new ArrayDeque<>();
        ArrayList<Deque<V>> paths = new ArrayList<>();

        allPaths(g, g.getVertex(voInf), g.getVertex(vdInf), knownVertices, auxStack, paths);

        return paths;
    }

    /**
     * Computes shortest-path distance from a source vertex to all reachable
     * vertices of a graph g with nonnegative edge weights This implementation
     * uses Dijkstra's algorithm
     *
     * @param g Graph instance
     * @param vOrig Vertex that will be the source of the path
     * @param visited set of discovered vertices
     * @param pathKeys minimum path vertices keys
     * @param dist minimum distances
     */
    private static <V, E> void shortestPathLength(Graph<V, E> g, Vertex<V, E> vOrig,
                                                  boolean[] visited, int[] pathKeys, double[] dist) {

        dist[(vOrig).getKey()] = 0;
        int index = vOrig.getKey();
        while (index != -1) {
            visited[index] = true;
            vOrig = g.getVertex(index);
            for (Edge<V, E> edge : g.outgoingEdges(vOrig)) {
                if (!visited[edge.getDestination().getKey()] && dist[edge.getDestination().getKey()] > dist[edge.getOrigin().getKey()] + edge.getWeight()) {
                    dist[(edge.getDestination()).getKey()] = dist[(edge.getOrigin()).getKey()] + edge.getWeight();
                    pathKeys[(edge.getDestination()).getKey()] = (vOrig).getKey();
                }
            }
            index = getVertMinDist(dist, visited);

        }
    }

    public static int getVertMinDist(double[] dist, boolean[] visited) {

        double index = Double.MAX_VALUE;
        int x = -1;
        for (int i = 0; i < visited.length; i++) {
            if (dist[i] < index && !visited[i]) {
                index = dist[i];
                x = i;
            }
        }
        return x;
    }

    /**
     * Extracts from pathKeys the minimum path between voInf and vdInf The path
     * is constructed from the end to the beginning
     *
     * @param g Graph instance
     * @param voInf information of the Vertex origin
     * @param vdInf information of the Vertex destination
     * @param pathKeys minimum path vertices keys
     * @param path stack with the minimum path (correct order)
     */
    private static <V, E> void getPath(Graph<V, E> g, V voInf, V vdInf, int[] pathKeys, Deque<V> path) {

        if (voInf != vdInf) {
            path.push(vdInf);

            Vertex<V, E> vert = g.getVertex(vdInf);
            int vKey = vert.getKey();
            int prevVKey = pathKeys[vKey];
            vert = g.getVertex(prevVKey);
            vdInf = vert.getValue();

            getPath(g, voInf, vdInf, pathKeys, path);
        } else {
            path.push(voInf);
        }
    }

    //shortest-path between voInf and vdInf
    public static <V, E> double shortestPath(Graph<V, E> g, V voInf, V vdInf, Deque<V> shortPath) {

        Vertex<V, E> vOrig = g.getVertex(voInf);
        Vertex<V, E> vDest = g.getVertex(vdInf);

        if (vOrig == null || vDest == null) {
            return 0;
        }

        int nverts = g.numVertices();
        boolean[] visited = new boolean[nverts]; //default value: false
        int[] pathKeys = new int[nverts];
        double[] dist = new double[nverts];

        for (int i = 0; i < nverts; i++) {
            dist[i] = Double.MAX_VALUE;
            pathKeys[i] = -1;
        }

        shortestPathLength(g, vOrig, visited, pathKeys, dist);

        double lengthPath = dist[vDest.getKey()];

        if (lengthPath != Double.MAX_VALUE) {
            getPath(g, voInf, vdInf, pathKeys, shortPath);
            return lengthPath;
        }
        return 0;
    }

    /**
     * Reverses the path
     *
     * @param path stack with path
     */
    public static <V, E> Deque<V> revPath(Deque<V> path) {

        Deque<V> pathrev = new ArrayDeque<>();

        while (!path.isEmpty()) {
            pathrev.push(path.pop());
        }

        return pathrev;
    }

    //O(V.E) - total
    //O(V)
    public static <V, E> Deque<V> topologicalSort(Graph<V, E> g) {
        boolean visited[] = new boolean[g.numVertices()];
        Deque<V> topsort = new LinkedList<>();
        for (int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }

        for (Vertex<V, E> v : g.vertices()) {
            if (visited[v.getKey()] == false) {
                topologSort(g, v, visited, topsort);
            }
        }
        return topsort;
    }

    //O(E)
    private static <V, E> void topologSort(Graph<V, E> g, Vertex<V, E> vOrig, boolean[] visited, Deque<V> toposort) {
        visited[vOrig.getKey()] = true;
        for (Edge e : g.outgoingEdges(vOrig)) {
            if (!visited[e.getDestination().getKey()]) {
                topologSort(g, e.getDestination(), visited, toposort);
            }
        }
        toposort.push(vOrig.getValue());
    }
}
