package npc.npc_manifest;

import clan.Clan;
import consts.ConstNpc;
import consts.ConstPlayer;
import dragon.Functions;

import java.util.ArrayList;

import npc.Npc;
import player.Player;
import services.ClanService;
import services.NpcService;
import services.Service;
import services.TaskService;
import services.func.ChangeMapService;
import services.func.Input;
import utils.Util;

public class DrDrief extends Npc {

    public DrDrief(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player pl) {
        if (canOpenNpc(pl)) {
            if (this.mapId == 84) {
                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                        "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                        pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất" : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda");
            } else if (this.mapId == 153) {

                ArrayList<String> menu = new ArrayList<>();
                Clan clan = pl.clan;
                if (clan != null && clan.isLeader(pl)) {
                    menu.add("Chức năng\nbang hội");
                }
                menu.add("Đảo Kame");
                menu.add("Từ chối");
                String[] menus = menu.toArray(String[]::new);

                this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Tôi có thể giúp gì cho bang hội của bạn ?", menus);
            } else if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                        "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                        "Đến\nNamếc", "Đến\nXayda", "Đến\nSiêu thị");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 84) {
                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
            } else if (this.mapId == 153) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.BASE_MENU -> {
                        Clan clan = player.clan;
                        if (clan != null && clan.isLeader(player)) {
                            switch (select) {
                                case 0 ->
                                    createOtherMenu(player, 1, "Tôi có thể giúp gì cho bang hội của bạn ?", "Đổi tên\ntên bang\nviết tắt", "Chọn ngẫu nhiên tên bang viết tắt", "Nâng cấp Bang hội", "Đóng");
                                case 1 ->
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, -1);
                                default -> {
                                }
                            }
                        } else if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, -1);
                        }
                    }
                    case 1 -> {
                        Clan clan;
                        switch (select) {
                            case 0 ->
                                Input.gI().createFormBangHoi(player);
                            case 1 -> {
                                clan = player.clan;
                                if (clan != null) {
                                    if (clan.isLeader(player)) {
                                        if (clan.canUpdateClan(player)) {
                                            String tenvt = Functions.generateRandomCharacters(Util.nextInt(2, 4));
                                            clan.name2 = tenvt;
                                            clan.update();
                                            Service.gI().sendThongBao(player, "[" + tenvt + "] OK");
                                        }
                                    }
                                }
                            }
                            case 2 -> {
                                clan = player.clan;
                                if (clan != null && clan.isLeader(player)) {
                                    if (clan.level >= Clan.LEVEL_MAX) {
                                        Service.gI().sendThongBao(player, "Bang hội đã đạt cấp tối đa (" + Clan.LEVEL_MAX + ").");
                                        return;
                                    }
                                    String npcSay = "Cần " + Util.chiaNho(ClanService.gI().capsuleCost(clan.level)) + " capsule bang [đang có " + Util.chiaNho(clan.capsuleClan) + " capsule bang] để nâng cấp bang hội lên cấp " + (clan.level + 1);
                                    npcSay += "\n+1 tối đa số lượng thành viên";
                                    npcSay += "\n+1 ô trống tối đa rương bang.";
                                    npcSay += "\n+Mở bán bùa bang cấp " + (clan.level + 1);
                                    npcSay += "\nMỗi cấp bang tăng 200% sức đánh cho toàn bang.";
                                    createOtherMenu(player, ConstNpc.MENU_CLAN_UP, npcSay, "Đồng ý", "Từ chối");
                                }
                            }
                            default -> {
                            }
                        }
                    }
                    case ConstNpc.MENU_CLAN_UP -> {
                        if (select == 0) {
                            ClanService.gI().upgradeClan(player);
                        }
                    }
                    default -> {
                    }
                }
            } else if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0 ->
                        ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                    case 1 ->
                        ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                    case 2 ->
                        ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                }
            }
        }
    }
}
