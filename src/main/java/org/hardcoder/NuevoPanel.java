package org.hardcoder;

import javax.swing.*;
import java.awt.*;

public class NuevoPanel extends JPanel {
    public NuevoPanel(JTextArea editorArea) {
        super(new BorderLayout());
        JLabel lbl = new JLabel("Espacio de trabajo - Nuevo documento", SwingConstants.CENTER);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 18f));
        add(lbl, BorderLayout.NORTH);
        add(new JScrollPane(editorArea), BorderLayout.CENTER);
    }
}

