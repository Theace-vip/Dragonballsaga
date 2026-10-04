package services;

/**
 * Cấu hình 1 tab Phúc Lợi = 1 dòng bảng phuc_loi.
 *
 * @author Hoàng Việt - 0857853150
 */
public class PhucLoiManager {

    public String tab_name;
    public int id_tab;
    public int max_tab;
    public String info_phucloi;
    public int action;
    public String tichLuy;
    /**
     * Tien te tra tien cua tab: 0 = Coin, >0 = id item trong han trang
     * (vd 1271 Luong Bac, 1270 Luong Vuang, 457 Thoi Vuang, 77 Ngoc, 1150 Co 4 La).
     */
    public int currencyItem;

}
