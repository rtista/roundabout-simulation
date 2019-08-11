package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class provides a GUI which allows the user to see
 * what positions of the roundabout are being taken and
 * general vehicle flow.
 */
public class RoundaboutVisualizer extends JPanel {

    /**
     * Circumference size.
     */
    private static final int SIZE = 256;
    /**
     * A map of the vertex keys against the respective value.
     */
    private UIDataUpdater updater;
    /**
     * The lane perimeter map.
     */
    private Map<Integer, Double> lanePerimeterMap;
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
    public RoundaboutVisualizer(UIDataUpdater updater, Map<Integer, Double> lanePerimeterMap) {
        super(true);
        int dim = (int) Math.round(lanePerimeterMap.get(0)) * 5;
        this.setPreferredSize(new Dimension(dim, dim));
        this.updater = updater;
        this.lanePerimeterMap = lanePerimeterMap;
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
            r = (int) (2 * this.lanePerimeterMap.get(i));

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
}
