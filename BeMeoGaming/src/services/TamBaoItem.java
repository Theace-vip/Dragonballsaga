package services;

import item.Item;

/**
 * Vật phẩm của một mốc Tầm Bảo (Vòng Quay) - port từ bản src (nro/services/TamBao_Item.java).
 *
 * @author Hoàng Việt - 0857853150
 */
public class TamBaoItem extends Item {

    /** id mốc trong bảng moc_vong_quay - client gửi lại đúng id này khi bấm nhận thưởng. */
    public int id_moc;

    /** Số điểm quay cần có để nhận mốc này (client so với điểm quay - diem_quay). */
    public int max_value;
}
