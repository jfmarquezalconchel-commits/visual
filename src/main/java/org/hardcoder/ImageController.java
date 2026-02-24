package org.hardcoder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class ImageController {
    private static final Logger LOGGER = Logger.getLogger(ImageController.class.getName());

    public BufferedImage loadImage(Path path) throws IOException {
        LOGGER.info(() -> "Loading image from: " + path);
        BufferedImage img = ImageIO.read(path.toFile());
        if (img == null) LOGGER.warning(() -> "ImageIO.read returned null for: " + path);
        else LOGGER.fine(() -> "Loaded image size=" + img.getWidth() + "x" + img.getHeight());
        return img;
    }
}
