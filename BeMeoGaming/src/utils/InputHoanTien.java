package utils;

import item.Item;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import player.Player;
import services.ItemService;
import shop.ItemShop;

public class InputHoanTien {
    
public static void ghiLichSuMua(Player pl, ItemShop itemShop, String payType, int cost) {
    if (!"Item".equalsIgnoreCase(payType)) return;
    String ts = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(new java.util.Date());
    String buyName = "Unknown";
    try {
        Item buyItem = ItemService.gI().createNewItem((short) itemShop.id);
        if (buyItem != null && buyItem.template != null) buyName = buyItem.template.name;
    } catch (Exception ignore) {}
    String payName = "Unknown";
    if (itemShop.iconSpec == 457) {
        payName = "zenni"; // đặt tên bạn muốn
    } else if (itemShop.iconSpec == 1150) {
        payName = "Cỏ 4 Lá";
    } else if (itemShop.iconSpec == 1027) {
        payName = "Xu Tân Thủ";
    } else if (itemShop.iconSpec == 542) {
        payName = "Đào Chín";
    } else if (itemShop.iconSpec == 541) {
        payName = "Đào Xanh";
        } else if (itemShop.iconSpec == 861) {
        payName = "ruby";
        } else if (itemShop.iconSpec == 77) {
        payName = "Ngọc Xanh";
        } else if (itemShop.iconSpec == 190) {
        payName = "Vàng";
    } else {
        try {
            Item payItem = ItemService.gI().createNewItem((short) itemShop.iconSpec);
            if (payItem != null && payItem.template != null) {
                payName = payItem.template.name;
            }
        } catch (Exception ignore) {}
    }

    String log = "[" + ts + "]\n"
            + "Người Chơi: " + pl.name + " (ID: " + pl.getSession().userId + ")\n"
            + "Mua: Đồ Tại Shop x1\n"
            + "Thanh toán Bằng: " + payName + " | Giá: " + cost + "\n\n";

    try (BufferedWriter bw = new BufferedWriter(
            new FileWriter("C:/xampp/htdocs/lichsu_mua.txt", true))) {
        bw.write(log);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    
    public static void logVipUpgrade(Player player, int vipCu, int vipMoi, int soTienHoan) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        int vndTruocHoan = player.getSession().vnd - soTienHoan;
        int vndSauHoan = player.getSession().vnd;

        String log = "[" + timestamp + "]\n"
                + "Player: " + player.name + " (ID: " + player.getSession().userId + ")\n"
                + "Nâng VIP : VIP " + player.Saga_VIP  + " -Hoàn-> VIP " + vipMoi + "\n"
                + "Hoàn lại: " + Util.FormatNumber(soTienHoan) + " VND\n"
                + "Số dư VND trước khi hoàn: " + vndTruocHoan + "\n"
                + "Số dư VND sau khi hoàn: " + vndSauHoan + "\n\n";

    try (BufferedWriter bw = new BufferedWriter(new FileWriter("C:/xampp/htdocs/vip_hoan.txt", true))) {
            bw.write(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}