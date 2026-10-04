package boss.boss_manifest.DaiChienThanThu;

import boss.Boss;
import boss.BossID;
import boss.BossesData;
import item.Item;
import map.ItemMap;
import player.Player;
import services.Service;
import utils.Util;

public class VuaBachThu extends Boss {
    public VuaBachThu() throws Exception{
        super(BossID.VUA_BACH_THU,BossesData.VUA_BACH_THU);
    }
    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(40, 100)) {
            ItemMap it = new ItemMap(this.zone, 1015, Util.nextInt(15,25), this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        
        if (Util.isTrue(10, 100)) {
            int point = Util.nextInt(4, 7);
            int[] lst = {1471,1472,1480,1561,1797,1799,1804};
            int id = lst[Util.nextInt(lst.length)];
            ItemMap it = new ItemMap(this.zone, id, 1, this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            int an = 1;
            it.options.add(new Item.ItemOption(50, point));
            it.options.add(new Item.ItemOption(77, point));
            it.options.add(new Item.ItemOption(103, point));
            it.options.add(new Item.ItemOption(210, an));
            if(Util.isTrue(990, 1000)){
                it.options.add(new Item.ItemOption(93, Util.nextInt(3,5)));
            }
            Service.gI().dropItemMap(this.zone, it);
        }
        
    }
}
