package org.hardcoder;

import javax.swing.*;
import java.awt.*;

public class BuscarReemplazarPanel extends JPanel {
    public BuscarReemplazarPanel(JTextArea editorArea, EditorController controller, DocumentModel model) {
        super(new BorderLayout());
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.add(new JLabel("Buscar:"));
        JTextField tfBuscar = new JTextField(15);
        controls.add(tfBuscar);
        controls.add(new JLabel("Reemplazar:"));
        JTextField tfReemplazar = new JTextField(15);
        controls.add(tfReemplazar);

        JButton bFind = new JButton("Buscar siguiente");
        bFind.addActionListener(e -> {
            String term = tfBuscar.getText();
            int caret = editorArea.getCaretPosition();
            int idx = controller.find(term, caret);
            if (idx >= 0) {
                editorArea.requestFocus();
                editorArea.select(idx, idx + term.length());
            } else {
                JOptionPane.showMessageDialog(this, "Término no encontrado.", "Buscar", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        JButton bReplace = new JButton("Reemplazar");
        bReplace.addActionListener(e -> {
            String term = tfBuscar.getText();
            String replacement = tfReemplazar.getText();
            boolean ok = controller.replace(term, replacement, false);
            if (ok) {
                editorArea.setText(model.getText());
            } else {
                JOptionPane.showMessageDialog(this, "Término no encontrado para reemplazar.", "Reemplazar", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        JButton bReplaceAll = new JButton("Reemplazar todo");
        bReplaceAll.addActionListener(e -> {
            String term = tfBuscar.getText();
            String replacement = tfReemplazar.getText();
            controller.replace(term, replacement, true);
            editorArea.setText(model.getText());
            JOptionPane.showMessageDialog(this, "Reemplazo completo realizado.", "Reemplazar todo", JOptionPane.INFORMATION_MESSAGE);
        });

        controls.add(bFind);
        controls.add(bReplace);
        controls.add(bReplaceAll);

        add(controls, BorderLayout.NORTH);
        add(new JScrollPane(editorArea), BorderLayout.CENTER);
    }
}
