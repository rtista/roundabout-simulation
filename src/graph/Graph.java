package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a graph.
 *
 * @param <V> The vertex value.
 * @param <E> The edge value.
 */
public class Graph<V, E> implements GraphInterface<V, E> {

    /**
     * The number of vertices in the graph.
     */
    private int numVert;

    /**
     * The number of edges in the graph.
     */
    private int numEdge;

    /**
     * Whether the edges of the graph are directed.
     */
    private boolean isDirected;

    /**
     * The list of vertices.
     */
    private ArrayList<Vertex<V, E>> listVert;

    /**
     * Empty graph constructor.
     *
     * @param directed Whether the edges are directed.
     */
    public Graph(boolean directed) {
        numVert = 0;
        numEdge = 0;
        isDirected = directed;
        listVert = new ArrayList<>();
    }

    /**
     * Returns the number of vertices in the graph.
     *
     * @return int
     */
    public int numVertices() {
        return this.numVert;
    }

    /**
     * Returns the number of edges in the graph.
     *
     * @return int
     */
    public int numEdges() {
        return this.numEdge;
    }

    /**
     * Returns an iterable with all the vertices from the graph.
     *
     * @return Iterable<Vertex<V, E>>
     */
    public Iterable<Vertex<V, E>> vertices() {
        return listVert;
    }

    /**
     * Returns an iterable with all the edges from the graph.
     *
     * @return Iterable<Edge<V, E>>
     */
    public Iterable<Edge<V, E>> edges() {
        List<Edge<V, E>> e = new ArrayList<>();

        for (Vertex v : this.listVert) {
            e.addAll(v.getOutgoing().values());
        }

        return e;
    }

    /**
     * Returns the edge which connects the given vertices.
     *
     * @param origin The origin vertex.
     * @param destination The destination vertex.
     *
     * @return Edge<V, E>
     */
    public Edge<V, E> getEdge(Vertex<V, E> origin, Vertex<V, E> destination) {

        // Check if both nodes exist in the graph
        if (listVert.contains(origin) && listVert.contains(destination)) {

            // Returns edge or null
            return origin.getOutgoing().get(destination);
        }

        return null;
    }

    /**
     * Returns the endpoint vertices of the given edge.
     *
     * @param e The edge.
     *
     * @return Vertex<V, E>
     */
    public Vertex<V, E>[] endVertices(Edge<V, E> e) {

        for (Vertex v : listVert) {

            if (v.getOutgoing().containsValue(e)) {
                return e.getEndpoints();
            }

        }
        return null;
    }

    /**
     * Returns the vertex on the opposite end of the edge.
     *
     * @param vertex The vertex.
     * @param e The edge.
     *
     * @return Vertex<V, E>
     */
    public Vertex<V, E> opposite(Vertex<V, E> vertex, Edge<V, E> e) {

        if (e.getDestination() == vertex) {
            return e.getOrigin();
        }

        if (e.getOrigin() == vertex) {
            return e.getDestination();
        }

        return null;
    }

    /**
     * Returns the number of outgoing edges for a given vertex.
     *
     * @param v The vertex.
     *
     * @return int
     */
    public int outDegree(Vertex<V, E> v) {

        if (listVert.contains(v)) {
            return v.getOutgoing().size();
        } else {
            return -1;
        }
    }

    /**
     * Returns the number of incoming edges for a given vertex.
     *
     * @param v The vertex.
     *
     * @return int
     */
    public int inDegree(Vertex<V, E> v) {
        if (!listVert.contains(v)) {
            return -1;
        }

        int cont = 0;
        for (Edge<V, E> e : edges()) {
            if (e.getDestination() == v) {
                cont++;
            }
        }
        return cont;
    }

    /**
     * Returns an iterable with the outgoing edges for a given vertex.
     *
     * @param v The vertex.
     *
     * @return Iterable<Edge<V, E>>
     */
    public Iterable<Edge<V, E>> outgoingEdges(Vertex<V, E> v) {

        // Returns null if vertex not contained in graph
        if (!listVert.contains(v)) {
            return null;
        }

        return v.getOutgoing().values();
    }

    /**
     * Returns the incoming edges for a given vertex.
     *
     * @param v The vertex.
     *
     * @return Iterable<Edge<V, E>>
     */
    public Iterable<Edge<V, E>> incomingEdges(Vertex<V, E> v) {
        List<Edge<V, E>> list = new ArrayList<>();

        if (!listVert.contains(v)) {
            return null;
        }

        for (Edge<V, E> e : this.edges()) {
            if (e.getDestination() == v) {
                list.add(e);
            }
        }

        return list;
    }

    /**
     * Inserts a vertex in the graph.
     *
     * @param value The vertex value.
     *
     * @return Vertex<V, E> The inserted vertex.
     */
    public Vertex<V, E> insertVertex(V value) {

        Vertex<V, E> vertex = this.getVertex(value);

        if (vertex == null) {
            Vertex<V, E> newvert = new Vertex<>(numVert, value);
            listVert.add(newvert);
            numVert++;
            return newvert;
        }

        return vertex;
    }

    /**
     * Inserts an edge of the graph.
     *
     * @param origin The edge origin vertex.
     * @param destination The edge destination vertex.
     * @param value The value of the edge.
     * @param weight The weight of the edge.
     *
     * @return Edge<V, E>
     */
    public Edge<V, E> insertEdge(V origin, V destination, E value, double weight) {

        // Validate origin node
        Vertex<V, E> vorig = getVertex(origin);
        if (vorig == null) {
            vorig = insertVertex(origin);
        }

        // Validate destination node
        Vertex<V, E> vdest = getVertex(destination);
        if (vdest == null) {
            vdest = insertVertex(destination);
        }

        // Validate if edge does not exist already
        if (getEdge(vorig, vdest) == null) {

            Edge<V, E> newedge = new Edge<>(value, weight, vorig, vdest);
            vorig.getOutgoing().put(vdest, newedge);
            numEdge++;

            //if graph is not direct insert other edge in the opposite direction
            if (!isDirected) {
                if (getEdge(vdest, vorig) == null) {
                    Edge<V, E> otheredge = new Edge<>(value, weight, vdest, vorig);
                    vdest.getOutgoing().put(vorig, otheredge);
                    numEdge++;
                }
            }

            return newedge;
        }

        return null;
    }

    /**
     * Removes the vertex with the given value.
     *
     * @param value The value of the vertex to remove.
     */
    public void removeVertex(V value) {

        Iterable<Edge<V, E>> list = incomingEdges(getVertex(value));
        for (Edge<V, E> e : list) {
            removeEdge(e);
        }

        list = outgoingEdges(getVertex(value));
        for (Edge<V, E> e : list) {
            removeEdge(e);
        }

        for (Vertex<V, E> vertex : listVert) {
            if (getVertex(value).getKey() < vertex.getKey()) {
                vertex.setKey(vertex.getKey() - 1);
            }
        }
        listVert.remove(getVertex(value));
        this.numVert--;

    }

    /**
     * Removes the given edge.
     *
     * @param edge The edge to remove.
     */
    public void removeEdge(Edge<V, E> edge) {

        Vertex<V, E>[] endpoints = endVertices(edge);

        Vertex<V, E> vorig = endpoints[0];
        Vertex<V, E> vdest = endpoints[1];

        if (vorig != null && vdest != null) {
            if (edge.equals(getEdge(vorig, vdest))) {
                vorig.getOutgoing().remove(vdest);
                numEdge--;
            }
        }
    }

    /**
     * Returns the vertex with the given value.
     *
     * @param value The value of the vertex.
     *
     * @return Vertex<V, E>
     */
    public Vertex<V, E> getVertex(V value) {

        for (Vertex<V, E> vert : this.listVert) {
            if (value.equals(vert.getValue())) {
                return vert;
            }
        }

        return null;
    }

    /**
     * Returns the vertex with the given key.
     *
     * @param vKey The vertex key.
     *
     * @return Vertex<V, E>
     */
    public Vertex<V, E> getVertex(int vKey) {

        if (vKey < listVert.size()) {
            return listVert.get(vKey);
        }

        return null;
    }

    //Returns a clone of the graph
    public Graph<V, E> clone() {

        Graph<V, E> newObject = new Graph<>(this.isDirected);

        newObject.numVert = this.numVert;

        newObject.listVert = new ArrayList<>();
        for (Vertex<V, E> v : this.listVert) {
            newObject.listVert.add(new Vertex<V, E>(v.getKey(), v.getValue()));
        }

        for (Vertex<V, E> v1 : this.listVert) {
            for (Edge<V, E> e : this.outgoingEdges(v1)) {
                if (e != null) {
                    Vertex<V, E> v2 = this.opposite(v1, e);
                    newObject.insertEdge(v1.getValue(), v2.getValue(),
                            e.getValue(), e.getWeight());
                }
            }
        }
        return newObject;
    }

    /* equals implementation
     * @param the other graph to test for equality
     * @return true if both objects represent the same graph
     */
    public boolean equals(Object oth) {

        if (oth == null) return false;

        if (this == oth) return true;

        if (!(oth instanceof Graph<?,?>))
            return false;

        Graph<?, ?> other = (Graph<?, ?>) oth;

        if (numVert != other.numVert || numEdge != other.numEdge)
            return false;

        //graph must have same vertices
        boolean eqvertex;
        for (Vertex<V,E> v1 : this.vertices()){
            eqvertex=false;
            for (Vertex<?,?> v2 : other.vertices())
                if (v1.equals(v2))
                    eqvertex=true;

            if (!eqvertex)
                return false;
        }

        //graph must have same edges
        boolean eqedge;
        for (Edge<V,E> e1 : this.edges()){
            eqedge=false;
            for (Edge<?,?> e2 : other.edges())
                if (e1.equals(e2))
                    eqedge=true;

            if (!eqedge)
                return false;
        }

        return true;

    }

    //string representation
    @Override
    public String toString() {
        String s = "";
        if (numVert == 0) {
            s = "\nGraph not defined!!";
        } else {
            s = "Graph: " + numVert + " vertices, " + numEdge + " edges\n";
            for (Vertex<V, E> vert : listVert) {
                s += vert + "\n";
                if (!vert.getOutgoing().isEmpty()) {
                    for (Map.Entry<Vertex<V, E>, Edge<V, E>> entry : vert.getOutgoing().entrySet()) {
                        s += entry.getValue() + "\n";
                    }
                } else {
                    s += "\n";
                }
            }
        }
        return s;

    }
}
