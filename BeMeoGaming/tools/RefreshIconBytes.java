import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.Paths;

/**
 * Cap nhat lai byte do dai moi icon trong data/smallimage_version/x{1..4}/smallimage_version_data.
 * extendSmallImageVersion (DataGame) chi BO SUNG id moi, khong doi id da co -> khi gen lai anh
 * cung id thi byte cu (len(png)%127) sai, client co the khong tai lai icon.
 *
 * Usage: java RefreshIconBytes [firstId] [lastId]   (mac dinh 32415 32457)
 */
public final class RefreshIconBytes {
    public static void main(String[] a) throws Exception {
        int first = a.length > 0 ? Integer.parseInt(a[0]) : 32415;
        int last = a.length > 1 ? Integer.parseInt(a[1]) : 32457;
        for (int z = 1; z <= 4; z++) {
            File f = new File("data/smallimage_version/x" + z + "/smallimage_version_data");
            if (!f.isFile()) { System.out.println("thieu " + f.getPath()); continue; }
            byte[] d = Files.readAllBytes(f.toPath());
            int header = ((d[0] & 255) << 8) | (d[1] & 255);
            if (last >= header) {
                System.out.printf("x%d: header=%d nho hon id %d - chay extendSmallImageVersion truoc%n",
                        z, header, last);
                continue;
            }
            int changed = 0;
            StringBuilder detail = new StringBuilder();
            for (int id = first; id <= last; id++) {
                byte nu = iconByte(z, id);
                int idx = 2 + id;
                if (d[idx] == nu) continue;
                detail.append(String.format("  %d: %d -> %d%n", id, d[idx] & 255, nu & 255));
                d[idx] = nu;
                changed++;
            }
            if (changed == 0) { System.out.printf("x%d: header=%d, byte da dung (khong doi)%n", z, header); continue; }
            Files.copy(f.toPath(), Paths.get(f.getPath() + ".bak2"), StandardCopyOption.REPLACE_EXISTING);
            Files.write(f.toPath(), d);
            System.out.printf("x%d: header=%d, doi %d byte id %d..%d%n", z, header, changed, first, last);
            System.out.print(detail);
        }
    }

    static byte iconByte(int zoom, int id) {
        File f = new File("data/icon_botnet/x" + zoom + "/" + id + ".png");
        if (!f.isFile()) return (byte) -1;
        return (byte) (f.length() % 127);
    }
}
