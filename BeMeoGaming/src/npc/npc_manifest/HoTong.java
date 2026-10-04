package npc.npc_manifest;

/**
 *
 *
 */
import boss.Boss;
import boss.BossManager;
import consts.ConstNpc;
import consts.ConstPlayer;
import item.Item;
import models.LeoThapNe.BossHoTong;
import npc.Npc;
import player.Player;
import services.ChatGlobalService;
import services.InventoryService;
import services.MapService;
import services.PlayerService;
import services.Service;
import services.func.ChangeMapService;
import shop.ShopService;
import utils.Util;

public class HoTong extends Npc {
     public String phamChatTieu;

    private static final int[] listMap = {5, 7, 8, 9, 10, 14, 15, 16, 17, 18, 19, 68, 69, 70, 
        71, 72, 73, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 73, 74, 75, 76, 77, 78,
        92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110};
    private int mapHoTong;
    private static final int[] phiHoTong = {1000, 10000, 30000, 50000};
    private static final String[] phamChatHoTong = {" 10k zenni ", " 10k Cỏ", " 30k Cỏ", " 50k Cỏ"};

    public HoTong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (this.mapId == 122) {
                 if (player.Saga_VIP < 99) {
                        Service.gI().sendThongBaoFromAdmin(player, "Đang Bảo Trì UPDATE");
                        return;
                    }
                mapHoTong = listMap[Util.nextInt(listMap.length - 1)];
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Ami Phọt Phọt\n"
                        + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                        + "|4|Thí Chủ Hãy Đưa Bần Tăng Đi Thích Kinh Nào !\n"
                        + "Đưa Ta Đến Tây Thiên Thỉnh Chân Kinh-Ami Phọt Phọt !\n"
                        + "Thu Gom Đủ Chân Kinh Để Đổi Lấy Sách Ép Trang Bị Nào\n"
                         + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                        + "|7|Thiện Tai -Thiện Tai- Ami Phọt Phọt",
                        "Lên Đường", "Shop\nNhư Lai", "Từ chối");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (player.iDMark.getIndexMenu()) {
                case ConstNpc.BASE_MENU -> {
                    switch (select) {
                        case 0 -> {
                            mapHoTong = listMap[Util.nextInt(listMap.length - 1)];
                            this.createOtherMenu(player, ConstNpc.MENU_HO_TONG, "Đưa Ta Đến Tây Thiên Thỉnh Chân Kinh-Ami Phọt Phọt !\n"
                                  + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                    + "\b|7|Hãy Đưa Ta Đi Xin Cơm Trước Nào : \n"
                                    + "|6|Hãy Di Chuyển Tới Map: " 
                                    + MapService.gI().getMapById(mapHoTong).mapName + " ?\n"
                                          + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                            + "Lưu Y: đi Free Thì Nhận Dc vp x10 - Mất Phí Nhận Về X3 Phí Bỏ Ra",
                                    "Đi Free", "Phí\n10k", "Phí\n30k",   "Phí\n50k",   "Từ chối");
                        }
                        case 1 -> {
                             ShopService.gI().opendShop(player, "CUAHANGHOTONG", false);
                        }
                    }
                }
                case ConstNpc.MENU_HO_TONG -> {
                    if (player.nPoint.power < 100000000000000000d) {
                            Service.gI().sendThongBaoFromAdmin(player, "Sức Mạnh Ko Đủ 100tr Tỷ Để Tham Gia\nMày Định Bug Clone Để Đi à\nMơ Đi Con ");
                            return;
                    }
                            
                    Item HoTong = InventoryService.gI().findItemBag(player, 1150);
                     Item HoTong1 = InventoryService.gI().findItemBag(player, 457);
                    switch (select) {
                        case 0 -> {
                            if (HoTong1 == null || HoTong1.quantity < phiHoTong[select]) {
                                this.npcChat(player, "Bạn không đủ Zenni!");
                            } else {
                                InventoryService.gI().subQuantityItemsBag(player, HoTong1, phiHoTong[select]);
                                InventoryService.gI().sendItemBag(player);
                                 PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_ALL);
                                Service.gI().changeFlag(player, player.cFlag);
                                Boss hoTong = null;
                                try {
                                    hoTong = new BossHoTong(Util.nextInt(100000, 1000000), player, phiHoTong[select], mapHoTong, phamChatHoTong[select]);
                                    hoTong.joinMap();
                                } catch (Exception e) {
                                }
                                ChangeMapService.gI().changeMap(hoTong, player.zone.map.mapId, player.zone.zoneId, player.location.x, player.location.y);
                                BossManager.gI().addBoss(hoTong);
                               // ChatGlobalService.gI().chatVanTieu(player, "Tao đang vận tiêu tại " + player.zone.map.mapName);
                            }
                        }
                        case 1 -> {
                            if (HoTong == null || HoTong.quantity < phiHoTong[select]) {
                                this.npcChat(player, "Bạn không đủ tiền phí!");
                            } else {
                                InventoryService.gI().subQuantityItemsBag(player, HoTong, phiHoTong[select]);
                                InventoryService.gI().sendItemBag(player);
                                 PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_ALL);
                                Service.gI().changeFlag(player, player.cFlag);
                                Boss hoTong = null;
                                try {
                                    hoTong = new BossHoTong(Util.nextInt(100000, 1000000), player, phiHoTong[select], mapHoTong, phamChatHoTong[select]);
                                    hoTong.joinMap();
                                } catch (Exception e) {
                                }
                                ChangeMapService.gI().changeMap(hoTong, player.zone.map.mapId, player.zone.zoneId, player.location.x, player.location.y);
                                BossManager.gI().addBoss(hoTong);
                             //   ChatGlobalService.gI().chatVanTieu(player, "Tao đang vận tiêu tại " + player.zone.map.mapName);
                            }
                        }
                        case 2 -> {
                            if (HoTong == null || HoTong.quantity < phiHoTong[select]) {
                                this.npcChat(player, "Bạn không đủ tiền phí!");
                            } else {
                                InventoryService.gI().subQuantityItemsBag(player, HoTong, phiHoTong[select]);
                                InventoryService.gI().sendItemBag(player);
                                 PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_ALL);
                                Service.gI().changeFlag(player, player.cFlag);
                                Boss hoTong = null;
                                try {
                                    hoTong = new BossHoTong(Util.nextInt(100000, 1000000), player, phiHoTong[select], mapHoTong, phamChatHoTong[select]);
                                    hoTong.joinMap();
                                } catch (Exception e) {
                                }
                                ChangeMapService.gI().changeMap(hoTong, player.zone.map.mapId, player.zone.zoneId, player.location.x, player.location.y);
                                BossManager.gI().addBoss(hoTong);
                           //     ChatGlobalService.gI().chatVanTieu(player, "Tao đang vận tiêu tại " + player.zone.map.mapName);
                            }
                        }
                        case 3 -> {
                            if (HoTong == null || HoTong.quantity < phiHoTong[select]) {
                                this.npcChat(player, "Bạn không đủ tiền phí!");
                            } else {
                                InventoryService.gI().subQuantityItemsBag(player, HoTong, phiHoTong[select]);
                                InventoryService.gI().sendItemBag(player);
                                  PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_ALL);
                                Service.gI().changeFlag(player, player.cFlag);
                                Boss hoTong = null;
                                try {
                                    hoTong = new BossHoTong(Util.nextInt(100000, 1000000), player, phiHoTong[select], mapHoTong, phamChatHoTong[select]);
                                    hoTong.joinMap();
                                } catch (Exception e) {
                                }
                                ChangeMapService.gI().changeMap(hoTong, player.zone.map.mapId, player.zone.zoneId, player.location.x, player.location.y);
                                BossManager.gI().addBoss(hoTong);
                             //   ChatGlobalService.gI().chatVanTieu(player, "Tao đang vận tiêu tại " + player.zone.map.mapName);
                            }
                        }
                    }
                }
            }
        }
    }
}
