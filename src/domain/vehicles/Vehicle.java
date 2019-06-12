package domain.vehicles;

import domain.roundabout.Roundabout;
import graphv2.Vertex;

import java.awt.*;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Vehicles are represented as threads and will
 * be placed in roundabout entries.
 */
public class Vehicle extends Thread {

    /**
     * The vehicle label for identification purposes.
     * Default: Vehicle_{THREAD_ID}
     */
    private String label;

    /**
     * The vehicle color for identification purposes.
     */
    private Color color;

    /**
     * The roundabout entry from which the vehicle is coming.
     */
    private final int source;

    /**
     * The roundabout exit which the vehicle is taking.
     */
    private final int destination;

    /**
     * The vehicle's acceleration.
     */
    private final double acceleration;

    /**
     * The vehicle's current speed in KM/h.
     */
    private double speed;

    /**
     * The roundabout data structure
     */
    private Roundabout roundabout;

    /**
     * Vehicle empty constructor.
     */
    public Vehicle() {

        this.label = null;
        this.source = 0;
        this.destination = 0;
        this.speed = 0;
        this.acceleration = 0;
    }

    /**
     * Vehicle constructor.
     *
     * @param source       The roundabout entry from which the vehicle is coming.
     * @param destination  The roundabout exit which the vehicle is taking.
     * @param acceleration The vehicle's acceleration.
     * @param roundabout   The roundabout data structure.
     */
    public Vehicle(Color color, int source, int destination, double acceleration, Roundabout roundabout) {

        this.label = null;
        this.color = color;
        this.source = source;
        this.destination = destination;
        this.speed = 0;
        this.acceleration = acceleration;
        this.roundabout = roundabout;
    }

    /**
     * Vehicle constructor.
     *
     * @param label        The vehicle's label.
     * @param source       The roundabout entry from which the vehicle is coming.
     * @param destination  The roundabout exit which the vehicle is taking.
     * @param acceleration The vehicle's acceleration.
     * @param roundabout   The roundabout data structure.
     */
    public Vehicle(String label, Color color, int source, int destination, double acceleration, Roundabout roundabout) {

        this.label = label;
        this.color = color;
        this.source = source;
        this.destination = destination;
        this.speed = 0;
        this.acceleration = acceleration;
        this.roundabout = roundabout;
    }

    /**
     * Returns the vehicle color.
     *
     * @return Color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Function defined for simpler code reading on run method.
     *
     * @param l The number of milliseconds to sleep.
     */
    private void vehicleSleep(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * This will run in a separate thread.
     * <p>
     * The method replicates the driver behaviour.
     * 1. Waits in queue for its turn.
     * 2. Asks which path should it follow to the roundabout object.
     * 3. Attempts to lock the AtomicReference for each of the nodes
     * in its path. Note that only after locking the next reference
     * does it unlock the previously locked reference. This assures
     * no vehicle will be in the same spot at the same time.
     */
    @Override
    public void run() {

        // Define vehicle label if undefined
        if (this.label == null) {
            this.label = "Vehicle_" + this.getId();
        }

        // Ask roundabout object for path
        Deque<Vertex<AtomicReference>> path = this.roundabout.getVehicleShortestRoute(this.source, this.destination);
        Vertex<AtomicReference> last = null;

        // Get entry queue
        ConcurrentLinkedQueue<Vehicle> entry = this.roundabout.queueOnEntry(this, this.source);

        // Wait for first in queue
        while(entry.peek() != this) {

            this.vehicleSleep(1000);
            System.out.println(this.label + ": Waiting on entry queue.");
        }

        // Traverse Path
        for (Vertex<AtomicReference> v : path) {

            System.out.println(this.label + ": Moving to node " + v.getKey());

            // Move to node
            while (!v.getValue().compareAndSet(null, this)) {

                // Print behaviour
                System.out.println(this.label + ": Waiting for lock on the " + v.getKey() + " node!");

                // Sleep
                this.vehicleSleep(1000);
            }

            // Remove myself from queue only after locking the first node
            if (path.peekFirst() == v) entry.remove(this);

            // Moving from node to node
            // TODO: Implements physic here to with vehicle speed
            this.vehicleSleep(1000);

            // Unlock previous locked node
            if (last != null) {

                // Release last node
                while (!last.getValue().compareAndSet(this, null)) {

                    // Print behaviour
                    System.out.println(this.label + ": Releasing lock on the " + last.getKey() + " node!");

                    // Sleep
                    this.vehicleSleep(1000);
                }
            }

            // Assign v as the last node which it travelled to
            last = v;
        }

        // Release last node
        while (!last.getValue().compareAndSet(this, null)) {

            // Print behaviour
            System.out.println(this.label + ": Releasing lock on the " + last.getKey() + " node!");

            // Sleep
            this.vehicleSleep(1000);
        }

    }
}
