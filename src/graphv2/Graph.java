package graphv2;


import java.util.*;
import java.util.stream.Collectors;

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
    protected boolean isDirected;

    /**
     * The vertices keys mapped to the vertex itself.
     */
    protected Map<Integer, Vertex<V>> vertices;

    /**
     * Graph is represented as adjacency list.
     */
    protected Map<Vertex<V>, List<Vertex<V>>> adjacencyMap;

    /**
     * Graph empty constructor.
     */
    public Graph() {

        this.isDirected = false;
        this.vertices = new HashMap<>();
        this.adjacencyMap = new HashMap<>();
    }

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
     * @param key The vertex key.
     *
     * @return List<Vertex<V>
     */
    public List<Vertex<V>> getAdjacentVertices(int key) {

        // Return adjacent vertices from vertex
        return this.adjacencyMap.get(this.vertices.get(key));
    }

    /**
     * Returns the vertex with the given key.
     *
     * @param key The vertex key.
     *
     * @return Vertex<V>
     */
    public Vertex<V> getVertex(int key) {

        return this.vertices.get(key);
    }

    /**
     * Returns a collection of the vertices with the given weight.
     *
     * @param weight The vertex weight.
     *
     * @return Collection<WeighedVertex<V>>
     */
    public Collection<Vertex<V>> getVertices(int weight) {

        return this.getVertices().stream().filter(v -> v.getWeight() == weight).collect(Collectors.toList());
    }

    /**
     * Add a vertex to the graph.
     *
     * @param vertex The vertex to add.
     */
    public Vertex<V> addVertex(Vertex<V> vertex) {

        vertex.setKey(this.vertices.size());

        // Place vertex into vertices map
        this.vertices.putIfAbsent(vertex.getKey(), vertex);

        // Place vertex into adjacency map
        this.adjacencyMap.putIfAbsent(vertex, new ArrayList<>());

        return vertex;
    }

    /**
     * Remove a vertex from the graph.
     *
     * @param key The vertex key.
     */
    public Vertex<V> removeVertex(int key) {

        // Remove vertex from vertices map
        Vertex<V> v = this.vertices.remove(key);

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
     * @param key1 The source vertex key.
     * @param key2 The destination vertex key.
     */
    public void addEdge(int key1, int key2) {

        // Get vertices by label
        Vertex<V> v1 = this.vertices.get(key1);
        Vertex<V> v2 = this.vertices.get(key2);

        // If the graph is not directed then create bidirectional edge
        if (!this.isDirected) {
            this.adjacencyMap.get(v2).add(v1);
        }

        // Add edge from vertex 1 to vertex 2
        this.adjacencyMap.get(v1).add(v2);
    }
}
