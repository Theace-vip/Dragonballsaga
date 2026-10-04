package EventSuKien;

/**
 *
 */
import consts.ConstNpc;
import item.Item;
import npc.Npc;
import player.Player;
import services.InventoryService;
import services.ItemService;
import services.Service;
import utils.Util;

public class EventTet extends Npc {

    public EventTet(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7| SỰ KIỆN HÈ SÔI ĐỘNG 2025"
                    + "\n\n|2|Nguyên liệu cần nấu bánh "
                    + "\n|-1|- Bánh Hạt sen : 999 Hạt sen + 999 Bột nếp + 999 Mồi lửa"
                    + "\n|-1|- Bánh Đậu xanh : 800 Đậu xanh + 999 Bột nếp + 800 Mồi lửa"
                    + "\n|-1|- Bánh Thập cẩm : 999 Hạt sen + 699 Đậu xanh + 999 Bột nếp + 500 Mồi lửa"
                    + "\n|-1|- Bánh Chưng : 1000 Thịt heo + 1000 Thúng nếp + 1000 Thúng đậu xanh + 1000 Lá dong"
                    + "\n|-1|- Bánh Tét : 500 Thịt heo + 500 Thúng nếp + 500 Thúng đậu xanh + 500 Lá dong"
                    + "\n|7|Làm bánh sẽ tốn phí ?? ??/lần"
                    + "\n\n|1|Điểm sự kiện : " + Util.format(player.diemfam) + " Điểm",
                    "Thể lệ", "Làm bánh\nCắm trại", "Đổi điểm\nHè sôi động");
        }
        //  }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
               if (this.mapId == 5) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0: {
                        this.createOtherMenu(player, 1234,
                                "|7|SỰ KIỆN HÈ SÔI ĐỘNG 2024"
                                + "\n\n|2|Cách thức tìm nguyên liệu nấu bánh "
                                + "\n|4|- Hạt sen : Đánh các quái, săn boss admin"
                                + "\n- Đậu xanh : Đánh các quái, săn boss admin"
                                + "\n- Bột nếp : Đánh các quái, săn boss admin"
                                + "\n- Mồi lửa : Giết Boss Thỏ trắng (5 phút xuất hiện 1 lần), mua shop gold"
                                + "\n\n|5|Ăn bánh nhận được Điểm tích lũy Sự kiện đổi được các phần quà hấp dẫn"
                                + "\n|-1|- Bánh Hạt sen : Nhận 2 Điểm Sự kiện"
                                + "\n|-1|- Bánh Đậu xanh : Nhận 2 Điểm Sự kiện"
                                + "\n|-1|- Bánh Thập cẩm : Nhận 5 Điểm Sự kiện"
                                + "\n|-1|- Bánh Tét : Mở ra được cải trang 250%, bánh điểm sự kiện, Ma hồn ngọc, Thần kim Bảo, đệ tử broly-zeno-goku"
                                + "\n|-1|- Bánh Chưng : Mở ra được cải trang 350%, bánh điểm sự kiện, Ngọc rồng siêu cấp, đệ tử gogeta, găng VIP (50K SĐG-10% SĐ)"
                                + "\n|5|- Quy đổi tiền 1.000Đ nhận thêm 1 Điểm Sự kiện (Không tính đổi Xu)"
                                + "\n\n|7|Chị hiểu hôn ???",
                                "Đã hiểu");
                        break;
                    }
                    case 1: {
                        this.createOtherMenu(player, 1111,
                                "|7|SỰ KIỆN HÈ SÔI ĐỘNG"
                                + "\n\n|2|Bạn muốn làm bánh gì?",
                                "Bánh\nHạt sen", "Bánh\nĐậu xanh", "Bánh\nThập cẩm", "Bánh\nChưng", "Bánh\nTét");
                        break;
                    }
                    case 2: {
                        this.createOtherMenu(player, 2222,
                                "|7|TÍCH ĐIỂM SỰ KIỆN HÈ SÔI ĐỘNG 2024"
                                + "\n\n|1|Khi đổi điểm thì sẽ được cộng điểm đua top \n"
                                + "|2|Mốc 10.000.000 Điểm\n"
                                + "|4|200 Mảnh thiên sứ ngẫu nhiên,1 hộp Nguyên Thủy, 50 rương thần linh, 30 Hộp quà TẾT, 30 Thẻ gia hạn, 1 Phiếu giảm giá + 5M HN \n"
                                + "|2|Mốc 5.000 Điểm\n"
                                + "|4|200 Mảnh thiên sứ ngẫu nhiên,1 hộp thánh tôn có găng 20k, 50 rương thần linh, 30 Hộp quà TẾT, 30 Thẻ gia hạn, 1 Phiếu giảm giá + 2,5M HN \n"
                                + "|2|Mốc 3.000 Điểm\n"
                                + "|4|100 Mảnh thiên sứ ngẫu nhiên,1 hộp sên Medusa, 40 rương thần linh, 10 Thẻ gia hạn, 15 Hộp Tết  + 150k HN \n"
                                + "|2|Mốc 200 Điểm\n"
                                + "|4|50 Mảnh thiên sứ ngẫu nhiên, 30 rương thần linh, 5 Thẻ gia hạn, 10 Hộp Tết + 100k HN \n"
                                + "|2|Mốc 50 Điểm\n"
                                + "|4|10 Mảnh thiên sứ ngẫu nhiên, 5 rương thần linh + 25k HN"
                                + "\n\n|7|Điểm sự kiện : " + Util.format(player.diemfam) + " Điểm"
                                + "\n|1|Điểm Top Tết Không nợ nần : " + Util.format(player.diemfam) + " Điểm",
                                "10.000.000 Điểm", "5.000 Điểm", "3.000 Điểm", "200 Điểm", "50 Điểm");
                        break;
                    }
                }
            } else if (player.iDMark.getIndexMenu() == 1111) {
                switch (select) {
                    case 0: {
                        Item hatsen = null;
                        Item botnep = null;
                        Item moilua = null;
                        try {
                            hatsen = InventoryService.gI().findItemBag(player, 1340);
                            botnep = InventoryService.gI().findItemBag(player, 1338);
                            moilua = InventoryService.gI().findItemBag(player, 1341);
                        } catch (Exception e) {
//                                        throw new RuntimeException(e);
                        }
                        if (hatsen == null || hatsen.quantity < 99) {
                            this.npcChat(player, "|7|Bạn không đủ 99 Hạt sen");
                        } else if (botnep == null || botnep.quantity < 99) {
                            this.npcChat(player, "|7|Bạn không đủ 99 Bột nếp");
                        } else if (moilua == null || moilua.quantity < 99) {
                            this.npcChat(player, "|7|Bạn không đủ 99 Mồi lửa");
                        } else if (player.inventory.gold < 2_000_000_000) {
                            this.npcChat(player, "|7|Bạn không đủ 2Ty Vàng");
                        } else if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                            this.npcChat(player, "|7|Hành trang của bạn không đủ chỗ trống");
                        } else {
                            player.inventory.gold -= 2_000_000_000;
                            InventoryService.gI().subQuantityItemsBag(player, hatsen, 99);
                            InventoryService.gI().subQuantityItemsBag(player, botnep, 50);
                            InventoryService.gI().subQuantityItemsBag(player, moilua, 2);
                            Service.getInstance().sendMoney(player);
                            Item banhtrungthu = ItemService.gI().createNewItem((short) 1336);
                            InventoryService.gI().addItemBag(player, banhtrungthu, 99999);
                            InventoryService.gI().sendItemBag(player);
                            this.npcChat(player, "|4|Bạn nhận được Bánh  Hạt sen");
                        }
                        break;
                    }
                    case 1: {
                        Item dauxanh = null;
                        Item botnep = null;
                        Item moilua = null;
                        try {
                            dauxanh = InventoryService.gI().findItemBag(player, 1339);
                            botnep = InventoryService.gI().findItemBag(player, 1338);
                            moilua = InventoryService.gI().findItemBag(player, 1341);
                        } catch (Exception e) {
//                                        throw new RuntimeException(e);
                        }
                        if (dauxanh == null || dauxanh.quantity < 80) {
                            this.npcChat(player, "|7|Bạn không đủ 80 Đậu xanh");
                        } else if (botnep == null || botnep.quantity < 99) {
                            this.npcChat(player, "|7|Bạn không đủ 99 Bột nếp");
                        } else if (moilua == null || moilua.quantity < 80) {
                            this.npcChat(player, "|7|Bạn không đủ 80 Mồi lửa");
                        } else if (player.inventory.gold < 2_000_000_000) {
                            this.npcChat(player, "|7|Bạn không đủ 2Ty Vàng");
                        } else if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                            this.npcChat(player, "|7|Hành trang của bạn không đủ chỗ trống");
                        } else {
                            player.inventory.gold -= 2_000_000_000;
                            InventoryService.gI().subQuantityItemsBag(player, dauxanh, 80);
                            InventoryService.gI().subQuantityItemsBag(player, botnep, 99);
                            InventoryService.gI().subQuantityItemsBag(player, moilua, 80);
                            Service.getInstance().sendMoney(player);
                            Item banhtrungthu = ItemService.gI().createNewItem((short) 1335);
                            InventoryService.gI().addItemBag(player, banhtrungthu, 9999);
                            InventoryService.gI().sendItemBag(player);
                            this.npcChat(player, "|4|Bạn nhận được Bánh Đậu xanh");
                        }
                        break;
                    }
                    case 2: {
                        Item hatsen = null;
                        Item dauxanh = null;
                        Item botnep = null;
                        Item moilua = null;
                        try {
                            hatsen = InventoryService.gI().findItemBag(player, 1340);
                            dauxanh = InventoryService.gI().findItemBag(player, 1339);
                            botnep = InventoryService.gI().findItemBag(player, 1338);
                            moilua = InventoryService.gI().findItemBag(player, 1341);
                        } catch (Exception e) {
//                                        throw new RuntimeException(e);
                        }
                        if (hatsen == null || hatsen.quantity < 99) {
                            this.npcChat(player, "|7|Bạn không đủ 99 Hạt sen");
                        } else if (botnep == null || botnep.quantity < 69) {
                            this.npcChat(player, "|7|Bạn không đủ 69 Bột nếp");
                        } else if (dauxanh == null || dauxanh.quantity < 99) {
                            this.npcChat(player, "|7|Bạn không đủ 99 Đậu xanh");
                        } else if (moilua == null || moilua.quantity < 50) {
                            this.npcChat(player, "|7|Bạn không đủ 50 Mồi lửa");
                        } else if (player.inventory.gold < 2_000_000_000) {
                            this.npcChat(player, "|7|Bạn không đủ 2Ty Vàng");
                        } else if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                            this.npcChat(player, "|7|Hành trang của bạn không đủ chỗ trống");
                        } else {
                            player.inventory.gold -= 2_000_000_000;
                            InventoryService.gI().subQuantityItemsBag(player, hatsen, 99);
                            InventoryService.gI().subQuantityItemsBag(player, dauxanh, 99);
                            InventoryService.gI().subQuantityItemsBag(player, botnep, 69);
                            InventoryService.gI().subQuantityItemsBag(player, moilua, 50);
                            Service.getInstance().sendMoney(player);
                            Item banhtrungthu = ItemService.gI().createNewItem((short) 1337);
                            InventoryService.gI().addItemBag(player, banhtrungthu, 99999);
                            InventoryService.gI().sendItemBag(player);
                            this.npcChat(player, "|4|Bạn nhận được Bánh Thập cẩm");
                        }
                        break;
                    }
                    ////// làm bánh chưng
                    case 3: {
                        Item thitheo = null;
                        Item thungnep = null;
                        Item thungdauxanh = null;
                        Item ladong = null;
                        try {
                            thitheo = InventoryService.gI().findItemBag(player, 748);
                            thungdauxanh = InventoryService.gI().findItemBag(player, 750);
                            thungnep = InventoryService.gI().findItemBag(player, 749);
                            ladong = InventoryService.gI().findItemBag(player, 751);
                        } catch (Exception e) {
//                                        throw new RuntimeException(e);
                        }
                        if (thitheo == null || thitheo.quantity < 99) {
                            this.npcChat(player, "|7|Bạn không đủ 99 miếng Thịt heo");
                        } else if (thungnep == null || thungnep.quantity < 99) {
                            this.npcChat(player, "|7|Bạn không đủ 99 Thúng nếp");
                        } else if (thungdauxanh == null || thungdauxanh.quantity < 99) {
                            this.npcChat(player, "|7|Bạn không đủ 99 Thúng Đậu xanh");
                        } else if (ladong == null || ladong.quantity < 50) {
                            this.npcChat(player, "|7|Bạn không đủ 50 Lá dong");
                        } else if (player.inventory.gold < 2_000_000_000) {
                            this.npcChat(player, "|7|Bạn không đủ 2Ty Vàng");
                        } else if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                            this.npcChat(player, "|7|Hành trang của bạn không đủ chỗ trống");
                        } else {
                            player.inventory.gold -= 2_000_000_000;
                            InventoryService.gI().subQuantityItemsBag(player, thitheo, 99);
                            InventoryService.gI().subQuantityItemsBag(player, thungdauxanh, 99);
                            InventoryService.gI().subQuantityItemsBag(player, thungnep, 99);
                            InventoryService.gI().subQuantityItemsBag(player, ladong, 99);
                            Service.getInstance().sendMoney(player);
                            Item banhchung = ItemService.gI().createNewItem((short) 753);
                            InventoryService.gI().addItemBag(player, banhchung, 99999);
                            InventoryService.gI().sendItemBag(player);
                            this.npcChat(player, "|4|Bạn nhận được Bánh Chưng");
                        }
                        break;
                    }
                    ///  ////// làm bánh tét
                    case 4: {
                        Item thitheo = null;
                        Item thungnep = null;
                        Item thungdauxanh = null;
                        Item ladong = null;
                        try {
                            thitheo = InventoryService.gI().findItemBag(player, 748);
                            thungdauxanh = InventoryService.gI().findItemBag(player, 750);
                            thungnep = InventoryService.gI().findItemBag(player, 749);
                            ladong = InventoryService.gI().findItemBag(player, 751);
                        } catch (Exception e) {
//                                        throw new RuntimeException(e);
                        }
                        if (thitheo == null || thitheo.quantity < 99) {
                            this.npcChat(player, "|7|Bạn không đủ 99 miếng Thịt heo");
                        } else if (thungnep == null || thungnep.quantity < 99) {
                            this.npcChat(player, "|7|Bạn không đủ 99 Thúng nếp");
                        } else if (thungdauxanh == null || thungdauxanh.quantity < 99) {
                            this.npcChat(player, "|7|Bạn không đủ 99 Thúng Đậu xanh");
                        } else if (ladong == null || ladong.quantity < 99) {
                            this.npcChat(player, "|7|Bạn không đủ 99 Lá dong");
                        } else if (player.inventory.gold < 2_000_000_000) {
                            this.npcChat(player, "|7|Bạn không đủ 2Ty Vàng");
                        } else if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                            this.npcChat(player, "|7|Hành trang của bạn không đủ chỗ trống");
                        } else {
                            player.inventory.gold -= 2_000_000_000;
                            InventoryService.gI().subQuantityItemsBag(player, thitheo, 99);
                            InventoryService.gI().subQuantityItemsBag(player, thungdauxanh, 99);
                            InventoryService.gI().subQuantityItemsBag(player, thungnep, 99);
                            InventoryService.gI().subQuantityItemsBag(player, ladong, 99);
                            Service.getInstance().sendMoney(player);
                            Item banhchung = ItemService.gI().createNewItem((short) 752);
                            InventoryService.gI().addItemBag(player, banhchung, 99999);
                            InventoryService.gI().sendItemBag(player);
                            this.npcChat(player, "|4|Bạn nhận được Bánh Tét");
                        }
                        break;
                    }
                    ///
                }
            }
        }
    }
    }
    }
