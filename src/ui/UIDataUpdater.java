package ui;

import domain.roundabout.Roundabout;
import domain.vehicles.Vehicle;
import graphv2.Vertex;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User interface monitors all the vertices
 * in the roundabout and shows, in real-time,
 * the vacant and occupied vertices to the user.
 */
public class UIDataUpdater extends Thread {

    /**
     * The roundabout object.
     */
    private final Roundabout roundabout;

    /**
     * A map of the vertex keys against whether
     * the vertex is occupied or not.
     */
    private Map<Integer, TreeMap<Integer, Color>> data;

    /**
     * Whether the thread can or not keep running.
     */
    private boolean canrun;

    /**
     * The constructor.
     *
     * @param roundabout The roundabout object.
     */
    public UIDataUpdater(Roundabout roundabout) {
        this.canrun = true;
        this.roundabout = roundabout;
        this.data = new HashMap<>();

        // Populate vertices and data maps
        this.roundabout.getVertices().forEach(vertex -> {

            int weight = vertex.getWeight();

            // Make entries and exits stay in outer lanes
            if (vertex.getWeight() < 0) {
                weight = 0;
            }

            // Create new weight hash map
            if (!this.data.containsKey(weight)) {

                this.data.put(vertex.getWeight(), new TreeMap<>());
            }

            // Place vertex
            this.data.get(weight).put(vertex.getKey(), Color.GREEN);
        });
    }

    /**
     * Get vertex vacancy data.
     *
     * @return Map<Integer, TreeMap < Integer, Color>>
     */
    public Map<Integer, TreeMap<Integer, Color>> getData() {

        return this.data;
    }

    /**
     * Signals the thread to gracefully terminate.
     *
     * @return boolean
     */
    public void end() {
        this.canrun = false;
    }

    /**
     * This is executed in a separate thread.
     */
    @Override
    public void run() {

        // Just so we're able to tell the thread to gracefully stop.
        while (this.canrun) {

            // Iterate all vertices and recreate data map
            for (Vertex<AtomicReference> v : this.roundabout.getVertices()) {

                int weight = v.getWeight();

                // Make entries and exits stay in outer lanes
                if (v.getWeight() < 0) {
                    weight = 0;
                }

                // Default color is GREEN (node is free)
                Color color = Color.GREEN;

                Object ref = v.getValue().get();

                // Is entry then blue
                if (this.roundabout.isEntry(v)) {

                    color = Color.BLUE;

                    // Is exit then orange
                } else if (this.roundabout.isExit(v)) {

                    color = Color.ORANGE;

                    // If instance of vehicle then get vehicle color
                } else if (ref instanceof Vehicle) {

                    color = ((Vehicle) ref).getColor();
                }

                // Override vehicle data
                this.data.get(weight).replace(v.getKey(), color);
            }
        }
    }
}
