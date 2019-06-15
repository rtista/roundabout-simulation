package domain.vehicles;

import domain.roundabout.Roundabout;
import graphv2.Vertex;

import java.awt.*;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Represent a vehicle which is always attempting to move to the next point fast.
 * It accelerates more and has higher max speed meaning it travels the roundabout faster.
 */
public class AggressiveBehaviourCar extends Vehicle {


    public AggressiveBehaviourCar(Color color, int source, int destination, Roundabout roundabout) {
        super(color, source, destination, 10, 60, roundabout);
    }

    @Override
    protected Deque<Vertex<AtomicReference>> getVehicleRoute(int entry, int exit) {
        return this.roundabout.getVehicleShortestRoute(entry, exit);
    }

    @Override
    protected float accelerate(float currentSpeed) {

        return currentSpeed + this.acceleration;
    }

    @Override
    protected float decelerate(float currentSpeed) {

        return currentSpeed - this.acceleration;
    }

    @Override
    protected long waitOnQueue() {

        return 100;
    }

    @Override
    protected long waitToTravel() {

        return 100;
    }
}
