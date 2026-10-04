package boss.boss_manifest.Nhan_Gioi;

import boss.Boss;
import boss.BossID;
import boss.BossStatus;
import boss.BossesData;
import item.Item;
import map.ItemMap;
import player.Player;
import services.EffectSkillService;
import services.InventoryService;
import services.Service;
import utils.Util;

public class Obito extends Boss {
    public Obito() throws Exception{
        super(BossID.OBITO, false, true, BossesData.OBITO);
        for(int i=0;i<13;i++){
            Item it = new Item();
            this.inventory.itemsBody.add(it);
        }
        Item it = new Item((short)1689);
        InventoryService.gI().putItemBody(this,it);
    }
    boolean hoaLucDao = false;
    public void LucDao(){
        this.hoaLucDao = true;
         if (this.bossAppearTogether == null || this.bossAppearTogether[this.currentLevel] == null) {
            return;
        }
        for (Boss boss : this.bossAppearTogether[this.currentLevel]) {
            if (boss.id == BossID.OBITO_LUC_DAO) {
                boss.changeStatus(BossStatus.RESPAWN);
            } 
        }
        this.hoaLucDao = true;
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
            int id = Util.nextInt(1687,1689);
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
    @Override
    protected void resetBase() {
        super.resetBase();
        this.hoaLucDao = false;
    }
    @Override
    public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }

            damage += damage * plAtt.nPoint.dameBoss / 100;
            damage = this.nPoint.subDameInjureWithDeff(damage);

            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if(damage >= this.nPoint.hp && !this.hoaLucDao){
                this.LucDao();
            }
            this.nPoint.subHP(damage);

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
