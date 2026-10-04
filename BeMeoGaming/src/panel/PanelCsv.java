package panel;

import java.util.ArrayList;
import java.util.List;

/** CSV UTF-8: Ten|TempID|Gia|LoaiTien|IsNew|IsSell|Options(47:6666|50:100)|ItemSpec */
public class PanelCsv {
    public static String exportTab(List<ShopItemModel> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ten,TempID,Gia,LoaiTien,IsNew,IsSell,Options,ItemSpec\n");
        for (ShopItemModel m : list) {
            String name = m.itemName == null ? "" : m.itemName.replace(",", " ").replace("\n", " ");
            sb.append(name).append(",").append(m.tempId).append(",").append(m.cost).append(",")
              .append(PanelService.typeSellName(m.typeSell)).append(",")
              .append(m.isNew ? 1 : 0).append(",").append(m.isSell ? 1 : 0).append(",")
              .append(m.optionsCsv()).append(",").append(m.itemSpec).append("\n");
        }
        return sb.toString();
    }
    public static class ImportResult { public List<ShopItemModel> items = new ArrayList<>(); public List<String> errors = new ArrayList<>(); }
    public static ImportResult importCsv(String csv) {
        ImportResult r = new ImportResult();
        if (csv == null) { r.errors.add("CSV rong"); return r; }
        String[] lines = csv.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;
            if (i == 0 && line.toLowerCase().startsWith("ten,")) continue;
            String[] c = line.split(",", -1);
            if (c.length < 7) { r.errors.add("Dong " + (i+1) + ": thieu cot"); continue; }
            try {
                ShopItemModel m = new ShopItemModel();
                m.itemName = c[0].trim();
                m.tempId = Integer.parseInt(c[1].trim());
                m.cost = Integer.parseInt(c[2].trim());
                m.typeSell = PanelService.parseTypeSell(c[3].trim());
                m.isNew = c[4].trim().equals("1") || c[4].trim().equalsIgnoreCase("true");
                m.isSell = c[5].trim().equals("1") || c[5].trim().equalsIgnoreCase("true");
                String opt = c[6].trim();
                if (!opt.isEmpty()) {
                    for (String p : opt.split("\\|")) {
                        String[] kv = p.trim().split(":");
                        if (kv.length == 2) m.options.add(new ShopItemModel.Opt(Integer.parseInt(kv[0].trim()), Long.parseLong(kv[1].trim())));
                    }
                }
                if (c.length >= 8) { try { m.itemSpec = Integer.parseInt(c[7].trim()); } catch (Exception e) {} }
                r.items.add(m);
            } catch (Exception e) { r.errors.add("Dong " + (i+1) + ": " + e.getMessage()); }
        }
        return r;
    }
}
