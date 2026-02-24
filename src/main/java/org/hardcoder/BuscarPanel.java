package org.hardcoder;

import javax.swing.*;
import java.awt.*;

public class BuscarPanel extends JPanel {
    public BuscarPanel(JTextArea editorArea, EditorController controller, DocumentModel model) {
        super(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lbl = new JLabel("Término a buscar:");
        JTextField tf = new JTextField(20);
        JButton bFind = new JButton("Buscar siguiente");
        bFind.addActionListener(e -> {
            String term = tf.getText();
            int caret = editorArea.getCaretPosition();
            int idx = controller.find(term, caret);
            if (idx >= 0) {
                editorArea.requestFocus();
                editorArea.select(idx, idx + term.length());
            } else {
                JOptionPane.showMessageDialog(this, "Término no encontrado.", "Buscar", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        top.add(lbl);
        top.add(tf);
        top.add(bFind);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(editorArea), BorderLayout.CENTER);
    }
}
