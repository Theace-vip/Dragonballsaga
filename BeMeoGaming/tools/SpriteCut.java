import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayDeque;

/**
 * Cat sprite TU CROP khoi sheet reference (tools/res/dragon_src2.png - RGB nen sang/kim).
 * - Cat vung truoc, flood fill tu CANH VUNG: loai pixel "sang + thap do bao" (nen kim/bach danh).
 * - Giu thanh phan lien thong lon nhat TRONG VUNG.
 * - Feather 1px, crop bbox, day ra PNG ARGB.
 * Usage: java SpriteCut <x> <y> <w> <h> <out> [lumT] [satT]
 */
public class SpriteCut {

    public static void main(String[] args) throws Exception {
        int x0 = Integer.parseInt(args[0]);
        int y0 = Integer.parseInt(args[1]);
        int cw = Integer.parseInt(args[2]);
        int ch = Integer.parseInt(args[3]);
        String out = args[4];
        int lumT = args.length > 5 ? Integer.parseInt(args[5]) : 165;
        int satT = args.length > 6 ? Integer.parseInt(args[6]) : 48;

        BufferedImage src = ImageIO.read(new File("tools/res/dragon_src2.png"));
        int W = src.getWidth(), H = src.getHeight();
        int[] all = src.getRGB(0, 0, W, H, null, 0, W);

        int mX = Math.max(0, x0), mY = Math.max(0, y0);
        int mW = Math.min(cw, W - mX), mH = Math.min(ch, H - mY);
        int w = mW, h = mH;
        int[] px = new int[w * h];
        for (int y = 0; y < h; y++)
            System.arraycopy(all, (mY + y) * W + mX, px, y * w, w);

        // bg toan vung = sang & thap do bao
        boolean[] bg = new boolean[w * h];
        for (int i = 0; i < w * h; i++) {
            int c = px[i];
            int r = (c >> 16) & 255, g = (c >> 8) & 255, b = c & 255;
            int lum = (r * 299 + g * 587 + b * 114) / 1000;
            int mx = Math.max(r, Math.max(g, b)), mn = Math.min(r, Math.min(g, b));
            if (lum >= lumT && mx - mn <= satT) bg[i] = true;
        }
        // flood tu canh VUNG
        boolean[] vis = new boolean[w * h];
        ArrayDeque<Integer> q = new ArrayDeque<>();
        for (int x = 0; x < w; x++) { enq(q, vis, bg, x, 0, w); enq(q, vis, bg, x, h - 1, w); }
        for (int y = 0; y < h; y++) { enq(q, vis, bg, 0, y, w); enq(q, vis, bg, w - 1, y, w); }
        while (!q.isEmpty()) {
            int i = q.poll(), x = i % w, y = i / w;
            if (x > 0) enq(q, vis, bg, x - 1, y, w);
            if (x < w - 1) enq(q, vis, bg, x + 1, y, w);
            if (y > 0) enq(q, vis, bg, x, y - 1, w);
            if (y < h - 1) enq(q, vis, bg, x, y + 1, w);
        }
        boolean[] fg = new boolean[w * h];
        for (int i = 0; i < w * h; i++) {
            if (vis[i]) continue; // flood tu canh = nen chac chan
            if (bg[i]) {
                // vung sang bi chan ben trong: chi giu neu MAU MAT (bui/thanh rong), bo neu am/nong (troi/may)
                int c = px[i];
                int r = (c >> 16) & 255, b = c & 255;
                if (b > r) fg[i] = true; // mat (tim/xanh) -> than rong
            } else {
                fg[i] = true;
            }
        }

        // thanh phan lien thong lon nhat TRONG vung
        int[] comp = new int[w * h];
        int[] stack = new int[w * h];
        int best = 0, bestSize = 0, cid = 0;
        for (int i = 0; i < w * h; i++) {
            if (!fg[i] || comp[i] != 0) continue;
            cid++;
            int sp = 0; stack[sp++] = i; comp[i] = cid; int size = 0;
            while (sp > 0) {
                int idx = stack[--sp]; size++;
                int x = idx % w, y = idx / w;
                if (x > 0 && fg[idx - 1] && comp[idx - 1] == 0) { comp[idx - 1] = cid; stack[sp++] = idx - 1; }
                if (x < w - 1 && fg[idx + 1] && comp[idx + 1] == 0) { comp[idx + 1] = cid; stack[sp++] = idx + 1; }
                if (y > 0 && fg[idx - w] && comp[idx - w] == 0) { comp[idx - w] = cid; stack[sp++] = idx - w; }
                if (y < h - 1 && fg[idx + w] && comp[idx + w] == 0) { comp[idx + w] = cid; stack[sp++] = idx + w; }
            }
            if (size > bestSize) { bestSize = size; best = cid; }
        }
        int[] alpha = new int[w * h];
        for (int i = 0; i < w * h; i++) alpha[i] = (comp[i] == best) ? 255 : 0;

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = -1, maxY = -1;
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                int i = y * w + x;
                if (alpha[i] == 0) continue;
                if (x < minX) minX = x;
                if (x > maxX) maxX = x;
                if (y < minY) minY = y;
                if (y > maxY) maxY = y;
            }
        if (maxX < 0) { System.out.println(out + " EMPTY"); return; }

        // feather 1px
        int[] na = alpha.clone();
        for (int y = Math.max(1, minY); y <= Math.min(h - 2, maxY); y++)
            for (int x = Math.max(1, minX); x <= Math.min(w - 2, maxX); x++) {
                int i = y * w + x;
                if (alpha[i] == 0) continue;
                int adj = 0, tot = 0;
                for (int d = -1; d <= 1; d++)
                    for (int e = -1; e <= 1; e++) { tot++; if (alpha[i + d * w + e] > 0) adj++; }
                if (adj < tot) na[i] = (int) (255L * adj / tot);
            }

        int ow = maxX - minX + 1, oh = maxY - minY + 1;
        BufferedImage outImg = new BufferedImage(ow, oh, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < oh; y++)
            for (int x = 0; x < ow; x++) {
                int i = (y + minY) * w + (x + minX);
                outImg.setRGB(x, y, (px[i] & 0x00FFFFFF) | (na[i] << 24));
            }
        File outFile = new File(out);
        if (outFile.getParentFile() != null) outFile.getParentFile().mkdirs();
        ImageIO.write(outImg, "png", outFile);
        System.out.println(out + " " + ow + "x" + oh + " comp=" + bestSize
                + "/" + (w * h) + " abs=(" + (mX + minX) + "," + (mY + minY) + ")");
    }

    static void enq(ArrayDeque<Integer> q, boolean[] vis, boolean[] bg, int x, int y, int w) {
        int i = y * w + x;
        if (vis[i] || !bg[i]) return;
        vis[i] = true;
        q.add(i);
    }
}
