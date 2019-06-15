import domain.roundabout.Factory;
import domain.roundabout.Roundabout;
import ui.GUI;

import java.security.InvalidParameterException;

/**
 * Main class.
 */
public class Main {

    /**
     * Main method.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {

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

        // Create GUI and start it
        GUI gui = new GUI(roundabout);
        new Thread(gui).start();
    }
}
