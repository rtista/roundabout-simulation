import domain.roundabout.Roundabout;
import domain.vehicles.Car;
/*
import graph.Edge;
import graph.Graph;
import graph.Vertex;
*/

import graphv2.Graph;
import graphv2.GraphAlgorithms;
import graphv2.Vertex;

import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Main class.
 */
public class Main {

    public static final double LANE_WIDTH = 3;

    public static final double VERTEX_PER_METER_RATIO = 0.25;

    /**
     * Main method.
     * @param args Command line arguments.
     * @throws ClassNotFoundException When Vehicle children class is not found.
     * @throws NoSuchMethodException When constructor is not found.
     * @throws IllegalAccessException When new instance is not possible.
     * @throws InvocationTargetException When new instance is not possible.
     * @throws InstantiationException When new instance is not possible.
     */
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        // Holds all the Vehicle children classes
        /*final String[] VEHICLE_CLASSES = {
            "domain.vehicles.Car",
            "domain.vehicles.Motorcycle"
        };

        // Simulation Configuration Variables
        final int VEHICLES = 100;
        final Vehicle[] vehicles = new Vehicle[VEHICLES];

        // Create and start Vehicles
        for (int i = 0 ; i < vehicles.length; i++) {

            // Get random class from array
            String rand = VEHICLE_CLASSES[new Random(VEHICLE_CLASSES.length).nextInt()];

            // Create instance of that class
            Vehicle v = (Vehicle) Class.forName(rand).getConstructor().newInstance();

            // Append instance
            vehicles[i] = v;

            // Start instance
            vehicles[i].start();
        }*/

        /*
        // Create Roundabout Graph
        double radius = 12;
        int nLanes = 1;
        int nExits = 4;

        Roundabout roundabout = new Roundabout(radius, nLanes, nExits);

        System.out.println(roundabout.toString());

        // Create Vehicle
        Car car = new Car(0, 3, 5, roundabout);
        car.start();
        */

        Graph g = build();
        int i = 0;

        for (Deque<Vertex> path : GraphAlgorithms.getAllPaths(g, "A", "G")) {

            System.out.print("Path " + i + ": ");

            for (Vertex v : path) {

                System.out.print(v.toString() + " -> ");
            }

            System.out.println();

            i++;
        }


    }

    public static Graph build() {

        // Create directed graph
        Graph<AtomicReference> graph = new Graph<>(true);

        // Create Vertices
        graph.addVertex("A", new AtomicReference(null));
        graph.addVertex("B", new AtomicReference(null));
        graph.addVertex("C", new AtomicReference(null));
        graph.addVertex("D", new AtomicReference(null));
        graph.addVertex("E", new AtomicReference(null));
        graph.addVertex("F", new AtomicReference(null));
        graph.addVertex("G", new AtomicReference(null));
        graph.addVertex("H", new AtomicReference(null));

        // Create edges
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");
        graph.addEdge("D", "E");
        graph.addEdge("E", "F");
        graph.addEdge("F", "G");
        graph.addEdge("G", "H");
        graph.addEdge("H", "A");

        return graph;
    }
}
