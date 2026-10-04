import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Tach lop tu anh hao quang Goku (tools/res/goku_src.png, da co alpha that):
 *   - base  = anh goc sau khi xoa cac thanh phan "da bay" va "tia set" + inpaint (lan can TB)
 *   - atlas = sprite rieng cho tung thanh phan de generator di chuyen/no bay tung frame
 *   - meta  = toa do + kich thuoc moi sprite
 *
 * Bo loc:
 *   - tia set: mau sang (bri cao, sac thap) - thanh phan mong/keo dai
 *   - da bay : mau toi tim/tim (bri thap) - thanh phan khoi khoi
 *   - khong cham vao vung dau/mat Goku (HEAD_RECT) va khong cham than (BODY_RECT)
 *
 * Output: tools/res/goku_base.png, tools/res/goku_layers.png, tools/res/goku_layers.txt
 *         + anh debug data/anh_the/dbg_bolt.png, dbg_rock.png, dbg_base.png
 */
public class SplitGokuAura {

    static int W, H;
    static int[] src;
    static boolean[] cleared;

    // Vung bao ve Goku (khong tach thanh phan nao nam trong do)
    static final int HEAD_X0 = 350, HEAD_Y0 = 55, HEAD_X1 = 715, HEAD_Y1 = 350;
    static final int BODY_X0 = 330, BODY_Y0 = 70, BODY_X1 = 785, BODY_Y1 = 1335;

    // Nguong loc thanh phan
    static final int BOLT_BRI = 224;    // do sang toi thieu cua tia set
    static final int BOLT_SAT = 90;     // sac toi da (trang / xanh nhat)
    static final int ROCK_BRI = 118;    // do sang toi da cua da
    static final int MIN_AREA = 40;
    static final int MAX_BOLT_AREA = 9000;
    static final int MAX_ROCK_AREA = 26000;

    static final class Comp {
        int area, x0, y0, x1, y1;
        boolean rock;
        int sumBri, sumR, sumG, sumB;
    }

    public static void main(String[] a) throws Exception {
        BufferedImage im = ImageIO.read(new File(a.length > 0 ? a[0] : "tools/res/goku_src.png"));
        W = im.getWidth();
        H = im.getHeight();
        src = new int[W * H];
        im.getRGB(0, 0, W, H, src, 0, W);
        cleared = new boolean[W * H];
        System.out.printf("src %dx%d%n", W, H);

        boolean[] mask = new boolean[W * H];
        int nBolt = 0, nRock = 0;
        for (int i = 0; i < W * H; i++) {
            int p = src[i];
            int al = (p >>> 24) & 255;
            if (al < 70) continue;
            int r = (p >> 16) & 255, g = (p >> 8) & 255, b = p & 255;
            int bri = (r + g + b) / 3;
            int mx = Math.max(r, Math.max(g, b)), mn = Math.min(r, Math.min(g, b));
            int sat = mx - mn;
            if (bri >= BOLT_BRI && sat <= BOLT_SAT) { mask[i] = true; nBolt++; }
            else if (bri <= ROCK_BRI && b >= g && b - g >= 8 && sat <= 130) { mask[i] = true; nRock++; }
        }
        System.out.printf("pixel tia set=%d, pixel da=%d%n", nBolt, nRock);

        // thanh phan ket noi (4-hop)
        boolean[] seen = new boolean[W * H];
        ArrayList<Comp> all = new ArrayList<>();
        ArrayList<ArrayList<Integer>> compPx = new ArrayList<>();
        for (int i = 0; i < W * H; i++) {
            if (!mask[i] || seen[i]) continue;
            ArrayDeque<Integer> q = new ArrayDeque<>();
            q.add(i); seen[i] = true;
            ArrayList<Integer> px = new ArrayList<>();
            Comp c = new Comp();
            c.x0 = W; c.y0 = H; c.x1 = -1; c.y1 = -1;
            while (!q.isEmpty()) {
                int idx = q.poll();
                px.add(idx);
                int x = idx % W, y = idx / W;
                c.area++;
                if (x < c.x0) c.x0 = x;
                if (x > c.x1) c.x1 = x;
                if (y < c.y0) c.y0 = y;
                if (y > c.y1) c.y1 = y;
                int p = src[idx];
                int r = (p >> 16) & 255, g = (p >> 8) & 255, b = p & 255;
                c.sumR += r; c.sumG += g; c.sumB += b;
                c.sumBri += (r + g + b) / 3;
                int[] nx = {x - 1, x + 1, x, x}, ny = {y, y, y - 1, y + 1};
                for (int k = 0; k < 4; k++) {
                    int xx = nx[k], yy = ny[k];
                    if (xx < 0 || xx >= W || yy < 0 || yy >= H) continue;
                    int j = yy * W + xx;
                    if (seen[j] || !mask[j]) continue;
                    seen[j] = true;
                    q.add(j);
                }
            }
            c.rock = (c.sumBri / c.area) < (BOLT_BRI + ROCK_BRI) / 2;
            all.add(c);
            compPx.add(px);
        }

        // loc + phan loai
        ArrayList<Integer> pick = new ArrayList<>();
        int skipHead = 0, skipBody = 0, skipSize = 0, tooSmall = 0;
        for (int k = 0; k < all.size(); k++) {
            Comp c = all.get(k);
            if (c.area < MIN_AREA) { tooSmall++; continue; }
            int bw = c.x1 - c.x0 + 1, bh = c.y1 - c.y0 + 1;
            if (c.rock) {
                if (c.area > MAX_ROCK_AREA) { skipSize++; continue; }
            } else {
                if (c.area > MAX_BOLT_AREA) { skipSize++; continue; }
                if (intersects(c, HEAD_X0, HEAD_Y0, HEAD_X1, HEAD_Y1)) { skipHead++; continue; }
            }
            if (intersects(c, BODY_X0, BODY_Y0, BODY_X1, BODY_Y1)) { skipBody++; continue; }
            pick.add(k);
        }
        System.out.printf("component=%d, chon=%d (bo qua: nho=%d, qua lon=%d, vung dau=%d, vung than=%d)%n",
                all.size(), pick.size(), tooSmall, skipSize, skipHead, skipBody);

        // in top thanh phan chon
        System.out.println("top sprite (type/area/bbox/meanBri):");
        pick.stream().sorted((p, q) -> all.get(q).area - all.get(p).area).limit(25)
                .forEach(k -> {
                    Comp c = all.get(k);
                    System.out.printf("  %s %5d (%d,%d)-(%d,%d) %dx%d bri=%d%n",
                            c.rock ? "DA " : "SET", c.area, c.x0, c.y0, c.x1, c.y1,
                            c.x1 - c.x0 + 1, c.y1 - c.y0 + 1, c.sumBri / c.area);
                });

        // xoa thanh phan chon khoi base
        for (int k : pick)
            for (int idx : compPx.get(k)) cleared[idx] = true;

        // inpaint: dien mau + alpha tu o lan lan den khi het lo
        int filled = inpaint();
        System.out.printf("inpaint %d pixel (lo da xoa)%n", filled);

        BufferedImage base = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        base.setRGB(0, 0, W, H, src, 0, W);
        new File("tools/res").mkdirs();
        ImageIO.write(base, "png", new File("tools/res/goku_base.png"));

        // atlas + meta
        ArrayList<Integer> chosen = new ArrayList<>(pick);
        int atlasH = 0, atlasW = 0;
        for (int k : chosen) {
            Comp c = all.get(k);
            atlasW = Math.max(atlasW, c.x1 - c.x0 + 1);
            atlasH += c.y1 - c.y0 + 1;
        }
        BufferedImage atlas = new BufferedImage(Math.max(1, atlasW), Math.max(1, atlasH),
                BufferedImage.TYPE_INT_ARGB);
        StringBuilder meta = new StringBuilder();
        meta.append(W).append(' ').append(H).append(' ').append(chosen.size()).append('\n');
        int ay = 0;
        for (int k : chosen) {
            Comp c = all.get(k);
            int bw = c.x1 - c.x0 + 1, bh = c.y1 - c.y0 + 1;
            for (int idx : compPx.get(k)) {
                int x = idx % W, y = idx / W;
                atlas.setRGB(x - c.x0, ay + (y - c.y0), src[idx]);
            }
            meta.append(c.rock ? 1 : 0).append(' ')
                .append(c.x0).append(' ').append(c.y0).append(' ')
                .append(bw).append(' ').append(bh).append(' ')
                .append(ay).append('\n');
            ay += bh;
        }
        ImageIO.write(atlas, "png", new File("tools/res/goku_layers.png"));
        Files.write(new File("tools/res/goku_layers.txt").toPath(),
                meta.toString().getBytes(StandardCharsets.UTF_8));
        System.out.printf("-> base %dx%d, atlas %dx%d, sprite=%d%n",
                W, H, atlas.getWidth(), atlas.getHeight(), chosen.size());

        debugMasks(all, pick);
    }

    static boolean intersects(Comp c, int x0, int y0, int x1, int y1) {
        return c.x0 <= x1 && c.x1 >= x0 && c.y0 <= y1 && c.y1 >= y0;
    }

    static int inpaint() {
        boolean[] done = new boolean[W * H];
        int filled = 0;
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int y = 0; y < H; y++)
                for (int x = 0; x < W; x++) {
                    int idx = y * W + x;
                    if (!cleared[idx] || done[idx]) continue;
                    int r = 0, g = 0, b = 0, al = 0, n = 0;
                    int[] nx = {x - 1, x + 1, x, x}, ny = {y, y, y - 1, y + 1};
                    for (int k = 0; k < 4; k++) {
                        int xx = nx[k], yy = ny[k];
                        if (xx < 0 || xx >= W || yy < 0 || yy >= H) continue;
                        int j = yy * W + xx;
                        if (cleared[j] && !done[j]) continue;
                        int p = src[j];
                        r += (p >> 16) & 255; g += (p >> 8) & 255; b += p & 255;
                        al += (p >>> 24) & 255;
                        n++;
                    }
                    if (n == 0) continue;
                    src[idx] = (Math.min(255, al / n) << 24) | (r / n << 16) | (g / n << 8) | (b / n);
                    done[idx] = true;
                    filled++;
                    changed = true;
                }
        }
        return filled;
    }

    static void debugMasks(ArrayList<Comp> all, ArrayList<Integer> pick) throws Exception {
        boolean[] isPick = new boolean[all.size()];
        for (int k : pick) isPick[k] = true;
        BufferedImage bolt = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        BufferedImage rock = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < W * H; i++) {
            int p = src[i];
            int al = (p >>> 24) & 255;
            if (al < 70) continue;
            int r = (p >> 16) & 255, g = (p >> 8) & 255, b = p & 255;
            int bri = (r + g + b) / 3;
            int mx = Math.max(r, Math.max(g, b)), mn = Math.min(r, Math.min(g, b));
            int sat = mx - mn;
            if (bri >= BOLT_BRI && sat <= BOLT_SAT) bolt.setRGB(i % W, i / W, 0xffffffff);
            else if (bri <= ROCK_BRI && b >= g && b - g >= 8 && sat <= 130) rock.setRGB(i % W, i / W, 0xffffffff);
        }
        ImageIO.write(bolt, "png", new File("data/anh_the/dbg_bolt.png"));
        ImageIO.write(rock, "png", new File("data/anh_the/dbg_rock.png"));
        BufferedImage b = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = b.createGraphics();
        g.setColor(new java.awt.Color(0x101014));
        g.fillRect(0, 0, W, H);
        g.drawImage(ImageIO.read(new File("tools/res/goku_base.png")), 0, 0, null);
        g.dispose();
        ImageIO.write(b, "png", new File("data/anh_the/dbg_base.png"));
        System.out.println("-> dbg_bolt.png / dbg_rock.png / dbg_base.png");
    }
}
