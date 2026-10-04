package event;

/**
 *

 */

import event.event_manifest.TopUp;
import event.event_manifest.TrungThu;
import event.event_manifest.HungVuong;
import event.event_manifest.Christmas;
import event.event_manifest.DaiChienThanThu;
import event.event_manifest.Halloween;
import event.event_manifest.LunarNewYear;
import event.event_manifest.NhanGia;
import event.event_manifest.Default;
import event.event_manifest.InternationalWomensDay;

public class EventManager {

    private static EventManager instance;

    public static boolean LUNNAR_NEW_YEAR = false;

    public static boolean INTERNATIONAL_WOMANS_DAY = false;

    public static boolean CHRISTMAS = false;

    public static boolean HALLOWEEN = true;

    public static boolean HUNG_VUONG = true;

    public static boolean TRUNG_THU = panel.tuning.TrungThuTuning.isOn();

    public static boolean TOP_UP = false;

    public static boolean GIAI_CUU_NHAN_GIOI = false;

    public static boolean DAI_CHIEN_THAN_THU = false;

    public static EventManager gI() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    public void init() {
        new Default().init();
        if (LUNNAR_NEW_YEAR) {
            new LunarNewYear().init();
        }
        if(DAI_CHIEN_THAN_THU){
            new DaiChienThanThu().init();
        }
        if(GIAI_CUU_NHAN_GIOI){
            new NhanGia().init();
        }
        if (INTERNATIONAL_WOMANS_DAY) {
            new InternationalWomensDay().init();
        }
        if (HALLOWEEN) {
            new Halloween().init();
        }
        if (CHRISTMAS) {
            new Christmas().init();
        }
        if (HUNG_VUONG) {
            new HungVuong().init();
        }
        if (TRUNG_THU) {
            new TrungThu().init();
        }
        if (TOP_UP) {
            new TopUp().init();
        }
    }
}
