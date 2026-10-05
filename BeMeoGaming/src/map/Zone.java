package map;

import consts.ConstTask;
import boss.Boss;
import boss.BossID;
import item.Item;
import mob.Mob;
import npc.Npc;
import npc.NpcManager;
import player.Player;
import network.Message;
import boss.boss_manifest.Training.TrainingBoss;
import com.mysql.cj.log.Log;
import consts.ConstMob;
import consts.ConstNpc;
import services.ItemMapService;
import services.ItemService;
import services.MapService;
import services.PlayerService;
import services.Service;
import services.TaskService;
import services.InventoryService;
import utils.FileIO;
import utils.Logger;
import utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Getter;
import lombok.Setter;
import models.Card.RadarService;
import npc.NonInteractiveNPC;
import player.Pet;
import power.CaptionManager;

public class Zone {

    public static final byte PLAYERS_TIEU_CHUAN_TRONG_MAP = 7;

    public int countItemAppeaerd = 0;
    public boolean isMapOffline;
    public Map map;

     public boolean isFullPlayer1() {
        return this.players.size() >= this.maxPlayer - 5;
    }
    public int zoneId;
    public int maxPlayer;
    public int shenronType = -1;

    private final List<Player> noninteractivenpcs; //npc
    private final List<Player> humanoids; //player, boss, pet
    private final List<Player> notBosses; //player, pet
    private final List<Player> players; //player
    private final List<Player> bosses; //boss
    private final List<Player> pets; //pet

    public final List<Mob> mobs;
    public final List<ItemMap> items;

    public long lastTimeDropBlackBall;
    public boolean finishBlackBallWar;
    public boolean finishMapMaBu;

    public boolean isbulon1Alive = true;
    public boolean isbulon2Alive = true;
    public boolean isTUTAlive = true;
    public boolean isGoldenFriezaAlive;

    public boolean isCompeting;
    public String rankName1;
    public String rankName2;
    public int rank1;
    public int rank2;

    public List<TrapMap> trapMaps;
    public List<MaBuHold> maBuHolds;

    @Setter
    @Getter
    public Player Npc;
    public byte effDragon;

    public boolean isFullPlayer() {
        return this.players.size() >= this.maxPlayer;
    }

    private void udMob() {
        for (int i = this.mobs.size() - 1; i >= 0; i--) {
            try {
                mobs.get(i).update();
            } catch (Exception e) {
                Logger.logException(Zone.class, e, "Lỗi update mobs");
            }
        }
    }

    private void udNonInteractiveNPC() {
        if (this.noninteractivenpcs.isEmpty()) {
            return;
        }
        try {
            for (int i = this.getNonInteractiveNPCs().size() - 1; i >= 0; i--) {
                if (i < this.getNonInteractiveNPCs().size()) {
                    Player pl = this.getNonInteractiveNPCs().get(i);
                    if (pl != null && pl.zone != null) {
                        pl.update();
                    }
                }
            }
        } catch (Exception e) {
            Logger.logException(Zone.class, e, "Lỗi update npcs");
        }
    }

    public void loadAnotherToMe(Player player) { //load những player trong map và gửi cho player vào map
        try {
            if (this.map.tileTypeAt(rank1, rank1, zoneId)) {
                for (int i = this.humanoids.size() - 1; i >= 0; i--) {
                    Player pl = this.humanoids.get(i);
                    if (pl != null) {
                        if (pl.id == -player.id) {
                            infoPlayer(player, pl);
                            break;
                        }
                    }
                }
            } else {
                for (int i = this.humanoids.size() - 1; i >= 0; i--) {
                    Player pl = this.humanoids.get(i);
                    if (pl != null) {
                        if (player != pl) {
                            infoPlayer(player, pl);
                            //      PlayerService.gI().sendPetFollow(player, pl);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //   Log.error(MapService.class, e);
        }
    }

    private void udItem() {
        if (this.items.isEmpty()) {
            return;
        }
        try {
            for (int i = this.items.size() - 1; i >= 0; i--) {
                try {
                    if (i < this.items.size()) {
                        ItemMap item = this.items.get(i);
                        if (item != null && item.itemTemplate != null) {
                            item.update();
                        } else {
                            items.remove(i);
                            System.err.println("Remove item " + i);
                        }
                    }
                } catch (Exception e) {
                    Logger.logException(Zone.class, e, "Lỗi item");
                }
            }
        } catch (Exception e) {
            Logger.logException(Zone.class, e, "Lỗi update items");
        }

    }

    public void update() {
        udMob();
        udItem();
        udNonInteractiveNPC();
    }

    public Zone(Map map, int zoneId, int maxPlayer) {
        this.map = map;
        this.zoneId = zoneId;
        this.maxPlayer = maxPlayer;
        this.noninteractivenpcs = new ArrayList<>();
        this.humanoids = new ArrayList<>();
        this.notBosses = new ArrayList<>();
        this.players = new ArrayList<>();
        this.bosses = new CopyOnWriteArrayList<>(); // khong CME khi duyet dong thoi boss add/remove
        this.pets = new ArrayList<>();
        this.mobs = new ArrayList<>();
        this.items = new ArrayList<>();
        this.trapMaps = new ArrayList<>();
        this.maBuHolds = new ArrayList<>();
    }

    public int getNumOfPlayers() {
        return this.players.size();
    }

    public int getNumOfBosses() {
        return this.bosses.size();
    }

    public boolean isBossCanJoin(Boss boss) {
        for (Player b : this.bosses) {
            if (b.id == boss.id) {
                return false;
            }
        }
        return true;
    }

    public List<Player> getNotBosses() {
        return this.notBosses;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public List<Player> getNonInteractiveNPCs() {
        return this.noninteractivenpcs;
    }

    public List<Player> getHumanoids() {
        return this.humanoids;
    }

    public List<Player> getBosses() {
        return this.bosses;
    }

    public void addPlayer(Player player) {
        if (player != null) {
            if (!this.humanoids.contains(player)) {
                this.humanoids.add(player);
            }

            if (player instanceof NonInteractiveNPC) {
                this.noninteractivenpcs.add(player);
            }

            if (!player.isBoss && !this.notBosses.contains(player) && !player.isNewPet && !(player instanceof NonInteractiveNPC)) {
                this.notBosses.add(player);
            }

            if (!player.isBoss && !player.isNewPet && !player.isPet && !this.players.contains(player) && !(player instanceof NonInteractiveNPC)) {
                this.players.add(player);
            }

            if (player.isBoss) {
                this.bosses.add(player);
            }
            if (player.isPet || player.isNewPet) {
                this.pets.add(player);
            }

        }
    }

    public void removePlayer(Player player) {
        this.noninteractivenpcs.remove(player);
        this.humanoids.remove(player);
        this.notBosses.remove(player);
        this.players.remove(player);
        this.bosses.remove(player);
        this.pets.remove(player);
    }

    public ItemMap getItemMapByItemMapId(int itemId) {
        for (ItemMap item : this.items) {
            if (item != null && item.itemMapId == itemId) {
                return item;
            }
        }
        return null;
    }

    public ItemMap getItemMapByTempId(int tempId) {
        for (ItemMap item : this.items) {
            if (item.itemTemplate.id == tempId) {
                return item;
            }
        }
        return null;
    }

    public List<ItemMap> getItemMapsForPlayer(Player player) {
        List<ItemMap> list = new ArrayList<>();
        for (ItemMap item : items) {
            if (item.itemTemplate.id == 78) {
                if (TaskService.gI().getIdTask(player) != ConstTask.TASK_3_1) {
                    continue;
                }
            }
            if (item.itemTemplate.id == 74) {
                if (TaskService.gI().getIdTask(player) < ConstTask.TASK_3_0) {
                    continue;
                }
            }
            if (item.itemTemplate.id == 726 && item.playerId != player.id) {
                continue;
            }
            list.add(item);
        }
        return list;
    }

    public Player getPlayerInMap(long idPlayer) {
        for (Player pl : humanoids) {
            if (pl != null && pl.id == idPlayer) {
                return pl;
            }
        }
        return null;
    }

    public Player getPlayerInMapOffline(Player player, long idPlayer) {
        for (Player pl : bosses) {
            if (pl.id == idPlayer && pl instanceof TrainingBoss && ((TrainingBoss) pl).playerAtt.equals(player)) {
                return pl;
            }
        }
        return null;
    }

    public void pickItem(Player player, int itemMapId, boolean ignoreRule) {
        ItemMap itemMap = getItemMapByItemMapId(itemMapId);
        if (itemMap != null && itemMap.itemTemplate != null) {
            if (itemMap.itemTemplate.type == 22) {
                return;
            }
            int playerId = Math.abs(itemMap.playerId > 100_000_000 ? 1_000_000_000 - (int) itemMap.playerId : (int) itemMap.playerId);
            if (playerId == player.id || itemMap.playerId == player.id || itemMap.playerId == -1) {
                Item item = ItemService.gI().createItemFromItemMap(itemMap);
                if (item.template.id == 648) {
                    if (!InventoryService.gI().findItemTatVoGiangSinh(player)) {
                        Service.gI().sendThongBao(player, "Cần thêm Tất,vớ giáng sinh");
                        return;
                    }
                }

                if (InventoryService.gI().addItemBag(player, item, 999999)) {
                    int itemType = item.template.type;
                    Message msg;
                    try {
                        msg = new Message(-20);
                        msg.writer().writeShort(itemMapId);
                        switch (itemType) {
                            case 9, 10, 34 -> {
                                msg.writer().writeUTF(item.quantity > Short.MAX_VALUE ? "Bạn vừa nhận được \n " + Util.FormatNumber(item.quantity) + " " + item.template.name : "");
                                PlayerService.gI().sendInfoHpMpMoney(player);
                            }
                            default -> {
                                switch (item.template.id) {
                                    case 73 ->
                                        msg.writer().writeUTF("");
                                    case 74 ->
                                        msg.writer().writeUTF("Bạn mới vừa ăn " + item.template.name);
                                    case 78 ->
                                        msg.writer().writeUTF("Wow, một cậu bé dễ thương!");
                                    default -> {
//                                        if (item.template.type >= 0 && item.template.type < 5) {
//                                            msg.writer().writeUTF(item.template.name + " ngon...");
//                                        } else {
                                        msg.writer().writeUTF("Bạn nhận được " + item.template.name);
//                                        }
                                        if (item.template.id == 648) {
                                            InventoryService.gI().subQuantityItemsBag(player, InventoryService.gI().findItemBag(player, 649), 1);
                                        }
                                        InventoryService.gI().sendItemBag(player);
                                    }
                                }
                            }

                        }
                        msg.writer().writeShort(item.quantity > Short.MAX_VALUE ? 9999 : item.quantity);
                        player.sendMessage(msg);
                        msg.cleanup();
                        Service.gI().sendToAntherMePickItem(player, itemMapId);
                        if (!(this.map.mapId >= 21 && this.map.mapId <= 23
                                && itemMap.itemTemplate != null && itemMap.itemTemplate.id == 74
                                || this.map.mapId >= 42 && this.map.mapId <= 44
                                && itemMap.itemTemplate != null && itemMap.itemTemplate.id == 78)) {
                            removeItemMap(itemMap);
                        }
                    } catch (Exception e) {
                        Logger.logException(Zone.class, e);
                    }
                } else {
                    if (!ItemMapService.gI().isBlackBall(item.template.id) && !ItemMapService.gI().isNamecBall(item.template.id) && !ItemMapService.gI().isNamecBallStone(item.template.id)) {
                        String text = "Hành trang không còn chỗ trống, không thể nhặt thêm";
                        Service.gI().sendThongBao(player, text);
                        return;
                    }
                }
//                if (!picked) {
//                    ItemMap itm = new ItemMap(itemMap);
//                    itm.x = player.location.x + Util.nextInt(-20, 20);
//                    itm.y = itm.zone.map.yPhysicInTop(itm.x, player.location.y);
//                    Service.gI().dropItemMap(player.zone, itm);
//                }
            } else {
                Service.gI().sendThongBao(player, "Không thể nhặt vật phẩm của người khác");
                return;
            }
            TaskService.gI().checkDoneTaskPickItem(player, itemMap);
            TaskService.gI().checkDoneSideTaskPickItem(player, itemMap);
            TaskService.gI().checkDoneClanTaskPickItem(player, itemMap);
        } else {
            Service.gI().sendThongBao(player, "Không thể thực hiện");
        }
    }

    public void addItem(ItemMap itemMap) {
        if (itemMap != null && !items.contains(itemMap)) {
            items.add(0, itemMap);
        }
    }

    public void removeItemMap(ItemMap itemMap) {
        this.items.remove(itemMap);
    }

    public Player getRandomPlayerInMap() {
        List<Player> plNotVoHinh = new ArrayList();

        //Lỗi
        for (Player pl : this.notBosses) {
            if (pl != null && (pl.effectSkin == null || !pl.effectSkin.isVoHinh) && pl.maBuHold == null && !pl.isMabuHold) {
                plNotVoHinh.add(pl);
            }
        }

        if (!plNotVoHinh.isEmpty()) {
            return plNotVoHinh.get(Util.nextInt(0, plNotVoHinh.size() - 1));
        }

        return null;
    }

    public Player PlayerPKinmap() {
        if (!this.humanoids.isEmpty()) {
            return this.humanoids.get(Util.nextInt(0, this.humanoids.size() - 1));
        } else {
            return null;
        }
    }

    public void load_Me_To_Another(Player player) { //load thông tin người chơi cho những người chơi khác
        try {
            if (player.zone != null) {
                if (MapService.gI().isMapOffline(this.map.mapId)) {
                    //Load boss
                    if (player instanceof TrainingBoss || player instanceof NonInteractiveNPC) {
                        for (int i = players.size() - 1; i >= 0; i--) {
                            Player pl = players.get(i);
                            if (!player.equals(pl) && (player instanceof NonInteractiveNPC
                                    || player instanceof TrainingBoss && ((TrainingBoss) player).playerAtt.equals(pl))) {
                                infoPlayer(pl, player);
                            }
                        }
                    }
                } else {
                    for (int i = players.size() - 1; i >= 0; i--) {
                        Player pl = players.get(i);
                        if (pl != null && !player.equals(pl)) {
                            infoPlayer(pl, player);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.logException(MapService.class, e);
        }
    }

    public void load_Another_To_Me(Player player) { //load những player trong map và gửi cho player vào map
        try {
            if (MapService.gI().isMapOffline(this.map.mapId)) {
                //Load boss
                for (int i = this.humanoids.size() - 1; i >= 0; i--) {
                    Player pl = this.humanoids.get(i);
                    if (pl != null && (pl instanceof NonInteractiveNPC
                            || pl instanceof TrainingBoss && ((TrainingBoss) pl).playerAtt.equals(player))) {
                        if (pl != null && pl.nPoint != null) {
                            infoPlayer(player, pl);
                        }
                    }
                }
            } else {
                for (int i = this.humanoids.size() - 1; i >= 0; i--) {
                    Player pl = this.humanoids.get(i);
                    if (pl != null && !player.equals(pl)) {
                        infoPlayer(player, pl);
                    }
                }
            }
        } catch (Exception e) {
            Logger.logException(MapService.class, e);
        }
    }

    private void infoPlayer(Player plReceive, Player plInfo) {
        if(plInfo.nPoint == null) {
            return;
        }
        Message msg;
        try {
            String name = "";
            msg = new Message(-5);
            msg.writer().writeInt((int) plInfo.id);
            if (plInfo.clan != null) {
                msg.writer().writeInt(plInfo.clan.id);
                name = "[" + plInfo.clan.name + "]" + plInfo.name;
            } else if (plInfo.isPet) {
                msg.writer().writeInt(-1);
                //  name = plInfo.name + "[Level " + ((Pet) plInfo).getLever() + "]";
            } else {
                msg.writer().writeInt(-1);
                name = plInfo.name;
            }
            msg.writer().writeUTF(plInfo.getTuVi());
            int level = CaptionManager.getInstance().getLevel(plInfo);
            msg.writer().writeByte(level);
            msg.writer().writeBoolean(false);
            msg.writer().writeByte(plInfo.typePk);
            msg.writer().writeByte(plInfo.gender);
            msg.writer().writeByte(plInfo.gender);
            msg.writer().writeShort(plInfo.getHead());
            String prefix = plInfo.Saga_VIP >= 1 && plInfo.Saga_VIP <= 13 ? "[VIP " + plInfo.Saga_VIP + "]" : "";
            msg.writer().writeUTF(prefix + plInfo.name);
            msg.writer().writeDouble(plInfo.nPoint.hp);
            msg.writer().writeDouble(plInfo.nPoint.hpMax);
            msg.writer().writeShort(plInfo.getBody());
            msg.writer().writeShort(plInfo.getLeg());
            msg.writer().writeByte(plInfo.getFlagBag()); //bag
            msg.writer().writeByte(-1);
            msg.writer().writeShort(plInfo.location.x);
            msg.writer().writeShort(plInfo.location.y);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0); //

            msg.writer().writeByte(0);

            //msg.writer().writeByte(plInfo.getUseSpaceShip());
            msg.writer().writeByte(plInfo.effectSkill.isMonkey ? 1 : 0);
            msg.writer().writeShort(plInfo.getMount());
            msg.writer().writeByte(plInfo.cFlag);
            msg.writer().writeByte(0);

//            if (!plInfo.isPet && !plInfo.isBoss && plInfo.isAdmin()) {
            msg.writer().writeShort(plInfo.getAura()); //idauraeff
            msg.writer().writeByte(-1); //seteff
//            }

            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Zone.class, e);

        }
        Service.getInstance().sendFlagPlayerToMe(plReceive, plInfo);
     
        try {
            if (plInfo.isDie()) {
                msg = new Message(-8);
                msg.writer().writeInt((int) plInfo.id);
                msg.writer().writeByte(0);
                msg.writer().writeShort(plInfo.location.x);
                msg.writer().writeShort(plInfo.location.y);
                plReceive.sendMessage(msg);
                msg.cleanup();
            }
        } catch (Exception e) {

        }
    }

    public void mapInfo(Player pl) {
        Message msg;
        long stGui24 = System.currentTimeMillis();
        try {
            msg = new Message(-24);
            msg.writer().writeByte(this.map.mapId);
            msg.writer().writeByte(this.map.planetId);
            msg.writer().writeByte(this.map.tileId);
            msg.writer().writeByte(this.map.bgId);
            msg.writer().writeByte(this.map.type);
            msg.writer().writeUTF(this.map.mapName);
            msg.writer().writeByte(this.zoneId);

            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);

            // waypoint
            try {
                List<WayPoint> wayPoints = this.map.wayPoints;
                msg.writer().writeByte(wayPoints.size());
                for (WayPoint wp : wayPoints) {
                    msg.writer().writeShort(wp.minX);
                    msg.writer().writeShort(wp.minY);
                    msg.writer().writeShort(wp.maxX);
                    msg.writer().writeShort(wp.maxY);
                    msg.writer().writeBoolean(wp.isEnter);
                    msg.writer().writeBoolean(wp.isOffline);
                    msg.writer().writeUTF(wp.name);
                }
            } catch (Exception e) {
                msg.writer().writeByte(0);
            }

            // mob
            try {
                List<Mob> mobs = new ArrayList<>();
                for (Mob mob : this.mobs) {
                    if (mob.isBigBoss() && mob.tempId != 70 && mob.isDie()) {
                        continue;
                    }
                    mobs.add(mob);
                }
                msg.writer().writeByte(mobs.size());
                for (Mob mob : mobs) {
                    msg.writer().writeBoolean(false); //is disable
                    msg.writer().writeBoolean(false); //is dont move
                    msg.writer().writeBoolean(false); //is fire
                    msg.writer().writeBoolean(false); //is ice
                    msg.writer().writeBoolean(false); //is wind
                    msg.writer().writeByte(mob.tempId);
                    msg.writer().writeByte(0); // sys
                    if (pl.isClientDoubleMessage()) {
                        msg.writer().writeDouble(mob.point.gethp());
                        msg.writer().writeByte(mob.level);
                        msg.writer().writeDouble(mob.point.getHpFull());
                    } else {
                        msg.writer().writeDouble(mob.point.gethp());
                        msg.writer().writeByte(mob.level);
                        msg.writer().writeDouble(mob.point.getHpFull());
                    }
                    msg.writer().writeShort(mob.location.x);
                    msg.writer().writeShort(mob.location.y);
                    msg.writer().writeByte(mob.status);
                    msg.writer().writeByte(mob.lvMob);
                    msg.writer().writeBoolean(mob.tempId == ConstMob.GAU_TUONG_CUOP || mob.tempId >= ConstMob.VOI_CHIN_NGA && mob.tempId <= ConstMob.PIANO); //is bigboss
                }
            } catch (Exception e) {
                msg.writer().writeByte(0);
            }

            msg.writer().writeByte(0);

            // npc
            try {
                List<Npc> npcs = NpcManager.getNpcsByMapPlayer(pl);
                msg.writer().writeByte(npcs.size());
                for (Npc npc : npcs) {
                    msg.writer().writeByte(npc.status);
                    msg.writer().writeShort(npc.cx);
                    msg.writer().writeShort(npc.cy);
                    msg.writer().writeByte(npc.tempId);
                    msg.writer().writeShort(npc.avartar);
                }
            } catch (Exception e) {
                msg.writer().writeByte(0);
            }

            // item
            try {
                List<ItemMap> itemsMap = this.getItemMapsForPlayer(pl);
                msg.writer().writeByte(itemsMap.size());
                for (ItemMap it : itemsMap) {
                    msg.writer().writeShort(it.itemMapId);
                    msg.writer().writeShort(it.itemTemplate.id);
                    msg.writer().writeShort(it.x);
                    msg.writer().writeShort(it.y);
                    msg.writer().writeInt((int) it.playerId);
                }
            } catch (Exception e) {
                msg.writer().writeByte(0);
            }

            // bg item
            try {
                final byte[] bgItem = FileIO.readFile("data/map/item_bg_map_data/" + this.map.mapId);
                msg.writer().write(bgItem);
            } catch (Exception e) {
                msg.writer().writeShort(0);
            }

            // eff item
            try {
                final byte[] effItem = FileIO.readFile("data/map/eff_map/" + this.map.mapId);
                msg.writer().write(effItem);
            } catch (Exception e) {
                msg.writer().writeShort(0);
            }

            msg.writer().writeByte(this.map.bgType);
            msg.writer().writeByte(pl.iDMark.getIdSpaceShip());
            msg.writer().writeByte(this.map.mapId == 148 ? 1 : 0);
            pl.sendMessage(msg);
            // DO THOI GIAN: xay goi -24 + enqueue cho client (nao client nhan duoc va load xong la chung ta do duoc)
            Logger.log("[MAP] gui -24 map=" + this.map.mapId + " zone=" + this.zoneId + " cho " + pl.name
                    + " mat " + (System.currentTimeMillis() - stGui24) + "ms, queue dang cho gui="
                    + (pl.getSession() != null ? pl.getSession().getNumMessages() : -1) + "\n");

            msg.cleanup();

        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public TrapMap isInTrap(Player player) {
        for (TrapMap trap : this.trapMaps) {
            if (player.location.x >= trap.x && player.location.x <= trap.x + trap.w
                    && player.location.y >= trap.y && player.location.y <= trap.y + trap.h) {
                return trap;
            }
        }
        return null;
    }

    public void sendBigBoss(Player player) {
        for (Mob mob : this.mobs) {
            if (!mob.isDie() && mob.tempId == ConstMob.HIRUDEGARN) {
                if (mob.lvMob >= 1) {
                    Service.gI().sendBigBoss2(player, 6, mob);
                }
                if (mob.lvMob >= 2) {
                    Service.gI().sendBigBoss2(player, 5, mob);
                }
                break;
            }
        }
    }

    public MaBuHold getMaBuHold() {
        for (MaBuHold hold : MapService.gI().getMapById(128).zones.get(this.zoneId).maBuHolds) {
            if (hold.player == null) {
                return hold;
            }
        }
        return null;
    }

    public void setMaBuHold(int slot, int zoneId, Player player) {
        MapService.gI().getMapById(128).zones.get(zoneId).maBuHolds.set(slot, new MaBuHold(slot, player));
    }

    public Player findPlayerByID(long id) {
        for (Player p : this.players) {
            if (p.id == id) {
                return p;
            }
        }
        return null;
    }
}
