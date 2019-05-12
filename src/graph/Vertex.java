package graph;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @param <V> The vertex value class.
 * @param <E> The edge class.
 */
public class Vertex<V, E> {

    /**
     * The vertex key number.
     */
    private int key;

    /**
     * The vertex information.
     */
    private V value;

    /**
     * Each vertex maps the vertices to which it connects
     * with the edge that connects them.
     */
    private Map<Vertex<V, E>, Edge<V, E>> outVerts; //adjacent vertices

    /**
     * Empty vertex constructor
     */
    public Vertex() {
    }

    /**
     * Vertex constructor.
     *
     * @param k The vertex key number.
     * @param value The vertex information.
     */
    public Vertex(int k, V value) {
        this.key = k;
        this.value = value;
        this.outVerts = new LinkedHashMap<>();
    }

    /**
     * Returns the vertex key number.
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
     * Sets the vertex key.
     *
     * @param key The vertex key.
     */
    public void setKey(int key) {

        this.key = key;
    }

    /**
     * Sets the vertex value.
     *
     * @param value The vertex value.
     */
    public void setValue(V value) {
        this.value = value;
    }

    /**
     * Returns the vertex outgoing vertices.
     *
     * @return Map<Vertex<V, E>, Edge<V, E>>
     */
    public Map<Vertex<V, E>, Edge<V, E>> getOutgoing() {
        return this.outVerts;
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
        Vertex<V, E> otherVertex = (Vertex<V, E>) otherObj;

        // Check if same value or key
        return (this.key == otherVertex.key
                && this.value != null && otherVertex.value != null
                && this.value.equals(otherVertex.value));
    }

    /**
     * Returns a vertex the same as this.
     *
     * @return Vertex<V, E>
     */
    @Override
    public Vertex<V, E> clone() {

        Vertex<V, E> newVertex = new Vertex<>();

        newVertex.key = key;
        newVertex.value = value;

        Map<Vertex<V, E>, Edge<V, E>> newMap = new LinkedHashMap<>();

        Iterator<Map.Entry<Vertex<V, E>, Edge<V, E>>> itmap;
        itmap = this.outVerts.entrySet().iterator();
        while (itmap.hasNext()) {
            Map.Entry<Vertex<V, E>, Edge<V, E>> entry = itmap.next();
            newMap.put(entry.getKey(), entry.getValue());
        }
        newVertex.outVerts = newMap;

        return newVertex;
    }

    /**
     * Returns vertex information and key in string.
     *
     * @return String
     */
    @Override
    public String toString() {
        return this.value + " (" + this.key + "): ";
    }
}

