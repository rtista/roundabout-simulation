package graph;

/**
 *
 * @param <V> The Vertex class.
 * @param <E> The Edge value class.
 */
public class Edge<V, E> implements Comparable {

    /**
     * The edge's value
     */
    private E value;

    /**
     * The edge's weight
     */
    private double weight;

    /**
     * The origin vertex
     */
    private Vertex<V, E> origin;

    /**
     * The destination vertex
     */
    private Vertex<V, E> destination;

    /**
     * Empty edge constructor
     */
    public Edge() {
        this.value = null;
        this.weight = 0.0;
        this.origin = null;
        this.destination = null;
    }

    /**
     * Edge constructor.
     *
     * @param value Edge information.
     * @param weight Edge weight.
     * @param origin The origin vertex.
     * @param destination The destination vertex.
     */
    public Edge(E value, double weight, Vertex<V, E> origin, Vertex<V, E> destination) {
        this.value = value;
        this.weight = weight;
        this.origin = origin;
        this.destination = destination;
    }

    /**
     * Returns the edge value element.
     *
     * @return E
     */
    public E getValue() {
        return this.value;
    }

    /**
     * Returns the edge weight.
     *
     * @return double
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * Returns the origin vertex.
     *
     * @return Vertex<V, E>
     */
    public Vertex<V, E> getOrigin() {
        return this.origin;
    }

    /**
     * Returns the destination vertex.
     *
     * @return Vertex<V, E>
     */
    public Vertex<V, E> getDestination() {
        return this.destination;
    }

    /**
     * Returns the edge vertices.
     *
     * @return Vertex<V, E>[]
     */
    public Vertex<V, E>[] getEndpoints() {

        return new Vertex[]{
                this.origin,
                this.destination
        };
    }

    /**
     * Sets the edge value element.
     *
     * @param value The edge value.
     */
    public void setValue(E value) {
        this.value = value;
    }

    /**
     * Sets the edge weight.
     * @param weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Sets the origin vertex.
     *
     * @param origin The vertex origin.
     */
    public void setOrigin(Vertex<V, E> origin) {
        this.origin = origin;
    }

    /**
     * Set the destination vertex.
     *
     * @param destination The destination vertex.
     */
    public void setDestination(Vertex<V, E> destination) {
        this.destination = destination;
    }

    /**
     * Returns whether two Edge objects are the same.
     *
     * @param otherObj The other Edge object.
     * @return boolean
     */
    @Override
    public boolean equals(Object otherObj) {

        // Check same memory reference
        if (this == otherObj) {
            return true;
        }

        // Check for null or different class
        if (otherObj == null || this.getClass() != otherObj.getClass()) {
            return false;
        }

        // Cast to Edge class
        Edge<V, E> otherEdge = (Edge<V, E>) otherObj;

        // Return true if all properties are the same
        return (this.weight == otherEdge.weight
                && this.value != null
                && otherEdge.value != null
                && this.origin.equals(otherEdge.origin)
                && this.destination.equals(otherEdge.destination)
                && this.value.equals(otherEdge.value));
    }

    /**
     * Compares two Edge objects.
     *
     * @param otherObject The Edge object to compare this to.
     * @return int
     */
    @Override
    public int compareTo(Object otherObject) {

        Edge<V, E> other = (Edge<V, E>) otherObject;
        if (this.weight < other.weight) {
            return -1;
        }
        if (this.weight == other.weight) {
            return 0;
        }
        return 1;
    }

    /**
     * Returns the string representation of the Edge.
     *
     * @return String
     */
    @Override
    public String toString() {
        String st = "";

        if(this.origin != null){
            st = "\t- (" + this.origin.getValue() + ")";
        } else {
            st = "\t- ";
        }

        if (weight != 0) {
            st += weight + " - " + this.destination.getValue();
        } else {
            st += this.destination.getValue();
        }

        return st;
    }

}
