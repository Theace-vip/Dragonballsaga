package npc.npc_manifest;

/**
 *
 */
import consts.ConstDailyGift;
import consts.ConstMenu;
import consts.ConstNpc;
import item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import models.Combine.Combine;

import models.Combine.CombineService;
import models.Combine.manifest.CheTaoCuonSachCu;
import models.Combine.manifest.DoiSachTuyetKy;
import models.Combine.manifest.NangCapVatPham;
import models.DeathOrAliveArena.DeathOrAliveArena;
import models.DeathOrAliveArena.DeathOrAliveArenaManager;
import models.DeathOrAliveArena.DeathOrAliveArenaService;
import npc.Npc;
import player.Player;
import player.dailyGift.DailyGiftService;
import server.Client;
import services.InventoryService;
import services.ItemService;
import services.Service;
import services.func.ChangeMapService;
import shop.ShopService;
import utils.Util;

public class BaHatMit extends Npc {

    public BaHatMit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (this.mapId) {
                case 5 ->
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|˚₊· ͟͟͞͞➳❥Ngươi tìm ta có việc gì?\n"
                            + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                            + "|4|Hãy Đi Kiếm Đồ Thần Tại Map Mabu Về Đây Tao Buff Vip Cho\n"
                            + "Số Người Online : " + Client.gI().getPlayers().size() + "0 Người\n"
                            + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n",
                            "Chức năng\nPha lê", "Chức năng\nBông tai", "Chức năng\nKhác");
                case 112 -> {
                    if (Util.isAfterMidnight(player.lastTimePKVoDaiSinhTu)) {
                        player.haveRewardVDST = false;
                        player.thoiVangVoDaiSinhTu = 0;
                    }
                    if (player.haveRewardVDST) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "˚₊· ͟͟͞͞➳❥Đây là phần thưởng cho con.", "1 ngọc bí\nbất kì", "1 bí ngô");
                        return;
                    }
                    if (DeathOrAliveArenaManager.gI().getVDST(player.zone) != null) {
                        if (DeathOrAliveArenaManager.gI().getVDST(player.zone).getPlayer().equals(player)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "˚₊· ͟͟͞͞➳❥Ngươi muốn hủy đăng ký thi đấu võ đài?", "Top 100", "Đồng ý\n" + player.thoiVangVoDaiSinhTu + " thỏi vàng", "Từ chối", "Về\nđảo rùa");
                            return;
                        }
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "˚₊· ͟͟͞͞➳❥Ngươi muốn đăng ký thi đấu võ đài?\nnhiều phần thưởng giá trị đang đợi ngươi đó", "Top 100", "Bình chọn", "Đồng ý\n" + player.thoiVangVoDaiSinhTu + " thỏi vàng", "Từ chối", "Về\nđảo rùa");
                        return;
                    }
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "˚₊· ͟͟͞͞➳❥Ngươi muốn đăng ký thi đấu võ đài?\nnhiều phần thưởng giá trị đang đợi ngươi đó", "Top 100", "Đồng ý\n" + player.thoiVangVoDaiSinhTu + " thỏi vàng", "Từ chối", "Về\nđảo rùa");
                }
                case 174 ->
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "˚₊· ͟͟͞͞➳❥Ngươi tìm ta có việc gì?", "Quay về", "Từ chối");
                case 181 ->
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "˚₊· ͟͟͞͞➳❥Ngươi tìm ta có việc gì?", "Quay về", "Từ chối");
                default -> {
                    List<String> menu = new ArrayList<>(Arrays.asList("Sách\nTuyệt Kỹ", "Cửa hàng\nBùa", "Nâng cấp\nVật phẩm", "Làm phép\nNhập đá", "Nhập\nNgọc Rồng"));
                    if (InventoryService.gI().findItem(player, 454) || InventoryService.gI().findItem(player, 921)) {
                        menu = new ArrayList<>(Arrays.asList("Sách\nTuyệt Kỹ", "Cửa hàng\nBùa", "Nâng cấp\nVật phẩm", InventoryService.gI().findItemBongTaiCap2(player) ? "Mở chỉ số\nBông tai\nPorata cấp\n2" : "Nâng cấp\nBông tai\nPorata", "Làm phép\nNhập đá", "Nhập\nNgọc Rồng"));
                    }
                    if (DailyGiftService.checkDailyGift(player, ConstDailyGift.NHAN_BUA_MIEN_PHI)) {
                        menu.add(0, "Thưởng\nBùa 1h\nngẫu nhiên");
                    }
                    String[] menus = menu.toArray(new String[0]);
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "˚₊· ͟͟͞͞➳❥Ngươi tìm ta có việc gì?", menus);
                }
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (this.mapId) {
                case 5 -> {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU -> {
                            switch (select) {
                                case 0 ->
                                    createOtherMenu(player, ConstMenu.MENU_PHA_LE,
                                            "|7|Chức Năng Ep Khảm Sao Pha Lê••\n"
                                            + "|4|˚Ép Sao Pha Lê : Áo, Quần, Găng, Giày, Nhẫn\n"
                                            + "•Ép Sao Phụ Kiện : Từ ô thứ 6 (Cải trang, Phụ kiện, Giáp luyện tập)•\n"
                                            + "•Pha Lê Hóa : Mọi trang bị đều đục được lỗ theo cấp VIP•\n"
                                            + "FREE\n",
                                            "Ép sao\ntrang bị",
                                            "Pha Lê Hóa",
                                            "Đục Khảm\nLỗ",
                                            "Ép Sao\n Phụ Kiện",
                                            "Gỡ Sao\nPha lê");
                                case 1 ->
                                    createOtherMenu(player, ConstMenu.MENU_BONG_TAI,
                                            "|7|•Chức Năng Bông Tai Poratal\n"
                                            + "|4|˚₊Chọn nâng Cấp Bông Tai Từ LV1\n"
                                            + "•Mở Chỉ Số RamDom 1-100% HP,KI,SD,CM•\n"
                                            + "|7|Hãy Đi Đến Map Up Bông Tai Kiếm Nguyên Liệu",
                                            "Nâng cấp\nbông tai",
                                            "Mở chỉ số\nbông tai",
                                            "Map Up\nbông tai"
                                    );
                                case 2 ->
                                    createOtherMenu(player, ConstMenu.MENU_CHUC_NANG_KHAC,
                                            "•Chức Năng Nâng Cấp Và Mở Chỉ Số Cai Trang•\n"
                                            + "˚₊Chân Mệnh : Cần Chân Mệnh Lv1 Free Và Đã Ngũ Sắc\n"
                                            + "•Mở Chỉ Số Cải Trang: Cần 20 Lượng Vàng Và CảiTrang Kích Hoạt•\n"
                                            + "|7|New Nâng Cấp Cánh VIP Nhận Chỉ Số Mới Riêng Biết Cực Mạnh\n",
                                            "Võ đài\nSinh Tử", "Nâng Cấp\nChân Mệnh", "Mở Chỉ Số\nCải Trang","Ghép\nNgọc Rồng","Ghép Rương\nThần Bí","Nâng Cấp\nCánh");
                                case 3 ->
                                    createOtherMenu(player, ConstMenu.MENU_mabu, "|7|˚₊· ͟͟͞͞➳❥Ta có thể giúp gì cho ngươi ?\n"
                                            + "|4|Kiếm Full Sét pro Max Đi cu , Mày Còn Cùi Lắm",
                                            "Đến Map\nSăn Mabu");

                            }
                        }
                        case ConstMenu.MENU_PHA_LE -> {
                            switch (select) {
                                case 0: //Ép sao trang bị
                                    CombineService.gI().openTabCombine(player, CombineService.EP_SAO_TRANG_BI);
                                    break;
                                case 1: //Pha lê hoá trang bị
                                    createOtherMenu(player, ConstMenu.MENU_PHA_LE_HOA_TRANG_BI, "˚₊· ͟͟͞͞➳❥Ngươi muốn pha lê hoá trang bị bằng cách nào?",
                                            "Bằng ngọc", "Từ chối");
                                    break;
                                case 2: //Ép sao trang bị
                                    CombineService.gI().openTabCombine(player, CombineService.DUC_LO_KHAM);
                                    break;
                                case 3:
                                    CombineService.gI().openTabCombine(player, CombineService.KHAM_EP_DA);
                                    break;
                                case 4:
                                    CombineService.gI().openTabCombine(player, CombineService.XOA_SPL);
                                    break;
                            }
                        }

                        case ConstMenu.MENU_BONG_TAI -> {
                            switch (select) {
                                case 0 ->
                                    CombineService.gI().openTabCombine(player, CombineService.NANG_CAP_BONG_TAI);
                                case 1 ->
                                    CombineService.gI().openTabCombine(player, CombineService.NANG_CHI_SO_BONG_TAI);
                                case 2 ->
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 156, 205, 504);
                            }
                        }
                        case ConstMenu.MENU_CHUC_NANG_KHAC -> {
                            switch (select) {
                                case 0 ->
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 112, 200 + Util.nextInt(-100, 100), 408);
                                case 1 ->
                                    CombineService.gI().openTabCombine(player, CombineService.NANG_CAP_CHAN_MENH);
                                case 2 ->
                                    CombineService.gI().openTabCombine(player, CombineService.MO_CHI_SO_CAI_TRANG);
//                              
                                case 3 ->
                                    CombineService.gI().openTabCombine(player, CombineService.NHAP_NGOC_RONG);
                                     case 4 ->
                                    CombineService.gI().openTabCombine(player, CombineService.nangcapruong);
                                      case 5 ->
                                    CombineService.gI().openTabCombine(player, CombineService.Nang_cap_canh);

//                                case 3 ->
//                                    ChangeMapService.gI().changeMapNonSpaceship(player, 127, 200 + Util.nextInt(-100, 100), 408);
                            }
                        }
                        case ConstMenu.MENU_mabu -> {
                            switch (select) {

                                case 0 ->
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 127, 200 + Util.nextInt(-100, 100), 408);
                            }
                        }

                        case ConstMenu.MENU_NANG_CAP_TRANG_BI -> {
                            switch (select) {
                                case 0:
                                    CombineService.gI().openTabCombine(player, CombineService.NANG_CAP_KICH_HOAT);
                                    break;
                                case 1:
                                    CombineService.gI().openTabCombine(player, CombineService.NANG_CAP_KICH_HOAT_VIP);
                                    break;
                            }
                        }
                        case ConstMenu.MENU_CHUYEN_HOA_TRANG_BI -> {
                            switch (select) {
                                case 0 -> {
                                    CombineService.gI().openTabCombine(player, CombineService.CHUYEN_HOA_TRANG_BI_DUNG_VANG);
                                }
                                case 1 -> {
                                    CombineService.gI().openTabCombine(player, CombineService.CHUYEN_HOA_TRANG_BI_DUNG_NGOC);
                                }
                            }
                        }
                        case ConstMenu.MENU_PHA_LE_HOA_TRANG_BI -> {
                            if (select == 0) {
                                CombineService.gI().openTabCombine(player, CombineService.PHA_LE_HOA_TRANG_BI);
                            }
                        }
                        case ConstNpc.MENU_START_COMBINE -> {
                            switch (player.combine.typeCombine) {
                                case CombineService.PHA_LE_HOA_TRANG_BI -> {
                                    switch (select) {
                                        case 0 ->
                                            CombineService.gI().startCombine(player, 10);
                                        case 1 ->
                                            CombineService.gI().startCombine(player, 1);
//                                        case 2 ->
//                                            CombineService.gI().startCombine(player,1);
                                    }
                                }
                                case CombineService.MO_CHI_SO_CAI_TRANG -> {
                                    // Mo chi so cai trang: 3 muc chi phi -> 0=20 luong (x1), 1=40 luong (x2), 2=100 luong (x6); 3=Tu choi
                                    switch (select) {
                                        case 0, 1, 2 ->
                                            CombineService.gI().startCombine(player, select);
                                    }
                                }
                                case CombineService.NANG_CAP_BONG_TAI, CombineService.NANG_CHI_SO_BONG_TAI, CombineService.NANG_CAP_KICH_HOAT_VIP, CombineService.NANG_CAP_KICH_HOAT, CombineService.NANG_CAP_SAO_PHA_LE, CombineService.DANH_BONG_SAO_PHA_LE, CombineService.CUONG_HOA_LO_SAO_PHA_LE, CombineService.TAO_DA_HEMATITE, CombineService.EP_SAO_TRANG_BI, CombineService.NANG_CAP_CHAN_MENH, CombineService.XOA_SPL, CombineService.DUC_LO_KHAM, CombineService.KHAM_EP_DA ,CombineService.NHAP_NGOC_RONG ,CombineService.nangcapruong ,CombineService.Nang_cap_canh-> {
                                    switch (select) {
                                        case 0:
                                            CombineService.gI().startCombine(player);
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
                case 112 -> {
                    if (player.iDMark.isBaseMenu()) {
                        if (player.haveRewardVDST) {
                            switch (select) {
                                case 0 -> {
                                    if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                        Item item = ItemService.gI().createNewItem((short) (Util.nextInt(705, 708)));
                                        item.itemOptions.add(new Item.ItemOption(93, 30));
                                        InventoryService.gI().addItemBag(player, item, 999999);
                                        InventoryService.gI().sendItemBag(player);
                                        Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
                                        player.haveRewardVDST = false;
                                    } else {
                                        Service.gI().sendThongBao(player, "Hành trang không còn chỗ trống, không thể nhặt thêm");
                                    }
                                }
                                case 1 -> {
                                    if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                        Item item = ItemService.gI().createNewItem((short) 585);
                                        item.itemOptions.add(new Item.ItemOption(93, 30));
                                        InventoryService.gI().addItemBag(player, item, 999999);
                                        InventoryService.gI().sendItemBag(player);
                                        Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name);
                                        player.haveRewardVDST = false;
                                    } else {
                                        Service.gI().sendThongBao(player, "Hành trang không còn chỗ trống, không thể nhặt thêm");
                                    }
                                }
                            }
                            return;
                        }
                        if (DeathOrAliveArenaManager.gI().getVDST(player.zone) != null) {
                            if (DeathOrAliveArenaManager.gI().getVDST(player.zone).getPlayer().equals(player)) {
                                switch (select) {
                                    case 0 -> {
                                    }
                                    case 1 ->
                                        this.npcChat("Không thể thực hiện");
                                    case 2 -> {
                                    }
                                    case 3 ->
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                }
                                return;
                            }
                            switch (select) {
                                case 0 -> {
                                }
                                case 1 ->
                                    this.createOtherMenu(player, ConstNpc.DAT_CUOC_HAT_MIT, "Phí bình chọn là 1 triệu vàng\nkhi trận đấu kết thúc\n90% tổng tiền bình chọn sẽ chia đều cho phe bình chọn chính xác", "Bình chọn cho " + DeathOrAliveArenaManager.gI().getVDST(player.zone).getPlayer().name + " (" + DeathOrAliveArenaManager.gI().getVDST(player.zone).getCuocPlayer() + ")", "Bình chọn cho hạt mít (" + DeathOrAliveArenaManager.gI().getVDST(player.zone).getCuocBaHatMit() + ")");
                                case 2 ->
                                    DeathOrAliveArenaService.gI().startChallenge(player);
                                case 3 -> {
                                }
                                case 4 ->
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                            }
                            return;
                        }
                        switch (select) {
                            case 0 -> {
                            }
                            case 1 ->
                                DeathOrAliveArenaService.gI().startChallenge(player);
                            case 2 -> {
                            }
                            case 3 ->
                                ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.DAT_CUOC_HAT_MIT) {
                        if (DeathOrAliveArenaManager.gI().getVDST(player.zone) != null) {
                            switch (select) {
                                case 0 -> {
                                    if (player.inventory.gold >= 1_000_000) {
                                        DeathOrAliveArena vdst = DeathOrAliveArenaManager.gI().getVDST(player.zone);
                                        vdst.setCuocPlayer(vdst.getCuocPlayer() + 1);
                                        vdst.addBinhChon(player);
                                        player.binhChonPlayer++;
                                        player.zoneBinhChon = player.zone;
                                        player.inventory.gold -= 1_000_000;
                                        Service.gI().sendMoney(player);
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn không đủ vàng, còn thiếu " + Util.numberToMoney(1_000_000 - player.inventory.gold) + " vàng nữa");
                                    }
                                }
                                case 1 -> {
                                    if (player.inventory.gold >= 1_000_000) {
                                        DeathOrAliveArena vdst = DeathOrAliveArenaManager.gI().getVDST(player.zone);
                                        vdst.setCuocBaHatMit(vdst.getCuocBaHatMit() + 1);
                                        vdst.addBinhChon(player);
                                        player.binhChonHatMit++;
                                        player.zoneBinhChon = player.zone;
                                        player.inventory.gold -= 1_000_000;
                                        Service.gI().sendMoney(player);
                                    } else {
                                        Service.gI().sendThongBao(player, "Bạn không đủ vàng, còn thiếu " + Util.numberToMoney(1_000_000 - player.inventory.gold) + " vàng nữa");
                                    }
                                }
                            }
                        }
                    }
                }
                case 174, 181 -> {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0 ->
                                ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                        }
                    }
                }
                case 42, 43, 44, 84 -> {
                    if (player.iDMark.isBaseMenu()) {
                        if (!DailyGiftService.checkDailyGift(player, ConstDailyGift.NHAN_BUA_MIEN_PHI)) {
                            select++;
                        }
                        if (!InventoryService.gI().findItem(player, 454) && !InventoryService.gI().findItem(player, 921)) {
                            if (select >= 4) {
                                select++;
                            }
                        }
                        switch (select) {
                            case 0:
                                if (DailyGiftService.checkDailyGift(player, ConstDailyGift.NHAN_BUA_MIEN_PHI)) {
                                    int idItem = Util.nextInt(213, 219);
                                    player.charms.addTimeCharms(idItem, 60);
                                    Item bua = ItemService.gI().createNewItem((short) idItem);
                                    Service.gI().sendThongBao(player, "Bạn vừa nhận thưởng " + bua.template.name);
                                    DailyGiftService.updateDailyGift(player, ConstDailyGift.NHAN_BUA_MIEN_PHI);
                                } else {
                                    Service.gI().sendThongBao(player, "Hôm nay bạn đã nhận bùa miễn phí rồi!!!");
                                }
                                break;
                            case 1:
                                createOtherMenu(player, ConstNpc.MENU_SACH_TUYET_KY, "Ta có thể giúp gì cho ngươi ?",
                                        "Đóng thành\nSách cũ",
                                        "Đổi Sách\nTuyệt kỹ",
                                        "Giám định\nSách",
                                        "Tẩy\nSách",
                                        "Nâng cấp\nSách\nTuyệt kỹ",
                                        "Hồi phục\nSách",
                                        "Phân rã\nSách");
                                break;
                            case 2:
                                createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA, "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để " + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                        "Bùa\n1 tháng", "Đóng");
                                break;
                            case 3:
                                CombineService.gI().openTabCombine(player, CombineService.NANG_CAP_VAT_PHAM);
                                break;
                            case 4:
                                if (InventoryService.gI().findItemBongTaiCap2(player)) {
                                    CombineService.gI().openTabCombine(player, CombineService.NANG_CHI_SO_BONG_TAI);
                                } else {
                                    CombineService.gI().openTabCombine(player, CombineService.NANG_CAP_BONG_TAI);
                                }
                                break;
                            case 5:
                                CombineService.gI().openTabCombine(player, CombineService.LAM_PHEP_NHAP_DA);
                                break;
                            case 6:
                                CombineService.gI().openTabCombine(player, CombineService.NHAP_NGOC_RONG);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_SACH_TUYET_KY) {
                        switch (select) {
                            case 0:
                                CheTaoCuonSachCu.showCombine(player);
                                break;
                            case 1:
                                DoiSachTuyetKy.showCombine(player);
                                break;
                            case 2:
                                CombineService.gI().openTabCombine(player, CombineService.GIAM_DINH_SACH);
                                break;
                            case 3:
                                CombineService.gI().openTabCombine(player, CombineService.TAY_SACH);
                                break;
                            case 4:
                                CombineService.gI().openTabCombine(player, CombineService.NANG_CAP_SACH_TUYET_KY);
                                break;
                            case 5:
                                CombineService.gI().openTabCombine(player, CombineService.HOI_PHUC_SACH);
                                break;
                            case 6:
                                CombineService.gI().openTabCombine(player, CombineService.PHAN_RA_SACH);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.DONG_THANH_SACH_CU) {
                        CheTaoCuonSachCu.cheTaoCuonSachCu(player);
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.DOI_SACH_TUYET_KY) {
                        DoiSachTuyetKy.doiSachTuyetKy(player);
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                        switch (select) {

                            case 0 ->
                                ShopService.gI().opendShop(player, "BUA_1M", true);
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                        switch (player.combine.typeCombine) {
                            case CombineService.NANG_CAP_BONG_TAI, CombineService.NANG_CHI_SO_BONG_TAI, CombineService.LAM_PHEP_NHAP_DA, CombineService.NHAP_NGOC_RONG, CombineService.GIAM_DINH_SACH, CombineService.TAY_SACH, CombineService.NANG_CAP_SACH_TUYET_KY, CombineService.HOI_PHUC_SACH, CombineService.PHAN_RA_SACH -> {
                                if (select == 0) {
                                    CombineService.gI().startCombine(player);
                                }
                            }
                            case CombineService.NANG_CAP_VAT_PHAM -> {
                                if (select == 0) {
                                    CombineService.gI().startCombine(player);
                                } else if (select == 1) {
                                    NangCapVatPham.nangCapVatPham(player, true);
                                }
                            }
                        }
                    }
                }
                default -> {
                }
            }
        }
    }
}
