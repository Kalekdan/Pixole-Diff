package main.java.com.pixolestudios.pdiff;

public class PixoleDiffMain {
    private PixoleDiffMain() {
    }

    public static void main(String[] args){
        Diff imgdiff = new Diff("smoothedHeightMap.png", "smoothedHeightMap_orig.png");
        imgdiff.CalculateDiff(true, true);
    }
}
