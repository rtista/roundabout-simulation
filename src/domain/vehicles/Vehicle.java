package domain.vehicles;

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
     */
    public Vehicle(int source, int destination, double acceleration) {

        this.source = source;
        this.destination = destination;
        this.speed = 0;
        this.acceleration = acceleration;
    }

    /**
     * This will run in a separate thread.
     */
    @Override
    public void run() {

        // Wait for first place in queue (roundabout entry)


        // Speed increases by acceleration every round (Travelling)
        this.speed += this.acceleration;

        // Take exit this.destination
    }
}
