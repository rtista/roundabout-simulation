package ui;

import domain.roundabout.Factory;
import domain.roundabout.Roundabout;
import domain.vehicles.Car;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Random;

/**
 * Class provides a GUI which allows the user to see
 * what positions of the roundabout are being taken and
 * general vehicle flow.
 */
public class GraphicalUserInterface extends JPanel {

    /**
     * A map of the vertex keys against the respective value.
     */
    private UserInterface updater;

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
    public GraphicalUserInterface(UserInterface updater) {
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
        g2d.setColor(Color.black);

        // Calculations
        a = getWidth() / 2;
        b = getHeight() / 2;
        int m = Math.min(a, b);
        r = 4 * m / 5;
        int r2 = Math.abs(m - r) / 2;

        // Draw circumference
        g2d.drawOval(a - r, b - r, 2 * r, 2 * r);

        int i = 0;

        Map<Integer, Boolean> data = this.updater.getData();

        // Draw
        for (int key : data.keySet()) {

            // Vacant is green
            g2d.setColor(Color.GREEN);

            // Occupied is red
            if (data.get(key)) {
                g2d.setColor(Color.RED);
            }

            // Calculate circle position
            double t = 2 * Math.PI * i / data.size();
            int x = (int) Math.round(a + r * Math.cos(t));
            int y = (int) Math.round(b + r * Math.sin(t));
            g2d.fillOval(x - r2, y - r2, 2 * r2, 2 * r2);

            // Increment counter
            i++;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        // Create Roundabout Graph
        double radius = 20;
        int nLanes = 1;
        int nExits = 4;
        int nEntries = 4;

        Roundabout roundabout = Factory.getInstance().buildRoundabout(
                radius, nLanes, nExits, nEntries);

        // Create watcher thread
        UserInterface updater = new UserInterface(roundabout.getVertices());
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

        int cars = 5;

        // Create Vehicle
        for (int i = 0; i < cars; i++) {
            Car car = new Car(
                    1,
                    4,
                    5,
                    roundabout);
            car.start();
        }

        /*int cars = 5;

        // Create Vehicle
        for (int i = 0; i < cars; i++) {
            Car car = new Car(
                    generator.nextInt(4),
                    generator.nextInt(4),
                    5,
                    roundabout);
            car.start();
        }*/

        // Query watcher thread and call repaint
        while (true) {

            // Repaint GUI
            gui.repaint();

            // Sleep for half a second
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
