package jdbc.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jdbc.DBConnecter;

public class WorldBossRewardDAO {

    // Tao bang log tra thuong neu chua co
    public static void ensureTable() {
        try {
            Connection con = DBConnecter.getConnectionServer();
            try {
                PreparedStatement ps = con.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS worldboss_reward_log ("
                        + "season_id VARCHAR(32) NOT NULL PRIMARY KEY, "
                        + "rewarded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                        + "top_json TEXT"
                        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
                try {
                    ps.execute();
                } finally {
                    ps.close();
                }
            } finally {
                con.close();
            }
        } catch (Exception e) {
            // Bo qua loi de khong anh huong game loop
        }
    }

    // Kiem tra mua giai da tra thuong chua
    public static boolean alreadyRewarded(String season) {
        try {
            Connection con = DBConnecter.getConnectionServer();
            try {
                PreparedStatement ps = con.prepareStatement(
                        "SELECT season_id FROM worldboss_reward_log WHERE season_id = ?");
                try {
                    ps.setString(1, season);
                    ResultSet rs = ps.executeQuery();
                    try {
                        if (rs.next()) {
                            return true;
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
        return false;
    }

    // Danh dau mua giai da tra thuong
    public static void markRewarded(String season, String topJson) {
        try {
            Connection con = DBConnecter.getConnectionServer();
            try {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO worldboss_reward_log (season_id, rewarded_at, top_json) "
                        + "VALUES (?, NOW(), ?) "
                        + "ON DUPLICATE KEY UPDATE rewarded_at = NOW(), top_json = VALUES(top_json)");
                try {
                    ps.setString(1, season);
                    ps.setString(2, topJson);
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
}
