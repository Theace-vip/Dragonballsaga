import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/** Kiem tra nhanh alpha cua anh icon: goc/tam/bien + ty le pixel trong suot. */
public class AlphaCheck {
    public static void main(String[] args) throws Exception {
        for (String p : args) {
            BufferedImage im = ImageIO.read(new File(p));
            int W = im.getWidth(), H = im.getHeight();
            long trong = 0;
            for (int y = 0; y < H; y++) {
                for (int x = 0; x < W; x++) {
                    if (((im.getRGB(x, y) >>> 24) & 0xFF) <= 8) {
                        trong++;
                    }
                }
            }
            System.out.printf("%s %dx%d | goc(0,0) alpha=%d | (W-1,0)=%d | tam=%d | trong suot %.1f%%%n",
                    new File(p).getName(), W, H,
                    (im.getRGB(0, 0) >>> 24), (im.getRGB(W - 1, 0) >>> 24), (im.getRGB(W / 2, H / 2) >>> 24),
                    100.0 * trong / ((long) W * H));
        }
    }
}
