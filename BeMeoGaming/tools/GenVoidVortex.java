import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * "Vong Xoay Hu Khong" - hao quang 8 frame procedural (item 1933).
 * Anh tham khao: tools/res/void_src.png (ho den tim + xoay cyan + set do + pha le).
 *
 * Y tuong (danh cho nguoi choi, 8 frame vong lap):
 *   f1 Khoi dong : ho den tim mo duoi chan, vai hat neon li ti bay len
 *   f2 Xoay dan  : mo rong, loc xoay nhe, 1 set do chat tu got chan len dau goi
 *   f3 Nut vro   : xoay nhanh hon, 4 manh pha le loi len, set do quanh dui/eo
 *   f4a/f4b, f5a/f5b Dinh diem: ho den mo rong toi da (tim -> do ruc), set chang chi
 *                    toan than (MOI FRAME SEED RIENG -> set nhay lien tuc, khong dung yen),
 *                    8 manh vay quanh nguoi. Moi stage dip 2 tick -> lau hon frame khac 2 lan
 *   f6 Ha nhiet  : thu hep, set yeu dan, manh vay ha do cao
 *   f7 Tan bien   : het set, con khi neon mem + ho den dang dong
 *   f8 Ve moc    : giong f1 (envelope cuoi == envelope dau) + buoc quay 36 do/frame deu
 *                  -> vong lap 10 tick khong giat
 *
 * Tong 10 tick (8 giai doan, dip cho 2 giai doan dinh) - hop icon_data.
 * Frame co ban 300x300 (x2) nhu item 1932. Draw tai 300x300 roi g.scale(zoom).
 * Usage: java GenVoidVortex [firstIconId]
 * Output: data/icon_botnet/x{2,3,4}/<id>.png + icon + data/anh_the/void_contact.png
 */
public final class GenVoidVortex {
    static final int W = 300, H = 300;
    static final int DEFAULT_FIRST = 32501, F = 10;   // 10 tick = 8 giai doan (dip f4/f5)
    static final int[] ZOOMS = {2, 3, 4};
    static final double[] ZOOM_SCALES = {1.0, 1.5, 2.0};

    static final double CX = W / 2.0;      // tam khung
    static final double HOLE_Y = 196;      // tam ho den (duoi chan nhan vien)
    static final double BODY_Y = 140;      // tam than (cho flash do)

    // envelope 10 tick - cuoi (idx9) giong dau (idx0) de vong lap muot
    static final double[] HOLE_R   = {34, 44, 56, 72, 74, 74, 72, 58, 40, 34};
    static final double[] HOLE_A   = {.45, .70, .90, 1.0, 1.0, 1.0, 1.0, .80, .55, .45};
    static final double[] SWIRL_A  = {.10, .45, .75, 1.0, 1.0, 1.0, 1.0, .65, .30, .10};
    static final int[]    BOLT_N   = {0, 1, 3, 7, 7, 7, 7, 3, 0, 0};
    static final int[]    BOLT_TOP = {0, 166, 138, 76, 76, 76, 76, 142, 0, 0};  // y nho = cao
    static final int[]    SHARD_N  = {0, 0, 4, 8, 8, 8, 8, 5, 0, 0};
    static final double[] SPARK_A  = {.35, .50, .70, 1.0, 1.0, 1.0, 1.0, .70, .40, .35};
    static final double[] RED_PEAK = {0, 0, .30, 1.0, 1.0, 1.0, 1.0, .30, 0, 0};

    private GenVoidVortex() { }

    public static void main(String[] a) throws Exception {
        int first = a.length > 0 ? Integer.parseInt(a[0]) : DEFAULT_FIRST;
        renderFrames(first);
        renderIcon(first + F);
        renderContact(first);
        System.out.printf("DONE void-vortex frames %d..%d; icon %d%n",
                first, first + F - 1, first + F);
    }

    // ---------- render ----------

    static void renderFrames(int first) throws Exception {
        for (int z = 0; z < ZOOMS.length; z++) {
            double zoom = ZOOM_SCALES[z];
            File dir = new File("data/icon_botnet/x" + ZOOMS[z]);
            dir.mkdirs();
            int fw = (int) (W * zoom), fh = (int) (H * zoom);
            for (int i = 0; i < F; i++) {
                BufferedImage frame = new BufferedImage(fw, fh, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = frame.createGraphics();
                setup(g);
                g.scale(zoom, zoom);
                drawFrame(g, i);
                g.dispose();
                ImageIO.write(frame, "png", new File(dir, (first + i) + ".png"));
            }
            System.out.printf("x%d: %d void frames %dx%d%n", ZOOMS[z], F, fw, fh);
        }
    }

    static void drawFrame(Graphics2D g, int i) {
        double spin = Math.toRadians(36.0 * i);     // 36 do/tick -> 360 do/10 tick
        double r = HOLE_R[i];

        // 1. sang do (dinh diem f4/f5) - sau lung
        if (RED_PEAK[i] > 0.01) {
            drawRadial(g, CX, BODY_Y, 105,
                    new Color(255, 40, 62, (int) (52 * RED_PEAK[i])),
                    new Color(255, 40, 62, 0));
        }

        // 2. cho duoi chan (mist tim-neon mem)
        drawRadialOval(g, CX, HOLE_Y + 6, r * 2.1, r * 0.75,
                new Color(120, 70, 255, (int) (46 * HOLE_A[i])),
                new Color(120, 70, 255, 0));

        // 3. ho den
        drawHole(g, i, r, spin);

        // 4. vong xoay cyan quanh nguoi + accretion disk
        drawSwirl(g, i, r, spin);

        // 5. manh pha le (sau swirl, sap xep theo vong)
        drawShards(g, i, spin);

        // 6. set do
        for (int b = 0; b < BOLT_N[i]; b++) drawBolt(g, i, b);

        // 7. hat sang neon bay len
        drawSparks(g, i);
    }

    static void drawHole(Graphics2D g, int i, double r, double spin) {
        double a = HOLE_A[i];
        // doi mau tim -> do khi suot dinh diem
        double red = RED_PEAK[i];
        Color rim = lerpColor(new Color(150, 60, 255), new Color(255, 60, 70), red);

        Graphics2D s = (Graphics2D) g.create();
        s.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) a));
        // thang ho: den tham giua, vien mau
        RadialGradientPaint gp = new RadialGradientPaint(
                new Point2D.Double(CX, HOLE_Y), (float) r,
                new float[]{0f, 0.5f, 0.82f, 1f},
                new Color[]{new Color(0, 0, 0, 255), new Color(10, 3, 22, 255),
                        new Color(rim.getRed(), rim.getGreen(), rim.getBlue(), 210),
                        new Color(rim.getRed(), rim.getGreen(), rim.getBlue(), 0)});
        s.setPaint(gp);
        s.fill(new Ellipse2D.Double(CX - r, HOLE_Y - r * 0.42, r * 2, r * 0.84));
        // vien
        s.setColor(new Color(rim.getRed(), rim.getGreen(), rim.getBlue(), 190));
        s.setStroke(new BasicStroke(1.6f));
        s.draw(new Ellipse2D.Double(CX - r, HOLE_Y - r * 0.42, r * 2, r * 0.84));
        s.dispose();

        // accretion disk: 2 net xoay quanh ho
        for (int k = 0; k < 2; k++) {
            double rr = r * (1.35 + 0.3 * k);
            double start = Math.toDegrees(spin * (k == 0 ? 1.6 : -1.2)) + k * 165;
            g.setStroke(new BasicStroke(k == 0 ? 2.4f : 1.6f, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND));
            g.setColor(new Color(rim.getRed(), rim.getGreen(), rim.getBlue(),
                    (int) (150 * a)));
            g.draw(new Arc2D.Double(CX - rr, HOLE_Y - rr * 0.40, rr * 2, rr * 0.80,
                    start, 210, Arc2D.OPEN));
        }
    }

    static void drawSwirl(Graphics2D g, int i, double r, double spin) {
        double a = SWIRL_A[i];
        if (a < 0.03) return;
        // 5 vong lon xoan len tren nguoi (cuon nguoc tu duoi len)
        for (int k = 0; k < 5; k++) {
            double y = HOLE_Y - 12 - k * 24;
            double rx = (66 - k * 5) * (0.55 + 0.45 * HOLE_R[i] / 74.0);
            double ry = rx * 0.30;
            int strands = 2;
            for (int t = 0; t < strands; t++) {
                double start = Math.toDegrees(spin) + k * 40 + t * 180 + k * 25;
                float w = (float) (k == 0 ? 2.6 : 2.0 - k * 0.2);
                // glow
                g.setStroke(new BasicStroke(w + 3.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.setColor(new Color(40, 200, 255, (int) (55 * a)));
                g.draw(new Arc2D.Double(CX - rx, y - ry, rx * 2, ry * 2, start, 165, Arc2D.OPEN));
                // core
                g.setStroke(new BasicStroke(w, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.setColor(new Color(150, 245, 255, (int) (215 * a)));
                g.draw(new Arc2D.Double(CX - rx, y - ry, rx * 2, ry * 2, start, 165, Arc2D.OPEN));
            }
        }
    }

    static void drawShards(Graphics2D g, int i, double spin) {
        int n = SHARD_N[i];
        if (n == 0) return;
        // ve phia sau truoc (y be hon), sau do phia truoc -> co chieu sau
        List<double[]> order = new ArrayList<>();
        for (int k = 0; k < n; k++) {
            double ang = k * (Math.PI * 2 / Math.max(n, 4)) + spin;
            order.add(new double[]{k, ang});
        }
        order.sort(Comparator.comparingDouble(p -> Math.sin(p[1])));
        for (double[] p : order) {
            int k = (int) p[0];
            double ang = p[1];
            double rad = 60 + (k % 3) * 13;
            double bob = 5 * Math.sin(2 * Math.PI * (i / (double) F + k * 0.13));
            double x = CX + rad * Math.cos(ang);
            double y = BODY_Y + 14 + rad * 0.72 * Math.sin(ang) + bob;
            double depth = 0.55 + 0.45 * (0.5 + 0.5 * Math.sin(ang)); // sau man
            double size = (7 + (k % 4) * 2.6) * depth;
            double rot = ang * 1.4 + i * 0.35 + k;
            drawShard(g, x, y, size, rot, (k % 2 == 0), 0.55 + 0.45 * depth);
        }
    }

    static void drawShard(Graphics2D g, double x, double y, double size, double rot,
                          boolean redEdge, double alpha) {
        Random rnd = new Random(31337 + (int) (x * 7 + y * 13));
        int nv = 5;
        Path2D p = new Path2D.Double();
        for (int v = 0; v < nv; v++) {
            double a = rot + v * (Math.PI * 2 / nv) + rnd.nextDouble() * 0.5;
            double rr = size * (0.7 + rnd.nextDouble() * 0.55);
            double px = x + rr * Math.cos(a);
            double py = y + rr * 1.55 * Math.sin(a);   // keo dai doc
            if (v == 0) p.moveTo(px, py); else p.lineTo(px, py);
        }
        p.closePath();

        Graphics2D s = (Graphics2D) g.create();
        s.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
        // fill toi tim
        s.setColor(new Color(16, 8, 34, 235));
        s.fill(p);
        // facet sang
        s.setColor(new Color(58, 28, 100, 200));
        s.setStroke(new BasicStroke(1f));
        s.draw(p);
        s.setColor(new Color(70, 36, 125, 150));
        s.fill(new Ellipse2D.Double(x - size * 0.4, y - size * 0.7, size * 0.7, size * 1.0));
        // vien glow
        Color edge = redEdge ? new Color(255, 77, 109) : new Color(77, 225, 255);
        s.setStroke(new BasicStroke(3.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        s.setColor(new Color(edge.getRed(), edge.getGreen(), edge.getBlue(), 60));
        s.draw(p);
        s.setStroke(new BasicStroke(1.2f));
        s.setColor(new Color(edge.getRed(), edge.getGreen(), edge.getBlue(), 225));
        s.draw(p);
        s.dispose();
    }

    /** Set do: midpoint displacement + nhanh, seed rieng tung frame -> chup hinh moi frame. */
    static void drawBolt(Graphics2D g, int i, int b) {
        Random rnd = new Random(9000 + i * 97 + b * 31);
        double r = HOLE_R[i];
        double x0 = CX + (rnd.nextDouble() - 0.5) * r * 1.1;
        double y0 = HOLE_Y - 6 - rnd.nextDouble() * 8;
        double x1, y1;
        if (BOLT_N[i] == 1 && i == 1) {           // f2: got chan -> dau goi
            x0 = CX - r * 0.55; y0 = HOLE_Y - 2;
            x1 = CX - 26 + rnd.nextDouble() * 10; y1 = 166;
        } else {
            x1 = CX + (rnd.nextDouble() - 0.5) * 92;
            y1 = BOLT_TOP[i] + rnd.nextDouble() * 16;
        }
        List<double[]> pts = jagged(x0, y0, x1, y1, 7, 11, rnd);
        paintBolt(g, pts, 1.0);
        // nhanh
        if (rnd.nextDouble() < 0.75) {
            int m = pts.size() / 2 + rnd.nextInt(pts.size() / 3);
            double[] q = pts.get(m);
            List<double[]> br = jagged(q[0], q[1],
                    q[0] + (rnd.nextDouble() - 0.5) * 70,
                    q[1] - 8 - rnd.nextDouble() * 30, 4, 8, rnd);
            paintBolt(g, br, 0.55);
        }
    }

    static List<double[]> jagged(double x0, double y0, double x1, double y1,
                                 int segs, double jag, Random rnd) {
        List<double[]> pts = new ArrayList<>();
        double dx = x1 - x0, dy = y1 - y0;
        double len = Math.hypot(dx, dy);
        double nx = len < 1e-6 ? 0 : -dy / len, ny = len < 1e-6 ? 0 : dx / len;
        for (int k = 0; k <= segs; k++) {
            double t = k / (double) segs;
            double j = k == 0 || k == segs ? 0 : (rnd.nextDouble() - 0.5) * 2 * jag;
            pts.add(new double[]{x0 + dx * t + nx * j, y0 + dy * t + ny * j});
        }
        return pts;
    }

    static void paintBolt(Graphics2D g, List<double[]> pts, double scale) {
        // glow
        g.setStroke(new BasicStroke(5.5f * (float) scale, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        g.setColor(new Color(255, 30, 55, (int) (70 * scale)));
        strokePath(g, pts);
        g.setStroke(new BasicStroke(2.6f * (float) scale, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        g.setColor(new Color(255, 45, 70, (int) (230 * scale)));
        strokePath(g, pts);
        // core sang
        g.setStroke(new BasicStroke(Math.max(1f, 1.2f * (float) scale),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(new Color(255, 225, 228, (int) (240 * scale)));
        strokePath(g, pts);
    }

    static void strokePath(Graphics2D g, List<double[]> pts) {
        for (int k = 0; k < pts.size() - 1; k++) {
            g.drawLine((int) pts.get(k)[0], (int) pts.get(k)[1],
                    (int) pts.get(k + 1)[0], (int) pts.get(k + 1)[1]);
        }
    }

    static void drawSparks(Graphics2D g, int i) {
        double a = SPARK_A[i];
        if (a < 0.03) return;
        Random rnd = new Random(777);
        for (int k = 0; k < 18; k++) {
            double x0 = CX + (rnd.nextDouble() - 0.5) * 130;
            double ph = rnd.nextDouble();
            double size = 1.2 + rnd.nextDouble() * 2.0;
            double hz = 1 + rnd.nextInt(2);           // so vong/lap (nguyen -> loop muot)
            double u = (i / (double) F * hz + ph) % 1.0;
            double y = HOLE_Y - 4 - u * 145;
            double x = x0 + 7 * Math.sin(2 * Math.PI * (u * hz + ph));
            double aa = Math.sin(Math.PI * u) * a;
            if (aa < 0.03) continue;
            boolean green = rnd.nextBoolean();
            g.setColor(green ? new Color(110, 255, 165, (int) (230 * aa))
                    : new Color(80, 232, 255, (int) (230 * aa)));
            drawStar(g, x, y, size * (0.6 + 0.5 * aa));
        }
    }

    static void drawStar(Graphics2D g, double x, double y, double s) {
        int[] vx = {(int) x, (int) (x + s * 0.45), (int) x, (int) (x - s * 0.45)};
        int[] vy = {(int) (y - s * 2.0), (int) y, (int) (y + s * 2.0), (int) y};
        g.fillPolygon(vx, vy, 4);
        int[] hx = {(int) (x - s * 1.6), (int) x, (int) (x + s * 1.6), (int) x};
        int[] hy = {(int) y, (int) (y - s * 0.5), (int) y, (int) (y + s * 0.5)};
        g.fillPolygon(hx, hy, 4);
    }

    // ---------- hieu ung chung ----------

    static void drawRadial(Graphics2D g, double cx, double cy, double r,
                           Color inner, Color outer) {
        RadialGradientPaint gp = new RadialGradientPaint(
                new Point2D.Double(cx, cy), (float) r,
                new float[]{0f, 1f}, new Color[]{inner, outer});
        g.setPaint(gp);
        g.fill(new Ellipse2D.Double(cx - r, cy - r, r * 2, r * 2));
    }

    static void drawRadialOval(Graphics2D g, double cx, double cy, double rx, double ry,
                               Color inner, Color outer) {
        AffineTransform gt = AffineTransform.getTranslateInstance(cx, cy);
        gt.scale(1.0, ry / rx);
        gt.translate(-cx, -cy);
        RadialGradientPaint gp = new RadialGradientPaint(
                new Point2D.Double(cx, cy), (float) rx,
                new Point2D.Double(cx, cy),
                new float[]{0f, 0.55f, 1f},
                new Color[]{inner,
                        new Color(inner.getRed(), inner.getGreen(), inner.getBlue(),
                                inner.getAlpha() / 3), outer},
                RadialGradientPaint.CycleMethod.NO_CYCLE,
                RadialGradientPaint.ColorSpaceType.SRGB, gt);
        g.setPaint(gp);
        g.fill(new Ellipse2D.Double(cx - rx, cy - ry, rx * 2, ry * 2));
    }

    static Color lerpColor(Color a, Color b, double t) {
        t = Math.max(0, Math.min(1, t));
        return new Color((int) (a.getRed() + (b.getRed() - a.getRed()) * t),
                (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t),
                (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * t));
    }

    // ---------- icon + contact ----------

    static void renderIcon(int iconId) throws Exception {
        // icon = frame dinh diem (i=3) scale xuong 42x48
        BufferedImage peak = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D pg = peak.createGraphics();
        setup(pg);
        drawFrame(pg, 3);
        pg.dispose();
        for (int z = 0; z < ZOOMS.length; z++) {
            double zoom = ZOOM_SCALES[z];
            int w = (int) (42 * zoom), h = (int) (48 * zoom);
            BufferedImage icon = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = icon.createGraphics();
            setup(g);
            double s = Math.min((w - 4) / (double) W, (h - 4) / (double) H);
            g.translate((w - W * s) / 2.0, (h - H * s) / 2.0);
            g.scale(s, s);
            g.drawImage(peak, 0, 0, null);
            g.dispose();
            ImageIO.write(icon, "png",
                    new File("data/icon_botnet/x" + ZOOMS[z] + "/" + iconId + ".png"));
        }
        System.out.printf("icon %d = frame dinh diem (i=3)%n", iconId);
    }

    static void renderContact(int first) throws Exception {
        int cols = 5, cellW = 160, cellH = 180, rows = (F + cols - 1) / cols;
        BufferedImage sheet = new BufferedImage(cols * cellW, rows * cellH,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = sheet.createGraphics();
        setup(g);
        g.setColor(new Color(17, 22, 24));
        g.fillRect(0, 0, sheet.getWidth(), sheet.getHeight());
        for (int i = 0; i < F; i++) {
            BufferedImage frame = ImageIO.read(
                    new File("data/icon_botnet/x2/" + (first + i) + ".png"));
            int x = (i % cols) * cellW, y = (i / cols) * cellH;
            g.drawImage(frame, x + 8, y + 6, cellW - 16, cellH - 28, null);
            g.setColor(Color.WHITE);
            String lbl = "f" + (i + 1);
            if (i >= 3 && i <= 6) lbl += " (dinh)";
            if (i == 9) lbl = "f8 = f1 (loop)";
            g.drawString(lbl, x + 8, y + cellH - 8);
        }
        g.dispose();
        ImageIO.write(sheet, "png", new File("data/anh_the/void_contact.png"));
        System.out.println("-> data/anh_the/void_contact.png");
    }

    static void setup(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
}
