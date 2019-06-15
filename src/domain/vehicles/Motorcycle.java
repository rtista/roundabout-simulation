package domain.vehicles;

import graphv2.Vertex;

import java.util.Deque;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents a motorcycle vehicle.
 */
public class Motorcycle extends Vehicle {

    /**
     * Motorcycle empty constructor.
     */
    public Motorcycle() {

    }

    @Override
    protected Deque<Vertex<AtomicReference>> getVehicleRoute(int entry, int exit) {
        return this.roundabout.getVehicleShortestRoute(entry, exit);
    }

    @Override
    protected float accelerate(float currentSpeed) {
        return 0;
    }

    @Override
    protected float decelerate(float currentSpeed) {
        return 0;
    }

    @Override
    protected long waitOnQueue() {
        return 0;
    }

    @Override
    protected long waitToTravel() {
        return 0;
    }

    @Override
    protected long travel() {
        return 0;
    }
}
