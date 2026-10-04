import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Sinh 24 frame "rong vang bay quanh nguoi" cho flag_bag (channel B - TYPE 11).
 *
 * Dong hinh (loop muot, khong hoi): dau rong di theo 1 vong khep:
 *   len lan TRAI (x=116) -> cong qua tren -> xuong lan PHAI (x=245) -> cong qua duoi -> lap lai.
 * => "bay tu duoi len uon luon roi xuong lai" dung nhu yeu cau, va khong bi lat dau khi dao huong.
 * Than = lich su vi tri dau (delay), cong them song song vuong goc -> uon luon.
 *
 * Kich thuoc x2 = 360x400 = 2.5 lan hao quang vong tron (144x160).
 * Icon id: frame = first..first+23, icon item (dau rong) = first+24.
 *
 * Usage: java GenDragon <firstIconId>     (mac dinh 32347)
 */
public class GenDragon {

    static final int W = 360, H = 400;      // x2 = 2.5 x (144x160)
    static final int FRAMES = 24;           // loop khep: 12 frame len + 12 frame xuong
    static final double LANE_L = 116, LANE_R = 245;
    static final double TOP = 92, BOT = 322, RY = 44;
    static final double BODY_LEN = 330;
    static final int SEG = 48;
    static final double AMP = 13, WAVELEN = 115;
    static final double CX = 180, CY = 205; // tam vong (de thanh huong vao trong = belly huong vao nguoi)

    // ---------------- duong vong (path) ----------------
    static final List<Point2D.Double> path = new ArrayList<>();
    static double[] cum;
    static double total;

    static void buildPath() {
        double cx = (LANE_L + LANE_R) / 2, rx = (LANE_R - LANE_L) / 2;
        // len lan trai (tu BOT len TOP)
        for (double y = BOT; y >= TOP; y -= 1) path.add(new Point2D.Double(LANE_L, y));
        // cong tren: a: PI -> 2PI (di qua dinh, y = TOP - RY*sin)
        for (int k = 1; k <= 48; k++) {
            double a = Math.PI + Math.PI * k / 48;
            path.add(new Point2D.Double(cx + rx * Math.cos(a), TOP + RY * Math.sin(a)));
        }
        // xuong lan phai
        for (double y = TOP + 1; y <= BOT; y += 1) path.add(new Point2D.Double(LANE_R, y));
        // cong duoi: a: 0 -> PI (di qua day, y = BOT + RY*sin)
        for (int k = 1; k <= 48; k++) {
            double a = Math.PI * k / 48;
            path.add(new Point2D.Double(cx + rx * Math.cos(a), BOT + RY * Math.sin(a)));
        }
        cum = new double[path.size()];
        for (int i = 1; i < path.size(); i++) {
            cum[i] = cum[i - 1] + path.get(i).distance(path.get(i - 1));
        }
        total = cum[cum.length - 1];
    }

    static Point2D.Double pointAt(double s) {
        s = ((s % total) + total) % total;
        int lo = 0, hi = cum.length - 1;
        while (lo < hi - 1) {
            int mid = (lo + hi) >> 1;
            if (cum[mid] <= s) lo = mid; else hi = mid;
        }
        double seg = cum[hi] - cum[lo];
        double t = seg < 1e-6 ? 0 : (s - cum[lo]) / seg;
        Point2D.Double a = path.get(lo), b = path.get(hi);
        return new Point2D.Double(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t);
    }

    /** huong duong (tangent) tai s, lay mep +-6px cho mem */
    static double[] tangentAt(double s) {
        Point2D.Double a = pointAt(s - 6), b = pointAt(s + 6);
        double dx = b.x - a.x, dy = b.y - a.y;
        double len = Math.hypot(dx, dy);
        if (len < 1e-6) return new double[]{1, 0};
        return new double[]{dx / len, dy / len};
    }

    // ---------------- ve ----------------

    static Color bodyColor(double t) {
        int r = (int) (255 + (236 - 255) * t);
        int g = (int) (206 + (133 - 206) * t);
        int b = (int) (58 + (18 - 58) * t);
        return new Color(r, g, b);
    }

    static void drawFrame(Graphics2D g, int f) {
        double sHead = total * f / FRAMES;
        double phase = 2 * Math.PI * f / FRAMES * 2.0;   // song chay 2 van/loop, huong duoi

        double[] qx = new double[SEG], qy = new double[SEG];
        double[] cxs = new double[SEG], cys = new double[SEG];
        double[] tx = new double[SEG], ty = new double[SEG];
        double[] nxs = new double[SEG], nys = new double[SEG];
        double[] rad = new double[SEG];

        for (int j = 0; j < SEG; j++) {
            double d = 8 + (double) j / (SEG - 1) * (BODY_LEN - 8);
            Point2D.Double p = pointAt(sHead - d);
            double[] t = tangentAt(sHead - d);
            double nx = -t[1], ny = t[0];
            // chon normal huong ve tam (belly huong vao nguoi, lung huong ra ngoai)
            if (nx * (CX - p.x) + ny * (CY - p.y) < 0) { nx = -nx; ny = -ny; }
            double env = Math.min(1, d / 50.0);
            double off = AMP * env * Math.sin(2 * Math.PI * d / WAVELEN - phase);
            qx[j] = p.x; qy[j] = p.y;
            tx[j] = t[0]; ty[j] = t[1];
            nxs[j] = nx; nys[j] = ny;
            cxs[j] = p.x + nx * off;
            cys[j] = p.y + ny * off;
            rad[j] = 4.2 + 11.5 * Math.pow(Math.max(0, 1 - d / BODY_LEN), 0.75);
        }

        // halo chung
        Graphics2D hg = (Graphics2D) g.create();
        hg.translate(CX, CY);
        hg.scale(1.0, 1.25);
        RadialGradientPaint halo = new RadialGradientPaint(new Point2D.Float(0, 0), 150f,
                new float[]{0f, 1f},
                new Color[]{new Color(255, 205, 70, 55), new Color(255, 205, 70, 0)});
        hg.setPaint(halo);
        hg.fill(new Ellipse2D.Double(-150, -150, 300, 300));
        hg.dispose();

        // pass 0: glow than
        g.setColor(new Color(255, 190, 55, 38));
        for (int j = 0; j < SEG; j++) {
            double r = rad[j] * 1.9;
            g.fillOval((int) (cxs[j] - r), (int) (cys[j] - r), (int) (2 * r), (int) (2 * r));
        }
        // pass 1: vien toan bo
        g.setColor(new Color(96, 54, 0));
        for (int j = 0; j < SEG; j++) {
            double r = rad[j] + 3;
            g.fillOval((int) (cxs[j] - r), (int) (cys[j] - r), (int) (2 * r), (int) (2 * r));
        }
        // pass 2: fill than
        for (int j = 0; j < SEG; j++) {
            double d = 8 + (double) j / (SEG - 1) * (BODY_LEN - 8);
            g.setColor(bodyColor(d / BODY_LEN));
            double r = rad[j];
            g.fillOval((int) (cxs[j] - r), (int) (cys[j] - r), (int) (2 * r), (int) (2 * r));
        }
        // pass 3: bong sang (huong tren-trai)
        g.setColor(new Color(255, 242, 165, 165));
        for (int j = 0; j < SEG; j += 1) {
            double r = rad[j] * 0.5;
            double ox = cxs[j] - rad[j] * 0.25, oy = cys[j] - rad[j] * 0.35;
            g.fillOval((int) (ox - r), (int) (oy - r), (int) (2 * r), (int) (2 * r));
        }
        // pass 4: bun (belly - huong ve trong)
        for (int j = 5; j < SEG; j += 2) {
            double a = Math.atan2(ty[j], tx[j]);
            Graphics2D bg = (Graphics2D) g.create();
            bg.translate(cxs[j] + nxs[j] * rad[j] * 0.5, cys[j] + nys[j] * rad[j] * 0.5);
            bg.rotate(a);
            bg.setColor(new Color(255, 246, 198, 235));
            double w = rad[j] * 1.1, h = rad[j] * 0.62;
            bg.fill(new Ellipse2D.Double(-w / 2, -h / 2, w, h));
            bg.dispose();
        }
        // pass 5: go lung (huong ra ngoai)
        for (int j = 6; j < SEG - 2; j += 3) {
            double r = rad[j];
            double apx = cxs[j] - nxs[j] * (r + r * 1.15);
            double apy = cys[j] - nys[j] * (r + r * 1.15);
            int[] xs = {(int) (cxs[j] + tx[j] * r * 0.6), (int) (apx), (int) (cxs[j] - tx[j] * r * 0.6)};
            int[] ys = {(int) (cys[j] + ty[j] * r * 0.6), (int) (apy), (int) (cys[j] - ty[j] * r * 0.6)};
            g.setColor(new Color(255, 158, 26));
            g.fillPolygon(xs, ys, 3);
            g.setColor(new Color(96, 54, 0));
            g.setStroke(new BasicStroke(1.4f));
            g.drawPolygon(xs, ys, 3);
        }
        g.setStroke(new BasicStroke(1f));

        // pass 6: duoi lua
        drawTail(g, cxs[SEG - 1], cys[SEG - 1], Math.atan2(ty[SEG - 1], tx[SEG - 1]), f);

        // pass 7: dau
        Point2D.Double hp = pointAt(sHead);
        double[] ht = tangentAt(sHead);
        drawHead(g, hp.x, hp.y, Math.atan2(ht[1], ht[0]));

        // sao lung danh
        drawSparkles(g, f);
    }

    /** duoi lua tai (x,y), huong thanh goc a (tangent = huong ve dau) */
    static void drawTail(Graphics2D g, double x, double y, double a, int f) {
        Graphics2D tg = (Graphics2D) g.create();
        tg.translate(x, y);
        tg.rotate(a); // -x = ra sau
        double fl = 1 + 0.12 * Math.sin(f * 1.7);
        // lua ngoai
        Path2D.Double p2 = new Path2D.Double();
        p2.moveTo(4, -9);
        p2.quadTo(-22 * fl, -20 * fl, -44 * fl, -4);
        p2.quadTo(-24 * fl, 2, 4, 9);
        p2.closePath();
        tg.setColor(new Color(255, 140, 20, 220));
        tg.fill(p2);
        // lua trong
        Path2D.Double p1 = new Path2D.Double();
        p1.moveTo(4, -6);
        p1.quadTo(-18 * fl, -13 * fl, -34 * fl, -2);
        p1.quadTo(-18 * fl, 3, 4, 6);
        p1.closePath();
        tg.setColor(new Color(255, 224, 110));
        tg.fill(p1);
        // man lua
        tg.setColor(new Color(255, 250, 210));
        tg.fill(new Ellipse2D.Double(-14 * fl, -4, 14 * fl, 6));
        tg.dispose();
    }

    /**
     * Dau rong, goc (0,0) = chan co, huong +x = phia truoc.
     * Bbox tam quan: x -36..60, y -42..30.
     */
    static void drawHead(Graphics2D g, double x, double y, double a) {
        Graphics2D hg = (Graphics2D) g.create();
        hg.translate(x, y);
        hg.rotate(a);

        // glow dau
        RadialGradientPaint gp = new RadialGradientPaint(new Point2D.Float(14, 0), 62f,
                new float[]{0f, 1f},
                new Color[]{new Color(255, 210, 80, 110), new Color(255, 210, 80, 0)});
        hg.setPaint(gp);
        hg.fill(new Ellipse2D.Double(-48, -62, 124, 124));

        Color out = new Color(96, 54, 0);

        // horn sau (nho,toi mau)
        Path2D.Double h2 = new Path2D.Double();
        h2.moveTo(2, -6);
        h2.quadTo(-14, -18, -32, -24);
        h2.lineTo(-27, -15);
        h2.quadTo(-12, -8, 4, 2);
        h2.closePath();
        hg.setColor(new Color(238, 216, 165));
        hg.fill(h2);
        hg.setColor(out);
        hg.setStroke(new BasicStroke(1.4f));
        hg.draw(h2);

        // ria mang tai + tai (phia sau)
        hg.setColor(new Color(255, 140, 20));
        int[] fin = {-4, -30, -6, 4, -24, -8};
        int[] finY = {-14, -26, -2, 6, 18, 12};
        hg.fillPolygon(fin, finY, 3);
        hg.drawPolygon(fin, finY, 3);

        // mane lung (tam giac nhon)
        int[] mn = {-2, -26, -8};
        int[] mnY = {-16, -30, -4};
        hg.fillPolygon(mn, mnY, 3);
        hg.drawPolygon(mn, mnY, 3);

        // horn chinh (lon, sang)
        Path2D.Double h1 = new Path2D.Double();
        h1.moveTo(6, -12);
        h1.quadTo(-12, -30, -34, -42);
        h1.lineTo(-29, -32);
        h1.quadTo(-11, -20, 10, -4);
        h1.closePath();
        hg.setColor(new Color(255, 246, 214));
        hg.fill(h1);
        hg.setColor(out);
        hg.setStroke(new BasicStroke(1.5f));
        hg.draw(h1);

        // mui (ve truoc skull)
        Ellipse2D.Double snout = new Ellipse2D.Double(20, -6.5, 28, 19);
        hg.setColor(new Color(255, 216, 80));
        hg.fill(snout);
        hg.setColor(out);
        hg.setStroke(new BasicStroke(2f));
        hg.draw(snout);

        // skull
        Ellipse2D.Double skull = new Ellipse2D.Double(-12, -15, 40, 30);
        hg.setColor(new Color(255, 206, 58));
        hg.fill(skull);
        hg.draw(skull);

        // mieng + rang
        Path2D.Double mouth = new Path2D.Double();
        mouth.moveTo(24, 6);
        mouth.quadTo(36, 10, 47, 6);
        hg.setColor(out);
        hg.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        hg.draw(mouth);
        hg.setColor(new Color(255, 252, 240));
        int[] t1 = {28, 34, 30};
        int[] t1y = {6, 6, 13};
        hg.fillPolygon(t1, t1y, 3);
        int[] t2 = {38, 43, 40};
        int[] t2y = {7, 7, 12};
        hg.fillPolygon(t2, t2y, 3);

        // chan mui
        hg.setColor(out);
        hg.fill(new Ellipse2D.Double(40, -2.5, 5, 4));

        // mat
        hg.setColor(new Color(255, 252, 238));
        Ellipse2D.Double eye = new Ellipse2D.Double(9, -10, 15, 13);
        hg.fill(eye);
        hg.setStroke(new BasicStroke(1.3f));
        hg.draw(eye);
        hg.setColor(new Color(30, 16, 0));
        hg.fill(new Ellipse2D.Double(13, -8, 7, 8.5));
        hg.setColor(Color.WHITE);
        hg.fill(new Ellipse2D.Double(15.4, -6, 2.6, 2.6));
        // long may
        hg.setColor(out);
        hg.setStroke(new BasicStroke(3.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        Path2D.Double brow = new Path2D.Double();
        brow.moveTo(6, -14);
        brow.quadTo(16, -17, 27, -10);
        hg.draw(brow);

        // ria
        hg.setStroke(new BasicStroke(2.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        hg.setColor(new Color(255, 242, 196));
        Path2D.Double w1 = new Path2D.Double();
        w1.moveTo(46, -2);
        w1.quadTo(74, -16, 46, -34);
        hg.draw(w1);
        Path2D.Double w2 = new Path2D.Double();
        w2.moveTo(46, 9);
        w2.quadTo(76, 16, 48, 32);
        hg.draw(w2);

        hg.setStroke(new BasicStroke(1f));
        hg.dispose();
    }

    static void drawSparkles(Graphics2D g, int f) {
        Random rnd = new Random(7919L + f);
        for (int k = 0; k < 12; k++) {
            double sx = 24 + rnd.nextDouble() * (W - 48);
            double sy = 24 + rnd.nextDouble() * (H - 48);
            double s = 3.5 + rnd.nextDouble() * 5.5;
            double tw = 0.3 + 0.7 * Math.abs(Math.sin(f * 0.7 + k * 1.3));
            int alpha = (int) (210 * tw);
            g.setColor(new Color(255, 245, 190, alpha));
            int n = 8;
            int[] xs = new int[n], ys = new int[n];
            for (int i = 0; i < n; i++) {
                double ang = Math.PI * 2 * i / n - Math.PI / 2;
                double rr = (i % 2 == 0) ? s : s * 0.3;
                xs[i] = (int) (sx + Math.cos(ang) * rr);
                ys[i] = (int) (sy + Math.sin(ang) * rr);
            }
            g.fillPolygon(xs, ys, n);
        }
    }

    /** icon item 42x48: chi dau rong */
    static void drawItemIcon(Graphics2D g) {
        Graphics2D ig = (Graphics2D) g.create();
        ig.translate(21, 25);
        ig.scale(0.46, 0.46);
        ig.rotate(-0.25);
        ig.translate(-6, 7);
        drawHead(ig, 0, 0, 0);
        ig.dispose();
        drawSparkles(g, 2);
    }

    // ---------------- main ----------------

    public static void main(String[] args) throws Exception {
        int first = args.length > 0 ? Integer.parseInt(args[0]) : 32347;
        buildPath();
        System.out.println("path total = " + (int) total + "px, loop = " + FRAMES + " frame");

        int[] zooms = {2, 3, 4};
        double[] sc = {1.0, 1.5, 2.0};
        for (int zi = 0; zi < zooms.length; zi++) {
            File dir = new File("data/icon_botnet/x" + zooms[zi]);
            dir.mkdirs();
            int w = (int) (W * sc[zi]), h = (int) (H * sc[zi]);
            for (int f = 0; f < FRAMES; f++) {
                BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = img.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                g.scale(sc[zi], sc[zi]);
                drawFrame(g, f);
                g.dispose();
                ImageIO.write(img, "png", new File(dir, (first + f) + ".png"));
            }
            System.out.println("x" + zooms[zi] + ": " + FRAMES + " frame " + w + "x" + h);
        }

        // icon item (dau rong) 42x48
        int iconId = first + FRAMES;
        for (int zi = 0; zi < zooms.length; zi++) {
            File dir = new File("data/icon_botnet/x" + zooms[zi]);
            int w = (int) (42 * sc[zi]), h = (int) (48 * sc[zi]);
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.scale(sc[zi], sc[zi]);
            drawItemIcon(g);
            g.dispose();
            ImageIO.write(img, "png", new File(dir, iconId + ".png"));
        }
        System.out.println("DONE frame " + first + ".." + (first + FRAMES - 1) + ", item icon " + iconId);
    }
}
