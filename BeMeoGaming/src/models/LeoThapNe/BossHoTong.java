package models.LeoThapNe;

import boss.Boss;
import boss.BossData;
import boss.BossManager;
import consts.ConstPlayer;
import item.Item;
import item.Item.ItemOption;
import map.ItemMap;
import player.Player;
import services.ChatGlobalService;
import services.InventoryService;
import services.ItemService;
import services.MapService;
import services.PlayerService;
import services.Service;
import services.TaskService;
import services.func.ChangeMapService;
import skill.Skill;
import utils.Util;

/**
 *
 * @author ADMIN
 */
public class BossHoTong extends Boss {

    public int mapHoTong;
    public Player plFollow;
    public int phiHoTong;
    public long lastTimeChat;
    public String phamChatTieu;

    public BossHoTong(int id, Player plFollow, int phiHoTong, int mapHoTong, String phamChatTieu) throws Exception {
        super(id, new BossData(
                "Đường Tank: " + plFollow.name,
                ConstPlayer.XAYDA, //gender
                new short[]{467,	468,	469, 160, -1, -1}, //outfit
                1, //dame
                new double[]{9000000000L}, //hp
                new int[]{122}, //map join
                new int[][]{ //skill
                    {Skill.KAMEJOKO, 1, 10}, {Skill.LIEN_HOAN, 2, 20}, {Skill.MASENKO, 7, 20}, {Skill.TAI_TAO_NANG_LUONG, 7, 90000}, {Skill.TAI_TAO_NANG_LUONG, 5, 50000}, {Skill.DE_TRUNG, 7, 20000},
                    {Skill.ANTOMIC, 1, 70}},
                new String[]{},
                new String[]{},
                new String[]{}, 0));
        this.currentLevel = 0;
        this.plFollow = plFollow;
        this.zoneFinal = plFollow.zone;
        this.phiHoTong = phiHoTong;
        this.mapHoTong = mapHoTong;
        this.phamChatTieu = phamChatTieu;
        this.lastTimeChangeMap = System.currentTimeMillis();
        this.lastTimeChat = System.currentTimeMillis();
    }

    public String getMap() {
        try {
            return MapService.gI().getMapById(mapHoTong).mapName;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void reward(Player pl) {
        if (Util.isTrue(10, 100)) {
            int[] ramdomroiitem = new int[]{1326};//list item
            ItemMap itemMap = new ItemMap(zone, ramdomroiitem[Util.nextInt(ramdomroiitem.length - 1)], 1, this.location.x,
                    zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            itemMap.options.add(new ItemOption(30, 1));
            Service.getInstance().dropItemMap(this.zone, itemMap);
        }
        this.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void attack() {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.NON_PK);
        this.followPlayer();
        TaskService.gI().sendTaskMain(plFollow);
    }

    @Override
    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(20, 30);
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y);
    }

    public void followPlayer() {
        if (plFollow == null || plFollow.zone == null || plFollow.isDie() || this.isDie()) {
            this.leaveMap();
            BossManager.gI().removeBoss(this);
            return;
        }
        this.moveTo(this.plFollow.location.x, this.plFollow.location.y);
        if (Util.canDoWithTime(lastTimeChat, 10000)) {
//            ChatGlobalService.gI().chatVanTieu(this, "Hộ Tống Trị giá (" + phamChatTieu
//                    + ") tại " + this.zone.map.mapName);
            Service.gI().sendThongBaoAllPlayer("|7|[" + plFollow.name + " ]\n|6|Tại Map " + plFollow.zone.map.mapName + " \nĐang Hộ Tống\nPhí " +   phamChatTieu + "\nPhần quà Hot\n Cướp Nào");
            lastTimeChat = System.currentTimeMillis();
        }
        if (Util.canDoWithTime(lastTimeChangeMap, 1200000)) {
            super.leaveMap();
            BossManager.gI().removeBoss(this);
        }
        if (this.zone != null && !this.zone.equals(plFollow.zone)) {
            ChangeMapService.gI().changeMap(this, plFollow.zone, plFollow.location.x, plFollow.location.y);
        }
        if (this.cFlag != plFollow.cFlag) {
            Service.gI().changeFlag(this, plFollow.cFlag);
        }
     //   System.err.println("MapId:" + this.zone.map.mapId);
        if (this.zone.map.mapId == this.mapHoTong) {
         //   System.err.println("MapId:" + this.zone.map.mapId);
            int[] listReward = {543,457,457,457,457};
            Item item = ItemService.gI().createNewItem((short) listReward[Util.nextInt(listReward.length - 1)], phiHoTong * Util.nextInt(1, 2));
            item.itemOptions.add(new ItemOption(30, 1));
            Service.gI().sendThongBaoFromAdmin(plFollow, "Mày Đã Hộ Tống thành công!\nBạn nhận được x" + item.quantity + " " + item.template.name);
            InventoryService.gI().addItemBag(plFollow, item, 999999);
            InventoryService.gI().sendItemBag(plFollow);
            this.leaveMap();
            BossManager.gI().removeBoss(this);
        }
    }
}
