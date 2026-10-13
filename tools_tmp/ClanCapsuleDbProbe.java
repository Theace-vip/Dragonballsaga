import clan.Clan;
import clan.ClanMember;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Probe DB: ghi 1 bang hoi TAM (id 999998) bang chinh Clan.insert() de kiem chung
 * bo dem diem capsule ngay (capsule_today / capsule_day) duoc luu dung format
 * ma Manager doc lai khi server khoi dong. Sau khi kiem tra thi XOA dong tam.
 */
public class ClanCapsuleDbProbe {

    private static final int TEST_ID = 999998;

    public static void main(String[] args) {
        try (Connection con = jdbc.DBConnecter.getConnectionServer()) {
            // don dong cu neu con sot
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM clan WHERE id = ?")) {
                ps.setInt(1, TEST_ID);
                ps.executeUpdate();
            }

            Clan clan = new Clan();
            clan.id = TEST_ID;
            clan.name = "probe_daily";
            clan.name2 = "PRB"; // NAME_2 = varchar(4)
            clan.slogan = "probe";
            clan.imgId = 1;
            clan.level = 1;
            clan.maxMember = 10;
            clan.capsuleClan = 7777;

            ClanMember cm = new ClanMember();
            cm.clan = clan;
            cm.id = 999998;
            cm.name = "probe_member";
            cm.head = 1;
            cm.body = 1;
            cm.leg = 1;
            cm.role = Clan.LEADER;
            cm.memberPoint = 555;
            cm.clanPoint = 666;
            cm.capsuleToday = 1234;
            cm.capsuleDay = 20261011;
            clan.addClanMember(cm);

            clan.insert();
            System.out.println("== da ghi bang tam id=" + TEST_ID + " ==");

            String json = null;
            try (PreparedStatement ps = con.prepareStatement("SELECT members FROM clan WHERE id = ?")) {
                ps.setInt(1, TEST_ID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        json = rs.getString("members");
                    }
                }
            }
            System.out.println("  members trong DB = " + json);

            if (json != null) {
                JSONArray arr = (JSONArray) JSONValue.parse(json);
                JSONObject o = (JSONObject) JSONValue.parse(String.valueOf(arr.get(0)));
                // dung DUNG bieu thuc Manager doc lai
                int today = Integer.parseInt(String.valueOf(o.get("capsule_today")));
                int day = Integer.parseInt(String.valueOf(o.get("capsule_day")));
                System.out.println("  Manager doc lai -> capsule_today=" + today + " capsule_day=" + day);
                System.out.println("  KET QUA: " + ((today == 1234 && day == 20261011) ? "DUNG (round-trip OK)" : "SAI"));
            } else {
                System.out.println("  KET QUA: KHONG DOC DUOC DONG TAM");
            }

            int deleted;
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM clan WHERE id = ?")) {
                ps.setInt(1, TEST_ID);
                deleted = ps.executeUpdate();
            }
            System.out.println("== da xoa bang tam (" + deleted + " dong) ==");
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM clan WHERE id = ?")) {
                ps.setInt(1, TEST_ID);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    System.out.println("  con lai trong DB = " + rs.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
