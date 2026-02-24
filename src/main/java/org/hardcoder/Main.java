package org.hardcoder;

import org.hardcoder.view.MenuBar;
import org.hardcoder.view.WorkArea;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
    private JFrame frame;
    private WorkArea workArea;
    private MenuBar menuBar;

    // Modelo y controladores (MVC)
    private DocumentModel docModel;
    private EditorController editorController;
    private ImageController imageController;

    public static void main(String[] args) {
        // Configurar FlatLaf Look and Feel (Windows 10 Dark style)
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Error al configurar FlatLaf: " + ex.getMessage());
        }

        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("Este entorno es headless. No es posible mostrar la interfaz gráfica.");
            return;
        }

        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        frame = new JFrame("Visual - Aplicación de ejemplo");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        // Inicializar modelo y controladores
        docModel = new DocumentModel();
        editorController = new EditorController(docModel);
        imageController = new ImageController();

        // Crear barra de menús
        menuBar = new MenuBar();
        frame.setJMenuBar(menuBar);
        attachMenuListeners();

        // Crear área de trabajo (pasando controlador y modelo)
        workArea = new WorkArea(editorController, docModel);
        frame.getContentPane().add(workArea, BorderLayout.CENTER);

        // Confirmación de salida
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                doExit();
            }
        });

        frame.setVisible(true);
        workArea.showCard(WorkArea.CARD_NUEVO);
    }

    private void attachMenuListeners() {
        // Ficheros
        menuBar.getFileMenuItemByCommand("nuevo").addActionListener(e -> doNuevo());
        menuBar.getFileMenuItemByCommand("cargar").addActionListener(e -> doCargar());
        menuBar.getFileMenuItemByCommand("salvar").addActionListener(e -> doSalvar());
        menuBar.getFileMenuItemByCommand("config").addActionListener(e -> workArea.showCard(WorkArea.CARD_CONFIG));
        menuBar.getFileMenuItemByCommand("salir").addActionListener(e -> doExit());

        // Edición
        menuBar.getEditMenuItemByCommand("buscar").addActionListener(e -> workArea.showCard(WorkArea.CARD_BUSCAR));
        menuBar.getEditMenuItemByCommand("buscar_reemplazar").addActionListener(e -> workArea.showCard(WorkArea.CARD_BUSCAR_REEMPLAZAR));

        // Ayuda
        menuBar.getHelpMenuItemByCommand("sobre").addActionListener(e -> showAboutDialog());
    }

    private void doNuevo() {
        workArea.getEditorArea().setText("");
        workArea.showCard(WorkArea.CARD_NUEVO);
    }

    private void doCargar() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt", "text", "jpg", "jpeg", "png", "gif"));
        int res = fc.showOpenDialog(frame);
        if (res == JFileChooser.APPROVE_OPTION) {
            Path p = fc.getSelectedFile().toPath();
            try {
                String lc = p.toString().toLowerCase();
                if (lc.endsWith(".jpg") || lc.endsWith(".jpeg") || lc.endsWith(".png") || lc.endsWith(".gif")) {
                    BufferedImage img = imageController.loadImage(p);
                    if (img == null) {
                        JOptionPane.showMessageDialog(frame, "No se pudo leer la imagen seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    workArea.setImagePanel(img);
                } else {
                    editorController.loadText(p);
                    workArea.getEditorArea().setText(docModel.getText());
                    workArea.showCard(WorkArea.CARD_BUSCAR_REEMPLAZAR);
                    JOptionPane.showMessageDialog(frame, "Fichero cargado: " + p.toString(), "Cargar", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error al leer el fichero: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void doSalvar() {
        JFileChooser fc = new JFileChooser();
        int res = fc.showSaveDialog(frame);
        if (res == JFileChooser.APPROVE_OPTION) {
            Path p = fc.getSelectedFile().toPath();
            try {
                editorController.saveText(p, workArea.getEditorArea().getText());
                JOptionPane.showMessageDialog(frame, "Fichero guardado: " + p.toString(), "Salvar", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error al guardar el fichero: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void doExit() {
        int r = JOptionPane.showConfirmDialog(frame, "¿Deseas salir de la aplicación?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            frame.dispose();
            System.exit(0);
        }
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(frame,
                "Visual 0.1, por Juan Francisco Marquez",
                "Sobre",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
