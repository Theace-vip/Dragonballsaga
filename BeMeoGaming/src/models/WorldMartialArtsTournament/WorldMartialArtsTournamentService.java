package models.WorldMartialArtsTournament;

import consts.ConstNpc;
import consts.ConstTournament;
import java.util.ArrayList;
import npc.Npc;
import player.Player;
import services.NpcService;
import services.Service;
import services.func.ChangeMapService;
import utils.TimeUtil;

public class WorldMartialArtsTournamentService extends ConstTournament {

    public static int getTournament() {
        int hours = TimeUtil.getCurrHour();
        switch (hours) {
            case 8, 14, 18 -> {
                return NHI_DONG;
            }
            case 9, 13, 19 -> {
                return SIEU_CAP_1;
            }
            case 10, 15, 20 -> {
                return SIEU_CAP_2;
            }
            case 11, 16, 21 -> {
                return SIEU_CAP_3;
            }
            case 12, 17, 22, 23 -> {
                return NGOAI_HANG;
            }
            default -> {
                return -1;
            }
        }
    }

    public static int getNextTournamentTime() {
        int hours = TimeUtil.getCurrHour() + 1;
        if (hours > 23 || hours < 8) {
            hours = 8;
        }
        return hours;
    }

    public static String sayText() {
        return WorldMartialArtsTournamentManager.gI().canReg
                ? "|7|Chào mừng bạn đến với đại hội võ thuật\nGiải " + tournamentNames[getTournament()] + " đang có " + WorldMartialArtsTournamentManager.gI().listReg.size() + " người đăng ký thi đấu\n"
                + "|7|Hãy Tham Gia PVP Giai Đấu Nhận Phần quà Cực Vip\n"
                + "|4|Chi Phí Cực Rẻ: 500k Điểm Fam \n"
                   + "Phần quà Là Đá Gỡ SPL Có Thể Giao dịch\n"
                + "WIN Vòng 1 Nhận : 10.000 Đá Gỡ SLP\n"
                + "WIN Vòng 2 Nhận : 20.000 Đá Gỡ SPL\n"
                + "WIN Vòng 3 Nhận : 30.000 Đá Gỡ SPL\n"
                : "Đã hết hạn đăng ký thi đấu, xin vui lòng chờ đến giải sau vào lúc\n"
                + "|7|Hãy Tham Gia PVP Giai Đấu Nhận Phần quà Cực Vip\n"
                  + "|4|Chi Phí Cực Rẻ: 500k Điểm Fam\n"
                  + "Phần quà Là Đá Gỡ SPL Có Thể Giao dịch\n"
                + "WIN Vòng 1 Nhận : 10.000 Đá Gỡ SLP\n"
                + "WIN Vòng 2 Nhận : 20.000 Đá Gỡ SPL\n"
                + "WIN Vòng 3 Nhận : 30.000 Đá Gỡ SPL\n"
               ;
    }

    public static void menu(Npc npc, Player player) {
        if (WorldMartialArtsTournamentManager.gI().round != 0 && WorldMartialArtsTournamentManager.gI().checkPlayer(player.id)) {
            NpcService.gI().createTutorial(player, npc.tempId, npc.avartar, "Bạn được vào vòng " + (WorldMartialArtsTournamentManager.gI().round + 1) + "\nTrận tiếp theo sắp diễn ra, hãy đợi tại đây");
            return;
        }
        boolean canReg = WorldMartialArtsTournamentManager.gI().canReg;
        int tour = getTournament();
        ArrayList<String> menu = new ArrayList<>();
        menu.add("Thông tin\nChi tiết");
        if (canReg && tour != -1) {
            if (!regCheck(player)) {
                menu.add("Đăng kí");
            } else {
                menu.add("Hủy\nđăng kí");
            }
            menu.add("Giải\nSiêu Hạng");
            menu.add("Đại Hội\nVõ Thuật\nLần thứ\n23");
        } else {
            menu.add("Giải\nSiêu Hạng");
            menu.add("Đại Hội\nVõ Thuật\nLần thứ\n23");
            menu.add("Đóng");
        }
        npc.createOtherMenu(player, ConstNpc.BASE_MENU, sayText(), menu.toArray(String[]::new));
    }

    public static void confirm(Npc npc, Player player, int select) {
        boolean canReg = WorldMartialArtsTournamentManager.gI().canReg;
        int tour = getTournament();

        switch (player.iDMark.getIndexMenu()) {
            case ConstNpc.DANGKYDHVT_CONFIRM -> {
                if (select == 0 && canReg && tour != -1) {
                    dangky_huy(npc, player);
                }
            }
            case ConstNpc.BASE_MENU -> {
                switch (select) {
                    case 0 ->
                        NpcService.gI().createTutorial(player, npc.tempId, npc.avartar, ConstNpc.THONG_TIN_DAI_HOI_VO_THUAT);
                    case 1 -> {
                        if (canReg && tour != -1) {
                            if (!regCheck(player)) {
                                ArrayList<String> menu = new ArrayList<>();
                                if (tour == NGOAI_HANG) {
                                    menu.add("Giải\n" + tournamentNames[tour] + "\n(" + tournamentGolds[tour] + "k Điểm Fam)");
                                } else {
                                    menu.add("Giải\n" + tournamentNames[tour] + "\n(" + tournamentGems[tour] + "k Điểm Fam)");
                                }
                                menu.add("Từ chối");
                                
                                if (player.getSession().tongnap < 500000) {
                                    Service.gI().sendThongBaoFromAdmin(player, "|4|Tối Thiểu Nạp 50k Để Thi Đấu\nTránh Clone Bug Đá");
                                    return;
                                }
                                npc.createOtherMenu(player, ConstNpc.DANGKYDHVT_CONFIRM, "Hiện đang có giải đấu " + tournamentNames[tour] + " bạn có muốn đăng ký không?\n"
                                        + "|7|Hãy Tham Gia PVP Giai Đấu Nhận Phần quà Cực Vip\n"
                                         + "|4|Chi Phí Cực Rẻ: 500k Điểm Fam\n"
                                        + "Phần quà Là Đá Gỡ SPL Có Thể Giao dịch\n"
                                        + "WIN Vòng 1 Nhận : 10.000 Đá Gỡ SLP\n"
                                        + "WIN Vòng 2 Nhận : 20.000 Đá Gỡ SPL\n"
                                        + "WIN Vòng 3 Nhận : 30.000 Đá Gỡ SPL\n",
                                        menu.toArray(String[]::new));
                            } else {
                                dangky_huy(npc, player);
                            }
                            break;
                        }
                        ChangeMapService.gI().changeMapNonSpaceship(player, 113, player.location.x, 360);
                    }
                    case 2 -> {
                        if (canReg) {
                            ChangeMapService.gI().changeMapNonSpaceship(player, 113, player.location.x, 360);
                            break;
                        }
                        ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                    }
                    case 3 -> {
                        if (canReg) {
                            ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                            break;
                        }
                    }
                }
            }
        }
    }

    public static boolean regCheck(Player player) {
        return WorldMartialArtsTournamentManager.gI().listReg.contains(player.id);
    }

    public static void dangky_huy(Npc npc, Player player) {
        if (WorldMartialArtsTournamentManager.gI().listChamp.contains(player.name)) {
            NpcService.gI().createTutorial(player, npc.tempId, npc.avartar, TEXT_DA_VO_DICH);
            return;
        }
        if (!WorldMartialArtsTournamentManager.gI().listReg.contains(player.id)) {
            int tour = getTournament();
         
            int gem = 500000;
            if (player.diemfam < gem) {
                NpcService.gI().createTutorial(player, npc.tempId, npc.avartar, "Bạn không đủ điểm fam, còn thiếu " + (gem - player.diemfam) + " điểm nữa");
                return;
            }
            player.diemfam -= gem;
            Service.gI().sendMoney(player);
            WorldMartialArtsTournamentManager.gI().listReg.add(player.id);
            NpcService.gI().createTutorial(player, npc.tempId, npc.avartar, ConstTournament.TEXT_DANG_KY_THANH_CONG.replaceAll("%1", TimeUtil.getCurrHour() + "").replaceAll("%2", TimeUtil.getCurrHour() + "h" + TimeUtil.getCurrMin()));
        } else {
            WorldMartialArtsTournamentManager.gI().listReg.remove(player.id);
            NpcService.gI().createTutorial(player, npc.tempId, npc.avartar, ConstTournament.TEXT_HUY_DANG_KY);
        }
    }
}
