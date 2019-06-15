package ui;

import domain.roundabout.Factory;
import domain.roundabout.Roundabout;
import domain.vehicles.DefaultBehaviourCar;

import javax.swing.*;
import java.awt.*;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * Class provides a GUI which allows the user to see
 * what positions of the roundabout are being taken and
 * general vehicle flow.
 */
public class GraphicalUserInterface extends JPanel {

    /**
     * A map of the vertex keys against the respective value.
     */
    private UIDataUpdater updater;

    /**
     * Circumference size.
     */
    private static final int SIZE = 256;

    /**
     *
     */
    private int a = SIZE / 4;
    private int b = a;
    private int r = 4 * SIZE / 5;

    /**
     * Base constructor.
     *
     * @param updater The roundabout graph vertices.
     */
    public GraphicalUserInterface(UIDataUpdater updater) {
        super(true);
        this.setPreferredSize(new Dimension(SIZE, SIZE));
        this.updater = updater;
    }

    /**
     * Called when component changes.
     *
     * @param g The Graphics object.s
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Get data map from updater class
        Map<Integer, TreeMap<Integer, Color>> dataMap = this.updater.getData();

        // For each of the lanes
        for (Integer i : dataMap.keySet()) {

            // Calculations
            a = getWidth() / 2;
            b = getHeight() / 2;

            // Radius of circumference
            r = (SIZE / 2) / (i + 1);

            // Radius of nodes
            int r2 = 10;

            // Draw circumference (roundabout lane)
            g2d.setColor(Color.black);
            g2d.drawOval(a - r, b - r, 2 * r, 2 * r);

            int k = 0;

            // Get vertices for this roundabout lane
            TreeMap<Integer, Color> data = dataMap.get(i);

            // Draw nodes
            for (Integer key : data.keySet()) {

                // Get color from map
                g2d.setColor(data.get(key));

                // Calculate circle position
                double t = 2 * Math.PI * k / data.size();
                int x = (int) Math.round(a + r * Math.cos(t));
                int y = (int) Math.round(b + r * Math.sin(t));
                g2d.fillOval(x - r2, y - r2, 2 * r2, 2 * r2);

                // Increment counter
                k++;
            }
        }
    }

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
        new Thread(
                () -> {
                    while (true) {

                        // Repaint GUI
                        gui.repaint();

                        // Sleep for a fifth of a second
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();

        // Read from user input
        do {

            Scanner reader = new Scanner(System.in);  // Reading from System.in
            System.out.println("Press enter to create a car");

            reader.nextLine();

            new DefaultBehaviourCar(new Color(generator.nextFloat(), generator.nextFloat(), generator.nextFloat()),
                    1,
                    4,
                    5,
                    60,
                    roundabout).start();

        } while (true);
    }
}
