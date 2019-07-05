package main.java.com.pixolestudios.pdiff;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {
    private BufferedImage img = null;
    private int height = 0;
    private int width = 0;

    public Image(String pathToImg) {
        try {
            img = ImageIO.read(new File(pathToImg));
        } catch (IOException e) {
            //TODO handle IO exception
            e.printStackTrace();
        }
        height = img.getHeight();
        width = img.getWidth();
    }

    protected int getHeight() {
        return height;
    }

    protected int getWidth() {
        return width;
    }

    protected int getRGB(int x, int y){
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

    protected static int toRGB(int a, int r, int g, int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
