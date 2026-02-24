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
        workArea.add(createNuevoPanel(), CARD_NUEVO);
        workArea.add(createBuscarPanel(), CARD_BUSCAR);
        workArea.add(createBuscarReemplazarPanel(), CARD_BUSCAR_REEMPLAZAR);
        workArea.add(createConfigPanel(), CARD_CONFIG);
        workArea.add(createSobrePanel(), CARD_SOBRE);
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
    private JPanel createNuevoPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel("Espacio de trabajo - Nuevo documento", SwingConstants.CENTER);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 18f));
        p.add(lbl, BorderLayout.NORTH);
        JScrollPane sp = new JScrollPane(editorArea);
        p.add(sp, BorderLayout.CENTER);
        return p;
    }
    private JPanel createBuscarPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lbl = new JLabel("Término a buscar:");
        JTextField tf = new JTextField(20);
        JButton bFind = new JButton("Buscar siguiente");
        bFind.addActionListener(e -> doFind(tf.getText(), false));
        top.add(lbl);
        top.add(tf);
        top.add(bFind);
        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(editorArea), BorderLayout.CENTER);
        return p;
    }
    private JPanel createBuscarReemplazarPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.add(new JLabel("Buscar:"));
        JTextField tfBuscar = new JTextField(15);
        controls.add(tfBuscar);
        controls.add(new JLabel("Reemplazar:"));
        JTextField tfReemplazar = new JTextField(15);
        controls.add(tfReemplazar);
        JButton bFind = new JButton("Buscar siguiente");
        bFind.addActionListener(e -> doFind(tfBuscar.getText(), false));
        JButton bReplace = new JButton("Reemplazar");
        bReplace.addActionListener(e -> doReplace(tfBuscar.getText(), tfReemplazar.getText(), false));
        JButton bReplaceAll = new JButton("Reemplazar todo");
        bReplaceAll.addActionListener(e -> doReplace(tfBuscar.getText(), tfReemplazar.getText(), true));
        controls.add(bFind);
        controls.add(bReplace);
        controls.add(bReplaceAll);
        p.add(controls, BorderLayout.NORTH);
        p.add(new JScrollPane(editorArea), BorderLayout.CENTER);
        return p;
    }
    private JPanel createConfigPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JCheckBox cb1 = new JCheckBox("Activar modo ejemplo");
        cb1.setSelected(true);
        p.add(cb1);
        p.add(Box.createVerticalStrut(10));
        p.add(new JLabel("Tamaño de fuente de editor:"));
        JSpinner sp = new JSpinner(new SpinnerNumberModel(14, 10, 36, 1));
        sp.addChangeListener(e -> editorArea.setFont(editorArea.getFont().deriveFont(((Integer) sp.getValue()).floatValue())));
        p.add(sp);
        return p;
    }
    private JPanel createSobrePanel() {
        JPanel p = new JPanel(new BorderLayout());
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setText("Visual - Aplicación de ejemplo\n\nEsta aplicación demuestra una interfaz básica con Swing.\n\nAutores: Equipo de ejemplo\n2026");
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        p.add(new JScrollPane(ta), BorderLayout.CENTER);
        return p;
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
    // Panel interno para mostrar y escalar imágenes manteniendo la proporción
    private static class ImagePanel extends JPanel {
        private final BufferedImage image;
        ImagePanel(BufferedImage image) {
            this.image = image;
            // para que el panel ocupe espacio inicial razonable
            setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image == null) return;
            int w = getWidth();
            int h = getHeight();
            double imgW = image.getWidth();
            double imgH = image.getHeight();
            double scale = Math.min((double) w / imgW, (double) h / imgH);
            int drawW = (int) Math.round(imgW * scale);
            int drawH = (int) Math.round(imgH * scale);
            int x = (w - drawW) / 2;
            int y = (h - drawH) / 2;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.drawImage(image, x, y, drawW, drawH, null);
            g2.dispose();
        }
    }
}

/**
 *
 * test push with ia
 *
 */

