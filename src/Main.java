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

        // Create GUI and start it
        GUI gui = new GUI("Nimbus");
        new Thread(gui).start();
    }
}
