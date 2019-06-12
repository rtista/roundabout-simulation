package domain.vehicles;

import domain.roundabout.Roundabout;

import java.awt.*;

public class Car extends Vehicle {


    public Car(Color color, int source, int destination, double acceleration, Roundabout roundabout) {
        super(color, source, destination, acceleration, roundabout);
    }
}
