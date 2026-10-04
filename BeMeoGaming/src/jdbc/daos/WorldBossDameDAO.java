package jdbc.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import jdbc.DBConnecter;

public class WorldBossDameDAO {

    // Dong du lieu tra ve cho bang xep hang
    public static class Row {
        public long playerId;
        public String name;
        public double dame;

        public Row(long playerId, String name, double dame) {
            this.playerId = playerId;
            this.name = name;
            this.dame = dame;
        }
    }

    // Chuyen cot dame tu BIGINT (chan 9.22e18) sang DOUBLE (toi ~1.8e308) - chi chay 1 lan moi process
    private static volatile boolean migrated = false;

    // Tao bang neu chua co
    public static void ensureTable() {
        try {
            Connection con = DBConnecter.getConnectionServer();
            try {
                PreparedStatement ps = con.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS worldboss_dame ("
                        + "season_id VARCHAR(32) NOT NULL, "
                        + "player_id BIGINT NOT NULL, "
                        + "player_name VARCHAR(64), "
                        + "dame DOUBLE DEFAULT 0, "
                        + "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                        + "PRIMARY KEY (season_id, player_id), "
                        + "KEY idx_season_dame (season_id, dame)"
                        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
                try {
                    ps.execute();
                } finally {
                    ps.close();
                }
                if (!migrated) {
                    try {
                        PreparedStatement al = con.prepareStatement(
                                "ALTER TABLE worldboss_dame MODIFY COLUMN dame DOUBLE DEFAULT 0");
                        try { al.execute(); } finally { al.close(); }
                        migrated = true;
                    } catch (Exception ex) {
                        // da la DOUBLE hoac khong du quyen - khong anh huong game
                        migrated = true;
                    }
                }
            } finally {
                con.close();
            }
        } catch (Exception e) {
            // Bo qua loi de khong anh huong game loop
        }
    }

    // Cong don dame cho player trong mua giai (DOUBLE - ho tro toi 1E305)
    public static void upsert(String season, long playerId, String name, double dameAdd) {
        if (Double.isNaN(dameAdd) || Double.isInfinite(dameAdd) || dameAdd <= 0) return;
        try {
            Connection con = DBConnecter.getConnectionServer();
            try {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO worldboss_dame (season_id, player_id, player_name, dame, updated_at) "
                        + "VALUES (?, ?, ?, ?, NOW()) "
                        + "ON DUPLICATE KEY UPDATE player_name = VALUES(player_name), "
                        + "dame = dame + VALUES(dame), updated_at = NOW()");
                try {
                    ps.setString(1, season);
                    ps.setLong(2, playerId);
                    ps.setString(3, name);
                    ps.setDouble(4, dameAdd);
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }
            } finally {
                con.close();
            }
        } catch (Exception e) {
            // Bo qua loi
        }
    }

    // Lay top dame cao nhat cua mua giai
    public static List<Row> top(String season, int limit) {
        List<Row> list = new ArrayList<Row>();
        try {
            Connection con = DBConnecter.getConnectionServer();
            try {
                PreparedStatement ps = con.prepareStatement(
                        "SELECT player_id, player_name, dame FROM worldboss_dame "
                        + "WHERE season_id = ? ORDER BY dame DESC LIMIT ?");
                try {
                    ps.setString(1, season);
                    ps.setInt(2, limit);
                    ResultSet rs = ps.executeQuery();
                    try {
                        while (rs.next()) {
                            long pid = rs.getLong("player_id");
                            String pname = rs.getString("player_name");
                            double dame = rs.getDouble("dame");
                            list.add(new Row(pid, pname, dame));
                        }
                    } finally {
                        rs.close();
                    }
                } finally {
                    ps.close();
                }
            } finally {
                con.close();
            }
        } catch (Exception e) {
            // Bo qua loi, tra ve list rong hoac mot phan
        }
        return list;
    }

    // Dem so nguoi co dame trong mua giai
    public static int count(String season) {
        try {
            Connection con = DBConnecter.getConnectionServer();
            try {
                PreparedStatement ps = con.prepareStatement(
                        "SELECT COUNT(*) FROM worldboss_dame WHERE season_id = ?");
                try {
                    ps.setString(1, season);
                    ResultSet rs = ps.executeQuery();
                    try {
                        if (rs.next()) {
                            return rs.getInt(1);
                        }
                    } finally {
                        rs.close();
                    }
                } finally {
                    ps.close();
                }
            } finally {
                con.close();
            }
        } catch (Exception e) {
            // Bo qua loi
        }
        return 0;
    }
}
