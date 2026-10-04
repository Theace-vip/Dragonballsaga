import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Sinh 42 frame hao quang "Goku Vo Cuc" tu anh da tach lop boi SplitGokuAura:
 *   tools/res/goku_base.png    - Goku + khien hieu (GIU NGUYEN, khong di chuyen)
 *   tools/res/goku_layers.png  - atlas 103 sprite; goku_layers.txt = meta
 *                                moi dong: type x y w h atlasY  (type 0=tia set, 1=da bay)
 *
 * Hieu ung moi frame (vong lap khop chinh 42 frame):
 *   - Da bay     : sin nhip rieng tung sprite (dx/dy/rot/scale), sap xep theo chieu sau
 *   - Tia set    : alpha strobe + jitter + dropout ngau nhien -> chay lien tuc
 *   - Soi hau quang sau Goku: radial gradient breathing (xanh/tim)
 *   - 6 net laze procedural quanh nguoi: sang theo lich, strobe manh
 *   - 48 hat sang (sparkle) bay len + tan ra
 *   - Cho duoi chan + 2 lan flash toan khung moi vong
 *
 * Frame co ban 1080x1200. Usage: java GenGokuAura [firstIconId] [frameCount]
 * Output: data/icon_botnet/x{2,3,4}/<id>.png + icon + contact sheet.
 */
public final class GenGokuAura {
    static final int W = 300, H = 300;          // frame co ban 300x300 (x2) - dung khop hao quang game
    static final double K = W / 1080.0;         // ty le hieu ung so voi ban ve 1080 cu
    static final int DEFAULT_FIRST = 32458, DEFAULT_FRAMES = 42;
    static final int[] ZOOMS = {2, 3, 4};
    static final double[] ZOOM_SCALES = {1.0, 1.5, 2.0};

    static BufferedImage base, atlas;
    static int srcW, srcH, nSprite;
    static int[] tType, sX, sY, sW, sH, aY;
    static double scale, offX, offY;
    static int F;

    // net laze (toa do khung da quy doi)
    static final int N_ARC = 6;
    static double[][] arcX, arcY;
    static int[] arcN;
    static double[] arcPhase, arcDuty;

    // hat sang
    static final int N_SPARK = 48;
    static double[] spX, spY0, spRise, spPhase, spSize, spWob;
    static int[] spHue;

    private GenGokuAura() { }

    public static void main(String[] a) throws Exception {
        int first = a.length > 0 ? Integer.parseInt(a[0]) : DEFAULT_FIRST;
        F = a.length > 1 ? Integer.parseInt(a[1]) : DEFAULT_FRAMES;
        if (F < 24 || F > 42) throw new IllegalArgumentException("frameCount must be 24..42");

        base = ImageIO.read(new File("tools/res/goku_base.png"));
        atlas = ImageIO.read(new File("tools/res/goku_layers.png"));
        List<String> lines = Files.readAllLines(new File("tools/res/goku_layers.txt").toPath(),
                StandardCharsets.UTF_8);
        String[] hd = lines.get(0).trim().split("\\s+");
        srcW = Integer.parseInt(hd[0]);
        srcH = Integer.parseInt(hd[1]);
        nSprite = Integer.parseInt(hd[2]);
        tType = new int[nSprite]; sX = new int[nSprite]; sY = new int[nSprite];
        sW = new int[nSprite]; sH = new int[nSprite]; aY = new int[nSprite];
        for (int i = 0; i < nSprite; i++) {
            String[] p = lines.get(i + 1).trim().split("\\s+");
            tType[i] = Integer.parseInt(p[0]);
            sX[i] = Integer.parseInt(p[1]); sY[i] = Integer.parseInt(p[2]);
            sW[i] = Integer.parseInt(p[3]); sH[i] = Integer.parseInt(p[4]);
            aY[i] = Integer.parseInt(p[5]);
        }
        scale = Math.min(W / (double) srcW, H / (double) srcH);
        offX = (W - srcW * scale) / 2.0;
        offY = (H - srcH * scale) / 2.0;
        System.out.printf("base %dx%d x%d sprite, scale=%.3f off=(%.0f,%.0f)%n",
                srcW, srcH, nSprite, scale, offX, offY);

        buildArcs();
        buildSparks();
        renderFrames(first);
        renderIcon(first + F);
        renderContact(first);
        System.out.printf("DONE goku-aura frames %d..%d; icon %d%n",
                first, first + F - 1, first + F);
    }

    // ---------- hieu ung phu ----------

    static void buildArcs() {
        Random rnd = new Random(7);
        double cx = srcW / 2.0, cy = srcH * 0.47;
        arcX = new double[N_ARC][]; arcY = new double[N_ARC][];
        arcN = new int[N_ARC]; arcPhase = new double[N_ARC]; arcDuty = new double[N_ARC];
        for (int k = 0; k < N_ARC; k++) {
            double ang0 = rnd.nextDouble() * Math.PI * 2;
            double span = 0.9 + rnd.nextDouble() * 0.9;
            double rad = 400 + rnd.nextDouble() * 80;
            int n = 22;
            double[] xs = new double[n], ys = new double[n];
            for (int i = 0; i < n; i++) {
                double u = i / (double) (n - 1);
                double ang = ang0 + (u - 0.5) * span;
                double r = rad + (rnd.nextDouble() - 0.5) * 55;
                double x = cx + r * Math.cos(ang);
                double y = cy + r * 1.08 * Math.sin(ang);
                // dut doc nho cho co set
                if (i > 0 && i < n - 1) {
                    x += (rnd.nextDouble() - 0.5) * 34;
                    y += (rnd.nextDouble() - 0.5) * 34;
                }
                xs[i] = offX + x * scale;
                ys[i] = offY + y * scale;
            }
            arcX[k] = xs; arcY[k] = ys; arcN[k] = n;
            arcPhase[k] = rnd.nextDouble();
            arcDuty[k] = 0.16 + rnd.nextDouble() * 0.12;
        }
    }

    static void buildSparks() {
        Random rnd = new Random(11);
        spX = new double[N_SPARK]; spY0 = new double[N_SPARK];
        spRise = new double[N_SPARK]; spPhase = new double[N_SPARK];
        spSize = new double[N_SPARK]; spWob = new double[N_SPARK];
        spHue = new int[N_SPARK];
        for (int i = 0; i < N_SPARK; i++) {
            spX[i] = 70 * K + rnd.nextDouble() * (W - 140 * K);
            spY0[i] = H - 30 * K - rnd.nextDouble() * 180 * K;
            spRise[i] = (520 + rnd.nextDouble() * 420) * K;
            spPhase[i] = rnd.nextDouble();
            spSize[i] = (3.5 + rnd.nextDouble() * 5.5) * K;
            spWob[i] = (8 + rnd.nextDouble() * 14) * K;
            spHue[i] = rnd.nextInt(3); // 0=trang 1=xanh 2=tim
        }
    }

    /** Alpha cua net laze tai frame t (strobe sang tat). */
    static double arcAlpha(int k, int t) {
        double u = ((t / (double) F) + arcPhase[k]) % 1.0;
        double d = arcDuty[k];
        if (u >= d) return 0;
        double s = Math.sin(Math.PI * u / d);
        return s * s; // sac, dep hon khi nhap
    }

    static double breath(double t, double cycles) {
        return 0.5 + 0.5 * Math.cos(2 * Math.PI * (cycles * t / F));
    }

    // ---------- render ----------

    static void renderFrames(int first) throws Exception {
        for (int z = 0; z < ZOOMS.length; z++) {
            double zoom = ZOOM_SCALES[z];
            File dir = new File("data/icon_botnet/x" + ZOOMS[z]);
            dir.mkdirs();
            int fw = (int) (W * zoom), fh = (int) (H * zoom);
            for (int t = 0; t < F; t++) {
                BufferedImage frame = new BufferedImage(fw, fh, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = frame.createGraphics();
                setup(g);
                g.scale(zoom, zoom);
                drawFrame(g, t);
                g.dispose();
                ImageIO.write(frame, "png", new File(dir, (first + t) + ".png"));
            }
            System.out.printf("x%d: %d goku frames %dx%d%n", ZOOMS[z], F, fw, fh);
        }
    }

    static void drawFrame(Graphics2D g, int t) {
        double cx = offX + (srcW / 2.0) * scale;

        // 1. soi hau quang sau Goku (breathing)
        double b1 = breath(t, 2), b2 = breath(t + F / 3.0, 3);
        int gy = (int) (offY + srcH * 0.46 * scale);
        drawRadial(g, cx, gy, (int) ((540 + 30 * b1) * K),
                new Color(150, 205, 255, (int) (52 + 40 * b1)),
                new Color(150, 205, 255, 0));
        drawRadial(g, cx, gy + 40 * K, (int) ((430 + 35 * b2) * K),
                new Color(175, 125, 255, (int) (44 + 34 * b2)),
                new Color(175, 125, 255, 0));

        // 2. base - GIU NGUYEN Goku
        AffineTransform at = AffineTransform.getTranslateInstance(offX, offY);
        at.scale(scale, scale);
        g.drawImage(base, at, null);

        // 3. da bay (sap xep chieu sau tang dan theo y goc)
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < nSprite; i++) if (tType[i] == 1) order.add(i);
        order.sort((p, q) -> Integer.compare(sY[p], sY[q]));
        for (int i : order) {
            double p = i * 0.6180339887 % 1.0;
            double dx = 6.5 * K * Math.sin(2 * Math.PI * (2.0 * t / F + p));
            double dy = 8.5 * K * Math.sin(2 * Math.PI * (3.0 * t / F + p * 1.7));
            double rot = Math.toRadians(6) * Math.sin(2 * Math.PI * (2.0 * t / F + p * 0.4));
            double sc = 1 + 0.035 * Math.sin(2 * Math.PI * (4.0 * t / F + p * 2.3));
            drawSprite(g, i, dx, dy, rot, sc, 1.0f);
        }

        // 4. tia set - nhap nhat lien tuc
        for (int i = 0; i < nSprite; i++) {
            if (tType[i] != 0) continue;
            double p = i * 0.7548776662 % 1.0;
            double strobe = 0.5 + 0.5 * Math.sin(2 * Math.PI * (3.0 * t / F + p * 3.1));
            strobe = Math.pow(strobe, 1.6);
            double alpha = 0.45 + 0.55 * strobe;
            double drop = hash01(i * 131 + t * 977);
            if (drop < 0.07) alpha *= 0.18;      // rut ngat dot ngat
            double dx = Math.max(1.0, 2.2 * K) * Math.sin(2 * Math.PI * (5.0 * t / F + p * 5.0));
            double dy = Math.max(1.0, 2.2 * K) * Math.cos(2 * Math.PI * (4.0 * t / F + p * 7.0));
            double sc = 1 + 0.03 * Math.sin(2 * Math.PI * (3.0 * t / F + p));
            drawSprite(g, i, dx, dy, 0, sc, (float) alpha);
        }

        // 5. net laze procedural
        for (int k = 0; k < N_ARC; k++) {
            double a = arcAlpha(k, t);
            if (a <= 0.01) continue;
            int n = arcN[k];
            for (int pass = 0; pass < 2; pass++) {
                g.setStroke(new BasicStroke(
                        (float) Math.max(2.0, 9 * K),
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                float coreW = (float) Math.max(1.2, 3.2 * K);
                if (pass == 0) {
                    g.setColor(new Color(130, 205, 255, (int) (110 * a)));
                } else {
                    g.setStroke(new BasicStroke(coreW, BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND));
                    g.setColor(new Color(255, 255, 255, (int) (235 * a)));
                }
                for (int i = 0; i < n - 1; i++)
                    g.drawLine((int) arcX[k][i], (int) arcY[k][i],
                            (int) arcX[k][i + 1], (int) arcY[k][i + 1]);
            }
        }

        // 6. hat sang bay len
        for (int i = 0; i < N_SPARK; i++) {
            double u = (t / (double) F + spPhase[i]) % 1.0;
            double y = spY0[i] - u * spRise[i];
            double x = spX[i] + spWob[i] * Math.sin(2 * Math.PI * (2.0 * u + spPhase[i]));
            double a = Math.sin(Math.PI * u);
            if (a <= 0.02) continue;
            int rgb = spHue[i] == 0 ? 0xffffff : (spHue[i] == 1 ? 0xaee4ff : 0xcdb4ff);
            g.setColor(new Color((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255,
                    (int) (215 * a)));
            drawStar(g, x, y, spSize[i] * (0.7 + 0.5 * a));
        }

        // 7. cho duoi chan (ellipse mem, khong cham bien khung)
        double gb = breath(t + F / 4.0, 2);
        drawRadialOval(g, cx, H - 78 * K, 300 * K, 72 * K,
                new Color(205, 232, 255, (int) (30 + 30 * gb)),
                new Color(205, 232, 255, 0));

        // 8. flash toan khung 2 lan/vong (disc tron, nam trong khung)
        double fl = flash(t);
        if (fl > 0.01)
            drawRadial(g, cx, gy, (int) (430 * K),
                    new Color(235, 245, 255, (int) (34 * fl)),
                    new Color(235, 245, 255, 0));
    }

    /** 2 lan flash moi vong, tang dan roi tat ngot. */
    static double flash(int t) {
        double best = 0;
        for (int c = 0; c < 2; c++) {
            double center = (c * F / 2 + F / 6) % F;
            double d = Math.abs(t - center);
            d = Math.min(d, F - d);
            if (d < 2.5) {
                double v = 1 - d / 2.5;
                best = Math.max(best, v * v);
            }
        }
        return best;
    }

    static double hash01(int v) {
        v = (v ^ 61) ^ (v >>> 16);
        v = v * 7;
        v = v ^ (v >>> 4);
        v *= 0x27d4eb2d;
        v ^= v >>> 15;
        return (v & 0x7fffffff) / (double) 0x7fffffff;
    }

    static void drawSprite(Graphics2D g, int i, double dx, double dy, double rot,
                           double sc, float alpha) {
        BufferedImage spr = atlas.getSubimage(0, aY[i], sW[i], sH[i]);
        Graphics2D s = (Graphics2D) g.create();
        s.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        AffineTransform at = new AffineTransform();
        at.translate(offX + (sX[i] + sW[i] / 2.0) * scale + dx,
                offY + (sY[i] + sH[i] / 2.0) * scale + dy);
        at.rotate(rot);
        at.scale(scale * sc, scale * sc);
        at.translate(-sW[i] / 2.0, -sH[i] / 2.0);
        s.setTransform(at);
        s.drawImage(spr, 0, 0, null);
        s.dispose();
    }

    static void drawRadial(Graphics2D g, double cx, double cy, double r,
                           Color inner, Color outer) {
        RadialGradientPaint gp = new RadialGradientPaint(
                new Point2D.Double(cx, cy), (float) r,
                new float[]{0f, 1f},
                new Color[]{inner, outer});
        g.setPaint(gp);
        g.fill(new java.awt.geom.Ellipse2D.Double(cx - r, cy - r, r * 2, r * 2));
    }

    /** Gradient elip (dung GradientTransform de nhan 2 truc) - cho glow duoi chan. */
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
                                inner.getAlpha() / 3),
                        outer},
                RadialGradientPaint.CycleMethod.NO_CYCLE,
                RadialGradientPaint.ColorSpaceType.SRGB,
                gt);
        g.setPaint(gp);
        g.fill(new java.awt.geom.Ellipse2D.Double(cx - rx, cy - ry, rx * 2, ry * 2));
    }

    static void drawStar(Graphics2D g, double x, double y, double s) {
        int[] vx = {(int) x, (int) (x + s * 0.42), (int) x, (int) (x - s * 0.42)};
        int[] vy = {(int) (y - s * 2.1), (int) y, (int) (y + s * 2.1), (int) y};
        g.fillPolygon(vx, vy, 4);
        int[] hx = {(int) (x - s * 1.7), (int) x, (int) (x + s * 1.7), (int) x};
        int[] hy = {(int) y, (int) (y - s * 0.5), (int) y, (int) (y + s * 0.5)};
        g.fillPolygon(hx, hy, 4);
    }

    // ---------- icon + contact ----------

    static void renderIcon(int iconId) throws Exception {
        // crop dau + than tren Goku (giu trong base da bao ve)
        int hx0 = 355, hy0 = 55, hx1 = 720, hy1 = 405;
        for (int z = 0; z < ZOOMS.length; z++) {
            double zoom = ZOOM_SCALES[z];
            int w = (int) (42 * zoom), h = (int) (48 * zoom);
            BufferedImage icon = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = icon.createGraphics();
            setup(g);
            double s = Math.min((w - 4) / (double) (hx1 - hx0), (h - 4) / (double) (hy1 - hy0));
            g.scale(s, s);
            g.translate(-hx0, -hy0);
            g.drawImage(base, 0, 0, null);
            g.dispose();
            ImageIO.write(icon, "png",
                    new File("data/icon_botnet/x" + ZOOMS[z] + "/" + iconId + ".png"));
        }
        System.out.printf("icon %d = crop (%d,%d)-(%d,%d)%n", iconId, hx0, hy0, hx1, hy1);
    }

    static void renderContact(int first) throws Exception {
        int cols = 7, cellW = 140, cellH = 160, rows = (F + cols - 1) / cols;
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
            g.drawImage(frame, x + 3, y + 2, cellW - 6, cellH - 23, null);
            g.setColor(Color.WHITE);
            g.drawString("f" + i, x + 6, y + cellH - 5);
        }
        g.dispose();
        ImageIO.write(sheet, "png", new File("data/anh_the/goku_contact.png"));
        System.out.println("-> data/anh_the/goku_contact.png");
    }

    static void setup(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
}
