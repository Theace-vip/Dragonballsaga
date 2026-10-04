package power;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jdbc.DBConnecter;
import lombok.Getter;

/**
 *

 */

public class PowerLimitManager {

    private static final PowerLimitManager instance = new PowerLimitManager();

    public static PowerLimitManager getInstance() {
        return instance;
    }

    @Getter
    private List<PowerLimit> powers;

    public PowerLimitManager() {
        powers = new ArrayList<>();
    }

    public void load() {
        try {
            try (Connection con = DBConnecter.getConnectionServer();) {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM `power_limit`");
                ResultSet rs = ps.executeQuery();
                try {
                    while (rs.next()) {
                        int id = rs.getShort("id");
                        double power = rs.getDouble("power");
                        double hp = rs.getDouble("hp");
                        double mp = rs.getDouble("mp");
                        double damage = rs.getDouble("damage");
                        double defense = rs.getDouble("defense");
                        int critical = rs.getInt("critical");
                        PowerLimit powerLimit = PowerLimit.builder()
                                .id(id)
                                .power(power)
                                .hp(hp)
                                .mp(mp)
                                .damage(damage)
                                .defense(defense)
                                .critical(critical)
                                .build();
                        add(powerLimit);
                    }
                } finally {
                    rs.close();
                    ps.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void add(PowerLimit powerLimit) {
        powers.add(powerLimit);
    }

    public void remove(PowerLimit powerLimit) {
        powers.remove(powerLimit);
    }

    public PowerLimit get(int index) {
        if (index < 0 || index >= powers.size()) {
            return null;
        }
        return powers.get(index);
    }
}
