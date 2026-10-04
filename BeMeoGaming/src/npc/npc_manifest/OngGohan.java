package npc.npc_manifest;

/**
 *
 */
import consts.ConstNpc;
import consts.ConstTaskBadges;
import item.Item;

import java.util.ArrayList;
import java.util.List;
import jdbc.DBConnecter;

import jdbc.daos.PlayerDAO;
import npc.Npc;
import panel.tuning.SystemTuning;
import player.Player;
import server.Client;
import services.InventoryService;
import services.ItemService;
import services.NpcService;
import services.PetService;
import services.Service;
import services.TaskService;
import services.func.Input;
import Mail.HomThuService;
import task.Badges.BadgesTaskService;
import utils.InputHoanTien;
import utils.Util;

public class OngGohan extends Npc {

    public OngGohan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    int costNapVang = 1;

    public static int getGiaVip(int vipLevel) {
        if (vipLevel < 1 || vipLevel > 13) {
            return 0;
        }
        // Gia VIP lay tu panel (tab He Thong, nhom "VIP")
        return SystemTuning.getI("vip_gia_" + vipLevel);
    }

    int[][] napVang = {{20000, 20000}, {50000, 60000}, {100000, 150000}, {500000, 800000}, {1000000, 2000000}, {2000000, 6000000}, {5000000, 50000000}};

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "|7|˚₊· ͟͟͞͞➳❥Xin Chào Anh Chị Đến Với Hòn Đảo━╬٨ـﮩﮩ❤٨ـﮩﮩـ╬━\n"
                        + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                        + "|6|ID Tên NV: " + (player.getSession().player.name) + " \n"
                        + "ID Đăng Nhập : " + (player.getSession().uu) + " \n"
                        + "Mở Thành Viên 20k Nhận 1 Đệ VIP Gohan\n"
                                + "Bán Source Nro Này Full Dame Ảo\n"
                                + "Liên Hệ  AD: 0836935740\n"
                        //  + "Số Người Online : " + Client.gI().getPlayers().size() + "0 Người\n"
                        + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n",
                        "Nhập\nGiftCode",
                        "Đổi\nThỏi Vàng",
                          "Nhận\nĐệ Tử",
                        "Mở TV\n20K",
                        "Nhận quà\nNạp Đầu",
                        "Nhận\nThư",
                        "Mua Thẻ\nVIP ");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0: // mã quà tặng
                        Input.gI().createFormGiftCode(player);
                        break;
                    case 1: // nạp tiền
                        String npcSay = "|7|•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                + "˚₊· ͟͟͞͞➳❥Số dư Cash: " + Util.numberToText(player.getSession().vnd) + " Cash\n|6|dùng để Đổi Vàng Ngọc\n"
                                + "|7|✎Kho Cất giữ " + Util.numberToText(player.getSession().goldBar) + " Thỏi Vàng\n"
                                + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n";
                        createOtherMenu(player, ConstNpc.NAP_TIEN, npcSay,
                                "Đổi Thỏi Vàng",
                                "Nhận\nThỏi Vàng",
                              //  "Đổi\nCỏ 4 Lá",
                              //  "Đổi\nNgọc",
                            //    "Đổi\nRuby",
                                "Đóng");
                        break;
                    case 2:
                        if (player.pet == null) {
                            PetService.gI().createMabuPet(player);
                            Service.gI().sendThongBao(player, "✎Bạn vừa nhận được đệ tử Mabu");
                        } else {
                            this.npcChat(player, "✎Bạn đã có rồi");
                        }
                        //  this.npcChat(player, "✎Tìm Super Broly Săn Đệ Broly Nhé");
                        break;
////
                    case 3:
                        if (player.getSession().actived == true) {
                            Service.getInstance().sendThongBao(player, "|4|Bạn đã mở thành viên rồi mà. Tiếp tục chơi game thui nào!!!!");
                            return;
                        }
                        if (player.getSession().vnd < 20000 && player.getSession().actived == false) {
                            Service.getInstance().sendThongBaoFromAdmin(player, "|3|Tài khoản của bạn không đủ 20.000 Cash. Vui lòng liên hệ ADMIN !\nMở 0đ Cash Tặng 1 Đại Lễ VIP 500k Bạc");
                            return;
                        }
                        if (player.getSession().vnd >= 20000 && player.getSession().actived == false) {
                            Item thoivang = ItemService.gI().createNewItem((short) 1639, 1);
                            InventoryService.gI().addItemBag(player, thoivang, 2000000000000000L);
                            InventoryService.gI().sendItemBag(player);
                            Service.getInstance().sendMoney(player);
                            Service.getInstance().point(player);
                            try {
                                player.getSession().actived = true;
                                PlayerDAO.subcash(player, 20000);
                                DBConnecter.executeUpdate("update account set active = 1 where id = " + player.getSession().userId);
                                Service.getInstance().sendThongBao(player, "|2|Bạn đã mở thành viên 20k Cash. Đã mở khóa chức năng Giao dịch và Nhận Bào lễ quà Vip!!");
                             //   Service.getInstance().sendThongBaoAllPlayer("|2|Bạn \n[" + player.name + "]\nđã mở thành viên VIP.\nNhận 10M Thỏi Vàng\nĐã mở khóa chức năng Giao dịch ALL Cải trang Pét !!");
                            } catch (Exception e) {
                                System.out.println("Loi chuc nang mo thanh vien");
                            }
                        }

                        break;
                    case 4:
                        NpcService.gI().createMenuConMeo(player, ConstNpc.NapDau, -1,
                                "|7|Nhận quà Nạp Đầu Game Trị giá 10.000%\n"
                                + "|6|Nạp Bất kỳ Mệnh giá Đầu Game Sau Đây\n"
                                + "|7|50k-200k-500k-1M-2M Cash\n"
                                + "|4|Phần quà Nhận 1 Lần Dành Cho Tân Thủ\n"
                                + "|6|Phần quà : 1 Sét Trang Bị Thần Long VIP PRO\n"
                                + "|4|Max Chỉ Số: SKH Siêu VIP Tùy Bạn quyết !\n"
                                + "|7|Đã Nạp Tổng: " + Util.numberToText(player.getSession().tongnap) + " Cash",
                                "Nhận quà\n50.000\nCash", "Nhận quà\n200.000\nCash", "Nhận quà\n500.000\nCash", "Nhận quà\n1.000.000\nCash", "Nhận quà\n2.000.000\nCash");

                        break;
//                          case 4:
//                       NpcService.gI().createBigMessage(player, avartar, "Ấn Mở Đập Trứng Web Nạp Tự Động\bĐập Free Hoặc Mất Phí\b"
//                                + "|7|Số Dư " + Util.numberToText(player.getSession().vnd) + " VND",
//                                (byte) 1, "Mở Ngay", 
//                                "https://dragonballsaga.vn/Event/daptrung.php");
//                        break;
//                    case 3:
//                        if (player.playerTask.taskMain.id < 19) {
//                            player.playerTask.taskMain.id = 19;
//                            TaskService.gI().sendNextTaskMain(player);
//                        } else {
//                            this.npcChat(player, "✎Mày Không Thể Next quá Nv ");
//                        }
//                        break;

//                    case 4:
//                        NpcService.gI().createMenuConMeo(player, ConstNpc.HDTANTHU, -1,
//                                "|4|Hưỡng Dẫn Sử Dụng Và Liên Quanღ\n"
//                                + "Lưu Ý: Không Đọc Thì Ko Biết Gì\n"
//                                + "Hưỡng Dẫn Cơ Bản Cần Lưu Y",
//                                "Tân Thủ", "Đâp Đồ", "Đóng"
//                        );
//                        break;
                    case 5:
                        try {
                            HomThuService.gI().openMailHub(player);
                        } catch (Exception e) {
                            try { Service.gI().sendThongBao(player, "Khong mo duoc hom thu"); } catch (Exception ex) {}
                        }
                        break;
                    case 6:
                        this.createOtherMenu(player, 0, "|7|˚₊· ͟͟͞͞➳❥KÍCH HOẠT VIP VĨNH VIỄN\n"
                                + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                + "|2|✎Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " Cash"
                                + "\n|5|✎Trạng Thái MTV : " + (player.getSession().actived == false ? "Chưa kích hoạt" : "Đã kích hoạt")
                                + "\n|7|✎TRẠNG THÁI : VIP " + player.Saga_VIP
                                + "\n✎ĐẶC QUYỀN HOÀN " + SystemTuning.getI("vip_hoan") + "% PHÍ VIP CŨ\n"
                                + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                + (player.timevip > 0 ? "\n|5|Hạn còn : " + "Vĩnh Viễn" : ""), "Kích Hoạt\nVIP", "Lịch Sử\nNâng Vip");

                        break;

                }
            } else if (player.iDMark.getIndexMenu() == 0) {
                switch (select) {
                    case 0:
                        this.createOtherMenu(player, 1, "|7|˚₊· ͟͟͞͞➳❥MUA VIP VĨNH VIỄN\n"
                                + "•❅────✧❅✦❅✧────❅••❅────✧❅✦❅✧────❅•\n"
                                + "|2|✎Số Dư hiện tại : " + Util.format(player.getSession().vnd) + " Cash"
                                + "\n|7|✎TRẠNG THÁI : VIP " + player.Saga_VIP
                                + "\n✎ĐẶC QUYỀN HOÀN " + SystemTuning.getI("vip_hoan") + "% PHÍ VIP CŨ\n"
                                //  + "\n|7|✎Điểm Vip :  " + Util.FormatNumber(player.point_vip)
                                + (player.timevip > 0 ? "\n|5|Hạn còn : " + "Vĩnh Viễn" : ""),
                                "VIP1\n" + Util.numberToText(SystemTuning.getI("vip_gia_1")) + "\n Cash",
                                "VIP2\n" + Util.numberToText(SystemTuning.getI("vip_gia_2")) + "\nCash",
                                "VIP3\n" + Util.numberToText(SystemTuning.getI("vip_gia_3")) + "\n Cash",
                                "VIP4\n" + Util.numberToText(SystemTuning.getI("vip_gia_4")) + "\n Cash",
                                "VIP5\n" + Util.numberToText(SystemTuning.getI("vip_gia_5")) + "\nCash",
                                "VIP6\n" + Util.numberToText(SystemTuning.getI("vip_gia_6")) + "\nCash"
                        );
                        break;

                    case 1:
                        NpcService.gI().createBigMessage(player, avartar, "Ấn Mở Để Xem Lịch Sử Nâng Vip -Hoàn Trả " + SystemTuning.getI("vip_hoan") + "%\b"
                                + "|7|Số Dư " + Util.numberToText(player.getSession().vnd) + " Cash",
                                (byte) 1,
                                "Mở",
                                "https://dragonballsaga.vn/vip_hoan.txt");
                        break;

                    case 2:
                        if (player.diemdanh == 0) {
                            int co4la = 0;
                            int daugod = 0;

                            switch (player.Saga_VIP) {

                                case 0:
                                    break;

                            }

//                                               
                            Item b3 = ItemService.gI().createNewItem((short) 1150, co4la);
                            b3.itemOptions.add(new Item.ItemOption(30, Util.nextInt(0, 1)));
                            InventoryService.gI().addItemBag(player, b3, 9999999999l);
                            InventoryService.gI().sendItemBag(player);
                            Service.getInstance().sendMoney(player);
//
//                                                //********************************************************************************** 
                            Item b4 = ItemService.gI().createNewItem((short) 457, daugod);
                            b4.itemOptions.add(new Item.ItemOption(30, Util.nextInt(0, 1)));
                            InventoryService.gI().addItemBag(player, b4, 999999999);
                            InventoryService.gI().sendItemBag(player);
                            Service.getInstance().sendMoney(player);
//
//                                              
                            player.diemdanh = System.currentTimeMillis();
                            Service.getInstance().sendThongBaoFromAdmin(player, "Bạn Nhận Qua Vip Hôm Nay Thành Công ! ");
                        } else {
                            this.npcChat(player, "Hôm nay đã nhận rồi mà mày bug nữa đi tao chèm chết con mẹ mày!!!");
                        }
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 1) {
                switch (select) {
                    case 0:
                        this.createOtherMenu(player, 2, "|7|VIP\n"
                                + "|4|Quyền lợi Mở Vip đi kèm.."
                                + "\nTăng 500% HP,KI,SD"
                                + "\nTăng 100% TNSM"
                                + "\nPha Lê Hóa +8sao"
                                + "\nĐặcquyền ThôngBáo Vào Game"
                                + "\nĐặcquyền Hoàn " + SystemTuning.getI("vip_hoan") + "% Phí VIP Cũ"
                                + "\n|7|TRẠNG THÁI : VIP " + player.Saga_VIP
                                + (player.timevip > 0 ? "\nHạn còn : " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                        break;
                    case 1:
                        this.createOtherMenu(player, 3, "|7|VIP\n"
                                + "|4|Quyền lợi Mở Vip đi kèm.."
                                + "\nTăng 1000% HP,KI,SD"
                                + "\nTăng 150% TNSM"
                                + "\nPha Lê Hóa +12sao"
                                + "\nĐặcquyền ThôngBáo Vào Game"
                                + "\nĐặcquyền Hoàn " + SystemTuning.getI("vip_hoan") + "% Phí VIP Cũ"
                                + "\n|7|TRẠNG THÁI : VIP " + player.Saga_VIP
                                + (player.timevip > 0 ? "\nHạn còn : " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                        break;
                    case 2:
                        this.createOtherMenu(player, 4, "|7|VIP 3\n"
                                + "|4|Quyền lợi Mở Vip đi kèm.."
                                + "\nTăng 3000% HP,KI,SD"
                                + "\nTăng 250% TNSM"
                                + "\nPha Lê Hóa +15sao"
                                + "\nĐặcquyền ThôngBáo Vào Game"
                                + "\nĐặcquyền Hoàn " + SystemTuning.getI("vip_hoan") + "% Phí VIP Cũ"
                                + "\n|7|TRẠNG THÁI : VIP " + player.Saga_VIP
                                + (player.timevip > 0 ? "\nHạn còn : " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                        break;
                    case 3:
                        this.createOtherMenu(player, 5, "|7|VIP 4\n"
                                + "|4|Quyền lợi Mở Vip đi kèm.."
                                + "\nTăng 5000% HP,KI,SD"
                                + "\nTăng 350% TNSM\n"
                                + "\nPha Lê Hóa +18sao"
                                + "\nĐặcquyền ThôngBáo Vào Game"
                                + "\nĐặcquyền Hoàn " + SystemTuning.getI("vip_hoan") + "% Phí VIP Cũ"
                                + "\n|7|TRẠNG THÁI : VIP " + player.Saga_VIP
                                + (player.timevip > 0 ? "\nHạn còn : " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                        break;
                    case 4:
                        this.createOtherMenu(player, 6, "|7|VIP 5\n"
                                + "|4|Quyền lợi Mở Vip đi kèm.."
                                + "\nTăng 10.000% HP,KI,SD"
                                + "\nTăng 550% TNSM"
                                + "\nPha Lê Hóa +25sao"
                                + "\nĐặcquyền ThôngBáo Vào Game"
                                + "\nĐặcquyền Hoàn " + SystemTuning.getI("vip_hoan") + "% Phí VIP Cũ"
                                + "\n|7|TRẠNG THÁI : VIP " + player.Saga_VIP
                                + (player.timevip > 0 ? "\nHạn còn : " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                        break;

                    case 5:
                        this.createOtherMenu(player, 7, "|7|VIP 6\n"
                                + "|4|Quyền lợi Mở Vip đi kèm.."
                                + "\nTăng 20.000% HP,KI,SD"
                                + "\nTăng 650% TNSM"
                                + "\nPha Lê Hóa +30sao"
                                + "\nĐặcquyền ThôngBáo Vào Game"
                                + "\nĐặcquyền Hoàn " + SystemTuning.getI("vip_hoan") + "% Phí VIP Cũ"
                                + "\n|7|TRẠNG THÁI : VIP " + player.Saga_VIP
                                + (player.timevip > 0 ? "\nHạn còn : " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                        break;
                    case 6:
                        this.createOtherMenu(player, 8, "|7|VIP 7\n"
                                + "|4|Quyền lợi Mở Vip đi kèm.."
                                + "\nTăng 50000% HP,KI,SD"
                                + "\nTăng 750% TNSM"
                                + "\nTăng Đục Sao,Khảm 65Sao"
                                + "\nĐặcquyền ThôngBáo Vào Game"
                                + "\nĐặcquyền Hoàn " + SystemTuning.getI("vip_hoan") + "% Phí VIP Cũ"
                                + "\n|7|TRẠNG THÁI : VIP " + player.Saga_VIP
                                + (player.timevip > 0 ? "\nHạn còn : " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                        break;
                    case 7:
                        this.createOtherMenu(player, 9, "|7|VIP 8\n"
                                + "|4|Quyền lợi Mở Vip đi kèm.."
                                + "\nTăng 100.000% HP,KI,SD"
                                + "\nTăng 850% TNSM"
                                + "\nTăng Đục Sao,Khảm 99Sao"
                                + "\nĐặcquyền ThôngBáo Vào Game"
                                + "\nĐặcquyền Hoàn " + SystemTuning.getI("vip_hoan") + "% Phí VIP Cũ"
                                + "\n|7|TRẠNG THÁI : VIP " + player.Saga_VIP
                                + (player.timevip > 0 ? "\nHạn còn : " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                        break;
                    case 8:
                        this.createOtherMenu(player, 10, "|7|VIP 9\n"
                                + "|4|Quyền lợi Mở Vip đi kèm.."
                                + "\nTăng 15.000% HP,KI,SD"
                                + "\nTăng 1050% TNSM"
                                + "\nTăng Đục Sao,Khảm 200Sao"
                                + "\nĐặcquyền ThôngBáo Vào Game"
                                + "\nĐặcquyền Hoàn " + SystemTuning.getI("vip_hoan") + "% Phí VIP Cũ"
                                + "\n|7|TRẠNG THÁI : VIP " + player.Saga_VIP
                                + (player.timevip > 0 ? "\nHạn còn : " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                        break;
                    case 9:
                        this.createOtherMenu(player, 11, "|7|VIP 10\n"
                                + "|4|Quyền lợi Mở Vip đi kèm.."
                                + "\nTăng 180.000% HP,KI,SD"
                                + "\nTăng 1250% TNSM"
                                + "\nTăng Đục Sao,Khảm 300Sao"
                                + "\nĐặcquyền ThôngBáo Vào Game"
                                + "\nĐặcquyền Hoàn " + SystemTuning.getI("vip_hoan") + "% Phí VIP Cũ"
                                + "\n|7|TRẠNG THÁI : VIP " + player.Saga_VIP
                                + (player.timevip > 0 ? "\nHạn còn : " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                        break;
                    case 10:
                        this.createOtherMenu(player, 12, "|7|VIP 11\n"
                                + "|4|Quyền lợi Mở Vip đi kèm.."
                                + "\nTăng 250.000% HP,KI,SD"
                                + "\nTăng 1550% TNSM"
                                + "\nTăng Đục Sao,Khảm 500Sao"
                                + "\nĐặcquyền ThôngBáo Vào Game"
                                + "\nĐặcquyền Hoàn " + SystemTuning.getI("vip_hoan") + "% Phí VIP Cũ"
                                + "\n|7|TRẠNG THÁI : VIP " + player.Saga_VIP
                                + (player.timevip > 0 ? "\nHạn còn : " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                        break;

                    case 11:
                        this.createOtherMenu(player, 13, "|7|VIP 12\n"
                                + "|4|Quyền lợi Mở Vip đi kèm.."
                                + "\nTăng 300.000% HP,KI,SD"
                                + "\nTăng 1850% TNSM"
                                + "\nTăng Đục Sao,Khảm 700Sao"
                                + "\nĐặcquyền ThôngBáo Vào Game"
                                + "\nĐặcquyền Hoàn " + SystemTuning.getI("vip_hoan") + "% Phí VIP Cũ"
                                + "\n|7|TRẠNG THÁI : VIP " + player.Saga_VIP
                                + (player.timevip > 0 ? "\nHạn còn : " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                        break;

                    case 12:
                        this.createOtherMenu(player, 14, "|7|VIP 13\n"
                                + "|4|Quyền lợi Mở Vip đi kèm.."
                                + "\nTăng 350.000% HP,KI,SD"
                                + "\nTăng 2250% TNSM"
                                + "\nTăng Đục Sao,Khảm 999Sao"
                                + "\nĐặcquyền ThôngBáo Vào Game"
                                + "\nĐặcquyền Hoàn " + SystemTuning.getI("vip_hoan") + "% Phí VIP Cũ"
                                + "\n|7|TRẠNG THÁI : VIP " + player.Saga_VIP
                                + (player.timevip > 0 ? "\nHạn còn : " + "Vĩnh Viễn" : ""), "Kích Hoạt", "Đóng");
                        break;

                }
            } else if (player.iDMark.getIndexMenu() == 2) {
                switch (select) {
                    case 0:
                        if (player.Saga_VIP >= 1) {
                            this.npcChat(player, "|7|Bạn đang là  " + (player.Saga_VIP == 13 ? "VIP 13" : "VIP" + player.Saga_VIP) + " rồi");
                            return;
                        }
                        if (player.getSession().vnd >= SystemTuning.getI("vip_gia_1")) {
                            player.Saga_VIP = 1;
                            player.timevip = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * SystemTuning.getI("vip_ngay"));
                            PlayerDAO.subcash(player, SystemTuning.getI("vip_gia_1"));

                            Service.getInstance().sendMoney(player);
                            this.npcChat(player, "|6|Đã Mua Thành Công " + player.Saga_VIP);
                        } else {
                            this.npcChat(player, "Bạn không đủ Tiền Để Nâng");
                        }
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 3) {
                switch (select) {
                    case 0:
                        if (player.Saga_VIP >= 2) {
                            this.npcChat(player, "|7|Bạn đang là " + (player.Saga_VIP == 13 ? "VIP 13" : "VIP" + player.Saga_VIP) + " rồi");
                            return;
                        }
                        int giavipmoi = SystemTuning.getI("vip_gia_2");
                        int vipCu = player.Saga_VIP;
                        int giavipcu = getGiaVip(vipCu);
                        boolean checkVip = vipCu > 0;
                        int sotienhoan = checkVip ? (int) (giavipcu * SystemTuning.getI("vip_hoan") / 100.0) : 0;
                        if (player.getSession().vnd >= giavipmoi) {
                            PlayerDAO.subcash(player, giavipmoi);
                            player.Saga_VIP = 2;
                            player.timevip = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * SystemTuning.getI("vip_ngay")); // 15 + 16 ngày
                            if (sotienhoan > 0) {
                                PlayerDAO.HoanTien(player, sotienhoan);
                                Service.getInstance().sendMoney(player);
                            }
                            if (sotienhoan > 0) {
                                InputHoanTien.logVipUpgrade(player, giavipcu, vipCu, sotienhoan);
                            }
                            Service.getInstance().sendMoney(player);
                            if (sotienhoan > 0) {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 2\nHoàn lại " + Util.FormatNumber(sotienhoan) + " Cash vì nâng từ VIP " + vipCu);
                            } else {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 2");
                            }
                        } else {
                            this.npcChat(player, "Bạn không đủ Tiền Để Nâng");
                        }
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 4) {
                switch (select) {
                    case 0:
                        if (player.Saga_VIP >= 3) {
                            this.npcChat(player, "|7|Bạn đang là " + (player.Saga_VIP == 13 ? "VIP 13" : "VIP" + player.Saga_VIP) + " rồi");
                            return;
                        }
                        int giavipmoi = SystemTuning.getI("vip_gia_3");
                        int vipCu = player.Saga_VIP;
                        int giavipcu = getGiaVip(vipCu);
                        boolean checkVip = vipCu > 0;
                        int sotienhoan = checkVip ? (int) (giavipcu * SystemTuning.getI("vip_hoan") / 100.0) : 0;
                        if (player.getSession().vnd >= giavipmoi) {
                            PlayerDAO.subcash(player, giavipmoi);
                            player.Saga_VIP = 3;
                            player.timevip = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * SystemTuning.getI("vip_ngay")); // 15 + 16 ngày
                            if (sotienhoan > 0) {
                                PlayerDAO.HoanTien(player, sotienhoan);
                                Service.getInstance().sendMoney(player);
                            }
                            if (sotienhoan > 0) {
                                InputHoanTien.logVipUpgrade(player, giavipcu, vipCu, sotienhoan);
                            }
                            Service.getInstance().sendMoney(player);
                            if (sotienhoan > 0) {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 3\nHoàn lại " + Util.FormatNumber(sotienhoan) + " Cash vì nâng từ VIP " + vipCu);
                            } else {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 3");
                            }
                        } else {
                            this.npcChat(player, "Bạn không đủ Tiền Để Nâng");
                        }
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 5) {
                switch (select) {
                    case 0:
                        if (player.Saga_VIP >= 4) {
                            this.npcChat(player, "|7|Bạn đang là " + (player.Saga_VIP == 13 ? "VIP 13" : "VIP" + player.Saga_VIP) + " rồi");
                            return;
                        }
                        int giavipmoi = SystemTuning.getI("vip_gia_4");
                        int vipCu = player.Saga_VIP;
                        int giavipcu = getGiaVip(vipCu);
                        boolean checkVip = vipCu > 0;
                        int sotienhoan = checkVip ? (int) (giavipcu * SystemTuning.getI("vip_hoan") / 100.0) : 0;
                        if (player.getSession().vnd >= giavipmoi) {
                            PlayerDAO.subcash(player, giavipmoi);
                            player.Saga_VIP = 4;
                            player.timevip = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * SystemTuning.getI("vip_ngay")); // 15 + 16 ngày
                            if (sotienhoan > 0) {
                                PlayerDAO.HoanTien(player, sotienhoan);
                                Service.getInstance().sendMoney(player);
                            }
                            if (sotienhoan > 0) {
                                InputHoanTien.logVipUpgrade(player, giavipcu, vipCu, sotienhoan);
                            }
                            Service.getInstance().sendMoney(player);
                            if (sotienhoan > 0) {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 4\nHoàn lại " + Util.FormatNumber(sotienhoan) + " Cash vì nâng từ VIP " + vipCu);
                            } else {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 4");
                            }
                        } else {
                            this.npcChat(player, "Bạn không đủ Tiền Để Nâng");
                        }
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 6) {
                switch (select) {
                    case 0:
                        if (player.Saga_VIP >= 5) {
                            this.npcChat(player, "|7|Bạn đang là " + (player.Saga_VIP == 13 ? "VIP 13" : "VIP" + player.Saga_VIP) + " rồi");
                            return;
                        }
                        int giavipmoi = SystemTuning.getI("vip_gia_5");
                        int vipCu = player.Saga_VIP;
                        int giavipcu = getGiaVip(vipCu);
                        boolean checkVip = vipCu > 0;
                        int sotienhoan = checkVip ? (int) (giavipcu * SystemTuning.getI("vip_hoan") / 100.0) : 0;
                        if (player.getSession().vnd >= giavipmoi) {
                            PlayerDAO.subcash(player, giavipmoi);
                            player.Saga_VIP = 5;
                            player.timevip = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * SystemTuning.getI("vip_ngay")); // 15 + 16 ngày
                            if (sotienhoan > 0) {
                                PlayerDAO.HoanTien(player, sotienhoan);
                                Service.getInstance().sendMoney(player);
                            }
                            if (sotienhoan > 0) {
                                InputHoanTien.logVipUpgrade(player, giavipcu, vipCu, sotienhoan);
                            }
                            Service.getInstance().sendMoney(player);
                            if (sotienhoan > 0) {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 5\nHoàn lại " + Util.FormatNumber(sotienhoan) + " Cash vì nâng từ VIP " + vipCu);
                            } else {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 5");
                            }
                        } else {
                            this.npcChat(player, "Bạn không đủ Tiền Để Nâng");
                        }
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 7) {
                switch (select) {
                    case 0:
                        if (player.Saga_VIP >= 6) {
                            this.npcChat(player, "|7|Bạn đang là " + (player.Saga_VIP == 13 ? "VIP 13" : "VIP " + player.Saga_VIP) + " rồi");
                            return;
                        }

                        int giavipmoi = SystemTuning.getI("vip_gia_6");
                        int vipCu = player.Saga_VIP;
                        int giavipcu = getGiaVip(vipCu);
                        boolean checkVip = vipCu > 0;
                        int sotienhoan = checkVip ? (int) (giavipcu * SystemTuning.getI("vip_hoan") / 100.0) : 0;

                        if (player.getSession().vnd >= giavipmoi) {
                            PlayerDAO.subcash(player, giavipmoi);
                            player.Saga_VIP = 6;
                            player.timevip = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * SystemTuning.getI("vip_ngay")); // 31 ngày

                            if (sotienhoan > 0) {
                                PlayerDAO.HoanTien(player, sotienhoan);
                                InputHoanTien.logVipUpgrade(player, vipCu, vipCu, sotienhoan);
                            }

                            Service.getInstance().sendMoney(player);

                            if (sotienhoan > 0) {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 6\nHoàn lại " + Util.FormatNumber(sotienhoan) + " Cash vì nâng từ VIP " + vipCu);
                            } else {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 6");
                            }
                        } else {
                            this.npcChat(player, "Bạn không đủ Tiền Để Nâng");
                        }

                }
            } else if (player.iDMark.getIndexMenu() == 8) {
                switch (select) {
                    case 0:
                        if (player.Saga_VIP >= 7) {
                            this.npcChat(player, "|7|Bạn đang là " + (player.Saga_VIP == 13 ? "VIP 13" : "VIP" + player.Saga_VIP) + " rồi");
                            return;
                        }
                        int giavipmoi = SystemTuning.getI("vip_gia_7");
                        int vipCu = player.Saga_VIP;
                        int giavipcu = getGiaVip(vipCu);
                        boolean checkVip = vipCu > 0;
                        int sotienhoan = checkVip ? (int) (giavipcu * SystemTuning.getI("vip_hoan") / 100.0) : 0;
                        if (player.getSession().vnd >= giavipmoi) {
                            PlayerDAO.subcash(player, giavipmoi);
                            player.Saga_VIP = 7;
                            player.timevip = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * SystemTuning.getI("vip_ngay")); // 15 + 16 ngày
                            if (sotienhoan > 0) {
                                PlayerDAO.HoanTien(player, sotienhoan);
                                Service.getInstance().sendMoney(player);
                            }
                            if (sotienhoan > 0) {
                                InputHoanTien.logVipUpgrade(player, giavipcu, vipCu, sotienhoan);
                            }
                            if (sotienhoan > 0) {
                                InputHoanTien.logVipUpgrade(player, giavipcu, 2, sotienhoan);
                            }
                            Service.getInstance().sendMoney(player);
                            if (sotienhoan > 0) {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 7\nHoàn lại " + Util.FormatNumber(sotienhoan) + " Cash vì nâng từ VIP " + vipCu);
                            } else {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 7");
                            }
                        } else {
                            this.npcChat(player, "Bạn không đủ Tiền Để Nâng");
                        }
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 9) {
                switch (select) {
                    case 0:
                        if (player.Saga_VIP >= 8) {
                            this.npcChat(player, "|7|Bạn đang là " + (player.Saga_VIP == 13 ? "VIP 13" : "VIP" + player.Saga_VIP) + " rồi");
                            return;
                        }
                        int giavipmoi = SystemTuning.getI("vip_gia_8");
                        int vipCu = player.Saga_VIP;
                        int giavipcu = getGiaVip(vipCu);
                        boolean checkVip = vipCu > 0;
                        int sotienhoan = checkVip ? (int) (giavipcu * SystemTuning.getI("vip_hoan") / 100.0) : 0;
                        if (player.getSession().vnd >= giavipmoi) {
                            PlayerDAO.subcash(player, giavipmoi);
                            player.Saga_VIP = 8;
                            player.timevip = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * SystemTuning.getI("vip_ngay")); // 15 + 16 ngày
                            if (sotienhoan > 0) {
                                PlayerDAO.HoanTien(player, sotienhoan);
                                Service.getInstance().sendMoney(player);
                            }
                            if (sotienhoan > 0) {
                                InputHoanTien.logVipUpgrade(player, giavipcu, vipCu, sotienhoan);
                            }
                            Service.getInstance().sendMoney(player);
                            if (sotienhoan > 0) {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 8\nHoàn lại " + Util.FormatNumber(sotienhoan) + " Cash vì nâng từ VIP " + vipCu);
                            } else {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 8");
                            }
                        } else {
                            this.npcChat(player, "Bạn không đủ Tiền Để Nâng");
                        }
                        break;
                }

            } else if (player.iDMark.getIndexMenu() == 10) {
                switch (select) {
                    case 0:
                        if (player.Saga_VIP >= 9) {
                            this.npcChat(player, "|7|Bạn đang là " + (player.Saga_VIP == 13 ? "VIP 13" : "VIP" + player.Saga_VIP) + " rồi");
                            return;
                        }
                        int giavipmoi = SystemTuning.getI("vip_gia_9");
                        int vipCu = player.Saga_VIP;
                        int giavipcu = getGiaVip(vipCu);
                        boolean checkVip = vipCu > 0;
                        int sotienhoan = checkVip ? (int) (giavipcu * SystemTuning.getI("vip_hoan") / 100.0) : 0;
                        if (player.getSession().vnd >= giavipmoi) {
                            PlayerDAO.subcash(player, giavipmoi);
                            player.Saga_VIP = 9;
                            player.timevip = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * SystemTuning.getI("vip_ngay")); // 15 + 16 ngày
                            if (sotienhoan > 0) {
                                PlayerDAO.HoanTien(player, sotienhoan);
                                Service.getInstance().sendMoney(player);
                            }
                            if (sotienhoan > 0) {
                                InputHoanTien.logVipUpgrade(player, giavipcu, vipCu, sotienhoan);
                            }
                            Service.getInstance().sendMoney(player);
                            if (sotienhoan > 0) {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 9\nHoàn lại " + Util.FormatNumber(sotienhoan) + " Cash vì nâng từ VIP " + vipCu);
                            } else {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 9");
                            }
                        } else {
                            this.npcChat(player, "Bạn không đủ Tiền Để Nâng");
                        }
                        break;
                }

            } else if (player.iDMark.getIndexMenu() == 11) {
                switch (select) {
                    case 0:
                        if (player.Saga_VIP >= 10) {
                            this.npcChat(player, "|7|Bạn đang là " + (player.Saga_VIP == 13 ? "VIP 13" : "VIP" + player.Saga_VIP) + " rồi");
                            return;
                        }
                        int giavipmoi = SystemTuning.getI("vip_gia_10");
                        int vipCu = player.Saga_VIP;
                        int giavipcu = getGiaVip(vipCu);
                        boolean checkVip = vipCu > 0;
                        int sotienhoan = checkVip ? (int) (giavipcu * SystemTuning.getI("vip_hoan") / 100.0) : 0;
                        if (player.getSession().vnd >= giavipmoi) {
                            PlayerDAO.subcash(player, giavipmoi);
                            player.Saga_VIP = 10;
                            player.timevip = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * SystemTuning.getI("vip_ngay")); // 15 + 16 ngày
                            if (sotienhoan > 0) {
                                PlayerDAO.HoanTien(player, sotienhoan);
                                Service.getInstance().sendMoney(player);
                            }
                            if (sotienhoan > 0) {
                                InputHoanTien.logVipUpgrade(player, giavipcu, vipCu, sotienhoan);
                            }
                            Service.getInstance().sendMoney(player);
                            if (sotienhoan > 0) {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 10\nHoàn lại " + Util.FormatNumber(sotienhoan) + " Cash vì nâng từ VIP " + vipCu);
                            } else {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 10");
                            }
                        } else {
                            this.npcChat(player, "Bạn không đủ Tiền Để Nâng");
                        }
                        break;
                }

            } else if (player.iDMark.getIndexMenu() == 12) {
                switch (select) {
                    case 0:
                        if (player.Saga_VIP >= 11) {
                            this.npcChat(player, "|7|Bạn đang là " + (player.Saga_VIP == 13 ? "VIP 13" : "VIP" + player.Saga_VIP) + " rồi");
                            return;
                        }
                        int giavipmoi = SystemTuning.getI("vip_gia_11");
                        int vipCu = player.Saga_VIP;
                        int giavipcu = getGiaVip(vipCu);
                        boolean checkVip = vipCu > 0;
                        int sotienhoan = checkVip ? (int) (giavipcu * SystemTuning.getI("vip_hoan") / 100.0) : 0;
                        if (player.getSession().vnd >= giavipmoi) {
                            PlayerDAO.subcash(player, giavipmoi);
                            player.Saga_VIP = 11;
                            player.timevip = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * SystemTuning.getI("vip_ngay")); // 15 + 16 ngày
                            if (sotienhoan > 0) {
                                PlayerDAO.HoanTien(player, sotienhoan);
                                Service.getInstance().sendMoney(player);
                            }
                            if (sotienhoan > 0) {
                                InputHoanTien.logVipUpgrade(player, giavipcu, vipCu, sotienhoan);
                            }
                            Service.getInstance().sendMoney(player);
                            if (sotienhoan > 0) {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 11\nHoàn lại " + Util.FormatNumber(sotienhoan) + " Cash vì nâng từ VIP " + vipCu);
                            } else {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 11");
                            }
                        } else {
                            this.npcChat(player, "Bạn không đủ Tiền Để Nâng");
                        }
                        break;
                }

            } else if (player.iDMark.getIndexMenu() == 13) {
                switch (select) {
                    case 0:
                        if (player.Saga_VIP >= 12) {
                            this.npcChat(player, "|7|Bạn đang là " + (player.Saga_VIP == 13 ? "VIP 13" : "VIP" + player.Saga_VIP) + " rồi");
                            return;
                        }
                        int giavipmoi = SystemTuning.getI("vip_gia_12");
                        int vipCu = player.Saga_VIP;
                        int giavipcu = getGiaVip(vipCu);
                        boolean checkVip = vipCu > 0;
                        int sotienhoan = checkVip ? (int) (giavipcu * SystemTuning.getI("vip_hoan") / 100.0) : 0;
                        if (player.getSession().vnd >= giavipmoi) {
                            PlayerDAO.subcash(player, giavipmoi);
                            player.Saga_VIP = 12;
                            player.timevip = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * SystemTuning.getI("vip_ngay")); // 15 + 16 ngày
                            if (sotienhoan > 0) {
                                PlayerDAO.HoanTien(player, sotienhoan);
                                Service.getInstance().sendMoney(player);
                            }
                            if (sotienhoan > 0) {
                                InputHoanTien.logVipUpgrade(player, giavipcu, vipCu, sotienhoan);
                            }
                            Service.getInstance().sendMoney(player);
                            if (sotienhoan > 0) {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 12\nHoàn lại " + Util.FormatNumber(sotienhoan) + " Cash vì nâng từ VIP " + vipCu);
                            } else {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 12");
                            }
                        } else {
                            this.npcChat(player, "Bạn không đủ Tiền Để Nâng");
                        }
                        break;
                }

            } else if (player.iDMark.getIndexMenu() == 14) {
                switch (select) {
                    case 0:
                        if (player.Saga_VIP >= 13) {
                            this.npcChat(player, "|7|Bạn đang là " + (player.Saga_VIP == 13 ? "VIP 13" : "VIP" + player.Saga_VIP) + " rồi");
                            return;
                        }
                        int giavipmoi = SystemTuning.getI("vip_gia_13");
                        int vipCu = player.Saga_VIP;
                        int giavipcu = getGiaVip(vipCu);
                        boolean checkVip = vipCu > 0;
                        int sotienhoan = checkVip ? (int) (giavipcu * SystemTuning.getI("vip_hoan") / 100.0) : 0;
                        if (player.getSession().vnd >= giavipmoi) {
                            PlayerDAO.subcash(player, giavipmoi);
                            player.Saga_VIP = 13;
                            player.timevip = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * SystemTuning.getI("vip_ngay")); // 15 + 16 ngày
                            if (sotienhoan > 0) {
                                PlayerDAO.HoanTien(player, sotienhoan);
                                Service.getInstance().sendMoney(player);
                            }
                            if (sotienhoan > 0) {
                                InputHoanTien.logVipUpgrade(player, giavipcu, vipCu, sotienhoan);
                            }
                            Service.getInstance().sendMoney(player);
                            if (sotienhoan > 0) {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 13\nHoàn lại " + Util.FormatNumber(sotienhoan) + " Cash vì nâng từ VIP " + vipCu);
                            } else {
                                this.npcChat(player, "|6|Đã Mua Thành Công VIP 13");
                            }
                        } else {
                            this.npcChat(player, "Bạn không đủ Tiền Để Nâng");
                        }
                        break;
                }

            } else if (player.iDMark.getIndexMenu() == ConstNpc.NAP_TIEN) {
                switch (select) {
                    case 0:
                        List<String> menu = new ArrayList<>();
                        for (int i = 0; i < napVang.length; i++) {
                            menu.add(i, Util.numberToText(napVang[i][0]) + "\n" + Util.numberToText(napVang[i][1] * costNapVang) + " Thỏi Vàng");
                        }
                        String[] menus = menu.toArray(new String[0]);
                        createOtherMenu(player, ConstNpc.NAP_VANG, "Ta sẽ giữ giúp con\n"
                                + "Nếu con cần dùng tới hãy quay lại đây gặp ta!\nDưới giá Cash Cần Đổi", 
                                menus);
                        break;
                    case 1:
                        if (player.getSession().goldBar > 0) {
                            List<Item> listItem = new ArrayList<>();
                            Item thoiVang = ItemService.gI().createNewItem((short) 457, player.getSession().goldBar);
                            listItem.add(thoiVang);
                            if (InventoryService.gI().getCountEmptyBag(player) < listItem.size()) {
                                Service.gI().sendThongBao(player, "Cần ít nhất " + listItem.size() + " ô trống trong hành trang");
                            }
                            for (Item it : listItem) {
                                InventoryService.gI().addItemBag(player, it, 9999999999l);
                                InventoryService.gI().sendItemBag(player);
                            }
                            Service.gI().sendThongBao(player, "Bạn đã nhận được " + player.getSession().goldBar + " Thỏi Vàng");
                            PlayerDAO.subGoldBar(player, -(napVang[select][1] * costNapVang));
                            PlayerDAO.subGoldBar(player, player.getSession().goldBar);
                        }
                        break;
//                    case 2:
//                        if (player.inventory.gem >= 100000) {
//                            this.npcChat(player, "Tham Lam");
//                            break;
//                        }
//                        player.inventory.gem = 100000;
//                        Service.getInstance().sendMoney(player);
//                        Service.getInstance().sendThongBao(player, "|1|Bạn vừa nhận được 100k Ngọc xanh");
//                        break;
//                    case 3:
//                        if (player.inventory.ruby >= 1000) {
//                            this.npcChat(player, "Tham Lam");
//                            break;
//                        }
//                        player.inventory.ruby = 1000;
//                        Service.getInstance().sendMoney(player);
//                        Service.getInstance().sendThongBao(player, "|1|Bạn vừa nhận được 1k Hồng Ngọc");
//                        break;
                    case 2:
                        Input.gI().createFormQDco4la(player);
                        break;
                         case 3:
                        Input.gI().createFormQDngocxanh(player);
                        break;
                         case 4:
                        Input.gI().createFormQDhongngoc(player);
                        break;

                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.NAP_VANG) {
                if (player.getSession().vnd >= napVang[select][0]) {
                    List<Item> listItem = new ArrayList<>();
                    if (InventoryService.gI().getCountEmptyBag(player) < listItem.size()) {
                        Service.gI().sendThongBao(player, "Cần ít nhất " + listItem.size() + " ô trống trong hành trang");
                    }
                    for (Item it : listItem) {
                        InventoryService.gI().addItemBag(player, it, 99999999999l);

                    }
                    InventoryService.gI().sendItemBag(player);
                    PlayerDAO.subcash(player, napVang[select][0]);
                    BadgesTaskService.updateCountBagesTask(player, ConstTaskBadges.DAI_GIA_MOI_NHU, napVang[select][0]);
                    PlayerDAO.subGoldBar(player, -(napVang[select][1] * costNapVang));
                    Service.gI().sendThongBao(player, "Bạn có thêm " + Util.numberToText(napVang[select][1] * costNapVang) + " Cash");
                } else {
                    Service.gI().sendThongBao(player, "Không đủ số dư");
                }
            }
        }
    }
}
