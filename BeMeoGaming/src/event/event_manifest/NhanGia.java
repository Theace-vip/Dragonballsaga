package event.event_manifest;

import boss.BossID;
import event.Event;

public class NhanGia extends Event {
    @Override
    public void npc() {
        createNpc(20, 78, 1117, 360);
    }
    @Override
    public void boss(){
        createBoss(BossID.CUU_VI, 3);
    }
    
    
}
