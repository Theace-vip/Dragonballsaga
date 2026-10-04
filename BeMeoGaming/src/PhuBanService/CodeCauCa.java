package PhuBanService;

import item.Item;
import java.util.HashMap;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import map.Map;
import player.Player;
import services.InventoryService;
import services.ItemService;
import services.Service;
import utils.Util;

/**
 * FishingSystem – thêm nhiều loại mồi, mỗi mồi có loot pool riêng và (tuỳ chọn)
 * bonus % tỉ lệ.
 *
 * YÊU CẦU HẠ TẦNG: - Player: các field pl.zone.map.mapId, useCanCau,
 * lasttimeCanCau, name - Item: template.id, template.name, quantity -
 * InventoryService: findItemBag, getCountEmptyBag, subQuantityItemsBag,
 * addItemBag, sendItemBag - ItemService: createNewItem(short) - Service:
 * sendThongBaoFromAdmin(Player, String), sendThongBaoAllPlayer(String) - Util:
 * isTrue(int chance, int total), nextInt(int min, int max) - Map câu cá = mapId
 * 216 (đổi nếu bạn dùng map khác)
 */
public class CodeCauCa {

    // ==== 0) EXECUTOR ====
    private static final ScheduledExecutorService FISH_EXEC
            = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "FishScheduler");
                t.setDaemon(true);
                return t;
            });
    private static final short MOI_THUONG = 1717;  
    private static final short MOI_VUA = 1728; 
    private static final short MOI_VIP = 1729; 

    private static final short CANG0 = 1713;  
    private static final short CANBAC = 1714; 
    private static final short CANVANG = 1715;  
    private static final short CANTITAN = 1716;  

    private static final int T_GIAN_CAU_MS = 10000; 
    private static final int MAP_CAU_CA = 216;   

    /**
     * Nếu true: mồi cộng thêm % tỉ lệ dính (giữ tỉ lệ của cần + bonus của mồi).
     * Nếu false: mồi KHÔNG ảnh hưởng tỉ lệ, chỉ thay loot pool (tỉ lệ phụ thuộc
     * mỗi cần).
     */
    private static final boolean BAIT_AFFECTS_SUCCESS = true;

    private static final short[] LIST_CA_THUONG = {
         1857, 1858, 1859, 1860, 1720, 1721, 1722, 987, 1857, 1857, 1857, 1857, 1110,1873, 673, 673, 673, 673, 673, 673
    };

    private static final short[] LIST_CA_VUA = {
        1718, 1719, 1720, 1721, 1722,  1873, 1873, 1873, 1873, 1719, 1719, 1719, 673, 673, 673, 673, 673, 673
    };
    private static final short[] LIST_CA_VIP = {
        1886, 1887, 1888, 1889, 1890, 1891, 1892, 1893, 1894, 1895, 1896, 1897, 1898, 1899, 1900, 1901, 1902, 1903, 1904, 1905, 1906, 1907, 1908, 1909, 1910, 1911,
        987, 987, 1718, 1719, 1720, 1721, 1722, 1723, 1724, 1725,1754, 1726, 1727, 1857, 1858, 1859, 1860, 1110, 1110, 1110, 996, 673, 673, 673, 673, 673, 673};
  
    
    private static final java.util.Map<Short, short[]> BAIT_POOLS = createBaitPools();
    private static final java.util.Map<Short, Integer> BAIT_BONUS = createBaitBonus();

    private static java.util.Map<Short, short[]> createBaitPools() {
        java.util.Map<Short, short[]> m = new java.util.HashMap<>();
        m.put(MOI_THUONG, LIST_CA_THUONG);
        m.put(MOI_VUA, LIST_CA_VUA);
        m.put(MOI_VIP, LIST_CA_VIP);
        return m;
    }

    private static java.util.Map<Short, Integer> createBaitBonus() {
        java.util.Map<Short, Integer> m = new java.util.HashMap<>();
        m.put(MOI_THUONG, 1);
        m.put(MOI_VUA, 3);
        m.put(MOI_VIP, 5);
        return m;
    }

    private static int getFishingChanceByRod(int rodId) {
        switch ((short) rodId) {
            case CANG0:
                return 3;  // gỗ 1%
            case CANBAC:
                return 6;  // bạc 3%
            case CANVANG:
                return 8;  // vàng 5%
            case CANTITAN:
                return 13;  // kim cương 7%
            default:
                return 1;
        }
    }

    private static short[] getLootPoolForBait(short baitId) {
        return BAIT_POOLS.getOrDefault(baitId, LIST_CA_THUONG);
    }

    private static int getBaitBonus(short baitId) {
        return BAIT_BONUS.getOrDefault(baitId, 0);
    }

    private static Item findBestBait(Player pl) {
        Item b;
        if ((b = InventoryService.gI().findItemBag(pl, MOI_VIP)) != null && b.quantity > 0) {
            return b;
        }
        if ((b = InventoryService.gI().findItemBag(pl, MOI_VUA)) != null && b.quantity > 0) {
            return b;
        }
        if ((b = InventoryService.gI().findItemBag(pl, MOI_THUONG)) != null && b.quantity > 0) {
            return b;
        }
        return null;
    }

    public static void cauCa(Player pl, Item rod) {
        if (pl == null || rod == null || rod.template == null) {
            return;
        }

        if (pl.zone == null || pl.zone.map == null || pl.zone.map.mapId != MAP_CAU_CA) {
            Service.getInstance().sendThongBaoFromAdmin(pl, "|7|Chỉ có thể câu cá tại map Câu cá.");
            return;
        }

        long now = System.currentTimeMillis();
        if (pl.useCanCau) {
            long remain = (T_GIAN_CAU_MS - (now - pl.lasttimeCanCau)) / 1000;
            if (remain > 0) {
                Service.getInstance().sendThongBaoFromAdmin(pl, "|7|Đang câu rồi, còn " + remain + " giây...");
                return;
            } else {
                pl.useCanCau = false;
            }
        }
        Item bait = findBestBait(pl);
        if (bait == null || bait.quantity <= 1) {
            Service.getInstance().sendThongBaoFromAdmin(pl, "|7|Bạn cần có Trên 1 Mồi câu để thả cần.");
            return;
        }

        if (InventoryService.gI().getCountEmptyBag(pl) <= 0) {
            Service.getInstance().sendThongBaoFromAdmin(pl, "|7|Hành trang đã đầy.");
            return;
        }
        InventoryService.gI().subQuantityItemsBag(pl, bait, 1);

        pl.useCanCau = true;
        pl.lasttimeCanCau = now;

        final short baitId = bait.template.id;
        final String baitName = bait.template != null ? bait.template.name : "Mồi";

        Service.getInstance().sendThongBaoFromAdmin(pl,
                "|7|Bạn dùng [" + baitName + "] và bắt đầu thả cần...\n|6|Vui lòng đợi 10 giây.");

        FISH_EXEC.schedule(() -> {
            try {
                if (pl == null || pl.zone == null || pl.zone.map == null || pl.zone.map.mapId != MAP_CAU_CA) {
                    return;
                }

                int baseChance = getFishingChanceByRod(rod.template.id);
                int totalChance = baseChance;

                if (BAIT_AFFECTS_SUCCESS) {
                    totalChance += getBaitBonus(baitId);
                }
                if (totalChance < 1) {
                    totalChance = 1;
                }
                if (totalChance > 95) {
                    totalChance = 95;
                }
                boolean success = Util.isTrue(totalChance, 100);
                if (!success) {
                    Service.getInstance().sendThongBaoFromAdmin(pl,
                            "|7|Cá chạy mất rồi!\n|4|Tiếp tục câu nào!");
                    return;
                }
                short[] pool = getLootPoolForBait(baitId);
                int idx = Util.nextInt(0, pool.length - 1);
                Item fish = ItemService.gI().createNewItem(pool[idx]);
                if (fish == null) {
                    Service.getInstance().sendThongBaoFromAdmin(pl, "|3|Có lỗi khi tạo vật phẩm cá.");
                    return;
                }
                if ((short) rod.template.id == CANTITAN) {
                    fish.quantity = Math.max(2, fish.quantity);
                }
                if (InventoryService.gI().getCountEmptyBag(pl) <= 0) {
                    Service.getInstance().sendThongBaoFromAdmin(pl, "|7|Hành trang đã đầy, không nhận được cá.");
                    return;
                }
                if (fish.template.type == 5) { // cải trang
                    fish.itemOptions.add(new Item.ItemOption(50, Util.nextInt(1, 10000)));
                    fish.itemOptions.add(new Item.ItemOption(77, Util.nextInt(1, 10000)));
                    fish.itemOptions.add(new Item.ItemOption(103, Util.nextInt(1, 10000)));
                    fish.itemOptions.add(new Item.ItemOption(5, Util.nextInt(1, 100)));
                    fish.itemOptions.add(new Item.ItemOption(226, Util.nextInt(1, 100)));
                    fish.itemOptions.add(new Item.ItemOption(231, 0));
                    InventoryService.gI().addItemBag(pl, fish, 999_999);
                    InventoryService.gI().sendItemBag(pl);

                }
                 if (fish.template.type > 0) { // cải trang
                   // fish.itemOptions.add(new Item.ItemOption(248, 2025));
                    InventoryService.gI().addItemBag(pl, fish, 999_999);
                    InventoryService.gI().sendItemBag(pl);
                    pl.point_cauca +=1;

                }
                    Service.getInstance().sendThongBaoFromAdmin(pl,
                            "|7|Bạn câu được \n|6|[" + fish.template.name + "] \n|7|(Mồi: " + baitName + ")\nXin chúc mừng!");
                    Service.getInstance().sendThongBaoAllPlayer(
                            "|7|Người chơi [" + pl.name + "] vừa câu được \n|6|[" + fish.template.name + "] với " + baitName + "!");
              
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (pl != null) {
                    pl.useCanCau = false;
                }
            }
        }, T_GIAN_CAU_MS, TimeUnit.MILLISECONDS);
    }
}
