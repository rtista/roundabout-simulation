import domain.roundabout.Factory;
import domain.roundabout.Roundabout;
import domain.vehicles.DefaultBehaviourHeavy;
import ui.GraphicalUserInterface;
import ui.UIDataUpdater;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.Random;
import java.util.Scanner;

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
            "domain.vehicles.DefaultBehaviourLight",
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

        // Create Roundabout Graph
        double radius = 15;
        int nLanes = 2;
        int nExits = 4;
        int nEntries = 4;

        Roundabout roundabout = null;

        // Build roundabout
        try {
            roundabout = Factory.getInstance().buildRoundabout(
                    radius, nLanes, nEntries, nExits);

        } catch (InvalidParameterException e) {

            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }

        // Create watcher thread
        UIDataUpdater updater = new UIDataUpdater(roundabout);
        updater.start();

        // Create GUI elements
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphicalUserInterface gui = new GraphicalUserInterface(updater);
        f.add(gui);

        f.pack();
        f.setVisible(true);

        // Create Random generator
        Random generator = new Random();

        // Query watcher thread and call repaint
        new Thread(gui).start();

        // Read from user input
        do {

            Scanner reader = new Scanner(System.in);  // Reading from System.in
            System.out.println("Press enter to create a car");

            reader.nextLine();

            new DefaultBehaviourHeavy(
                    new Color(generator.nextFloat(), generator.nextFloat(), generator.nextFloat()),
                    1,
                    4,
                    roundabout).start();

        } while (true);
    }
}
