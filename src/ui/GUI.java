package ui;

import domain.roundabout.Roundabout;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame implements Runnable {

    /**
     * The roundabout visualizer.
     */
    private RoundaboutVisualizer roundaboutVisualizer;

    /**
     * The vehicle spawn configuration panel.
     */
    private SpawnPanel spawnPanel;

    /**
     * The UI Data Updater.
     */
    private UIDataUpdater updater;

    /**
     * Creates the graphic user interface JFrame.
     *
     * @param roundabout The roundabout object.
     */
    public GUI(Roundabout roundabout) {

        super();

        // Set layout and default close operation
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Spawn UI Data Updater
        this.updater = new UIDataUpdater(roundabout);

        // Create JPanel instances
        this.roundaboutVisualizer = new RoundaboutVisualizer(updater, roundabout.getLanePerimeterMap());
        this.spawnPanel = new SpawnPanel(roundabout);

        // Add panels to layout
        add(this.roundaboutVisualizer, BorderLayout.NORTH);
        add(this.spawnPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    /**
     * Updates UI roundabout representation with current values.
     * Executed in a separated thread.
     */
    @Override
    public void run() {

        // Start UI Data Updater
        this.updater.start();

        // Update Roundabout Visualizer
        while (true) {

            // Repaint GUI
            this.roundaboutVisualizer.repaint();

            // Sleep for a fifth of a second
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
