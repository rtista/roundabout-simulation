package ui;

import domain.roundabout.Factory;
import domain.roundabout.Roundabout;
import ui.components.BuildPanel;
import ui.components.RoundaboutVisualizer;
import ui.components.SpawnPanel;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame implements Runnable {

    /**
     * The roundabout visualizer.
     */
    private RoundaboutVisualizer roundaboutVisualizer;

    /**
     * Creates the graphic user interface JFrame.
     *
     * @param lookandfeel LookAndFeel class name for the Graphical User Interface.
     */
    public GUI(String lookandfeel) {

        super();

        // Safely set look and feel
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

            //  If theme is available
            if (info.getName().equals(lookandfeel)) {

                try {
                    UIManager.setLookAndFeel(info.getClassName());

                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    System.out.println("Could not set Nimbus look and feel safely.");
                }
            }
        }

        // Set layout and default close operation
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // The roundabout configuration object
        BuildPanel buildPanel = new BuildPanel();

        // The vehicle spawn configuration panel
        SpawnPanel spawnPanel = new SpawnPanel();

        // Create JPanel instances
        this.roundaboutVisualizer = new RoundaboutVisualizer();

        // Add panels to layout
        add(this.roundaboutVisualizer, BorderLayout.NORTH);
        add(buildPanel, BorderLayout.CENTER);
        add(spawnPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    /**
     * Updates UI roundabout representation with current values.
     * Executed in a separated thread.
     */
    @Override
    public void run() {

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
