package boss.ServerData.Boss.TASK.Other;

import boss.Boss;
import boss.BossID;
import boss.BossesData;
import item.Item;
import map.ItemMap;
import player.Player;
import services.PlayerService;
import services.Service;
import utils.Util;

/**
 *
 * @author Administrator
 */
public class BaDo extends Boss {

    public BaDo() throws Exception {
        super(BossID.O_DO_NEW, BossesData.O_DO_NEW);
    }

    private static final int[] itemsReward = {441, 442, 443, 444, 445, 446, 447, 459};

    private static final String[] textChat = new String[]{"Hôi quá, tránh xa ta ra", "Biến đi", "Trời ơi đồ ở dơ", "Thúi quá", "Mùi gì hôi quá"};

    private long lastTimeSubHp;

    public void subHpWithOdo() {
        if (Util.canDoWithTime(lastTimeSubHp, 10000)) {
            try {
                zone.getNotBosses().forEach(pl -> {
                    try {
                        if (pl != null && !pl.isNewPet && !pl.isDie() && pl.nPoint != null) {
                            Service.gI().chat(pl, textChat[Util.nextInt(textChat.length)]);
                            pl.injured(this, Math.min(pl.nPoint.hp - 1, pl.nPoint.hpMax * 10 / 100), true, false);
                            PlayerService.gI().sendInfoHp(pl);
                            Service.gI().Send_Info_NV(pl);
                        }
                    } catch (Exception e) {
                        this.chat("Hừ, thèm mút buồi quá...");
                    }
                });
            } catch (Exception e) {
            }
            this.lastTimeSubHp = System.currentTimeMillis();
        }
    }

    @Override
    public void attack() {
        super.attack();
        subHpWithOdo();
    }

    @Override
    public void reward(Player plKill) {
        int[] saophale = {441, 442, 443, 444, 445, 446, 447};
        int saocount = 1; // số lượng rơi
        for (int i = 0; i < saocount; i++) {
            // chọn ngẫu nhiên 1 ID trong mảng
            int id = saophale[Util.nextInt(0, saophale.length - 1)];

            if (Util.isTrue(2, 100)) {
                ItemMap carot = new ItemMap(zone, id, 1, 10 * i + this.location.x,
                        zone.map.yPhysicInTop(this.location.x, 0), -1);
                int rand = Util.nextInt(0, 6);
                ItemMap it = new ItemMap(zone, 441 + rand, 1, this.location.x,
                        zone.map.yPhysicInTop(this.location.x, 0), -1);
                carot.options.add(new Item.ItemOption(95 + rand, (rand == 3 || rand == 4) ? 3 : 5));
                Service.getInstance().dropItemMap(this.zone, carot);
            }
        }
        for (int i = 0; i < 50; i += 10) {
            try {
                Service.gI().dropItemMap(new ItemMap(zone, 1271, Util.nextInt(i), location.x + i, location.y, plKill.id));
                Service.gI().dropItemMap(new ItemMap(zone, 190, Util.nextInt(1,1000000), location.x - i, location.y, plKill.id));
            } catch (Exception e) {
            }
        }
    }

    @Override
    public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            this.nPoint.subHP(damage > 500 ? damage = Util.nextInt(450, 500) : damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }
}
