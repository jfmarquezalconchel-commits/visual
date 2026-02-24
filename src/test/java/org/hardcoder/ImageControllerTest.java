package org.hardcoder;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ImageControllerTest {

    @Test
    void loadPngImage() throws Exception {
        // create a small test image
        BufferedImage img = new BufferedImage(10, 8, BufferedImage.TYPE_INT_RGB);
        Path tmp = Files.createTempFile("testimg", ".png");
        File f = tmp.toFile();
        ImageIO.write(img, "png", f);

        ImageController ic = new ImageController();
        BufferedImage loaded = ic.loadImage(tmp);
        assertNotNull(loaded);
        assertEquals(10, loaded.getWidth());
        assertEquals(8, loaded.getHeight());
    }
}

