import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class InspectImg {
    static int px(BufferedImage img, int x, int y) {
        return img.getRGB(x, y);
    }

    public static void main(String[] a) throws Exception {
        for (String f : a) {
            BufferedImage img = ImageIO.read(new File(f));
            if (img == null) { System.out.println(f + " -> DOC LOI"); continue; }
            int w = img.getWidth(), h = img.getHeight();
            boolean alpha = img.getColorModel().hasAlpha();
            // mau 4 goc
            int[] c = { px(img, 0, 0), px(img, w - 1, 0), px(img, 0, h - 1), px(img, w - 1, h - 1) };
            // dem mau goc (4 goc + 20 diem tren canh)
            java.util.Map<Integer, Integer> border = new java.util.HashMap<>();
            for (int x = 0; x < w; x += Math.max(1, w / 40)) {
                for (int y : new int[]{0, h - 1}) {
                    int p = px(img, x, y) & 0xFFFFFF;
                    border.merge(p, 1, Integer::sum);
                }
            }
            for (int y = 0; y < h; y += Math.max(1, h / 40)) {
                for (int x : new int[]{0, w - 1}) {
                    int p = px(img, x, y) & 0xFFFFFF;
                    border.merge(p, 1, Integer::sum);
                }
            }
            int topColor = -1, topCount = 0, distinct = border.size();
            for (var e : border.entrySet()) if (e.getValue() > topCount) { topCount = e.getValue(); topColor = e.getKey(); }
            // ty le pixel giong mau dominant tai bien (neu >90% -> nen dong nhat)
            int total = 0; for (var e : border.entrySet()) total += e.getValue();
            double domPct = 100.0 * topCount / total;
            // dem so pixel alpha=0 neu co alpha
            int transparent = 0;
            if (alpha) {
                for (int x = 0; x < w; x += 4) for (int y = 0; y < h; y += 4)
                    if (((img.getRGB(x, y) >>> 24) & 0xFF) < 16) transparent++;
            }
            // bounding box cua pixel KHAC mau dominant (threshold 30)
            int tr = (topColor >> 16) & 0xFF, tg = (topColor >> 8) & 0xFF, tb = topColor & 0xFF;
            int minX = w, minY = h, maxX = -1, maxY = -1;
            long diff = 0, sample = 0;
            int step = Math.max(1, Math.min(w, h) / 200);
            for (int x = 0; x < w; x += step) for (int y = 0; y < h; y += step) {
                int p = px(img, x, y);
                int pr = (p >> 16) & 0xFF, pg = (p >> 8) & 0xFF, pb = p & 0xFF;
                boolean isAlpha = alpha && ((p >>> 24) & 0xFF) < 16;
                sample++;
                if (isAlpha || Math.abs(pr - tr) + Math.abs(pg - tg) + Math.abs(pb - tb) > 60) {
                    diff++;
                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;
                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;
                }
            }
            System.out.println(f);
            System.out.println("  size=" + w + "x" + h + " alpha=" + alpha
                    + " goc=(" + hex(c[0]) + "," + hex(c[1]) + "," + hex(c[2]) + "," + hex(c[3]) + ")");
            System.out.println("  nenBien: mauDominant=" + hex(topColor) + " tyLe=" + String.format("%.0f%%", domPct)
                    + " soMauDacBiet=" + distinct + (alpha ? (" transparent~" + transparent) : ""));
            System.out.println("  noiDung bbox=(" + minX + "," + minY + ")-(" + maxX + "," + maxY + ") tyLeKhacNen="
                    + String.format("%.1f%%", 100.0 * diff / sample));
        }
    }

    static String hex(int p) {
        return String.format("#%06X", p & 0xFFFFFF);
    }
}
