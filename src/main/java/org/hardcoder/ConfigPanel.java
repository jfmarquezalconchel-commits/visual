package org.hardcoder;

import javax.swing.*;
import java.awt.*;

public class ConfigPanel extends JPanel {
    public ConfigPanel(JTextArea editorArea) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JCheckBox cb1 = new JCheckBox("Activar modo ejemplo");
        cb1.setSelected(true);
        add(cb1);

        add(Box.createVerticalStrut(10));
        add(new JLabel("Tamaño de fuente de editor:"));
        JSpinner sp = new JSpinner(new SpinnerNumberModel(14, 10, 36, 1));
        sp.addChangeListener(e -> editorArea.setFont(editorArea.getFont().deriveFont(((Integer) sp.getValue()).floatValue())));
        add(sp);
    }
}

