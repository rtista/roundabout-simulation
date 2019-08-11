package ui.components;

import domain.roundabout.Factory;
import domain.vehicles.Vehicle;
import graphv2.Vertex;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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
     *
     */
    private int a = SIZE / 4;
    private int b = a;
    private int r = 4 * SIZE / 5;

    /**
     * Base constructor.
     */
    public RoundaboutVisualizer() {
        super(true);

        // Get Lane perimeter map and set preferred size based on roundabout
        Map<Integer, Double> lanePerimeterMap = Factory.getInstance().getRoundabout().getLanePerimeterMap();
        int dim = (int) Math.round(lanePerimeterMap.get(0)) * 5;
        this.setPreferredSize(new Dimension(dim, dim));
    }

    /**
     * Called when component changes.
     *
     * @param g The Graphics object.s
     */
    @Override
    protected void paintComponent(Graphics g) {

        // Get Lane perimeter map and set preferred size based on roundabout
        Map<Integer, Double> lanePerimeterMap = Factory.getInstance().getRoundabout().getLanePerimeterMap();
        int dim = (int) Math.round(lanePerimeterMap.get(0)) * 5;
        this.setPreferredSize(new Dimension(dim, dim));

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // For each of the lanes
        for (Integer i : lanePerimeterMap.keySet()) {

            // Calculations
            a = getWidth() / 2;
            b = getHeight() / 2;

            // Radius of circumference
            r = (int) (2 * lanePerimeterMap.get(i));

            // Radius of nodes
            int r2 = 10;

            // Draw circumference (roundabout lane)
            g2d.setColor(Color.black);
            g2d.drawOval(a - r, b - r, 2 * r, 2 * r);

            int k = 0;

            Collection<Vertex<AtomicReference>> laneVertices = Factory.getInstance().getRoundabout().getVertices(i);

            // Iterate all vertices and recreate data map
            for (Vertex<AtomicReference> v : laneVertices) {

                // Radius of circumference
                r = (int) (2 * lanePerimeterMap.get(i));

                // Default color is GREEN (node is free)
                g2d.setColor(Color.GREEN);

                Object ref = v.getValue().get();

                // Is entry then blue
                if (Factory.getInstance().getRoundabout().isEntry(v)) {

                    r += 8 * Factory.LANE_WIDTH;
                    g2d.setColor(Color.BLUE);

                // Is exit then orange
                } else if (Factory.getInstance().getRoundabout().isExit(v)) {

                    r += 8 * Factory.LANE_WIDTH;
                    g2d.setColor(Color.ORANGE);

                // If instance of vehicle then get vehicle color
                } else if (ref instanceof Vehicle) {

                    g2d.setColor(((Vehicle) ref).getColor());
                }

                // Calculate circle position
                double t = 2 * Math.PI * k / laneVertices.size();
                int x = (int) Math.round(a + r * Math.cos(t));
                int y = (int) Math.round(b + r * Math.sin(t));

                // Draw vertex
                g2d.fillOval(x - r2, y - r2, 2 * r2, 2 * r2);

                // Increment counter
                k++;
            }
        }
    }
}
