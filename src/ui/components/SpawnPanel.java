package ui.components;

import domain.roundabout.Factory;
import domain.vehicles.AggressiveBehaviourLight;
import domain.vehicles.DefaultBehaviourHeavy;
import domain.vehicles.DefaultBehaviourLight;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SpawnPanel extends JPanel {

    /**
     * Random number generator.
     */
    private final Random generator = new Random();
    /**
     * Vehicle types combo box.
     */
    private JComboBox<String> vehicleTypes;
    /**
     * Entry values.
     */
    private JSpinner entrySpinner;
    /**
     * Exit values.
     */
    private JSpinner exitSpinner;
    /**
     * The spawn button.
     */
    private JButton spawnButton;

    /**
     * Spawn panel constructor.
     */
    public SpawnPanel() {

        super(true);

        // Vehicle Types Combo Box
        this.vehicleTypes = new JComboBox<>();
        this.vehicleTypes.addItem("heavy:default");
        this.vehicleTypes.addItem("light:default");
        this.vehicleTypes.addItem("light:aggressive");
        this.vehicleTypes.setSelectedIndex(0);

        // Entries spinner
        this.entrySpinner = new JSpinner(new SpinnerNumberModel(1, 1, Factory.getInstance().getRoundabout().getEntriesNumber(), 1));

        // Exits spinner
        this.exitSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Factory.getInstance().getRoundabout().getExitsNumber(), 1));

        // Vehicle Spawn Button
        this.spawnButton = new JButton("Spawn Vehicle");
        this.spawnButton.addActionListener(actionEvent -> {

            // Get vehicle type
            String vehicleType = this.vehicleTypes.getSelectedItem().toString();

            System.out.println(vehicleType);

            // Get entry and exit from spinners
            int entryNumber = (int) this.entrySpinner.getValue();
            int exitNumber = (int) this.exitSpinner.getValue();

            // Evaluate vehicle class to spawn
            // Heavy vehicle with default behaviour
            if (vehicleType.equals("heavy:default")) {

                System.out.println("Creating heavy default");

                new DefaultBehaviourHeavy(
                        new Color(this.generator.nextFloat(), this.generator.nextFloat(), this.generator.nextFloat()),
                        entryNumber, exitNumber, Factory.getInstance().getRoundabout()).start();

                // Light vehicle with default behaviour
            } else if (vehicleType.equals("light:default")) {

                System.out.println("Creating light default");

                new DefaultBehaviourLight(
                        new Color(this.generator.nextFloat(), this.generator.nextFloat(), this.generator.nextFloat()),
                        entryNumber, exitNumber, Factory.getInstance().getRoundabout()).start();

                // Light vehicle with aggressive behaviour
            } else if (vehicleType.equals("light:aggressive")) {

                System.out.println("Creating light default");

                new AggressiveBehaviourLight(
                        new Color(this.generator.nextFloat(), this.generator.nextFloat(), this.generator.nextFloat()),
                        entryNumber, exitNumber, Factory.getInstance().getRoundabout()).start();
            }
        });

        // Set panel layout
        setLayout(new FlowLayout());

        // Add all components
        add(new JLabel("Vehicle/Driver:"));
        add(this.vehicleTypes);
        add(new LabeledJSpinner(new JLabel("Entry:"), this.entrySpinner));
        add(new LabeledJSpinner(new JLabel("Exit:"), this.exitSpinner));
        add(this.spawnButton);

        // Set visible
        setVisible(true);
    }
}
