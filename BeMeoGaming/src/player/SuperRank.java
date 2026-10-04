package player;

/**
 *
 *
 */
import item.Item;
import java.util.ArrayList;
import java.util.List;
import jdbc.daos.PlayerDAO;
import models.SuperRank.SuperRankService;
import services.InventoryService;
import services.ItemService;
import services.NpcService;
import services.Service;
import utils.TimeUtil;

public class SuperRank {

    private Player player;
    public int rank;
    public int win;
    public int lose;
    public List<String> history;
    public List<Long> lastTime;
    public long lastTimePK;
    public long lastTimeReward;
    public int ticket = 1;

    public SuperRank(Player player) {
        this.player = player;
        this.history = new ArrayList<>();
        this.lastTime = new ArrayList<>();
    }

    public void history(String text, long lastTime) {
        if (this.history.size() > 4) {
            this.history.remove(0);
            this.lastTime.remove(0);
        }
        this.history.add(text);
        this.lastTime.add(lastTime);
    }

  public void reward() {
    int rw = SuperRankService.gI().reward(rank);
    if (rw <= 0) {
        lastTimeReward = TimeUtil.currentTimeMillisPlus11();
        return;
    }
    int cur = player.getSession().vnd;
    int add = rw;
    if (cur < 0) cur = 0;
    if (add < 0) add = 0;

    int MAX = 20000000;
    int after = cur + add;
    if (after > MAX) after = MAX;

    player.getSession().vnd = after;

    // Lưu DB và cập nhật UI tiền
    PlayerDAO.updateVND(player);
    Service.getInstance().sendMoney(player);

    // Thông báo đổi sang Coin
   Service.gI().sendThongBaoFromAdmin(player, "|7|Bạn đang ở TOP [" + rank + "]\n|4|võ đài Siêu Hạng Pro Max\n|6|được thưởng " + rw + " Coin Hàng ngày Hãy Phấn Đấu");
     

    lastTimeReward = TimeUtil.currentTimeMillisPlus11();
}

    public void dispose() {
        history.clear();
        lastTime.clear();
        win = -1;
        lose = -1;
        lastTimePK = -1;
        player = null;
    }
}
