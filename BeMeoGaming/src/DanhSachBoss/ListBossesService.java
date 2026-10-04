package DanhSachBoss;

import item.Item;
import item.Item.ItemOption;
import java.io.IOException;
import java.util.List;
import network.Message;
import player.Player;
import services.Service;

/**
 *
 * @author HairMod
 */
public class ListBossesService {

    private static ListBossesService _Instance;

    public static ListBossesService gI() {
        if (_Instance == null) {
            _Instance = new ListBossesService();
        }
        return _Instance;
    }

    private Message messageBosses(byte cmd) throws IOException {
        Message msg = Service.gI().messageSubCommand((byte) 98);
        msg.writer().writeByte(cmd);
        return msg;
    }

    public void ReceiveMessage(Player player, Message msg) throws IOException {
        byte type = msg.reader().readByte();
        switch (type) {
            case 0:
                sendListBosses(player);
                break;
            case 1:
                int selected = msg.reader().readInt();
                sendSelectedBoss(player, selected);
                break;
        }
    }

    public void sendSelectedBoss(Player pl, int selected) throws IOException {
        Message msg = messageBosses((byte) 2);
        BossStruct b = ListBosses.listBosses.get(selected);
        msg.writer().writeInt(selected);
        msg.writer().writeUTF(b.hp);
        msg.writer().writeUTF(b.dame);
        msg.writer().writeUTF(b.timeAppear);
        msg.writer().writeByte(b.mapAppears.length);
        for (int i = 0; i < b.mapAppears.length; i++) {
            msg.writer().writeInt(b.mapAppears[i]);
        }
        msg.writer().writeShort(b.head);
         msg.writer().writeShort(b.body);
          msg.writer().writeShort(b.leg);
        if (b.dropItems.size() > 0) {
            msg.writer().writeByte(b.dropItems.size());
            for (int i = 0; i < b.dropItems.size(); i++) {
                Item item = b.dropItems.get(i);
                msg.writer().writeInt(item.template.id);
                msg.writer().writeInt(item.quantity);
                msg.writer().writeByte(item.itemOptions.size());
                if (item.itemOptions.size() > 0) {
                    for (int j = 0; j < item.itemOptions.size(); j++) {
                        ItemOption o = item.itemOptions.get(j);
                        msg.writer().writeByte(o.optionTemplate.id);
                        msg.writer().writeLong(o.param);
                    }
                }
            }
        }
        pl.sendMessage(msg);
    }

    public void sendListBosses(Player player) throws IOException {
        Message msg = messageBosses((byte) 0);
        List<BossStruct> bs = ListBosses.listBosses; 
        msg.writer().writeInt(bs.size());
        for (int i = 0; i < bs.size(); i++) {
            BossStruct b = bs.get(i);
            msg.writer().writeUTF(b.name);
        }
        BossStruct b = bs.get(0);
        msg.writer().writeUTF(b.hp);
        msg.writer().writeUTF(b.dame);
        msg.writer().writeUTF(b.timeAppear);
        msg.writer().writeByte(b.mapAppears.length);
        for (int i = 0; i < b.mapAppears.length; i++) {
            msg.writer().writeInt(b.mapAppears[i]);
        }
         msg.writer().writeShort(b.head);
          msg.writer().writeShort(b.body);
           msg.writer().writeShort(b.leg);
        if (b.dropItems.size() > 0) {
            msg.writer().writeByte(b.dropItems.size());
            for (int i = 0; i < b.dropItems.size(); i++) {
                Item item = b.dropItems.get(i);
                msg.writer().writeInt(item.template.id);
                msg.writer().writeInt(item.quantity);
                msg.writer().writeByte(item.itemOptions.size());
                if (item.itemOptions.size() > 0) {
                    for (int j = 0; j < item.itemOptions.size(); j++) {
                        ItemOption o = item.itemOptions.get(j);
                        msg.writer().writeByte(o.optionTemplate.id);
                        msg.writer().writeLong(o.param);
                    }
                }
            }
        }
        player.sendMessage(msg);
    }
}
