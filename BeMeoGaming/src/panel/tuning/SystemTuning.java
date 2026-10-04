package panel.tuning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SystemTuning - bang so lieu "he thong game" (Thien Dao, Dia Dao, Chuyen Sinh, Tu Tien, ...).
 *
 * Truoc day cac con so nay nam rai rac trong code (NpcFactory, SkillService, PlayerService,
 * Player, NPoint...). Gio gom het ve day, admin sua tren panel (tab "He Thong"), ap dung ngay,
 * khong can build lai.
 *
 * - MOI key phai duoc dang ky trong defaults() kem nhan tieng Viet (de hien thi tren panel).
 * - Gia tri luu o data/config/system_tuning.properties (chi luu key bi doi).
 * - Doc bang get()/getL()/getI()/getB() - ham rat nhe, goi thoai mai trong code game.
 */
public class SystemTuning {

    private static final String PATH = "data/config/system_tuning.properties";

    private static final Map<String, Double> DEF = new LinkedHashMap<>();
    private static final Map<String, String> LABEL = new LinkedHashMap<>();
    private static final Map<String, String> GROUP = new LinkedHashMap<>();
    /** Phan nho trong 1 nhom (tang 3 tren panel) - giup tab "He Thong" khong con 1 bang dai. */
    private static final Map<String, String> SECTION = new LinkedHashMap<>();
    /** Chi chua cac key bi doi (override) - doc khong can lock. */
    private static volatile Map<String, Double> VAL = new ConcurrentHashMap<>();

    private static void reg(String group, String section, String key, double def, String label) {
        GROUP.put(key, group);
        SECTION.put(key, section);
        DEF.put(key, def);
        LABEL.put(key, label);
    }

    /**
     * Dang ky toan bo con so he thong co the sua tren panel.
     *
     * Cau truc 3 tang tren panel: NHOM (group) -> PHAN (section) -> TUNG THONG SO (key).
     * Nhom nao cung tu dong hien ra o tang 1, khong phai sua code giao dien khi them nhom moi.
     *
     * QUY UOC: moi nhom gom 2-4 phan, moi phan 2-6 thong so (de nhin 1 man hinh la thay het,
     * khong phai keo chuot qua lai tim dong).
     */
    private static void defaults() {
        // ================= THIEN DAO =================
        reg("Thien Dao", "Nang cap & diem", "thiendao_diem_moi_cap", 500000, "Diem Phap Tac can cho moi cap (nhan voi cap+1)");
        reg("Thien Dao", "Nang cap & diem", "thiendao_cap_toi_da", 5000, "Cap Thien Dao toi da (auto nang khi bat)");
        reg("Thien Dao", "Moc hieu ung", "thiendao_moc_30", 30, "Moc cap 1: bat hieu ung dac biet (Khinh cong/Noi tai)");
        reg("Thien Dao", "Moc hieu ung", "thiendao_moc_70", 70, "Moc cap 2: bat Cap Phong Ba + dame theo cap");
        reg("Thien Dao", "Moc hieu ung", "thiendao_moc_100", 100, "Moc cap 3: bat bonus dame/giap them");
        reg("Thien Dao", "Moc hieu ung", "thiendao_moc_500", 500, "Moc cap 4: bat bonus cho De tu (HP/KI/SD)");
        reg("Thien Dao", "Thuong theo cap", "thiendao_roi_cs_goc_moi_cap", 20, "Ti le roi Cai Sinh Goc cong them moi cap (%)");
        reg("Thien Dao", "Thuong theo cap", "thiendao_giam_st_boss", 53, "Giam sat thuong cua Boss cong them (theo cap)");

        // ================= DIA DAO =================
        reg("Dia Dao", "Nang cap & diem", "diadao_diem_goc", 7000000, "Diem Nhap Ma can cho cap Dia Dao dau tien");
        reg("Dia Dao", "Nang cap & diem", "diadao_diem_moi_cap", 200000, "Diem Nhap Ma cong them moi cap Dia Dao");
        reg("Dia Dao", "Nang cap & diem", "diadao_cap_toi_da", 10000, "Cap Dia Dao toi da");

        // ================= CHUYEN SINH (SU PHU) =================
        reg("Chuyen Sinh (Su Phu)", "Dieu kien vao", "cs_tutien_yeucau", 19, "Can dat canh gioi Tu Tien (moc SagaTuTien[1])");
        reg("Chuyen Sinh (Su Phu)", "Dieu kien vao", "cs_sm_yeucau", 150000000000000d, "Suc manh toi thieu de chuyen sinh");
        reg("Chuyen Sinh (Su Phu)", "Chi phi", "cs_vang", 500000000000d, "Vang can cho 1 lan chuyen sinh");
        reg("Chuyen Sinh (Su Phu)", "Chi phi", "cs_luong_bac", 50000, "Luong Bac ghi tren menu (yeu cau)");
        reg("Chuyen Sinh (Su Phu)", "Chi phi", "cs_luong_bac_tru", 2000, "Luong Bac thuc tru khi chuyen sinh (kiem tra lai so nay)");
        reg("Chuyen Sinh (Su Phu)", "Ti le thanh cong", "cs_tile_thanh_cong", 20, "Ti le thanh cong (% tren 100)");
        reg("Chuyen Sinh (Su Phu)", "Thuong sau khi chuyen sinh", "cs_hp_goc", 500000, "HP goc cong moi lan chuyen sinh");
        reg("Chuyen Sinh (Su Phu)", "Thuong sau khi chuyen sinh", "cs_ki_goc", 500000, "KI goc cong moi lan chuyen sinh");
        reg("Chuyen Sinh (Su Phu)", "Thuong sau khi chuyen sinh", "cs_sd_goc", 50000, "Suc danh goc cong moi lan chuyen sinh");
        reg("Chuyen Sinh (Su Phu)", "Thuong sau khi chuyen sinh", "cs_giap_goc", 1000, "Giap goc cong moi lan chuyen sinh");

        // ================= CHUYEN SINH DE TU =================
        reg("Chuyen Sinh (De Tu)", "Dieu kien vao", "cspet_tutien_yeucau", 170, "De tu: can dat canh gioi Tu Tien");
        reg("Chuyen Sinh (De Tu)", "Dieu kien vao", "cspet_diem_fam", 500000, "De tu: diem Fam can (cong them 2 moi cap CS hien co)");
        reg("Chuyen Sinh (De Tu)", "Chi phi", "cspet_vang", 500000000000d, "De tu: vang can");
        reg("Chuyen Sinh (De Tu)", "Chi phi", "cspet_luong_bac", 50000, "De tu: Luong Bac can");
        reg("Chuyen Sinh (De Tu)", "Ti le thanh cong", "cspet_tile_thanh_cong", 15, "De tu: ti le thanh cong (tren 50)");
        reg("Chuyen Sinh (De Tu)", "Ti le thanh cong", "cspet_bonus_thiendo", 3, "De tu: cong them ti le neu Thien Dao >= 5000");
        reg("Chuyen Sinh (De Tu)", "Thuong sau khi chuyen sinh", "cspet_hp_goc", 50000, "De tu: HP goc cong moi lan");
        reg("Chuyen Sinh (De Tu)", "Thuong sau khi chuyen sinh", "cspet_ki_goc", 50000, "De tu: KI goc cong moi lan");
        reg("Chuyen Sinh (De Tu)", "Thuong sau khi chuyen sinh", "cspet_sd_goc", 1000, "De tu: suc danh goc cong moi lan");
        reg("Chuyen Sinh (De Tu)", "Thuong sau khi chuyen sinh", "cspet_giap_goc", 100, "De tu: giap goc cong moi lan");

        // ================= TU TIEN =================
        reg("Tu Tien", "Tu luyen (chay tu dong)", "tutien_linh_khi_moi_giay", 28999, "Linh khi tu luyen tu dong moi giay");
        reg("Tu Tien", "Tu luyen (chay tu dong)", "tutien_tho_nguyen_moi_giay", 5, "Tho nguyen tieu hao moi giay");
        reg("Tu Tien", "Tay Luyen / Do Kiep", "tutien_chi_phi_tay_luyen", 500000000d, "Linh khi de Tay Luyen Thien Phu");
        reg("Tu Tien", "Tay Luyen / Do Kiep", "tutien_chi_phi_do_kiep", 50000000d, "Linh khi de Do Kiep (dot pha canh gioi)");
        reg("Tu Tien", "Tay Luyen / Do Kiep", "tutien_canh_gioi_toi_da", 340, "Canh gioi Tu Tien toi da (Do Kiep)");
        reg("Tu Tien", "Phap Chi Do Kiep", "tutien_phap_chi_1", 3, "Phap Chi do kiep loai 1: +% ti le");
        reg("Tu Tien", "Phap Chi Do Kiep", "tutien_phap_chi_2", 5, "Phap Chi do kiep loai 2: +% ti le");
        reg("Tu Tien", "Phap Chi Do Kiep", "tutien_phap_chi_3", 7, "Phap Chi do kiep loai 3: +% ti le");
        reg("Tu Tien", "Phap Chi Do Kiep", "tutien_phap_chi_4", 13, "Phap Chi do kiep loai 4: +% ti le");
        reg("Tu Tien", "Phap Chi Do Kiep", "tutien_phap_chi_5", 15, "Phap Chi do kiep loai 5: +% ti le");
        reg("Tu Tien", "Phap Chi Do Kiep", "tutien_phap_chi_6", 35, "Phap Chi do kiep loai 6: +% ti le");

        // ================= DIEM FARM (mob drop diem fam - Mob.getItemMobReward) =================
        reg("Diem Farm", "Qua mob", "farm_min", 500, "Diem Fam toi thieu moi kill");
        reg("Diem Farm", "Qua mob", "farm_base_max", 1500, "Gia tri goc toi da (truoc khi cong VIP)");
        reg("Diem Farm", "Qua mob", "farm_vip_moi", 10000, "Cong them vao toi da moi cap VIP");
        reg("Diem Farm", "Qua mob", "farm_toi_da", 150000, "Tran toi da diem Fam moi kill (0 = khong gioi han)");

        // [The Diem Farm 27/09/2026] the x2/x3/x5 (item 1916/1917/1918) + ve tang diem (1251)
        reg("Diem Farm", "The diem farm", "farm_card_thoi_gian", 3600, "Thoi luong buff cua The diem farm (giay)");
        reg("Diem Farm", "The diem farm", "farm_card_vuot_tran", 1, "The co duoc vuot tran farm_toi_da (1 = co, 0 = bi chan lai)");
        reg("Diem Farm", "The diem farm", "fam_transfer_min", 1000, "So diem toi thieu moi lan tang diem (Ve Tang Diem)");
        reg("Diem Farm", "The diem farm", "fam_transfer_max", 10000000, "So diem toi da moi lan tang diem (Ve Tang Diem)");

        // ================= DAO LU / EM BE (pet Tamkjll) =================
        reg("Dao Lu / Em Be", "Thuc an", "embe_chu_ky_thuc_an", 900000, "Chu ky tru thuc an (ms) - mac dinh 900000 = 15 phut");
        reg("Dao Lu / Em Be", "Thuc an", "embe_thuc_an_tru_moi_chu_ky", 1, "So diem thuc an tru moi chu ky");
        reg("Dao Lu / Em Be", "Thuc an", "embe_sm_moi_chu_ky_min", 50, "Suc manh pet nhan moi chu ky (toi thieu)");
        reg("Dao Lu / Em Be", "Thuc an", "embe_sm_moi_chu_ky_max", 100, "Suc manh pet nhan moi chu ky (toi da)");
        reg("Dao Lu / Em Be", "Thuc an", "embe_cho_an_linh_khi", 5000000000d, "Linh khi tru khi cho an qua NPC (menu '5k Ruby')");
        reg("Dao Lu / Em Be", "Thuc an", "embe_cho_an_min", 1, "Thuc an nhan toi thieu moi lan cho an");
        reg("Dao Lu / Em Be", "Thuc an", "embe_cho_an_max", 20, "Thuc an nhan toi da moi lan cho an");
        reg("Dao Lu / Em Be", "Thuc an", "embe_thuc_an_toi_da", 500, "Cho an vuot qua do nay thi em be bi bo nha (no qua)");
        reg("Dao Lu / Em Be", "Thuc an", "embe_canh_bao_do", 10, "Canh bao thuc an duoi muc nay (%)");
        reg("Dao Lu / Em Be", "Tao pet", "embe_thuc_an_dau", 80, "Thuc an khi moi tao pet");
        reg("Dao Lu / Em Be", "Tao pet", "embe_exp_dau", 500, "Exp khi moi tao pet");
        reg("Dao Lu / Em Be", "Tao pet", "embe_giong_min", 0, "Linh can toi thieu khi tao pet");
        reg("Dao Lu / Em Be", "Tao pet", "embe_giong_max", 9, "Linh can toi da khi tao pet (set 10 de cho phep roll HON DON TIEN LINH CAN)");
        reg("Dao Lu / Em Be", "Tao pet", "embe_ten_toi_thieu", 4, "Do dai ten em be toi thieu (ky tu)");
        reg("Dao Lu / Em Be", "Tao pet", "embe_ten_toi_da", 20, "Do dai ten em be toi da (ky tu)");
        reg("Dao Lu / Em Be", "Tao pet", "embe_trung_tru", 0, "So trung (item 457) tru khi sinh em be (0 = mien phi)");
        reg("Dao Lu / Em Be", "Tao pet", "embe_can_ket_hon", 1, "So lan ket hon toi thieu de duoc sinh em be");
        reg("Dao Lu / Em Be", "Len cap", "embe_lv_moi_dan_min", 1, "Cap Em Be tang toi thieu moi lan pet danh");
        reg("Dao Lu / Em Be", "Len cap", "embe_lv_moi_dan_max", 50, "Cap Em Be tang toi da moi lan pet danh");
        reg("Dao Lu / Em Be", "Len cap", "embe_exp_goc", 3000000, "Exp can cho lan len cap dau (cong them lv * embe_exp_moi_lv)");
        reg("Dao Lu / Em Be", "Len cap", "embe_exp_moi_lv", 1500000, "Exp tang them moi cap hien tai");
        reg("Dao Lu / Em Be", "Len cap", "embe_sm_len_lv_goc", 500000000, "Suc manh pet nhan khi len 1 cap (goc)");
        reg("Dao Lu / Em Be", "Len cap", "embe_sm_len_lv_giong", 100000000, "Suc manh tang them moi diem linh can khi len cap");
        reg("Dao Lu / Em Be", "Len cap", "embe_ti_le_tim_do", 2, "Ti le pet tim duoc ngoc cho chu - linh can 0/10 (don vi 0.02 = 0.02%% nhu code cu)");
        reg("Dao Lu / Em Be", "Chi so nhan duoc", "embe_hp_moi_lv", 1, "%% HP/KI/Giap tang moi cap Em Be (linh can 4/9/10)");
        reg("Dao Lu / Em Be", "Chi so nhan duoc", "embe_dame_moi_lv", 2, "%% Suc danh tang moi cap Em Be (linh can 5/9/10)");
        reg("Dao Lu / Em Be", "Chi so nhan duoc", "embe_cm_moi_lv", 2, "%% SD chi menh tang moi cap Em Be (linh can 6/9/10)");
        reg("Dao Lu / Em Be", "Item cho an", "embe_item_1662_lv", 1, "Item 1662: tang bao nhieu cap Em Be");
        reg("Dao Lu / Em Be", "Item cho an", "embe_item_1663_thuc_an", 1, "Item 1663 (thuc an nho): tang bao nhieu diem thuc an");
        reg("Dao Lu / Em Be", "Item cho an", "embe_item_1664_thuc_an", 10, "Item 1664 (thuc an lon): tang bao nhieu diem thuc an");

        // ================= KET HON =================
        reg("Ket Hon", "Vat lieu nhan nhan (1213)", "kh_oc_bien", 500, "Oc Bien (1857) can cho nhan ket hon");
        reg("Ket Hon", "Vat lieu nhan nhan (1213)", "kh_cua_bien", 500, "Cua Bien (1858) can cho nhan ket hon");
        reg("Ket Hon", "Vat lieu nhan nhan (1213)", "kh_sao_bien", 100, "Sao Bien (1859) can cho nhan ket hon");
        reg("Ket Hon", "Vat lieu nhan nhan (1213)", "kh_so_bien", 100, "So Bien (1860) can cho nhan ket hon");
        reg("Ket Hon", "Vat lieu nhan nhan (1213)", "kh_zenni", 99999, "Zenni (457) can cho nhan ket hon (dieu kien)");
        reg("Ket Hon", "Vat lieu nhan nhan (1213)", "kh_zenni_tru", 9999, "Zenni THUC TRU khi nhan nhan");
        reg("Ket Hon", "Vat lieu nhan nhan (1213)", "kh_ruby_check", 9999999, "Hong ngoc yeu cau (dieu kien kiem tra)");
        reg("Ket Hon", "Vat lieu nhan nhan (1213)", "kh_ruby_tru", 9999, "Hong ngoc THUC TRU khi nhan nhan");
        reg("Ket Hon", "Vat lieu nhan nhan (1213)", "kh_bong_hong", 99, "Bong hong (723) can cho nhan ket hon");
        reg("Ket Hon", "Dieu kien he so", "kh_vip", 4, "InGameVIP toi thieu de nhan nhan");
        reg("Ket Hon", "Dieu kien he so", "kh_chuyen_sinh", 30, "So lan chuyen sinh toi thieu");
        reg("Ket Hon", "Dieu kien he so", "kh_tutien", 90, "Canh gioi Tu Tien toi thieu");
        reg("Ket Hon", "Dieu kien he so", "kh_bdkb", 10000, "Diem Ban Do Kho Bau toi thieu");
        reg("Ket Hon", "Cau hon", "kh_cai_trang_cau_hon", 1046, "ID cai trang (Nu Thong) phai mang de cau hon");
        reg("Ket Hon", "Cau hon", "kh_dakethon_toi_da", 20, "So lan CAU HON toi da cua 1 nhan vat");
        reg("Ket Hon", "Cau hon", "kh_duockethon_toi_da", 10, "So lan DUOC CAU HON toi da cua 1 nhan vat");
        reg("Ket Hon", "Quyen loi", "kh_ruby_check_menu", 9999, "So hong ngoc hien tren menu (text) - rieng kh_ruby_check la dieu kien thuc");

        // ================= CAY PHEP (Dau Than) =================
        reg("Cay Phep", "Nang cap nhanh", "cayphep_ngoc_ket_hat", 4, "Ngoc tru khi Ket hat nhanh (0 = mien phi)");
        reg("Cay Phep", "Nang cap nhanh", "cayphep_ngoc_nang_cap", 9, "Ngoc tru khi Nang cap nhanh (0 = mien phi)");
        reg("Cay Phep", "Chi phi", "cayphep_vang_ti_le", 100, "Ti le vang nang cap boi sung (%% - 100 = nhu cu)");
        reg("Cay Phep", "Thoi gian", "cayphep_thoi_gian_ti_le", 100, "Ti le thoi gian nang cap (%% - 100 = nhu cu, giam = nhanh hon)");

        // ================= HIEU UNG KET LIET QUAI =================
        reg("Hieu Ung Ket Liet", "Tong quan", "killfx_enable", 1, "Bat hieu ung ket liu quai khi player giet quai (1 = bat, 0 = tat)");
        reg("Hieu Ung Ket Liet", "Tong quan", "killfx_vip", 3, "VIP toi thieu (Saga_VIP) de co hieu ung ket liu");

        // ================= VIP =================
        // Gia mua/nang VIP tai NPC Ong Gohan
        reg("VIP", "Gia mua/nang VIP", "vip_gia_1", 10000, "Gia mua VIP 1 (Cash)");
        reg("VIP", "Gia mua/nang VIP", "vip_gia_2", 75000, "Gia mua VIP 2 (Cash)");
        reg("VIP", "Gia mua/nang VIP", "vip_gia_3", 250000, "Gia mua VIP 3 (Cash)");
        reg("VIP", "Gia mua/nang VIP", "vip_gia_4", 550000, "Gia mua VIP 4 (Cash)");
        reg("VIP", "Gia mua/nang VIP", "vip_gia_5", 1200000, "Gia mua VIP 5 (Cash)");
        reg("VIP", "Gia mua/nang VIP", "vip_gia_6", 2100000, "Gia mua VIP 6 (Cash)");
        reg("VIP", "Gia mua/nang VIP", "vip_gia_7", 3250000, "Gia mua VIP 7 (Cash)");
        reg("VIP", "Gia mua/nang VIP", "vip_gia_8", 4950000, "Gia mua VIP 8 (Cash)");
        reg("VIP", "Gia mua/nang VIP", "vip_gia_9", 10000000, "Gia mua VIP 9 (Cash)");
        reg("VIP", "Gia mua/nang VIP", "vip_gia_10", 15000000, "Gia mua VIP 10 (Cash)");
        reg("VIP", "Gia mua/nang VIP", "vip_gia_11", 25000000, "Gia mua VIP 11 (Cash)");
        reg("VIP", "Gia mua/nang VIP", "vip_gia_12", 35000000, "Gia mua VIP 12 (Cash)");
        reg("VIP", "Gia mua/nang VIP", "vip_gia_13", 49500000, "Gia mua VIP 13 (Cash)");
        reg("VIP", "Thoi han & hoan tien", "vip_ngay", 31, "So ngay VIP nhan duoc khi mua/nang cap");
        reg("VIP", "Thoi han & hoan tien", "vip_hoan", 40, "Phan tram hoan lai phi VIP cu (%)");
        reg("VIP", "Thoi han & hoan tien", "vip_nap_moi", 5000000, "Moi nap X Cash thi tu dong +1 VIP");
        reg("VIP", "Aura theo VIP", "vip_aura_0", 40, "Aura mac dinh (khong VIP / ngoai danh sach)");
        reg("VIP", "Aura theo VIP", "vip_aura_1", 40, "Aura VIP 1");
        reg("VIP", "Aura theo VIP", "vip_aura_2", 41, "Aura VIP 2");
        reg("VIP", "Aura theo VIP", "vip_aura_3", 42, "Aura VIP 3");
        reg("VIP", "Aura theo VIP", "vip_aura_4", 19, "Aura VIP 4");
        reg("VIP", "Aura theo VIP", "vip_aura_5", 19, "Aura VIP 5");
        reg("VIP", "Aura theo VIP", "vip_aura_6", 19, "Aura VIP 6");
        reg("VIP", "Aura theo VIP", "vip_aura_7", 19, "Aura VIP 7");
        reg("VIP", "Aura theo VIP", "vip_aura_8", 19, "Aura VIP 8");
        reg("VIP", "Aura theo VIP", "vip_aura_9", 40, "Aura VIP 9");
        reg("VIP", "Aura theo VIP", "vip_aura_10", 40, "Aura VIP 10");
        reg("VIP", "Aura theo VIP", "vip_aura_11", 40, "Aura VIP 11");
        reg("VIP", "Aura theo VIP", "vip_aura_12", 40, "Aura VIP 12");
        reg("VIP", "Aura theo VIP", "vip_aura_13", 40, "Aura VIP 13");
    }

    static {
        try {
            defaults();
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============ DOC ============
    public static double get(String key) {
        try {
            Double v = VAL.get(key);
            if (v != null) return v;
            Double d = DEF.get(key);
            return d == null ? 0 : d;
        } catch (Exception e) {
            return 0;
        }
    }

    public static long getL(String key) {
        return (long) get(key);
    }

    public static int getI(String key) {
        return (int) get(key);
    }

    public static boolean getB(String key) {
        return get(key) > 0;
    }

    // ============ GHI ============
    public static synchronized void set(String key, double value) {
        if (key == null) return;
        Map<String, Double> m = new ConcurrentHashMap<>(VAL);
        m.put(key, value);
        VAL = m;
        save();
    }

    public static synchronized void load() {
        Properties p = new Properties();
        Map<String, Double> m = new ConcurrentHashMap<>();
        try {
            File f = new File(PATH);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                save();
                return;
            }
            try (FileInputStream in = new FileInputStream(f)) {
                p.load(in);
            }
            for (String name : new TreeSet<>(p.stringPropertyNames())) {
                try {
                    m.put(name, Double.parseDouble(p.getProperty(name).trim()));
                } catch (Exception e) {
                }
            }
            VAL = m;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void save() {
        Properties p = new Properties();
        try {
            Map<String, Double> snap = VAL;
            for (String k : new TreeSet<>(snap.keySet())) {
                p.setProperty(k, String.valueOf(snap.get(k)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File f = new File(PATH);
            f.getParentFile().mkdirs();
            try (FileOutputStream o = new FileOutputStream(f)) {
                p.store(o, "SystemTuning - so lieu he thong game, admin sua tren panel (tab He Thong)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============ THONG TIN CHO PANEL ============
    public static List<String> keys() {
        return new ArrayList<>(DEF.keySet());
    }

    public static String label(String key) {
        String s = LABEL.get(key);
        return s == null ? key : s;
    }

    public static String group(String key) {
        String s = GROUP.get(key);
        return s == null ? "Khac" : s;
    }

    /** Phan nho trong nhom (vi du "Chi phi", "Ti le thanh cong"...). */
    public static String section(String key) {
        String s = SECTION.get(key);
        return s == null ? "" : s;
    }

    /** Gia tri mac dinh trong code (de biet da bi doi hay chua). */
    public static double def(String key) {
        Double d = DEF.get(key);
        return d == null ? 0 : d;
    }
}
