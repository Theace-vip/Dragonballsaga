package boss.boss_manifest.WorldBoss;

import boss.*;
import player.Pet;
import player.Player;
import services.Service;
import services.func.ChangeMapService;
import utils.Util;

/**
 * WorldBoss V2 - Boss The Gioi BAT TU, tinh dame Top 10, xoay khu, song theo LIFE_MIN.
 * - Khong bao gio chet: HP tu hoi khi gan het, bo qua die().
 * - Moi don danh deu cong vao WorldBossTracker theo player that (pet -> chu).
 * - HP/DAME/MAP lay tu panel.tuning.WorldBossTuning (admin sua tren panel).
 * - Xoay khu moi ZONE_SEC giay, song het LIFE_MIN thi trao thuong roi hoi sinh tiep.
 */
public class WorldBoss extends Boss {

    private long lastTopChat = 0;
    private long bornAt = 0;
    private long lastZoneHop = 0;
    private int zoneIdx = 0;
    private volatile boolean killedByAdmin = false;

    public WorldBoss() throws Exception {
        super(BossID.WORLD_BOSS, BossesData.WORLD_BOSS);
        try {
            int life = 30;
            try { life = Math.max(1, (int) panel.tuning.WorldBossTuning.LIFE_MIN); } catch (Exception e) {}
            WorldBossTracker.start(life);
        } catch (Exception e) {}
    }

    @Override
    public void initBase() {
        super.initBase();
        try {
            panel.tuning.WorldBossTuning.load();
            if (panel.tuning.WorldBossTuning.HP > 0) {
                this.nPoint.hpg = panel.tuning.WorldBossTuning.HP;
                this.nPoint.hp = panel.tuning.WorldBossTuning.HP;
            }
            if (panel.tuning.WorldBossTuning.DAME >= 0) {
                this.nPoint.dameg = panel.tuning.WorldBossTuning.DAME;
            }
            this.nPoint.calPoint();
            this.nPoint.hp = this.nPoint.hpMax;
        } catch (Exception e) {}
    }

    @Override
    public void joinMap() {
        try {
            panel.tuning.WorldBossTuning.load();
            int mapId = panel.tuning.WorldBossTuning.MAP_ID;
            int lifeMin = 30;
            try { lifeMin = Math.max(1, (int) panel.tuning.WorldBossTuning.LIFE_MIN); } catch (Exception e) {}
            map.Map map = services.MapService.gI().getMapById(mapId);
            if (map != null && map.zones != null && !map.zones.isEmpty()) {
                map.Zone z = null;
                try {
                    int sz = panel.tuning.WorldBossTuning.START_ZONE;
                    if (sz >= 0 && sz < map.zones.size()) {
                        z = map.zones.get(sz);
                    } else {
                        z = map.zones.get(0);
                    }
                } catch (Exception e) {
                    z = map.zones.get(0);
                }
                if (z != null) {
                    int x = 300;
                    try { x = Util.nextInt(100, Math.max(200, z.map.mapWidth - 100)); } catch (Exception e) {}
                    int y = 300;
                    try { y = z.map.yPhysicInTop(300, 100); } catch (Exception e) {}
                    ChangeMapService.gI().changeMap(this, z, x, y);
                    this.changeStatus(BossStatus.CHAT_S);
                    try { Service.gI().sendThongBaoAllPlayer("Boss The Gioi da xuat hien tai " + z.map.mapName + " - Khu " + z.zoneId + "!"); } catch (Exception ex) {}
                    try { WorldBossTracker.start(lifeMin); } catch (Exception ex) {}
                    bornAt = System.currentTimeMillis();
                    lastZoneHop = bornAt;
                    zoneIdx = 0;
                    try { this.nPoint.hp = this.nPoint.hpMax; } catch (Exception ex) {}
                    return;
                }
            }
        } catch (Exception e) {}
        try {
            super.joinMap();
        } catch (Exception e) {}
        try {
            if (bornAt <= 0) bornAt = System.currentTimeMillis();
            if (lastZoneHop <= 0) lastZoneHop = bornAt;
        } catch (Exception e) {}
    }

    /**
     * Giet boss tu panel: thoat khoi map + xoa khoi BossManager.
     * Boss bi go khoi vong update nen KHONG the hoi sinh/hoac song tiep - chi goi
     * lai duoc bang nut "Goi Boss The Gioi" hoac lich SPAWN_TIMES.
     * @return vi tri boss truoc khi bi giet (de panel hien thi)
     */
    public synchronized String adminKill() {
        String viTri = "chua o map";
        try {
            if (this.zone != null && this.zone.map != null) {
                viTri = this.zone.map.mapName + " [" + this.zone.map.mapId + "] - Khu " + this.zone.zoneId;
            }
        } catch (Exception e) {}
        killedByAdmin = true;
        try { this.chat("Ta thua roi... Top dame dot nay ket thuc!"); } catch (Exception e) {}
        try { Service.gI().sendThongBaoAllPlayer("Boss The Gioi da bi tieu diet tai " + viTri + "!"); } catch (Exception e) {}
        try { ChangeMapService.gI().exitMap(this); } catch (Exception e) {}
        try { this.zone = null; } catch (Exception e) {}
        try { this.lastZone = null; } catch (Exception e) {}
        try { BossManager.gI().removeBoss(this); } catch (Exception e) {}
        return viTri;
    }

    public boolean isKilledByAdmin() { return killedByAdmin; }

    @Override
    public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (killedByAdmin) return 0;
        if (plAtt == null) return 0;
        Player real = plAtt;
        try {
            if (plAtt.isPet && plAtt instanceof Pet) {
                Pet pet = (Pet) plAtt;
                if (pet.master != null) real = pet.master;
            }
        } catch (Exception e) {}
        if (real == null || real.isBoss) return 0;

        double dmg = damage;
        try { dmg = this.nPoint.subDameInjureWithDeff(damage); } catch (Exception e) {}
        if (dmg < 0) dmg = 0;
        if (dmg > 0) {
            try { WorldBossTracker.addDame(real.id, real.name, dmg); } catch (Exception e) {}
        }
        try {
            this.nPoint.subHP(dmg);
            if (this.nPoint.hp <= 0 || this.isDie()) {
                this.nPoint.hp = this.nPoint.hpMax;
                this.nPoint.setHp(this.nPoint.hpMax);
                try { Service.getInstance().point(this); } catch (Exception e) {}
                chatTop();
            } else if (this.nPoint.hp < this.nPoint.hpMax * 0.05) {
                this.nPoint.hp = Math.min(this.nPoint.hpMax, this.nPoint.hp + this.nPoint.hpMax * 0.2);
            }
        } catch (Exception e) {}
        if (Util.canDoWithTime(lastTopChat, 60000)) {
            lastTopChat = System.currentTimeMillis();
            chatTop();
        }
        return dmg;
    }

    private void chatTop() {
        try {
            java.util.List<WorldBossTracker.Entry> t = WorldBossTracker.top(3);
            if (t.isEmpty()) {
                this.chat("Ta bat tu! Ai danh dau nhat se Top 1!");
                return;
            }
            String s = "Top dame: 1." + t.get(0).name
                    + (t.size() > 1 ? " 2." + t.get(1).name : "")
                    + (t.size() > 2 ? " 3." + t.get(2).name : "")
                    + " - Ta khong bao gio guc nga!";
            this.chat(s);
        } catch (Exception e) {}
    }

    @Override
    public void die(Player plKill) {
        try {
            this.nPoint.hp = this.nPoint.hpMax;
            this.nPoint.setHp(this.nPoint.hpMax);
        } catch (Exception e) {}
    }

    @Override
    public void reward(Player plKill) {
    }

    @Override
    public void autoLeaveMap() {
        try {
            if (killedByAdmin) return;
            if (bornAt <= 0) bornAt = System.currentTimeMillis();
            if (lastZoneHop <= 0) lastZoneHop = bornAt;
            long now = System.currentTimeMillis();
            long lifeMin = 60;
            long zoneSec = 15;
            try {
                panel.tuning.WorldBossTuning.load();
                lifeMin = panel.tuning.WorldBossTuning.LIFE_MIN;
                zoneSec = panel.tuning.WorldBossTuning.ZONE_SEC;
            } catch (Exception e) {}
            if (lifeMin <= 0) lifeMin = 60;
            if (zoneSec < 5) zoneSec = 5;
            if (now - bornAt > lifeMin * 60000L) {
                String log = "";
                try { log = WorldBossTracker.rewardAll(); } catch (Exception e) {}
                try { utils.Logger.log("WorldBoss ket thuc:\n" + log + "\n"); } catch (Exception e) {}
                try { WorldBossTracker.reset(); } catch (Exception e) {}
                try { WorldBossTracker.start(lifeMin); } catch (Exception e) {}
                bornAt = now;
                lastZoneHop = now;
                zoneIdx = 0;
                try {
                    this.nPoint.hp = this.nPoint.hpMax;
                    this.nPoint.setHp(this.nPoint.hpMax);
                    try { Service.getInstance().point(this); } catch (Exception e) {}
                } catch (Exception e) {}
                return;
            }
            if (now - lastZoneHop >= zoneSec * 1000L) {
                lastZoneHop = now;
                try {
                    if (this.zone != null && this.zone.map != null && this.zone.map.zones != null && this.zone.map.zones.size() > 1) {
                        zoneIdx++;
                        java.util.List<map.Zone> zones = this.zone.map.zones;
                        map.Zone nz = zones.get(Math.abs(zoneIdx) % zones.size());
                        if (nz != null && nz != this.zone) {
                            int x = 300;
                            try { x = Util.nextInt(100, Math.max(200, nz.map.mapWidth - 100)); } catch (Exception e) {}
                            int y = 300;
                            try { y = nz.map.yPhysicInTop(300, 100); } catch (Exception e) {}
                            ChangeMapService.gI().changeMap(this, nz, x, y);
                            try { Service.gI().sendThongBaoAllPlayer("Boss The Gioi di chuyen sang Khu " + nz.zoneId + "!"); } catch (Exception e) {}
                        }
                    }
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
    }
}
