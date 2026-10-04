import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Xe anh rong ngang (tools/res/hdragon.png, da cat nen) thanh cac mang doc,
 * sau do dan theo duong helix 2 vong quanh nguoi choi -> 42 frame hao quang.
 * Khong ve gi toan bo: moi diem anh lay theo (cuong do theo tam ong, do doc),
 * vi tri = diem duong + phap tuyen * khoang cach tu tam ong.
 *
 * Frame co ban 1080x1200 (3x hao quang 1930). 42 frame hop flag_bag.icon_data varchar(255).
 * Usage: java GenStripDragon [firstIconId] [frameCount]
 */
public final class GenStripDragon {
    static final int W = 1080, H = 1200;
    static final int DEFAULT_FIRST = 32415, DEFAULT_FRAMES = 42;
    static final int NODES = 8192;
    static final double PLAYER_X = W / 2.0, PLAYER_Y = 620;
    static final double ORBIT_X = 285, Y_BOTTOM = 1010, Y_TOP = 230;
    static final double BODY_LENGTH = 2450;   // do dai than tren duong (px co ban)
    static final double KY = 1.0;             // ty le doc: 1.0 = khong bien dang
    static final int STRIP_W = 1;             // do rong moi mang (px nguon) - nho de khong thay vet cat
    static final int OVERLAP = 1;             // tron de khong ke mat (1px la du)
    static final double HEAD_NATIVE = 0;      // 0 = uniform; >0 = giam co dang cho dau
    static final int HEAD_SRC = 340;          // do rong vung dau (px nguon) khi HEAD_NATIVE > 0
    static final int[] ZOOMS = {2, 3, 4};
    static final double[] ZOOM_SCALES = {1.0, 1.5, 2.0};

    static BufferedImage src;
    static int minX, maxX, minY, maxY;
    static double[] yRef;          // tam ong than theo cot
    static double[] arcOf;         // khoang cach tu dau rong tai tung cot nguon
    static double kx;              // ty le ngang

    static PathPoint[] path;
    static double[] pathDistance;
    static double pathLength;
    static int frameCount;

    private GenStripDragon() { }

    static final class PathPoint {
        final double x, y, depth;
        PathPoint(double x, double y, double depth) {
            this.x = x; this.y = y; this.depth = depth;
        }
    }

    static final class Strip {
        final int sx0, sx1;
        final double depth;
        Strip(int sx0, int sx1, double depth) {
            this.sx0 = sx0; this.sx1 = sx1; this.depth = depth;
        }
    }

    public static void main(String[] args) throws Exception {
        int first = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_FIRST;
        frameCount = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_FRAMES;
        if (frameCount < 24 || frameCount > 42) {
            throw new IllegalArgumentException("frameCount must be 24..42 (flag_bag.icon_data varchar(255))");
        }
        src = ImageIO.read(new File("tools/res/hdragon.png"));
        buildGeometry();
        buildPath();
        renderFrames(first);
        renderIcon(first + frameCount);
        renderContactSheet(first);
        System.out.printf("DONE strip-dragon frames %d..%d; icon %d; path %.0fpx; body %.0fpx; kx=%.2f%n",
                first, first + frameCount - 1, first + frameCount, pathLength, BODY_LENGTH, kx);
    }

    /** Do tam ong than moi cot: trung vi trong luong (on dinh, khong bi long keo) + lam mem. */
    static void buildGeometry() {
        int w = src.getWidth(), h = src.getHeight();
        minX = w; maxX = -1; minY = h; maxY = -1;
        double[] med = new double[w];
        boolean[] has = new boolean[w];
        for (int x = 0; x < w; x++) {
            int total = 0;
            int[] col = new int[h];
            for (int y = 0; y < h; y++) {
                int al = (src.getRGB(x, y) >>> 24) & 255;
                if (al == 0) continue;
                col[y] = al;
                total += al;
                if (x < minX) minX = x;
                if (x > maxX) maxX = x;
                if (y < minY) minY = y;
                if (y > maxY) maxY = y;
            }
            if (total == 0) continue;
            int acc = 0;
            for (int y = 0; y < h; y++) {
                acc += col[y];
                if (acc * 2 >= total) { med[x] = y; has[x] = true; break; }
            }
        }
        // dien cot thieu bang gan nhat
        int last = -1;
        for (int x = 0; x < w; x++) {
            if (has[x]) { last = x; continue; }
            if (last >= 0) med[x] = med[last];
        }
        last = -1;
        for (int x = w - 1; x >= 0; x--) {
            if (has[x]) { last = x; continue; }
            if (last >= 0 && !has[x]) med[x] = med[last];
        }
        // lam mem mang tam (khong cho tam tuyn dich do long/chan/rau lam hinh bi vet)
        yRef = new double[w];
        int win = 14;
        for (int x = 0; x < w; x++) {
            double s = 0; int n = 0;
            for (int k = -win; k <= win; k++) {
                int xx = x + k;
                if (xx < 0 || xx >= w) continue;
                s += med[xx]; n++;
            }
            yRef[x] = n == 0 ? h / 2.0 : s / n;
        }
        kx = BODY_LENGTH / (maxX - minX + 1);
        // khoang cach cung dau (tinh theo ty le ngang, co thay doi tu nhien o dau neu can)
        arcOf = new double[w];
        for (int x = 0; x < w; x++) arcOf[x] = (maxX - x) * kx;
        System.out.printf("src %dx%d content %dx%d (x %d..%d, y %d..%d), tam ong TB=%.0f%n",
                w, h, maxX - minX + 1, maxY - minY + 1, minX, maxX, minY, maxY, avg(yRef, minX, maxX));
    }

    static double avg(double[] a, int from, int to) {
        double s = 0; int n = 0;
        for (int i = from; i <= to; i++) { s += a[i]; n++; }
        return n == 0 ? 0 : s / n;
    }

    static void buildPath() {
        path = new PathPoint[NODES + 1];
        pathDistance = new double[NODES + 1];
        for (int i = 0; i <= NODES; i++) {
            double u = (double) i / NODES;
            double angle = 4 * Math.PI * u;
            boolean rising = u < 0.5;
            double t = rising ? u * 2 : (u - 0.5) * 2;
            double rise = rising ? ease(t) : 1 - ease(t);
            double y = Y_BOTTOM + (Y_TOP - Y_BOTTOM) * rise;
            path[i] = new PathPoint(PLAYER_X + ORBIT_X * Math.sin(angle), y, Math.cos(angle));
            if (i > 0) {
                pathDistance[i] = pathDistance[i - 1] + Math.hypot(
                        path[i].x - path[i - 1].x, path[i].y - path[i - 1].y);
            }
        }
        pathLength = pathDistance[NODES];
    }

    static double ease(double t) { return 0.5 - 0.5 * Math.cos(Math.PI * t); }

    static PathPoint pointAtDistance(double d) {
        d = ((d % pathLength) + pathLength) % pathLength;
        int lo = 0, hi = NODES;
        while (lo < hi - 1) {
            int mid = (lo + hi) >>> 1;
            if (pathDistance[mid] <= d) lo = mid; else hi = mid;
        }
        double span = pathDistance[hi] - pathDistance[lo];
        double f = span <= 1e-9 ? 0 : (d - pathDistance[lo]) / span;
        PathPoint a = path[lo], b = path[hi];
        return new PathPoint(a.x + (b.x - a.x) * f, a.y + (b.y - a.y) * f,
                a.depth + (b.depth - a.depth) * f);
    }

    static double[] tangentAt(double d) {
        PathPoint a = pointAtDistance(d - 4), b = pointAtDistance(d + 4);
        double dx = b.x - a.x, dy = b.y - a.y, len = Math.hypot(dx, dy);
        return len < 1e-8 ? new double[]{1, 0} : new double[]{dx / len, dy / len};
    }

    static void drawFrame(Graphics2D g, int frameNo) {
        double headD = (double) frameNo / frameCount * pathLength;
        List<Strip> strips = new ArrayList<>();
        int sw = STRIP_W + OVERLAP * 2;
        for (int sx = minX; sx <= maxX; sx += STRIP_W) {
            int x0 = sx, x1 = Math.min(maxX + 1, sx + STRIP_W);
            double d = headD - arcOf[(x0 + x1) / 2];
            PathPoint p = pointAtDistance(d);
            strips.add(new Strip(x0, x1, p.depth));
        }
        strips.sort(Comparator.comparingDouble(s -> s.depth));
        for (Strip s : strips) drawStrip(g, headD, s.sx0, s.sx1);
    }

    /**
     * Ve 1 mang: affine dan tu he toa do nguon (x, y-yRef[x]) sang he duong.
     * (x0,y0)->P0, (x1,y1)->P1, phap tuyen tai P0 lam chieu doc.
     */
    static void drawStrip(Graphics2D g, double headD, int sx0, int sx1) {
        int cx0 = sx0 - OVERLAP, cx1 = sx1 + OVERLAP;
        double d0 = headD - arcOf[clampi(sx0)];
        double d1 = headD - arcOf[clampi(sx1)];
        PathPoint p0 = pointAtDistance(d0), p1 = pointAtDistance(d1);
        double[] t0 = tangentAt(d0);
        double nx = -t0[1], ny = t0[0];
        double dw = (double) (sx1 - sx0);
        double m00 = (p1.x - p0.x) / dw, m10 = (p1.y - p0.y) / dw;
        double m01 = nx * KY, m11 = ny * KY;
        double y0 = yRef[clampi(sx0)];
        double tx = p0.x - m00 * sx0 - m01 * y0;
        double ty = p0.y - m10 * sx0 - m11 * y0;

        Graphics2D s = (Graphics2D) g.create();
        s.transform(new AffineTransform(m00, m10, m01, m11, tx, ty));
        s.clipRect(cx0, 0, cx1 - cx0, src.getHeight());
        s.drawImage(src, 0, 0, null);
        s.dispose();
    }

    static int clampi(int v) { return Math.max(0, Math.min(src.getWidth() - 1, v)); }

    static void renderFrames(int first) throws Exception {
        for (int z = 0; z < ZOOMS.length; z++) {
            double zoom = ZOOM_SCALES[z];
            File dir = new File("data/icon_botnet/x" + ZOOMS[z]);
            dir.mkdirs();
            int fw = (int) (W * zoom), fh = (int) (H * zoom);
            for (int i = 0; i < frameCount; i++) {
                BufferedImage frame = new BufferedImage(fw, fh, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = frame.createGraphics();
                setup(g);
                g.scale(zoom, zoom);
                drawFrame(g, i);
                g.dispose();
                ImageIO.write(frame, "png", new File(dir, (first + i) + ".png"));
            }
            System.out.printf("x%d: %d strip frames %dx%d%n", ZOOMS[z], frameCount, fw, fh);
        }
    }

    static void renderIcon(int iconId) throws Exception {
        // crop dau rong (phan ben phai cua anh)
        int cw = Math.min(360, maxX - minX + 1);
        int hx0 = maxX - cw + 1, hx1 = maxX + 1;
        int hy0 = src.getHeight(), hy1 = -1;
        for (int x = hx0; x < hx1; x++)
            for (int y = 0; y < src.getHeight(); y++) {
                if (((src.getRGB(x, y) >>> 24) & 255) == 0) continue;
                if (y < hy0) hy0 = y;
                if (y > hy1) hy1 = y;
            }
        if (hy1 < hy0) { hy0 = 0; hy1 = src.getHeight() - 1; }
        for (int z = 0; z < ZOOMS.length; z++) {
            double zoom = ZOOM_SCALES[z];
            int w = (int) (42 * zoom), h = (int) (48 * zoom);
            BufferedImage icon = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = icon.createGraphics();
            setup(g);
            double s = Math.min((w - 4) / (double) cw, (h - 4) / (double) (hy1 - hy0 + 1));
            g.scale(s, s);
            g.translate(-hx0, -hy0);
            g.drawImage(src, 0, 0, null);
            g.dispose();
            ImageIO.write(icon, "png", new File("data/icon_botnet/x" + ZOOMS[z] + "/" + iconId + ".png"));
        }
        System.out.printf("icon %d = crop dau (%d,%d)-(%d,%d)%n", iconId, hx0, hy0, hx1 - 1, hy1);
    }

    static void renderContactSheet(int first) throws Exception {
        int cols = 7, cellW = 140, cellH = 160, rows = (frameCount + cols - 1) / cols;
        BufferedImage sheet = new BufferedImage(cols * cellW, rows * cellH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = sheet.createGraphics();
        setup(g);
        g.setColor(new Color(17, 22, 24));
        g.fillRect(0, 0, sheet.getWidth(), sheet.getHeight());
        for (int i = 0; i < frameCount; i++) {
            BufferedImage frame = ImageIO.read(new File("data/icon_botnet/x2/" + (first + i) + ".png"));
            int x = (i % cols) * cellW, y = (i / cols) * cellH;
            g.drawImage(frame, x + 3, y + 2, cellW - 6, cellH - 23, null);
            g.setColor(Color.WHITE);
            g.drawString("frame " + i, x + 6, y + cellH - 5);
        }
        g.dispose();
        ImageIO.write(sheet, "png", new File("data/anh_the/strip_contact.png"));
    }

    static void setup(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
}
