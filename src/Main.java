import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * Main class.
 */
public class Main {

    public static final String DASHES = new String(new char[80]).replace("\0", "-");

    /**
     * Main method.
     * @param args Command line arguments.
     * @throws ClassNotFoundException When Vehicle children class is not found.
     * @throws NoSuchMethodException When constructor is not found.
     * @throws IllegalAccessException When new instance is not possible.
     * @throws InvocationTargetException When new instance is not possible.
     * @throws InstantiationException When new instance is not possible.
     */
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {

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
        int cars = 1;
        double radius = 12;
        int nLanes = 1;
        int nExits = 4;

        /*Roundabout roundabout = new Roundabout(radius, nLanes, nExits);

        // Create watcher thread
        UIDataUpdater ui = new UIDataUpdater(roundabout.getVertices());
        ui.start();

        // Create Vehicle
        for (int i = 0; i < cars; i++) {
            Car car = new Car(
                    // generator.nextInt(4),
                    // generator.nextInt(4),
                    0,
                    3,
                    5,
                    roundabout);
            car.start();
        }

        final StringBuilder builder = new StringBuilder();

        // Query watcher thread and output results
        while(true) {

            // Clear builder data
            builder.setLength(0);
            builder.append(DASHES + "\n");

            Map<Integer, Boolean> data = ui.getData();

            // Iterate vertices data
            for (int key : data.keySet()) {

                builder.append("(").append(key).append(" - ").append(data.get(key)).append(") ");

                // Paragraph every 6 vertices
                if (key % 6 == 0 && key != 0) {
                    builder.append("\n");
                }
            }

            // Output info
            builder.append("\n" + DASHES + "\n");
            System.out.print(builder.toString());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }
}
