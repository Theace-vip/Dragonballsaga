import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * IconMap - in ban do ASCII chi tiet + thong ke pixel (trong suot / trang-xam / co mau)
 * de kiem tra icon that su trong ra sao.
 *
 * Dung: java IconMap <anh.png> [x0 y0 w h step]
 */
public class IconMap {
    public static void main(String[] args) throws Exception {
        BufferedImage im = ImageIO.read(new File(args[0]));
        int x0 = 0, y0 = 0, w = im.getWidth(), h = im.getHeight(), step = 1;
        if (args.length >= 6) {
            x0 = Integer.parseInt(args[1]);
            y0 = Integer.parseInt(args[2]);
            w = Integer.parseInt(args[3]);
            h = Integer.parseInt(args[4]);
            step = Integer.parseInt(args[5]);
        }
        System.out.printf("%s %dx%d | vung (%d,%d) %dx%d step=%d%n", args[0], im.getWidth(), im.getHeight(), x0, y0, w, h, step);
        long tong = 0, trong = 0, trang = 0, mau = 0;
        for (int y = y0; y < Math.min(y0 + h, im.getHeight()); y += step) {
            StringBuilder sb = new StringBuilder();
            for (int x = x0; x < Math.min(x0 + w, im.getWidth()); x += step) {
                int rgb = im.getRGB(x, y);
                int a = rgb >>> 24, r = (rgb >> 16) & 255, g = (rgb >> 8) & 255, b = rgb & 255;
                int mx = Math.max(r, Math.max(g, b)), mn = Math.min(r, Math.min(g, b));
                tong++;
                if (a <= 8) {
                    sb.append('.');
                    trong++;
                } else if (mx - mn > 20) {
                    sb.append('#');
                    mau++;
                } else if (mn > 200) {
                    sb.append('W');
                    trang++;
                } else if (mn > 140) {
                    sb.append('g');
                    trang++;
                } else {
                    sb.append('D');
                    mau++;
                }
            }
            System.out.println(sb);
        }
        System.out.printf("tong mau lay: %d | trong suot %.1f%% | trang-xam %.1f%% | co mau %.1f%%%n",
                tong, 100.0 * trong / tong, 100.0 * trang / tong, 100.0 * mau / tong);
    }
}
