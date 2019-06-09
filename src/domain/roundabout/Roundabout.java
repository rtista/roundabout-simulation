package domain.roundabout;


import domain.vehicles.Vehicle;
import graphv2.Graph;
import graphv2.GraphAlgorithms;
import graphv2.Vertex;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Roundabout class.
 */
public class Roundabout {

    /**
     * Roundabout traffic lane width in meters.
     */
    public static final double LANE_WIDTH = 3;

    /**
     * Number of nodes per meter of perimeter of the roundabout lane.
     */
    public static final double VERTEX_PER_METER_RATIO = 0.25;

    /**
     * The roundabout radius.
     */
    private final double radius;

    /**
     * The number of lanes.
     */
    private final int nLanes;

    /**
     * The number of lanes.
     */
    private final int nExits;

    /**
     * The roundabout graph
     */
    private Graph<AtomicReference> graph;

    /**
     * The exit vertices of the roundabout.
     */
    private HashMap<Integer, Vertex<AtomicReference>> exitNodes;

    /**
     * Roundabout empty constructor.
     *
     * @param radius The radius of the roundabout in meters.
     * @param nLanes Number of lanes in the roundabout.
     */
    public Roundabout(double radius, int nLanes, int nExits) {

        this.radius = radius;
        this.nLanes = nLanes;
        this.nExits = nExits;
        this.exitNodes = new HashMap<>();
        this.graph = this.buildRoundaboutGraph();
    }

    /**
     * Build the roundabout graph.
     *
     * @return Graph
     */
    private Graph<AtomicReference> buildRoundaboutGraph() {

        // Check if graph is possible
        if (this.radius / LANE_WIDTH < this.nLanes) {
            throw new InvalidParameterException("Radius too small for so many lanes!");
        }

        // Create directed graph
        Graph<AtomicReference> graph = new Graph<>(true);

        // For each of the lanes to be created
        for (int i = 0; i < this.nLanes; i++) {

            // Calculate number of nodes required to represent the lane
            double laneRadius = this.radius - (i * LANE_WIDTH) - (LANE_WIDTH / 2);
            double perimeter = (2 * Math.PI * laneRadius);
            int nodes = (int) Math.round(perimeter * VERTEX_PER_METER_RATIO);

            // First node
            Vertex<AtomicReference> origin = graph.addVertex(new AtomicReference(null));
            Vertex<AtomicReference> curr = origin;

            // Create nodes for lane in graph
            for (int j = 0 ; j < nodes - 1; j++) {

                // Create next vertex
                Vertex<AtomicReference> destination = graph.addVertex(new AtomicReference(null));

                // Build oriented edge between vertices.
                graph.addEdge(curr.getKey(), destination.getKey());

                // Set destination to origin
                curr = destination;
            }

            // Connect last vertex to first vertex for each lane
            graph.addEdge(curr.getKey(), origin.getKey());

            // Outer lane in the roundabout
            if (i == 0) {

                // Create roundabout exits
                for (int j = 0; j < this.nExits; j++) {

                    // Calculate exit vertex key
                    int exitVertexKey = j * (nodes / this.nExits);

                    // Get vertex by key
                    Vertex<AtomicReference> exitVertex = graph.getVertex(exitVertexKey);

                    // Place the node mapped to the exit
                    this.exitNodes.put(j, exitVertex);
                }
            }
        }

        return graph;
    }

    /**
     * Returns a list with the vehicle route inside the roundabout graph.
     *
     * @param v The vehicle.
     *
     * @return Deque<AtomicReference>
     */
    public Deque<AtomicReference> getVehicleShortestRoute(Vehicle v) {

        // Get source and destination vertex
        int origin = this.exitNodes.get(v.getSource()).getKey();
        int destination = this.exitNodes.get(v.getDestination()).getKey();

        // Get all paths from source to destination
        ArrayList<Deque<Vertex>> route = GraphAlgorithms.getAllPaths(this.graph, origin, destination);

        // Convert into Deque of Vertex value
        Deque<AtomicReference> shortestRoute = new ArrayDeque<>();

        /*
        Add all elements from first route
        TODO: Get shortest route, not first. Shortest route should
         be the best according to traffic rules
         */
        StringBuilder builder = new StringBuilder().append("Vehicle Route: Start -> ");
        for(Vertex vertex: route.get(0)) {

            shortestRoute.add((AtomicReference) vertex.getValue());

            builder.append("(").append(vertex.getKey()).append(") -> ");
        }

        builder.append("End");

        System.out.println(builder.toString());

        // Return first path
        return shortestRoute;
    }

    @Override
    public String toString() {
        return this.graph.toString();
    }
}
