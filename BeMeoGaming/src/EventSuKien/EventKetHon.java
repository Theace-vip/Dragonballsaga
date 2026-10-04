package EventSuKien;

/**
 *
 */
import consts.ConstNpc;
import item.Item;
import models.SuperRank.SuperRankService;
import npc.Npc;
import player.Player;
import server.Manager;
import services.InventoryService;
import services.ItemService;
import services.NpcService;
import services.Service;
import services.TaskService;
import utils.Util;

public class EventKetHon extends Npc {

    public EventKetHon(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            Item ocbien = null;
            Item cuabien = null;
            Item saobien = null;
            Item sobien = null;
            Item thoivang = null;
            Item bonghong = null;
            try {
                ocbien = InventoryService.gI().findItemBag(player, 1857);
                cuabien = InventoryService.gI().findItemBag(player, 1858);
                saobien = InventoryService.gI().findItemBag(player, 1859);
                sobien = InventoryService.gI().findItemBag(player, 1860);
                thoivang = InventoryService.gI().findItemBag(player, 457);
                bonghong = InventoryService.gI().findItemBag(player, 723);
            } catch (Exception e) {
            }

            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "|7|KẾT HÔN"
                        + "\n|5|Bạn cần Nhẫn cầu hôn để thực hiện Kết hôn với người khác"
                        + "\nĐiều kiện nhận Nhẫn kết hôn:"
                        + " \n|6|Ốc Biển: x" + (ocbien == null ? 0 : ocbien.quantity) + " / x" + panel.tuning.SystemTuning.getI("kh_oc_bien")
                        + "\nCua Biển: x" + (cuabien == null ? 0 : cuabien.quantity) + " / x" + panel.tuning.SystemTuning.getI("kh_cua_bien")
                        + "\nSao Biển: x" + (saobien == null ? 0 : saobien.quantity) + " / x" + panel.tuning.SystemTuning.getI("kh_sao_bien")
                        + "\nSò Biển: x" + (sobien == null ? 0 : sobien.quantity) + " / x" + panel.tuning.SystemTuning.getI("kh_so_bien")
                        + "\nTu tiên đạt Thánh Nhân: Cấp hiện tại: " + player.TamkjllTuviTutien(Util.maxInt(player.SagaTuTien[1]))
                        + "\nChuyển sinh đạt x" + player.SagaChuyenSinh + " / x" + panel.tuning.SystemTuning.getI("kh_chuyen_sinh")
                        + "\nTham gia Bản đồ kho báu: x" + player.point_bdkb + " / x" + panel.tuning.SystemTuning.getI("kh_bdkb") + " Điểm"
                        + "\nĐạt InGameVIP: " + player.Saga_VIP + " / " + panel.tuning.SystemTuning.getI("kh_vip")
                        + "\nZenni: x" + (thoivang == null ? 0 : thoivang.quantity) + " / x" + Util.format(panel.tuning.SystemTuning.getL("kh_zenni"))
                        + "\nHồng ngọc: x" + Util.format(player.inventory.ruby) + " / x" + Util.format(panel.tuning.SystemTuning.getL("kh_ruby_check"))
                        + "\nBông hồng: x" + (bonghong == null ? 0 : bonghong.quantity) + " / x" + panel.tuning.SystemTuning.getI("kh_bong_hong"),
                        "Nhận Quần\nĐi Biển","Nhận nhẫn", "Thông tin\nKết hôn", "Sinh Em Bé","Thông Tin\nEm Bé"
                );
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    
                      case 0:
                        Item zenni = null;
                       
                        try {
                            zenni = InventoryService.gI().findItemBag(player, 457);
                           
                        } catch (Exception e) {
                        }
                       
                        if (player.Saga_VIP < 0) {
                            Service.gI().sendThongBao(player, "Bạn chưa đủ  VIP");
                            return;
                      
                       
                        } else {
                            InventoryService.gI().subQuantityItemsBag(player, zenni, 1);
                            Item nhan = ItemService.gI().createNewItem((short) 691);
                            InventoryService.gI().addItemBag(player, nhan, 999999);
                            nhan.itemOptions.add(new Item.ItemOption(67, 0));
                            Service.getInstance().sendMoney(player);
                            InventoryService.gI().sendItemBag(player);
                            Service.gI().sendThongBao(player, "|5|Đã nhận được " + nhan.template.name);
                        }
                        break;
                    
                    case 1:
                        Item ocbien = null;
                        Item cuabien = null;
                        Item saobien = null;
                        Item sobien = null;
                        Item thoivang = null;
                        Item bonghong = null;
                        try {
                            ocbien = InventoryService.gI().findItemBag(player, 1857);
                            cuabien = InventoryService.gI().findItemBag(player, 1858);
                            saobien = InventoryService.gI().findItemBag(player, 1859);
                            sobien = InventoryService.gI().findItemBag(player, 1860);
                            thoivang = InventoryService.gI().findItemBag(player, 457);
                            bonghong = InventoryService.gI().findItemBag(player, 723);
                        } catch (Exception e) {
                        }
                        if (ocbien == null || ocbien.quantity < panel.tuning.SystemTuning.getI("kh_oc_bien")) {
                            Service.gI().sendThongBao(player, "Bạn chưa đủ Ôc Biển");
                            return;
                        }
                        if (cuabien == null || cuabien.quantity < panel.tuning.SystemTuning.getI("kh_cua_bien")) {
                            Service.gI().sendThongBao(player, "Bạn chưa đủ Cua Biển");
                            return;
                        }
                        if (saobien == null || saobien.quantity < panel.tuning.SystemTuning.getI("kh_sao_bien")) {
                            Service.gI().sendThongBao(player, "Bạn chưa đủ Sao Biển");
                            return;
                        }
                        if (sobien == null || sobien.quantity < panel.tuning.SystemTuning.getI("kh_so_bien")) {
                            Service.gI().sendThongBao(player, "Bạn chưa đủ Sò Biển");
                            return;
                        }
                        if (thoivang == null || thoivang.quantity < panel.tuning.SystemTuning.getI("kh_zenni")) {
                            Service.gI().sendThongBao(player, "Bạn chưa đủ 99999 Zenni");
                            return;
                        }
                        if (player.inventory.ruby < panel.tuning.SystemTuning.getL("kh_ruby_check")) {
                            Service.gI().sendThongBao(player, "Bạn chưa đủ Hồng ngọc");
                            return;
                        }
                        if (bonghong == null || bonghong.quantity < panel.tuning.SystemTuning.getI("kh_bong_hong")) {
                            Service.gI().sendThongBao(player, "Bạn chưa đủ Bông hồng");
                            return;
                        }
                        if (player.Saga_VIP < panel.tuning.SystemTuning.getI("kh_vip")) {
                            Service.gI().sendThongBao(player, "Bạn chưa đủ  VIP");
                            return;
                        }
                        if (player.SagaChuyenSinh < panel.tuning.SystemTuning.getI("kh_chuyen_sinh")) {
                            Service.gI().sendThongBao(player, "Bạn chưa đủ Số lẩn Chuyển sinh");
                            return;
                        }
                        if (player.SagaTuTien[1] < panel.tuning.SystemTuning.getI("kh_tutien")) {
                            Service.gI().sendThongBao(player, "Bạn chưa đủ Cấp Tu tiên");
                            return;
                        }

                        if (player.point_bdkb < panel.tuning.SystemTuning.getI("kh_bdkb")) {
                            Service.gI().sendThongBao(player, "Bạn chưa đủ 10k Điểm Trong Bản đồ kho báu");
                        } else {
                            player.inventory.ruby -= panel.tuning.SystemTuning.getL("kh_ruby_tru");
                            player.point_bdkb -= panel.tuning.SystemTuning.getI("kh_bdkb");
                            InventoryService.gI().subQuantityItemsBag(player, ocbien, panel.tuning.SystemTuning.getI("kh_oc_bien"));
                            InventoryService.gI().subQuantityItemsBag(player, cuabien, panel.tuning.SystemTuning.getI("kh_cua_bien"));
                            InventoryService.gI().subQuantityItemsBag(player, saobien, panel.tuning.SystemTuning.getI("kh_sao_bien"));
                            InventoryService.gI().subQuantityItemsBag(player, sobien, panel.tuning.SystemTuning.getI("kh_so_bien"));
                            InventoryService.gI().subQuantityItemsBag(player, thoivang, panel.tuning.SystemTuning.getI("kh_zenni_tru"));
                            InventoryService.gI().subQuantityItemsBag(player, bonghong, panel.tuning.SystemTuning.getI("kh_bong_hong"));

                            Item nhan = ItemService.gI().createNewItem((short) 1213);
                            InventoryService.gI().addItemBag(player, nhan, 999999);
                            nhan.itemOptions.add(new Item.ItemOption(Util.nextInt(67, 71), 0));
                            nhan.itemOptions.add(new Item.ItemOption(59, Util.nextInt(1, 2)));
                            nhan.itemOptions.add(new Item.ItemOption(50, Util.nextInt(1, 200)));
                            nhan.itemOptions.add(new Item.ItemOption(77, Util.nextInt(1, 200)));
                            nhan.itemOptions.add(new Item.ItemOption(103, Util.nextInt(1, 200)));
                            nhan.itemOptions.add(new Item.ItemOption(60, Util.nextInt(1, 2)));
                            nhan.itemOptions.add(new Item.ItemOption(249, 1));
                            nhan.itemOptions.add(new Item.ItemOption(61, Util.nextInt(1, 2)));
                            nhan.itemOptions.add(new Item.ItemOption(251, 1));
                            nhan.itemOptions.add(new Item.ItemOption(231, 0));
                            Service.getInstance().sendMoney(player);
                            InventoryService.gI().sendItemBag(player);
                            Service.gI().sendThongBao(player, "|5|Đã nhận được " + nhan.template.name);
                        }
                        break;
                    case 2:
                        this.createOtherMenu(player, 54855, "|7|Thông tin kết hôn"
                                + "\n|5|Số lần Kết hôn: " + player.dakethon + " Lần"
                                + "\n|4|+" + Math.round(player.dakethon * panel.tuning.StatRateTuning.real("dakethon_hp", 10000) / 100d) + "% Chỉ số HP,KI,SD"
                                + "\n|5|Số lần Được Cầu hôn: " + player.duockethon + " Lần"
                                + "\n|4|+" + Math.round(player.duockethon * panel.tuning.StatRateTuning.real("duockethon_hp", 1000) / 100d) + "% Chỉ số HP,KI,SD",
                                "OK");
                        break;

                    case 3:
                          if (player.dakethon < panel.tuning.SystemTuning.getI("embe_can_ket_hon")) {
                                    Service.gI().sendThongBaoFromAdmin(player, "✎Mày Đã Kết Hôn Đâu -Chích Cây Chuối À");
                                    return;
                                }
                        NpcService.gI().createMenuConMeo(player, ConstNpc.EMbe, -1,
                                "|7|HELLO\n"
                                + "|4|Động Phòng Sinh Con Thôi Nào.....\n"
                                + "|6|xxxxxyyyyyyyzzzzzzzzzz\n",
                                "Khởi Tạo\nEm Bé", "Đóng"
                                
                        );
                        break;

                    case 4:
                       NpcService.gI().createMenuConMeo(player, ConstNpc.Saga_EmBe, -1,
                                        "|7|Em Bé Rồng\n"
                                        + "|7|Name Em Bé: " + player.TamkjllNamePet + " \n"
                                        + "|7|LV Em Bé: (" + (player.EmBeLv * 100 / (3000000L + player.EmBeLv * 1500000L))
                                        + "%)-LV: "
                                        + player.EmBeLv + "\n"
                                        + "|4|Linh Căn: " + player.LinhCanEmBe(player.TamkjllPetGiong) + "\n"
                                        + "Thức ăn: " + player.TamkjllPetHunger + "%\n"
                                        + "Sức mạnh: " + Util.getFormatNumber(player.TamkjllPetPower) + "\n"
                                        + "Hãy Cho EmBe Ăn Liên Tục Không Sẽ Chết Đói Đó\n"
                                        + "Chết EmBe Sẽ Bỏ Nhà Gia Đi Đó\n"
                                        + "|6|Baba Hãy Ra Lệnh Cho Con\n",
                                        "Cho Ăn\n5k Ruby", "Đi Theo", "PK Người", "Pk quái",
                                        "Về Nhà", "Kĩ Năng\nEmBe");

                                break;
                }
            }
        }
    }
}
