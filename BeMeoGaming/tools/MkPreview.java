import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Dung trang xem anh (base64) vi server preview chi phuc vu duong dan .html.
 * Usage: java MkPreview out.html img1.png [img2.png ...]
 */
public final class MkPreview {
    public static void main(String[] a) throws Exception {
        if (a.length < 2) throw new IllegalArgumentException("usage: MkPreview out.html img1.png ...");
        StringBuilder sb = new StringBuilder();
        sb.append("<!doctype html><meta charset=\"utf-8\">")
          .append("<style>body{margin:0;background:#111;color:#ddd;font:12px monospace}")
          .append(".c{margin:6px}.c img{display:block;max-width:100%}</style>");
        for (int i = 1; i < a.length; i++) {
            String spec = a[i];
            String path = spec;
            int at = spec.indexOf('@');
            java.awt.image.BufferedImage cropImg = null;
            if (at > 0) {
                path = spec.substring(0, at);
                String[] p = spec.substring(at + 1).split(",");
                int cx = Integer.parseInt(p[0]), cy = Integer.parseInt(p[1]);
                int cw = Integer.parseInt(p[2]), ch = Integer.parseInt(p[3]);
                java.awt.image.BufferedImage im = javax.imageio.ImageIO.read(new java.io.File(path));
                cropImg = im.getSubimage(cx, cy, cw, ch);
            }
            byte[] b;
            if (cropImg != null) {
                java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
                javax.imageio.ImageIO.write(cropImg, "png", bos);
                b = bos.toByteArray();
            } else {
                b = Files.readAllBytes(Paths.get(path));
            }
            sb.append("<div class=\"c\">").append(spec).append(" (").append(b.length).append(" bytes)")
              .append("<img src=\"data:image/png;base64,")
              .append(Base64.getEncoder().encodeToString(b))
              .append("\"></div>");
        }
        Files.write(Paths.get(a[0]), sb.toString().getBytes("UTF-8"));
        System.out.println("-> " + a[0]);
    }
}
