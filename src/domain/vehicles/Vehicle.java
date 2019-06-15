package domain.vehicles;

import domain.roundabout.Factory;
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
public abstract class Vehicle extends Thread {

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
    protected float acceleration;

    /**
     * The vehicle's current speed.
     */
    private float speed;

    /**
     * The vehicle's maximum speed.
     */
    private float maxSpeed;

    /**
     * The roundabout data structure
     */
    protected Roundabout roundabout;

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
     * @param maxSpeed     The vehicle's maximum speed.
     * @param roundabout   The roundabout data structure.
     */
    public Vehicle(Color color, int source, int destination, float acceleration, float maxSpeed, Roundabout roundabout) {

        this.label = null;
        this.color = color;
        this.source = source;
        this.destination = destination;
        this.speed = 0;
        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;
        this.roundabout = roundabout;
    }

    /**
     * Vehicle constructor.
     *
     * @param label        The vehicle's label.
     * @param source       The roundabout entry from which the vehicle is coming.
     * @param destination  The roundabout exit which the vehicle is taking.
     * @param acceleration The vehicle's acceleration.
     * @param maxSpeed     The vehicle's maximum speed.
     * @param roundabout   The roundabout data structure.
     */
    public Vehicle(String label, Color color, int source, int destination, float acceleration, float maxSpeed, Roundabout roundabout) {

        this.label = label;
        this.color = color;
        this.source = source;
        this.destination = destination;
        this.speed = 0;
        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;
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
     * Returns the vehicle route in the roundabout.
     *
     * @param entry The entry the vehicle is approaching.
     * @param exit The exit the vehicle intends to take.
     *
     * @return Deque<Vertex<AtomicReference>> The vehicle route in the roundabout
     */
    protected abstract Deque<Vertex<AtomicReference>> getVehicleRoute(int entry, int exit);

    /**
     * Accelerates the vehicle.
     *
     * @param currentSpeed The current speed before accelerating.
     *
     * @return float The new current speed after accelerating.
     */
    protected abstract float accelerate(float currentSpeed);

    /**
     * Decelerates the vehicle.
     *
     * @param currentSpeed The current speed before decelerating.
     *
     * @return float The new current speed after deceleration.
     */
    protected abstract float decelerate(float currentSpeed);

    /**
     * Waits on the vehicle queue at the roundabout entrance.
     *
     * @return long The number of milliseconds to wait before attempting to enter the roundabout again.
     */
    protected abstract long waitOnQueue();

    /**
     * Travels between two points.
     *
     * @return long The number of milliseconds to to wait before attempting to move to the next point again.
     */
    protected abstract long waitToTravel();

    /**
     * Travels between two points based on the general rules of physics.
     *
     * @return long The number of milliseconds to move from one point to another.
     */
    protected long travel() {

        return Math.round(((1 / Factory.VERTEX_PER_METER_RATIO) / (this.speed / 3.6)) * 1000);
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

        // Define vehicle label if undefined
        if (this.label == null) {
            this.label = "Vehicle_" + this.getId();
        }

        // Ask roundabout object for path
        Deque<Vertex<AtomicReference>> path = this.getVehicleRoute(this.source, this.destination);
        Vertex<AtomicReference> last = null;

        // Get entry queue
        ConcurrentLinkedQueue<Vehicle> entry = this.roundabout.queueOnEntry(this, this.source);

        // Wait for first in queue
        while(entry.peek() != this) this.vehicleSleep(waitOnQueue());

        // Traverse Path
        for (Vertex<AtomicReference> v : path) {

            // Accelerate between path nodes
            if (this.speed < this.maxSpeed) this.speed = accelerate(this.speed);

            // Move to node
            while (!v.getValue().compareAndSet(null, this)) {

                // Decelerate to not crash into another vehicle
                while(this.speed > 0) decelerate(this.speed);

                System.out.println(this.label + ": Waiting for next node " + v.getKey());

                // Wait
                this.vehicleSleep(waitToTravel());
            }

            System.out.println(this.label + ": Moving to node " + v.getKey());

            // Moving from node to node
            this.vehicleSleep(travel());

            // Remove myself from queue only after locking the first node
            if (path.peekFirst() == v) entry.remove(this);

            // Release last node
            while(last != null && !last.getValue().compareAndSet(this, null));

            // Assign v as the last node which it travelled to
            last = v;
        }

        // Release last node
        while(!last.getValue().compareAndSet(this, null));
    }
}
