package domain.vehicles;

import domain.roundabout.Roundabout;
import graphv2.Vertex;

import java.util.Deque;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Vehicles are represented as threads and will
 * be placed in roundabout entries.
 */
public class Vehicle extends Thread {

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
    public Vehicle(int source, int destination, double acceleration, Roundabout roundabout) {

        this.source = source;
        this.destination = destination;
        this.speed = 0;
        this.acceleration = acceleration;
        this.roundabout = roundabout;
    }

    /**
     * Returns the vehicle roundabout entry point.
     *
     * @return int
     */
    public int getSource() {
        return this.source;
    }

    /**
     * Returns the vehicle roundabout destination exit.
     *
     * @return int
     */
    public int getDestination() {
        return this.destination;
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

        // Ask for path to roundabout
        Deque<Vertex<AtomicReference>> path = this.roundabout.getVehicleShortestRoute(this);
        Vertex<AtomicReference> last = null;

        // Traverse Path
        for (Vertex<AtomicReference> v : path) {

            System.out.println("Moving to node " + v.getKey());

            // Move to node
            while (!v.getValue().compareAndSet(null, this)) {

                // Print behaviour
                System.out.println("Waiting for lock on the " + v.getKey() + " node!");

                // Sleep
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Moving from node to node
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Unlock previous locked node
            if (last != null) {

                // Release last node
                while (!last.getValue().compareAndSet(this, null)) {

                    // Print behaviour
                    System.out.println("Releasing lock on the " + last.getKey() + " node!");

                    // Sleep
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Assign v as the last node which it travelled to
            last = v;
        }

        // Release last node
        while (!last.getValue().compareAndSet(this, null)) {

            // Print behaviour
            System.out.println("Releasing lock on the " + last.getKey() + " node!");

            // Sleep
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
