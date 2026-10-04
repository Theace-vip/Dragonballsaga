//package boss.boss_manifest.Broly;
//
//import boss.*;
//import consts.ConstPlayer;
//import java.util.Random;
//import map.ItemMap;
//import player.Player;
//import services.PetService;
//import services.Service;
//import services.SkillService;
//import services.func.ChangeMapService;
//import skill.Skill;
//import utils.SkillUtil;
//import utils.Util;
//
//
//
//public class SuperBrolyNew extends Boss {
//
//    public SuperBrolyNew() throws Exception {
//        super(BossID.SUPER_BROLY, false, true, new BossData(
//                "Super Broly", // name
//                ConstPlayer.XAYDA, // gender
//                new short[] { 294, 295, 296, -1, -1, -1 }, // outfit {head, body, leg, bag, aura, eff}
//                100, // dame
//                new double[] { 1000 }, // hp
//                new int[] { 5, 13, 20, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38 }, // map join
//                new int[][] {
//                        { Skill.TAI_TAO_NANG_LUONG, 1, 1000 }, { Skill.TAI_TAO_NANG_LUONG, 2, 1000 },
//                        { Skill.TAI_TAO_NANG_LUONG, 3, 1000 }, { Skill.TAI_TAO_NANG_LUONG, 4, 1000 },
//                        { Skill.TAI_TAO_NANG_LUONG, 5, 1000 }, { Skill.TAI_TAO_NANG_LUONG, 6, 1000 },
//                        { Skill.TAI_TAO_NANG_LUONG, 7, 1000 },
//                        { Skill.DRAGON, 1, 1000 }, { Skill.DRAGON, 2, 1000 }, { Skill.DRAGON, 3, 1000 },
//                        { Skill.DRAGON, 4, 1000 }, { Skill.DRAGON, 5, 1000 }, { Skill.DRAGON, 6, 1000 },
//                        { Skill.DRAGON, 7, 1000 },
//                        { Skill.DEMON, 1, 1000 }, { Skill.DEMON, 2, 1000 }, { Skill.DEMON, 3, 1000 },
//                        { Skill.DEMON, 4, 1000 }, { Skill.DEMON, 5, 1000 }, { Skill.DEMON, 6, 1000 },
//                        { Skill.DEMON, 7, 1000 },
//                        { Skill.GALICK, 1, 1000 }, { Skill.GALICK, 2, 1000 }, { Skill.GALICK, 3, 1000 },
//                        { Skill.GALICK, 4, 1000 }, { Skill.GALICK, 5, 1000 }, { Skill.GALICK, 6, 1000 },
//                        { Skill.GALICK, 7, 1000 },
//                        { Skill.KAMEJOKO, 1, 1000 }, { Skill.KAMEJOKO, 2, 1000 }, { Skill.KAMEJOKO, 3, 1000 },
//                        { Skill.KAMEJOKO, 4, 1000 }, { Skill.KAMEJOKO, 5, 1000 }, { Skill.KAMEJOKO, 6, 1000 },
//                        { Skill.KAMEJOKO, 7, 1000 },
//                        { Skill.MASENKO, 1, 1000 }, { Skill.MASENKO, 2, 1000 }, { Skill.MASENKO, 3, 1000 },
//                        { Skill.MASENKO, 4, 1000 }, { Skill.MASENKO, 5, 1000 }, { Skill.MASENKO, 6, 1000 },
//                        { Skill.MASENKO, 7, 1000 },
//                        { Skill.ANTOMIC, 1, 1000 }, { Skill.ANTOMIC, 2, 1000 }, { Skill.ANTOMIC, 3, 1000 },
//                        { Skill.ANTOMIC, 4, 1000 }, { Skill.ANTOMIC, 5, 1000 }, { Skill.ANTOMIC, 6, 1000 },
//                        { Skill.ANTOMIC, 7, 1000 }, }, // skill
//                new String[] {}, // text chat 1
//                new String[] { "|-1|Haha! ta sẽ giết hết các ngươi",
//                        "|-1|Sức mạnh của ta là tuyệt đối",
//                        "|-1|Vào hết đây!!!", }, // text chat 2
//                new String[] { "|-1|Các ngươi giỏi lắm. Ta sẽ quay lại." }, // text chat 3
//                1// type appear
//        ));
//    }
//
//    @Override
//    public void reward(Player plKill) {
//        // ItemMap it = new ItemMap(this.zone, 568, 1, this.location.x + Util.nextInt(-15, 15),
//        //         this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
//        // Service.gI().dropItemMap(this.zone, it);
//        if(plKill.pet == null){
//            PetService.gI().changeBroly(plKill);
//            Service.gI().sendThongBao(plKill, "Bạn nhận được đệ tử! Broly");
//        }
//    }
//
////     @Override
////    public void reward(Player plKill) {
////        
////        
////           int diemsb = Util.nextInt(1, 2); //50 sd 
////        plKill.point_sb += diemsb;
////         Service.getInstance().sendThongBaoBenDuoi("Bạn \n" + plKill.name  + "\nNhận Được " + diemsb + " \nĐiểm Săn Boss");        
////         
////      int[] detubase = new int[]{722,1214,1215,1160};//list item
////      int[] detuvip = new int[]{1300,1301,1302,1160,1160,1160,1160,1160,1160};//list item
////      
////        int detubaselist = new Random().nextInt(detubase.length);
////         int detubaselistvip = new Random().nextInt(detuvip.length);
////        //****************************************************************list rơi bên dưới****************************************************
////
////           if (Util.isTrue(95, 100)) { // tỉ lệ rơi item theo vip player
////            Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, detubase[detubaselist], 1, this.location.x,
////                    zone.map.yPhysicInTop(this.location.x, this.location.y - 24), -1));
////
////        }
////        if (Util.isTrue(5, 100)) { // tỉ lệ rơi item theo vip player
////            Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, detuvip[detubaselistvip], 1, this.location.x,
////                    zone.map.yPhysicInTop(this.location.x, this.location.y - 24), -1));
////
////        }
////    }
//    
//    
//    
//    
//    
//    @Override
//    public void joinMap() {
//        this.name = "Super Broly " + Util.nextInt(10, 500);
//        this.nPoint.hpMax = 50000000;
//        this.nPoint.hp = this.nPoint.hpMax;
//        this.nPoint.dame = this.nPoint.hpMax / 100;
//        this.nPoint.crit = Util.nextInt(50);
//        this.joinMap2();
//        st = System.currentTimeMillis();
//        this.notifyJoinMap();
//        PetService.gI().createbroly(this);
//    }
//
//    public void joinMap2() {
//        if (this.zone == null) {
//            if (this.parentBoss != null) {
//                this.zone = parentBoss.zone;
//            } else if (this.lastZone == null) {
//                this.zone = getMapJoin();
//            } else {
//                this.zone = this.lastZone;
//            }
//        }
//        if (this.zone != null) {
//            try {
//
//                int zoneid = Util.nextInt(2, this.zone.map.zones.size() - 1);
//
//                while (zoneid < this.zone.map.zones.size() && this.zone.map.zones.get(zoneid).getBosses().size() > 0) {
//                    zoneid++;
//                }
//
//                if (zoneid < this.zone.map.zones.size()) {
//                    this.zone = this.zone.map.zones.get(zoneid);
//                } else {
//                    if (this.id == BossID.BROLY) {
//                        this.changeStatus(BossStatus.DIE);
//                        return;
//                    }
//                    this.zone = this.zone.map.zones.get(Util.nextInt(2, this.zone.map.zones.size() - 1));
//                }
//
//                if (this.zone.zoneId < 2) {
//                    this.leaveMap();
//                }
//                ChangeMapService.gI().changeMap(this, this.zone, -1, -1);
//                this.changeStatus(BossStatus.CHAT_S);
//            } catch (Exception e) {
//                this.changeStatus(BossStatus.REST);
//            }
//        } else {
//            this.changeStatus(BossStatus.RESPAWN);
//        }
//        PetService.gI().createNormalPet(this);
//    }
//
//    private long st;
//
//    @Override
//    public void autoLeaveMap() {
//    }
//
//    @Override
//    public synchronized double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
//        if (!this.isDie()) {
//            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
//                 this.chat("Mày Ăn Bố Mày Đi - Nhóc Con");
//                return 0;
//            }
//            if (Util.isTrue(1, 30)) {
//                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, 6));
//                SkillService.gI().useSkill(this, null, null, -1, null);
//            }
//            damage = this.nPoint.subDameInjureWithDeff(damage);
//            if (!piercing && plAtt.playerSkill.skillSelect.template.id != Skill.TU_SAT
//                    
//                    && damage > this.nPoint.hpMax / 100) {
//                damage = this.nPoint.hpMax / 50;
//            }
//            this.nPoint.subHP(damage);
//            if (isDie()) {
//                this.setDie(plAtt);
//                die(plAtt);
//            }
//            return damage;
//        } else {
//            return 0;
//        }
//    }
//
//    private long lastTimeAttack;
//
//    @Override
//    public void attack() {
//        if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL) {
//            this.lastTimeAttack = System.currentTimeMillis();
//            try {
//                Player pl = getPlayerAttack();
//                if (pl == null || pl.isDie()) {
//                    return;
//                }
//                this.playerSkill.skillSelect = this.playerSkill.skills
//                        .get(Util.nextInt(7, this.playerSkill.skills.size() - 1));
//                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
//                    if (Util.isTrue(5, 20)) {
//                        if (SkillUtil.isUseSkillChuong(this)) {
//                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
//                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 70));
//                        } else {
//                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
//                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50));
//                        }
//                    }
//                    SkillService.gI().useSkill(this, pl, null, -1, null);
//                    checkPlayerDie(pl);
//                } else {
//                    if (Util.isTrue(1, 2)) {
//                        this.moveToPlayer(pl);
//                    }
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void leaveMap() {
//        ChangeMapService.gI().exitMap(this);
//        if (this.pet != null) {
//            ChangeMapService.gI().exitMap(this.pet);
//        }
//        this.lastZone = null;
//        this.lastTimeRest = System.currentTimeMillis();
//        this.changeStatus(BossStatus.REST);
//        BrolyManager.gI().removeBoss(this);
//        BossManager.gI().createBossBroly(BossID.BROLY, 100000);
//        this.dispose();
//    }
//}
