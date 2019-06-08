package domain.roundabout;


import domain.vehicles.Vehicle;
import graph.Edge;
import graph.Graph;
import graph.GraphAlgorithms;
import graph.Vertex;

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
    private Graph<AtomicReference, Double> graph;

    /**
     * The exit vertices of the roundabout.
     */
    private HashMap<Integer, Edge<AtomicReference, Double>> exitEdges;

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
        this.exitEdges = new HashMap<>();
        this.graph = this.buildRoundaboutGraph();
    }

    /**
     * Build the roundabout graph.
     *
     * @return Graph
     */
    private Graph<AtomicReference, Double> buildRoundaboutGraph() {

        // Check if graph is possible
        if (this.radius / LANE_WIDTH < this.nLanes) {
            throw new InvalidParameterException("Radius too small for so many lanes!");
        }

        // Create directed graph
        Graph<AtomicReference, Double> graph = new Graph<>(true);

        // For each of the lanes to be created
        for (int i = 0; i < this.nLanes; i++) {

            // Calculate number of nodes required to represent the lane
            double laneRadius = this.radius - (i * LANE_WIDTH) - (LANE_WIDTH / 2);
            double perimeter = (2 * Math.PI * laneRadius);
            int nodes = (int) Math.round(perimeter * VERTEX_PER_METER_RATIO);

            // First node
            Vertex<AtomicReference, Double> origin = graph.insertVertex(new AtomicReference(null));
            Vertex<AtomicReference, Double> v = origin;

            // Create nodes for lane in graph
            for (int j = 0; j < nodes - 1; j++) {

                /*
                 * Build oriented edge between vertices.
                 * Edge weight is 0 because there's no change between roundabout lane
                 */
                Edge e = graph.insertEdge( v.getValue(), new AtomicReference(null),
                        10.0, 0);

                // Set destination to origin
                v = e.getDestination();
            }

            // Connect last vertex to first vertex for each lane
            graph.insertEdge( v.getValue(), origin.getValue(),10.0,0);

            // First lane in the roundabout
            if (i == 0) {

                // Create roundabout exits
                for (int j = 0; j < this.nExits; j++) {

                    // Calculate exit vertex key
                    int exitVertexKey = i * (nodes / this.nExits);

                    // Get vertex by key
                    Vertex<AtomicReference, Double> exitVertex = graph.getVertex(exitVertexKey);

                    // Exit edges weight is -2
                    this.exitEdges.put(j, graph.insertEdge(exitVertex.getValue(),
                            new AtomicReference(null),10.0,-2));
                }
            }
        }

        return graph;
    }

    /**
     * Returns a list with the vehicle route inside the roundabout graph.
     *
     * @param v The vehicle.
     * @return ArrayDeque<AtomicReference>
     */
    public Deque<AtomicReference> getVehicleShortestRoute(Vehicle v) {

        /*
        // Get starting vertex
        Vertex<AtomicReference, Double> origin = this.graph.getVertex(v.getSource());

        // Get exit node
        Vertex<AtomicReference, Double> destination = this.exitEdges.get(v.getDestination()).getDestination();
        */

        // Get starting vertex
        AtomicReference origin = this.graph.getVertex(v.getSource()).getValue();

        // Get exit node
        AtomicReference destination = this.exitEdges.get(v.getDestination()).getDestination().getValue();

        // Get path
        ArrayList<Deque<AtomicReference>> route = GraphAlgorithms.allPaths(this.graph, origin, destination);

        return route.get(0);
    }

    @Override
    public String toString() {
        return this.graph.toString();
    }
}
