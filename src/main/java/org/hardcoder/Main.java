package org.hardcoder;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;
public class Main {
    // Identificadores de tarjetas
    private static final String CARD_NUEVO = "NUEVO";
    private static final String CARD_BUSCAR = "BUSCAR";
    private static final String CARD_BUSCAR_REEMPLAZAR = "BUSCAR_REEMPLAZAR";
    private static final String CARD_CONFIG = "CONFIG";
    private static final String CARD_SOBRE = "SOBRE";
    private static final String CARD_IMAGE = "IMAGE";
    private JFrame frame;
    private JPanel workArea;
    private CardLayout cardLayout;
    // Área de edición común que usaremos para buscar/reemplazar y cargar/guardar
    private JTextArea editorArea;
    // Panel usado para mostrar la imagen cargada (si existe)
    private JPanel imagePanel;
    public static void main(String[] args) {
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
        // Barra de menús
        JMenuBar menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);
        // Área de trabajo con CardLayout
        workArea = new JPanel();
        cardLayout = new CardLayout();
        workArea.setLayout(cardLayout);
        // Editor común
        editorArea = new JTextArea();
        editorArea.setLineWrap(true);
        editorArea.setWrapStyleWord(true);
        // Tarjetas
        workArea.add(new NuevoPanel(editorArea), CARD_NUEVO);
        workArea.add(new BuscarPanel(editorArea), CARD_BUSCAR);
        workArea.add(new BuscarReemplazarPanel(editorArea), CARD_BUSCAR_REEMPLAZAR);
        workArea.add(new ConfigPanel(editorArea), CARD_CONFIG);
        workArea.add(new SobrePanel(), CARD_SOBRE);
        // imagePanel se crea dinámicamente al cargar una imagen
        // Confirmación de salida
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                doExit();
            }
        });
        frame.setVisible(true);
        showCard(CARD_NUEVO);
    }
    private JMenuBar createMenuBar() {
        JMenuBar mb = new JMenuBar();
        // Ficheros
        JMenu mF = new JMenu("Ficheros");
        JMenuItem miNuevo = new JMenuItem("Nuevo");
        miNuevo.addActionListener(e -> doNuevo());
        JMenuItem miCargar = new JMenuItem("Cargar");
        miCargar.addActionListener(e -> doCargar());
        JMenuItem miSalvar = new JMenuItem("Salvar");
        miSalvar.addActionListener(e -> doSalvar());
        JMenuItem miConfig = new JMenuItem("Configuración");
        miConfig.addActionListener(e -> showCard(CARD_CONFIG));
        JMenuItem miSalir = new JMenuItem("Salir");
        miSalir.addActionListener(e -> doExit());
        mF.add(miNuevo);
        mF.add(miCargar);
        mF.add(miSalvar);
        mF.addSeparator();
        mF.add(miConfig);
        mF.addSeparator();
        mF.add(miSalir);
        // Edición
        JMenu mE = new JMenu("Edición");
        JMenuItem miBuscar = new JMenuItem("Buscar");
        miBuscar.addActionListener(e -> showCard(CARD_BUSCAR));
        JMenuItem miBuscarReplace = new JMenuItem("Buscar y reemplazar");
        miBuscarReplace.addActionListener(e -> showCard(CARD_BUSCAR_REEMPLAZAR));
        mE.add(miBuscar);
        mE.add(miBuscarReplace);
        // Ayuda
        JMenu mA = new JMenu("Ayuda");
        JMenuItem miSobre = new JMenuItem("Sobre nosotros");
        // ahora muestra un diálogo emergente con la información solicitada
        miSobre.addActionListener(e -> showAboutDialog());
        mA.add(miSobre);
        mb.add(mF);
        mb.add(mE);
        mb.add(Box.createHorizontalGlue()); // empuja el menú de ayuda a la derecha
        mb.add(mA);
        return mb;
    }
    private void showCard(String name) {
        cardLayout.show(workArea, name);
    }
    private void doNuevo() {
        editorArea.setText("");
        showCard(CARD_NUEVO);
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
                    // Cargar imagen y mostrarla en la zona de trabajo
                    BufferedImage img = ImageIO.read(p.toFile());
                    if (img == null) {
                        JOptionPane.showMessageDialog(frame, "No se pudo leer la imagen seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // Si ya existe un panel de imagen anterior, lo removemos
                    if (imagePanel != null) {
                        workArea.remove(imagePanel);
                    }
                    // Crear un panel que escala la imagen al tamaño disponible
                    imagePanel = new ImagePanel(img);
                    workArea.add(imagePanel, CARD_IMAGE);
                    workArea.revalidate();
                    workArea.repaint();
                    showCard(CARD_IMAGE);
                } else {
                    // Tratamiento por defecto: intentar leer como texto
                    String content = Files.readString(p, StandardCharsets.UTF_8);
                    editorArea.setText(content);
                    showCard(CARD_BUSCAR_REEMPLAZAR);
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
                // Si el fichero existe, pedir confirmación
                if (Files.exists(p)) {
                    int o = JOptionPane.showConfirmDialog(frame, "El fichero ya existe. ¿Sobrescribir?", "Confirmar sobrescritura", JOptionPane.YES_NO_OPTION);
                    if (o != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                Files.writeString(p, editorArea.getText(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
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
    private void doFind(String term, boolean wrap) {
        if (term == null || term.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Introduce un término de búsqueda.", "Buscar", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String text = editorArea.getText();
        int caret = editorArea.getCaretPosition();
        int idx = text.indexOf(term, caret);
        if (idx == -1 && wrap) {
            idx = text.indexOf(term);
        }
        if (idx >= 0) {
            editorArea.requestFocus();
            editorArea.select(idx, idx + term.length());
        } else {
            JOptionPane.showMessageDialog(frame, "Término no encontrado.", "Buscar", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void doReplace(String term, String replacement, boolean replaceAll) {
        if (term == null || term.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Introduce un término a buscar.", "Reemplazar", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String text = editorArea.getText();
        if (replaceAll) {
            String replaced = text.replace(term, replacement);
            editorArea.setText(replaced);
            JOptionPane.showMessageDialog(frame, "Reemplazo completo realizado.", "Reemplazar todo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int selStart = editorArea.getSelectionStart();
        int selEnd = editorArea.getSelectionEnd();
        if (selStart != selEnd && text.substring(selStart, selEnd).equals(term)) {
            editorArea.replaceRange(replacement, selStart, selEnd);
            return;
        }
        // Si no hay selección válida, buscar siguiente y reemplazar
        int caret = editorArea.getCaretPosition();
        int idx = text.indexOf(term, caret);
        if (idx >= 0) {
            editorArea.select(idx, idx + term.length());
            editorArea.replaceSelection(replacement);
        } else {
            JOptionPane.showMessageDialog(frame, "Término no encontrado para reemplazar.", "Reemplazar", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    // Método que muestra la ventana emergente "Sobre"
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(frame,
                "Visual 0.1, por Juan Francisco Marquez",
                "Sobre",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
