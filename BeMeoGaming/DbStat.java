import java.sql.*;
public class DbStat {
    public static void main(String[] a) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hondaodragon?useSSL=false&characterEncoding=utf8", "root", "")) {
            try (Statement st = con.createStatement()) {
                ResultSet rs = st.executeQuery("SELECT MIN(id), MAX(id), COUNT(*) FROM item_template");
                if (rs.next()) sb.append("item_template min=").append(rs.getInt(1)).append(" max=").append(rs.getInt(2)).append(" count=").append(rs.getInt(3)).append('\n');
                rs = st.executeQuery("SHOW COLUMNS FROM item_template");
                while (rs.next()) sb.append(rs.getString(1)).append(" | ").append(rs.getString(2)).append('\n');
                rs = st.executeQuery("SELECT id,TYPE,gender,NAME,description,level,icon_id,part FROM item_template WHERE id IN (1413,1735,1914,1412)");
                while (rs.next()) sb.append("chest? ").append(rs.getInt(1)).append(" type=").append(rs.getByte(2)).append(" gender=").append(rs.getByte(3)).append(" name=").append(rs.getString(4)).append(" icon=").append(rs.getShort(7)).append(" part=").append(rs.getShort(8)).append(" desc=").append(rs.getString(5)).append('\n');
            }
        }
        java.nio.file.Files.write(java.nio.file.Paths.get("dbstat.out"), sb.toString().getBytes("UTF-8"));
        System.out.println("done");
    }
}
