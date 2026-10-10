import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;

/**
 * GenItemIcon - tao icon vat pham 3 zoom (x2=32, x3=48, x4=64) tu 1 anh goc.
 *
 * Quy trinh: cat vien trong suot -> thu nho dan 1/2 (progressive halving, net nhu
 * cach GenCaitrang lam) -> buoc cuoi dua vao box, giu ti le + can giua.
 *
 * Chay trong thu muc BeMeoGaming:
 *   java GenItemIcon <anh_goc.png> <icon_id>
 */
public class GenItemIcon {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Dung: java GenItemIcon <anh_goc.png> <icon_id>");
            return;
        }
        File in = new File(args[0]);
        int id = Integer.parseInt(args[1]);
        BufferedImage src = ImageIO.read(in);
        if (src == null) {
            throw new IllegalArgumentException("Khong doc duoc anh: " + in);
        }
        System.out.printf("Anh goc: %s  %dx%d%n", in.getPath(), src.getWidth(), src.getHeight());
        src = ensureArgb(src);
        BufferedImage cut = trim(src);
        System.out.printf("Sau khi cat vien trong suot: %dx%d%n", cut.getWidth(), cut.getHeight());

        int[][] zooms = {{2, 32}, {3, 48}, {4, 64}};
        for (int[] z : zooms) {
            BufferedImage out = fitSquare(cut, z[1]);
            File f = new File("data/icon_botnet/x" + z[0] + "/" + id + ".png");
            Files.createDirectories(f.getParentFile().toPath());
            ImageIO.write(out, "png", f);
            System.out.printf("  -> %s (%dx%d, %d bytes)%n", f.getPath(), out.getWidth(), out.getHeight(), f.length());
        }
    }

    static BufferedImage ensureArgb(BufferedImage s) {
        if (s.getType() == BufferedImage.TYPE_INT_ARGB) {
            return s;
        }
        BufferedImage o = new BufferedImage(s.getWidth(), s.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = o.createGraphics();
        g.drawImage(s, 0, 0, null);
        g.dispose();
        return o;
    }

    /** Cat vien trong suot (alpha <= 8). Anh khong co pixel nao -> giu nguyen. */
    static BufferedImage trim(BufferedImage im) {
        int W = im.getWidth(), H = im.getHeight();
        int x0 = W, y0 = H, x1 = -1, y1 = -1;
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                if (((im.getRGB(x, y) >>> 24) & 0xFF) > 8) {
                    if (x < x0) x0 = x;
                    if (x > x1) x1 = x;
                    if (y < y0) y0 = y;
                    if (y > y1) y1 = y;
                }
            }
        }
        if (x1 < 0) {
            return im;
        }
        BufferedImage o = new BufferedImage(x1 - x0 + 1, y1 - y0 + 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = o.createGraphics();
        g.drawImage(im, 0, 0, o.getWidth(), o.getHeight(), x0, y0, x1 + 1, y1 + 1, null);
        g.dispose();
        return o;
    }

    /** Thu nho dan 1/2 roi dua ve box x box, giu ti le va can giua. */
    static BufferedImage fitSquare(BufferedImage src, int box) {
        BufferedImage cur = src;
        while (cur.getWidth() > box * 2 && cur.getHeight() > box * 2) {
            cur = scale(cur, Math.max(1, cur.getWidth() / 2), Math.max(1, cur.getHeight() / 2));
        }
        double s = Math.min(box / (double) cur.getWidth(), box / (double) cur.getHeight());
        int tw = Math.max(1, (int) Math.round(cur.getWidth() * s));
        int th = Math.max(1, (int) Math.round(cur.getHeight() * s));
        BufferedImage scaled = scale(cur, tw, th);
        BufferedImage o = new BufferedImage(box, box, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = o.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(scaled, (box - tw) / 2, (box - th) / 2, null);
        g.dispose();
        return o;
    }

    static BufferedImage scale(BufferedImage src, int w, int h) {
        BufferedImage o = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = o.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(src, 0, 0, w, h, null);
        g.dispose();
        return o;
    }
}
