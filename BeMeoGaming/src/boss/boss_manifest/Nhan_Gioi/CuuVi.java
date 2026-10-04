package boss.boss_manifest.Nhan_Gioi;

import boss.Boss;
import boss.BossID;
import boss.BossesData;
import item.Item;
import map.ItemMap;
import player.Player;
import services.Service;
import utils.Util;

public class CuuVi extends Boss {
    public CuuVi() throws Exception {
        super(BossID.CUU_VI, BossesData.CUU_VI);
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(20, 100)) {
            ItemMap it = new ItemMap(this.zone, 1536, Util.nextInt(20,50), this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        if (Util.isTrue(10, 100)) {
            int[] nrs = { 15, 16, 17, 18 };
            ItemMap it = new ItemMap(this.zone, nrs[Util.nextInt(nrs.length)], 1, this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        if (Util.isTrue(10, 100)) {
            int point = Util.nextInt(4, 7);
            ItemMap it = new ItemMap(this.zone, 1461, 1, this.location.x,
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
