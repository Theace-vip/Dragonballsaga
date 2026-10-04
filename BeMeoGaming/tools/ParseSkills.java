import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Doc dump TSV (nclass_id, id, name, mana_use_type, type, max_point, skills)
 * va in bang thong tin skill - parse tung object {..} nhu server van dung.
 * Usage: java ParseSkills in.tsv out.txt
 */
public class ParseSkills {

    static String clsName(int id) {
        return id == 0 ? "Trai Dat" : id == 1 ? "Namec" : "Xayda";
    }

    public static void main(String[] args) throws Exception {
        String in = args.length > 0 ? args[0] : "/tmp/skills_raw.tsv";
        String out = args.length > 1 ? args[1] : "/tmp/skills_report.txt";

        FileInputStream fis = new FileInputStream(in);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int nn;
        while ((nn = fis.read(buf)) > 0) {
            bos.write(buf, 0, nn);
        }
        fis.close();
        String all = new String(bos.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);
        all = all.replace("\r", ""); // bo CR le trong data

        PrintWriter pw = new PrintWriter(out, "UTF-8");
        int totalSkill = 0;
        for (String line : all.split("\n")) {
            if (line.isEmpty()) {
                continue;
            }
            String[] p = line.split("\t", -1);
            if (p.length < 7) {
                pw.printf("  SKIP line(%d fields): %s%n", p.length,
                        line.length() > 120 ? line.substring(0, 120) + "..." : line);
                continue;
            }
            int nclass, tempId, manaType, type, maxPoint;
            try {
                nclass = Integer.parseInt(p[0].trim());
                tempId = Integer.parseInt(p[1].trim());
                manaType = Integer.parseInt(p[3].trim());
                type = Integer.parseInt(p[4].trim());
                maxPoint = Integer.parseInt(p[5].trim());
            } catch (NumberFormatException e) {
                pw.printf("  SKIP bad header: %s%n",
                        line.length() > 120 ? line.substring(0, 120) + "..." : line);
                continue;
            }
            String name = p[2];
            String skills = p[6];
            // trich tung object {..} (khong chua { } lan nhau) ra parse rieng
            List<JSONObject> objs = new ArrayList<>();
            java.util.regex.Matcher m = java.util.regex.Pattern
                    .compile("\\{[^{}]*\\}").matcher(skills);
            while (m.find()) {
                String chunk = m.group().replace("\\\"", "\""); // \" -> "
                try {
                    Object o = JSONValue.parse(chunk);
                    if (o instanceof JSONObject) {
                        objs.add((JSONObject) o);
                    }
                } catch (Exception ignore) {
                }
            }
            pw.printf("### [%s] tempId=%d type=%d manaType=%d maxPoint=%d name=%s%n",
                    clsName(nclass), tempId, type, manaType, maxPoint, name);
            if (objs.isEmpty()) {
                pw.println("  PARSE FAIL");
                continue;
            }
            for (JSONObject d : objs) {
                totalSkill++;
                pw.printf("  pt=%s | skillId=%s | dmg=%s%% | mp=%s | cd=%sms | powReq=%s | dx=%s dy=%s | maxFight=%s | price=%s | info=%s%n",
                        d.get("point"), d.get("id"), d.get("damage"), d.get("mana_use"),
                        d.get("cool_down"), d.get("power_require"), d.get("dx"), d.get("dy"),
                        d.get("max_fight"), d.get("price"), d.get("info"));
            }
        }
        pw.printf("%nTOTAL: %d muc skill%n", totalSkill);
        pw.close();
        System.out.println("OK -> " + out);
    }
}
