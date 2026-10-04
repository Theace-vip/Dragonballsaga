import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import java.util.Base64;

/**
 * GenAura - sinh hao quang moi id 43 (pulsing halo) tu codegen DataEffect.
 * Format da reverse (xem DataEffectCodec):
 *   section1: [N][N x (idx,x,y,w,h)]      - rect frame trong sheet
 *   section2: [00][H] + H frame [c][c x (dx s16be, dy s16be, sprite)]
 *   section3: [count][count x frameIdx][3232]
 * Output: Eff/effect/x{1..4}/data/DataEffect_43 + img/ImgEffect_43.png + preview html.
 * Usage: java GenAura <root_BeMeoGaming>
 */
public class GenAura {

    static final int FW = 72, FH = 80, NF = 8; // kich thuoc frame goc (x1), so frame
    static int ID = 43;                          // id hieu ung (set qua arg)
    static final int COLS = 3, ROWS = 3;       // pack 2D vi rect field la u8 (<=255)
    static final int DX = -36, DY = -75;       // top-left anchor: trung tam vong (36,40) -> (-,-35) tren nguoi (chan = 0)
    static final float TAU = (float) (Math.PI * 2);

    static Color C(int r, int g, int b, float a) { return new Color(r / 255f, g / 255f, b / 255f, Math.min(1f, Math.max(0f, a))); }

    /** sao 4 canh */
    static void star(Graphics2D g, float x, float y, float r, float a) {
        if (a <= 0.02f) return;
        GeneralPath p = new GeneralPath();
        float in = r * 0.16f;
        p.moveTo(x, y - r);
        p.quadTo(x + in, y - in, x + r, y);
        p.quadTo(x + in, y + in, x, y + r);
        p.quadTo(x - in, y + in, x - r, y);
        p.quadTo(x - in, y - in, x, y - r);
        p.closePath();
        g.setColor(C(240, 248, 255, a));
        g.fill(p);
    }

    static void drawFrame(Graphics2D g, int i) {
        float ph = i / (float) NF;
        float ang = ph * TAU;
        float pulse = 0.5f + 0.5f * (float) Math.sin(ang);
        float cx = 36, cy = 40;
        float rx = 28.5f + 1.6f * (float) Math.sin(ang);
        float ry = 32.5f + 1.6f * (float) Math.cos(ang);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        // 1) ambient glow elip (giua de trong de thay nhan vat)
        Graphics2D g1 = (Graphics2D) g.create();
        g1.translate(cx, cy);
        g1.scale(1f, ry / rx);
        RadialGradientPaint amb = new RadialGradientPaint(0, 0, rx,
                new float[]{0f, 0.5f, 0.78f, 1f},
                new Color[]{
                        C(90, 160, 255, 0.02f),
                        C(90, 160, 255, 0.10f + 0.05f * pulse),
                        C(150, 110, 255, 0.12f + 0.05f * pulse),
                        C(150, 110, 255, 0f)});
        g1.setPaint(amb);
        g1.fill(new Ellipse2D.Float(-rx, -rx, 2 * rx, 2 * rx));
        g1.dispose();

        // 2) vong chinh (3 lop: mem -> core)
        Ellipse2D ring = new Ellipse2D.Float(cx - rx, cy - ry, 2 * rx, 2 * ry);
        g.setStroke(new BasicStroke(9f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(C(80, 150, 255, 0.10f + 0.06f * pulse));
        g.draw(ring);
        g.setStroke(new BasicStroke(3.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(C(130, 205, 255, 0.50f + 0.20f * pulse));
        g.draw(ring);
        g.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(C(235, 246, 255, 0.90f));
        g.draw(ring);

        // 3) 4 doan arc tim quay
        float rot = ph * 90f;
        g.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(C(178, 118, 255, 0.45f + 0.30f * pulse));
        for (int k = 0; k < 4; k++) {
            g.draw(new Arc2D.Float(cx - rx, cy - ry, 2 * rx, 2 * ry, rot + k * 90f + 12f, 52f, Arc2D.OPEN));
        }

        // 4) 6 khoanh sao quay quanh vong
        for (int k = 0; k < 6; k++) {
            float a = (float) Math.toRadians(k * 60 + ph * 60);
            float px = cx + (rx + 4f) * (float) Math.cos(a);
            float py = cy + (ry + 4f) * (float) Math.sin(a);
            float tw = 0.35f + 0.65f * (0.5f + 0.5f * (float) Math.sin(TAU * (ph + k / 6f)));
            star(g, px, py, 1.8f + 2.4f * tw, 0.35f + 0.55f * tw);
        }

        // 5) hat than bay len
        for (int k = 0; k < 7; k++) {
            float ex = 11 + ((k * 29) % 50);
            int tick = (i * 9 + k * 23) % 74;
            float ey = FH - 6 - tick;
            float prog = tick / 74f;
            float ea = (1 - prog) * 0.75f + 0.08f;
            float sz = 1.6f - 0.7f * prog;
            g.setColor(C(255, 220, 140, ea));
            g.fill(new Ellipse2D.Float(ex - sz / 2, ey - sz / 2, sz, sz));
        }

        // 6) glow chan
        Graphics2D g6 = (Graphics2D) g.create();
        g6.translate(cx, 75);
        g6.scale(1f, 0.26f);
        RadialGradientPaint fg = new RadialGradientPaint(0, 0, 18,
                new float[]{0f, 0.6f, 1f},
                new Color[]{C(120, 190, 255, 0.22f + 0.10f * pulse), C(120, 190, 255, 0.10f), C(120, 190, 255, 0f)});
        g6.setPaint(fg);
        g6.fill(new Ellipse2D.Float(-18, -18, 36, 36));
        g6.dispose();
    }

    static BufferedImage buildSheet(int z) {
        BufferedImage img = new BufferedImage(FW * COLS * z, FH * ROWS * z, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.scale(z, z);
        for (int i = 0; i < NF; i++) {
            Graphics2D gf = (Graphics2D) g.create((i % COLS) * FW, (i / COLS) * FH, FW, FH);
            drawFrame(gf, i);
            gf.dispose();
        }
        g.dispose();
        return img;
    }

    static byte[] buildData() {
        DataEffectCodec.Data d = new DataEffectCodec.Data();
        d.rects = new DataEffectCodec.Rect[NF];
        for (int i = 0; i < NF; i++)
            d.rects[i] = new DataEffectCodec.Rect(i, (i % COLS) * FW, (i / COLS) * FH, FW, FH);
        d.frames = new DataEffectCodec.Part[NF][];
        for (int i = 0; i < NF; i++) d.frames[i] = new DataEffectCodec.Part[]{new DataEffectCodec.Part(DX, DY, i)};
        d.seq = new int[NF * 2];
        for (int i = 0; i < NF; i++) { d.seq[2 * i] = i; d.seq[2 * i + 1] = i; }
        return DataEffectCodec.write(d);
    }

    static String b64(Path p) throws Exception {
        return Base64.getEncoder().encodeToString(Files.readAllBytes(p));
    }

    static String stage(String b64, int scale, String label) {
        // stage 140x140; chan tai (70,115); aura top-left = (70-36, 115-75) = (34,40) * scale
        int s = scale;
        StringBuilder sb = new StringBuilder();
        sb.append("<div style=\"display:inline-block;text-align:center;margin:8px\">");
        sb.append("<div style=\"position:relative;width:").append(140 * s).append("px;height:").append(140 * s)
          .append("px;overflow:hidden;background:#10131a;border:1px solid #333\">");
        // silhouette: rong 26 cao 65, chan tai y=115, trung tam x=70
        sb.append("<svg style=\"position:absolute;left:").append((70 - 13) * s).append("px;top:")
          .append((115 - 65) * s).append("px\" width=\"").append(26 * s).append("\" height=\"").append(65 * s)
          .append("\" viewBox=\"0 0 26 65\"><ellipse cx=\"13\" cy=\"7\" rx=\"6\" ry=\"7\" fill=\"#666\"/>")
          .append("<path d=\"M5 16 h16 v26 l-3 20 h-4 l-1 -18 l-1 18 h-4 l-3 -20 z\" fill=\"#666\"/></svg>");
        // aura: viewport 72x80 tai (34,40); img sheet day, dich frame i = (col,row)
        sb.append("<div class=\"auraView\" style=\"position:absolute;left:").append(34 * s).append("px;top:")
          .append(40 * s).append("px;width:").append(FW * s).append("px;height:").append(FH * s)
          .append("px;overflow:hidden\">");
        sb.append("<img class=\"auraImg\" data-fw=\"").append(FW * s).append("\" data-fh=\"").append(FH * s)
          .append("\" data-cols=\"").append(COLS).append("\" data-nf=\"").append(NF)
          .append("\" src=\"data:image/png;base64,").append(b64)
          .append("\" style=\"position:absolute;left:0;top:0;width:")
          .append(FW * COLS * s).append("px;height:").append(FH * ROWS * s).append("px\">");
        sb.append("</div></div><div style=\"color:#888;font:12px sans-serif\">").append(label).append("</div></div>");
        return sb.toString();
    }

    public static void main(String[] a) throws Exception {
        Path root = Paths.get(a.length > 0 ? a[0] : ".");
        if (a.length > 1) ID = Integer.parseInt(a[1]);
        byte[] data = buildData();
        // sanity: roundtrip
        DataEffectCodec.Data chk = DataEffectCodec.parse(data);
        byte[] again = DataEffectCodec.write(chk);
        if (!java.util.Arrays.equals(data, again)) throw new IllegalStateException("roundtrip FAIL");

        Path[] imgPaths = new Path[4];
        for (int z = 1; z <= 4; z++) {
            BufferedImage sheet = buildSheet(z);
            Path ip = root.resolve("Eff/effect/x" + z + "/img/ImgEffect_" + ID + ".png");
            ImageIO.write(sheet, "png", ip.toFile());
            Path dp = root.resolve("Eff/effect/x" + z + "/data/DataEffect_" + ID);
            Files.write(dp, data);
            imgPaths[z - 1] = ip;
            System.out.println("x" + z + ": sheet " + sheet.getWidth() + "x" + sheet.getHeight() + " -> " + ip + " + " + dp);
        }

        // preview html (embed base64)
        String s1 = b64(imgPaths[0]);
        String s4 = b64(imgPaths[3]);
        StringBuilder html = new StringBuilder();
        html.append("<!doctype html><html><head><meta charset=\"utf-8\"><title>Aura 43 preview</title></head><body>")
            .append("<body style=\"background:#181b22;color:#ccc;font:14px sans-serif\">")
            .append("<h3>DataEffect/ImgEffect id=43 - pulsing halo (codegen)</h3>")
            .append("Anchor: dx=").append(DX).append(" dy=").append(DY).append(" (chan = 0), ")
            .append(NF).append(" frame, seq 2 tick/frame<br>")
            .append("<div>").append(stage(s1, 1, "x1 72x80/frame (scale 1)"))
            .append(stage(s4, 4, "x4 288x320/frame (scale 4)")).append("</div>")
            .append("<h4>Sheet x1</h4><div style=\"background:#10131a;display:inline-block;padding:4px\">")
            .append("<img src=\"data:image/png;base64,").append(s1).append("\" style=\"image-rendering:pixelated\"></div>")
            .append("<h4>Sheet x4</h4><div style=\"background:#10131a;display:inline-block;padding:4px;overflow:auto\">")
            .append("<img src=\"data:image/png;base64,").append(s4).append("\"></div>")
            .append("<script>var t=0;setInterval(function(){t++;")
            .append("document.querySelectorAll('.auraImg').forEach(function(im){")
            .append("var nf=+im.dataset.nf,fw=+im.dataset.fw,fh=+im.dataset.fh,cols=+im.dataset.cols,f=t%nf;")
            .append("im.style.left=(-((f%cols)*fw))+'px';im.style.top=(-(Math.floor(f/cols)*fh))+'px';});},140);</script>")
            .append("</body></html>");
        Path htmlPath = root.resolve("data/anh_the/aura43.html");
        Files.write(htmlPath, html.toString().getBytes("UTF-8"));
        System.out.println("preview: " + htmlPath);
    }
}
