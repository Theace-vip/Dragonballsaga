import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Tach con rong ra khoi nen cua anh AI (tools/res/dragon.png).
 * - Region-grow tu 4 canh anh: dan theo lang gieng mau giong (nen troi/may mem -> troi di het),
 *   dung lai o vien rong (do sang do/tim sac do khac manh).
 * - Giu thanh phan lien thong lon nhat cua "khong phai nen".
 * - Feather 2px alpha -> crop bbox.
 * Output: tools/res/dragon_cut.png
 * Usage: java CutDragon [tolerance]   (mac dinh 42)
 */
public class CutDragon {

    public static void main(String[] args) throws Exception {
        int tol = args.length > 0 ? Integer.parseInt(args[0]) : 42;
        File src = new File("tools/res/dragon.png");
        BufferedImage img = ImageIO.read(src);
        int w = img.getWidth(), h = img.getHeight();
        int[] px = img.getRGB(0, 0, w, h, null, 0, w);

        // region-grow tu cac o tren 4 canh
        boolean[] bg = new boolean[w * h];
        int[] qx = new int[w * h], qy = new int[w * h];
        int head = 0, tail = 0;
        for (int x = 0; x < w; x++) {
            if (!bg[x]) { bg[x] = true; qx[tail] = x; qy[tail] = 0; tail++; }
            int i = (h - 1) * w + x;
            if (!bg[i]) { bg[i] = true; qx[tail] = x; qy[tail] = h - 1; tail++; }
        }
        for (int y = 1; y < h - 1; y++) {
            if (!bg[y * w]) { bg[y * w] = true; qx[tail] = 0; qy[tail] = y; tail++; }
            int i = y * w + w - 1;
            if (!bg[i]) { bg[i] = true; qx[tail] = w - 1; qy[tail] = y; tail++; }
        }
        while (head < tail) {
            int x = qx[head], y = qy[head]; head++;
            int c = px[y * w + x];
            int[] dx = {1, -1, 0, 0}, dy = {0, 0, 1, -1};
            for (int k = 0; k < 4; k++) {
                int nx = x + dx[k], ny = y + dy[k];
                if (nx < 0 || ny < 0 || nx >= w || ny >= h) continue;
                int ni = ny * w + nx;
                if (bg[ni]) continue;
                if (close(c, px[ni], tol)) {
                    bg[ni] = true;
                    qx[tail] = nx; qy[tail] = ny; tail++;
                }
            }
        }

        // dem thanh phan lien thong cua "khong nen" (4-connect), giu lon nhat
        int[] comp = new int[w * h];
        int[] stack = new int[w * h];
        int bestId = 0, bestSize = 0, cid = 0;
        for (int i = 0; i < w * h; i++) {
            if (bg[i] || comp[i] != 0) continue;
            cid++;
            int sp = 0; stack[sp++] = i; comp[i] = cid; int size = 0;
            while (sp > 0) {
                int idx = stack[--sp]; size++;
                int x = idx % w, y = idx / w;
                int[] dx = {1, -1, 0, 0}, dy = {0, 0, 1, -1};
                for (int k = 0; k < 4; k++) {
                    int nx = x + dx[k], ny = y + dy[k];
                    if (nx < 0 || ny < 0 || nx >= w || ny >= h) continue;
                    int ni = ny * w + nx;
                    if (!bg[ni] && comp[ni] == 0) { comp[ni] = cid; stack[sp++] = ni; }
                }
            }
            if (size > bestSize) { bestSize = size; bestId = cid; }
        }

        // alpha: rong = comp lon nhat, feather 2px, xoa nen con lai
        int[] alpha = new int[w * h];
        for (int i = 0; i < w * h; i++) alpha[i] = (comp[i] == bestId) ? 255 : 0;
        for (int pass = 0; pass < 2; pass++) {
            int[] na = alpha.clone();
            for (int y = 1; y < h - 1; y++)
                for (int x = 1; x < w - 1; x++) {
                    int i = y * w + x;
                    if (na[i] == 0) continue;
                    int avg = (alpha[i] * 4 + alpha[i - 1] + alpha[i + 1]
                            + alpha[i - w] + alpha[i + w]) / 8;
                    if (avg < na[i]) na[i] = avg;
                }
            alpha = na;
        }

        // bbox
        int minX = w, minY = h, maxX = 0, maxY = 0, count = 0;
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                if (alpha[y * w + x] > 8) {
                    count++;
                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;
                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;
                }
        int cw = maxX - minX + 1, ch = maxY - minY + 1;
        BufferedImage out = new BufferedImage(cw, ch, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < ch; y++)
            for (int x = 0; x < cw; x++) {
                int i = (y + minY) * w + (x + minX);
                int c = px[i] & 0x00FFFFFF;
                int a = alpha[i] << 24;
                out.setRGB(x, y, c | a);
            }
        ImageIO.write(out, "png", new File("tools/res/dragon_cut.png"));
        System.out.println("tol=" + tol + " bg=" + (100 * (w * h - count) / (w * h))
                + "% kept=" + count + " bbox=" + cw + "x" + ch
                + " (comp " + bestSize + " tot " + w * h + ")");
    }

    static boolean close(int a, int b, int tol) {
        int dr = ((a >> 16) & 255) - ((b >> 16) & 255);
        int dg = ((a >> 8) & 255) - ((b >> 8) & 255);
        int db = (a & 255) - (b & 255);
        return Math.abs(dr) + Math.abs(dg) + Math.abs(db) <= tol;
    }
}
