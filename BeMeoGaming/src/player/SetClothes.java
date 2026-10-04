package player;

import item.Item;

public class SetClothes {

    private Player player;

    public SetClothes(Player player) {
        this.player = player;
    }

    public byte songoku;
    public byte setsaga;
    public byte setbroly;
    public byte setpicolo;
    public byte setvegeta;
    public byte setgoku;
    public byte setphuho;
    public byte setmabu;

    public byte setkichhoat18sao;
    public byte setkichhoat30sao;
    public byte setkichhoat45sao;
    public byte setkichhoat65sao;
    public byte setkichhoat99sao;
    public byte setkichhoat200sao;
    public byte setkichhoat300sao;
    public byte setkichhoat500sao;
    public byte setkichhoat700sao;
    public byte setkichhoat999sao;
    public byte tinhluyen16;
    public byte setduongtangs;
    public byte setduongtangss;
    public byte thienXinHang;
    public byte kirin;

    /**
     * Dem so mon trong 5 mon dau (ao/quan/gang/giay/nhan) mang tung option.
     * Dung chung cho cac set cau hinh tren panel (bang set_config): muon biet
     * 1 option co du 5/5 mon thi goi countSet(optionId).
     */
    public final java.util.Map<Integer, Integer> setCount = new java.util.HashMap<>();

    public int countSet(int optionId) {
        Integer v = setCount.get(optionId);
        return v == null ? 0 : v;
    }

    public byte ocTieu;
    public byte pikkoroDaimao;
    public byte picolo;

    public byte kakarot;
    public byte cadic;
    public byte nappa;

    public byte worldcup;
    public byte setDHD;

    public boolean godClothes;
    public int ctHaiTac = -1;

    public void setup() {
        setDefault();
        this.setCount.clear();
        setupSKT();
        this.godClothes = true;
        for (int i = 0; i < 18; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id > 567 || item.template.id < 555) {
                    this.godClothes = false;
                    break;
                }
            } else {
                this.godClothes = false;
                break;
            }
        }
        Item ct = this.player.inventory.itemsBody.get(5);
        if (ct.isNotNullItem()) {
            switch (ct.template.id) {
                case 618:
                case 619:
                case 620:
                case 621:
                case 622:
                case 623:
                case 624:
                case 626:
                case 627:
                    this.ctHaiTac = ct.template.id;
                    break;

            }
        }
    }

    private void setupSKT() {
        // Chi tinh tren 5 mon dau tien: slot 0 ao, 1 quan, 2 gang, 3 giay, 4 nhan/rada.
        // Moi option dem rieng tren tung mon (khong break): set can du 5/5 mon moi kich hoat.
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item != null && item.isNotNullItem()) {
                boolean isActSet = false;
                java.util.Set<Integer> seen = new java.util.HashSet<>();
                for (Item.ItemOption io : item.itemOptions) {
                    // option trung trong cung 1 mon chi dem 1 lan
                    if (seen.add(io.optionTemplate.id)) {
                        setCount.merge(io.optionTemplate.id, 1, Integer::sum);
                    }
                    switch (io.optionTemplate.id) {
                        case 129:

                            isActSet = true;
                            songoku++;
                            break;
                        case 127:
                            isActSet = true;
                            thienXinHang++;
                            break;
                        case 128:
                            isActSet = true;
                            kirin++;
                            break;
                        case 131:
                            isActSet = true;
                            ocTieu++;
                            break;
                        case 132:
                            isActSet = true;
                            pikkoroDaimao++;
                            break;
                        case 130:
                            isActSet = true;
                            picolo++;
                            break;
                        case 66:
                            isActSet = true;
                            setsaga++;
                            break;
                        case 51:
                            isActSet = true;
                            setbroly++;
                            break;
                        case 56:
                            isActSet = true;
                            setmabu++;
                            break;
                        case 52:
                            isActSet = true;
                            setpicolo++;
                            break;
                        case 57:
                            isActSet = true;
                            setduongtangs++;
                            break;
                        case 58:
                            isActSet = true;
                            setduongtangss++;
                            break;

                        case 53:
                            isActSet = true;
                            setvegeta++;
                            break;
                        case 54:
                            isActSet = true;
                            setgoku++;
                            break;
                        case 55:
                            isActSet = true;
                            setphuho++;
                            break;                        // option 149-157 (Thanh Long...Hoa) dua vao setCount o tren,
                        // bonus doc tu bang set_config (SetConfigService) - khong dem o day nua

                        case 135:
                            isActSet = true;
                            nappa++;
                            break;
                        case 133:
                            isActSet = true;
                            kakarot++;
                            break;
                        case 134:
                            isActSet = true;
                            cadic++;
                            break;

                        case 141:
                            isActSet = true;
                            setkichhoat18sao++;
                            break;
                        case 142:
                            isActSet = true;
                            setkichhoat30sao++;
                            break;
                        case 143:
                            isActSet = true;
                            setkichhoat45sao++;
                            break;
                        case 144:
                            isActSet = true;
                            setkichhoat65sao++;
                            break;
                        case 136:
                            isActSet = true;
                            setkichhoat99sao++;
                            break;
                        case 137:
                            isActSet = true;
                            setkichhoat200sao++;
                            break;
                        case 138:
                            isActSet = true;
                            setkichhoat300sao++;
                            break;
                        case 139:
                            isActSet = true;
                            setkichhoat500sao++;
                            break;
                        case 140:
                            isActSet = true;
                            setkichhoat700sao++;
                            break;
                        case 145:
                            isActSet = true;
                            setkichhoat999sao++;
                            break;
                        case 146:
                            isActSet = true;
                            tinhluyen16++;
                            break;

                        case 21:
                            if (io.param == 80) {
                                setDHD++;
                            }
                            break;
                    }

                }
            }
        }
    }

    private void setDefault() {
        this.songoku = 0;
        this.thienXinHang = 0;
        this.kirin = 0;
        this.ocTieu = 0;
        this.pikkoroDaimao = 0;
        this.picolo = 0;
        this.setkichhoat18sao = 0;
        this.setkichhoat30sao = 0;
        this.setkichhoat45sao = 0;
        this.setkichhoat65sao = 0;
        this.setkichhoat99sao = 0;
        this.setkichhoat200sao = 0;
        this.setkichhoat300sao = 0;
        this.setkichhoat500sao = 0;
        this.setkichhoat700sao = 0;
        this.setkichhoat999sao = 0;
        this.tinhluyen16 = 0;
        this.kakarot = 0;
        this.cadic = 0;
        this.nappa = 0;

        this.setDHD = 0;
        this.setsaga = 0;
        this.setgoku = 0;
        this.setbroly = 0;
        this.setpicolo = 0;
        this.setvegeta = 0;
        this.setphuho = 0;
        this.setmabu = 0;
        this.setduongtangs = 0;
        this.setduongtangss = 0;
        this.worldcup = 0;
        this.godClothes = false;
        this.ctHaiTac = -1;
    }

    public boolean checkSetGod() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id < 555 || item.template.id > 567) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean checkSetDes() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id < 650 || item.template.id > 662) {

                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public void dispose() {
        this.player = null;
    }
}
