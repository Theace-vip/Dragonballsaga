package boss.boss_manifest.DaiChienThanThu;

import boss.Boss;
import boss.BossID;
import boss.BossesData;
import item.Item;
import map.ItemMap;
import player.Player;
import services.Service;
import utils.Util;

public class HaiKhuyen extends Boss {
    public HaiKhuyen() throws Exception{
        super(BossID.HAI_KHUYEN,BossesData.HAI_KHUYEN);
    }
    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(40, 100)) {
            ItemMap it = new ItemMap(this.zone, 1015, Util.nextInt(5,15), this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        
        if (Util.isTrue(10, 100)) {
            int point = Util.nextInt(4, 7);
            int id = Util.nextInt(1173, 1184);
            ItemMap it = new ItemMap(this.zone, id, 1, this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            int an = 1;
            it.options.add(new Item.ItemOption(50, point));
            it.options.add(new Item.ItemOption(77, point));
            it.options.add(new Item.ItemOption(103, point));
            it.options.add(new Item.ItemOption(210, an));
            if(Util.isTrue(999, 1000)){
                it.options.add(new Item.ItemOption(93, Util.nextInt(3,5)));
            }
            Service.gI().dropItemMap(this.zone, it);
        }
        
    }
    
}
