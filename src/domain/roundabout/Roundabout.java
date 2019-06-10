package domain.roundabout;


import domain.vehicles.Vehicle;
import graphv2.Graph;
import graphv2.GraphAlgorithms;
import graphv2.Vertex;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Roundabout class.
 */
public class Roundabout {

    /**
     * The roundabout graph
     */
    private Graph<AtomicReference> graph;

    /**
     * The exit vertices of the roundabout.
     */
    private Map<Integer, Vertex<AtomicReference>> entryNodes;

    /**
     * The exit vertices of the roundabout.
     */
    private Map<Integer, Vertex<AtomicReference>> exitNodes;

    /**
     * Roundabout constructor.
     *
     * @param graph The roundabout graph.
     * @param entryNodes The entry nodes map.
     * @param exitNodes The exit nodes map.
     */
    public Roundabout(Graph<AtomicReference> graph,
                      Map<Integer, Vertex<AtomicReference>> entryNodes,
                      Map<Integer, Vertex<AtomicReference>> exitNodes) {

        this.entryNodes = entryNodes;
        this.exitNodes = exitNodes;
        this.graph = graph;
    }

    /**
     * Returns the vertices in the graph.
     *
     * @return Collection<Vertex<AtomicReference>>
     */
    public Collection<Vertex<AtomicReference>> getVertices() {

        return this.graph.getVertices();
    }

    /**
     * Returns the number of lanes in the roundabout.
     *
     * @return int
     */
    public int getLaneCount() {

        int nLanes = this.getVertices().stream().mapToInt(Vertex::getWeight).filter(v -> v >= 0).max().orElse(0);

        return nLanes + 1;
    }

    /**
     * Queues the vehicle on a certain entry.
     *
     * @param v The vehicle to be queued.
     * @param entry The entry in which to be queued.
     *
     * @return ConcurrentLinkedQueue<Vehicle> The queue.
     */
    public ConcurrentLinkedQueue<Vehicle> queueOnEntry(Vehicle v, int entry) {

        // Get the entry queue
        ConcurrentLinkedQueue<Vehicle> queue = (ConcurrentLinkedQueue<Vehicle>) this.entryNodes.get(entry).getValue().get();

        // Add vehicle to the entry queue
        queue.add(v);

        // Return the queue reference so the vehicle can peek
        return queue;
    }

    /**
     * Returns a list with the vehicle route inside the roundabout graph.
     *
     * @param entry The entry the vehicle is coming from.
     * @param exit The exit the vehicle is taking.
     *
     * @return Deque<AtomicReference>
     */
    public Deque<Vertex<AtomicReference>> getVehicleShortestRoute(int entry, int exit) {

        // Get source and destination vertex
        int origin = this.entryNodes.get(entry).getKey();
        int destination = this.exitNodes.get(exit).getKey();

        // Get all paths from source to destination but remove entry node
        Deque<Vertex> route = GraphAlgorithms.getShortestPath(this.graph, origin, destination);
        route.removeFirst();

        // Convert into Deque of Vertex value
        Deque<Vertex<AtomicReference>> shortestRoute = new ArrayDeque<>();

        // Add all elements from first route
        StringBuilder builder = new StringBuilder().append("Vehicle Route: Start -> ");
        for(Vertex vertex: route) {

            shortestRoute.add(vertex);
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
