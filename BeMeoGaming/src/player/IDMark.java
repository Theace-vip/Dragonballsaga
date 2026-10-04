package player;



import boss.Boss;
import consts.ConstNpc;
import java.util.List;
import npc.Npc;
import shop.Shop;
import lombok.Data;
import map.Zone;

@Data
public class IDMark {

    private int idItemUpTop;
    private int typeChangeMap; //capsule, ngọc rồng đen...
    private int indexMenu; //menu npc
    private int typeInput; //input
    private byte typeLuckyRound; //type lucky round

    private long idPlayThachDau; //id người chơi được mời thách đấu
    private int goldThachDau; //vàng thách đấu
    private long killCharId = -9999;

    private long idEnemy; //id kẻ thù - trả thù

    private Shop shopOpen; //shop người chơi đang mở
    private String tagNameShop; //thẻ tên shop đang mở

    /**
     * loại tàu vận chuyển dùng ;0 - Không dùng ;1 - Tàu vũ trụ ;2 - Dịch chuyển
     * tức thời ;3 - Tàu tenis
     */
    private byte idSpaceShip;

    private int mbv;

    private String captcha;
    private long recaptcha;

    private long lastTimeBan;
    private boolean isBan;

    private int ott;

    //giao dịch
    private int playerTradeId = -1;
    private Player playerTrade;
    private long lastTimeTrade;

    private long lastTimeNotifyTimeHoldBlackBall;
    private long lastTimeHoldBlackBall;
    private int tempIdBlackBallHold = -1;
    private boolean holdBlackBall;

    private int tempIdNamecBallHold = -1;
    private boolean holdNamecBall;

    private boolean loadedAllDataPlayer; //load thành công dữ liệu người chơi từ database

    private long lastTimeChangeFlag;

    //xoc dia
    private int typeDatXD;
    private int slDatXD;
    private Npc npcXD;

    //Tai Xiu
    private int typeDatTX;
    private Npc npcTX;

    //Bau cua
    private int typeDatBC;
    private Npc npcBC;

    //tới tương lai
    private boolean gotoFuture;
    private long lastTimeGoToFuture;
    
      private boolean gotoFuture1;
    private long lastTimeGoToFuture1;

    //ChangeMap Khi gas
    private Zone zoneKhiGasHuyDiet;
    private int xMapKhiGasHuyDiet;
    private int yMapKhiGasHuyDiet;
    private boolean goToKGHD;
    private long lastTimeGoToKGHD;

    private long lastTimeChangeZone;
    private long lastTimeChatGlobal;
    private long lastTimeChatPrivate;

    private long lastTimePickItem;

    private boolean goToBDKB;
    private long lastTimeGoToBDKB;
    private long lastTimeAnXienTrapBDKB;

    private int shenronType = 0;

    private Npc npcChose; //npc mở

    private byte loaiThe; //loại thẻ nạp

    private boolean acpTrade;

    private double damePST;

    private long lastTimeRevenge;

    private int menuType;

    //may do boss (item 1881): list boss da gui cho client + thoi diem gui,
    //de khi client bam dong lay dung boss theo vi tri (khong goi getBoss)
    private List<Boss> bossTeleList;
    private long bossTeleListTime;

    //gop gui chi so (-42 + cap nhat mau) khi cong diem nhanh (acs)
    private long lastTimePointSend;
    private boolean pendingPointSend;

    //dem toc do yeu cau cong diem (case 16) de chan spam
    private long acsWindowTime;
    private int acsCount;

    //dedupe thong bao loi cong diem (acs loi lien tuc se spam toast)
    private long lastTimeThongBaoTiemNang;

    private int tangHoaType;

    private boolean transactionWP;

    private boolean transactionWVP;

    private long lastTimeCombine;

    private int moneyKeoBuaBao;
    private long timePlayKeoBuaBao;

    private byte keoBuaBaoPlayer;
    private byte keoBuaBaoServer;

    private boolean isGemCSMM;

    public boolean isBaseMenu() {
        return this.indexMenu == ConstNpc.BASE_MENU;
    }

    public void dispose() {
        if (this.shopOpen != null) {
            this.shopOpen.dispose();
            this.shopOpen = null;
        }
        this.npcChose = null;
        this.tagNameShop = null;
        this.playerTrade = null;
        this.npcXD = null;
        this.npcTX = null;
        this.npcBC = null;
        this.zoneKhiGasHuyDiet = null;
    }
}
