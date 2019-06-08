import domain.roundabout.Roundabout;
import domain.vehicles.Car;

import java.lang.reflect.InvocationTargetException;

/**
 * Main class.
 */
public class Main {

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

        /**
         * Create Roundabout Graph
         */
        double radius = 12;
        int nLanes = 1;
        int nExits = 4;

        Roundabout roundabout = new Roundabout(radius, nLanes, nExits);

        System.out.println(roundabout.toString());

        /**
         * Create Vehicle
         */
        Car car = new Car(0, 3, 5, roundabout);

        car.start();
    }
}
