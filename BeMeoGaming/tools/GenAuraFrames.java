import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Sinh 24 frame hao quang "rong bay vong quanh nguoi (cot) len tu tu" tu sprite da cat.
 *
 * Mo hinh cot: player = truc doc tai x=180. Rong di vong elip quanh truc:
 *   - i = 0..11 : len (yBottom -> yTop), quay 1 vong day du (theta 0->2PI)
 *   - i = 12..23: xuong lai (yTop -> yBottom), quay tiep 1 vong (theta 2PI->4PI)
 *   - ease cos -> ben duong/muot, khong nhay o 2 dau.
 * Moi frame: x = cx + Rx*sin(theta), scale/alpha theo depth = cos(theta)
 *   (khop sau = nho/mo, khop truoc = to/ro), lat anh khi di sang trai de rong luon huong vao huong chay.
 * Frame 360x400 (2.5x hao quang vong tron 144x160).
 *
 * Usage: java GenAuraFrames [firstIconId]   (mac dinh 32347)
 */
public class GenAuraFrames {

    static final int W = 360, H = 400;
    static final int N = 24;
    static final double CX = 180;
    static final double RX = 96;            // ban kinh ngang vong elip
    static final double Y_BOT = 300, Y_TOP = 95;  // tam sprite luc thap/nhat
    static final double SPRITE_W = 250;     // chieu rong sprite luc o khop truoc (scale 1.0)

    static BufferedImage sprite;

    public static void main(String[] args) throws Exception {
        int first = args.length > 0 ? Integer.parseInt(args[0]) : 32347;
        sprite = ImageIO.read(new File("tools/res/dragon_fly.png"));
        int sw = sprite.getWidth(), sh = sprite.getHeight();

        int[] zooms = {2, 3, 4};
        double[] zs = {1.0, 1.5, 2.0};
        for (int zi = 0; zi < zooms.length; zi++) {
            File dir = new File("data/icon_botnet/x" + zooms[zi]);
            dir.mkdirs();
            int fw = (int) (W * zs[zi]), fh = (int) (H * zs[zi]);
            for (int i = 0; i < N; i++) {
                BufferedImage frame = new BufferedImage(fw, fh, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = frame.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g.scale(zs[zi], zs[zi]);
                drawFrame(g, i);
                g.dispose();
                ImageIO.write(frame, "png", new File(dir, (first + i) + ".png"));
            }
            System.out.println("x" + zooms[zi] + ": " + N + " frame " + fw + "x" + fh);
        }

        // icon item: dau rong (cat tu dragon_head) scale ve 42x48
        BufferedImage head = ImageIO.read(new File("tools/res/dragon_head.png"));
        int iconId = first + N;
        for (int zi = 0; zi < zooms.length; zi++) {
            File dir = new File("data/icon_botnet/x" + zooms[zi]);
            int iw = (int) (42 * zs[zi]), ih = (int) (48 * zs[zi]);
            BufferedImage icon = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = icon.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            // fit head vao 42x48 (cover, giu ty le, cat 2 ben)
            double s = Math.max((double) iw / head.getWidth(), (double) ih / head.getHeight());
            int dw = (int) (head.getWidth() * s), dh = (int) (head.getHeight() * s);
            g.drawImage(head, (iw - dw) / 2, (ih - dh) / 2, dw, dh, null);
            g.dispose();
            ImageIO.write(icon, "png", new File(dir, iconId + ".png"));
        }
        System.out.println("DONE frame " + first + ".." + (first + N - 1) + ", item icon " + iconId);
    }

    static void drawFrame(Graphics2D g, int i) {
        double p = (double) i / N;                 // 0..1 loop
        // theta quay 2 vong/trong 24 frame (1 vong len + 1 vong xuong)
        double theta = p * 4 * Math.PI;
        // y: len 0..12 (ease), xuong 12..24 (ease)
        double y;
        if (i <= N / 2) {
            double t = (double) i / (N / 2);
            y = Y_BOT + (Y_TOP - Y_BOT) * ease(t);
        } else {
            double t = (double) (i - N / 2) / (N / 2);
            y = Y_TOP + (Y_BOT - Y_TOP) * ease(t);
        }
        double x = CX + RX * Math.sin(theta);
        double depth = Math.cos(theta);            // 1 = khop truoc (giap nguoi), -1 = khop sau
        double scale = 0.72 + 0.28 * (depth + 1) / 2;     // 0.72 (sau) .. 1.0 (truoc)
        int alpha = (int) (150 + 105 * (depth + 1) / 2); // 150..255

        double dw = SPRITE_W * scale;
        double dh = dw * sprite.getHeight() / sprite.getWidth();

        // glow PHIA SAU sprite (ve truoc), radial gradient 1 lan (khong dung vong tron chong len)
        if (depth > 0.75) {
            Graphics2D lg = (Graphics2D) g.create();
            float gr = (float) (dw * 0.5);
            RadialGradientPaint rgp = new RadialGradientPaint(
                    new Point2D.Float((float) x, (float) y), gr,
                    new float[]{0f, 0.6f, 1f},
                    new Color[]{new Color(170, 90, 255, 46),
                                new Color(170, 90, 255, 18),
                                new Color(170, 90, 255, 0)});
            lg.setPaint(rgp);
            lg.fill(new Ellipse2D.Double(x - gr, y - gr, gr * 2, gr * 2));
            lg.dispose();
        }

        // huong chay: dx/d theta = cos(theta); luc depth<0 -> sang trai hoac phai tuy sin
        // flip khi di sang TRAI (velocity x < 0). velocity x ~ cos(theta) * ... = depth
        boolean flip = (theta % (2 * Math.PI) > Math.PI / 2 && theta % (2 * Math.PI) < 3 * Math.PI / 2);
        // velocity x = d(sin)/d theta * dtheta = cos(theta) -> >0 thi sang phai
        flip = depth < 0;

        Graphics2D sg = (Graphics2D) g.create();
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f);
        sg.setComposite(ac);
        sg.translate(x, y);
        if (flip) sg.scale(-1, 1);
        int dxw = (int) dw, dxh = (int) dh;
        // thieu sang ben trai: flip thi van giu tam
        sg.drawImage(sprite, -dxw / 2, -dxh / 2, dxw, dxh, null);
        sg.dispose();
    }

    static double ease(double t) {
        return 0.5 - 0.5 * Math.cos(Math.PI * t);
    }
}
