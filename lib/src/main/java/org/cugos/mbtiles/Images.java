package org.cugos.mbtiles;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.Logger;

public class Images {

    private static final Logger LOGGER = Logger.getLogger(Images.class.getName());

    public static BufferedImage createImage(Size<Integer> size, Color color) {
        BufferedImage image = new BufferedImage(size.getWidth(), size.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setColor(color);
        graphics2D.fillRect(0,0, size.getWidth(), size.getHeight());
        graphics2D.dispose();
        return image;
    }

    public static BufferedImage createImage(Tile tile, Size<Integer> size, Color backgrounColor, Color textColor) {
        BufferedImage image = new BufferedImage(size.getWidth(), size.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setColor(backgrounColor);
        graphics2D.fillRect(0,0, size.getWidth(), size.getHeight());
        graphics2D.setColor(textColor);
        drawString(graphics2D, tile.toString(), size);
        graphics2D.dispose();
        return image;
    }

    public static byte[] getBytes(BufferedImage image, Type type) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, type.toString(), outputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    public static byte[] getBytes(File file) {
        String fileName = file.getName();
        String fileNameExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        Type imageType = Type.valueOf(fileNameExtension.toUpperCase());
        return getBytes(file, imageType);
    }
    public static byte[] getBytes(File file, Type type) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            BufferedImage image = ImageIO.read(file);
            if (isJpegAndHasAlpha(image, type)) {
                image = removeAlpha(image);
            }
            ImageIO.write(image, type.toString().toLowerCase(), outputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        byte[] bytes = outputStream.toByteArray();
        return bytes;
    }

    public static void write(byte[] bytes, File file) {
        try(OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(bytes);
        } catch(IOException ex) {
            LOGGER.warning(String.format("Error writing bytes to %s: %s", file.getAbsolutePath(), ex.getMessage()));
        }
    }

    private static boolean isJpegAndHasAlpha(BufferedImage image, Type type) {
        return type == Type.JPEG && image.getType() == BufferedImage.TYPE_INT_ARGB;
    }

    private static BufferedImage removeAlpha(BufferedImage image) {
        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return img;
    }

    private static void drawString(Graphics2D graphics, String text, Size<Integer> size) {
        graphics.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fontMetrics = graphics.getFontMetrics(graphics.getFont());
        int x = 0 + (size.getWidth() - fontMetrics.stringWidth(text)) / 2;
        int y = 0 + ((size.getHeight() - fontMetrics.getHeight()) / 2) + fontMetrics.getHeight() - fontMetrics.getDescent();
        graphics.drawString(text, x, y);
    }

    public enum Type {
        PNG, JPEG
    }

}
