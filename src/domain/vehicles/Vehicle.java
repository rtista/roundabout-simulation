package domain.vehicles;

import domain.roundabout.Roundabout;

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
     * @param source The roundabout entry from which the vehicle is coming.
     * @param destination The roundabout exit which the vehicle is taking.
     * @param acceleration The vehicle's acceleration.
     * @param roundabout The roundabout data structure.
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
     *
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

        int wait = 5;

        // Wait for first place in queue (roundabout entry)
        while(wait > 0) {

            System.out.println("Waiting in queue...");
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wait--;
        }

        // Speed increases by acceleration every round (Travelling)
        // this.speed += this.acceleration;

        // Ask for path to roundabout
        Deque<AtomicReference> path = this.roundabout.getVehicleShortestRoute(this);

        int i = 0;

        // Traverse Path
        for (AtomicReference v : path) {

            i++;

            // Print behaviour
            System.out.println("Going to node " + i);

            // Move to node
            do {
                // Print behaviour
                System.out.println("Waiting for lock on the " + i + " node!");

                // Sleep
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } while(!v.compareAndSet(null, this));

            // Release last node
            do {
                // Print behaviour
                System.out.println("Releasing lock on the " + i + " node!");

                // Sleep
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } while(!v.compareAndSet(this, null));
        }
    }
}
