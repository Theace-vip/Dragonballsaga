import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.*;

/**
 * Xem PNG bang ASCII de kiem tra art ma khong can browser.
 * Usage: java AsciiArt <png> [frameIndex] [cols]
 */
public class AsciiArt {
    public static void main(String[] a) throws Exception {
        BufferedImage img = ImageIO.read(Paths.get(a[0]).toFile());
        int fi = a.length > 1 ? Integer.parseInt(a[1]) : 0;
        int cols = a.length > 2 ? Integer.parseInt(a[2]) : 60;
        int FW = 72, FH = 80, COLS = 3;
        int fx = (fi % COLS) * FW, fy = (fi / COLS) * FH;
        int rows = cols * FH / FW / 2;
        System.out.println("frame " + fi + " of " + img.getWidth() + "x" + img.getHeight() + " (sheet), cell " + FW + "x" + FH);
        String ramp = " .:-=+*#%@";
        for (int r = 0; r < rows; r++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < cols; c++) {
                // sample block, max alpha*lum
                int x0 = fx + c * FW / cols, x1 = fx + (c + 1) * FW / cols;
                int y0 = fy + r * FH / rows, y1 = fy + (r + 1) * FH / rows;
                double best = 0;
                for (int y = y0; y < y1; y++)
                    for (int x = x0; x < x1; x++) {
                        int p = img.getRGB(x, y);
                        int al = (p >>> 24) & 255;
                        int lum = (((p >> 16) & 255) * 3 + ((p >> 8) & 255) * 6 + (p & 255)) / 10;
                        double v = al / 255.0 * (0.35 + 0.65 * lum / 255.0);
                        if (v > best) best = v;
                    }
                sb.append(ramp.charAt(Math.min(9, (int) (best * 9.99))));
            }
            System.out.println(sb);
        }
        // stats
        int nonZero = 0, solid = 0;
        long aSum = 0;
        for (int y = fy; y < fy + FH; y++)
            for (int x = fx; x < fx + FW; x++) {
                int al = (img.getRGB(x, y) >>> 24) & 255;
                aSum += al;
                if (al > 10) nonZero++;
                if (al > 240) solid++;
            }
        System.out.println("alpha>10: " + nonZero + "/" + (FW * FH) + ", alpha>240: " + solid + ", avg alpha: " + aSum / (FW * FH));
    }
}
