import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Cat nen checkerboard bi "nung" vao anh rong ngang.
 * Output: tools/res/hdragon.png (alpha that) + data/anh_the/hdragon_cut.png (preview tren nen toi).
 *
 * Buoc 1: flood tu bien qua pixel "chac chan la nen" (xam/trang nhiet do).
 * Buoc 2: vung nen bi bao kin ben trong rong (giua chan, lung...) -> xoa het.
 * Buoc 3: mo rong them 3 vong vao pixel lech nen nho (vet soi checker do resize sinh ra)
 *         nhung chi khi no ke sat voi vung da xoa.
 * Buoc 4: day lai cac lo MONG ben trong than (o che giua bang buong vs rong) bang mau
 *         o lan can -> than rong khong bi vet thung. Lo rong van giu trong.
 * Buoc 5: alpha that, mep mem 1px.
 */
public class CutChecker {
    static int w, h;
    static BufferedImage src;

    /** Nen chac chan: xam/trang nhiet do, do sang cao. */
    static boolean isBg(int p) {
        int r = (p >> 16) & 255, g = (p >> 8) & 255, b = p & 255;
        int mx = Math.max(r, Math.max(g, b)), mn = Math.min(r, Math.min(g, b));
        return mx - mn <= 14 && (r + g + b) / 3 >= 200 && Math.abs(r - g) <= 10 && Math.abs(g - b) <= 10;
    }

    /** Nen lech: cho phep xam troi/lech mau it do resize sinh ra. */
    static boolean isBgLoose(int p) {
        int r = (p >> 16) & 255, g = (p >> 8) & 255, b = p & 255;
        int mx = Math.max(r, Math.max(g, b)), mn = Math.min(r, Math.min(g, b));
        return mx - mn <= 24 && (r + g + b) / 3 >= 185 && Math.abs(r - g) <= 16 && Math.abs(g - b) <= 16;
    }

    public static void main(String[] a) throws Exception {
        src = ImageIO.read(new File("tools/res/hdragon_src.png"));
        w = src.getWidth();
        h = src.getHeight();
        boolean[] bg = new boolean[w * h];
        System.out.printf("src %dx%d%n", w, h);

        // Buoc 1: flood tu bien
        ArrayDeque<Integer> q = new ArrayDeque<>();
        for (int x = 0; x < w; x++) { seed(q, bg, x, 0); seed(q, bg, x, h - 1); }
        for (int y = 0; y < h; y++) { seed(q, bg, 0, y); seed(q, bg, w - 1, y); }
        int flood = 0;
        while (!q.isEmpty()) {
            int idx = q.poll();
            flood++;
            int x = idx % w, y = idx / w;
            if (x > 0) seed(q, bg, x - 1, y);
            if (x < w - 1) seed(q, bg, x + 1, y);
            if (y > 0) seed(q, bg, x, y - 1);
            if (y < h - 1) seed(q, bg, x, y + 1);
        }

        // Buoc 2: vung nen chac chan bi bao kin -> xoa het
        boolean[] vis = new boolean[w * h];
        int holes = 0, holePx = 0;
        for (int i = 0; i < w * h; i++) {
            if (bg[i] || vis[i] || !isBg(src.getRGB(i % w, i / w))) continue;
            ArrayDeque<Integer> comp = new ArrayDeque<>();
            comp.add(i);
            vis[i] = true;
            int cnt = 0;
            while (!comp.isEmpty()) {
                int idx = comp.poll();
                int x = idx % w, y = idx / w;
                cnt++;
                int[] nx = {x - 1, x + 1, x, x}, ny = {y, y, y - 1, y + 1};
                for (int k = 0; k < 4; k++) {
                    int xx = nx[k], yy = ny[k];
                    if (xx < 0 || xx >= w || yy < 0 || yy >= h) continue;
                    int j = yy * w + xx;
                    if (vis[j] || bg[j]) continue;
                    if (!isBg(src.getRGB(xx, yy))) continue;
                    vis[j] = true;
                    comp.add(j);
                }
            }
            holes++;
            holePx += cnt;
        }
        for (int i = 0; i < w * h; i++) if (vis[i]) bg[i] = true;
        System.out.printf("buoc1 flood=%d px; buoc2 vung nen bao kin=%d / %d px%n", flood, holes, holePx);

        // Buoc 3: mo rong 3 vong vao pixel lech nen ke sat vung da xoa
        int grown = 0;
        for (int pass = 0; pass < 3; pass++) {
            boolean[] add = new boolean[w * h];
            for (int y = 0; y < h; y++)
                for (int x = 0; x < w; x++) {
                    int idx = y * w + x;
                    if (bg[idx]) continue;
                    if (nearBg(bg, x, y) && isBgLoose(src.getRGB(x, y))) add[idx] = true;
                }
            int n = 0;
            for (int i = 0; i < w * h; i++) if (add[i]) { bg[i] = true; n++; }
            grown += n;
            if (n == 0) break;
        }
        System.out.printf("buoc3 mo rong them %d px (vet soi checker)%n", grown);

        // Buoc 4: day lai lo mong ben trong than
        int filled = fillThinHoles(bg, 14);
        System.out.printf("buoc4 day lai %d px lo mong ben trong than%n", filled);

        // Buoc 5: alpha that + mep mem 1px
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        int[] alpha = new int[w * h];
        for (int i = 0; i < w * h; i++) alpha[i] = bg[i] ? 0 : 255;
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                int idx = y * w + x;
                if (alpha[idx] == 0) continue;
                if (nearBg(bg, x, y)) alpha[idx] = 170;
            }
        int minX = w, maxX = -1, minY = h, maxY = -1, content = 0;
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                int idx = y * w + x;
                int al = alpha[idx];
                if (al == 0) continue;
                int p = src.getRGB(x, y);
                out.setRGB(x, y, (al << 24) | (p & 0xffffff));
                content++;
                minX = Math.min(minX, x); maxX = Math.max(maxX, x);
                minY = Math.min(minY, y); maxY = Math.max(maxY, y);
            }
        System.out.printf("noi dung=%d px, bbox=(%d,%d)-(%d,%d) %dx%d%n",
                content, minX, minY, maxX, maxY, maxX - minX + 1, maxY - minY + 1);
        new File("tools/res").mkdirs();
        ImageIO.write(out, "png", new File("tools/res/hdragon.png"));

        BufferedImage prev = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = prev.createGraphics();
        g.setColor(new Color(0x101014));
        g.fillRect(0, 0, w, h);
        g.drawImage(out, 0, 0, null);
        g.dispose();
        ImageIO.write(prev, "png", new File("data/anh_the/hdragon_cut.png"));
        System.out.println("-> tools/res/hdragon.png + data/anh_the/hdragon_cut.png");
    }

    /**
     * Tim lo ben trong (khong ra duoc bien) va day bang mau o lan can neu lo MONG
     * (xoi erodePass lan ma van con -> lo rong, giu trong).
     */
    static int fillThinHoles(boolean[] bg, int erodePass) {
        boolean[] outer = new boolean[w * h];
        ArrayDeque<Integer> q = new ArrayDeque<>();
        for (int x = 0; x < w; x++) { addOuter(q, outer, bg, x, 0); addOuter(q, outer, bg, x, h - 1); }
        for (int y = 0; y < h; y++) { addOuter(q, outer, bg, 0, y); addOuter(q, outer, bg, w - 1, y); }
        while (!q.isEmpty()) {
            int idx = q.poll();
            int x = idx % w, y = idx / w;
            addOuter(q, outer, bg, x - 1, y); addOuter(q, outer, bg, x + 1, y);
            addOuter(q, outer, bg, x, y - 1); addOuter(q, outer, bg, x, y + 1);
        }
        boolean[] hole = new boolean[w * h];
        for (int i = 0; i < w * h; i++) hole[i] = bg[i] && !outer[i];

        boolean[] seen = new boolean[w * h];
        boolean[] filledAll = new boolean[w * h];
        int total = 0;
        for (int i = 0; i < w * h; i++) {
            if (!hole[i] || seen[i]) continue;
            ArrayList<Integer> members = new ArrayList<>();
            ArrayDeque<Integer> comp = new ArrayDeque<>();
            comp.add(i); seen[i] = true; members.add(i);
            while (!comp.isEmpty()) {
                int idx = comp.poll();
                int x = idx % w, y = idx / w;
                int[] nx = {x - 1, x + 1, x, x}, ny = {y, y, y - 1, y + 1};
                for (int k = 0; k < 4; k++) {
                    int xx = nx[k], yy = ny[k];
                    if (xx < 0 || xx >= w || yy < 0 || yy >= h) continue;
                    int j = yy * w + xx;
                    if (seen[j] || !hole[j]) continue;
                    seen[j] = true; comp.add(j); members.add(j);
                }
            }

            boolean[] cur = new boolean[w * h];
            for (int idx : members) cur[idx] = true;
            boolean thin = members.size() == 1;
            for (int p = 0; p < erodePass && !thin; p++) {
                boolean any = false;
                boolean[] nxt = new boolean[w * h];
                for (int idx : members) {
                    if (!cur[idx]) continue;
                    int x = idx % w, y = idx / w;
                    if (x > 0 && x < w - 1 && y > 0 && y < h - 1
                            && cur[idx - 1] && cur[idx + 1] && cur[idx - w] && cur[idx + w]) {
                        nxt[idx] = true;
                        any = true;
                    }
                }
                if (!any) { thin = true; break; }
                cur = nxt;
            }
            if (!thin) continue;

            boolean[] inComp = new boolean[w * h];
            for (int idx : members) inComp[idx] = true;
            boolean[] done = new boolean[w * h];
            boolean changed = true;
            while (changed) {
                changed = false;
                for (int idx : members) {
                    if (done[idx]) continue;
                    int x = idx % w, y = idx / w;
                    int r = 0, g = 0, b = 0, n = 0;
                    int[] nx = {x - 1, x + 1, x, x}, ny = {y, y, y - 1, y + 1};
                    for (int k = 0; k < 4; k++) {
                        int xx = nx[k], yy = ny[k];
                        if (xx < 0 || xx >= w || yy < 0 || yy >= h) continue;
                        int j = yy * w + xx;
                        if (!filledAll[j] && hole[j]) continue;   // chi lay mau "that"
                        int p = src.getRGB(xx, yy);
                        r += (p >> 16) & 255; g += (p >> 8) & 255; b += p & 255; n++;
                    }
                    if (n == 0) continue;
                    src.setRGB(x, y, 0xff000000 | (r / n << 16) | (g / n << 8) | (b / n));
                    done[idx] = true;
                    filledAll[idx] = true;
                    bg[idx] = false;      // giu lai noi dung
                    changed = true;
                    total++;
                }
            }
        }
        return total;
    }

    static void addOuter(ArrayDeque<Integer> q, boolean[] outer, boolean[] bg, int x, int y) {
        if (x < 0 || x >= w || y < 0 || y >= h) return;
        int idx = y * w + x;
        if (outer[idx] || !bg[idx]) return;
        outer[idx] = true;
        q.add(idx);
    }

    static boolean nearBg(boolean[] bg, int x, int y) {
        if (x > 0 && bg[y * w + x - 1]) return true;
        if (x < w - 1 && bg[y * w + x + 1]) return true;
        if (y > 0 && bg[(y - 1) * w + x]) return true;
        if (y < h - 1 && bg[(y + 1) * w + x]) return true;
        return false;
    }

    static void seed(ArrayDeque<Integer> q, boolean[] bg, int x, int y) {
        int idx = y * w + x;
        if (bg[idx]) return;
        if (!isBg(src.getRGB(x, y))) return;
        bg[idx] = true;
        q.add(idx);
    }
}
