package org.hardcoder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class ImageController {
    public BufferedImage loadImage(Path path) throws IOException {
        return ImageIO.read(path.toFile());
    }
}

