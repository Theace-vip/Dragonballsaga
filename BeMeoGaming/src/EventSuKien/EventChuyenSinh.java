package EventSuKien;

/**
 *
 */
import consts.ConstNpc;
import item.Item;
import npc.Npc;
import static npc.NpcFactory.MenuBH;
import static npc.NpcFactory.MenuDiaDao;
import player.Player;
import services.InventoryService;
import services.NpcService;
import services.Service;
import utils.Util;

public class EventChuyenSinh extends Npc {

    public EventChuyenSinh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }
    

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            Item luongBac = InventoryService.gI().findItemBag(player, (short) 1271);
            int soLuongLuongBac = (luongBac != null) ? luongBac.quantity : 0;
            if (this.mapId == 5) {
                createOtherMenu(player,
                        ConstNpc.BASE_MENU,
                        "|7|CHUYỂN SINH"
                        + "\n|1|Yêu cầu Sức mạnh đạt " + Util.FormatNumber(panel.tuning.SystemTuning.get("cs_sm_yeucau"))
                        + "\nTu Tiên Đạt : Trúc Cơ Đỉnh Phong "
                        + "\nTu Vi hiện tại: " + player.TamkjllTuviTutien(Util.maxInt(player.SagaTuTien[1]))
                        + "\n\n|2|Sau khi chuyển sinh Thành công: + 200% HP,KI,SD"
                        + "\n-Sức mạnh Reset Về 0  "
                        + "\n-Cấp chuyển sinh tăng 1 Lần: Hiện Tại : " + player.SagaChuyenSinh + " Lần"
                        + "\n\n|1|Sức Mạnh: " + Util.FormatNumber(player.nPoint.power) + "/" + " 150k Tỉ Tỉ"
                        + "\n|1|Lượng Bạc " + soLuongLuongBac + " /" + Util.FormatNumber(panel.tuning.SystemTuning.get("cs_luong_bac")) + " Lượng Bạc"
                        + "\n|3|=>Mỗi cấp chuyển sinh sẽ cộng " + Util.FormatNumber(panel.tuning.SystemTuning.get("cs_hp_goc")) + " HP,KI Gốc + " + Util.FormatNumber(panel.tuning.SystemTuning.get("cs_sd_goc")) + " Sức Đánh Gốc"
                        + "\n|7|Cs Thất bại sẽ Giảm Về 0 Sức mạnh\n"
                        + "|4|Tỉ Lệ Thành Công " + panel.tuning.SystemTuning.getI("cs_tile_thanh_cong") + "%",
                        "Chuyển Sinh",
                        "Tính Năng\nThiên Đạo",
                        "Tính Năng\nĐịa Đạo",
                        //  "Tính Năng\nThợ Mỏ",
                        "Tính năng\nTu Tiên");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 5) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            NpcService.gI().createMenuConMeo(player, ConstNpc.SagaChuyenSinh, -1,
                                    "|7|Bạn Định Làm Gì ?ღ\n"
                                    + "|4|Chuyển Sinh : " + Util.cap(player.SagaChuyenSinh) + " Lầnღ\n",
                                    //   + "|4|Chuyển Sinh Pet : " + Util.cap(player.pet.SagaChuyenSinh) + " Lầnღ\n",
                                    "Chuyển Sinh\nSư Phụ", "Chuyển Sinh\nĐệ Tử",
                                    "Đóng");

                            break;
                        case 1:
                            String hieuung = "Hành tinh: " + Service.gI().get_HanhTinh(player.gender);
                            MenuBH(player, hieuung);
                            break;
                        case 2:
                            String hieuungNe = "Hành tinh: " + Service.gI().get_HanhTinh(player.gender);
                            MenuDiaDao(player, hieuungNe);
                            break;
//                        case 3: {
//                            int soThoDao = player.TamkjllThomo;
//                            int buffVang = soThoDao * 5000;
//                            int buffNgoc = soThoDao * 10;
//                            int buffRuby = soThoDao * 1;
//                            int buffCo4La = soThoDao * 0;
//                            int tileHiemSPL = 3;
//                            int tileTheCaoPct = 1;
//                            final int BAR_W = 24;
//
//                            java.util.function.Function<Double, String> makeBar = (p) -> {
//                                double x = Math.max(0, Math.min(100, p));
//                                int filled = (int) Math.round(x / 100.0 * BAR_W);
//                                StringBuilder sb = new StringBuilder("[");
//                                for (int i = 0; i < BAR_W; i++) {
//                                    sb.append(i < filled ? '█' : '░');
//                                }
//                                sb.append("]");
//                                return sb.toString();
//                            };
//
//                            String barVang = makeBar.apply((double) Math.min(100, buffVang));
//                            String barNgoc = makeBar.apply((double) Math.min(100, buffNgoc));
//                            String barRuby = makeBar.apply((double) Math.min(100, buffRuby));
//                            String barCo4La = makeBar.apply((double) Math.min(100, buffCo4La));
//                            String barSPL = makeBar.apply((double) Math.min(100, tileHiemSPL));
//                            String barTheCao = makeBar.apply((double) Math.min(100, tileTheCaoPct));
//                            NpcService.gI().createMenuConMeo(player, ConstNpc.ThoMo, -1,
//                                    "|2|✦✦✦  HỆ THỐNG ĐÀO MỎ  ✦✦✦\n"
//                                    + "|7|────────────────────────────────\n"
//                                    + "|5|👷 Số Thợ Đang Đào: " + soThoDao + " người\n"
//                                    + "|6|✦ Buff Vàng: " + Util.getFormatNumber(buffVang) + " Vàng\n"
//                                    + "|6|✦ Buff Ngọc: " + Util.getFormatNumber(buffNgoc) + " Ngọc\n"
//                                    + "|6|✦ Buff Ruby: " + Util.getFormatNumber(buffRuby) + " Ruby\n"
//                                   // + "|6|✦ Buff Cỏ 4 Lá : " + Util.getFormatNumber(buffCo4La) + " Cỏ\n"
//                                //    + "|7|────────────────────────────────\n"
//                                   // + "|3|Tỉ Lệ Hiếm SPL : " + barSPL + (tileHiemSPL > 100 ? "" : "") + "\n"
//                                   // + "|4|Tỉ Lệ Thẻ Cào  : " + barTheCao + (tileTheCaoPct > 100 ? "" : "") + "\n"
//                                    + "|7|────────────────────────────────\n",
//                                    "Tới Map\nĐào Mỏ"
//                            );
//                            break;
//                        }

                        case 3: {
                            final short[] IDThanKhi = {1759, 1760, 1761, 1762, 1763, 1854};
                            // Ti le cong cua Phap Chi Do Kiep - sua o Panel > He Thong (nhom Tu Tien)
                            final int[] Ti_Le_Than_Khi = {
                                panel.tuning.SystemTuning.getI("tutien_phap_chi_1"),
                                panel.tuning.SystemTuning.getI("tutien_phap_chi_2"),
                                panel.tuning.SystemTuning.getI("tutien_phap_chi_3"),
                                panel.tuning.SystemTuning.getI("tutien_phap_chi_4"),
                                panel.tuning.SystemTuning.getI("tutien_phap_chi_5"),
                                panel.tuning.SystemTuning.getI("tutien_phap_chi_6")
                            };
                            double Ti_Le_Goc = player.bemeocanhgioi(Util.bemeo(player.SagaTuTien[1]));

                            int bestBonus = 0;
                            for (int i = 0; i < IDThanKhi.length; i++) {
                                Item it = InventoryService.gI().findItemBag(player, IDThanKhi[i]);
                                if (it != null && it.quantity > 0) {
                                    if (Ti_Le_Than_Khi[i] > bestBonus) {
                                        bestBonus = Ti_Le_Than_Khi[i];
                                    }
                                }
                            }
                            double Ti_Le_Chung = Ti_Le_Goc + bestBonus;
                            if (Ti_Le_Chung > 100) {
                                Ti_Le_Chung = 100;
                            }

                            // Thanh 20 ô theo tileFinal
                            int filled = (int) ((Ti_Le_Chung / 100.0) * 20);
                            StringBuilder bar = new StringBuilder("[");
                            for (int i = 0; i < 20; i++) {
                                bar.append(i < filled ? '█' : '░');
                            }
                            bar.append("]");

                            NpcService.gI().createMenuConMeo(player, ConstNpc.TuTienNPC, -1,
                                    "|7|Tu Tiên Đỉnh Cao Thế giới\n"
                                    + "|4|Thọ Nguyên Còn\n"
                                    + "|7|[ " + Util.formatTime(player.tho_nguyen) + " ]\n"
                                    + "|6|Cảnh giới Hiện\n"
                                    + "|7|[ " + player.TamkjllTuviTutien(Util.maxInt(player.SagaTuTien[1])) + " ]\n"
                                    + "|6|Thiên Phú Linh Căn\n"
                                    + "|7|[" + player.linhcan(Util.maxInt(player.SagaTuTien[2])) + " ]\n"
                                    + "|6|Linh Khí Tu Luyện\n"
                                    + "|7|[ " + Util.getFormatNumber(player.SagaTuTien[0]) + " ]\n"
                                            + "|6|[ Auto Nhận Linh Khí: " + Util.getFormatNumber(panel.tuning.SystemTuning.get("tutien_linh_khi_moi_giay")) + "/S]\n"
                                    + "|4|Tỉ Lệ Độ Kiếp : " + bar + " : " + (int) Ti_Le_Chung + "%\n"
                                    + (bestBonus > 0 ? "|7|(Gốc: " + (int) Ti_Le_Goc + "% + Pháp Chỉ: " + bestBonus + "%)\n" : "")
                                    + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                    + "|4|SB Thu Thập Pháp Chỉ Độ Kiếp Tăng Tỉ Lệ Đột Phá\n"
                                    + "|7|Loại Pháp Chỉ :[" + panel.tuning.SystemTuning.getI("tutien_phap_chi_1") + "-" + panel.tuning.SystemTuning.getI("tutien_phap_chi_2") + "-" + panel.tuning.SystemTuning.getI("tutien_phap_chi_3") + "-" + panel.tuning.SystemTuning.getI("tutien_phap_chi_4") + "-" + panel.tuning.SystemTuning.getI("tutien_phap_chi_5") + "-" + panel.tuning.SystemTuning.getI("tutien_phap_chi_6") + "%] Tính Phẩm Cao Nhất",
                                    "Luyện Hóa\nThiên Phú",
                                    "Thông Tin\nChỉ Số",
                                    "Độ Kiếp \nCảnh giới",
                                    "Tới Dị giới\nTu Luyện"
                            );
                            break;
                        }

                    }

                }
            }
        }
    }
}
