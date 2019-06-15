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
     * The mapping of the lanes to its perimeter.
     */
    private Map<Integer, Double> lanePerimeterMap;

    /**
     * Roundabout constructor.
     *
     * @param graph The roundabout graph.
     * @param entryNodes The entry nodes map.
     * @param exitNodes The exit nodes map.
     * @param lanePerimeterMap The mapping of the lanes to its perimeter.
     */
    public Roundabout(Graph<AtomicReference> graph,
                      Map<Integer, Vertex<AtomicReference>> entryNodes,
                      Map<Integer, Vertex<AtomicReference>> exitNodes,
                      Map<Integer, Double> lanePerimeterMap) {

        this.entryNodes = entryNodes;
        this.exitNodes = exitNodes;
        this.graph = graph;
        this.lanePerimeterMap= lanePerimeterMap;
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
     * Returns the perimeters for each lane.
     *
     * @return Map<Integer, Double>
     */
    public Map<Integer, Double> getLanePerimeterMap() {
        return this.lanePerimeterMap;
    }

    /**
     * Returns the number of entries in the roundabout.
     *
     * @return int The number of entries in the roundabout.
     */
    public int getEntriesNumber() {
        return this.entryNodes.size();
    }

    /**
     * Returns the number of exits in the roundabout.
     *
     * @return int The number of exits in the roundabout.
     */
    public int getExitsNumber() {
        return this.exitNodes.size();
    }

    /**
     * Check if vertex is entry.
     *
     * @param vertex The vertex to check.
     *
     * @return boolean
     */
    public boolean isEntry(Vertex<AtomicReference> vertex) {

        return this.entryNodes.containsValue(vertex);
    }

    /**
     * Check if vertex is exit.
     *
     * @param vertex The vertex to check.
     *
     * @return boolean
     */
    public boolean isExit(Vertex<AtomicReference> vertex) {

        return this.exitNodes.containsValue(vertex);
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
    public Deque<Vertex<AtomicReference>> getVehicleRoute(int entry, int exit, boolean heavy) {

        // Get source and destination vertex
        int origin = this.entryNodes.get(entry).getKey();
        int destination = this.exitNodes.get(exit).getKey();

        // Get all paths from source to destination but remove entry node
        Deque<Vertex> route;
        if (heavy) {

            route = GraphAlgorithms.getOuterLanePath(this.graph, origin, destination);

        } else {
            route = GraphAlgorithms.getShortestPath(this.graph, origin, destination);
        }
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
