package main.java.com.pixolestudios.pdiff;

import main.java.com.pixolestudios.plogger.PLog;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("MagicNumber")
public class Diff {
    private Image image1;
    private Image image2;

    private BufferedImage diffImg = null;
    private static int diffA = 255;    // Alpha
    private static int diffR = 255;    // Red
    private static int diffG = 0;      // Green
    private static int diffB = 0;      // Blue
    // Color to highlight diffs in output image (default red)
    private static int diffColor = (diffA << 24) | (diffR << 16) | (diffG << 8) | diffB;

    /**
     * Constructor for Diff tool
     *
     * @param imgPath1 image to compare
     * @param imgPath2 secont image to compare to
     */
    public Diff(String imgPath1, String imgPath2) {
        image1 = new Image(imgPath1);
        image2 = new Image(imgPath2);
    }

    /**
     * Calculates the diff between the two images
     *
     * @param ignoreColor     if true, diffs only of color will be ignored
     * @param generateDiffImg if true, will generate a diff image
     * @param quitOnDiff      if true, will stop comparison at first diff found (can significantly increase performance)
     */
    public void CalculateDiff(boolean ignoreColor, boolean generateDiffImg, boolean quitOnDiff) {
        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
            PLog.info("different size images");
        } else {
            CompareImgs(ignoreColor, generateDiffImg, quitOnDiff);
        }
    }

    /**
     * Steps through each pixel and checks if pixels match between images
     * @param ignoreColor     if true, diffs only of color will be ignored
     * @param generateDiffImg if true, will generate a diff image
     * @param quitOnDiff      if true, will stop comparison at first diff found (can significantly increase performance)
     */
    private void CompareImgs(boolean ignoreColor, boolean generateDiffImg, boolean quitOnDiff) {
        if (generateDiffImg) {
            diffImg = new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        boolean foundDiffAtPixel;
        int numDiffs = 0;
        for (int i = 0; i < image1.getWidth(); i++) {
            foundDiffAtPixel = false;
            for (int j = 0; j < image1.getHeight(); j++) {
                foundDiffAtPixel = false;
                if (image1.getRGB(i, j) != image2.getRGB(i, j)) {
                    foundDiffAtPixel = true;
                    numDiffs++;
                }
                if (generateDiffImg) {
                    GenerateDiffImg(i, j, foundDiffAtPixel);
                }
                if (quitOnDiff && foundDiffAtPixel) break; //Stops if quitOnDiff flag is set
            }
            if (quitOnDiff && foundDiffAtPixel) break;
        }
        if (generateDiffImg) {
            try {
                ImageIO.write(diffImg, "png", new File("output.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PLog.info(CalcPercentageDiff(numDiffs, image1.getHeight() * image1.getWidth()) + "% diff");
        PLog.info(CalcPercentageMatch(numDiffs, image1.getHeight() * image1.getWidth()) + "% match");
        PLog.info(numDiffs + " pixels with diffs");
    }

    /**
     * Adds a pixel too the diff image
     * @param x x coordinate of the pixel
     * @param y y coordinate of the pixel
     * @param isDiffPixel if true, will draw pixel as diffColor instead of original color
     */
    private void GenerateDiffImg(int x, int y, boolean isDiffPixel) {
        if (!isDiffPixel) {
            diffImg.setRGB(x, y, Image.toRGB(image1.getAlpha(x, y) / 2,
                    image1.getRed(x, y),
                    image1.getGreen(x, y),
                    image1.getBlue(x, y)));
        } else {
            diffImg.setRGB(x, y, diffColor);
        }
    }

    /**
     * Calculates the percentage diff
     * @param numDiffPixels the number of diff pixels found
     * @param totalNoPixels the total number of pixels in the image
     * @return the percentage difference between the images
     */
    private float CalcPercentageDiff(int numDiffPixels, int totalNoPixels) {
        return (((float) numDiffPixels) / ((float) totalNoPixels)) * 100;
    }

    /**
     * Calculates the percentage diff
     * @param numDiffPixels the number of diff pixels found
     * @param totalNoPixels the total number of pixels in the image
     * @return the percentage matching between the images
     */
    private float CalcPercentageMatch(int numDiffPixels, int totalNoPixels) {
        return (((float) totalNoPixels - numDiffPixels) / ((float) totalNoPixels)) * 100;
    }

    public static int getDiffColor() {
        return diffColor;
    }

    public static void setDiffColor(int newDiffColor) {
        diffColor = newDiffColor;
    }
}
