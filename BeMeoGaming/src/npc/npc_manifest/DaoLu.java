package npc.npc_manifest;

/**
 *

 */

import consts.ConstNpc;
import npc.Npc;
import player.Player;
import services.NpcService;
import services.Service;
import shop.ShopService;
import utils.Util;

public class DaoLu extends Npc {

    public DaoLu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player pl) {
        if (canOpenNpc(pl)) {
           if (pl.TamkjllPetGiong != -1) {
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.Saga_EmBe, -1,
                            "|7|Đạo Lữ SSS\n"
                            + "|7|Name Đạo Lữ: " + pl.TamkjllNamePet + " \n"
                            + "|7|LV Đạo Lữ: (" + (pl.EmBeEXP * 100 / (3000000L + pl.EmBeLv * 1500000L))
                            + "%)-LV: "
                            + pl.EmBeLv + "\n"
                            + "|4|Linh Hồn: " + pl.LinhCanEmBe(pl.TamkjllPetGiong) + "\n"
                            + "Thức ăn: " + pl.TamkjllPetHunger + "%\n"
                            + "Sức mạnh: " + Util.getFormatNumber(pl.TamkjllPetPower) + "\n"
                            + "cần 15 phút để load hoặc thoát game ra vào lại\n"
                            + "Hãy Cho Đạo Lữ Ăn Liên Tục Không Sẽ Chết Đói Đó\n"
                            + "Chết Đạo Lữ Sẽ Bỏ Nhà Gia Đi\n"
                            + "|6|Chủ Nhân Hãy Ra Lệnh Cho Ta\n",
                            "Đi Theo", "PK Người", "Pk quái",
                            "Về Nhà", "Kĩ Năng\nĐạo Lữ");
                } else {
                    Service.gI().sendThongBaoOK(pl, "Bạn Chưa Có Đạo Lữ Để Dùng Tính Năng Này");
                }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {

        }
    }
}
