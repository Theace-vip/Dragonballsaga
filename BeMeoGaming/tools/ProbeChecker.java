import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

/**
 * Son nen checker da "nung" vao anh goc + do vet con lai sau khi cat.
 * - top mau o bien
 * - chu ky o (cell size) bang autocorrelation
 * - so pixel "giong nen" con lai trong anh da cat + ban do luoi
 * - do cao "ong than" theo cot (giua vs dai nhat)
 */
public class ProbeChecker {
    public static void main(String[] a) throws Exception {
        BufferedImage src = ImageIO.read(new File("tools/res/hdragon_src.png"));
        int w = src.getWidth(), h = src.getHeight();
        System.out.printf("src %dx%d type=%d%n", w, h, src.getType());

        HashMap<Integer, Integer> cnt = new HashMap<>();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < Math.min(40, w); x++)
                cnt.merge(src.getRGB(x, y) & 0xffffff, 1, Integer::sum);
        System.out.println("top border colors:");
        cnt.entrySet().stream().sorted((p, q) -> q.getValue() - p.getValue()).limit(12)
                .forEach(e -> System.out.printf("  #%06x %d%n", e.getKey(), e.getValue()));

        // chu ky checker: tim offset dx,dy khien mau giong nhau nhieu nhat tren vung bien
        int bx = 20, by = 20;
        int bestDx = -1, bestDy = -1, bestScore = -1;
        for (int d = 4; d <= 32; d++) {
            int s1 = 0, t1 = 0, s2 = 0, t2 = 0;
            for (int y = by; y < by + 80; y++)
                for (int x = bx; x < bx + 200; x++) {
                    if ((src.getRGB(x, y) & 0xffffff) == (src.getRGB(x + d, y) & 0xffffff)) s1++;
                    t1++;
                    if ((src.getRGB(x, y) & 0xffffff) == (src.getRGB(x, y + d) & 0xffffff)) s2++;
                    t2++;
                }
            if (s1 > bestScore) { bestScore = s1; bestDx = d; bestDy = -1; }
            if (s2 > bestScore) { bestScore = s2; bestDx = -1; bestDy = d; }
        }
        System.out.printf("cell hint: dx=%d dy=%d match=%d/16000%n", bestDx, bestDy, bestScore);

        // pixel "giong nen" con lai trong anh da cat
        BufferedImage cut = ImageIO.read(new File("data/anh_the/hdragon_cut.png"));
        int cw = cut.getWidth(), ch = cut.getHeight();
        int GX = 32, GY = 16;
        int[][] grid = new int[GY][GX];
        int remain = 0, total = 0;
        for (int y = 0; y < ch; y++)
            for (int x = 0; x < cw; x++) {
                int p = cut.getRGB(x, y);
                int al = (p >>> 24) & 255;
                if (al == 0) continue;
                total++;
                int r = (p >> 16) & 255, g = (p >> 8) & 255, b = p & 255;
                int mx = Math.max(r, Math.max(g, b)), mn = Math.min(r, Math.min(g, b));
                if (mx - mn <= 16 && (r + g + b) / 3 >= 190) {
                    remain++;
                    grid[y * GY / ch][x * GX / cw]++;
                }
            }
        System.out.printf("cut: %d px con mau, %d px giong nen (%.2f%%)%n", total, remain,
                100.0 * remain / Math.max(1, total));
        System.out.println("vi tri vet nen con lai (luoi " + GX + "x" + GY + ", so = so px/100):");
        for (int j = 0; j < GY; j++) {
            StringBuilder sb = new StringBuilder("  ");
            for (int i = 0; i < GX; i++) {
                int v = grid[j][i] / 100;
                sb.append(v == 0 ? '.' : (char) ('0' + Math.min(9, v)));
            }
            System.out.println(sb);
        }

        // do cao "ong than" moi cot: do dai run dai nhat co alpha>40
        int[] runs = new int[w];
        int[] center = new int[w];
        int sum = 0, n = 0;
        for (int x = 0; x < w; x++) {
            int best = 0, cur = 0, start = 0, bestStart = 0;
            for (int y = 0; y < h; y++) {
                int al = (cut.getRGB(Math.min(x, cw - 1), Math.min(y, ch - 1)) >>> 24) & 255;
                if (al > 40) {
                    if (cur == 0) start = y;
                    cur++;
                    if (cur > best) { best = cur; bestStart = start; }
                } else cur = 0;
            }
            runs[x] = best;
            center[x] = bestStart + best / 2;
            if (best > 0) { sum += best; n++; }
        }
        System.out.printf("ong than TB=%d px (max=%d, min=%d) tren %d cot co noi dung%n",
                n == 0 ? 0 : sum / n, max(runs), minPos(runs), n);
        StringBuilder prof = new StringBuilder("profile (1 ky tu = " + Math.max(1, w / 100) + "px): ");
        int step = Math.max(1, w / 100);
        for (int x = 0; x < w; x += step)
            prof.append(runs[x] == 0 ? '.' : (char) ('0' + Math.min(9, runs[x] / 40)));
        System.out.println(prof);
        StringBuilder cen = new StringBuilder("center y (0=top,9=bot):          ");
        for (int x = 0; x < w; x += step)
            cen.append(runs[x] == 0 ? '.' : (char) ('0' + Math.min(9, center[x] * 10 / h)));
        System.out.println(cen);
    }

    static int max(int[] a) {
        int m = 0;
        for (int v : a) m = Math.max(m, v);
        return m;
    }

    static int minPos(int[] a) {
        int m = Integer.MAX_VALUE;
        for (int v : a) if (v > 0) m = Math.min(m, v);
        return m == Integer.MAX_VALUE ? 0 : m;
    }
}
