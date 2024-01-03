package org.cugos.mbtiles;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagesTest {

    @Test
    void createColorImage() throws IOException {
        BufferedImage image = Images.createImage(Size.of(256,256), Color.BLUE);
        ImageIO.write(image, "PNG", new File("tile.png"));
    }

    @Test
    void getBytesFromImage() throws IOException {
        BufferedImage image = Images.createImage(Size.of(256,256), Color.BLUE);
        byte[] data = Images.getBytes(image, Images.Type.PNG);
        assertFalse(data.length == 0);
    }

    @Test
    void getBytesFromFile() throws IOException {
        File file = new File("src/test/resources/tiles/0/0/0.png");
        assertTrue(file.exists());
        byte[] data = Images.getBytes(file, Images.Type.PNG);
        assertFalse(data.length == 0);
    }

}
