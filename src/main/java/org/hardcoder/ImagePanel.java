package org.hardcoder;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

// Panel para mostrar y escalar imágenes manteniendo la proporción
public class ImagePanel extends JPanel {
    private final BufferedImage image;

    public ImagePanel(BufferedImage image) {
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

