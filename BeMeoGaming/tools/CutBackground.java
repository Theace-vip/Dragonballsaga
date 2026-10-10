import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.concurrent.ArrayBlockingQueue;
import java.io.File;

/**
 * CutBackground - tach nen sang/xam (anh AI xuat ra nen trang, khong co alpha) thanh trong suot.
 *
 * Nguyen tac: chi xoa vung nen DINH LIEN voi vien anh (loang tu 4 canh) va la pixel
 * "sang + trung tinh" (do lech kenh mau nho, do sang cao). Vung sang nam trong vat the
 * (loi trang o giua) khong bi xoa vi khong dinh vien.
 *
 * Dung: java CutBackground <vao.png> <ra.png> [doSangToiThieu=185] [lechMauToiDa=30]
 */
public class CutBackground {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Dung: java CutBackground <vao.png> <ra.png> [doSangToiThieu] [lechMauToiDa]");
            return;
        }
        BufferedImage in = ImageIO.read(new File(args[0]));
        int minLight = args.length >= 3 ? Integer.parseInt(args[2]) : 185;
        int maxSpread = args.length >= 4 ? Integer.parseInt(args[3]) : 30;
        int W = in.getWidth(), H = in.getHeight();
        BufferedImage out = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        boolean[] bg = new boolean[W * H];

        ArrayBlockingQueue<Integer> q = new ArrayBlockingQueue<>(W * H);
        for (int x = 0; x < W; x++) {
            push(in, bg, q, x, 0, minLight, maxSpread);
            push(in, bg, q, x, H - 1, minLight, maxSpread);
        }
        for (int y = 0; y < H; y++) {
            push(in, bg, q, 0, y, minLight, maxSpread);
            push(in, bg, q, W - 1, y, minLight, maxSpread);
        }
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        while (!q.isEmpty()) {
            int p = q.poll();
            int x = p % W, y = p / W;
            for (int k = 0; k < 4; k++) {
                int nx = x + dx[k], ny = y + dy[k];
                if (nx < 0 || ny < 0 || nx >= W || ny >= H) {
                    continue;
                }
                push(in, bg, q, nx, ny, minLight, maxSpread);
            }
        }

        long xoa = 0, conLaiSang = 0;
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                int rgb = in.getRGB(x, y);
                boolean isBg = bg[y * W + x];
                if (isBg) {
                    xoa++;
                    out.setRGB(x, y, 0);
                } else {
                    out.setRGB(x, y, rgb | 0xFF000000);
                    if (sangTrungTinh(rgb, minLight, maxSpread)) {
                        conLaiSang++;
                    }
                }
            }
        }
        ImageIO.write(out, "png", new File(args[1]));
        System.out.printf("%s -> %s | da xoa %.1f%% pixel nen | con %d pixel sang-trung tinh ben trong (loi vat the)%n",
                args[0], args[1], 100.0 * xoa / ((long) W * H), conLaiSang);
    }

    static void push(BufferedImage im, boolean[] bg, ArrayBlockingQueue<Integer> q, int x, int y, int minLight, int maxSpread) {
        int p = y * im.getWidth() + x;
        if (bg[p]) {
            return;
        }
        if (!sangTrungTinh(im.getRGB(x, y), minLight, maxSpread)) {
            return;
        }
        bg[p] = true;
        q.add(p);
    }

    static boolean sangTrungTinh(int rgb, int minLight, int maxSpread) {
        if ((rgb >>> 24) <= 8) {
            return true; // da trong suot roi thi coi nhu nen
        }
        int r = (rgb >> 16) & 255, g = (rgb >> 8) & 255, b = rgb & 255;
        int mx = Math.max(r, Math.max(g, b)), mn = Math.min(r, Math.min(g, b));
        return mn >= minLight && (mx - mn) <= maxSpread;
    }
}
