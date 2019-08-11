package ui.components;

import domain.roundabout.Factory;
import domain.roundabout.Roundabout;

import javax.swing.*;
import java.awt.*;
import java.security.InvalidParameterException;

public class BuildPanel extends JPanel {

    /**
     * Radius value.
     */
    private JSpinner radiusSpinner;

    /**
     * Lane width spinner.
     */
    private JSpinner laneWidthSpinner;

    /**
     * Vertex per meter ratio spinner.
     */
    private JSpinner vertexPerMeterSpinner;

    /**
     * Number of lanes value.
     */
    private JSpinner lanesSpinner;

    /**
     * Number of entries value.
     */
    private JSpinner entriesSpinner;

    /**
     * Number of exits value.
     */
    private JSpinner exitsSpinner;

    /**
     * The spawn button.
     */
    private JButton buildButton;

    /**
     * Instantiates a BuildPanel object.
     */
    public BuildPanel() {

        super(true);

        // Float spinners
        this.radiusSpinner = new JSpinner(new SpinnerNumberModel(15, 1, 50, 0.25));
        this.laneWidthSpinner = new JSpinner(new SpinnerNumberModel(Factory.LANE_WIDTH, 0.25, 10, 0.25));
        this.vertexPerMeterSpinner = new JSpinner(new SpinnerNumberModel(Factory.VERTEX_PER_METER_RATIO, 0.05, 10, 0.05));

        // Integer Spinners
        this.lanesSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        this.entriesSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));
        this.exitsSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));

        // Vehicle Spawn Button
        this.buildButton = new JButton("Build Roundabout");
        this.buildButton.addActionListener(actionEvent -> {

            // Get values from integer spinners
            int entries = (int) this.entriesSpinner.getValue();
            int exits = (int) this.exitsSpinner.getValue();
            int lanes = (int) this.lanesSpinner.getValue();

            // Get values from float spinners
            double radius = (double) this.radiusSpinner.getValue();
            double laneWidth = (double) this.laneWidthSpinner.getValue();
            double vertexPerMeter = (double) this.vertexPerMeterSpinner.getValue();

            // Set lane width and vertex per meter values
            Factory.getInstance().setLaneWidth(laneWidth);
            Factory.getInstance().setVertexPerMeterRatio(vertexPerMeter);

            // Attempt to build and set the roundabout
            try {
                Roundabout newRoundabout = Factory.getInstance().buildRoundabout(radius, lanes, entries, exits);

                // Set the new roundabout
                Factory.getInstance().setRoundabout(newRoundabout);

            } catch (InvalidParameterException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", ERROR);
            }
        });

        // Set panel layout
        setLayout(new FlowLayout());

        // Add all components
        add(new LabeledJSpinner(new JLabel("Radius:"), this.radiusSpinner));
        add(new LabeledJSpinner(new JLabel("Lane Width:"), this.laneWidthSpinner));
        add(new LabeledJSpinner(new JLabel("Vertex p/ meter:"), this.vertexPerMeterSpinner));
        add(new LabeledJSpinner(new JLabel("Lanes:"), this.lanesSpinner));
        add(new LabeledJSpinner(new JLabel("Entries:"), this.entriesSpinner));
        add(new LabeledJSpinner(new JLabel("Exits:"), this.exitsSpinner));
        add(this.buildButton);

        // Set visible
        setVisible(true);

        // Build default roundabout
        this.buildButton.doClick();
    }
}
