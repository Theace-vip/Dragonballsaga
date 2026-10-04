package services.func;

import clan.Clan;
import clan.ClanMember;
import jdbc.DBConnecter;
import consts.ConstNpc;
import item.Item;
import item.Item.ItemOption;
import map.Zone;
import minigame.cost.LuckyNumberCost;
import minigame.LuckyNumber.LuckyNumberService;
import npc.Npc;
import npc.NpcManager;
import player.Player;
import network.Message;
import network.inetwork.ISession;
import server.Client;
import services.Service;
import models.GiftCode.GiftCodeService;
import services.InventoryService;
import services.ItemService;
import services.NpcService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jdbc.daos.PlayerDAO;

import player.Inventory;
import server.Manager;
import services.ClanService;
import utils.Util;

public class Input {

    private static final Map<Integer, Object> PLAYER_ID_OBJECT = new HashMap<>();

    public static final int CHANGE_PASSWORD = 500;
    public static final int GIFT_CODE = 501;
    public static final int FIND_PLAYER = 502;
    public static final int CHANGE_NAME = 503;
    public static final int doingocxanh = 67331;
    public static final int doihongngoc = 67332;
    public static final int doico4la = 67333;
    public static final int CHOOSE_LEVEL_BDKB = 504;
    public static final int NAP_THE = 505;
    public static final int CHANGE_NAME_BY_ITEM = 506;

    public static final int CHATALL = 5211;
    public static final int CHATallplayer = 5212;
    public static final int DANGKY = 509;
    public static final int SEND_ITEM_OP = 507;
    public static final int SEND_ITEM = 512;
    public static final int CHOOSE_LEVEL_KGHD = 510;
    public static final int CHOOSE_LEVEL_CDRD = 511;
    public static final int DISSOLUTION_CLAN = 513;
    public static final int tangdiem = 1265;

    public static final int SELECT_LUCKYNUMBER = 514;

    public static final int TAO_PET = 515;

    public static final byte NUMERIC = 0;
    public static final byte ANY = 1;
    public static final byte PASSWORD = 2;
    public static final byte MBV = 23;
    public static final byte BANSLL = 24;
    public static final byte BANGHOI = 25;
    public static final byte BANSLLthiendao = 26;
    public static final byte BANSLLdiadao = 27;
    public static final byte BANSLLKnb = 28;
    public static final byte BANSLLKnbac = 29;

    private static Input intance;

    private Input() {

    }

    public static Input gI() {
        if (intance == null) {
            intance = new Input();
        }
        return intance;
    }

    public void doInput(Player player, Message msg) {
        try {
            String[] text = new String[msg.reader().readByte()];
            for (int i = 0; i < text.length; i++) {
                text[i] = msg.reader().readUTF();
            }
            switch (player.iDMark.getTypeInput()) {
                case TAO_PET: {
                    if (player.tamkjllpet != null) {
                        Service.gI().sendThongBao(player, "Đẻ Lắm Thế!");
                        return;
                    }                    Item trung = InventoryService.gI().findItem(player.inventory.itemsBag,
                            457);
                    if (trung != null) {
                        String NamePet = text[0];
                        int tenMin = panel.tuning.SystemTuning.getI("embe_ten_toi_thieu");
                        int tenMax = panel.tuning.SystemTuning.getI("embe_ten_toi_da");
                        if (NamePet.length() < tenMin || NamePet.length() > tenMax) {
                            Service.gI().sendThongBao(player, "không ngắn hơn " + tenMin
                                    + " và dài hơn " + tenMax
                                    + " kí tự, Và cho phép kí tự đặt biệt.");
                            break;
                        }
                        player.TamkjllPetGiong = -1;
                        if (player.tamkjllpet != null) {
                            ChangeMapService.gI().exitMap(player.tamkjllpet);
                            player.tamkjllpet.dispose();
                            player.tamkjllpet = null;
                        }
                        player.CreatePet(NamePet);
                        int trungTru = panel.tuning.SystemTuning.getI("embe_trung_tru");
                        if (trungTru > 0) {
                            InventoryService.gI().subQuantityItem(player.inventory.itemsBag, trung, trungTru);
                        }
                        InventoryService.gI().sendItemBag(player);
                        Service.gI().sendThongBao(player, "Bạn đã thu nhận Pet: " + NamePet);
                    } else {
                        Service.gI().sendThongBao(player, "Cần có trứng pet");
                    }
                    break;
                }

                case tangdiem: {
                    // [Sua 5 loi Ve Tang Diem 27/09/2026]
                    // 1. chi cat ve khi chuyen THANH CONG (khong con mat ve khi huy form)
                    // 2. bo bug bat phai co > 100 trieu diem
                    // 3. min/max doc tren panel (fam_transfer_min / fam_transfer_max) = dung voi thong bao
                    // 4. ho tro tang cho nguoi OFFLINE (update truc tiep DB)
                    // 5. ghi lich su vao log/chuyen_diem_fam.log + thong bao dung chuc nang
                    if (text.length < 2) {
                        break;
                    }
                    String tenNhan = text[0] == null ? "" : text[0].trim();
                    long soDiem;
                    try {
                        soDiem = Long.parseLong(text[1].trim());
                    } catch (Exception e) {
                        Service.gI().sendThongBao(player, "Giá trị Điểm Fam không hợp lệ.");
                        break;
                    }

                    long minChuyen = 1000L;
                    long maxChuyen = 10000000L;
                    try {
                        int t = panel.tuning.SystemTuning.getI("fam_transfer_min");
                        if (t > 0) {
                            minChuyen = t;
                        }
                        t = panel.tuning.SystemTuning.getI("fam_transfer_max");
                        if (t > 0) {
                            maxChuyen = t;
                        }
                    } catch (Throwable ignored) {
                    }

                    if (tenNhan.isEmpty()) {
                        Service.gI().sendThongBao(player, "Bạn chưa nhập tên người nhận.");
                        break;
                    }
                    if (soDiem < minChuyen || soDiem > maxChuyen) {
                        Service.gI().sendThongBao(player, "Chỉ có thể chuyển từ " + Util.FormatNumber(minChuyen)
                                + " Đến " + Util.FormatNumber(maxChuyen) + " Điểm Fam mỗi lần.");
                        break;
                    }
                    if (tenNhan.equalsIgnoreCase(player.name)) {
                        Service.gI().sendThongBao(player, "Không thể tự tặng điểm cho chính mình.");
                        break;
                    }
                    if (soDiem > player.diemfam) {
                        Service.gI().sendThongBao(player, "Bạn không đủ Điểm Fam. Hiện có: " + Util.FormatNumber(player.diemfam));
                        break;
                    }

                    Item veTang = InventoryService.gI().findItemBag(player, 1251);
                    if (veTang == null || !veTang.isNotNullItem()) {
                        Service.gI().sendThongBao(player, "Cần có [Vé Tặng Điểm] trong túi để thực hiện.");
                        break;
                    }

                    Player nhanOnline = Client.gI().getPlayer(tenNhan);
                    boolean offline = false;
                    if (nhanOnline != null) {
                        player.diemfam -= soDiem;
                        nhanOnline.diemfam += soDiem;
                    } else {
                        // chi update DB khi chac chan nguoi choi khong dang online
                        int rows = 0;
                        try {
                            java.sql.Connection con = jdbc.DBConnecter.getConnectionServer();
                            java.sql.PreparedStatement ps = con.prepareStatement("UPDATE player SET diemfam = diemfam + ? WHERE name = ?");
                            ps.setLong(1, soDiem);
                            ps.setString(2, tenNhan);
                            rows = ps.executeUpdate();
                            ps.close();
                            con.close();
                        } catch (Exception e) {
                            Service.gI().sendThongBao(player, "Lỗi khi chuyển điểm, vui lòng thử lại.");
                            break;
                        }
                        if (rows == 0) {
                            Service.gI().sendThongBao(player, "Người chơi \"" + tenNhan + "\" không tồn tại.");
                            break;
                        }
                        player.diemfam -= soDiem;
                        offline = true;
                    }

                    // thanh cong moi cat ve
                    InventoryService.gI().subQuantityItemsBag(player, veTang, 1);
                    Service.gI().sendThongBao(player, "|7|Bạn đã tặng " + Util.FormatNumber(soDiem) + " Điểm Fam cho "
                            + (nhanOnline != null ? nhanOnline.name : tenNhan) + (offline ? " (offline)" : "") + ".");
                    if (nhanOnline != null) {
                        Service.gI().sendThongBao(nhanOnline, "|7|Bạn nhận " + Util.FormatNumber(soDiem)
                                + " Điểm Fam từ " + player.name + ".");
                    }
                    logChuyenDiemFam(player.name, nhanOnline != null ? nhanOnline.name : tenNhan, soDiem, offline);
                    break;
                }

                case SEND_ITEM_OP:
                    String name1 = text[0];
                    int id1 = Integer.parseInt(text[1]);
                    int q1 = Integer.parseInt(text[2]);
                    String option = text[3];
                    String param = text[4];
                    String[] option1 = option.split("-");
                    String[] param1 = param.split("-");
                    int length1 = option1.length;
                    int length2 = param1.length;
                    if (length1 == length2) {
                        if (Client.gI().getPlayer(name1) != null) {
                            Item item = ItemService.gI().createNewItem(((short) id1));
                            item.quantity = q1;
                            for (int i = 0; i < length1; i++) {
                                String option2 = option1[i];
                                String param2 = param1[i];
                                int opt;
                                int par;
                                try {
                                    opt = Integer.parseInt(option2);
                                    par = Integer.parseInt(param2);
                                    item.itemOptions.add(new ItemOption(opt, par));
                                    Service.getInstance().sendThongBaoFromAdmin(player, "|7|BUFF ITEM OPTION CHO " + name1 + " THÀNH CÔNG!");
                                } catch (NumberFormatException e) {
                                    break;
                                }

                            }
                            InventoryService.gI().addItemBag(Client.gI().getPlayer(name1), item, 999999999);
                            InventoryService.gI().sendItemBag(Client.gI().getPlayer(name1));
                            Service.getInstance().sendThongBaoOK(Client.gI().getPlayer(name1), "Nhận được [" + item.template.name + "] từ QTV " + player.name);
                        } else {
                            Service.getInstance().sendThongBao(player, "Không online");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Nhập dữ liệu không đúng");
                    }
                    break;
                case SEND_ITEM:
                    if (player.isAdmin()) {
                        int idItemBuff = Integer.parseInt(text[1]);
                        int quantityItemBuff = Integer.parseInt(text[2]);
                        Player pBuffItem = Client.gI().getPlayer(text[0]);
                        if (pBuffItem != null) {
                            String txtBuff = "Buff to player: " + pBuffItem.name + "\b";
                            if (idItemBuff == -1) {
                                pBuffItem.inventory.gold = Math.min(pBuffItem.inventory.gold + (long) quantityItemBuff, Inventory.LIMIT_GOLD);
                                txtBuff += quantityItemBuff + " vàng\b";
                                Service.getInstance().sendMoney(player);
                            } else if (idItemBuff == -2) {
                                pBuffItem.inventory.gem = Math.min(pBuffItem.inventory.gem + quantityItemBuff, 2000000000);
                                txtBuff += quantityItemBuff + " ngọc\b";
                                Service.getInstance().sendMoney(player);
                            } else if (idItemBuff == -3) {
                                pBuffItem.inventory.ruby = Math.min(pBuffItem.inventory.ruby + quantityItemBuff, 2000000000);
                                txtBuff += quantityItemBuff + " ngọc khóa\b";
                                Service.getInstance().sendMoney(player);
                            } else {
                                Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff);
                                itemBuffTemplate.quantity = quantityItemBuff;
                                InventoryService.gI().addItemBag(pBuffItem, itemBuffTemplate, 999999999);
                                InventoryService.gI().sendItemBag(pBuffItem);
                                txtBuff += "x" + quantityItemBuff + " " + itemBuffTemplate.template.name + "\b";
                            }
                            NpcService.gI().createTutorial(player, 24, txtBuff);
                            if (player.id != pBuffItem.id) {
                                NpcService.gI().createTutorial(pBuffItem, 24, txtBuff);
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Player không online");
                        }
                        break;
                    }
                    break;

                case CHANGE_PASSWORD:
                    Service.gI().changePassword(player, text[0], text[1], text[2]);
                    break;
                case GIFT_CODE:
                    GiftCodeService.gI().giftCode(player, text[0].toLowerCase());
                    break;

                case CHATALL:
                    String chat = text[0];
                    Service.gI().ChatAll(22713, "|7|[ - •⊹٭Tin Nhắn Kết Bạn Bồn Phương⊹• - ]" + "\n"
                            + "|4|Tin nhắn Từ Bạn\n"
                            + "|7|[" + player.name + "]\n"
                            + "|8|" + chat);
                    break;

                case CHATallplayer:
                    String chat1 = text[0];
                    Service.gI().sendThongBaoAllPlayer(""
                            + "|4|Tin nhắn Từ Bạn\n"
                            + "|7|[" + player.name + "]\n"
                            + "|8|" + chat1);
                    break;

                case FIND_PLAYER:
                    Player pl = Client.gI().getPlayer(text[0]);
                    if (pl != null) {
                        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_FIND_PLAYER, -1, "Ngài muốn..?",
                                new String[]{"Đi tới\n" + pl.name, "Gọi " + pl.name + "\ntới đây", "Đổi tên", "Ban", "Kick"},
                                pl);
                    } else {
                        Service.gI().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                    }
                    break;
                case CHANGE_NAME: {
                    Player plChanged = (Player) PLAYER_ID_OBJECT.get((int) player.id);
                    if (plChanged != null) {
                        if (DBConnecter.executeQuery("select * from player where name = ?", text[0]).next()) {
                            Service.gI().sendThongBao(player, "Tên nhân vật đã tồn tại");
                        } else {
                            plChanged.name = text[0];
                            DBConnecter.executeUpdate("update player set name = ? where id = ?", plChanged.name, plChanged.id);
                            Service.gI().player(plChanged);
                            Service.gI().Send_Caitrang(plChanged);
                            Service.gI().sendFlagBag(plChanged);
                            Zone zone = plChanged.zone;
                            ChangeMapService.gI().changeMap(plChanged, zone, plChanged.location.x, plChanged.location.y);
                            Service.gI().sendThongBao(plChanged, "Chúc mừng bạn đã có cái tên mới đẹp đẽ hơn tên ban đầu");
                            Service.gI().sendThongBao(player, "Đổi tên người chơi thành công");
                        }
                    }
                }
                break;
                case CHANGE_NAME_BY_ITEM: {
                    if (player != null) {
                        if (DBConnecter.executeQuery("select * from player where name = ?", text[0]).next()) {
                            Service.gI().sendThongBao(player, "Tên nhân vật đã tồn tại");
                            createFormChangeNameByItem(player);
                        } else if (Util.haveSpecialCharacter(text[0])) {
                            Service.gI().sendThongBaoOK(player, "Tên nhân vật không được chứa ký tự đặc biệt");
                        } else if (text[0].length() < 5) {
                            Service.gI().sendThongBaoOK(player, "Tên nhân vật quá ngắn");
                        } else if (text[0].length() > 10) {
                            Service.gI().sendThongBaoOK(player, "Tên nhân vật chỉ đồng ý các ký tự a-z, 0-9 và chiều dài từ 5 đến 10 ký tự");
                        } else {
                            Item theDoiTen = InventoryService.gI().findItem(player.inventory.itemsBag, 1665);
                            if (theDoiTen == null) {
                                Service.gI().sendThongBao(player, "Không tìm thấy thẻ đổi tên");
                            } else {
                                InventoryService.gI().subQuantityItemsBag(player, theDoiTen, 1);
                                player.name = text[0].toLowerCase();
                                DBConnecter.executeUpdate("update player set name = ? where id = ?", player.name, player.id);
                                Service.gI().player(player);
                                Service.gI().Send_Caitrang(player);
                                Service.gI().sendFlagBag(player);
                                Zone zone = player.zone;
                                ChangeMapService.gI().changeMap(player, zone, player.location.x, player.location.y);
                                Service.gI().sendThongBao(player, "Chúc mừng bạn đã có cái tên mới đẹp đẽ hơn tên ban đầu");
                            }
                        }
                    }
                }
                break;
                case CHOOSE_LEVEL_BDKB:
                    int level = Integer.parseInt(text[0]);
                    if (level >= 1 && level <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.QUY_LAO_KAME, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_BDKB,
                                    "Con có chắc muốn đến\nhang kho báu cấp độ " + level + " ?",
                                    new String[]{"Đồng ý", "Từ chối"}, level);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }

                    break;
                case CHOOSE_LEVEL_KGHD:
                    level = Integer.parseInt(text[0]);
                    if (level >= 1 && level <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.MR_POPO, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, 2,
                                    "Cậu có chắc muốn đến\nDestron Gas cấp độ " + level + " ?",
                                    new String[]{"Đồng ý", "Từ chối"}, level);
                        }
                    }
                    break;
                case CHOOSE_LEVEL_CDRD:
                    level = Integer.parseInt(text[0]);
                    if (level >= 1 && level <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.THAN_VU_TRU, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, 3,
                                    "Con có chắc muốn đến\ncon đường rắn độc cấp độ " + level + " ?",
                                    new String[]{"Đồng ý", "Từ chối"}, level);
                        }
                    }
                    break;

                case doingocxanh:
                    int goldTrade117 = Integer.parseInt(text[0]); // Số tiền VNĐ nhập vào
                    int heso17 = 10000; // 1 VNĐ = 10 gem, tức 1000 VNĐ = 10.000 gem
                    if (goldTrade117 % 1000 == 0) {
                        if (goldTrade117 <= 0 || goldTrade117 > 1000000) {
                            Service.getInstance().sendThongBao(player, "|7|Số tiền phải nằm trong khoảng 1.000 - 1.000.000 VNĐ");
                        } else if (player.getSession().vnd >= goldTrade117) {
                            Item thehongngoc = InventoryService.gI().findItemBag(player, 2002);
                            int gemSoLuong = goldTrade117 * heso17 / 1000; // Quy đổi VNĐ sang gem

                            Item gemItem = ItemService.gI().createNewItem((short) 77, gemSoLuong);
                            InventoryService.gI().addItemBag(player, gemItem, Math.min(gemSoLuong, 9999999L)); // Giới hạn số lượng
                            InventoryService.gI().sendItemBag(player);

                            Service.getInstance().sendThongBao(player, "Bạn nhận được\n " + gemSoLuong + " " + gemItem.template.name);

                            PlayerDAO.subcash(player, goldTrade117); // Trừ tiền VNĐ sau khi đổi gem
                        } else {
                            int soTienThieu = goldTrade117 - player.getSession().vnd;
                            Service.getInstance().sendThongBao(player, "|7|Bạn chỉ có " + player.getSession().vnd + " VNĐ, thiếu " + soTienThieu + " VNĐ để đổi " + (goldTrade117 / 1000) + " gem.");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "|7|Số tiền nhập phải chia hết cho 1000 VNĐ");
                    }
                    break;

                case doihongngoc:
                    int goldTrade118 = Integer.parseInt(text[0]); // Số tiền Cash nhập vào
                    if (goldTrade118 % 1000 == 0) {
                        if (goldTrade118 < 10000 || goldTrade118 > 100000) {
                            Service.getInstance().sendThongBao(player, "|7|Số tiền phải nằm trong khoảng 10.000 - 100.000 Cash");
                        } else if (player.getSession().vnd >= goldTrade118) {
                            // 100.000 Cash = 10.000 Ruby => 1 Cash = 0.1 Ruby
                            int gemSoLuong = goldTrade118 / 10; // quy đổi

                            Item gemItem = ItemService.gI().createNewItem((short) 861, gemSoLuong);
                            InventoryService.gI().addItemBag(player, gemItem, 99999999);
                            InventoryService.gI().sendItemBag(player);

                            Service.getInstance().sendThongBao(player, "Bạn nhận được\n " + gemSoLuong + " " + gemItem.template.name);

                            PlayerDAO.subcash(player, goldTrade118); // Trừ tiền Cash sau khi đổi
                        } else {
                            int soTienThieu = goldTrade118 - player.getSession().vnd;
                            Service.getInstance().sendThongBao(player, "|7|Bạn chỉ có " + player.getSession().vnd + " Cash, thiếu " + soTienThieu + " Cash để đổi " + (goldTrade118 / 10) + " Ruby.");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "|7|Số tiền nhập phải chia hết cho 1.000 Cash");
                    }
                    break;

                case doico4la: {
                    final long PRICE = 15_000L;           // 15k Cash = 1 item
                    final long MAX_INPUT = 1_000_000_000L;// Tối đa 1 tỷ/lần (tùy chỉnh)
                    final int ITEM_ID = 1270;
                    final int STACK_LIMIT = 2_000_000_000; // Giới hạn mỗi stack item (int an toàn)

                    long input = Long.parseLong(text[0]); // Số tiền người chơi nhập

                    // Yêu cầu tối thiểu 15k
                    if (input < PRICE || input > MAX_INPUT) {
                        Service.getInstance().sendThongBao(player,
                                "|7|Nhập từ " + PRICE + " đến " + MAX_INPUT + " Cash.");
                        break;
                    }

                    // Làm tròn lên tới bội số của 15.000
                    long qty = (input + PRICE - 1) / PRICE; // số item sau khi làm tròn
                    long charge = qty * PRICE;              // số tiền THỰC TRỪ (đã làm tròn lên)

                    // Ví phải đủ TRẢ charge, nếu không thì từ chối (không lỗ)
                    if (player.getSession().vnd < charge) {
                        long thieu = charge - player.getSession().vnd;
                        Service.getInstance().sendThongBao(player,
                                "|7|Bạn có " + player.getSession().vnd
                                + " Cash, thiếu " + thieu + " Cash để đổi " + qty + " vật phẩm (tính chẵn).");
                        break;
                    }

                    // Cấp item, chia lô để an toàn
                    long remain = qty;
                    while (remain > 0) {
                        int batch = (int) Math.min(remain, STACK_LIMIT);
                        Item it = ItemService.gI().createNewItem((short) ITEM_ID, batch);
                        InventoryService.gI().addItemBag(player, it, 9999999);
                        remain -= batch;
                    }
                    InventoryService.gI().sendItemBag(player);

                    // Trừ đúng số tiền đã làm tròn (thu đủ -> không lỗ)
                    PlayerDAO.subcash(player, (int) Math.min(charge, Integer.MAX_VALUE));

                    Service.getInstance().sendThongBao(player,
                            "|2|Đổi thành công\n(tính chẵn)!\n"
                            + "|7|Đã trừ: " + charge + " Cash\n"
                            + "|7|Nhận: " + qty + " " + ItemService.gI().getTemplate((short) ITEM_ID).name);
                    break;
                }

                case MBV:
                    int mbv = Integer.parseInt(text[0]);
                    int nmbv = Integer.parseInt(text[1]);
                    int rembv = Integer.parseInt(text[2]);
                    if ((mbv + "").length() != 6 || (nmbv + "").length() != 6 || (rembv + "").length() != 6) {
                        Service.gI().sendThongBao(player, "Trêu bố mày à?");
                    } else if (player.mbv == 0) {
                        Service.gI().sendThongBao(player, "Bạn chưa cài mã bảo vệ!");
                    } else if (player.mbv != mbv) {
                        Service.gI().sendThongBao(player, "Mã bảo vệ không đúng");
                    } else if (nmbv != rembv) {
                        Service.gI().sendThongBao(player, "Mã bảo vệ không trùng khớp");
                    } else {
                        player.mbv = nmbv;
                        Service.gI().sendThongBao(player, "Đổi mã bảo vệ thành công!");
                    }
                    break;
                case BANSLL:
                    int sltv = Integer.parseInt(text[0]);
                    long cost = (long) sltv * 500000000;
                    if (sltv < 0) {
                        Service.gI().sendThongBao(player, "Có cái dái");
                        return;
                    }
                    Item ThoiVang = InventoryService.gI().findItemBag(player, 457);
                    if (ThoiVang != null) {
                        if (ThoiVang.quantity < sltv) {
                            Service.gI().sendThongBao(player, "Bạn chỉ có " + ThoiVang.quantity + " Thỏi Vàng");
                        } else {
                            if (player.inventory.gold + cost > Inventory.LIMIT_GOLD) {
                                int slban = (int) ((Inventory.LIMIT_GOLD - player.inventory.gold) / 500000000);
                                if (slban < 1) {
                                    Service.gI().sendThongBao(player, "Vàng sau khi bán vượt quá giới hạn");
                                } else if (slban < 2) {
                                    Service.gI().sendThongBao(player, "Bạn chỉ có thể bán 1 Thỏi Vàng");
                                } else {
                                    Service.gI().sendThongBao(player, "Số lượng trong khoảng 1 tới " + slban);
                                }
                            } else {
                                InventoryService.gI().subQuantityItemsBag(player, ThoiVang, sltv);
                                InventoryService.gI().sendItemBag(player);
                                player.inventory.gold += cost;
                                Service.gI().sendMoney(player);
                                Service.gI().sendThongBao(player, "Đã bán " + sltv + " Thỏi Vàng thu được " + Util.numberToMoney(cost) + " vàng");
                                TransactionService.gI().cancelTrade(player);
                            }
                        }
                    }
                    break;

                case BANSLLKnb:
                    int slknb = Integer.parseInt(text[0]);
                    long costknb = (long) slknb * 10000;
                    if (slknb < 0) {
                        Service.gI().sendThongBao(player, "Có cái dái");
                        return;
                    }
                    Item luongvang = InventoryService.gI().findItemBag(player, 1270);
                    if (luongvang != null) {
                        if (luongvang.quantity < slknb) {
                            Service.gI().sendThongBao(player, "Bạn chỉ có " + luongvang.quantity + " Lượng Vàng");
                        } else {

                            InventoryService.gI().subQuantityItemsBag(player, luongvang, slknb);
                            InventoryService.gI().sendItemBag(player);
                            player.getSession().vnd += costknb;
                            PlayerDAO.updateVND(player);
                            Service.gI().sendMoney(player);
                            Service.gI().sendThongBaoFromAdmin(player, "Đã bán " + slknb + " Lượng Vàng\n thu được " + Util.numberToMoney(costknb) + " Cash");
                            TransactionService.gI().cancelTrade(player);

                        }
                    }
                    break;
                case BANSLLKnbac:
                    int slknbac = Integer.parseInt(text[0]);
                    long costknbac = (long) slknbac * 100;
                    if (slknbac < 0) {
                        Service.gI().sendThongBao(player, "Có cái dái");
                        return;
                    }
                    Item luongbac = InventoryService.gI().findItemBag(player, 1271);
                    if (luongbac != null) {
                        if (luongbac.quantity < slknbac) {
                            Service.gI().sendThongBao(player, "Bạn chỉ có " + luongbac.quantity + " Lượng Bạc");
                        } else {

                            InventoryService.gI().subQuantityItemsBag(player, luongbac, slknbac);
                            InventoryService.gI().sendItemBag(player);
                            player.inventory.gem += costknbac;
                            Service.gI().sendMoney(player);
                            Service.gI().sendThongBaoFromAdmin(player, "Đã bán " + slknbac + " Lượng Bạc \nthu được " + Util.numberToMoney(costknbac) + " Ngọc Xanh");
                            TransactionService.gI().cancelTrade(player);

                        }
                    }
                    break;

                case BANSLLthiendao: {
                    // Nhập: số lượng muốn dùng (text[0])
                    int sltv1;
                    try {
                        sltv1 = Integer.parseInt(text[0]);
                    } catch (Exception e) {
                        Service.gI().sendThongBao(player, "Nhập số lượng hợp lệ (ví dụ: 10)");
                        return;
                    }

                    if (sltv1 <= 0) {
                        Service.gI().sendThongBao(player, "Số lượng phải > 0");
                        return;
                    }

                    Item ThoiVang1 = InventoryService.gI().findItemBag(player, 1111);
                    if (ThoiVang1 == null || ThoiVang1.quantity < sltv1) {
                        int dangCo = (ThoiVang1 == null ? 0 : ThoiVang1.quantity);
                        Service.gI().sendThongBao(player, "Bạn chỉ có " + dangCo + " Thiên Đạo Pháp Tắc");
                        return;
                    }

                    // Tỷ lệ quy đổi: 1 Thỏi = 50.000 Pháp Tắc
                    long rate = 50_000L;

                    // Tính Pháp Tắc nhận được, chống tràn
                    long addPhapTac;
                    try {
                        addPhapTac = Math.multiplyExact((long) sltv1, rate);
                    } catch (ArithmeticException ex) {
                        addPhapTac = Long.MAX_VALUE; // nếu cực lớn thì coi như tràn
                    }

                    long current = Math.max(0L, player.PhapTac_ThienDao);
                    if (addPhapTac > Long.MAX_VALUE - current) {
                        addPhapTac = Long.MAX_VALUE - current; // chặn vượt quá giới hạn kiểu long
                    }

                    // Trừ vật phẩm
                    InventoryService.gI().subQuantityItemsBag(player, ThoiVang1, sltv1);
                    InventoryService.gI().sendItemBag(player);

                    // Cộng Thiên Đạo · Pháp Tắc
                    player.PhapTac_ThienDao = current + addPhapTac;

                    // Thông báo
                    Service.gI().sendThongBao(player,
                            "Đã dùng " + sltv1 + " Vp" + "\nnhận "
                            + Util.numberToMoney(addPhapTac) + " Thiên Đạo · Pháp Tắc. "
                            + "Tổng hiện tại: " + Util.numberToMoney(player.PhapTac_ThienDao));

                    // Nếu đang trade thì hủy để tránh lệch dữ liệu
                    TransactionService.gI().cancelTrade(player);
                    break;
                }
                case BANSLLdiadao: {
                    // Nhập: số lượng muốn dùng (text[0])
                    int sltv1;
                    try {
                        sltv1 = Integer.parseInt(text[0]);
                    } catch (Exception e) {
                        Service.gI().sendThongBao(player, "Nhập số lượng hợp lệ (ví dụ: 10)");
                        return;
                    }

                    if (sltv1 <= 0) {
                        Service.gI().sendThongBao(player, "Số lượng phải > 0");
                        return;
                    }

                    Item ThoiVang1 = InventoryService.gI().findItemBag(player, 1112);
                    if (ThoiVang1 == null || ThoiVang1.quantity < sltv1) {
                        int dangCo = (ThoiVang1 == null ? 0 : ThoiVang1.quantity);
                        Service.gI().sendThongBao(player, "Bạn chỉ có " + dangCo + "  Luân Hồi");
                        return;
                    }

                    // Tỷ lệ quy đổi: 1 Thỏi = 50.000 Pháp Tắc
                    long rate = 50_000L;

                    // Tính Pháp Tắc nhận được, chống tràn
                    long addPhapTac;
                    try {
                        addPhapTac = Math.multiplyExact((long) sltv1, rate);
                    } catch (ArithmeticException ex) {
                        addPhapTac = Long.MAX_VALUE; // nếu cực lớn thì coi như tràn
                    }

                    long current = Math.max(0L, player.DLbTamkjll);
                    if (addPhapTac > Long.MAX_VALUE - current) {
                        addPhapTac = Long.MAX_VALUE - current; // chặn vượt quá giới hạn kiểu long
                    }

                    // Trừ vật phẩm
                    InventoryService.gI().subQuantityItemsBag(player, ThoiVang1, sltv1);
                    InventoryService.gI().sendItemBag(player);

                    // Cộng Thiên Đạo · Pháp Tắc
                    player.DLbTamkjll = current + addPhapTac;

                    // Thông báo
                    Service.gI().sendThongBao(player,
                            "Đã dùng " + sltv1 + " Vp" + "\nnhận "
                            + Util.numberToMoney(addPhapTac) + " Địa Đạo Luân Hồi. "
                            + "Tổng hiện tại: " + Util.numberToMoney(player.DLbTamkjll));

                    // Nếu đang trade thì hủy để tránh lệch dữ liệu
                    TransactionService.gI().cancelTrade(player);
                    break;
                }

                case BANGHOI:
                    Clan clan = player.clan;
                    if (clan != null) {
                        ClanMember cm = clan.getClanMember((int) player.id);
                        if (clan.isLeader(player)) {
                            if (clan.canUpdateClan(player)) {
                                String tenvt = text[0];
                                if (!Util.haveSpecialCharacter(tenvt) && tenvt.length() > 1 && tenvt.length() < 5) {
                                    clan.name2 = tenvt;
                                    clan.update();
                                    Service.gI().sendThongBao(player, "[" + tenvt + "] OK");
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Chỉ chấp nhận các ký tự a-z, 0-9 và chiều dài từ 2 đến 4 ký tự");
                                }
                            }
                        }
                    }
                    break;
                case DISSOLUTION_CLAN:
                    String xacNhan = text[0];
                    if (xacNhan.equalsIgnoreCase("OK")) {
                        clan = player.clan;
                        if (clan.isLeader(player)) {
                            clan.deleteDB(clan.id);
                            Manager.CLANS.remove(clan);
                            player.clan = null;
                            player.clanMember = null;
                            ClanService.gI().sendMyClan(player);
                            ClanService.gI().sendClanId(player);
                            Service.gI().sendThongBao(player, "Bang hội đã giải tán thành công.");
                        }
                    }
                    break;
                case SELECT_LUCKYNUMBER: {
                    int number = Integer.parseInt(text[0]);
                    LuckyNumberService.addNumber(player, number);
                }
                break;
            }
        } catch (Exception e) {
        }
    }

    public void createForm(Player pl, int typeInput, String title, SubInput... subInputs) {
        pl.iDMark.setTypeInput(typeInput);
        Message msg = null;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(subInputs.length);
            for (SubInput si : subInputs) {
                msg.writer().writeUTF(si.name);
                msg.writer().writeByte(si.typeInput);
            }
            pl.sendMessage(msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public void createForm(ISession session, int typeInput, String title, SubInput... subInputs) {
        Message msg = null;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(subInputs.length);
            for (SubInput si : subInputs) {
                msg.writer().writeUTF(si.name);
                msg.writer().writeByte(si.typeInput);
            }
            session.sendMessage(msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public void createFormChangePassword(Player pl) {
        createForm(pl, CHANGE_PASSWORD, "Đổi mật khẩu", new SubInput("Mật khẩu cũ", PASSWORD),
                new SubInput("Mật khẩu mới", PASSWORD),
                new SubInput("Nhập lại mật khẩu mới", PASSWORD));
    }

    public void createFormQDhongngoc(Player pl) {

        createForm(pl, doihongngoc, "Quy Đổi ruby", new SubInput("Nhập số lượng muốn đổi(1000 Cash = 1000 ruby)", NUMERIC));
    }

    public void createFormQDco4la(Player pl) {

        createForm(pl, doico4la, "Quy Đổi Lượng Vàng", new SubInput("Nhập số lượng muốn đổi(15k Cash = 1 lượng Vàng)", NUMERIC));
    }

    public void createFormQDngocxanh(Player pl) {

        createForm(pl, doingocxanh, "Quy Đổi nx", new SubInput("Nhập số lượng muốn đổi(1000 Cash = 10.000 nx)", NUMERIC));
    }

    public void ChatAll(Player pl) {
        createForm(pl, CHATALL, "CHAT ALL PLAYER POPUP", new SubInput("Chat NÈ", ANY));
    }

    public void Chatallplayer(Player pl) {
        createForm(pl, CHATallplayer, "CHAT TỚI ALL NGƯỜI CHƠI CÓ MẶT", new SubInput("Chat NÈ", ANY));
    }

    public void createFormSenditem(Player pl) {
        createForm(pl, SEND_ITEM, "SEND ITEM",
                new SubInput("Tên người chơi", ANY),
                new SubInput("ID item", NUMERIC),
                new SubInput("Số lượng", NUMERIC));
    }

    public void TAOPET(Player pl) {
        createForm(pl, TAO_PET, "Sau khi Chịch Chịch Có Con Hãy Đặt Tên Nó Đi", new SubInput("Tên Em Bé", ANY));
    }

    public void createFormSenditem1(Player pl) {
        createForm(pl, SEND_ITEM_OP, "BUFF ITEM KÈM LIST OPTION",
                new SubInput("Player Name", ANY),
                new SubInput("Id Item", ANY),
                new SubInput("Quantity", ANY),
                new SubInput("Id Option (List sau dấu -)", ANY),
                new SubInput("Param Option (List sau dấu -)", ANY));
    }

    public void createFormGiftCode(Player pl) {
        createForm(pl, GIFT_CODE, "GiftCode [Trên Website]", new SubInput("Giftcode", ANY));
    }

    public void createFormMBV(Player pl) {
        createForm(pl, MBV, "Đồ ngu! Đồ ăn hại! Cút mẹ mày đi!", new SubInput("Nhập Mã Bảo Vệ Đã Quên", NUMERIC), new SubInput("Nhập Mã Bảo Vệ Mới", NUMERIC), new SubInput("Nhập Lại Mã Bảo Vệ Mới", NUMERIC));
    }

    public void createFormBangHoi(Player pl) {
        createForm(pl, BANGHOI, "Nhập tên viết tắt bang hội", new SubInput("Tên viết tắt từ 2 đến 4 kí tự", ANY));
    }

    public void createFormFindPlayer(Player pl) {
        createForm(pl, FIND_PLAYER, "Tìm kiếm người chơi", new SubInput("Tên người chơi", ANY));
    }

    public void createFormNapThe(Player pl, byte loaiThe) {
        pl.iDMark.setLoaiThe(loaiThe);
        createForm(pl, NAP_THE, "Nạp thẻ", new SubInput("Mã thẻ", ANY), new SubInput("Seri", ANY));
    }

    public void createFormChangeName(Player pl, Player plChanged) {
        PLAYER_ID_OBJECT.put((int) pl.id, plChanged);
        createForm(pl, CHANGE_NAME, "Đổi tên " + plChanged.name, new SubInput("Tên mới", ANY));
    }

    public void createFormChangeNameByItem(Player pl) {
        createForm(pl, CHANGE_NAME_BY_ITEM, "Đổi tên " + pl.name, new SubInput("Tên mới", ANY));
    }

    public void createFormChooseLevelBDKB(Player pl) {
        createForm(pl, CHOOSE_LEVEL_BDKB, "Hãy chọn cấp độ hang kho báu từ 1-110", new SubInput("Cấp độ", NUMERIC));
    }

    public void phieutangdiem(Player pl) {
        createForm(pl, tangdiem, "Tặng Điểm Fam\nNhập tên người nhận và số điểm (mỗi lần 1 vé)",
                new SubInput("Nhập tên người nhận", ANY), new SubInput("Nhập số Điểm Fam", NUMERIC));
    }

    /** Ghi lich su tang diem fam vao log/chuyen_diem_fam.log (moi dong 1 giao dich). */
    private static void logChuyenDiemFam(String gui, String nhan, long soDiem, boolean offline) {
        try {
            java.io.File dir = new java.io.File("log");
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            java.io.FileWriter fw = new java.io.FileWriter("log/chuyen_diem_fam.log", true);
            fw.append(utils.TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss")
                    + " | gui: " + gui
                    + " | nhan: " + nhan
                    + " | so diem: " + soDiem
                    + " | offline: " + offline + "\n");
            fw.close();
        } catch (Exception ignored) {
        }
    }

    public void createFormChooseLevelCDRD(Player pl) {
        createForm(pl, CHOOSE_LEVEL_CDRD, "Hãy chọn cấp độ từ 1-110", new SubInput("Cấp độ", NUMERIC));
    }

    public void createFormChooseLevelKGHD(Player pl) {
        createForm(pl, CHOOSE_LEVEL_KGHD, "Hãy chọn cấp độ từ 1-110", new SubInput("Cấp độ", NUMERIC));
    }

    public void createFormBanSLL(Player pl) {
        createForm(pl, BANSLL, "Bạn muốn bán bao nhiêu [Thỏi Vàng] ?\n"
                + "1 Thỏi Vàng = 500M Vàng - Có Thể Săn Từ Boss Hoặc Up Vàng ALL quái", new SubInput("Số lượng", NUMERIC));
    }

    public void createFormBanSLLKnb(Player pl) {
        createForm(pl, BANSLLKnb, "Bạn muốn bán bao nhiêu [Lượng Vàng] ?\n"
                + "1 Lượng Vàng = 10.000 Cash - Có Thể Nạp Đổi 12.000 Cash/Lượng",
                new SubInput("Số lượng", NUMERIC));
    }

    public void createFormBanSLLKnbac(Player pl) {
        createForm(pl, BANSLLKnbac, "Bạn muốn bán bao nhiêu [Lượng Bạc] ?\n"
                + "1 Lượng Bạc = 100 Ngọc Xanh - Có Thể Up quái 1 Con/Lượng",
                new SubInput("Số lượng", NUMERIC));
    }

    public void createFormBanSLLthiendao(Player pl) {
        createForm(pl, BANSLLthiendao, "Bạn muốn Sử Dụng Bao Nhiêu [Thiên Đạo Pháp Tác] ?\n"
                + "1 Cái = 50k Pháp Tắc - Có Thể Săn Từ Boss Hoặc Up ALL quái\n"
                + "\nĐi Up Bí Kíp Để Đổi Cải Trang Thỏ 1M % Up Pháp Tắc", new SubInput("Số lượng", NUMERIC));
    }

    public void createFormBanSLLdiadao(Player pl) {
        createForm(pl, BANSLLdiadao, "Bạn muốn Sử Dụng Bao Nhiêu [Địa Đạo Luân Hồi] ?\n"
                + "1 Cái = 50k Luân Hồi - Có Thể Săn Từ Boss Hoặc Up ALL quái\n"
                + "\nĐi Up Bí Kíp Để Đổi Cải Trang Thỏ 1M % Up Luân Hồi", new SubInput("Số lượng", NUMERIC));
    }

    public void createFormGiaiTanBangHoi(Player pl) {
        createForm(pl, DISSOLUTION_CLAN, "Nhập OK để xác nhận giải tán bang hội.", new SubInput("", ANY));
    }

    public void createFormSelectOneNumberLuckyNumber(Player pl, boolean isGem) {
        String text = "";
        if (isGem) {
            text = "Hãy chọn 1 số từ 0 đến 99 giá " + Util.numberFormat(LuckyNumberCost.costPlayGem) + " ngọc";
        } else {
            text = "Hãy chọn 1 số từ 0 đến 99 giá " + Util.numberFormat(LuckyNumberCost.costPlayGold) + " vàng";
        }
        createForm(pl, SELECT_LUCKYNUMBER, text, new SubInput("Số bạn chọn", NUMERIC));
    }

    public static class SubInput {

        private String name;
        private byte typeInput;

        public SubInput(String name, byte typeInput) {
            this.name = name;
            this.typeInput = typeInput;
        }
    }

}
