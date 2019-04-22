package domain.roundabout;

import domain.vehicles.Vehicle;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Roundabout entries are concurrent linked queues
 * to provide order to vehicle circulation.
 */
public class RoundaboutEntry extends ConcurrentLinkedQueue<Vehicle> {

    /**
     * Roundabout entry empty constructor.
     */
    public RoundaboutEntry() {
    }
}
