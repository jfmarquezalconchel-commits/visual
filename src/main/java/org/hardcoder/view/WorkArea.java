package org.hardcoder.view;

import org.hardcoder.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Clase que encapsula el área de trabajo con CardLayout.
 * Gestiona las tarjetas (panels) para cada sección de la aplicación.
 */
public class WorkArea extends JPanel {
    private CardLayout cardLayout;
    private JTextArea editorArea;
    private JPanel imagePanel;
    private EditorController editorController;
    private DocumentModel docModel;

    // Identificadores de tarjetas
    public static final String CARD_NUEVO = "NUEVO";
    public static final String CARD_BUSCAR = "BUSCAR";
    public static final String CARD_BUSCAR_REEMPLAZAR = "BUSCAR_REEMPLAZAR";
    public static final String CARD_CONFIG = "CONFIG";
    public static final String CARD_SOBRE = "SOBRE";
    public static final String CARD_IMAGE = "IMAGE";

    public WorkArea(EditorController editorController, DocumentModel docModel) {
        this.editorController = editorController;
        this.docModel = docModel;

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Editor común
        editorArea = new JTextArea();
        editorArea.setLineWrap(true);
        editorArea.setWrapStyleWord(true);

        // Añadir tarjetas
        add(new NuevoPanel(editorArea), CARD_NUEVO);
        add(new BuscarPanel(editorArea, editorController, docModel), CARD_BUSCAR);
        add(new BuscarReemplazarPanel(editorArea, editorController, docModel), CARD_BUSCAR_REEMPLAZAR);
        add(new ConfigPanel(editorArea), CARD_CONFIG);
        add(new SobrePanel(), CARD_SOBRE);
    }

    public void showCard(String cardName) {
        cardLayout.show(this, cardName);
    }

    public JTextArea getEditorArea() {
        return editorArea;
    }

    public EditorController getEditorController() {
        return editorController;
    }

    public DocumentModel getDocModel() {
        return docModel;
    }

    public void setImagePanel(BufferedImage img) {
        // Si ya existe un panel de imagen anterior, lo removemos
        if (imagePanel != null) {
            remove(imagePanel);
        }
        // Crear un panel que escala la imagen al tamaño disponible
        imagePanel = new ImagePanel(img);
        add(imagePanel, CARD_IMAGE);
        revalidate();
        repaint();
        showCard(CARD_IMAGE);
    }

    public JPanel getImagePanel() {
        return imagePanel;
    }
}
