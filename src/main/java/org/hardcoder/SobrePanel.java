package org.hardcoder;

import javax.swing.*;
import java.awt.*;

public class SobrePanel extends JPanel {
    public SobrePanel() {
        super(new BorderLayout());
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setText("Visual - Aplicación de ejemplo\n\nEsta aplicación demuestra una interfaz básica con Swing.\n\nAutores: Equipo de ejemplo\n2026");
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        add(new JScrollPane(ta), BorderLayout.CENTER);
    }
}

