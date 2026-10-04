package event.event_manifest;

import boss.BossID;
import event.Event;

public class DaiChienThanThu extends Event {
    @Override
    public void npc() {
        createNpc(5, 71, 306, 288);
    }
    @Override
    public void boss(){
        createBoss(BossID.THIET_MA);
        createBoss(BossID.HAI_KHUYEN);
        createBoss(BossID.BACH_LANG);
        createBoss(BossID.VUA_BACH_THU);
    }
    
    
}
