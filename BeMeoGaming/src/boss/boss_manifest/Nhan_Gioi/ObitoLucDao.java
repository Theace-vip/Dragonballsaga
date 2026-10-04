package boss.boss_manifest.Nhan_Gioi;

import boss.Boss;
import boss.BossID;
import boss.BossesData;
import item.Item;
import map.ItemMap;
import player.Player;
import services.Service;
import utils.Util;

public class ObitoLucDao extends Boss {
    public ObitoLucDao() throws Exception{
        super(BossID.OBITO_LUC_DAO,BossesData.OBITO_LUC_DAO);
    }
    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(40, 100)) {
            ItemMap it = new ItemMap(this.zone, 1536, Util.nextInt(20,50), this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        if (Util.isTrue(40, 100)) {
            ItemMap it = new ItemMap(this.zone, 1525, Util.nextInt(5,20), this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        if (Util.isTrue(50, 100)) {
            int[] nrs = { 15, 16, 17, 18 };
            ItemMap it = new ItemMap(this.zone, nrs[Util.nextInt(nrs.length)], 1, this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }

        if (Util.isTrue(10, 100)) {
            int point = Util.nextInt(4, 7);
            int[] li = {1513,1521,1522,1523,1470,1690};
            int id = li[Util.nextInt(li.length)];
            ItemMap it = new ItemMap(this.zone, id, 1, this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x,
                            this.location.y - 24),
                    plKill.id);
            int an = 1;
            if(it.itemTemplate.type == 5){
                an = Util.nextInt(2,4);
                point = Util.nextInt(15,27);
            }
            it.options.add(new Item.ItemOption(50, point));
            it.options.add(new Item.ItemOption(77, point));
            it.options.add(new Item.ItemOption(103, point));
            it.options.add(new Item.ItemOption(210, an));
            if(Util.isTrue(98, 100)){
                it.options.add(new Item.ItemOption(93, Util.nextInt(3,5)));
            }
            Service.gI().dropItemMap(this.zone, it);
        }
        
    }
}
