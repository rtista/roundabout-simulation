package domain.vehicles;

import domain.roundabout.Roundabout;
import graphv2.Vertex;

import java.awt.*;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultBehaviourCar extends Vehicle {


    public DefaultBehaviourCar(Color color, int source, int destination, float acceleration, float maxSpeed, Roundabout roundabout) {
        super(color, source, destination, acceleration, maxSpeed, roundabout);
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

        return 1000;
    }

    @Override
    protected long waitToTravel() {

        return 1000;
    }

    @Override
    protected long travel() {

        return 1000;
    }
}
