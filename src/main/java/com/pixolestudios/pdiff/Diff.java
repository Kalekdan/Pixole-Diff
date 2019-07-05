package main.java.com.pixolestudios.pdiff;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("MagicNumber")
public class Diff {
    private Image image1 = null;
    private Image image2 = null;

    private BufferedImage diffImg = null;
    private int diffA = 255;    // Alpha
    private int diffR = 255;    // Red
    private int diffG = 0;      // Green
    private int diffB = 0;      // Blue
    // Color to highlight diffs in output image
    private int diffColor = (diffA << 24) | (diffR << 16) | (diffG << 8) | diffB;

    public Diff(String imgPath1, String imgPath2) {
        image1 = new Image(imgPath1);
        image2 = new Image(imgPath2);
    }

    protected void CalculateDiff(boolean ignoreColor, boolean generateDiffImg) {
        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
            System.out.print("different size images");
        } else {
            CompareImgs(ignoreColor, generateDiffImg);
        }
    }

    private void CompareImgs(boolean ignoreColor, boolean generateDiffImg) {
        if (generateDiffImg) {
            diffImg = new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        boolean foundDiffAtPixel = false;
        int numDiffs = 0;
        for (int i = 0; i < image1.getWidth(); i++) {
            for (int j = 0; j < image1.getHeight(); j++) {
                foundDiffAtPixel = false;
                if (image1.getRGB(i, j) != image2.getRGB(i, j)) {
//                    System.out.print("diff found at " + i + ", " + j);
                    foundDiffAtPixel = true;
                    numDiffs++;
                }
                if (generateDiffImg) {
                    GenerateDiffImg(i, j, foundDiffAtPixel);
                }
            }
        }
        if (generateDiffImg) {
            try {
                ImageIO.write(diffImg, "png", new File("output.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(CalcPercentageDiff(numDiffs, image1.getHeight() * image1.getWidth()) + "% diff");
        System.out.print(CalcPercentageMatch(numDiffs, image1.getHeight() * image1.getWidth()) + "% match");
    }

    private void GenerateDiffImg(int x, int y, boolean isDiffPixel) {
        if (!isDiffPixel) {
            diffImg.setRGB(x, y, toRGB(getPixelAlpha(image1.getRGB(x, y)) / 2,
                    getPixelRed(image1.getRGB(x, y)),
                    getPixelGreen(image1.getRGB(x, y)),
                    getPixelBlue(image1.getRGB(x, y))));
        } else {
            diffImg.setRGB(x, y, diffColor);
        }
    }

    private float CalcPercentageDiff(int numDiffPixels, int totalNoPixels) {
        return (((float) numDiffPixels) / ((float) totalNoPixels)) * 100;
    }

    private float CalcPercentageMatch(int numDiffPixels, int totalNoPixels) {
        return (((float) totalNoPixels - numDiffPixels) / ((float) totalNoPixels)) * 100;
    }

    private int getPixelAlpha(int pixel) {
        return (pixel >> 24) & 0xff;
    }

    private int getPixelRed(int pixel) {
        return (pixel >> 16) & 0xff;
    }

    private int getPixelGreen(int pixel) {
        return (pixel >> 8) & 0xff;
    }

    private int getPixelBlue(int pixel) {
        return (pixel) & 0xff;
    }

    private int toRGB(int a, int r, int g, int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

}
