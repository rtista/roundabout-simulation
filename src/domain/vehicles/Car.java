package domain.vehicles;

import domain.roundabout.Roundabout;

public class Car extends Vehicle {


    public Car(int source, int destination, double acceleration, Roundabout roundabout) {
        super(source, destination, acceleration, roundabout);
    }
}
