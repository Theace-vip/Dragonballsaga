import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class AddAccount {
    public static void main(String[] args) throws Exception {
        String username = args[0];
        String password = args[1];

        Properties cfg = new Properties();
        try (FileReader fr = new FileReader("data/config/config.properties")) {
            cfg.load(fr);
        }
        String host = cfg.getProperty("database.host", "127.0.0.1");
        String port = cfg.getProperty("database.port", "3306");
        String db = cfg.getProperty("database.name", "hondaodragon");
        String user = cfg.getProperty("database.user", "root");
        String pass = cfg.getProperty("database.pass", "");
        String url = "jdbc:mysql://" + host + ":" + port + "/" + db
                + "?useSSL=false&allowPublicKeyRetrieval=true";

        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            Integer id = null;
            String oldPass = null;
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT id, password FROM account WHERE username = ?")) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        id = rs.getInt(1);
                        oldPass = rs.getString(2);
                    }
                }
            }

            if (id == null) {
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO account (username, password, ip_address, admin, mabaove, anh_web, gioithieu, gmail, last_time_login, last_time_logout) "
                                + "VALUES (?, ?, ?, 0, 0, '', 0, '', NOW(), NOW())")) {
                    ps.setString(1, username);
                    ps.setString(2, password);
                    ps.setString(3, "127.0.0.1");
                    ps.executeUpdate();
                }
                System.out.println("ACTION: INSERTED new account '" + username + "'");
            } else if (!password.equals(oldPass)) {
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE account SET password = ? WHERE id = ?")) {
                    ps.setString(1, password);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                }
                System.out.println("ACTION: UPDATED password of existing account id=" + id);
            } else {
                System.out.println("ACTION: account already exists with same password, id=" + id);
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT id, username, password, active, ban, vnd FROM account WHERE username = ?")) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        System.out.println("VERIFY: id=" + rs.getInt(1)
                                + " username=" + rs.getString(2)
                                + " password=" + rs.getString(3)
                                + " active=" + rs.getInt(4)
                                + " ban=" + rs.getInt(5)
                                + " vnd=" + rs.getInt(6));
                    }
                }
            }
        }
    }
}
