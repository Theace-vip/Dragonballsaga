package panel.tuning;

import item.Item.ItemOption;
import java.util.ArrayList;
import java.util.List;

/**
 * DropEntry - 1 dong drop cua boss/mob.
 *  - tempId : temp_id vat pham
 *  - qty    : so luong
 *  - options: danh sach option (id,param) - param la long
 *  - rate   : ty le roi %, 1-100 (100 = chac chan)
 *  - minParam/maxParam: neu >0 thi random param cua TAT CA option trong khoang [min,max]
 *    (dung cho truong hop "cai trang 25k-30k % suc danh")
 */
public class DropEntry {
    public int tempId;
    public int qty = 1;
    public List<ItemOption> options = new ArrayList<>();
    public int rate = 100;
    public long minParam = -1;
    public long maxParam = -1;

    /** Clone moi moi lan roi (tranh share option object). */
    public DropEntry copy() {
        DropEntry e = new DropEntry();
        e.tempId = this.tempId;
        e.qty = this.qty;
        e.rate = this.rate;
        e.minParam = this.minParam;
        e.maxParam = this.maxParam;
        for (ItemOption o : this.options) {
            e.options.add(new ItemOption(o.optionTemplate.id, o.param));
        }
        return e;
    }
}
