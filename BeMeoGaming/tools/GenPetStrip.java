import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import java.util.*;
import java.util.List;

/**
 * GenPetStrip - ghep frame PNG (data/spine_frames/<set>/<anim>) thanh strip doc
 * dat vao data/icon_botnet/x{2,3,4}/{petId}.png (SmallImage - pet follow, msg 31).
 *
 * Quy uoc (doi chieu linh thu 12670 dang co):
 *   - Cell VUONG 75 logical; client drawRegion x zoomLevel -> anh x2 = 150, x3 = 225, x4 = 300.
 *   - 8 frame doc (msg 31 gui frame {0..7}, w = h = 75).
 *   - Server gui smallId = iconID - 1 -> file pet = {iconID-1}.png, icon tui = {iconID}.png.
 *
 * Cach dung: java -cp build/tools GenPetStrip <root> <srcDir> <petId> <iconId>
 *   vd:     java -cp build/tools GenPetStrip . data/spine_frames/H21601/walk 33000 33001
 * Output: data/icon_botnet/x{2,3,4}/{petId}.png + {iconId}.png + _gen_pet.html preview
 */
public class GenPetStrip {

    static final int CELL = 150;   // = 75 logical * zoomLevel 2 (x3 = 225, x4 = 300)

    public static void main(String[] args) throws Exception {
        Path root = Paths.get(args.length > 0 ? args[0] : ".");
        Path src = Paths.get(args[1]);
        int petId = Integer.parseInt(args[2]);
        int iconId = Integer.parseInt(args[3]);

        List<Path> pngs = new ArrayList<>();
        Files.newDirectoryStream(src, p -> p.toString().endsWith(".png")).forEach(pngs::add);
        pngs.sort(Comparator.comparing(p -> p.getFileName().toString()));
        int n = pngs.size();
        if (n == 0) { System.err.println("Khong co frame: " + src); System.exit(1); }

        BufferedImage[] frames = new BufferedImage[n];
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = -1, maxY = -1;
        for (int i = 0; i < n; i++) {
            frames[i] = ImageIO.read(pngs.get(i).toFile());
            BufferedImage f = frames[i];
            int[] px = f.getRGB(0, 0, f.getWidth(), f.getHeight(), null, 0, f.getWidth());
            for (int y = 0; y < f.getHeight(); y++)
                for (int x = 0; x < f.getWidth(); x++)
                    if (((px[y * f.getWidth() + x] >>> 24) & 0xFF) > 16) {
                        if (x < minX) minX = x;
                        if (x > maxX) maxX = x;
                        if (y < minY) minY = y;
                        if (y > maxY) maxY = y;
                    }
        }
        if (maxX < 0) { System.err.println("Frame rong"); System.exit(0); }
        int cropX = Math.max(0, minX - 1), cropY = Math.max(0, minY - 1);
        int cropW = Math.min(frames[0].getWidth(), maxX + 2) - cropX;
        int cropH = Math.min(frames[0].getHeight(), maxY + 2) - cropY;

        // fit vao cell co le 2px lenh
        double scale = Math.min((CELL - 4) / (double) cropW, (CELL - 4) / (double) cropH);
        int fw = Math.max(1, (int) Math.round(cropW * scale));
        int fh = Math.max(1, (int) Math.round(cropH * scale));
        int ox = (CELL - fw) / 2, oy = (CELL - fh) / 2;

        // strip x2 (goc), x3 = x1.5, x4 = x2
        BufferedImage strip = new BufferedImage(CELL, CELL * n, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = strip.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        for (int i = 0; i < n; i++) {
            g.drawImage(frames[i].getSubimage(cropX, cropY, cropW, cropH),
                    ox, i * CELL + oy, fw, fh, null);
        }
        g.dispose();

        // icon tui: frame nhieu pixel nhat fit 48x48 (uong x2 nhu 12671 dang co)
        BufferedImage peak = frames[0]; long best = -1;
        for (BufferedImage f : frames) {
            long p = 0;
            int[] px = f.getRGB(cropX, cropY, cropW, cropH, null, 0, cropW);
            for (int v : px) if (((v >>> 24) & 0xFF) > 16) p++;
            if (p > best) { best = p; peak = f; }
        }
        double is = Math.min(48 / (double) cropW, 48 / (double) cropH);
        int iw = Math.max(1, (int) Math.round(cropW * is)), ih = Math.max(1, (int) Math.round(cropH * is));
        BufferedImage icon = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
        Graphics2D ig = icon.createGraphics();
        ig.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        ig.drawImage(peak.getSubimage(cropX, cropY, cropW, cropH), (48 - iw) / 2, (48 - ih) / 2, iw, ih, null);
        ig.dispose();

        int[][] zooms = {{2, 100}, {3, 150}, {4, 200}}; // percent
        for (int[] z : zooms) {
            Path dir = root.resolve("data/icon_botnet/x" + z[0]);
            Files.createDirectories(dir);
            ImageIO.write(scalePct(strip, z[1]), "png", dir.resolve(petId + ".png").toFile());
            ImageIO.write(scalePct(icon, z[1]), "png", dir.resolve(iconId + ".png").toFile());
        }

        // preview
        StringBuilder html = new StringBuilder();
        html.append("<!doctype html><html><head><meta charset='utf-8'><style>")
            .append("body{background:#1e2530;color:#eee;font-family:sans-serif;margin:14px}")
            .append(".row{display:flex;gap:16px;align-items:flex-start}")
            .append(".cell{background:#111;padding:8px;border-radius:8px;text-align:center}")
            .append("small{color:#8ab}</style></head><body><div class='row'>")
            .append("<div class='cell'><h3>pet ").append(petId).append(" strip (8 frame)</h3>")
            .append("<img src='data:image/png;base64,").append(b64(strip))
            .append("' style='height:600px;background:repeating-conic-gradient(#333 0% 25%,#3d3d3d 0% 50%) 50%/16px 16px'>")
            .append("<br><small>").append(CELL).append("x").append(CELL * n).append(" cell ").append(fw).append("x").append(fh).append("</small></div>")
            .append("<div class='cell'><h3>icon ").append(iconId).append("</h3>")
            .append("<img src='data:image/png;base64,").append(b64(icon))
            .append("' style='width:96px;background:repeating-conic-gradient(#333 0% 25%,#3d3d3d 0% 50%) 50%/16px 16px'>")
            .append("</div></div></body></html>");
        Files.write(root.resolve("data/spine_frames/_gen_pet.html"), html.toString().getBytes("UTF-8"));

        System.out.println("XONG petId=" + petId + " iconId=" + iconId
                + " frames=" + n + " cell=" + CELL + " content=" + fw + "x" + fh
                + " strip=" + CELL + "x" + (CELL * n));
        System.out.println("Preview: data/spine_frames/_gen_pet.html");
    }

    static BufferedImage scalePct(BufferedImage src, int pct) {
        int w = src.getWidth() * pct / 100, h = src.getHeight() * pct / 100;
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(src, 0, 0, w, h, null);
        g.dispose();
        return out;
    }

    static String b64(BufferedImage img) throws Exception {
        java.io.ByteArrayOutputStream b = new java.io.ByteArrayOutputStream();
        ImageIO.write(img, "png", b);
        return Base64.getEncoder().encodeToString(b.toByteArray());
    }
}
