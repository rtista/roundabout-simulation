package domain.roundabout;

import domain.vehicles.Vehicle;
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
    private static final Factory instance = new Factory();

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
    public static Factory getInstance() {

        return instance;
    }

    /**
     * Returns a roundabout built with the given parameters.
     *
     * @param radius   The roundabout outer lane radius.
     * @param nLanes   The number of lanes in the roundabout.
     * @param nEntries The number of entries in the roundabout.
     * @param nExits   The number of exits in the roundabout.
     * @return Roundabout
     */
    public Roundabout buildRoundabout(double radius, int nLanes, int nEntries, int nExits) {

        // Check if graph is possible
        if (radius / LANE_WIDTH < nLanes) {
            throw new InvalidParameterException("Radius too small for so many lanes!");
        }

        // Create directed graph and exit and entry node maps
        Graph<AtomicReference> graph = new Graph<>(true);
        Map<Integer, Vertex<AtomicReference>> exitNodes = new HashMap<>();
        Map<Integer, Vertex<AtomicReference>> entryNodes = new HashMap<>();
        Map<Integer, Double> lanePerimeterMap = new HashMap<>();

        // For each of the lanes to be created
        for (int i = 0; i < nLanes; i++) {

            // Counters for created entries and exits
            int entriesCreated = 0;
            int exitsCreated = 0;

            // Calculate number of nodes required to represent the lane
            double laneRadius = radius - (i * LANE_WIDTH) - (LANE_WIDTH / 2);
            double perimeter = (2 * Math.PI * laneRadius);
            int nodes = (int) (perimeter * VERTEX_PER_METER_RATIO);

            // Save perimeter on map
            lanePerimeterMap.put(i, perimeter);

            // Check if graph is possible
            if (i == 0 && nodes < (nEntries + nExits)) {
                throw new InvalidParameterException("Too many entries and exits for so little nodes!");
            }

            /*
             * Create and add first node
             * The weight is equal to the roundabout lane (0 is the outer lane)
             */
            Vertex<AtomicReference> origin = new Vertex<>(0, new AtomicReference(null), i);
            Vertex<AtomicReference> curr = graph.addVertex(origin);

            // Create nodes for lane in graph
            for (int j = 0; j < nodes - 1; j++) {

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

                // Outer Lane - Create entries and exits
                if (i == 0) {

                    // Place entry or exit
                    if (j % (nodes / (nEntries + nExits)) == 0) {

                        // Create entry
                        if (entriesCreated < nEntries) {

                            // Add entry vertex - Weight for entry nodes is -1
                            Vertex<AtomicReference> entryVertex = graph.addVertex(
                                    new Vertex<>(
                                            0,
                                            new AtomicReference(new ConcurrentLinkedQueue<Vehicle>()),
                                            -1
                                    )
                            );

                            // Create edge from entry node to roundabout node
                            graph.addEdge(entryVertex.getKey(), curr.getKey());

                            // Place the node mapped to the entry
                            entryNodes.put(entriesCreated + 1, entryVertex);

                            entriesCreated++;

                            // Create exit
                        } else if (exitsCreated < nExits) {

                            // Add exit vertex - Weight for exit nodes is -2
                            Vertex<AtomicReference> exitVertex = graph.addVertex(
                                    new Vertex<>(0, new AtomicReference<>(null), -2)
                            );

                            // Create edge from roundabout node to exit node
                            graph.addEdge(curr.getKey(), exitVertex.getKey());

                            // Place the node mapped to the exit
                            exitNodes.put(exitsCreated + 1, exitVertex);

                            exitsCreated++;
                        }
                    }
                }
            }

            // Connect last vertex to first vertex for each lane
            graph.addEdge(curr.getKey(), origin.getKey());
        }

        // Create links between lanes
        for (int i = 0; i < nLanes - 1; i++) {

            System.out.println("Lane " + i + " vertices: " + graph.getVertices(i).size());
            System.out.println("Lane " + (i + 1) + " vertices: " + graph.getVertices(i + 1).size());

            // Get vertices from outer lane and inner lane
            ArrayList<Vertex<AtomicReference>> outer = new ArrayList<>(graph.getVertices(i));
            ArrayList<Vertex<AtomicReference>> inner = new ArrayList<>(graph.getVertices(i + 1));

            // Calculate vertex count difference
            int dif = outer.size() - inner.size();

            // Keep track of inner vertex index as it is different
            int innerIndex = 0;

            // Iterate outer lane vertices and create edges
            for (int j = 0; j < outer.size(); j++) {

                // Keep track of next outer vertex index as it is different
                int nextOuterIndex = j + 1;

                // When end is reached the next outer node is the first node
                if (nextOuterIndex == outer.size()) {
                    nextOuterIndex = 0;
                }

                // When end is reached the next outer node is the first node
                if (innerIndex == inner.size()) {
                    innerIndex = 0;
                }

                // Ignore every dif nth node
                if (j % dif != 0) {

                    // Get current and next outer vertices and inner lane vertex
                    Vertex outerVert = outer.get(j);
                    Vertex nextOuterVert = outer.get(nextOuterIndex);
                    Vertex innerVert = inner.get(innerIndex);

                    // Add edge from outer node to inner node
                    graph.addEdge(outerVert.getKey(), innerVert.getKey());

                    // Add edge from inner node to next outer node
                    graph.addEdge(innerVert.getKey(), nextOuterVert.getKey());

                    // Increment innerIndex
                    innerIndex++;
                }
            }
        }

        return new Roundabout(graph, entryNodes, exitNodes, lanePerimeterMap);
    }
}
