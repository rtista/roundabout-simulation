
package graph;

/**
 *
 * @param <V>
 * @param <E>
 */
public interface GraphInterface<V,E> {

    // Returns the number of vertices of the graph
    int numVertices();

    // Returns all the vertices of the graph as an iterable collection
    Iterable<Vertex<V,E>> vertices();

    // Returns the number of edges of the graph
    int numEdges();

    // Returns all the edges of the graph as an iterable collection
    Iterable<Edge<V,E>> edges();

    /* Returns the edge from vorig to vdest, or null if vertices are not adjacent
     * @param vorig
     * @param vdest
     * @return the edge or null if vertices are not adjacent or don't exist
     */
    Edge<V,E> getEdge(Vertex<V,E> vorig, Vertex<V,E> vdest);

    /* Returns the vertices of edge e as an array of length two
     * If the graph is directed, the first vertex is the origin, and
     * the second is the destination.  If the graph is undirected, the
     * order is arbitrary.
     * @param e
     * @return array of two vertices or null if edge doesn't exist
     */
    Vertex<V,E>[] endVertices(Edge<V,E> e);

    /* Returns the vertex that is opposite vertex v on edge e.
     * @param v
     * @param e
     * @return opposite vertex, or null if vertex or edge don't exist
     */
    Vertex<V,E> opposite(Vertex<V,E> v, Edge<V,E> e);

    /**
     * Returns the number of edges leaving vertex v
     * For an undirected graph, this is the same result returned by inDegree
     * @param v
     * @return number of edges leaving vertex v, -1 if vertex doesn't exist
     */
    int outDegree(Vertex<V,E> v) ;

    /**
     * Returns the number of edges for which vertex v is the destination
     * For an undirected graph, this is the same result returned by outDegree
     * @param v
     * @return number of edges leaving vertex v, -1 if vertex doesn't exist
     */
    int inDegree(Vertex<V,E> v) ;

    /* Returns an iterable collection of edges for which vertex v is the origin
     * for an undirected graph, this is the same result returned by incomingEdges
     * @param v
     * @return iterable collection of edges, null if vertex doesn't exist
     */
    Iterable<Edge<V,E>> outgoingEdges (Vertex<V,E> v);

    /* Returns an iterable collection of edges for which vertex v is the destination
     * For an undirected graph this is the same result as returned by incomingEdges
     * @param v
     * @return iterable collection of edges reaching vertex, null if vertex doesn't exist
     */
    Iterable<Edge<V,E>> incomingEdges(Vertex<V,E> v);

    /**
     * Inserts and returns a new vertex with some specific comparable type
     * @param vInf The vertex value.
     * @return a new vertex
     */
    Vertex<V,E> insertVertex(V vInf);

    /* Adds and returns a new edge between vertices u and v, with some
     * specific comparable type. If vertices u, v don't exist in the graph they
     * are inserted
     * @param vorigInf Information of vertex source
     * @param vdestInf Information of vertex destination
     * @param eInf edge information
     * @param eWeight edge weight
     * @return new edge, or null if an edge already exists between the two verts.
     */
    Edge<V,E> insertEdge(V vorigInf, V vdestInf, E eInf, double eWeight);


    /* Removes a vertex and all its incident edges from the graph
     * @param vInf Information of vertex source
     * * @param weight The vertex weight.
     */
    void removeVertex(V vInf);

    /* Removes the edge between with source vertex vorigInf and destination vertex
     * vdestInf
     * @param vorigInf Information of vertex source
     * @param vdestInf Information of vertex destination
     */
    void removeEdge(Edge<V,E> e);

}


