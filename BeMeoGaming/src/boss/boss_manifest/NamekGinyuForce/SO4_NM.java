package boss.boss_manifest.NamekGinyuForce;

import boss.Boss;
import boss.BossID;
import boss.BossManager;
import boss.BossesData;
import item.Item;
import map.ItemMap;
import player.Player;
import services.Service;
import utils.Util;



public class SO4_NM extends Boss {

    public SO4_NM() throws Exception {
        super(BossID.SO_4_NM, BossesData.SO_4_NM);
    }

   @Override
    public void reward(Player plKill) {
        if (Util.isTrue(50, 100)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Util.nextInt(17, 20), 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id));
        } else {
            ItemMap it = new ItemMap(this.zone, 429, 1, location.x, location.y, plKill.id);
            it.options.add(new Item.ItemOption(50, Util.nextInt(12, 500)));
            it.options.add(new Item.ItemOption(94, Util.nextInt(8, 20)));
            it.options.add(new Item.ItemOption(231, Util.nextInt(2) + 3));
            Service.getInstance().dropItemMap(this.zone, it);
        }
    }

    @Override
    public void joinMap() {
        super.joinMap();
        location.x = BossManager.gI().getBoss(BossID.SO_3_NM).location.x + 40;
    }
}
