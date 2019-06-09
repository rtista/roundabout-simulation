package graphv2;

/**
 * The Vertex class represents a node on a graph.
 *
 * @param <V> The vertex value class.
 */
public class Vertex<V> {

    /**
     * The vertex key.
     */
    private int key;

    /**
     * The vertex value.
     */
    private V value;

    /**
     * Empty vertex constructor
     */
    public Vertex() {
    }

    /**
     * Vertex constructor.
     *
     * @param key The vertex key.
     * @param value The vertex value.
     */
    public Vertex(int key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns the vertex key.
     *
     * @return int
     */
    public int getKey() {

        return this.key;
    }

    /**
     * Returns the value of the vertex.
     *
     * @return V The vertex value.
     */
    public V getValue() {

        return this.value;
    }

    /**
     * Returns whether two vertex objects are equal.
     *
     * @param otherObj The other vertex object.
     * @return boolean
     */
    @Override
    public boolean equals(Object otherObj) {

        // Check if same reference
        if (this == otherObj) {
            return true;
        }

        // Check if null or different class
        if (otherObj == null || this.getClass() != otherObj.getClass()) {
            return false;
        }

        // Cast to Vertex
        Vertex<V> otherVertex = (Vertex<V>) otherObj;

        // Check if same value or key
        return (this.key == otherVertex.key);
    }

    /**
     * Returns vertex string representation.
     *
     * @return String
     */
    @Override
    public String toString() {
        return "(" + this.key + ")";
    }
}

