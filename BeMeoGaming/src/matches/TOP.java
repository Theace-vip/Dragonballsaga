package matches;

import lombok.Builder;
import lombok.Data;
import player.Player;

@Data
@Builder
public class TOP {

    private String name;
    private byte gender;
    private short head;
    private short body;
    private short leg;
    private double power;
    private long ki;
    private long hp;
    private long sd;
    private byte nv;
    private byte subnv;
    private int sk;
    private int pvp;
    private int nhs;
    private int dicanh;
    private int juventus;
    private long lasttime;
    private long time;
    private int level;
    private String info1;
    private String info2;
    private int id_player;
}
