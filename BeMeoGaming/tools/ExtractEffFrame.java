import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.*;

/**
 * Cat 1 frame tu sheet ImgEffect_{id}.png (pack 3x3 hoac hang ngang) ra file rieng.
 * Dung: lay anh nguon ve icon (GenIcons3) cho item hao quang.
 * Usage: java ExtractEffFrame <sheet.png> <frameIndex> <out.png>
 */
public class ExtractEffFrame {
    public static void main(String[] a) throws Exception {
        BufferedImage img = ImageIO.read(Paths.get(a[0]).toFile());
        int fi = Integer.parseInt(a[1]);
        // sheet logic 216x240 (x1) -> scale theo tile; frame 72x80 * zoom
        int zoom = img.getWidth() / 216;
        if (zoom < 1) zoom = 1;
        int fw = 72 * zoom, fh = 80 * zoom, cols = 3;
        int x = (fi % cols) * fw, y = (fi / cols) * fh;
        BufferedImage out = img.getSubimage(x, y, fw, fh);
        BufferedImage copy = new BufferedImage(fw, fh, BufferedImage.TYPE_INT_ARGB);
        copy.getGraphics().drawImage(out, 0, 0, null);
        ImageIO.write(copy, "png", Paths.get(a[2]).toFile());
        System.out.println("frame " + fi + " " + fw + "x" + fh + " -> " + a[2]);
    }
}
