import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

/**
 * GenIconPreview - tao icon_preview.html tu chua (base64) de xem anh goc + 3 zoom
 * tren nen caro ma khong can server phuc file tinh.
 *
 * Chay trong thu muc BeMeoGaming: java GenIconPreview
 */
public class GenIconPreview {

    public static void main(String[] args) throws Exception {
        String nang = dataUri(new File("icon_moi/nang_cap.png"), 300);
        String nangCut = dataUri(new File("icon_moi/nang_cap_cut.png"), 300);
        String dot = dataUri(new File("icon_moi/dot_pha.png"), 300);
        String[][] icons = {
            {"32745 — Mảnh Vỡ Tinh Thạch", "32745"},
            {"32746 — Hạt Giống Khởi Nguyên", "32746"}
        };

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html lang=\"vi\"><head><meta charset=\"utf-8\">")
          .append("<title>Icon 2 vật phẩm Bản Nguyên</title><style>")
          .append("body{background:#2b2f3a;color:#e8e8e8;font:13px/1.5 Consolas,monospace;margin:16px}")
          .append(".card{background:#1e222b;border:1px solid #3a4152;border-radius:8px;padding:10px 12px;margin-bottom:14px}")
          .append("h2{font-size:14px;margin:0 0 8px;color:#ffd479}")
          .append(".checker{background-image:linear-gradient(45deg,#555 25%,transparent 25%,transparent 75%,#555 75%),")
          .append("linear-gradient(45deg,#555 25%,transparent 25%,transparent 75%,#555 75%);")
          .append("background-size:16px 16px;background-position:0 0,8px 8px;background-color:#888;")
          .append("display:inline-block;border-radius:4px;overflow:hidden}")
          .append("img{display:block;image-rendering:pixelated}")
          .append(".zooms{display:flex;gap:16px;align-items:flex-end}")
          .append(".zooms figure{margin:0;text-align:center}")
          .append(".z2 img{width:128px}.z3 img{width:192px}.z4 img{width:256px}")
          .append("figcaption{color:#9fb0c9;margin-top:4px}")
          .append(".note{color:#ff9c6e;margin-top:8px}")
          .append("</style></head><body>")
          .append("<h1 style=\"font-size:16px\">Kiểm tra 2 icon vật phẩm Bản Nguyên — nền caro = trong suốt</h1>")
          .append("<div class=\"card\"><h2>1) Ảnh gốc \"nâng cấp\" (Mảnh Vỡ Tinh Thạch)</h2><div class=\"checker\">")
          .append("<img src=\"").append(nang).append("\"></div>")
          .append("<div class=\"note\">Nền trắng đục (0% trong suốt) — dùng nguyên bản sẽ thành ô vuông trắng trong hành trang.</div></div>")
          .append("<div class=\"card\"><h2>1b) Cùng ảnh, đã tách nền trắng (CutBackground)</h2><div class=\"checker\">")
          .append("<img src=\"").append(nangCut).append("\"></div>")
          .append("<div class=\"note\">Bản này mới là bản dùng để tạo 3 zoom bên dưới.</div></div>")
          .append("<div class=\"card\"><h2>2) Ảnh gốc \"đột phá\" (Hạt Giống Khởi Nguyên)</h2><div class=\"checker\">")
          .append("<img src=\"").append(dot).append("\"></div>")
          .append("<div class=\"note\">Nền trong suốt OK (58.3% pixel trong suốt).</div></div>")
          .append("<h1 style=\"font-size:16px\">Icon đã tạo — phóng to 4x trên nền caro</h1>");

        for (String[] ic : icons) {
            sb.append("<div class=\"card\"><h2>").append(ic[0]).append("</h2><div class=\"zooms\">");
            int[][] zooms = {{2, 0}, {3, 0}, {4, 0}};
            for (int[] z : zooms) {
                String uri = dataUri(new File("data/icon_botnet/x" + z[0] + "/" + ic[1] + ".png"), 0);
                sb.append("<figure class=\"z").append(z[0]).append("\"><span class=\"checker\">")
                  .append("<img src=\"").append(uri).append("\"></span><figcaption>x").append(z[0])
                  .append("</figcaption></figure>");
            }
            sb.append("</div></div>");
        }
        sb.append("</body></html>");

        Files.write(new File("icon_preview.html").toPath(), sb.toString().getBytes(StandardCharsets.UTF_8));
        System.out.println("Da ghi icon_preview.html (" + sb.length() + " bytes)");
    }

    /** PNG -> data URI; max > 0 thi thu nho cho nhe. */
    static String dataUri(File f, int max) throws Exception {
        BufferedImage im = ImageIO.read(f);
        if (im == null) {
            throw new IllegalArgumentException("Khong doc duoc " + f);
        }
        if (max > 0 && Math.max(im.getWidth(), im.getHeight()) > max) {
            double s = max / (double) Math.max(im.getWidth(), im.getHeight());
            int w = Math.max(1, (int) Math.round(im.getWidth() * s));
            int h = Math.max(1, (int) Math.round(im.getHeight() * s));
            BufferedImage o = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = o.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(im, 0, 0, w, h, null);
            g.dispose();
            im = o;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(im, "png", bos);
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(bos.toByteArray());
    }
}
