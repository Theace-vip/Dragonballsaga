package models.Combine;

import item.Item;
import player.Player;

public class CombineUtil {

    //_______________________________PHA_LÊ_HÓA_______________________________
    public static long pointUp(long n, int iters) {
        for (int i = 0; i < iters; i++) {
            n += Math.max(n / 10, 1);
        }
        return n;
    }

    public static long reversePoint(long n, int iters) {
        long[] interValues = new long[iters + 1];
        interValues[iters] = n;
        for (int i = iters; i > 0; i--) {
            long prevValue = interValues[i];
            long subValue = prevValue / 11;
            interValues[i - 1] = prevValue - subValue;
        }
        return interValues[0];
    }

    public static boolean isTrangBiPhaLeHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            return item.template.type < 6 || item.template.type == 32;
        } else {
            return false;
        }
    }

      private float ratioTheoVip(Player player) {
        if (player.Saga_VIP >= 1 && player.Saga_VIP <= 10) {
            return player.Saga_VIP * 1f; // Mỗi VIP tăng 1%
        }
        return 0f; // Nếu VIP ngoài phạm vi 1-8, không tăng tỷ lệ
    }
    public static boolean isDaPhaLe(Item item) {
        return item != null && (item.template.type == 30 || (item.template.id >= 14 && item.template.id <= 20));
    }

    public static boolean isTrangBiDacCau(Item item) {
        if (item != null && item.isNotNullItem()) {
            for (Item.ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 220) {
                    return true;
                }
            }
        }
        return false;
    }

    public static long soLanNangCapConLai(Item item) {
        if (item != null && item.isNotNullItem()) {
            for (Item.ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 221) {
                    return io.param;
                }
            }
        }
        return 0;
    }

    public static int getGemEpSao(int star) {
        switch (star) {
            case 0 -> {
                return 1;
            }
            case 1 -> {
                return 200;
            }
            case 2 -> {
                return 500;
            }
            case 3 -> {
                return 1000;
            }
            case 4 -> {
                return 2500;
            }
            case 5 -> {
                return 5000;
            }
            case 6 -> {
                return 10000;
            }
        }
        return 0;
    }

    public static int getOptionDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).optionTemplate.id;
        }
        return switch (daPhaLe.template.id) {
            case 20 ->
                77;
            case 19 ->
                103;
            case 18 ->
                80;
            case 17 ->
                81;
            case 16 ->
                50;
            case 15 ->
                94;
            case 14 ->
                108;
                
           
                  
                  
                
                
            default ->
                -1;
        };
    }

    public static long getParamDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).param;
        }
        return switch (daPhaLe.template.id) {
            case 20 ->
                50;
            case 19 ->
                50;
            case 18 ->
                5;
            case 17 ->
                5;
            case 16 ->
                30;
            case 15 ->
                5;
            case 14 ->
                5;
                
            
                
                
                
            default ->
                -1;
        };
    }

    public static long getGoldPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 5000000;
            case 1:
                return 50000000;
            case 2:
                return 200_000_000;
            case 3:
                return 500_000_000;
            case 4:
                return 800_000_000;
            case 5:
                return 1000_000_000;
            case 6:
                return 1500_000_000;
            case 7:
                return 1800_000_000;
            case 8:
                return 2500_000_000l;
            case 9:
                return 3000000000l;
            case 10:
                return 5000000000l;
            case 11:
                return 7000000000l;
            case 12:
                return 10000000000l;
        }
        return 0;
    }

    public static float getRatioPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 80f;
            case 1:
                return 50f;
            case 2:
                return 30f;
            case 3:
                return 20f;
            case 4:
                return 3;
            case 5:
                return 1f;
            case 6:
                return 0.2f;
            case 7:
                return 0.05f;
            case 8:
                return 0.01f;
            case 9:
                return 0.01f;
            case 10:
                return 0.02f;
            case 11:
                return 0.01f;
            case 12:
                return 0.01f;
        }

        return 0;
    }

    public static int getGemPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 50;
            case 1:
                return 100;
            case 2:
                return 200;
            case 3:
                return 300;
            case 4:
                return 400;
            case 5:
                return 500;
            case 6:
                return 600;
            case 7:
                return 700;
            case 8:
                return 1400;
            case 9:
                return 2800;
            case 10:
                return 5600;
            case 11:
                return 11200;
            case 12:
                return 22400;
        }
        return 0;
    }

    public static boolean isCoupleItemNangCapCheck(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else {
                return trangBi.template.type == 4 && daNangCap.template.id == 220;
            }
        } else {
            return false;
        }
    }

    public static int getCountDaNangCapDo(int level) {
        switch (level) {
            case 0:
                return 30;
            case 1:
                return 70;
            case 2:
                return 110;
            case 3:
                return 170;
            case 4:
                return 230;
            case 5:
                return 350;
            case 6:
                return 500;
            case 7:
                return 700;
        }
        return 0;
    }

    public static int getCountDaBaoVe(int level) {
        return level + 1;
    }

    public static long getGoldNangCapDo(int level) {
        switch (level) {
            case 0:
                return 100000;
            case 1:
                return 700000;
            case 2:
                return 3000000;
            case 3:
                return 15000000;
            case 4:
                return 70000000;
            case 5:
                return 230000000;
            case 6:
                return 1000000000;
            case 7:
                return 2500000000l;
        }
        return 0;
    }

    public static double getTileNangCapDo(int level) {
        switch (level) {
            case 0:
                return 80;
            case 1:
                return 50;
            case 2:
                return 20;
            case 3:
                return 10;
            case 4:
                return 7;
            case 5:
                return 5;
            case 6:
                return 1;
            case 7: // 7 sao
                return 0.3;
            case 8:
                return 5;
            case 9:
                return 1;
            case 10: // 7 sao
                return 0.3;
            case 11: // 7 sao
                return 0.3;
            case 12: // 7 sao
                return 0.3;
        }
        return 0;
    }

    public static double getTileNangChanMenh(int id) {
        switch (id) {
            case 2139:
                return 80;
            case 2140:
                return 70;
            case 2141:
                return 60;
            case 2142:
                return 50;
            case 2143:
                return 40;
            case 2144:
                return 30;
            case 2145:
                return 20;
            case 2146:
                return 10;
        }
        return 0;
    }
}
