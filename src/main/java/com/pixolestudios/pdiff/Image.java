package main.java.com.pixolestudios.pdiff;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("MagicNumber")
public class Image {
    private BufferedImage img = null;
    private int height;
    private int width;

    /**
     * Loads an image and its details
     * @param pathToImg the path to the image to load
     */
    public Image(String pathToImg) {
        try {
            img = ImageIO.read(new File(pathToImg));
            height = img.getHeight();
            width = img.getWidth();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred reading file " + pathToImg, "I/O Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected int getHeight() {
        return height;
    }

    protected int getWidth() {
        return width;
    }

    protected int getRGB(int x, int y) {
        return img.getRGB(x, y);
    }

    protected int getAlpha(int x, int y) {
        return (img.getRGB(x, y) >> 24) & 0xff;
    }

    protected int getRed(int x, int y) {
        return (img.getRGB(x, y) >> 16) & 0xff;
    }

    protected int getGreen(int x, int y) {
        return (img.getRGB(x, y) >> 8) & 0xff;
    }

    protected int getBlue(int x, int y) {
        return (img.getRGB(x, y)) & 0xff;
    }

    /**
     * Returns a bitwise integer value of an aRGB value
     * @param a alpha value
     * @param r red value
     * @param g green value
     * @param b blue value
     * @return the bitwise integer value of the color
     */
    protected static int toRGB(int a, int r, int g, int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
