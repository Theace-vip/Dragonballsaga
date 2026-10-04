import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Ve rong cong bo quanh nguoi choi bang Java2D, khong dung artwork nao.
 * Than la day elip uon len/xe theo duong helix 2 vong (sau -> truoc nguoi choi),
 * them song ngang de than nhun di nhu rong bo that. Dau, suoi, kim lung, duoi
 * ve rieng bang hinh hoc va sap xep theo depth.
 * Frame co ban 1080x1200 (3x hao quang 1930). 42 frame hop flag_bag.icon_data varchar(255).
 * Usage: java GenOrbitDragon [firstIconId] [frameCount]
 */
public final class GenOrbitDragon {
    static final int W = 1080, H = 1200;
    static final int DEFAULT_FIRST = 32415, DEFAULT_FRAMES = 42;
    static final int NODES = 8192;
    static final double PLAYER_X = W / 2.0, PLAYER_Y = 620;
    static final double ORBIT_X = 285, Y_BOTTOM = 1010, Y_TOP = 230;
    static final double BODY_LENGTH = 2560;
    static final double STEP = 5.0;
    static final double WAVE_AMP = 24;
    static final int WAVES = 3;
    static final int[] ZOOMS = {2, 3, 4};
    static final double[] ZOOM_SCALES = {1.0, 1.5, 2.0};

    static final Color OUTLINE = new Color(0x0c2f24);
    static final Color BODY = new Color(0x2f9469);
    static final Color BODY_DARK = new Color(0x185a41);
    static final Color BELLY = new Color(0xc7e4d3);
    static final Color SKULL = new Color(0x3fa276);
    static final Color JAW = new Color(0x1f6b4d);
    static final Color GOLD = new Color(0xd9b75e);
    static final Color GOLD_LIGHT = new Color(0xefd896);
    static final Color TEAL = new Color(0x7fd6b0);
    static final Color EYE = new Color(0xffe08a);
    static final Color PUPIL = new Color(0x0d1a14);
    static final Color MOUTH = new Color(0x6e2b30);
    static final Color TOOTH = new Color(0xf7f7ee);
    static final Color SPIKE = new Color(0x54c093);

    static PathPoint[] path;
    static double[] pathDistance;
    static double pathLength;
    static int frameCount;

    private GenOrbitDragon() { }

    static final class PathPoint {
        final double x, y, depth;
        PathPoint(double x, double y, double depth) {
            this.x = x; this.y = y; this.depth = depth;
        }
    }

    /** 0 = than, 1 = dau, 2 = duoi */
    static final class Item {
        final int kind;
        final double depth, x, y, r, angle, belly;
        final double tx, ty;
        final boolean spike;
        Item(int kind, double depth, double x, double y, double r, double angle,
             double tx, double ty, boolean spike, double belly) {
            this.kind = kind; this.depth = depth; this.x = x; this.y = y;
            this.r = r; this.angle = angle; this.tx = tx; this.ty = ty;
            this.spike = spike; this.belly = belly;
        }
    }

    public static void main(String[] args) throws Exception {
        int first = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_FIRST;
        frameCount = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_FRAMES;
        if (frameCount < 24 || frameCount > 42) {
            throw new IllegalArgumentException("frameCount must be 24..42 (flag_bag.icon_data varchar(255))");
        }
        buildPath();
        renderFrames(first);
        renderIcon(first + frameCount);
        renderContactSheet(first);
        System.out.printf("DONE orbit-dragon frames %d..%d; icon %d; path %.0fpx; body %.0fpx%n",
                first, first + frameCount - 1, first + frameCount, pathLength, BODY_LENGTH);
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
        PathPoint a = pointAtDistance(d - 5), b = pointAtDistance(d + 5);
        double dx = b.x - a.x, dy = b.y - a.y, len = Math.hypot(dx, dy);
        return len < 1e-8 ? new double[]{1, 0} : new double[]{dx / len, dy / len};
    }

    /** Ban kinh than: co -> day -> nho dan ve duoi. */
    static double radiusAt(double t) {
        double neck = 26, max = 38, tip = 3;
        if (t < 0.18) {
            double s = t / 0.18;
            return neck + (max - neck) * (s * s * (3 - 2 * s));
        }
        if (t < 0.62) return max;
        double u = (t - 0.62) / 0.38;
        return max + (tip - max) * Math.pow(u, 1.5);
    }

    static void drawFrame(Graphics2D g, int frameNo) {
        double headD = (double) frameNo / frameCount * pathLength;
        double phase = 2.0 * Math.PI * frameNo / frameCount;
        List<Item> items = new ArrayList<>();
        int i = 0;
        for (double d = 0; d <= BODY_LENGTH + 1e-6; d += STEP, i++) {
            double t = d / BODY_LENGTH;
            PathPoint p = pointAtDistance(headD - d);
            double[] tan = tangentAt(headD - d);
            double nx = -tan[1], ny = tan[0];
            double amp = WAVE_AMP * Math.min(1.0, t / 0.12);
            double off = amp * Math.sin(2 * Math.PI * WAVES * t - phase);
            double x = p.x + nx * off, y = p.y + ny * off;
            boolean spike = i % 4 == 0 && t > 0.04 && t < 0.92;
            double belly = clamp((0.94 - t) / 0.12);
            items.add(new Item(0, p.depth, x, y, radiusAt(t), 0, tan[0], tan[1], spike, belly));
        }
        PathPoint hp = pointAtDistance(headD);
        double[] htan = tangentAt(headD);
        items.add(new Item(1, hp.depth, hp.x, hp.y, 0,
                Math.atan2(htan[1], htan[0]), htan[0], htan[1], false, 0));
        PathPoint tp = pointAtDistance(headD - BODY_LENGTH);
        double[] ttan = tangentAt(headD - BODY_LENGTH);
        items.add(new Item(2, tp.depth, tp.x, tp.y, 0,
                Math.atan2(ttan[1], ttan[0]), ttan[0], ttan[1], false, 0));
        items.sort(Comparator.comparingDouble((Item it) -> it.depth));
        // Pass 1: chi ve duong vien cua tat ca (khong xen ke -> khong sinh ring)
        for (Item it : items) {
            if (it.kind != 0) continue;
            double front = (it.depth + 1) / 2;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    (float) (0.72 + 0.28 * front)));
            g.setColor(OUTLINE);
            g.fillOval((int) (it.x - it.r - 2.5), (int) (it.y - it.r - 2.5),
                    (int) (2 * it.r + 5), (int) (2 * it.r + 5));
        }
        // Pass 2: day theo depth (fill trung mau ghep mem, khong vong)
        for (Item it : items) {
            double front = (it.depth + 1) / 2;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    (float) (0.72 + 0.28 * front)));
            if (it.kind == 0) drawBodyFill(g, it, front);
            else if (it.kind == 1) drawHead(g, it.x, it.y, it.angle, front);
            else drawTail(g, it.x, it.y, it.angle, front);
        }
        // Pass 3: belly sau cung
        for (Item it : items) {
            double front = (it.depth + 1) / 2;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    (float) (0.72 + 0.28 * front)));
            drawBelly(g, it, front);
        }
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    static void drawBodyFill(Graphics2D g, Item it, double front) {
        double r = it.r, x = it.x, y = it.y;
        if (it.spike) {
            double bx = PLAYER_X - x, by = PLAYER_Y - y, bl = Math.hypot(bx, by);
            if (bl > 1) {
                double ox = -bx / bl, oy = -by / bl, w = r * 0.7;
                int[] xs = {(int) (x - it.tx * w), (int) (x + it.tx * w), (int) (x + ox * r * 1.9)};
                int[] ys = {(int) (y - it.ty * w), (int) (y + it.ty * w), (int) (y + oy * r * 1.9)};
                g.setColor(mix(SPIKE, OUTLINE, 0.35 + 0.3 * (1 - front)));
                g.fillPolygon(xs, ys, 3);
            }
        }
        g.setColor(mix(BODY, OUTLINE, 0.45 * (1 - front)));
        g.fillOval((int) (x - r), (int) (y - r), (int) (2 * r), (int) (2 * r));
    }

    /** Pass 3: belly ve sau khi tat ca da day -> khong bi segment sau de. */
    static void drawBelly(Graphics2D g, Item it, double front) {
        if (it.kind != 0 || it.belly <= 0) return;
        double r = it.r, x = it.x, y = it.y;
        double bx = PLAYER_X - x, by = PLAYER_Y - y, bl = Math.hypot(bx, by);
        if (bl < 1) return;
        double ux = bx / bl, uy = by / bl;
        double br = r * 0.44 * it.belly;
        g.setColor(mix(BELLY, BODY_DARK, 0.20 + 0.28 * (1 - front)));
        g.fillOval((int) (x + ux * r * 0.34 - br), (int) (y + uy * r * 0.34 - br),
                (int) (2 * br), (int) (2 * br));
    }

    /** Dau rong: buom sau, 2 canh, mat, mui, ria chay lui. +x = huong bay. */
    static void drawHead(Graphics2D g, double x, double y, double angle, double front) {
        Graphics2D h = (Graphics2D) g.create();
        h.translate(x, y);
        h.rotate(angle);
        drawHeadLocal(h, front);
        h.dispose();
    }

    static void drawHeadLocal(Graphics2D g, double front) {
        g.setColor(mix(TEAL, OUTLINE, 0.4 * (1 - front)));
        spike(g, 30, -24, -18, -58, -74, -58, 20, 3);
        spike(g, 22, -2, -26, -34, -84, -20, 18, 3);
        spike(g, 18, 18, -22, 44, -64, 62, 16, 3);
        g.setColor(mix(GOLD, OUTLINE, 0.35 * (1 - front)));
        spike(g, 44, -30, -6, -74, -62, -88, 17, 3);
        g.setColor(mix(GOLD_LIGHT, OUTLINE, 0.35 * (1 - front)));
        spike(g, 38, -24, -14, -66, -46, -108, 13, 2);
        g.setColor(mix(TEAL, OUTLINE, 0.4 * (1 - front)));
        spike(g, 34, -4, 6, -56, -26, -48, 14, 3);
        g.setColor(OUTLINE);
        g.fillOval(0, -38, 92, 76);
        g.fillOval(60, -20, 88, 56);
        g.setColor(mix(SKULL, OUTLINE, 0.4 * (1 - front)));
        g.fillOval(3, -34, 86, 68);
        g.fillOval(63, -16, 82, 48);
        g.setColor(mix(JAW, OUTLINE, 0.4 * (1 - front)));
        g.fillOval(70, 14, 76, 30);
        g.setColor(MOUTH);
        g.fillOval(76, 4, 66, 18);
        g.setColor(TOOTH);
        int[] tx1 = {92, 100, 108}, ty1 = {14, 14, 14};
        g.fillPolygon(tx1, new int[]{14, 14, 24}, 3);
        g.fillPolygon(new int[]{118, 126, 134}, new int[]{14, 14, 23}, 3);
        g.fillPolygon(new int[]{100, 106, 112}, new int[]{14, 14, 21}, 3);
        g.fillPolygon(new int[]{86, 92, 98}, new int[]{12, 18, 12}, 3);
        g.fillPolygon(new int[]{124, 130, 136}, new int[]{12, 17, 12}, 3);
        g.setColor(OUTLINE);
        g.fillOval(54, -26, 28, 22);
        g.setColor(EYE);
        g.fillOval(57, -23, 22, 16);
        g.setColor(PUPIL);
        g.fillOval(65, -21, 7, 12);
        g.setColor(mix(BODY_DARK, OUTLINE, 0.3));
        g.fillPolygon(new int[]{50, 88, 86, 50}, new int[]{-36, -32, -25, -29}, 4);
        g.setColor(PUPIL);
        g.fillOval(136, -4, 7, 6);
        g.setColor(mix(TEAL, OUTLINE, 0.35 * (1 - front)));
        spike(g, 124, 10, 174, 44, 52, 94, 5, 1);
        spike(g, 124, 0, 170, -30, 56, -62, 5, 1);
        g.setColor(mix(BELLY, BODY_DARK, 0.35));
        spike(g, 70, 40, 40, 74, 12, 58, 13, 4);
    }

    static void drawTail(Graphics2D g, double x, double y, double angle, double front) {
        Graphics2D h = (Graphics2D) g.create();
        h.translate(x, y);
        h.rotate(angle);
        h.setColor(mix(TEAL, OUTLINE, 0.4 * (1 - front)));
        spike(h, -4, -2, -44, -34, -92, -52, 14, 3);
        spike(h, -4, 0, -56, 2, -104, 4, 15, 3);
        spike(h, -4, 4, -44, 36, -88, 56, 13, 3);
        h.dispose();
    }

    /** Day cong bezier co dan tu w0 -> w1 (canh, rang, ria). */
    static void spike(Graphics2D g, double x0, double y0, double cx, double cy,
                      double x1, double y1, double w0, double w1) {
        int n = 10;
        double[] xs = new double[n + 1], ys = new double[n + 1];
        for (int i = 0; i <= n; i++) {
            double s = i / (double) n, om = 1 - s;
            xs[i] = om * om * x0 + 2 * om * s * cx + s * s * x1;
            ys[i] = om * om * y0 + 2 * om * s * cy + s * s * y1;
        }
        Path2D p = new Path2D.Double();
        for (int side = 0; side < 2; side++) {
            int from = side == 0 ? 0 : n, to = side == 0 ? n : 0, stp = side == 0 ? 1 : -1;
            for (int i = from; ; i += stp) {
                int j = Math.min(i + 1, n), k = Math.max(i - 1, 0);
                double dx = xs[j] - xs[k], dy = ys[j] - ys[k], l = Math.hypot(dx, dy);
                if (l < 1e-9) { dx = 1; dy = 0; l = 1; }
                double w = (w0 + (w1 - w0) * (i / (double) n)) / 2;
                double nx = -dy / l * w, ny = dx / l * w;
                double sx = side == 0 ? 1 : -1;
                if (i == from && side == 0) p.moveTo(xs[i] + sx * nx, ys[i] + sx * ny);
                else p.lineTo(xs[i] + sx * nx, ys[i] + sx * ny);
                if (i == to) break;
            }
        }
        p.closePath();
        g.fill(p);
    }

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
            System.out.printf("x%d: %d orbit frames %dx%d%n", ZOOMS[z], frameCount, fw, fh);
        }
    }

    static void renderIcon(int iconId) throws Exception {
        for (int z = 0; z < ZOOMS.length; z++) {
            double zoom = ZOOM_SCALES[z];
            int w = (int) (42 * zoom), h = (int) (48 * zoom);
            BufferedImage icon = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = icon.createGraphics();
            setup(g);
            double s = Math.min((w - 4) / 240.0, (h - 4) / 210.0);
            g.scale(s, s);
            g.translate(-46, 6);
            g.setComposite(AlphaComposite.SrcOver);
            drawHead(g, 0, 0, 0, 1);
            g.dispose();
            ImageIO.write(icon, "png", new File("data/icon_botnet/x" + ZOOMS[z] + "/" + iconId + ".png"));
        }
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
        ImageIO.write(sheet, "png", new File("data/anh_the/orbit_contact.png"));
    }

    static double clamp(double v) { return Math.max(0, Math.min(1, v)); }

    static Color mix(Color a, Color b, double t) {
        t = Math.max(0, Math.min(1, t));
        return new Color((int) (a.getRed() + (b.getRed() - a.getRed()) * t),
                (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t),
                (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * t));
    }

    static void setup(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
}
