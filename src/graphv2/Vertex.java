package graphv2;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The Vertex class represents a
 *
 * @param <V> The vertex value class.
 */
public class Vertex<V> {

    /**
     * The vertex label.
     */
    private String label;

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
     * @param label The vertex label.
     * @param value The vertex value.
     */
    public Vertex(String label, V value) {
        this.label = label;
        this.value = value;
    }

    /**
     * Returns the vertex label.
     *
     * @return String
     */
    public String getLabel() {

        return this.label;
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
        return (this.label == otherVertex.label);
    }

    /**
     * Returns vertex string representation.
     *
     * @return String
     */
    @Override
    public String toString() {
        return "(" + this.label + ")";
    }
}

