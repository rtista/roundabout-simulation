import domain.roundabout.Factory;
import domain.roundabout.Roundabout;
import domain.vehicles.AggressiveBehaviourLight;
import domain.vehicles.DefaultBehaviourHeavy;
import domain.vehicles.DefaultBehaviourLight;
import ui.GUI;
import ui.RoundaboutVisualizer;
import ui.SpawnPanel;
import ui.UIDataUpdater;

import javax.swing.*;
import java.awt.*;
import java.security.InvalidParameterException;
import java.util.Random;
import java.util.Scanner;

/**
 * Main class.
 */
public class Main {

    /**
     * Main method.
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
