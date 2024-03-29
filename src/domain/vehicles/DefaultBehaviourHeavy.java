package domain.vehicles;

import domain.roundabout.Roundabout;
import graphv2.Vertex;

import java.awt.*;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultBehaviourHeavy extends Vehicle {


    public DefaultBehaviourHeavy(Color color, int source, int destination, Roundabout roundabout) {
        super(color, source, destination, 4, 30, roundabout);
    }

    @Override
    protected Deque<Vertex<AtomicReference>> getVehicleRoute(int entry, int exit) {
        return this.roundabout.getVehicleRoute(entry, exit, true);
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
}
