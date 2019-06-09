package graphv2;

import java.util.*;

/**
 * Represents a graph as an adjacency list.
 *
 * Each vertex will be a key on the Adjacency Map and
 * will be mapped to a list of all its adjacent vertices.
 *
 * @param <V> The vertex value class.
 */
public class Graph<V> {

    /**
     * Whether the graph is or not directed.
     */
    private boolean isDirected;

    /**
     * The vertices label mapped to the vertex itself.
     */
    private Map<String, Vertex<V>> vertices;

    /**
     * Graph is represented as adjacency list.
     */
    private Map<Vertex<V>, List<Vertex<V>>> adjacencyMap;

    /**
     * Graph empty constructor.
     *
     * @param isDirected Whether the graph is or not directed.
     */
    public Graph(boolean isDirected) {

        this.isDirected = isDirected;
        this.vertices = new HashMap<>();
        this.adjacencyMap = new HashMap<>();
    }

    /**
     * Returns the vertices list.
     *
     * @return Collection<Vertex<V>>
     */
    public Collection<Vertex<V>> getVertices() {

        return this.vertices.values();
    }

    /**
     * Returns the adjacent vertices of the given vertex.
     *
     * @param label The vertex label.
     *
     * @return List<Vertex<V>
     */
    public List<Vertex<V>> getAdjacentVertices(String label) {

        // Return adjacent vertices from vertex
        return this.adjacencyMap.get(this.vertices.get(label));
    }

    /**
     * Returns the vertex with the given label.
     *
     * @param label The vertex label.
     *
     * @return Vertex<V>
     */
    public Vertex<V> getVertex(String label) {

        return this.vertices.get(label);
    }

    /**
     * Add a vertex to the graph.
     *
     * @param label The vertex label.
     * @param value The vertex value.
     */
    public Vertex<V> addVertex(String label, V value) {

        Vertex<V> v = new Vertex<>(label, value);

        // Place vertex into vertices map
        this.vertices.putIfAbsent(label, v);

        // Place vertex into adjacency map
        this.adjacencyMap.putIfAbsent(v, new ArrayList<>());

        return v;
    }

    /**
     * Remove a vertex from the graph.
     *
     * @param label The vertex label.
     */
    public Vertex<V> removeVertex(String label) {

        // Remove vertex from vertices map
        Vertex<V> v = this.vertices.remove(label);

        // Remove vertex from adjacency map
        this.adjacencyMap.remove(v);

        // Iterate all graph vertices adjacency map and remove vertex
        for (List<Vertex<V>> adjList: this.adjacencyMap.values()) {

            // Remove vertex
            adjList.remove(v);
        }

        return v;
    }

    /**
     * Creates an edge from the v1 to the v2 vertex.
     *
     * @param label1 The source vertex label.
     * @param label2 The destination vertex label.
     */
    public void addEdge(String label1, String label2) {

        // Get vertices by label
        Vertex<V> v1 = this.vertices.get(label1);
        Vertex<V> v2 = this.vertices.get(label2);

        // If the graph is not directed then create bidirectional edge
        if (!this.isDirected) {
            this.adjacencyMap.get(v2).add(v1);
        }

        // Add edge from vertex 1 to vertex 2
        this.adjacencyMap.get(v1).add(v2);
    }
}
