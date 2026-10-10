package models.Combine;

import consts.ConstNpc;
import item.Item;

import java.io.IOException;

import models.Combine.manifest.CheTaoTrangBiThienSu;
import models.Combine.manifest.CuongHoaLoSaoPhaLe;
import models.Combine.manifest.DanhBongSaoPhaLe;
import models.Combine.manifest.DucLoKham;
import models.Combine.manifest.EpSaoTrangBi;
import models.Combine.manifest.GiamDinhSach;
import models.Combine.manifest.HoiPhucSach;
import models.Combine.manifest.KhamEpDa;
import models.Combine.manifest.MoChiSoCaiTrang;
import models.Combine.manifest.NangCapBongTai;
import models.Combine.manifest.NangCapCanh;
import models.Combine.manifest.NangCapChanMenh;
import models.Combine.manifest.NangCapKichHoat;
import models.Combine.manifest.NangCapKichHoatVip;
import models.Combine.manifest.NangCapSachTuyetKy;
import models.Combine.manifest.NangCapSaoPhaLe;
import models.Combine.manifest.NangCapVatPham;
import models.Combine.manifest.NangChiSoBongTai;
import models.Combine.manifest.NangcapRuongthanbi;
import models.Combine.manifest.NhapNgocRong;
import models.Combine.manifest.PhaLeHoaTrangBi;
import models.Combine.manifest.PhanRaSach;
import models.Combine.manifest.TaoDaHematite;
import models.Combine.manifest.TaySach;
import models.Combine.manifest.XoaSpl;
import player.Player;
import network.Message;
import npc.Npc;
import npc.NpcManager;
import services.InventoryService;

public class CombineService {

    private static final int COST = 500000000;
    private static final int TIME_COMBINE = 1500;
    public static final int MAX_STAR_ITEM = 999;
    public static final int MAX_DUCKHAM = 999;
    public static final byte MAX_LEVEL_ITEM = 16;
    private static final byte OPEN_TAB_COMBINE = 0;
    private static final byte REOPEN_TAB_COMBINE = 1;
    private static final byte combineSUCCESS = 2;
    private static final byte combineFAIL = 3;
    private static final byte combineCHANGE_OPTION = 4;
    private static final byte combineDRAGON_BALL = 5;
    public static final byte OPEN_ITEM = 6;
    public static final int EP_SAO_TRANG_BI = 500;
    public static final int PHA_LE_HOA_TRANG_BI = 501;
    public static final int CHUYEN_HOA_TRANG_BI_DUNG_VANG = 502;
    public static final int CHUYEN_HOA_TRANG_BI_DUNG_NGOC = 503;
    public static final int NHAP_DA = 504;
    public static final int NANG_CAP_SAO_PHA_LE = 100;
    public static final int DANH_BONG_SAO_PHA_LE = 101;
    public static final int CUONG_HOA_LO_SAO_PHA_LE = 102;
    public static final int TAO_DA_HEMATITE = 103;
    public static final int GIAM_DINH_SACH = 104;
    public static final int TAY_SACH = 105;
    public static final int NANG_CAP_SACH_TUYET_KY = 106;
    public static final int HOI_PHUC_SACH = 107;
    public static final int PHAN_RA_SACH = 108;
    public static final int CHE_TAO_TRANG_BI_THIEN_SU = 109;
    public static final int NANG_CAP_VAT_PHAM = 510;
    public static final int NANG_CAP_BONG_TAI = 511;
    public static final int LAM_PHEP_NHAP_DA = 512;
    public static final int NHAP_NGOC_RONG = 513;
    public static final int NANG_CHI_SO_BONG_TAI = 517;
    public static final int NANG_CAP_KICH_HOAT = 518;
    public static final int NANG_CAP_KICH_HOAT_VIP = 519;
    public static final int NANG_CAP_CHAN_MENH = 520;
    public static final int MO_CHI_SO_CAI_TRANG = 521;
    public static final int XOA_SPL = 522;
    public static final int DUC_LO_KHAM = 523;
    public static final int KHAM_EP_DA = 524;
    public static final int Nang_cap_canh = 5555;
     public static final int nangcapruong = 5556;

    private static CombineService instance;

    public final Npc baHatMit;
    public final Npc whis;

    private CombineService() {
        this.baHatMit = NpcManager.getNpc(ConstNpc.BA_HAT_MIT);
        this.whis = NpcManager.getNpc(ConstNpc.WHIS);
    }

    public static CombineService gI() {
        if (instance == null) {
            instance = new CombineService();
        }
        return instance;
    }

    /**
     * Hiển thị thông tin đập đồ
     *
     * @param player
     * @param index
     */
    public void showInfoCombine(Player player, int[] index) {
        if (player.combine == null) {
            return;
        }
        player.combine.clearItemCombine();
        if (index.length > 0) {
            for (int i = 0; i < index.length; i++) {
                player.combine.itemsCombine.add(player.inventory.itemsBag.get(index[i]));
            }
        }
        switch (player.combine.typeCombine) {
            case KHAM_EP_DA->
                KhamEpDa.showInfoCombine(player);
                case nangcapruong->
                NangcapRuongthanbi.showInfoCombine(player);
            case DUC_LO_KHAM->
                DucLoKham.showInfoCombine(player);
            case XOA_SPL ->
                XoaSpl.showCombine(player);
            case MO_CHI_SO_CAI_TRANG ->
                MoChiSoCaiTrang.showCombine(player);
            case NANG_CAP_CHAN_MENH ->
                NangCapChanMenh.showCombine(player);
                  case Nang_cap_canh ->
                NangCapCanh.showCombine(player);
            case EP_SAO_TRANG_BI ->
                EpSaoTrangBi.showInfoCombine(player);
            case PHA_LE_HOA_TRANG_BI ->
                PhaLeHoaTrangBi.showInfoCombine(player);
            case NHAP_NGOC_RONG ->
                NhapNgocRong.showInfoCombine(player);
            case NANG_CAP_VAT_PHAM ->
                NangCapVatPham.showInfoCombine(player);
            case NANG_CAP_BONG_TAI ->
                NangCapBongTai.showInfoCombine(player);
            case NANG_CHI_SO_BONG_TAI ->
                NangChiSoBongTai.showInfoCombine(player);
            case NANG_CAP_SAO_PHA_LE ->
                NangCapSaoPhaLe.showInfoCombine(player);
            case DANH_BONG_SAO_PHA_LE ->
                DanhBongSaoPhaLe.showInfoCombine(player);
            case CUONG_HOA_LO_SAO_PHA_LE ->
                CuongHoaLoSaoPhaLe.showInfoCombine(player);
            case TAO_DA_HEMATITE ->
                TaoDaHematite.showInfoCombine(player);
            case GIAM_DINH_SACH ->
                GiamDinhSach.showInfoCombine(player);
            case TAY_SACH ->
                TaySach.showInfoCombine(player);
            case NANG_CAP_SACH_TUYET_KY ->
                NangCapSachTuyetKy.showInfoCombine(player);
            case HOI_PHUC_SACH ->
                HoiPhucSach.showInfoCombine(player);
            case PHAN_RA_SACH ->
                PhanRaSach.showInfoCombine(player);
            case CHE_TAO_TRANG_BI_THIEN_SU ->
                CheTaoTrangBiThienSu.showInfoCombine(player);
            case NANG_CAP_KICH_HOAT ->
                NangCapKichHoat.showInfoCombine(player);
            case NANG_CAP_KICH_HOAT_VIP ->
                NangCapKichHoatVip.showInfoCombine(player);
        }
    }

    /**
     * Bắt đầu đập đồ - điều hướng từng loại đập đồ
     *
     * @param player
     * @param n
     */
    public void startCombine(Player player, int... n) {
        int num = 0;
        if (n.length > 0) {
            num = n[0];
        }
        switch (player.combine.typeCombine) {
            case KHAM_EP_DA->
                KhamEpDa.KhamEpDa(player);
             case DUC_LO_KHAM->
                DucLoKham.DucLoKham(player);
                 case nangcapruong->
                NangcapRuongthanbi.NangcapRuongthanbi1(player, num == 1);
            case XOA_SPL ->
                XoaSpl.XoaSpl(player);
            case MO_CHI_SO_CAI_TRANG ->
                MoChiSoCaiTrang.MoChiSoCaiTrang(player, num);
            case NANG_CAP_CHAN_MENH ->
                NangCapChanMenh.NangCapChanMenh(player);
            case EP_SAO_TRANG_BI ->
                EpSaoTrangBi.epSaoTrangBi(player);
            case PHA_LE_HOA_TRANG_BI ->
                PhaLeHoaTrangBi.phaLeHoa(player, num);
            case NHAP_NGOC_RONG ->
                NhapNgocRong.nhapNgocRong(player, num == 1);
            case NANG_CAP_VAT_PHAM ->
                NangCapVatPham.nangCapVatPham(player, num == 1);
            case NANG_CAP_BONG_TAI ->
                NangCapBongTai.nangCapBongTai(player);
            case NANG_CHI_SO_BONG_TAI ->
                NangChiSoBongTai.nangChiSoBongTai(player);
            case NANG_CAP_SAO_PHA_LE ->
                NangCapSaoPhaLe.nangCapSaoPhaLe(player);
            case DANH_BONG_SAO_PHA_LE ->
                DanhBongSaoPhaLe.danhBongSaoPhaLe(player);
            case CUONG_HOA_LO_SAO_PHA_LE ->
                CuongHoaLoSaoPhaLe.cuongHoaLoSaoPhaLe(player);
            case TAO_DA_HEMATITE ->
                TaoDaHematite.taoDaHematite(player);
            case GIAM_DINH_SACH ->
                GiamDinhSach.giamDinhSach(player);
            case TAY_SACH ->
                TaySach.taySach(player);
            case NANG_CAP_SACH_TUYET_KY ->
                NangCapSachTuyetKy.nangCapSachTuyetKy(player);
            case HOI_PHUC_SACH ->
                HoiPhucSach.hoiPhucSach(player);
            case PHAN_RA_SACH ->
                PhanRaSach.phanRaSach(player);
            case CHE_TAO_TRANG_BI_THIEN_SU ->
                CheTaoTrangBiThienSu.cheTaoTrangBiThienSu(player);
            case NANG_CAP_KICH_HOAT ->
                NangCapKichHoat.startCombine(player);
            case NANG_CAP_KICH_HOAT_VIP ->
                NangCapKichHoatVip.startCombine(player);
                case Nang_cap_canh ->
                NangCapCanh.NangCapCanh222(player); 
                
        }

        player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
        player.combine.clearParamCombine();
        player.combine.lastTimeCombine = System.currentTimeMillis();

    }

    public int getDiemNangcapChanmenh(int star) {
        switch (star) {
            case 0:
                return 100000;
            case 1:
                return 200000;
            case 2:
                return 300000;
            case 3:
                return 400000;
            case 4:
                return 500000;
            case 5:
                return 600000;
            case 6:
                return 700000;
            case 7:
                return 800000;
             case 8:
                return 900000;    
                
        }
        return 0;
    }
      public int getDiemNangcapcanh(int star) {
        switch (star) {
            case 0:
                return 20;
            case 1:
                return 50;
            case 2:
                return 70;
            case 3:
                return 90;
            case 4:
                return 150;
            case 5:
                return 200;
            case 6:
                return 250;
            case 7:
                return 380;
             case 8:
                return 500;    
                
        }
        return 0;
    }
         public int coinNangcapcanh(int star) {
        switch (star) {
            case 0:
                return 10000;
            case 1:
                return 18000;
            case 2:
                return 25000;
            case 3:
                return 34000;
            case 4:
                return 45000;
            case 5:
                return 50000;
            case 6:
                return 70000;
            case 7:
                return 90000;
             case 8:
                return 100000;    
                
        }
        return 0;
    }
    

    public int dnsdapdo(int star) {
        switch (star) {
            
              
            case 0:
                return 1000;
            case 1:
                return 2000;
              
            case 2:
                return 3000;
                 
            case 3:
                return 4000;
            case 4:
                return 5000;
            case 5:
                return 6000;
            case 6:
                return 7000;
            case 7:
                return 8000;
            case 8:
                return 9000;
            case 9:
                return 10000;
            case 10:
                return 11000;
            default:
                return 15000;
        }

    }

    public float getTiLeNangcapChanmenh(int star) {
        switch (star) {
            case 0:
                return 5f;
            case 1:
                return 3f;
            case 2:
                return 2f;
            case 3:
                return 1f;
            case 4:
                return 0.1f;
            case 5:
                return 0.5f;
            case 6:
                return 0.1f;
            case 7:
                return 0.1f;
            default:
                return 0.1f;
        }
    }
    public float getTiLeNangcapCanh(int star) {
        switch (star) {
            case 0:
                return 7;
            case 1:
                return 5f;
            case 2:
                return 2f;
            case 3:
                return 1f;
            case 4:
                return 0.8f;
            case 5:
                return 0.5f;
            case 6:
                return 0.3f;
            case 7:
                return 0.2f;
            default:
                return 0.1f;
        }
    }

    /**
     * Mở tab đập đồ
     *
     * @param player
     * @param type kiểu đập đồ
     */
    public void openTabCombine(Player player, int type) {
        player.combine.setTypeCombine(type);
        Message msg = null;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_TAB_COMBINE);
            msg.writer().writeUTF(getTextInfoTabCombine(type));
            msg.writer().writeUTF(getTextTopTabCombine(type));
            if (player.iDMark.getNpcChose() != null) {
                msg.writer().writeShort(player.iDMark.getNpcChose().tempId);
            }
            player.sendMessage(msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    /**
     * Hiệu ứng mở item
     *
     * @param player
     * @param icon1
     * @param icon2
     */
    public void sendEffectOpenItem(Player player, short icon1, short icon2) {
        Message msg = null;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_ITEM);
            msg.writer().writeShort(icon1);
            msg.writer().writeShort(icon2);
            player.sendMessage(msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public void sendEffectCombineItem(Player player, byte type, short icon1, short icon2) {
        Message msg = null;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(type);
            switch (type) {
                case 0:
                    msg.writer().writeUTF("");
                    msg.writer().writeUTF("");
                    break;
                case 1:
                    msg.writer().writeByte(0);
                    msg.writer().writeByte(-1);
                    break;
                case 2: // success 0 eff 0
                case 3: // success 1 eff 0
                    break;
                case 4: // success 0 eff 1
                    msg.writer().writeShort(icon1);
                    break;
                case 5: // success 0 eff 2
                    msg.writer().writeShort(icon1);
                    break;
                case 6: // success 0 eff 3
                    msg.writer().writeShort(icon1);
                    msg.writer().writeShort(icon2);
                    break;
                case 7: // success 0 eff 4
                    msg.writer().writeShort(icon1);
                    break;
                case 8: // success 1 eff 4
                    break;
            }
            msg.writer().writeShort(-1); // id npc
//            msg.writer().writeShort(-1); // x
//            msg.writer().writeShort(-1); // y
            player.sendMessage(msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    /**
     * Hiệu ứng đập đồ thành công
     *
     * @param player
     */
    public void sendEffectSuccessCombine(Player player) {
        Message msg = null;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(combineSUCCESS);
            player.sendMessage(msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    /**
     * Hiệu ứng đập đồ thất bại
     *
     * @param player
     */
    public void sendEffectFailCombine(Player player) {
        Message msg = null;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(combineFAIL);
            player.sendMessage(msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    /**
     * Gửi lại danh sách đồ trong tab combine
     *
     * @param player
     */
    public void reOpenItemCombine(Player player) {
        Message msg = null;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(REOPEN_TAB_COMBINE);
            msg.writer().writeByte(player.combine.itemsCombine.size());
            for (Item it : player.combine.itemsCombine) {
                for (int j = 0; j < player.inventory.itemsBag.size(); j++) {
                    if (it == player.inventory.itemsBag.get(j)) {
                        msg.writer().writeByte(j);
                    }
                }
            }
            player.sendMessage(msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    /**
     * Hiệu ứng ghép ngọc rồng
     *
     * @param player
     * @param icon
     */
    public void sendEffectCombineDB(Player player, short icon) {
        Message msg = null;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(combineDRAGON_BALL);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public void sendAddItemCombine(Player player, int npcId, Item... items) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Dragon Boy");
            msg.writer().writeUTF("Dragon Boy - Đẳng Cấp Là Mãi Mãi");
            msg.writer().writeShort(npcId);
            player.sendMessage(msg);
            msg.cleanup();
            msg = new Message(-81);
            msg.writer().writeByte(1);
            msg.writer().writeByte(items.length);
            for (Item item : items) {
                msg.writer().writeByte(InventoryService.gI().getIndexItemBag(player, item));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendEffSuccessVip(Player player, int iconID) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(7);
            msg.writer().writeShort(iconID);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendEffFailVip(Player player) {
        try {
            Message msg;
            msg = new Message(-81);
            msg.writer().writeByte(8);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    private String getTextTopTabCombine(int type) {
        return switch (type) {
            case EP_SAO_TRANG_BI ->
                "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở nên mạnh mẽ";
            case PHA_LE_HOA_TRANG_BI ->
                "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
            case CHUYEN_HOA_TRANG_BI_DUNG_VANG, CHUYEN_HOA_TRANG_BI_DUNG_NGOC ->
                "Lưu ý trang bị mới\nphải hơn trang bị gốc\n1 bậc";
            case NHAP_NGOC_RONG ->
                "Ta sẽ phù phép\ncho 7 viên Ngọc Rồng\nthành 1 viên Ngọc Rồng cấp cao";
            case NHAP_DA ->
                "Ta sẽ phù phép\ncho 10 mảnh đá vụn\ntrở thành 1 đá nâng cấp";
            case NANG_CAP_VAT_PHAM ->
                "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở nên mạnh mẽ";
            case NANG_CAP_BONG_TAI ->
                "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành cấp 2";
            case NANG_CHI_SO_BONG_TAI ->
                "Ta sẽ phù phép\ncho bông tai Porata cấp 2 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case NANG_CAP_SAO_PHA_LE ->
                "Ta sẽ phù phép\nnâng cấp Sao Pha Lê\nthành cấp 2";
            case DANH_BONG_SAO_PHA_LE ->
                "Đánh bóng\nSao pha lê cấp 2";
            case CUONG_HOA_LO_SAO_PHA_LE ->
                "Cường hóa\nÔ Sao Pha Lê";
            case TAO_DA_HEMATITE ->
                "Ta sẽ phù phép\ntạo đá hematite";
            case GIAM_DINH_SACH ->
                "Ta sẽ phù phép\ngiám định sách đó cho ngươi";
            case TAY_SACH ->
                "Ta sẽ phù phép\ntẩy sách đó cho ngươi";
            case NANG_CAP_SACH_TUYET_KY ->
                "Ta sẽ phù phép\nnâng cấp Sách Tuyệt Kỹ cho ngươi";
            case HOI_PHUC_SACH ->
                "Ta sẽ phù phép\nphục hồi sách cho ngươi";
            case PHAN_RA_SACH ->
                "Ta sẽ phù phép\nphân rã sách đó cho ngươi";
            case CHE_TAO_TRANG_BI_THIEN_SU ->
                "Chế tạo\ntrang bị thiên sứ";
            case LAM_PHEP_NHAP_DA ->
                "Ta sẽ phù phép\n"
                + "cho 10 mảnh đá vụn\n"
                + "trở thành 1 đá nâng cấp";
            case NANG_CAP_KICH_HOAT ->
                "Ta sẽ phù phép\nchế tạo trang bị Huỷ Diệt\nthành trang bị Kích Hoạt";
            case NANG_CAP_KICH_HOAT_VIP ->
                "Ta sẽ phù phép\nchế tạo trang bị Thiên Sứ\nthành trang bị Kích Hoạt Vip";
            case NANG_CAP_CHAN_MENH ->
                "chào sếp";
            case MO_CHI_SO_CAI_TRANG ->
                "chào sếp";
            case XOA_SPL ->
                "Chào Sếp";
                
           case DUC_LO_KHAM ->
                "Chào Sếp";     
           case KHAM_EP_DA ->
                "Chào Sếp"; 
                case Nang_cap_canh ->
                "Chào Sếp";      
                case nangcapruong ->
                "Chào Sếp";      
                
            default ->
                "";
        };
    }

    private String getTextInfoTabCombine(int type) {
        return switch (type) {
            case EP_SAO_TRANG_BI ->
                "Vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày, nhẫn) có ô đặt sao pha lê\nChọn loại sao pha lê\n"
                + "Cải trang / phụ kiện hãy dùng 'Ép Sao Phụ Kiện'\n"
                + "Ép Full Sét 18,30,45,65,99,200,300,500,999sao\n"
                + "Xuất Hiện Dòng SKH Siêu Vip\n"
                + "Chi Phí 10k Lượng Bạc\n"
                + "Sau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI ->
                "Vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày, nhẫn, cải trang, phụ kiện...)\n"
                + "Mọi trang bị đều pha lê hóa được\n"
                + "Số lỗ đục tối đa theo cấp VIP\n"
                + "Tỉ Lệ Đục Phụ Thuộc Vip Nữa\n"
                + "Tiêu Tốn Rất Nhiều Tài Nguyên\n"
                + "Sau đó chọn 'Nâng cấp'\n";
            case CHUYEN_HOA_TRANG_BI_DUNG_VANG, CHUYEN_HOA_TRANG_BI_DUNG_NGOC ->
                "Vào hành trang\nChọn trang bị gốc\n(Áo,quần,găng,giày hoặc rađa)\ntừ cấp [+4] trở lên\nChọn tiếp trang bị mới\nchưa nâng cấp cần nhập thể\nsau đó chọn 'Nâng cấp'";
            case NHAP_NGOC_RONG ->
                "Vào hành trang\nChọn 7 viên ngọc cùng sao\n"
                + "Chi Phí 1k Lượng Bạc\n"
                + "\nSau đó chọn 'Làm phép'";
            case NHAP_DA ->
                "Vào hành trang\nChọn 10 mảnh đá vụn\nChọn 1 bình nước phép\n(mua tại Uron ở trạm tàu vũ trụ)\nSau đó chọn 'Làm phép'";
            case NANG_CAP_VAT_PHAM ->
                "Vào hành trang\nChọn trang bị\n(Áo,quần,găng,giày hoặc rađa)\nChọn loại đá để nâng cấp\nSau đó chọn 'Nâng cấp'\n"
                + "Đập Full +16 Sẽ Thêm Dòng Kích Hoạt Bộ 100%stcm";
            case NANG_CAP_BONG_TAI ->
                "Vào hành trang\nChọn bông tai Porata\nChọn mảnh bông tai để nâng cấp, số lượng 9999 cái\nSau đó chọn 'Nâng cấp'";
            case NANG_CHI_SO_BONG_TAI ->
                "Vào hành trang\nChọn bông tai Porata\nChọn mảnh hồn porata số lượng 9999\ncái và đá xanh lam để nâng cấp.\nCần 85k Điểm Fam\nSau đó chọn 'Nâng cấp chỉ số'";
            case NANG_CAP_SAO_PHA_LE ->
                "Vào hành trang\nChọn đá Hematite\nChọn loại sao pha lê (cấp 1)\nSau đó chọn 'Nâng cấp'";
            case DANH_BONG_SAO_PHA_LE ->
                "Vào hành trang\nChọn loại sao pha lê cấp 2 có từ 2 viên trở lên\nChọn 1 đá mài\nSau đó chọn 'Đánh bóng'";
            case CUONG_HOA_LO_SAO_PHA_LE ->
                "Vào hành trang\nChọn trang bị có Ô sao thứ 8 trở lên chưa cường hóa\nChọn đá Hematite\nChọn dùi đục\nSau đó chọn 'Cường hóa'";
            case TAO_DA_HEMATITE ->
                "Vào hành trang\nChọn 5 sao pha lê cấp 2 cùng màu\nChọn 'Tạo đá Hematite'";
            case GIAM_DINH_SACH ->
                "Vào hành trang chọn\n1 sách cần giám định";
            case TAY_SACH ->
                "Vào hành trang chọn\n1 sách cần tẩy";
            case NANG_CAP_SACH_TUYET_KY ->
                "Vào hành trang chọn\nSách Tuyệt Kỹ 1 cần nâng cấp và 10 Kìm bấm giấy";
            case HOI_PHUC_SACH ->
                "Vào hành trang chọn\nCác Sách Tuyệt Kỹ cần phục hồi";
            case PHAN_RA_SACH ->
                "Vào hành trang chọn\n1 sách cần phân rã";
            case CHE_TAO_TRANG_BI_THIEN_SU ->
                "Cần 1 công thức\nMảnh trang bị tương ứng\n1 đá nâng cấp (tùy chọn)\n1 đá may mắn (tùy chọn)";
            case LAM_PHEP_NHAP_DA ->
                "Vào hành trang\n"
                + "Chọn 10 mảnh đá vụn\n"
                + "Chọn 1 bình nước phép\n"
                + "(mua tại Uron ở trạm tàu vũ trụ)\n"
                + "Sau đó chọn 'Làm phép'";
            case NANG_CAP_KICH_HOAT ->
                "Vào hành trang\nChọn 1 trang bị Huỷ Diệt\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_KICH_HOAT_VIP ->
                "Vào hành trang\nChọn 1 trang bị Thiên Sứ\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_CHAN_MENH ->
                "Chọn Chân Mệnh Lv1 Và đá Ngũ sắc\n"
                + "\nYêu cầu cần:\n1k Đá Ngũ Sắc Trở Lên Fam Tại Map Đá quý \n"
                + "100k Điểm Fam Trở Lên\n"
                + " TrainFam Từ ALL quái Các Map\n"
                + "Nhận Chân Mệnh Lv1 Mua Trên Shop\n"
                + "Chỉ Số Mở 1000%/lV Nâng\n"
                + "Sau đó chọn 'Nâng cấp'";
                  case Nang_cap_canh ->
                "Chọn Cánh Lv1 Và Thỏi Hàn Thiết\n"
                + "\nYêu cầu cần:\n10k Thỏi Hàn Thiết Trở Lên Săn Boss Rơi\n"
                + "10 Điểm Săn Boss Trở Lên\n"
                + "Săn ALL Boss Các Map\n"
                + "Săn Boss Để Nhận Cánh Lv1\n"
                + "Chỉ Số Mở 1000%/lV Nâng Chỉ Số Cánh VIP\n"
                + "Sau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_CAI_TRANG ->
                 "•Hello•\n"
                + "Chọn Cải Trang\n(Yêu Cầu Có dòng kích hoạt)\n"
                + "Yêu cầu: cần 20 Lượng Vàng Mở 1 Lần\n"
                + "Ramdom chỉ số Cải Trang 1-100k% HP,KI,SD,CM\n"
                + "Chắc Chắn Có Tỉ Lệ Vĩnh Viễn Nhé Và Ramdom\n"
                + "Sau đó chọn 'Nâng cấp'";
            case XOA_SPL ->
                "Chọn Trang Bị,Cải Trang Muốn Gỡ Lỗ\n"
                + "\nYêu cầu cần 10k Viên Hỏa Băng \n"
                + "\nHỏa Băng Có Thể Săn Boss Hoặc Nạp Ngày\n"
                + "Chúng Tôi Tiến Hành Gỡ Lỗ Bạn Đã Ép\n"
                + " Giữ Nguyên Chỉ Số Đã Ép Và Ép 1 Sao Mới OKKK\n"
                + "Sau đó chọn 'Nâng cấp'";
               case DUC_LO_KHAM ->
                "Chọn Trang Bị,Cải Trang Trở Lên\n"
                + "Đục Khảm Như Đục Sao Pha Lê \n"
                + "Yêu Cầu: 1 Lượng Vàng + Và 1 TV\n"
                + "Mỗi Lần Đập Tốn 1 Lượng Vàng + TV\n"
                + "Sau đó chọn 'Nâng cấp'\n"
                + "Max Đục 30Sao"; 
                
                 case KHAM_EP_DA ->
                "Chọn Trang Bị Từ Ô Thứ 6 Trở Lên\n"
                + "(Cải trang, Phụ kiện, Giáp tập luyện...)\n"
                + "Ép Đá Khảm, Sao Pha Lê Hoặc Sách Ép Premium\n"
                + "Chi Phí: 100k Lượng Bạc + 1 Lượng Vàng\n"
                + "(Tự trừ trong hành trang, không cần kéo vào ô)\n"
                + "Chưa có lỗ thì Pha Lê Hóa trước\n"
                + "Sau đó chọn 'Nâng cấp'";     
                       case nangcapruong ->
                "Chọn 7 Rương Thần Bí 1Sao Hoặc sao cao Hơn\n"
                + "Nâng Cấp Có Tỉ Lệ Lên Rương Cao hơn \n"
                + "giá trị Trong Rương Càng Ngon\n"
                + "Yêu Cầu: Free"
                + "Sau đó chọn 'Làm Phép'";     
                   
                   
            default ->
                "";
        };
    }

}
