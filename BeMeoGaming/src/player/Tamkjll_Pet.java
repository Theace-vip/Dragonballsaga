/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package player;

import consts.ConstPlayer;
import mob.Mob;
import server.ServerNotify;
import services.MapService;
import services.PlayerService;
import services.Service;
import services.SkillService;
import services.func.ChangeMapService;
import skill.Skill;
import utils.Util;

/**
 *
 * @author Administrator
 */
public class Tamkjll_Pet extends Player {

    public Player master;
    public static final byte FOLLOW = 0;
    public static final byte ATTACK_PLAYER = 1;
    public static final byte ATTACK_MOB = 2;
    public static final byte GOHOME = 3;
    public byte status = 0;
    public static int idb = -4000000;

    public Tamkjll_Pet(Player master) {
        this.master = master;
        this.isNewPet = true;
        this.id = idb;
        idb--;
    }

    @Override
    public short getHead() {
        return 573;
    }

    @Override
    public short getBody() {
        return 574;
    }

    @Override
    public short getLeg() {
        return 575;
    }

    public byte getStatus() {
        return this.status;
    }

    private boolean goingHome;
    private long lastTimeTargetPlayer;
    private int timeTargetPlayer;

    private static final short ARANGE_CAN_ATTACK = 1500;

    public void changeStatus(byte status) {
        if (goingHome || this.isDie()) {
            Service.gI().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (status == GOHOME) {
            goHome();
        }
        this.status = status;
    }

    public void goHome() {
        if (this.status == GOHOME) {
            return;
        }
        goingHome = true;
        ChangeMapService.gI().goToMap(this, MapService.gI().getMapCanJoin(this, master.gender + 21, -1));
        this.zone.load_Me_To_Another(this);
        Tamkjll_Pet.this.status = Tamkjll_Pet.GOHOME;
        goingHome = false;
    }

    private Mob findMobAttack() {
        int dis = ARANGE_CAN_ATTACK;
        Mob mobAtt = null;
        for (Mob mob : zone.mobs) {
            if (mob.isDie()) {
                continue;
            }
            int d = Util.getDistance(this, mob);
            if (d <= dis) {
                dis = d;
                mobAtt = mob;
            }
        }
        return mobAtt;
    }

    public Player getPlayerAttack() {
        if (master.TamkjllPlayerAttack != null) {
            if (master.TamkjllPlayerAttack != null
                    && (master.TamkjllPlayerAttack.isDie() || !this.zone.equals(master.TamkjllPlayerAttack.zone))) {
                master.TamkjllPlayerAttack = null;
                if (master.TamkjllPlayerAttack == null) {
                    if (this.playerAttack != null
                            && (this.playerAttack.isDie() || !this.zone.equals(this.playerAttack.zone)
                            || (this.playerAttack.pvp == null || this.pvp == null)
                            || (this.playerAttack.typePk != ConstPlayer.PK_ALL
                            || this.typePk != ConstPlayer.PK_ALL)
                            || (!this.pvp.isInPVP(this.playerAttack) || !this.playerAttack.pvp.isInPVP(this))
                            || ((this.playerAttack.cFlag == 0 && this.cFlag == 0)
                            && (this.playerAttack.cFlag != 8 || this.cFlag == this.playerAttack.cFlag)))
                            || this.playerAttack == this || this.playerAttack == master) {
                        this.playerAttack = null;
                    }
                    if (this.zone != null
                            && (this.playerAttack == null || this.playerAttack == this || this.playerAttack == master)
                            || Util.canDoWithTime(this.lastTimeTargetPlayer, this.timeTargetPlayer)) {
                        this.playerAttack = this.zone.PlayerPKinmap();
                        this.lastTimeTargetPlayer = System.currentTimeMillis();
                        this.timeTargetPlayer = Util.nextInt(40000, 45000);
                    }
                    return this.playerAttack;
                }
            }
            return master.TamkjllPlayerAttack;
        } else {
            if (this.playerAttack != null && (this.playerAttack.isDie() || !this.zone.equals(this.playerAttack.zone)
                    || (this.playerAttack.pvp == null || this.pvp == null)
                    || (this.playerAttack.typePk != ConstPlayer.PK_ALL || this.typePk != ConstPlayer.PK_ALL)
                    || (!this.pvp.isInPVP(this.playerAttack) || !this.playerAttack.pvp.isInPVP(this))
                    || ((this.playerAttack.cFlag == 0 && this.cFlag == 0)
                    && (this.playerAttack.cFlag != 8 || this.cFlag == this.playerAttack.cFlag)))
                    || this.playerAttack == this || this.playerAttack == master) {
                this.playerAttack = null;
            }
            if (this.zone != null
                    && (this.playerAttack == null || this.playerAttack == this || this.playerAttack == master)
                    || Util.canDoWithTime(this.lastTimeTargetPlayer, this.timeTargetPlayer)) {
                this.playerAttack = this.zone.PlayerPKinmap();
                this.lastTimeTargetPlayer = System.currentTimeMillis();
                this.timeTargetPlayer = Util.nextInt(40000, 45000);
            }
            return this.playerAttack;
        }
    }

    public void joinMapMaster() {
        if (status != GOHOME && !isDie()) {
            this.location.x = master.location.x + Util.nextInt(-10, 10);
            this.location.y = master.location.y;
            ChangeMapService.gI().goToMap(this, master.zone);
            this.zone.load_Me_To_Another(this);
        }
    }

    private long lastTimeMoveAtHome;
    private byte directAtHome = -1;
    private long lastTimeMoveIdle;
    private int timeMoveIdle;
    public boolean idle;

    private void moveIdle() {
        if (status == GOHOME) {
            return;
        }
        if (idle && Util.canDoWithTime(lastTimeMoveIdle, timeMoveIdle)) {
            int dir = this.location.x - master.location.x <= 0 ? -1 : 1;
            PlayerService.gI().playerMove(this, master.location.x
                    + Util.nextInt(dir == -1 ? 30 : -50, dir == -1 ? 50 : 30), master.location.y);
            lastTimeMoveIdle = System.currentTimeMillis();
            timeMoveIdle = Util.nextInt(5000, 8000);
        }
    }

    public long LasttimeHs;

    private Mob mobAttack;
    private Player playerAttack;

    @Override
    public void update() {
        super.update();
        if (isDie()) {
            if (System.currentTimeMillis() - LasttimeHs > 30000) {
                Service.gI().hsChar(this, nPoint.hpMax, nPoint.mpMax);
            } else {
                return;
            }
        }
        if (master != null && (this.zone == null || this.zone != master.zone)) {
            joinMapMaster();
        }
        if (master != null && master.isDie() || effectSkill.isHaveEffectSkill()) {
            return;
        }
        if (Util.canDoWithTime(master.TamkjlllastTimeThucan,
                panel.tuning.SystemTuning.getL("embe_chu_ky_thuc_an"))) {
            master.TamkjlllastTimeThucan = System.currentTimeMillis();
            master.TamkjllPetHunger -= panel.tuning.SystemTuning.getI("embe_thuc_an_tru_moi_chu_ky");
            master.TamkjllPetPower += Util.nextInt(
                    panel.tuning.SystemTuning.getI("embe_sm_moi_chu_ky_min"),
                    panel.tuning.SystemTuning.getI("embe_sm_moi_chu_ky_max"));
            if (master.TamkjllPetHunger <= 0) {
                master.TamkjllPetGiong = -1;
                Service.gI().sendThongBaoOK(master, "Đạo Lữ vì quá đói nên đã bỏ nhà ra đi");
            } else if (master.TamkjllPetHunger > 0
                    && master.TamkjllPetHunger <= panel.tuning.SystemTuning.getI("embe_canh_bao_do")) {
                Service.gI().sendThongBaoOK(master, "Thức ăn pet dưới "
                        + panel.tuning.SystemTuning.getI("embe_canh_bao_do") + "%");
            }
            ServerNotify.gI().sendNotifyTab(master);
        }
        moveIdle();
        switch (status) {
            case FOLLOW:
                followMaster(60);
                break;
            case ATTACK_PLAYER:
                Player plPr = getPlayerAttack();
                if (plPr != null && plPr != this && plPr != master && plPr.location != null) {
                    this.playerSkill.skillSelect = getSkill(1);
                    if (SkillService.gI().canUseSkillWithCooldown(this)) {
                        PlayerService.gI().playerMove(this, plPr.location.x + Util.nextInt(-60, 60),
                                plPr.location.y);
                        SkillService.gI().useSkill(this, plPr, null, -1, null);
                    }
                } else {
                    idle = true;
                }
                break;
            case ATTACK_MOB:
                mobAttack = findMobAttack();
                if (mobAttack != null) {
                    this.playerSkill.skillSelect = getSkill(1);
                    if (SkillService.gI().canUseSkillWithCooldown(this)) {
                        PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-60, 60),
                                mobAttack.location.y);
                        SkillService.gI().useSkill(this, null, mobAttack, -1, null);
                    }
                } else {
                    idle = true;
                }
                break;
            case GOHOME:
                if (this.zone != null
                        && (this.zone.map.mapId == 21 || this.zone.map.mapId == 22 || this.zone.map.mapId == 23)) {
                    if (System.currentTimeMillis() - lastTimeMoveAtHome <= 5000) {
                        return;
                    } else {
                        if (this.zone.map.mapId == 21) {
                            if (directAtHome == -1) {

                                PlayerService.gI().playerMove(this, 250, 336);
                                directAtHome = 1;
                            } else {
                                PlayerService.gI().playerMove(this, 200, 336);
                                directAtHome = -1;
                            }
                        } else if (this.zone.map.mapId == 22) {
                            if (directAtHome == -1) {
                                PlayerService.gI().playerMove(this, 500, 336);
                                directAtHome = 1;
                            } else {
                                PlayerService.gI().playerMove(this, 452, 336);
                                directAtHome = -1;
                            }
                        } else if (this.zone.map.mapId == 22) {
                            if (directAtHome == -1) {
                                PlayerService.gI().playerMove(this, 250, 336);
                                directAtHome = 1;
                            } else {
                                PlayerService.gI().playerMove(this, 200, 336);
                                directAtHome = -1;
                            }
                        }
                        lastTimeMoveAtHome = System.currentTimeMillis();
                    }
                }
                break;
        }
    }

    private Skill getSkill(int indexSkill) {
        return this.playerSkill.skills.get(indexSkill - 1);
    }

    public void followMaster() {
        if (this.isDie() || effectSkill.isHaveEffectSkill()) {
            return;
        }
        switch (this.status) {
            case ATTACK_MOB:
                if (mobAttack != null && Util.getDistance(this, master) <= 1500) {
                    break;
                }
            case ATTACK_PLAYER:
                if (getPlayerAttack() != null && Util.getDistance(this, master) <= 1500) {
                    break;
                }
            case FOLLOW:
                followMaster(500);
                break;
        }
    }

    private void followMaster(int dis) {
        int mX = master.location.x;
        int mY = master.location.y;
        int disX = this.location.x - mX;
        if (Math.sqrt(Math.pow(mX - this.location.x, 2) + Math.pow(mY - this.location.y, 2)) >= dis) {
            if (disX < 0) {
                this.location.x = mX - Util.nextInt(0, dis);
            } else {
                this.location.x = mX + Util.nextInt(0, dis);
            }
            this.location.y = mY;
            PlayerService.gI().playerMove(this, this.location.x, this.location.y);
        }
    }

    @Override
    public void dispose() {
        this.master = null;
        super.dispose();
    }
}
