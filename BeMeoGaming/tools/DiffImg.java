import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/** So sanh 2 anh PNG, ve mask (do) cho pixel khac nhau hon nguong. */
public class DiffImg {
    public static void main(String[] a) throws Exception {
        BufferedImage i1 = ImageIO.read(new File(a[0]));
        BufferedImage i2 = ImageIO.read(new File(a[1]));
        int w = i1.getWidth(), h = i1.getHeight();
        int[] p1 = new int[w * h], p2 = new int[w * h];
        i1.getRGB(0, 0, w, h, p1, 0, w);
        i2.getRGB(0, 0, w, h, p2, 0, w);
        int th = a.length > 2 ? Integer.parseInt(a[2]) : 12;
        int n = 0;
        int[] out = new int[w * h];
        for (int i = 0; i < w * h; i++) {
            int x1 = p1[i], x2 = p2[i];
            int dr = Math.abs(((x1 >> 16) & 255) - ((x2 >> 16) & 255));
            int dg = Math.abs(((x1 >> 8) & 255) - ((x2 >> 8) & 255));
            int db = Math.abs((x1 & 255) - (x2 & 255));
            int da = Math.abs(((x1 >>> 24) & 255) - ((x2 >>> 24) & 255));
            if (dr > th || dg > th || db > th || da > th) { out[i] = 0xffff4040; n++; }
        }
        BufferedImage o = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        o.setRGB(0, 0, w, h, out, 0, w);
        ImageIO.write(o, "png", new File(a.length > 3 ? a[3] : "data/anh_the/dbg_diff.png"));
        System.out.printf("khac %d pixel (%.2f%%)%n", n, 100.0 * n / (w * h));
    }
}
