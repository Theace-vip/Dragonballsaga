import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CharFrameSheet - render 33 pose (cf 0..32) cua NRO bang CharInfo + part mac dinh
 * de do mat match voi spine truoc khi lam bang map cai trang.
 *
 * Doc (chi doc, khong sua):
 *   - CharInfo tu TuanBip/Assets/Scripts/Game1/Char.cs (new int[3] {idx,dx,dy} x 4 hang/cf:
 *     [0]=head, [1]=leg, [2]=body, [3]=phu)
 *   - part mac dinh tu build/part_default.txt (mysql: select id,TYPE,DATA from part where id in (0,1,2))
 *   - anh tu data/icon_botnet/x2/<id>.png
 *
 * Ve (cdir=1, giong paintCharBody): x = cx + CharInfo[cf][i][1] + pi.dx,
 *   y = cy - CharInfo[cf][i][2] + pi.dy, anchor TOP_LEFT, thu tu: head -> leg -> body.
 *
 * Cach dung: java -cp build/tools CharFrameSheet <root_BeMeoGaming> <Char.cs>
 * Output: data/spine_frames/_charinfo.html
 */
public class CharFrameSheet {

    public static void main(String[] args) throws Exception {
        Path root = Paths.get(args[0]);
        String charCs = new String(Files.readAllBytes(Paths.get(args[1])), "UTF-8");

        // --- CharInfo: lay toan bo new int[3] {a,b,c} sau chuoi "CharInfo = new int[33]" ---
        int st = charCs.indexOf("CharInfo = new int[33]");
        if (st < 0) { System.err.println("Khong tim thay CharInfo"); System.exit(1); }
        List<int[]> rows = new ArrayList<>();
        int en = charCs.indexOf("\n        };", st);
        if (en < 0) en = st + 40000;
        Matcher m = Pattern.compile("new\\s+int\\s*\\[3\\]").matcher(charCs.substring(st, en));
        while (m.find()) {
            String rest = charCs.substring(st + m.end()).trim();
            if (rest.startsWith("{")) {
                Matcher v = Pattern.compile("\\{\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*\\}").matcher(rest);
                if (!v.find()) { System.err.println("Row loi: " + rest.substring(0, Math.min(40, rest.length()))); continue; }
                rows.add(new int[]{Integer.parseInt(v.group(1)), Integer.parseInt(v.group(2)), Integer.parseInt(v.group(3))});
            } else {
                rows.add(new int[]{0, 0, 0}); // new int[3] tran = {0,0,0}
            }
        }
        int cfN = rows.size() / 4;
        System.out.println("CharInfo: " + rows.size() + " hang -> " + cfN + " cf");

        // --- part mac dinh: id 0 = head, 1 = body, 2 = leg ---
        int[][][] part = new int[3][][];
        for (String line : Files.readAllLines(root.resolve("build/part_default.txt"))) {
            Matcher mt = Pattern.compile("^(\\d+)\\t(\\d+)\\t(.+)$").matcher(line.trim());
            if (!mt.find()) continue;
            int id = Integer.parseInt(mt.group(1)), type = Integer.parseInt(mt.group(2));
            List<int[]> es = new ArrayList<>();
            Matcher me = Pattern.compile("\\[(-?\\d+),(-?\\d+),(-?\\d+)\\]").matcher(mt.group(3));
            while (me.find()) es.add(new int[]{Integer.parseInt(me.group(1)), Integer.parseInt(me.group(2)), Integer.parseInt(me.group(3))});
            int slot = type == 0 ? 0 : (type == 1 ? 1 : 2); // 0=head,1=body,2=leg
            part[slot] = es.toArray(new int[0][]);
        }
        System.out.println("part mac dinh: head=" + part[0].length + " body=" + part[1].length + " leg=" + part[2].length);

        // --- render ---
        // toan bo toa do logic x2 -> nhan 2 khi draw (anh x2 la goc, khong phai giam)
        int CX = 120, CY = 150, W = 240, H = 200;
        int SC = 2;
        Map<Integer, BufferedImage> imgCache = new HashMap<>();
        StringBuilder html = new StringBuilder();
        html.append("<!doctype html><html><head><meta charset='utf-8'><style>")
            .append("body{background:#1e2530;color:#eee;font-family:sans-serif;margin:14px}")
            .append(".grid{display:grid;grid-template-columns:repeat(6,1fr);gap:4px}")
            .append(".c{background:#111;padding:2px;border-radius:6px;text-align:center}")
            .append(".c img{image-rendering:pixelated;background:")
            .append("repeating-conic-gradient(#333 0% 25%,#3d3d3d 0% 50%) 50%/16px 16px}")
            .append("small{color:#8ab;font-size:11px}</style></head><body>")
            .append("<h3>33 pose NRO base (cf 0..32) - huong phai, chan tai y=").append(CY / SC).append(" (logic)</h3><div class='grid'>");

        for (int cf = 0; cf < cfN; cf++) {
            BufferedImage canvas = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = canvas.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            // thu tu ve: head -> leg -> body (giong paintCharBody)
            // CharInfo[cf]: [0]=head, [1]=leg, [2]=body  <->  part slot: 0=head, 1=body, 2=leg
            int[][] parts = {rows.get(cf * 4), rows.get(cf * 4 + 1), rows.get(cf * 4 + 2)};
            int[] slotOf = {0, 2, 1};
            for (int i = 0; i < 3; i++) {
                int pIdx = parts[i][0];
                int slot = slotOf[i];
                if (pIdx < 0 || pIdx >= part[slot].length) continue;
                int[] pi = part[slot][pIdx];
                BufferedImage im = imgCache.computeIfAbsent(pi[0], id -> {
                    try { return ImageIO.read(root.resolve("data/icon_botnet/x2/" + id + ".png").toFile()); }
                    catch (Exception e) { return null; }
                });
                if (im == null) continue;
                int x = CX + SC * (parts[i][1] + pi[1]);
                int y = CY - SC * parts[i][2] + SC * pi[2];
                g.drawImage(im, x, y, null);
            }
            g.dispose();
            html.append("<div class='c'><img src='data:image/png;base64,").append(b64(canvas))
                .append("' width='240' height='200'><br><small>cf ").append(cf).append("</small></div>");
        }
        html.append("</div>");
        // neu co fragment contact-sheet spine thi ghep vao (chay mksheet.js truoc)
        Path frag = root.resolve("data/spine_frames/_sheet_frag.html");
        if (Files.exists(frag)) {
            html.append(new String(Files.readAllBytes(frag), "UTF-8"));
            System.out.println("Ghep them fragment spine: " + Files.size(frag) + " bytes");
        }
        // preview cai trang da bake (GenCaitrang ghi ra)
        Path ct = root.resolve("build/ct_preview.html");
        if (Files.exists(ct)) {
            html.append(new String(Files.readAllBytes(ct), "UTF-8"));
            System.out.println("Ghep them preview cai trang: " + Files.size(ct) + " bytes");
        }
        html.append("</body></html>");
        Files.write(root.resolve("data/spine_frames/_match.html"), html.toString().getBytes("UTF-8"));
        System.out.println("Da ghi data/spine_frames/_match.html");
    }

    static String b64(BufferedImage img) throws Exception {
        java.io.ByteArrayOutputStream b = new java.io.ByteArrayOutputStream();
        ImageIO.write(img, "png", b);
        return Base64.getEncoder().encodeToString(b.toByteArray());
    }
}
