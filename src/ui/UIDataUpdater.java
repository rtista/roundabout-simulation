package ui;

import graphv2.Vertex;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User interface monitors all the vertices
 * in the roundabout and shows, in real-time,
 * the vacant and occupied vertices to the user.
 */
public class UIDataUpdater extends Thread {

    /**
     * A map of the vertex keys against the respective value.
     */
    private Map<Integer, AtomicReference> vertices;

    /**
     * A map of the vertex keys against whether
     * the vertex is occupied or not.
     */
    private Map<Integer, Boolean> data;

    /**
     * Whether the thread can or not keep running.
     */
    private boolean canrun;

    /**
     * The constructor.
     *
     * @param vertices The roundabout graph vertices.
     */
    public UIDataUpdater(Collection<Vertex<AtomicReference>> vertices) {
        this.canrun = true;
        this.vertices = new HashMap<>();
        this.data = new HashMap<>();

        // Populate vertices and data maps
        vertices.forEach(vertex -> {
            this.vertices.put(vertex.getKey(), vertex.getValue());
            this.data.put(vertex.getKey(), false);
        });
    }

    /**
     * Get vertex vacancy data.
     *
     * @return Map<Integer, Boolean>
     */
    public Map<Integer, Boolean> getData() {

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
        while(this.canrun) {

            // Iterate all vertices and recreate data map
            for (int key : this.vertices.keySet()) {

                // Presence of vehicle
                boolean presence = true;

                // Get value from the atomic reference
                Object ref = this.vertices.get(key).get();

                // If the value is null then vertex is vacant
                if (ref == null) {

                    presence = false;
                }

                this.data.replace(key, presence);
            }
        }
    }
}
