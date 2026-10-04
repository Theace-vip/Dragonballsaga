package boss.boss_manifest.NamekGinyuForce;

import boss.Boss;
import boss.BossID;
import boss.BossManager;
import boss.BossesData;
import item.Item.ItemOption;
import map.ItemMap;
import player.Player;
import services.Service;
import utils.Util;



public class SO1_NM extends Boss {

    Boss bTogether;

    public SO1_NM() throws Exception {
        super(BossID.SO_1_NM, BossesData.SO_1_NM);
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(50, 100)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Util.nextInt(17, 20), 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id));
        } else {
            ItemMap it = new ItemMap(this.zone, 432, 1, location.x, location.y, plKill.id);
            it.options.add(new ItemOption(50, Util.nextInt(12, 500)));
            it.options.add(new ItemOption(94, Util.nextInt(8, 20)));
            it.options.add(new ItemOption(231, Util.nextInt(2) + 3));
            Service.getInstance().dropItemMap(this.zone, it);
        }
    }

    @Override
    public void active() {
        if (bTogether != null && bTogether.zone != null) {
            return;
        }
        super.active();
    }

    @Override
    public void joinMap() {
        super.joinMap();
        bTogether = BossManager.gI().getBoss(BossID.SO_2_NM);
        location.x = BossManager.gI().getBoss(BossID.TIEU_DOI_TRUONG_NM).location.x + 40;
    }
}
