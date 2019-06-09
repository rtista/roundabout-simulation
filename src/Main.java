import domain.roundabout.Roundabout;
import domain.vehicles.Car;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * Main class.
 */
public class Main {

    public static final double LANE_WIDTH = 3;

    public static final double VERTEX_PER_METER_RATIO = 0.25;

    /**
     * Main method.
     * @param args Command line arguments.
     * @throws ClassNotFoundException When Vehicle children class is not found.
     * @throws NoSuchMethodException When constructor is not found.
     * @throws IllegalAccessException When new instance is not possible.
     * @throws InvocationTargetException When new instance is not possible.
     * @throws InstantiationException When new instance is not possible.
     */
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        // Holds all the Vehicle children classes
        /*final String[] VEHICLE_CLASSES = {
            "domain.vehicles.Car",
            "domain.vehicles.Motorcycle"
        };

        // Simulation Configuration Variables
        final int VEHICLES = 100;
        final Vehicle[] vehicles = new Vehicle[VEHICLES];

        // Create and start Vehicles
        for (int i = 0 ; i < vehicles.length; i++) {

            // Get random class from array
            String rand = VEHICLE_CLASSES[new Random(VEHICLE_CLASSES.length).nextInt()];

            // Create instance of that class
            Vehicle v = (Vehicle) Class.forName(rand).getConstructor().newInstance();

            // Append instance
            vehicles[i] = v;

            // Start instance
            vehicles[i].start();
        }*/

        // Create Random generator
        Random generator = new Random();

        // Create Roundabout Graph
        double radius = 12;
        int nLanes = 1;
        int nExits = 4;

        Roundabout roundabout = new Roundabout(radius, nLanes, nExits);

        // Create Vehicle
        for (int i = 0; i < 200; i++) {
            Car car = new Car(
                    generator.nextInt(4),
                    generator.nextInt(4),
                    5,
                    roundabout);
            car.start();
        }
    }
}
