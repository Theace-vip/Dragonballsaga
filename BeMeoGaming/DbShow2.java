import java.sql.*;

public class DbShow2 {
    public static void main(String[] a) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hondaodragon?useSSL=false&characterEncoding=utf8", "root", "")) {
            for (String t : new String[]{"napthe", "history_bank"}) {
                sb.append("-- ").append(t).append('\n');
                try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SHOW COLUMNS FROM `" + t + "`")) {
                    while (rs.next()) sb.append(rs.getString(1)).append(" | ").append(rs.getString(2)).append(" | def=").append(rs.getString(5)).append('\n');
                } catch (Exception e) { sb.append("ERR ").append(e.getMessage()).append('\n'); }
            }
        }
        java.nio.file.Files.write(java.nio.file.Paths.get("dbshow2.out"), sb.toString().getBytes("UTF-8"));
        System.out.println("done");
    }
}
