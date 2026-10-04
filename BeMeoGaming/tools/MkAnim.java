import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Ghep nhieu frame vao 1 file HTML, tu dong phat lap (JS doi anh).
 * Usage: java MkAnim <out.html> <scale> <frameStep> <frame1> <frame2> ...
 * Vi du: java MkAnim data/anh_the/dragon.html 0.4 3 32458 32461 ...
 */
public final class MkAnim {
    public static void main(String[] a) throws Exception {
        File out = new File(a[0]);
        double scale = Double.parseDouble(a[1]);
        StringBuilder sb = new StringBuilder();
        sb.append("<!doctype html><html><head><meta charset='utf-8'>")
          .append("<style>body{margin:0;background:#111;color:#ddd;font:12px monospace}")
          .append("img{display:block;max-width:100%}</style></head><body>")
          .append("<div id='cap'></div>");
        for (int i = 3; i < a.length; i++) {
            BufferedImage im = ImageIO.read(new File(a[i]));
            int w = (int) (im.getWidth() * scale), h = (int) (im.getHeight() * scale);
            BufferedImage s = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = s.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(im, 0, 0, w, h, null);
            g.dispose();
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            ImageIO.write(s, "png", bos);
            String b64 = Base64.getEncoder().encodeToString(bos.toByteArray());
            sb.append("<img class='f' src='data:image/png;base64,").append(b64).append("'>");
            System.out.printf("%s -> %dx%d (%d KB b64)%n", a[i], w, h, b64.length() / 1024);
        }
        sb.append("<script>var f=document.querySelectorAll('.f'),i=0;")
          .append("f[1]&&f.forEach(function(e,k){if(k)e.style.display='none';});")
          .append("setInterval(function(){f[i].style.display='none';i=(i+1)%f.length;")
          .append("f[i].style.display='block';document.getElementById('cap').textContent='frame '+i;},110);")
          .append("</script></body></html>");
        Files.write(out.toPath(), sb.toString().getBytes(StandardCharsets.UTF_8));
        System.out.printf("-> %s (%d KB)%n", out, out.length() / 1024);
    }
}
