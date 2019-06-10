package domain.roundabout;

import graphv2.Graph;
import graphv2.Vertex;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public class Factory {

    /**
     * Roundabout traffic lane width in meters.
     */
    public static final double LANE_WIDTH = 3;

    /**
     * Number of nodes per meter of perimeter of the roundabout lane.
     */
    public static final double VERTEX_PER_METER_RATIO = 0.25;

    /**
     *
     */
    private Factory instance = null;

    /**
     * Roundabout empty private constructor.
     */
    private Factory() {
    }

    /**
     * Returns the singleton factory instance.
     *
     * @return Factory
     */
    public Factory getInstance() {

        // If not created then create it
        if (this.instance == null) {
            this.instance = new Factory();
        }

        return this.instance;
    }

    /**
     * Returns a roundabout built with the given parameters.
     *
     * @param radius The roundabout outer lane radius.
     * @param nLanes The number of lanes in the roundabout.
     * @param nEntries The number of entries in the roundabout.
     * @param nExits The number of exits in the roundabout.
     *
     * @return Roundabout
     */
    public Roundabout buildRoundabout(double radius, int nLanes, int nEntries, int nExits) {

        Graph<AtomicReference> graph = buildRoundaboutGraph(radius, nLanes);
        Map<Integer, Vertex<AtomicReference>> exitNodes = addExits(graph, nExits);
        Map<Integer, Vertex<AtomicReference>> entryNodes = addEntries(graph, nEntries);

        return new Roundabout(graph, entryNodes, exitNodes);
    }

    /**
     * Build the roundabout graph.
     *
     * @return Graph
     */
    private Graph<AtomicReference> buildRoundaboutGraph(double radius, int nLanes) {

        // Check if graph is possible
        if (radius / LANE_WIDTH < nLanes) {
            throw new InvalidParameterException("Radius too small for so many lanes!");
        }

        // Create directed graph
        Graph<AtomicReference> graph = new Graph<>(true);

        // For each of the lanes to be created
        for (int i = 0; i < nLanes; i++) {

            // Calculate number of nodes required to represent the lane
            double laneRadius = radius - (i * LANE_WIDTH) - (LANE_WIDTH / 2);
            double perimeter = (2 * Math.PI * laneRadius);
            int nodes = (int) Math.round(perimeter * VERTEX_PER_METER_RATIO);

            /*
             * Create and add first node
             * The weight is equal to the roundabout lane (0 is the outer lane)
             */
            Vertex<AtomicReference> origin = new Vertex<>(0, new AtomicReference(null), i);
            Vertex<AtomicReference> curr = graph.addVertex(origin);

            // Create nodes for lane in graph
            for (int j = 0 ; j < nodes - 1; j++) {

                /*
                 * Create next vertex
                 * The weight is equal to the roundabout lane (0 is the outer lane)
                 */
                Vertex<AtomicReference> destination = new Vertex<>(0, new AtomicReference(null), i);
                graph.addVertex(destination);

                // Build oriented edge between vertices.
                graph.addEdge(curr.getKey(), destination.getKey());

                // Set destination to origin
                curr = destination;
            }

            // Connect last vertex to first vertex for each lane
            graph.addEdge(curr.getKey(), origin.getKey());
        }

        return graph;
    }

    /**
     * Creates entries in the roundabout graph.
     *
     * @param graph The graph to add entries to.
     * @param nEntries The number of entries to add.
     *
     * @return Map<Integer, WeighedVertex<AtomicReference>>
     */
    private Map<Integer, Vertex<AtomicReference>> addEntries(Graph<AtomicReference> graph, int nEntries) {

        // Entry nodes map
        Map<Integer, Vertex<AtomicReference>> entryNodes = new HashMap<>();

        // Get all outer lane vertices
        ArrayList<Vertex<AtomicReference>> list = new ArrayList<>(graph.getVertices(0));

        // Iterate outer lane vertices
        for (int i = 0; i < nEntries; i++) {

            // Calculate exit vertex key
            int entryVertexKey = i * (list.size() / nEntries);

            // Check if this node does not connect to an exit
            for (Vertex v : graph.getAdjacentVertices(entryVertexKey)) {

                // If one of the adjacent node is an exit, use the next node
                if (v.getWeight() == -2) {
                    entryVertexKey++;
                }
            }

            // Add exit vertex - Weight for entry nodes is -1
            Vertex<AtomicReference> entryVertex = graph.addVertex(
                    new Vertex<>(
                            0,
                            new AtomicReference(new ConcurrentLinkedQueue<>()),
                            -1
                    )
            );

            // Create edge from roundabout node to exit node
            graph.addEdge(entryVertexKey, entryVertex.getKey());

            // Place the node mapped to the exit
            entryNodes.put(entryVertex.getKey(), entryVertex);
        }

        return entryNodes;
    }

    /**
     * Creates exits in the roundabout graph and returns the exit nodes map.
     *
     * @param graph The graph to add exits to.
     * @param nExits The number of exits to add.
     *
     * @return Map<Integer, WeighedVertex<AtomicReference>>
     */
    private Map<Integer, Vertex<AtomicReference>> addExits(Graph<AtomicReference> graph, int nExits) {

        // Exit nodes map
        Map<Integer, Vertex<AtomicReference>> exitNodes = new HashMap<>();

        // Get all outer lane vertices
        ArrayList<Vertex<AtomicReference>> list = new ArrayList<>(graph.getVertices(0));

        // Iterate outer lane vertices
        for (int i = 0; i < nExits; i++) {

            // Calculate exit vertex key
            int exitVertexKey = i * (list.size() / nExits);

            // Add exit vertex - Weight for exit nodes is -2
            Vertex<AtomicReference> exitVertex = graph.addVertex(
                    new Vertex<>(0, new AtomicReference(null), -2)
            );

            // Create edge from roundabout node to exit node
            graph.addEdge(exitVertexKey, exitVertex.getKey());

            // Place the node mapped to the exit
            exitNodes.put(exitVertex.getKey(), exitVertex);
        }

        return exitNodes;
    }
}
