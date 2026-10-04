import java.awt.*;
import java.awt.image.*;
import java.nio.file.*;
import java.util.Base64;
import javax.imageio.*;

public class GenIcons3 {

    static boolean nearWhite(int p) {
        int r = (p >> 16) & 0xFF, g = (p >> 8) & 0xFF, b = p & 0xFF;
        int mx = Math.max(r, Math.max(g, b)), mn = Math.min(r, Math.min(g, b));
        return mn >= 235 && (mx - mn) <= 20;
    }

    static BufferedImage copyArgb(BufferedImage s) {
        int w = s.getWidth(), h = s.getHeight();
        BufferedImage d = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                d.setRGB(x, y, s.getRGB(x, y));
        return d;
    }

    static BufferedImage keyWhite(BufferedImage s) {
        int w = s.getWidth(), h = s.getHeight();
        BufferedImage d = copyArgb(s);
        boolean[] seen = new boolean[w * h];
        int[] q = new int[w * h];
        int head = 0, tail = 0;
        for (int x = 0; x < w; x++) {
            if (nearWhite(d.getRGB(x, 0)) && !seen[x]) { seen[x] = true; q[tail++] = x; }
            int bi = (h - 1) * w + x;
            if (nearWhite(d.getRGB(x, h - 1)) && !seen[bi]) { seen[bi] = true; q[tail++] = bi; }
        }
        for (int y = 0; y < h; y++) {
            if (nearWhite(d.getRGB(0, y)) && !seen[y * w]) { seen[y * w] = true; q[tail++] = y * w; }
            int ri = y * w + w - 1;
            if (nearWhite(d.getRGB(w - 1, y)) && !seen[ri]) { seen[ri] = true; q[tail++] = ri; }
        }
        int[] dx = {1, -1, 0, 0}, dy = {0, 0, 1, -1};
        while (head < tail) {
            int idx = q[head++];
            int x = idx % w, y = idx / w;
            for (int k = 0; k < 4; k++) {
                int nx = x + dx[k], ny = y + dy[k];
                if (nx < 0 || ny < 0 || nx >= w || ny >= h) continue;
                int nidx = ny * w + nx;
                if (!seen[nidx] && nearWhite(d.getRGB(nx, ny))) { seen[nidx] = true; q[tail++] = nidx; }
            }
        }
        for (int i = 0; i < seen.length; i++) if (seen[i]) d.setRGB(i % w, i / w, 0);
        return d;
    }

    static Rectangle bbox(BufferedImage img, int thr) {
        int w = img.getWidth(), h = img.getHeight();
        int minX = w, minY = h, maxX = -1, maxY = -1;
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                int a = (img.getRGB(x, y) >>> 24) & 0xFF;
                if (a > thr) {
                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;
                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;
                }
            }
        if (maxX < 0) return null;
        int mx = (maxX - minX) / 50 + 2, my = (maxY - minY) / 50 + 2;
        minX = Math.max(0, minX - mx); minY = Math.max(0, minY - my);
        maxX = Math.min(w - 1, maxX + mx); maxY = Math.min(h - 1, maxY + my);
        return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }

    // box filter: tinh trung binh PREMULTIPLIED -> khong con bien mo/halo nhu bilinear mot lan
    static BufferedImage scaleArea(BufferedImage s, int tw, int th) {
        int sw = s.getWidth(), sh = s.getHeight();
        int[] src = s.getRGB(0, 0, sw, sh, null, 0, sw);
        BufferedImage d = new BufferedImage(tw, th, BufferedImage.TYPE_INT_ARGB);
        int[] out = new int[tw * th];
        for (int ty = 0; ty < th; ty++) {
            double y0 = (double) ty * sh / th, y1 = (double) (ty + 1) * sh / th;
            int iy0 = (int) y0, iy1 = Math.min(sh - 1, (int) Math.ceil(y1) - 1);
            for (int tx = 0; tx < tw; tx++) {
                double x0 = (double) tx * sw / tw, x1 = (double) (tx + 1) * sw / tw;
                int ix0 = (int) x0, ix1 = Math.min(sw - 1, (int) Math.ceil(x1) - 1);
                double sa = 0, sr = 0, sg = 0, sb = 0, sw2 = 0;
                for (int y = iy0; y <= iy1; y++) {
                    double fy0 = Math.max(y, y0), fy1 = Math.min(y + 1, y1);
                    double wy = Math.max(0, fy1 - fy0);
                    if (wy <= 0) continue;
                    for (int x = ix0; x <= ix1; x++) {
                        double fx0 = Math.max(x, x0), fx1 = Math.min(x + 1, x1);
                        double wx = Math.max(0, fx1 - fx0);
                        if (wx <= 0) continue;
                        double wgt = wx * wy;
                        int p = src[y * sw + x];
                        double a = (p >>> 24) & 0xFF;
                        sr += ((p >> 16) & 0xFF) * a * wgt;
                        sg += ((p >> 8) & 0xFF) * a * wgt;
                        sb += (p & 0xFF) * a * wgt;
                        sa += a * wgt;
                        sw2 += wgt;
                    }
                }
                int na = (int) Math.round(sa / Math.max(1, sw2));
                int nr, ng, nb;
                if (sa > 0) {
                    nr = (int) Math.round(sr / sa);
                    ng = (int) Math.round(sg / sa);
                    nb = (int) Math.round(sb / sa);
                } else { nr = ng = nb = 0; }
                out[ty * tw + tx] = (na << 24) | (nr << 16) | (ng << 8) | nb;
            }
        }
        d.setRGB(0, 0, tw, th, out, 0, tw);
        return d;
    }

    // sharpen nhe sau khi giam: new = p + amount*(p - blur3x3), chi sua pixel day mau
    static BufferedImage sharpen(BufferedImage s, double amount) {
        int w = s.getWidth(), h = s.getHeight();
        int[] src = s.getRGB(0, 0, w, h, null, 0, w);
        int[] out = src.clone();
        for (int y = 1; y < h - 1; y++)
            for (int x = 1; x < w - 1; x++) {
                int i = y * w + x;
                int a = (src[i] >>> 24) & 0xFF;
                if (a < 200) continue;
                for (int c = 0; c < 3; c++) {
                    int sh = c == 16 ? 16 : (c == 8 ? 8 : 0);
                    int p = (src[i] >> sh) & 0xFF;
                    int up = (src[i - 1] >> sh) & 0xFF, dn = (src[i + 1] >> sh) & 0xFF;
                    int lf = (src[i - w] >> sh) & 0xFF, rt = (src[i + w] >> sh) & 0xFF;
                    int blur = (up + dn + lf + rt + 4 * p) / 8;
                    int v = (int) Math.round(p + amount * (p - blur));
                    v = Math.max(0, Math.min(255, v));
                    out[i] = (out[i] & ~(0xFF << sh)) | (v << sh);
                }
            }
        BufferedImage d = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        d.setRGB(0, 0, w, h, out, 0, w);
        return d;
    }

    // dieu chinh mau sau khi giam: gamma lift (mo chi toi) + contrast + saturation
    static BufferedImage grade(BufferedImage s, double gamma, double contrast, double sat) {
        int w = s.getWidth(), h = s.getHeight();
        int[] src = s.getRGB(0, 0, w, h, null, 0, w);
        int[] out = new int[src.length];
        int[] lut = new int[256];
        for (int v = 0; v < 255; v++) {
            double x = v / 255.0;
            x = Math.pow(x, gamma);          // gamma < 1 -> sang hon o vung toi
            x = (x - 0.5) * contrast + 0.5;  // tang tuong phan
            x = Math.max(0, Math.min(1, x));
            lut[v] = (int) Math.round(x * 255);
        }
        lut[255] = 255;
        for (int i = 0; i < src.length; i++) {
            int p = src[i];
            int r = lut[(p >> 16) & 0xFF], g = lut[(p >> 8) & 0xFF], b = lut[p & 0xFF];
            if (sat != 1.0) {
                int gray = (r * 299 + g * 587 + b * 114) / 1000;
                r = (int) Math.round(gray + (r - gray) * sat);
                g = (int) Math.round(gray + (g - gray) * sat);
                b = (int) Math.round(gray + (b - gray) * sat);
                r = Math.max(0, Math.min(255, r));
                g = Math.max(0, Math.min(255, g));
                b = Math.max(0, Math.min(255, b));
            }
            out[i] = (p & 0xFF000000) | (r << 16) | (g << 8) | b;
        }
        BufferedImage d = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        d.setRGB(0, 0, w, h, out, 0, w);
        return d;
    }

    static String b64(BufferedImage img) throws Exception {
        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
        ImageIO.write(img, "png", bos);
        return Base64.getEncoder().encodeToString(bos.toByteArray());
    }

    static BufferedImage onChecker(BufferedImage img, int cw, int ch) {
        BufferedImage b = new BufferedImage(cw, ch, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < ch; y++)
            for (int x = 0; x < cw; x++)
                b.setRGB(x, y, ((x / 8) + (y / 8)) % 2 == 0 ? 0xFFCCCCCC : 0xFF888888);
        Graphics2D g = b.createGraphics();
        g.drawImage(img, (cw - img.getWidth()) / 2, (ch - img.getHeight()) / 2, null);
        g.dispose();
        return b;
    }

    static BufferedImage zoom(BufferedImage s, int f) {
        BufferedImage d = new BufferedImage(s.getWidth() * f, s.getHeight() * f, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = d.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(s, 0, 0, d.getWidth(), d.getHeight(), null);
        g.dispose();
        return d;
    }

    static String cell(BufferedImage img, int cw, int ch) throws Exception {
        return "data:image/png;base64," + b64(onChecker(img, cw, ch));
    }

    public static void main(String[] a) throws Exception {
        // a: srcDir oldX4Dir outX2 outX3 outX4 preview  name=id ...
        Path src = Paths.get(a[0]);
        Path oldX4 = Paths.get(a[1]);
        Path ox2 = Paths.get(a[2]), ox3 = Paths.get(a[3]), ox4 = Paths.get(a[4]);
        Path preview = Paths.get(a[5]);
        Files.createDirectories(ox2); Files.createDirectories(ox3); Files.createDirectories(ox4);
        StringBuilder sb = new StringBuilder();
        sb.append("<!doctype html><meta charset='utf-8'><body style='background:#1b1f2a;color:#eee;font-family:Segoe UI;padding:12px'>\n");
        sb.append("<style>.row{display:flex;gap:14px;align-items:flex-end;margin-bottom:8px}.lbl{font-size:11px;color:#8ab}</style>\n");
        for (int i = 6; i < a.length; i++) {
            String[] p = a[i].split("=");
            String name = p[0];
            int id = Integer.parseInt(p[1]);
            BufferedImage raw = ImageIO.read(src.resolve(name + ".png").toFile());
            BufferedImage img = raw.getColorModel().hasAlpha() ? raw : keyWhite(raw);
            Rectangle bb = bbox(img, 16);
            if (bb != null) img = img.getSubimage(bb.x, bb.y, bb.width, bb.height);
            // giu ty le, canh dai nhat = 96
            int tw96, th96;
            if (img.getWidth() >= img.getHeight()) { tw96 = 96; th96 = Math.max(1, (int) Math.round(96.0 * img.getHeight() / img.getWidth())); }
            else { th96 = 96; tw96 = Math.max(1, (int) Math.round(96.0 * img.getWidth() / img.getHeight())); }
            // scale -> grade (mo chi toi, tang tuong phan/sac do) -> sharpen
            BufferedImage base = scaleArea(img, tw96, th96);
            BufferedImage graded = grade(base, 0.80, 1.30, 1.25);
            BufferedImage n4 = sharpen(graded, 0.70);
            int tw72 = Math.max(1, (int) Math.round(n4.getWidth() * 0.75)), th72 = Math.max(1, (int) Math.round(n4.getHeight() * 0.75));
            int tw48 = Math.max(1, (int) Math.round(n4.getWidth() * 0.5)), th48 = Math.max(1, (int) Math.round(n4.getHeight() * 0.5));
            BufferedImage n3 = scaleArea(n4, tw72, th72);
            BufferedImage n2 = scaleArea(n4, tw48, th48);
            ImageIO.write(n2, "png", ox2.resolve(id + ".png").toFile());
            ImageIO.write(n3, "png", ox3.resolve(id + ".png").toFile());
            ImageIO.write(n4, "png", ox4.resolve(id + ".png").toFile());
            // set moi toan phan -> chua co icon OLD -> dung NEW lam thay de preview khong crash
            BufferedImage o4 = Files.exists(oldX4.resolve(id + ".png"))
                    ? ImageIO.read(oldX4.resolve(id + ".png").toFile()) : n4;
            System.out.println(id + " <- " + name + " new=" + n4.getWidth() + "x" + n4.getHeight() + " old=" + o4.getWidth() + "x" + o4.getHeight());
            // preview: old 4x | new 4x | new 1:1
            BufferedImage oz = zoom(o4, 4), nz = zoom(n4, 4);
            sb.append("<div class='lbl'>").append(id).append(" (").append(name).append(")</div><div class='row'>")
              .append("<div class='lbl'>OLD 4x<img src='").append(cell(oz, oz.getWidth() + 8, oz.getHeight() + 8)).append("'></div>")
              .append("<div class='lbl'>NEW 4x<img src='").append(cell(nz, nz.getWidth() + 8, nz.getHeight() + 8)).append("'></div>")
              .append("<div class='lbl'>NEW 1:1<img style='image-rendering:pixelated' src='").append(cell(n4, 104, 104)).append("'></div>")
              .append("</div>\n");
        }
        sb.append("<h3>HAC AM (reference 4x)</h3><div class='row'>\n");
        for (int id : new int[]{32328, 32329}) {
            if (!Files.exists(oldX4.resolve(id + ".png"))) continue;
            BufferedImage o = ImageIO.read(oldX4.resolve(id + ".png").toFile());
            BufferedImage z = zoom(o, 4);
            sb.append("<div class='lbl'>").append(id).append("<img src='").append(cell(z, z.getWidth() + 8, z.getHeight() + 8)).append("'></div>\n");
        }
        sb.append("</div></body>\n");
        Files.write(preview, sb.toString().getBytes("UTF-8"));
        System.out.println("preview -> " + preview);
    }
}
