package ui.components;

import javax.swing.*;
import java.awt.*;

public class LabeledJSpinner extends JPanel {

    public LabeledJSpinner(JLabel label, JSpinner spinner) {

        setLayout(new FlowLayout(FlowLayout.LEADING));

        add(label);
        add(spinner);

        setBorder(null);
        setVisible(true);
    }
}
