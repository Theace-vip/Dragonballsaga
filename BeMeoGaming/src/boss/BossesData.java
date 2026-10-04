package boss;

import consts.ConstPlayer;
import skill.Skill;
import utils.Util;

public class BossesData {

    /**
     * Prefix text chat |-1| Boss chat |-2| Player in map chat |-3| Parent chat
     * |0|,|1|,|n| Index boss in list chat
     */
    private static final int[][] FULL_DRAGON = new int[][]{{Skill.DRAGON, 1}, {Skill.DRAGON, 2},
    {Skill.DRAGON, 3}, {Skill.DRAGON, 4}, {Skill.DRAGON, 5}, {Skill.DRAGON, 6},
    {Skill.DRAGON, 7}};
    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2},
    {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6},
    {Skill.DEMON, 7}};
    private static final int[][] FULL_GALICK = new int[][]{{Skill.GALICK, 1}, {Skill.GALICK, 2},
    {Skill.GALICK, 3}, {Skill.GALICK, 4}, {Skill.GALICK, 5}, {Skill.GALICK, 6},
    {Skill.GALICK, 7}};
    private static final int[][] FULL_KAMEJOKO = new int[][]{{Skill.KAMEJOKO, 1}, {Skill.KAMEJOKO, 2},
    {Skill.KAMEJOKO, 3}, {Skill.KAMEJOKO, 4}, {Skill.KAMEJOKO, 5}, {Skill.KAMEJOKO, 6},
    {Skill.KAMEJOKO, 7}};
    private static final int[][] FULL_MASENKO = new int[][]{{Skill.MASENKO, 1}, {Skill.MASENKO, 2},
    {Skill.MASENKO, 3}, {Skill.MASENKO, 4}, {Skill.MASENKO, 5}, {Skill.MASENKO, 6},
    {Skill.MASENKO, 7}};
    private static final int[][] FULL_ANTOMIC = new int[][]{{Skill.ANTOMIC, 1}, {Skill.ANTOMIC, 2},
    {Skill.ANTOMIC, 3}, {Skill.ANTOMIC, 4}, {Skill.ANTOMIC, 5}, {Skill.ANTOMIC, 6},
    {Skill.ANTOMIC, 7}};
    private static final int[][] FULL_LIENHOAN = new int[][]{{Skill.LIEN_HOAN, 1}, {Skill.LIEN_HOAN, 2},
    {Skill.LIEN_HOAN, 3}, {Skill.LIEN_HOAN, 4}, {Skill.LIEN_HOAN, 5}, {Skill.LIEN_HOAN, 6},
    {Skill.LIEN_HOAN, 7}};
    private static final int[][] FULL_TDHS = new int[][]{{Skill.THAI_DUONG_HA_SAN, 1},
    {Skill.THAI_DUONG_HA_SAN, 2}, {Skill.THAI_DUONG_HA_SAN, 3}, {Skill.THAI_DUONG_HA_SAN, 4},
    {Skill.THAI_DUONG_HA_SAN, 5}, {Skill.THAI_DUONG_HA_SAN, 6},
    {Skill.THAI_DUONG_HA_SAN, 7}};

    private static final int REST_1_S = 10;        // 1 giây
    private static final int REST_2_S = 10;        // 2 giây
    private static final int REST_5_S = 10;        // 5 giây
    private static final int REST_10_S = 10;       // 10 giây
    private static final int REST_20_S = 10;       // 20 giây
    private static final int REST_30_S = 10;       // 30 giây

    private static final int REST_1_M = 10;       // 1 phút  = 60.000 ms
    private static final int REST_2_M = 10;      // 2 phút  = 120.000 ms
    private static final int REST_5_M = 10;      // 5 phút  = 300.000 ms
    private static final int REST_10_M = 10;      // 10 phút = 600.000 ms
    private static final int REST_15_M = 10;      // 15 phút = 900.000 ms
    private static final int REST_30_M = 10;     // 30 phút = 1.800.000 ms
    private static final int REST_60_M = 10;     // 60 phút = 3.600.000 ms (1 giờ)
    private static final int REST_90_M = 10;     // 90 phút = 5.400.000 ms (1.5 giờ)
    private static final int REST_300_M = 10;// 90 phút = 5.400.000 ms (1.5 giờ)
    private static final int REST_24_H = 10;    // 24 giờ = 86.400.000 ms (1 ngày)

    // **************************************************************************
    // Boss nappa
    public static final BossData KUKU = new BossData(
            "Kuku", // name
            ConstPlayer.XAYDA, // gender
            new short[]{159, 160, 161, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            100000, // dame
            new double[]{2_00_000_000}, // hp
            new int[]{68}, // map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.TAI_TAO_NANG_LUONG, 1, 20000},
                {Skill.LIEN_HOAN, 7, 50},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 80000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_15_M // second rest
    );
    public static final BossData TEST_DAME = new BossData(
            "TEST DAME", // name
            ConstPlayer.XAYDA, // gender
            new short[]{1261, 1262, 1263, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            1, // dame
            new double[]{Double.MAX_VALUE}, // hp
            new int[]{14}, // map join
            new int[][]{
                {Skill.TAI_TAO_NANG_LUONG, 7, 1000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 10000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 10000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_2_M // second rest
    );

    public static final BossData GOHAN1 = new BossData(
            "Gohan Chuẩn Thánh", // name
            ConstPlayer.XAYDA, // gender
            new short[]{2114, 2120, 2121, 142, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            5000000, // dame
            new double[]{500000000000l}, // hp
            new int[]{174}, // map join
            new int[][]{
                {Skill.ANTOMIC, 7, 10},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.MASENKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 90000},
                {Skill.THAI_DUONG_HA_SAN, 7, 150000},
                {Skill.SOCOLA, 1, 80000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 90000}},
            //    {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_5_M // second rest
    );
    public static final BossData GOHAN2 = new BossData(
            "Gohan Thánh Nhân", // name
            ConstPlayer.XAYDA, // gender
            new short[]{2115, 2120, 2121, 142, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            8000000, // dame
            new double[]{1500000000000l}, // hp
            new int[]{174}, // map join
            new int[][]{
                {Skill.ANTOMIC, 7, 10},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.MASENKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 90000},
                {Skill.THAI_DUONG_HA_SAN, 7, 150000},
                {Skill.SOCOLA, 1, 80000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 90000}},
            //    {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_5_M // second rest
    );

    public static final BossData GOHAN3 = new BossData(
            "Gohan Chuẩn Đế", // name
            ConstPlayer.XAYDA, // gender
            new short[]{2116, 2120, 2121, 142, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            12000000, // dame
            new double[]{5000000000000l}, // hp
            new int[]{174}, // map join
            new int[][]{
                {Skill.ANTOMIC, 7, 10},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.MASENKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 90000},
                {Skill.THAI_DUONG_HA_SAN, 7, 150000},
                {Skill.SOCOLA, 1, 80000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 90000}},
            //    {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_5_M // second rest
    );
    public static final BossData GOHAN4 = new BossData(
            "Gohan Đại Đế", // name
            ConstPlayer.XAYDA, // gender
            new short[]{2117, 2122, 2123, 142, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            20000000, // dame
            new double[]{9200000000000l}, // hp
            new int[]{174}, // map join
            new int[][]{
                {Skill.ANTOMIC, 7, 10},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.MASENKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 90000},
                {Skill.THAI_DUONG_HA_SAN, 7, 150000},
                {Skill.SOCOLA, 1, 80000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 90000}},
            //    {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_5_M // second rest
    );
    public static final BossData GOHAN5 = new BossData(
            "Gohan Chân Tiên", // name
            ConstPlayer.XAYDA, // gender
            new short[]{2118, 2122, 2123, 142, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            50000000, // dame
            new double[]{25100000000000l}, // hp
            new int[]{174}, // map join
            new int[][]{
                {Skill.ANTOMIC, 7, 10},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.MASENKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 90000},
                {Skill.THAI_DUONG_HA_SAN, 7, 150000},
                {Skill.SOCOLA, 1, 80000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 90000}},
            //    {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_1_M // second rest
    );
    public static final BossData GOHAN6 = new BossData(
            "Gohan Tiên Nhân", // name
            ConstPlayer.XAYDA, // gender
            new short[]{2119, 2122, 2123, 142, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
           50000000, // dame
            new double[]{85100000000000l}, // hp
            new int[]{174}, // map join
            new int[][]{
                {Skill.ANTOMIC, 7, 10},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.MASENKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 90000},
                {Skill.THAI_DUONG_HA_SAN, 7, 150000},
                {Skill.SOCOLA, 1, 80000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 90000}},
            //    {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_1_M // second rest
    );



    public static final BossData Luffy = new BossData(
            "BOSS Luffy[Đá Khảm 10]", // name
            ConstPlayer.XAYDA, // gender
            new short[]{582, 583, 584, 160, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            6600000, // dame
            new double[]{1_000_000_000_000l}, // hp
            new int[]{136}, // map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 100}},
            //  { Skill.KHIEN_NANG_LUONG, 7, 80000 },
            //   {Skill.THAI_DUONG_HA_SAN, 7, 150000},
            //     {Skill.SOCOLA, 1, 80000},
            //     {Skill.TAI_TAO_NANG_LUONG, 1, 90000},
            //    {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_10_M // second rest
    );

    public static final BossData Zoro = new BossData(
            "BOSS Zoro[Đá Khảm 30]", // name
            ConstPlayer.XAYDA, // gender
            new short[]{585, 586, 587, 160, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            55000000000l, // dame
            new double[]{500_895_000_000_000_000_000_400_000_000_000_000_000_000_000_000_000_000_000d}, // hp
            new int[]{137, 138}, // map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 60000},
                {Skill.THAI_DUONG_HA_SAN, 7, 40000},
                {Skill.SOCOLA, 7, 50000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 20000},
                // {Skill.BIEN_KHI, 7, 20000},
                {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_10_M // second rest
    );

    public static final BossData Sanji = new BossData(
            "BOSS Sanji[Đá Khảm 50]", // name
            ConstPlayer.XAYDA, // gender
            new short[]{588, 589, 590, 160, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            85000000000l, // dame
            new double[]{9_000_500_895_000_000_000_000_000_400_000_000_000_000_000_000_000_000_000_000_000d}, // hp
            new int[]{137, 138}, // map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 60000},
                {Skill.THAI_DUONG_HA_SAN, 7, 40000},
                {Skill.SOCOLA, 7, 50000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 20000},
                // {Skill.BIEN_KHI, 7, 20000},
                {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_10_M // second rest
    );
    public static final BossData Brook = new BossData(
            "BOSS Brook[Đá Khảm 80]", // name
            ConstPlayer.XAYDA, // gender
            new short[]{591, 592, 593, 160, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            85000000000l, // dame
            new double[]{59_000_500_895_000_000_000_000_000_400_000_000_000_000_000_000_000_000_000_000_000d}, // hp
            new int[]{137, 138}, // map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 60000},
                {Skill.THAI_DUONG_HA_SAN, 7, 40000},
                {Skill.SOCOLA, 7, 50000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 20000},
                // {Skill.BIEN_KHI, 7, 20000},
                {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_10_M // second rest
    );

    public static final BossData Chopper = new BossData(
            "BOSS Chopper[Đá Khảm 150]", // name
            ConstPlayer.XAYDA, // gender
            new short[]{606, 607, 608, 160, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            185000000000l, // dame
            new double[]{9_000_000_590_000_500_895_000_000_000_000_000_400_000_000_000_000_000_000_000_000_000_000_000d}, // hp
            new int[]{137, 138}, // map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 60000},
                {Skill.THAI_DUONG_HA_SAN, 7, 40000},
                {Skill.SOCOLA, 7, 50000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 20000},
                // {Skill.BIEN_KHI, 7, 20000},
                {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_10_M // second rest
    );

    public static final BossData Nami = new BossData(
            "BOSS Nami[Đá Khảm 200]", // name
            ConstPlayer.XAYDA, // gender
            new short[]{600, 601, 602, 160, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            185000000000l, // dame
            new double[]{90_000_000_000_000_000_590_000_500_895_000_000_000_000_000_400_000_000_000_000_000_000_000_000_000_000_000d}, // hp
            new int[]{137, 138}, // map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 60000},
                {Skill.THAI_DUONG_HA_SAN, 7, 40000},
                {Skill.SOCOLA, 7, 50000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 20000},
                // {Skill.BIEN_KHI, 7, 20000},
                {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_10_M // second rest
    );

    public static final BossData Franky = new BossData(
            "BOSS Franky[Đá Khảm 200]", // name
            ConstPlayer.XAYDA, // gender
            new short[]{594, 595, 596, 160, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            185000000000l, // dame
            new double[]{90_000_000_000_000_000_590_000_500_895_000_000_000_000_000_400_000_000_000_000_000_000_000_000_000_000_000d}, // hp
            new int[]{137, 138}, // map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 60000},
                {Skill.THAI_DUONG_HA_SAN, 7, 40000},
                {Skill.SOCOLA, 7, 50000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 20000},
                // {Skill.BIEN_KHI, 7, 20000},
                {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_10_M // second rest
    );

    public static final BossData Usopp = new BossData(
            "BOSS Usopp[Đá Khảm 200]", // name
            ConstPlayer.XAYDA, // gender
            new short[]{597, 598, 599, 160, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            185000000000l, // dame
            new double[]{90_000_000_000_000_000_590_000_500_895_000_000_000_000_000_400_000_000_000_000_000_000_000_000_000_000_000d}, // hp
            new int[]{137, 138}, // map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 60000},
                {Skill.THAI_DUONG_HA_SAN, 7, 40000},
                {Skill.SOCOLA, 7, 50000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 20000},
                // {Skill.BIEN_KHI, 7, 20000},
                {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_10_M // second rest
    );

    public static final BossData Robin = new BossData(
            "BOSS Robin[Đá Khảm 200]", // name
            ConstPlayer.XAYDA, // gender
            new short[]{603, 604, 605, 160, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            185000000000l, // dame
            new double[]{90_000_000_000_000_000_590_000_500_895_000_000_000_000_000_400_000_000_000_000_000_000_000_000_000_000_000d}, // hp
            new int[]{137, 138}, // map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 60000},
                {Skill.THAI_DUONG_HA_SAN, 7, 40000},
                {Skill.SOCOLA, 7, 50000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 20000},
                // {Skill.BIEN_KHI, 7, 20000},
                {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_10_M // second rest
    );

    public static final BossData BOSSDIEMFAM = new BossData(
            "BOSS 20K ĐIỂM FAM", // name
            ConstPlayer.XAYDA, // gender
            new short[]{1440, 1441, 1442, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            66000, // dame
            new double[]{1_500_000_000l}, // hp
            new int[]{17, 18, 19, 20, 25, 26, 27, 30, 31, 35, 36}, // map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 100},
                //  { Skill.KHIEN_NANG_LUONG, 7, 80000 },
                //    {Skill.THAI_DUONG_HA_SAN, 7, 40000},
                //   {Skill.SOCOLA, 7, 50000},
                {Skill.TAI_TAO_NANG_LUONG, 1, 40000},
                {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_1_M // second rest
    );

    public static final BossData BLACKGOKU = new BossData(
            "Siêu Black Goku[SPL 70 SD]", // name
            ConstPlayer.XAYDA, // gender
            new short[]{1909, 1911, 1912, 129, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            500000000000l, // dame
            new double[]{900_500_000_000_000_000l}, // hp
            new int[]{93, 94, 96, 97}, // map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 80000},
                {Skill.THAI_DUONG_HA_SAN, 7, 40000},
                {Skill.SOCOLA, 7, 50000},
                {Skill.TAI_TAO_NANG_LUONG, 1, 40000},
                {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_30_M // second rest
    );

    public static final BossData BLACKGOKUROSE = new BossData(
            "Siêu Black Goku [SPL 100 SD]", // name
            ConstPlayer.XAYDA, // gender
            new short[]{1910, 1911, 1912, 129, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            800000000000l, // dame
            new double[]{20_000_000_000_000_000_000d}, // hp
            new int[]{92, 93, 97, 98}, // map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 80000},
                {Skill.THAI_DUONG_HA_SAN, 7, 40000},
                {Skill.SOCOLA, 7, 50000},
                {Skill.TAI_TAO_NANG_LUONG, 1, 40000},
                {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_30_M // second rest
    );
    
      public static BossData O_DO_NEW = new BossData(
            "Bá dơ", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{400, 401, 402, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            3000, //dame
            new double[]{20000}, //hp
            new int[]{0,7,14,5}, //map join
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_GALICK), //skill
            new String[]{}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_2_M //second rest
    );

     public static final BossData DORAEMON = new BossData(
            "Người Máy Doraemon",
            ConstPlayer.XAYDA,
            new short[]{790, 791, 792, -1, -1, -1},
            500000,
            new double[]{100_000_000_000l},
            new int[]{19},
            //            new int[]{14},
            new int[][]{
                {Skill.DRAGON, 7, 1000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, //skill//skill
            new String[]{"|-1|Á đù, Doraemon !!!!!"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            REST_15_M, //second rest
            new int[]{BossID.CHAIEN, BossID.XEKO, BossID.NOBITA, BossID.XUKA} //boss join map together
    );
     

    public static final BossData XUKA = new BossData(
            "Shizuka", //name
            ConstPlayer.XAYDA, //gender
            new short[]{802, 803, 804, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            211000, //dame
            new double[]{75000_000_000L}, //hp
            new int[]{19}, //map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.DE_TRUNG, 7, 10000},},//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Khoa có nhầm không nhỉ",
                "|-1|Các ngươi không nhúc nhích được sao?",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{}, //text chat 3
            AppearType.APPEAR_WITH_ANOTHER //type appear
    );

    public static final BossData XEKO = new BossData(
            "Suneo", //name
            ConstPlayer.XAYDA, //gender
            new short[]{850, 851, 852, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            310100, //dame
            new double[]{75000_000_000l}, //hp
            new int[]{19}, //map join
            new int[][]{
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 10000},
                {Skill.KAMEJOKO, 7, 1000},},//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{}, //text chat 3
            AppearType.APPEAR_WITH_ANOTHER //type appear
    );

    public static final BossData CHAIEN = new BossData(
            "Jaien", //name
            ConstPlayer.XAYDA, //gender
            new short[]{847, 848, 849, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            411100, //dame
            new double[]{100_000_000_000l}, //hp
            new int[]{19}, //map join
            new int[][]{
                {Skill.GALICK, 7, 1000},
                {Skill.KAMEJOKO, 7, 2000},
                {Skill.BIEN_KHI, Util.nextInt(1, 7), 300000},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{}, //text chat 3
            AppearType.APPEAR_WITH_ANOTHER //type appear
    );

    public static final BossData NOBITA = new BossData(
            "Nobita", //name
            ConstPlayer.XAYDA, //gender
            new short[]{844, 845, 846, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            410100, //dame
            new double[]{75000_000_000l}, //hp
            new int[]{19}, //map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.ANTOMIC, 7, 10000},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Doraemon có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{}, //text chat 3
            AppearType.APPEAR_WITH_ANOTHER //type appear
    );
    public static final BossData CauThanThu = new BossData(
            "Cẩu Thần Thú", // name
            ConstPlayer.XAYDA, // gender
            new short[]{1900, 1901, 1902, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            500000, // dame
            new double[]{350000000000l}, // hp
            new int[]{63, 64, 65, 66, 67, 68}, // map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 60000},
                {Skill.THAI_DUONG_HA_SAN, 7, 40000},
                {Skill.SOCOLA, 7, 50000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 20000},
                //      {Skill.BIEN_KHI, 7, 20000},
                {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_5_M // second rest
    );
   public static final BossData VUA_COLD = new BossData(
            "Thống Chế King Cold",
            ConstPlayer.XAYDA,
            new short[]{712, 713, 714, -1, -1, -1},
            1500000,
            new double[]{1200_000_000_000l},
            new int[]{105,106,110},
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 60000},
                {Skill.THAI_DUONG_HA_SAN, 7, 40000},
                {Skill.SOCOLA, 7, 50000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 20000},
                //      {Skill.BIEN_KHI, 7, 20000},
                {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_5_M // second rest
    );
             public static final BossData admin = new BossData(
            "admin",
            ConstPlayer.XAYDA,
            new short[]{127	,472,	473, -1, -1, -1},
            1000,
            new double[]{1200_000_000_000l},
            new int[]{0,7,14,5},
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.KHIEN_NANG_LUONG, 7, 60000},
                {Skill.THAI_DUONG_HA_SAN, 7, 40000},
                {Skill.SOCOLA, 7, 50000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 20000},
                //      {Skill.BIEN_KHI, 7, 20000},
                {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_5_M // second rest
    );


    public static final BossData THANHUYDIET = new BossData(
            "Thần Hủy Diệt", // name
            ConstPlayer.XAYDA, // gender
            new short[]{508, 509, 510, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            55_000_000_000_000_000_000_000_000_000d, // dame
            new double[]{5000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000d}, // hp
            new int[]{175}, // map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.TAI_TAO_NANG_LUONG, 7, 20000},
                // {Skill.BIEN_KHI, 7, 20000},
                {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_90_M);

    public static final BossData THIENSUWHIS = new BossData(
            "Thiên Sứ whis", // name
            ConstPlayer.XAYDA, // gender
            new short[]{505, 506, 507, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            6_000_000_000_000_000_000_000_000_000d,// dame
            new double[]{8_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000d}, // hp
            new int[]{175}, // map join
            new int[][]{
                {Skill.TAI_TAO_NANG_LUONG, 7, 20000},
                {Skill.SOCOLA, 7, 40000},
                //   {Skill.TROI, 7, 90000},
                //  {Skill.PHAN_THAN, 7, 90000},
                {Skill.ANTOMIC, 7, 5000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 40000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha,Anh Tao Đang Tàn Sát",
                "|-1|Cút Ngay Khi Tao Nóng",
                "|-1|Tao hơn hẳn mày, Tao Nạp Card",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_90_M);

    public static final BossData CHAMPA = new BossData(
            "Thần Champa", // name
            ConstPlayer.XAYDA, // gender
            new short[]{511, 512, 513, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            55_000_000_000_000_000_000_000_000_000d, // dame
            new double[]{5000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000d}, // hp
            new int[]{175}, // map join
            new int[][]{
                {Skill.MASENKO, 7, 100},
                {Skill.ANTOMIC, 7, 100},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.TAI_TAO_NANG_LUONG, 7, 20000},
                {Skill.SOCOLA, 7, 50000},
                {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha, mày đây rồi",
                "|-1|Tao đã có lệnh của đại ca Fide rồi",
                "|-1|Tao hơn hẳn mày, nhìn máy đo đi",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_90_M);

    public static final BossData VASDO = new BossData(
            "Thiên Sứ Vasdo", // name
            ConstPlayer.XAYDA, // gender
            new short[]{530, 531, 532, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            6_000_000_000_000_000_000_000_000_000d,// dame
            new double[]{8_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000d}, // hp

            new int[]{175}, // map join
            new int[][]{
                {Skill.TAI_TAO_NANG_LUONG, 7, 20000},
                {Skill.SOCOLA, 7, 40000},
                //   {Skill.TROI, 7, 90000},
                //  {Skill.PHAN_THAN, 7, 90000},
                {Skill.ANTOMIC, 7, 5000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 40000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Haha,Anh Tao Đang Tàn Sát",
                "|-1|Cút Ngay Khi Tao Nóng",
                "|-1|Tao hơn hẳn mày, Tao Nạp Card",
                "|-1|Mày yếu đi đó, với sức chiến đấu đó sao có thể thắng được tao",
                "|-1|Định chạy trốn hả, hử"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_90_M);

   

    public static final BossData MAP_DAU_DINH = new BossData(
            "Mập Đầu Đinh", // name
            ConstPlayer.XAYDA, // gender
            new short[]{165, 166, 167, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            100000, // dame
            new double[]{3000_000_000l}, // hp
            new int[]{ 67}, // map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.TAI_TAO_NANG_LUONG, 1, 20000},
                {Skill.LIEN_HOAN, 7, 50},
                {Skill.ANTOMIC, 7, 80000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|HAHAHA",
                "|-1|Tao chỉ cần 10 giây để giết hết bọn mày",
                "|-1|Được rồi tao sẽ thổi bay hết",
                "|-1|Chết hết đi cho tao",
                "|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Tao sẽ giết hết bọn mày"}, // text chat 2
            new String[]{}, // text chat 3
            300 // second rest
    );

    public static final BossData RAMBO = new BossData(
            "Rambo", // name
            ConstPlayer.XAYDA, // gender
            new short[]{162, 163, 164, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            120000, // dame
            new double[]{500_000_0000l}, // hp
            new int[]{76}, // map join
            new int[][]{
                {Skill.MASENKO, 7, 1000},
                {Skill.KAMEJOKO, 7, 100},
                {Skill.TAI_TAO_NANG_LUONG, 1, 20000},
                {Skill.LIEN_HOAN, 7, 50},
                {Skill.ANTOMIC, 7, 80000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|HAHAHA",
                "|-1|Tao chỉ cần 10 giây để giết hết bọn mày",
                "|-1|Thấy ta đẹp trai không",
                "|-1|Mày sợ tao chưa",
                "|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Tao sẽ giết hết bọn mày"}, // text chat 2
            new String[]{"|-1|Ôi bạn ơi..."}, // text chat 3
            300 // second rest
    );

    // ========================TDST========================
    public static final BossData SO_4 = new BossData(
            "Số 4 Guldo", //name
            ConstPlayer.XAYDA, //gender
            new short[]{168, 169, 170, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            50000, //dame
            new double[]{5000_000_000l}, //hp
            new int[]{81, 82, 83}, //map join
            //           new int[]{86}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.MASENKO, 7, 1000},
                {Skill.THOI_MIEN, 7, 100000}},//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Fide có nhầm không nhỉ",
                "|-1|Các ngươi không nhúc nhích được sao?",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!",
                "|-1|Ta mà lại thua được sao?",
                "|-1|Hãy trả thù cho ta!"
            }, //text chat 3
            AppearType.APPEAR_WITH_ANOTHER //type appear
    );

    public static final BossData SO_3 = new BossData(
            "Số 3 Recome", //name
            ConstPlayer.XAYDA, //gender
            new short[]{174, 175, 176, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            50000, //dame
            new double[]{750_000_0000l}, //hp
            new int[]{81, 82, 83}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.ANTOMIC, 4, 1000}},//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Fide có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!",
                "|-1|Ta mà lại thua được sao?",
                "|-1|Hãy trả thù cho ta!"
            }, //text chat 3
            AppearType.APPEAR_WITH_ANOTHER //type appear
    );

    public static final BossData SO_2 = new BossData(
            "Số 2 Jeice", //name
            ConstPlayer.XAYDA, //gender
            new short[]{171, 172, 173, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            20720, //dame
            new double[]{1000_000_0000l}, //hp
            new int[]{81, 82, 83}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.ANTOMIC, 3, 3000}},//skill//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?",
                "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Fide có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!",
                "|-1|Ta mà lại thua được sao?",
                "|-1|Hãy trả thù cho ta!"
            }, //text chat 3
            AppearType.APPEAR_WITH_ANOTHER //type appear
    );

    public static final BossData SO_1 = new BossData(
            "Số 1 Burter", //name
            ConstPlayer.XAYDA, //gender
            new short[]{177, 178, 179, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            25020, //dame
            new double[]{1250_000_0000l}, //hp
            new int[]{81, 82, 83}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.KAMEJOKO, 4, 10000}},//skill//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?",
                "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Fide có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!",
                "|-1|Ta mà lại thua được sao?",
                "|-1|Hãy trả thù cho ta!"
            }, //text chat 3
            AppearType.APPEAR_WITH_ANOTHER //type appear
    );

    public static final BossData TIEU_DOI_TRUONG = new BossData(
            "Tiểu đội trưởng Ginyu", //name
            ConstPlayer.XAYDA, //gender
            new short[]{180, 181, 182, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            30000, //dame
            new double[]{1500_000_0000l}, //hp
            new int[]{81, 82, 83}, //map join
            new int[][]{
                {Skill.SOCOLA, 7, 100000},
                {Skill.MASENKO, 7, 1000},
                {Skill.GALICK, 7, 1000}},//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?",
                "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Fide có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!"
            }, //text chat 3
            REST_10_M,
            new int[]{BossID.SO_4, BossID.SO_3, BossID.SO_2, BossID.SO_1}
    );
   public static final BossData SO_4_NM = new BossData(
            "Số 4 NM", //name
            ConstPlayer.XAYDA, //gender
            new short[]{168, 169, 170, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            170000, //dame
            new double[]{2000_000_0000l}, //hp
            new int[]{7, 8, 9, 10, 11, 12, 13, 25, 34, 33, 43}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.MASENKO, 7, 1000},
                {Skill.THOI_MIEN, 7, 100000},},//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?",
                "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Fide có nhầm không nhỉ",
                "|-1|Các ngươi không nhúc nhích được sao?",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!",
                "|-1|Ta mà lại thua được sao?",
                "|-1|Hãy trả thù cho ta!"
            }, //text chat 3
            AppearType.APPEAR_WITH_ANOTHER //type appear
    );

    public static final BossData SO_3_NM = new BossData(
            "Số 3 NM", //name
            ConstPlayer.XAYDA, //gender
            new short[]{174, 175, 176, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            180000, //dame
            new double[]{2_200_000_0000l}, //hp
            new int[]{7, 8, 9, 10, 11, 12, 13, 25, 34, 33, 43}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.ANTOMIC, 4, 1000},},//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?",
                "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Fide có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!",
                "|-1|Ta mà lại thua được sao?",
                "|-1|Hãy trả thù cho ta!"
            }, //text chat 3
            AppearType.APPEAR_WITH_ANOTHER //type appear
    );

    public static final BossData SO_2_NM = new BossData(
            "Số 2 NM", //name
            ConstPlayer.XAYDA, //gender
            new short[]{171, 172, 173, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            202000, //dame
            new double[]{2_500_000_0000d}, //hp
            new int[]{7, 8, 9, 10, 11, 12, 13, 25, 34, 33, 43}, //map join
            new int[][]{
                {Skill.GALICK, 7, 1000},
                {Skill.ANTOMIC, 3, 3000},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Fide có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!"
            }, //text chat 3
            AppearType.APPEAR_WITH_ANOTHER //type appear
    );

    public static final BossData SO_1_NM = new BossData(
            "Số 1 NM", //name
            ConstPlayer.XAYDA, //gender
            new short[]{177, 178, 179, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            252000, //dame
            new double[]{28000000000l}, //hp
            new int[]{7, 8, 9, 10, 11, 12, 13, 25, 34, 33, 43}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.KAMEJOKO, 4, 10000},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Fide có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Hãy trả thù cho ta!"
            }, //text chat 3
            AppearType.APPEAR_WITH_ANOTHER //type appear
    );
   public static final BossData TIEU_DOI_TRUONG_NM = new BossData(
            "Tiểu đội trưởng NM", //name
            ConstPlayer.XAYDA, //gender
            new short[]{180, 181, 182, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            300000, //dame
            new double[]{3_200_000_0000l}, //hp
            new int[]{7, 8, 9, 10, 11, 12, 13, 25, 34, 33, 43}, //map join
            new int[][]{
                {Skill.SOCOLA, 7, 1000},
                {Skill.MASENKO, 7, 1000},
                {Skill.GALICK, 7, 1000},},//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Fide có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!"
            }, //text chat 3
            REST_15_M,
            new int[]{BossID.SO_4_NM, BossID.SO_3_NM, BossID.SO_2_NM, BossID.SO_1_NM} //type appear
    );

    // ========================FIDE========================
    public static final BossData FIDE_DAI_CA_1 = new BossData(
            "Fide đại ca 1", //name
            ConstPlayer.XAYDA, //gender
            new short[]{183, 184, 185, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            50000, //dame
            new double[]{25000_000_000l}, //hp
            new int[]{80}, //map join
            (int[][]) Util.addArray(FULL_ANTOMIC, FULL_GALICK), //skill
            new String[]{"|-1|Khá lắm con trai, dám phá tan giấc mộng của ta...",
                "|-1|Ta không thấy đám Ginyu đâu,... các ngươi đã giết chúng rồi à?",
                "|-1|Tuy không biết các ngươi dùng quỷ kế gì, nhưng ta rất ấn tượng đấy!",
                "|-1|Không thể tha thứ, ta không thể tha cho lũ sâu bọ các ngươi được!!"
            }, //text chat 1
            new String[]{"|-1|Các ngươi tới số rồi mới gặp phải ta",
                "|-1|Toàn bọn tốt thí",
                "|-2|Không..thể..nào!!",
                "|-2|Không ngờ..Hắn mạnh cỡ này sao..!!",
                "|-1|Chúng mày nghĩ kiến lại thắng nổi khủng long sao?",
                "|-1|Hô hô hô",
                "|-1|Được thôi, nếu muốn chết đến vậy, ta rất vui lòng!!"
            }, //text chat 2
            new String[]{"|-1|Biến hình, hây aaaa..."}, //text chat 3
            REST_10_M //second rest
    );

    public static final BossData FIDE_DAI_CA_2 = new BossData(
            "Fide đại ca 2", //name
            ConstPlayer.XAYDA, //gender
            new short[]{186, 187, 188, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            60000, //dame
            new double[]{35000_000_000l}, //hp
            new int[]{80}, //map join
            (int[][]) Util.addArray(FULL_ANTOMIC, FULL_LIENHOAN), //skill
            new String[]{"|-1|Hê hê, cẩn thận đi",
                "|-1|Nếu đã biến thành thế này thì ta sẽ không nhùn nhặn như trước đâu"
            }, //text chat 1
            new String[]{"|-1|Oải rồi hả?",
                "|-1|Ê cố lên nhóc",
                "|-1|Ôi, xin lỗi nhé. Sức mạnh lớn quá nên ta cũng chẳng điều khiển nổi nữa!",
                "|-1|Hahaha! Ấn tượng đấy! Tên nào cũng lủi rất nhanh!",
                "|-2|A...Tốc độ nhanh quá!",
                "|-1|Hình như... mày không phải là một thằng nhóc bình thường!",
                "|-1|Mấy đòn vừa rồi, nói thật là cũng đau đấy!",
                "|-1|Nhưng tiếc rằng đối thủ của mày lại là Fide này...",
                "|-2|Chết tiệt.. chúng ta đã đánh giá quá thấp sức mạnh của hắn!!",
                "|-2|Đồ..Đồ quái vật..!",
                "|-2|Tốc độ kinh hoàng quá! Ai mà né nổi chứ!",}, //text chat 2
            new String[]{"|-1|Ác quỷ biến hình, Graaaaa...."}, //text chat 3
            AppearType.ANOTHER_LEVEL //type appear
    );

    public static final BossData FIDE_DAI_CA_3 = new BossData(
            "Fide đại ca 3", //name
            ConstPlayer.XAYDA, //gender
            new short[]{189, 190, 191, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            80000, //dame
            new double[]{45000_000_000l}, //hp
            new int[]{80}, //map join
            (int[][]) Util.addArray(FULL_MASENKO, FULL_LIENHOAN), //skill
            new String[]{"|-1|Ta sẽ cho các ngươi thấy đâu mới là sức mạnh của ta!!"}, //text chat 1
            new String[]{"|-1|Ta nói các ngươi rồi! Sức mạnh này của ta còn đáng sợ hơn địa ngục!!",
                "|-1|Ta chơi thêm chút nữa chắc ngươi chóng mặt buồn nôn mất!!",
                "|-2|Ăn gì mà khỏe thế!",
                "|-2|Chết đi Fide!!!!",
                "|-1|Hô hô hô hô",
                "|-1|Chán thật! Khí của ngươi sắp hết rồi. Để ta tiễn ngươi về địa ngục!",
                "|-1|Ngươi quá tự cao rồi đấy,xem ta đây!",}, //text chat 2
            new String[]{"|-1|Lũ khốn..",
                "|-1|..Một ngày nào đó ta sẽ quay lại và trả thù các ngươi",
                "|-1|Nhớ mặt tao đấy !",}, //text chat 3
            AppearType.ANOTHER_LEVEL //type appear
    );

    // **************************************************************************
    // Boss Android
    public static final BossData DR_KORE = new BossData(
            "Dr.Kôrê", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{255, 256, 257, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            100000, // dame
            new double[]{20000000000l}, // hp
            new int[]{96, 94, 93}, // map join
            new int[][]{
                {Skill.THOI_MIEN, 3, 15000},
                {Skill.KAMEJOKO, 7, 10000},
                {Skill.LIEN_HOAN, 7, 1000},}, // skill
            new String[]{"|-2|Chào anh! em đứng đây từ chiều",
                "|-1|Quái lạ! Sao chúng biết rõ tung tích của bọn ta thế nhỉ?",
                "|-1|Chúng còn biết chính xác ta sẽ xuất hiện ở đây để đón đánh nữa!",
                "|-1|Chúng mày là ai từ đâu tới?Cho tao xin cái địa chỉ",
                "|-2|Điều ấy biết hay không cũng không còn quan trọng nữa",
                "|-1|Ừ bọn bây chỉ là hạng tôm tép ta chẳng cần biết tên làm gì!",
                "|-1|Số 19! Xuất chiêu đi nào",
                "|0|Okê đại ca, em sẽ xử lý bọn này trong vòng 2 tiếng."
            }, // text chat 1
            new String[]{"|-1|Oải rồi hả?",
                "|-1|Ê cố lên nhóc",
                "|-1|Chán",
                "|-1|Mi khá đấy, nhưng so với ta cũng chỉ là hạng tôm tép",
                "|-1|Lôi Công Trảo",
                "|-1|Cho dù ngươi có mạnh đến đâu.. thì cũng không đánh bại được rôbốt bọn ta",
                "|-2|Lão già khôn thật!!",
                "|-2|Hừ! Lão già khốn kiếp!",}, // text chat 2
            new String[]{}, // text chat 3
            REST_10_M, // second rest
            new int[]{BossID.ANDROID_19});

    public static final BossData ANDROID_19 = new BossData(
            "Android 19", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{249, 250, 251, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
             100000, // dame
            new double[]{20000000000l}, // hp
            new int[]{96, 94, 93}, // map join
            new int[][]{
                {Skill.KAMEJOKO, 7, 1000},
                {Skill.LIEN_HOAN, 7, 10000},}, // skill//skill
            new String[]{}, // text chat 1
            new String[]{"|-1|Oải rồi hả?",
                "|-1|Ê cố lên nhóc",
                "|-1|Chán",
                "|-1|Ngươi sẽ không bao giờ thắng được đâu!!",
                "|-2|Ngươi vừa hút được nhiều rồi đấy, nhưng giờ thì đừng hòng!!",}, // text
            // chat 2
            new String[]{}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER);

    // **************************************************************************
    public static final BossData ANDROID_13 = new BossData(
            "Android 13", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{252, 253, 254, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
             100000, // dame
            new double[]{20000000000l}, // hp
            new int[]{104}, // map join
            new int[][]{
                {Skill.KAMEJOKO, 7, 10000},
                {Skill.LIEN_HOAN, 7, 1000},}, // skill
            new String[]{"|-1|Sôn..gôku",
                "|-2|Lại là tiến sĩ Kôrê à.. rốt cuộc ông ta đã tạo ra bao nhiêu rôbốt nhân tạo thế nhỉ?",
                "|-1|Bọn ta là rôbốt sát thủ, sinh ra từ máy tính ngài Kôrê,..",
                "|-1|..cho một mục tiêu duy nhất là giết Sôngôku!",
                "|-2|Máy tính? Để giết Gôku sao?",
                "|-1|Mong muốn trả thù Gôku của ngài Kôrê đã được lưu hết vào máy tính..",
                "|-1|.., Bọn ta sinh ra từ lòng căm thù ngày một tăng bên trong chiếc máy tính có chứa mong muốn trả thù",
                "|-1|Mục tiêu của bọn ta chỉ là Gôku, nhưng mà.. nếu ngươi mà cản đường thì là chuyện khác!",}, // text
            // chat
            // 1
            new String[]{"|-1|Sao thế hả? Ta mới chỉ khởi động thôi mà!",
                "|-2|Ngươi đánh giá thấp bọn ta quá đấy!",
                "|-2|Đừng có tưởng bở, lũ sâu bọ!",
                "|-1|Nếu có ý định gây trở ngại cho cuộc chiến giữa ta và Sôngôku, thì ta cũng sẽ giết ngươi ngay lập tức",
                "|-2|Ngươi tưởng ta để cho ngươi giết được ta ngay à?",
                "|-2|Đúng là mạnh mồm thật đấy!",
                "|-2|Đỡ này",}, // text chat 2
            new String[]{"|-1|Sô..Sông...gôku....."}, // text chat 3
            AppearType.CALL_BY_ANOTHER);

    public static final BossData ANDROID_14 = new BossData(
            "Android 14", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{246, 247, 248, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
             100000, // dame
            new double[]{20000000000l}, // hp
            new int[]{104}, // map join
            new int[][]{
                {Skill.KAMEJOKO, 7, 10000},
                {Skill.LIEN_HOAN, 7, 1000},}, // skill
            new String[]{"|-2|Các ngươi là ai?",
                "|-2|Ta không thể cảm nhận được khí của các ngươi, các ngươi không phải là con người đúng chứ?",
                "|-2|Ta hiểu rồi, các ngươi là rôbốt sát thủ do tiến sĩ Kôrê tạo ra chứ gì?"
            }, // text chat 1
            new String[]{}, // text chat 2
            new String[]{"|0|Số 14 và số 15 tiêu tùng cả rồi à?"}, // text chat 3
            REST_10_M,
            new int[]{BossID.ANDROID_13, BossID.ANDROID_15});

    public static final BossData ANDROID_15 = new BossData(
            "Android 15", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{261, 262, 263, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
              100000, // dame
            new double[]{20000000000l}, // hp
            new int[]{104}, // map join
            new int[][]{
                {Skill.KAMEJOKO, 7, 10000},
                {Skill.LIEN_HOAN, 7, 1000},}, // skill//skill
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{"|-2|Thì ra vẫn chỉ là một đống sắt vụn!"}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER);
    // **************************************************************************

    public static final BossData PIC = new BossData(
            "Pic", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{237, 238, 239, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
             100000, // dame
            new double[]{20000000000l}, // hp
            new int[]{97, 98, 99}, // map join
            (int[][]) Util.addArray(FULL_GALICK, FULL_KAMEJOKO), // skill
            new String[]{"|-1|Chào! Có Gôku ở đây không?",
                "|-3|Tôi không nghĩ Gôku ở đây đâu!",
                "|-2|Biến khỏi đây đi, Gôku không có ở đây đâu!",
                "|-1|Bọn ta cũng nghĩ vậy, ngươi nói cho bọn ta biết hắn ở đâu được không!?",
                "|-2|Ngươi nghĩ bọn ta sẽ nói sao??",
                "|-1|Nếu ngươi không chịu nói bọn ta sẽ phải ra tay.."
            }, // text chat 1
            new String[]{"|-1|Ngươi thực sự rất mạnh dù không phải là một rôbốt. Ngươi không phải là Piccôlô",
                "|-1|Nhưng ta không quan tâm ngươi là ai, ta chỉ cần biết Gôku đang ở đâu!",
                "|-1|Sao ngươi không chịu nói cho ta biết chứ!?",
                "|-2|Mục đích của ngươi không phải là giết Gôku sao? Vì vậy ta không nói cho ngươi biết cậu ấy đang ở đâu",
                "|-1|Vậy thì ta bắt buộc phải tiếp tục đánh buộc ngươi phải nói ra!",
                "|-1|Lần này ta không nương tay đâu!",
                "|-2|Ngươi thực sự rất nhanh. Nhưng chưa đủ thực lực đâu!!",
                "|-1|Cái gì!? Đó là điều ngu ngốc nhất ta từng nghe.. ta là chiến binh mạnh nhất từ trước đến giờ.!",
                "|1|Hắn thực sự rất mạnh, đúng là cuộc chiến cân sức",
                "|-3|Pic, trả nhẽ cậu lại để thua mấy tên nhóc vặt này sao?"
            }, // text chat 2
            new String[]{"|1|Pic tiêu rồi, tớ lên trước nhé!",
                "|-3|Okê, xin cứ tự nhiên"
            }, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER);

    public static final BossData POC = new BossData(
            "Poc", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{240, 241, 242, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
             100000, // dame
            new double[]{20000000000l}, // hp
            new int[]{97, 98, 99}, // map join
            (int[][]) Util.addArray(FULL_KAMEJOKO), // skill
            new String[]{}, // text chat 1
            new String[]{"|-1|Đừng tưởng ta đây là con gái mà dễ bắt nạt nhé",
                "|-1|Khôn hồn thì chỉ chỗ Gôku cho bọn ta nhanh đi",
                "|-3|Coi kìa, một lũ xúm lại bắt nạt một cô gái kìa..",
                "|-1|Đừng có mà trọng nam khinh nữ",
                "|-2|Tại sao cô gái xinh đẹp thế này mà lại là rôbốt nhỉ?"
            }, // text chat 2
            new String[]{"|-2|Cô gái xinh đẹp vậy mà lại bị tên tiến sĩ Kôrê biến thành người máy.."}, // text
            // chat
            // 3
            AppearType.APPEAR_WITH_ANOTHER);

    public static final BossData KING_KONG = new BossData(
            "King Kong", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{243, 244, 245, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
             100000, // dame
            new double[]{20000000000l}, // hp
            new int[]{97, 98, 99}, // map join
            (int[][]) Util.addArray(FULL_LIENHOAN, FULL_MASENKO), // skill
            new String[]{}, // text chat 1
            new String[]{"|-1|Mau đền mạng cho những người bạn của ta",
                "|-1|Sức mạnh của ta chênh nhau với các ngươi một trời một vực đấy!",
                "|-1|Thằng kia đừng để bọn nó trói tao !"
            }, // text chat 2
            new String[]{}, // text chat 3
            REST_10_M,
            new int[]{BossID.PIC, BossID.POC});
    // **************************************************************************
    // Boss cell


   public static final BossData XEN_BO_HUNG_1 = new BossData(
            "Xên bọ hung",
            ConstPlayer.XAYDA,
            new short[]{228, 229, 230, -1, -1, -1},
            2000000,
            new double[]{1_000_000_0000l},
            new int[]{100},
            new int[][]{
                {Skill.KAMEJOKO, 7, 1000},
                {Skill.KAMEJOKO, 7, 1000},
                {Skill.LIEN_HOAN, 7, 10000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 50000}},//skill
            new String[]{"|-2|Cái gì kia vậy!? Đó là loài gì thế!!?",
                "|-1|Hôm nay sẽ là ngày đáng nhớ đây!",
                "|-1|Ta sẽ hấp thụ số 17 và 18 để đạt được dạng hoàn hảo!",
                "|-1|Sao vậy, Picôlô? Nếu ngươi muốn ngăn ta lại thì xong lên đi chứ!?"
            }, //text chat 1
            new String[]{"|-2|Hắn làm ta bất ngờ đấy! Khốn kiếp!",
                "|-2|Tên đó muốn hấp thụ số 17 và 18 sao?",
                "|-1|Đến đây nào! Khi kết hợp với ta, ngươi sẽ trở nên bất bại, trở thành một thể sống hoàn mỹ!",
                "|-2|Mơ đi, sao ta phải để ngươi hấp thu hả!?",
                "|-2|Dù muốn hay không, ngươi cũng sẽ bị ta hấp thu thôi..",
                "|-2|Chúng ta không thể để hắn đạt đến dạng hoàn hảo được!",
                "|-2|Mục tiêu của hắn không phải là Sôngôku.., mà là phá hủy cả vũ trụ này!",
                "|-1|Làm đứt đuôi ta ư? Đừng quên ta có tế bào của Picôlô!!",
                "|-1|Ta có thể tái tạo.. mọi bộ phận cơ thể!",
                "|-2|Vậy thì để ngăn cản ngươi đạt đến dạng hoàn hảo, ta phải tiêu diệt ngươi!",
                "|-2|Hắn quá mạnh! Mình có thể làm được gì đây!?",
                "|-1|Có vẻ như ta đã trở nên quá mạnh, chắc là ta đã giết nhiều người hơn dự tính!!",
                "|-1|Ngươi không thể thắng nổi ta! Từ bỏ đi!!",
                "|-1|Đến lúc ta hấp thu ngươi rồi",
                "|-2|Đồ quái vật chết tiệt...",
                "|-1|Hê hê hê, rồi ngươi sẽ trở thành một phần của con quái vật này thôi!",
                "|-1|Lại thêm một tên ngốc nữa chán sống!"
            }, //text chat 2
            new String[]{"|-2|Khốn kiếp, Pic.. hắn bị Cell hấp thu rồi!!"}, //text chat 3
            REST_15_M
    );

    public static final BossData XEN_BO_HUNG_2 = new BossData(
            "Xên bọ hung 2",
            ConstPlayer.XAYDA,
            new short[]{231, 232, 233, -1, -1, -1},
            2000000,
            new double[]{2_500_000_0000l},
            new int[]{100},
            new int[][]{
                {Skill.KAMEJOKO, 7, 1000},
                {Skill.KAMEJOKO, 7, 1000},
                {Skill.LIEN_HOAN, 7, 1000}}, //skill
            new String[]{}, //text chat 1
            new String[]{"|-2|Nguy rồi... thực sự nguy to rồi!",
                "|-1|Các ngươi nghĩ có thể chạy được sao!?",
                "|-1|Muốn chạy khỏi ta thì đừng hòng!!",
                "|-1|Ta cũng ngạc nhiên với tốc độ của mình! Đó tất nhiên là do ta hấp thụ được số 17!",
                "|-2|Hắn nhanh quá!!",
                "|-1|Ta muốn thử xem sức mạnh này đến đâu...",
                "|-1|Hmm.. có vẻ như sức mạnh này đã tăng lên gấp bội!",
                "|-1|Đã đến lúc ta đạt đến trạng thái hoàn hảo.!",
                "|-1|Có vẻ như ngươi muốn bị ép hơn là tự nguyện!!",
                "|-2|Bây giờ ta chưa thể thắng được ngươi!! Nhưng ngươi đừng hòng huyênh hoang!!!",
                "|-1|Muốn chạy à!!? Ta sẽ không để ngươi thoát đâu!!",}, //text chat 2
            new String[]{"|-1|Đến lúc rồi!"}, //text chat 3
            AppearType.ANOTHER_LEVEL
    );

    public static final BossData XEN_BO_HUNG_3 = new BossData(
            "Xên hoàn thiện",
            ConstPlayer.XAYDA,
            new short[]{234, 235, 236, -1, -1, -1},
            3500000,
            new double[]{3_0000_000_000l},
            new int[]{100},
            new int[][]{
                {Skill.KAMEJOKO, 7, 1000},
                {Skill.KAMEJOKO, 7, 1000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 60000},
                {Skill.KHIEN_NANG_LUONG, 7, 120000},
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.THOI_MIEN, 7, 100000}},
            //skill

            new String[]{"|-2|Cuối cùng hắn cũng đã biến đổi",
                "|-2|Khốn kiếp! Phải kết liễu hắn ngay lúc này!"
            }, //text chat 1
            new String[]{"|-2|Cell đã đạt đến dạng hoàn hảo rồi!",
                "|-2|Đồ khốn, sao ngươi dám làm vậy với số 18!!",
                "|-2|Không ấn tượng lắm với dạng hoàn hảo của ngươi..",
                "|-2|Sao hắn không hề hấn gì?",
                "|-1|Xin lỗi.. Ngươi có thể giúp ta làm nóng cơ thể lên không !?",
                "|-2|Tình hình nguy cấp rồi!",
                "|-2|Khốn kiếp! Ngươi không chú tâm vào trận đấu!",
                "|-1|Thì ta đã bảo đây chỉ là làm nóng cơ thể mà!!",
                "|-1|Giờ ngươi chỉ là rác rưởi mà thôi!",
                "|-2|Không thể nào! Ngươi dù sao cũng chỉ là đồ sâu bọ!",}, //text chat 2
            new String[]{"|-1|Oái.. không...",
                "|-1|Cơ thể hoàn hảo của ta!!",
                "|-1|Ta không tin chuyện này sẽ xảy ra!!",
                "|-1|Đồ khốn kiếp!! Rồi ngươi sẽ phải trả giá"
            }, //text chat 3
            AppearType.ANOTHER_LEVEL
    );//***********************************************************

      public static final BossData SIEU_BO_HUNG_1 = new BossData(
            "Xên Hoàn Thiện",
            ConstPlayer.XAYDA,
            new short[]{234, 235, 236, -1, -1, -1},
            3000000,
            new double[]{2_750_000_0000l},
            new int[]{103},
            new int[][]{
                {Skill.KAMEJOKO, 7, 10000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 50000},
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 150000}},
            //skill
            new String[]{"|-1|Cuối cùng thì ngươi cũng đến Goku,... ta đang đợi ngươi đây...",
                "|-1|Chào mừng đến với giải đấu!!!",
                "|-2|Vậy thì... Chúng ta bắt đầu cuộc chiến thôi chứ! Tớ lên trước nhé!",
                "|-1|Đến giờ rồi..",
                "|-1|Ai muốn đấu trước cũng được,.. kẻ nào muốn đầu trước thì bước lên đi!",
                "|-1|Hãy bắt đầu trò chơi của cell thôi!!"
            }, //text chat 1
            new String[]{"|-1|Đến đây",
                "|-1|Làm tốt lắm! Thế mới là chiến đấu chứ",
                "|-1|Chẳng hay ho gì nếu không dồn hết sức vào trận đấu!",
                "|-1|Ngươi được lắm, thực sự rất khá đấy...",
                "|-1|Cảnh báo với ngươi là ta sẽ không thất bại như lúc nãy nữa đâu!!",
                "|-1|Giờ mà ngươi phí sức vào những đòn tấn công vô ích thì trận đấu sẽ vô vị lắm đấy!!",
                "|-1|Ta muốn ngươi giải trí cho ta thêm chút nữa!!",
                "|-1|Nếu không còn ai tham dự trò chơi của Cell,.. thì toàn bộ cư dân trái đất sẽ bị tiêu diệt!"
            }, //text chat 2
            new String[]{"|-1|Hô hô hô, đây sẽ là kết thúc của lũ ngu ngốc các ngươi!! Ta sẽ chết nhưng sẽ kéo theo cái hành tinh này luôn"}, //text chat 3
            REST_30_M
    );

    public static final BossData SIEU_BO_HUNG_2 = new BossData(
            "Xên Hoàn Thiện 2",
            ConstPlayer.XAYDA,
            new short[]{234, 235, 236, -1, -1, -1},
            2000000,
            new double[]{4_000_000_0000l},
            new int[]{103},
            new int[][]{
                {Skill.KAMEJOKO, 7, 10000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 50000},
                {Skill.THAI_DUONG_HA_SAN, 7, 125000},
                {Skill.LIEN_HOAN, 7, 1000}},//skill
            new String[]{"|-1|Cuối cùng thì ngươi cũng đến Goku,... ta đang đợi ngươi đây...",
                "|-1|Chào mừng đến với giải đấu!!!",
                "|-2|Vậy thì... Chúng ta bắt đầu cuộc chiến thôi chứ! Tớ lên trước nhé!",
                "|-1|Đến giờ rồi..",
                "|-1|Ai muốn đấu trước cũng được,.. kẻ nào muốn đầu trước thì bước lên đi!",
                "|-1|Hãy bắt đầu trò chơi của cell thôi!!"}, //text chat 1
            new String[]{"|-1|Đến đây",
                "|-1|Làm tốt lắm! Thế mới là chiến đấu chứ",
                "|-1|Chẳng hay ho gì nếu không dồn hết sức vào trận đấu!",
                "|-1|Ngươi được lắm, thực sự rất khá đấy...",
                "|-1|Cảnh báo với ngươi là ta sẽ không thất bại như lúc nãy nữa đâu!!",
                "|-1|Giờ mà ngươi phí sức vào những đòn tấn công vô ích thì trận đấu sẽ vô vị lắm đấy!!",
                "|-1|Ta muốn ngươi giải trí cho ta thêm chút nữa!!",
                "|-1|Nếu không còn ai tham dự trò chơi của Cell,.. thì toàn bộ cư dân trái đất sẽ bị tiêu diệt!"}, //text chat 2
            new String[]{}, //text chat 3
            AppearType.ANOTHER_LEVEL
    );

    public static final BossData SIEU_BO_HUNG_3 = new BossData(
            "Siêu Xên Hoàn Thiện",
            ConstPlayer.XAYDA,
            new short[]{234, 235, 236, -1, -1, -1},
            1200000,
            new double[]{20_500_000_0000L},
            new int[]{103},
            new int[][]{
                {Skill.DEMON, 3, 1}, {Skill.DEMON, 6, 2}, {Skill.DRAGON, 7, 3}, {Skill.DRAGON, 1, 4}, {Skill.GALICK, 5, 5},
                {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9}, {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11}, {Skill.KAMEJOKO, 1, 12},
                {Skill.ANTOMIC, 1, 13}, {Skill.ANTOMIC, 2, 14}, {Skill.ANTOMIC, 3, 15}, {Skill.ANTOMIC, 4, 16}, {Skill.ANTOMIC, 5, 17}, {Skill.ANTOMIC, 6, 19}, {Skill.ANTOMIC, 7, 20},
                {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23},
                {Skill.KAMEJOKO, 7, 1000},
                {Skill.QUA_CAU_KENH_KHI, 7, 100000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 20000},
                {Skill.KHIEN_NANG_LUONG, 7, 130000},
                {Skill.THOI_MIEN, 7, 40000}},
            //skill
            new String[]{"|-2|Tại sao!? Sao ngươi vẫn còn sống vậy!?",
                "|-1|... Xem ra bọn bây rất ngạc nhiên",
                "|-1|Được rồi, ta sẽ nói cho các ngươi biết đã xảy ra chuyện gì...",
                "|-1|Ta cũng ngạc nhiên không kém đâu, hê hê hê...",
                "|-1|Bên trong đầu ta có một khối nhỏ, đó là nơi chữa trị cho cơ thể ta khi ta bị thương tổn.",
                "|-1|Nếu khối đó không bị hủy diệt thì ta vẫn còn có khả năng hồi phục lại toàn bộ cơ thể này.",
                "|-1|Khi ta tự nổ tung, thật may mắn khi khối thịt đó không bị tổn thương gì...",
                "|-1|Nói thật là việc này nằm ngoài dự tính của ta. Ta quả là gặp may mắn...",
                "|-1|Ta không định chơi đùa thêm nữa đâu... nên kết thúc mọi chuyện ngay bây giờ thôi!!",}, //text chat 1
            new String[]{"|-1|Đến đây",
                "|-1|Ta sẽ không chừa đứa nào trong bọn bây",
                "|-1|Hãy tan thành cát bụi cùng với hành tinh này!!",
                "|-1|Ha ha ha! Các ngươi thích thế này chứ hả?!",
                "|-2|Ta hận sức mạnh của chúng ta không đủ đánh bại hắn!!",
                "|-2|Thật khốn kiếp!!!",
                "|-2|Tấn công đi!! Ta biết mình không còn đủ sức nữa...",
                "|-2|Nhưng ta vẫn muốn tiêu diệt ngươi!!!",}, //text chat 2
            new String[]{"|-1|Aahhh...",
                "|-1|Không thể nào... Điều này không thể nào xảy ra...",
                "|-1|Ta không tin...",
                "|-1|Ta là bấ...",}, //text chat 3
            //            TypeAppear.ANOTHER_LEVEL
            REST_15_M
    );
    
      public static final BossData XEN_CON = new BossData(
            "Xên con", //name
            ConstPlayer.XAYDA, //gender
            new short[]{264, 265, 266, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            1500000, //dame
            new double[]{3_500_000_0000L}, //hp
            new int[]{103}, //map join
            (int[][]) Util.addArray(FULL_DEMON, FULL_MASENKO), //skill
            new String[]{"|-1|Hello cục cưng",
                "|-1|Mày có biết tao là ai không?",
                "|-2|Tao không cần biết mày là ai, mày nghĩ mày dọa được tao à?",
                "|-1|Thôi không nói nhiều nữa,giờ tao cho mày biết tao là ai."
            }, //text chat 1
            new String[]{"|-1|Tao hơn hẳn mày, mày nên cầu cho may mắn ở phía mày đi",
                "|-1|Ghê chưa ghê chưa!",
                "|-1|Tao có rất nhiều vật phẩm quý giá,nhưng với mày thì có cái..nịt",
                "|-1|Đánh tao à,lo mà luyện tập thêm đi",
                "|-1|Nói cho mày biết,tao là con của Xên ",
                "|-1|Tao sẽ thiêu rụi mày"
            }, //text chat 2
            new String[]{"|-2|Đêm qua em đẹp lắm!"}, //text chat 3
            REST_15_M //second rest
    );
    
    // ========================BLACK GOKU========================
   public static final BossData BLACK_GOKU = new BossData(
            "Black Goku", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{550, 880, 881, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            3000000, //dame
            new double[]{10_500_000_0000l}, //hp
            new int[]{92, 93, 94, 96, 97, 98, 99, 100}, //map join
            (int[][]) Util.addArray(FULL_GALICK, FULL_KAMEJOKO), //skill
            new String[]{"|-1|Ta là Sôn Gô Ku",
                "|-1|Cơ thể này,sức mạnh này",
                "|-1|Ta khá thích việc loại bỏ các ngươi",
                "|-1|Mau chấp nhận số phận đi lũ sâu bọ"
            }, //text chat 1
            new String[]{"|-1|Các ngươi chỉ có vậy thôi sao?",
                "|-1|Đúng là loài người thấp kém",
                "|-2|Ngươi nói như thể ngươi không phải con người vậy?",
                "|-2|Chiếc nhẫn kia lẽ nào ngươi là một Kaioshin?!",
                "|-1|Các ngươi không nên biết quá nhiều",
                "|-2|Xem đòn đánh của ta đây !",
                "|-1|Được thôi, nếu muốn chết đến vậy, ta rất vui lòng!!"
            }, //text chat 2
            new String[]{"|-1|Biến hình! Super Sayan Rose"}, //text chat 3
            REST_15_M //second rest
    );

    public static final BossData BLACK_GOKU_ROSE = new BossData(
            "Black Goku Rose", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{553, 880, 881, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            4800000, //dame
            new double[]{20_000_000_0000l}, //hp
            new int[]{92, 93, 94, 96, 97, 98, 99, 100}, //map join
            new int[][]{
                {Skill.THAI_DUONG_HA_SAN, 7, 30000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 45000},
                {Skill.KAMEJOKO, 7, 1000},
                {Skill.LIEN_HOAN, 7, 500}},
            //skill
             new String[]{"|-1|Ta là Sôn Gô Ku",
                "|-1|Cơ thể này,sức mạnh này",
                "|-1|Ta khá thích việc loại bỏ các ngươi",
                "|-1|Mau chấp nhận số phận đi lũ sâu bọ"
            }, //text chat 1
            new String[]{"|-1|Các ngươi chỉ có vậy thôi sao?",
                "|-1|Đúng là loài người thấp kém",
                "|-2|Ngươi nói như thể ngươi không phải con người vậy?",
                "|-2|Chiếc nhẫn kia lẽ nào ngươi là một Kaioshin?!",
                "|-1|Các ngươi không nên biết quá nhiều",
                "|-2|Xem đòn đánh của ta đây !",
                "|-1|Được thôi, nếu muốn chết đến vậy, ta rất vui lòng!!"
            }, //text chat 2
            new String[]{"|-1|Biến hình! Super Sayan Rose"}, //text chat 3
            REST_15_M //second rest
    );
    
    // =====================CUMBER_HANH_TINH_NGUC_TU==================================
    public static final BossData CUMBER = new BossData(
            "Cumber", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{1311, 1312, 1313, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            100000, // dame
            new double[]{1_500_000_000L}, // hp
            new int[]{155, 165}, // map join
            (int[][]) Util.addArray(FULL_GALICK, FULL_ANTOMIC, FULL_TDHS), // skill
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            REST_5_M // second rest
    );

    public static final BossData SUPER_CUMBER = new BossData(
            "Super Cumber", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{1314, 1312, 1313, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            100000, // dame
            new double[]{3_000_000_000L}, // hp
            new int[]{155, 165}, // map join
            (int[][]) Util.addArray(FULL_GALICK, FULL_ANTOMIC, FULL_TDHS), // skill
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.ANOTHER_LEVEL // type appear
    );
    // =====================CUMBER_HANH_TINH_NGUC_TU==================================

    public static final BossData MABU = new BossData(
            "Mabư mập",
            ConstPlayer.XAYDA,
            new short[]{297, 298, 299, -1, -1, -1},
            500000,
            new double[]{1_000_000_000L},
            new int[]{127},
            new int[][]{
                {Skill.KAMEJOKO, 3, 5000},
                {Skill.DRAGON, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo",
                "|-1|Úm ba la xì bùa"}, // text chat 2
            new String[]{"|-1|Biến hình"}, // text chat 3
            REST_10_M);

    public static final BossData SUPER_BU = new BossData(
            "Super Bư",
            ConstPlayer.XAYDA,
            new short[]{421, 422, 423, -1, -1, -1},
            500000,
            new double[]{1_000_000_000L},
            new int[]{127, 128},
            new int[][]{
                {Skill.KAMEJOKO, 3, 5000},
                {Skill.DRAGON, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{"|-1|Biến hình"}, // text chat 3
            REST_10_M);

    public static final BossData BU_TENK = new BossData(
            "Bư Tênk",
            ConstPlayer.XAYDA,
            new short[]{424, 425, 426, -1, -1, -1},
            500000,
            new double[]{1_000_000_000L},
            new int[]{127},
            new int[][]{
                {Skill.KAMEJOKO, 3, 5000},
                {Skill.DRAGON, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo",
                "|-1|Ui da đau bụng quá"}, // text chat 2
            new String[]{"|-1|Biến hình"}, // text chat 3
            REST_10_M);

    public static final BossData BU_HAN = new BossData(
            "Bư Han",
            ConstPlayer.XAYDA,
            new short[]{427, 428, 429, -1, -1, -1},
            500000,
            new double[]{1_000_000_000L},
            new int[]{127},
            new int[][]{
                {Skill.KAMEJOKO, 3, 5000},
                {Skill.DRAGON, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{"|-1|Biến hình"}, // text chat 3
            REST_10_M);

    public static final BossData KID_BU = new BossData(
            "Kid Bư",
            ConstPlayer.XAYDA,
            new short[]{439, 440, 441, -1, -1, -1},
            500000,
            new double[]{1_000_000_000L},
            new int[]{127},
            new int[][]{
                {Skill.KAMEJOKO, 3, 5000},
                {Skill.DRAGON, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{"|-1|Biến hình"}, // text chat 3
            REST_10_M);

    public static final BossData SUPER_BU_BUNG = new BossData(
            "Super Bư",
            ConstPlayer.XAYDA,
            new short[]{421, 422, 423, -1, -1, -1},
            500000,
            new double[]{1_000_000_000L},
            new int[]{127, 128},
            new int[][]{
                {Skill.KAMEJOKO, 3, 5000},
                {Skill.DRAGON, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{}, // text chat 3
            REST_10_S);

    // ========================BOSS 12H========================
    public static final BossData MABU_12H = new BossData(
            "Mabư",
            ConstPlayer.XAYDA,
            new short[]{297, 298, 299, -1, -1, -1},
            10000,
            new double[]{500_000_000},
            new int[]{120},
            new int[][]{
                {Skill.TAI_TAO_NANG_LUONG, 3, 120000},
                {Skill.GALICK, 7, 1000}},
            new String[]{"|-1|Bư! Bư! Bư!",
                "|-1|Bư! Bư!",}, // text chat 1
            new String[]{"|-1|Oe Oe"}, // text chat 2
            new String[]{}, // text chat 3
            REST_1_M,
            new int[]{BossID.DRABURA_3});

    public static final BossData GOKU = new BossData(
            "Gôcu", // name
            ConstPlayer.XAYDA, // gender
            new short[]{101, 65, 66, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            100000, // dame
            new double[]{1_000_000_000L}, // hp
            new int[]{119}, // map join
            new int[][]{
                {Skill.GALICK, 7, 1000},
                {Skill.KAMEJOKO, 7, 1000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 10000},
                {Skill.THAI_DUONG_HA_SAN, 1, 60000}}, // skill
            new String[]{"|-1|Tỉnh lại đi Cađíc!",
                "|-1| ",
                "|-1|Đừng lùa gà nữa",
                "|-1|Một Server là quá đủ rồi!",
                "|-1|Các em chỉ có 10k ăn sáng thôi",
                "|-1| ",
                "|-1| ",
                "|-1| "}, // text chat 1
            new String[]{"|-1|Cađíc tỉnh lại đi!",
                "|-1|Trời ơi...Hắn định giết mọi người ở đây chắc?",
                "|-1|Cađíc! dừng tay lại! Cậu điên mất rồi!",
                "|-1|Cađíc tỉnh lại đi!",}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.CALL_BY_ANOTHER);

    public static final BossData CADIC = new BossData(
            "Ca Đít", // name
            ConstPlayer.XAYDA, // gender
            new short[]{103, 16, 17, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            100000, // dame
            new double[]{2_000_000_000}, // hp
            new int[]{119}, // map join
            new int[][]{
                {Skill.GALICK, 7, 1000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 10000},}, // skill
            new String[]{"|-1| ",
                "|-1|Không, còn điện thoại mới, xe mới, nhà mới thì sao",
                "|-1| ",
                "|-1| ",
                "|-1| ",
                "|-1|Khônggggggggggggggggggg!",
                "|-1|Tối nay ta chuẩn bị ăn gà ngập mồm rồi",
                "|-1| ",
                "|-1|Từ giờ hãy gọi ta là CađícMẹc"}, // text chat 1
            new String[]{"|-1|Chúng ta sẽ 1 mất 1 còn!",
                "|-1|Hãy xem đây",
                "|-1|Ngươi có một tội rất lớn là...",
                "|-1|Dám qua mặt ta",
                "|-1|Kakalốt! Ta chờ đợi giây phút này đã từ lâu!"}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.CALL_BY_ANOTHER);

    public static final BossData DRABURA = new BossData(
            "Drabura",
            ConstPlayer.XAYDA,
            new short[]{418, 419, 420, -1, -1, -1},
            10000,
            new double[]{100_000_000},
            new int[]{114},
            new int[][]{
                {Skill.GALICK, 7, 1000}},
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{"|-1|Đừng vội mừng, ta sẽ hồi sinh và thịt hết bọn mi"}, // text chat 3
            REST_1_M);
    public static final BossData DRABURA_2 = new BossData(
            "Drabura",
            ConstPlayer.XAYDA,
            new short[]{418, 419, 420, -1, -1, -1},
            200000,
            new double[]{50_000_000},
            new int[]{119},
            new int[][]{
                {Skill.GALICK, 7, 1000}},
            new String[]{"|-1|Ta đã trở lại, lợi hại gấp hai, hahaha"}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{"|-1|Hêhê..ta chẳng cần tốn sức đánh với các ngươi nữa",
                "|-1|Mà ta sẽ để cho các ngươi tự thanh toán lẫn nhau, xin chào",
                "|-2|Hắn nói sẽ để chúng ta tự tiêu diệt lẫn nhau",
                "|-2|Nghĩa là sao nhỉ?"}, // text chat 3
            REST_5_M,
            new int[]{BossID.GOKU, BossID.CADIC});

    public static final BossData DRABURA_3 = new BossData(
            "Drabura",
            ConstPlayer.XAYDA,
            new short[]{418, 419, 420, -1, -1, -1},
            100000,
            new double[]{100_000_000},
            new int[]{114},
            new int[][]{
                {Skill.GALICK, 7, 1000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 10000},},
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{"|-1|Đừng vội mừng, ta sẽ hồi sinh và thịt hết bọn mi"}, // text chat 3
            AppearType.CALL_BY_ANOTHER);

    public static final BossData BUI_BUI = new BossData(
            "Bui Bui",
            ConstPlayer.XAYDA,
            new short[]{451, 452, 453, -1, -1, -1},
            200000,
            new double[]{500_000_000},
            new int[]{115},
            new int[][]{
                {Skill.GALICK, 7, 10000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Hãy xem đây nhóc"}, // text chat 2
            new String[]{}, // text chat 3
            REST_1_M);

    public static final BossData BUI_BUI_2 = new BossData(
            "Bui Bui",
            ConstPlayer.XAYDA,
            new short[]{451, 452, 453, -1, -1, -1},
            200000,
            new double[]{300_000_000},
            new int[]{117},
            new int[][]{
                {Skill.GALICK, 7, 10000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Trọng lực bây giờ đã tăng gấp 10 lần",
                "|-1|Đó là điều kiện không gian lý tưởng của ta",
                "|-1|Nhưng lại rất bất lợi cho bọn mi"}, // text chat 2
            new String[]{"|-1|Đừng vội mừng, ta sẽ hồi sinh và thịt hết bọn mi"}, // text chat 3
            REST_1_M);

    public static final BossData YACON = new BossData(
            "Ya côn",
            ConstPlayer.XAYDA,
            new short[]{415, 416, 417, -1, -1, -1},
            200000,
            new double[]{500_000_000},
            new int[]{118},
            new int[][]{
                {Skill.GALICK, 7, 100}},
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            REST_1_M);

    public static final BossData SOI_HEC_QUYN = new BossData(
            "Sói hẹc quyn", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{394, 395, 396, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            500000000, // dame
            new double[]{1000_000_000_000L}, // hp
            new int[]{168}, // map join
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_GALICK), // skill
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            REST_5_S // second rest
    );

    public static final BossData O_DO = new BossData(
            "Ở dơ", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{400, 401, 402, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            1000000000, // dame
            new double[]{50_000_000_000_000L}, // hp
            new int[]{168}, // map join
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_GALICK), // skill
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            REST_5_S // second rest
    );

    public static final BossData XINBATO = new BossData(
            "Xinbatô", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{359, 360, 361, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            5000000000l, // dame
            new double[]{5000_000_000_000_000L}, // hp
            new int[]{168}, // map join
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_GALICK), // skill
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            REST_5_S // second rest
    );

    public static final BossData CHA_PA = new BossData(
            "Cha pa", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{362, 363, 364, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            50000000000l, // dame
            new double[]{500_000_000_000_000_000L}, // hp
            new int[]{168}, // map join
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_GALICK), // skill
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            REST_5_S // second rest
    );
    public static final BossData PON_PUT = new BossData(
            "Pon put", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{365, 366, 367, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            50000000000l, // dame
            new double[]{15_000_000_000_000_000_000d}, // hp
            new int[]{168}, // map join
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_GALICK), // skill
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            REST_5_S // second rest
    );

    public static final BossData CHAN_XU = new BossData(
            "Chan xư", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{371, 372, 373, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            100000000000l, // dame
            new double[]{500_000_000_000_000_000_000d}, // hp
            new int[]{168}, // map join
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_GALICK), // skill
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            REST_5_S // second rest
    );

    public static final BossData TAU_PAY_PAY = new BossData(
            "Tàu Pảy Pảy", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{92, 93, 94, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            200000000000l, // dame
            new double[]{50_000_000_000_000_000_000_000d}, // hp
            new int[]{168}, // map join
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_GALICK), // skill
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            REST_5_S // second rest
    );

    public static final BossData YAMCHA = new BossData(
            "Yamcha", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{374, 375, 376, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            500000000000l, // dame
            new double[]{5_000_000_000_000_000_000_000_000d}, // hp
            new int[]{168}, // map join
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_GALICK), // skill
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            REST_5_S // second rest
    );

    public static final BossData JACKY_CHUN = new BossData(
            "Jacky Chun", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{356, 357, 358, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            5000000000000l, // dame
            new double[]{5_000_000_000_000_000_000_000_000_000d}, // hp
            new int[]{168}, // map join
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_GALICK), // skill
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            REST_5_S // second rest
    );

    public static final BossData THIEN_XIN_HANG = new BossData(
            "Thiên xin hăng", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{368, 369, 370, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            5000000000000l, // dame
            new double[]{50_000_000_000_000_000_000_000_000_000d}, // hp
            new int[]{168}, // map join
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_GALICK), // skill
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            REST_5_S // second rest
    );
    public static final BossData THIEN_XIN_HANG_CLONE = new BossData(
            "Thiên xin hăng", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{368, 369, 370, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            100000, // dame
            new double[]{50_000_000_000_000_000_000_000_000_000d}, // hp
            new int[]{168}, // map join
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_GALICK, FULL_TDHS), // skill
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            REST_5_S // second rest
    );

    public static final BossData LIU_LIU = new BossData(
            "Liu Liu", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{397, 398, 399, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            5000000000000l, // dame
            new double[]{50_000_000_000_000_000_000_000_000_000_000d}, // hp
            new int[]{168}, // map join
            (int[][]) Util.addArray(FULL_KAMEJOKO, FULL_GALICK), // skill
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            REST_5_S // second rest
    );

    public static final BossData TAU_PAY_PAY_DONG_NAM_KARIN = new BossData(
            "Tàu Pảy Pảy", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{338, 339, 340, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10000, // dame
            new double[]{10000000000L}, // hp
            new int[]{111}, // map join
            (int[][]) Util.addArray(FULL_GALICK, FULL_KAMEJOKO), // skill
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            REST_30_M // second rest
    );

    public static final BossData TANTHUTNSM = new BossData(
            "Trợ Lý TNSM cᴀᴘʏʙᴀʀᴀ", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{1252, 1253, 1254, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10, // dame
            new double[]{10000000000l}, // hp
            new int[]{49}, // map join
            new int[][]{
                {Skill.KAMEJOKO, 7, 10000},
                {Skill.ANTOMIC, 7, 10000},
                {Skill.MASENKO, 7, 10000}},
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            0 // second rest
    );

   //************************************************************************** Boss Bojack
    public static final BossData BUJIN = new BossData(
            "Bujin", //name
            ConstPlayer.XAYDA, //gender
            new short[]{341, 342, 343, -1, -1, -1}, //outfit
            50000, //dame
            new double[]{5000_000_000l}, //hp
            new int[]{1, 2, 3, 4, 6}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.MASENKO, 7, 1000},
                {Skill.THOI_MIEN, 7, 100000}},//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Bojack có nhầm không nhỉ",
                "|-1|Các ngươi không nhúc nhích được sao?",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!",
                "|-1|Ta mà lại thua được sao?",
                "|-1|Hãy trả thù cho ta!"
            }, //text chat 3
            REST_10_M,
            new int[]{BossID.KOGU, BossID.ZANGYA, BossID.BIDO, BossID.BOJACK}
    );

    public static final BossData KOGU = new BossData(
            "Kogu", //name
            ConstPlayer.XAYDA, //gender
            new short[]{329, 330, 331, -1, -1, -1}, //outfit
            50000, //dame
            new double[]{5000_000_000l}, //hp
            new int[]{-1}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.MASENKO, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 100000}},//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Bojack có nhầm không nhỉ",
                "|-1|Các ngươi không nhúc nhích được sao?",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!",
                "|-1|Ta mà lại thua được sao?",
                "|-1|Hãy trả thù cho ta!"
            }, //text chat 3
            AppearType.APPEAR_WITH_ANOTHER //type appear
    );

    public static final BossData ZANGYA = new BossData(
            "Zangya", //name
            ConstPlayer.XAYDA, //gender
            new short[]{332, 333, 334, -1, -1, -1}, //outfit
            50000, //dame
            new double[]{7500_000_000l}, //hp
            new int[]{-1}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.ANTOMIC, 7, 1000}},//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Bojack có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!",
                "|-1|Ta mà lại thua được sao?",
                "|-1|Hãy trả thù cho ta!"
            }, //text chat 3
            AppearType.APPEAR_WITH_ANOTHER //type appear
    );

    public static final BossData BIDO = new BossData(
            "Bido", //name
            ConstPlayer.XAYDA, //gender
            new short[]{335, 336, 337, -1, -1, -1}, //outfit
            20720, //dame
            new double[]{10000_000_000l}, //hp
            new int[]{-1}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.ANTOMIC, 7, 3000}},//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Bojack có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!",
                "|-1|Ta mà lại thua được sao?",
                "|-1|Hãy trả thù cho ta!"
            }, //text chat 3
            AppearType.APPEAR_WITH_ANOTHER //type appear
    );

    public static final BossData BOJACK = new BossData(
            "Bojack", //name
            ConstPlayer.XAYDA, //gender
            new short[]{323, 324, 325, -1, -1, -1}, //outfit
            25020, //dame
            new double[]{12500_000_000l}, //hp
            new int[]{-1}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.KAMEJOKO, 7, 5000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 20000},
                {Skill.KHIEN_NANG_LUONG, 7, 130000},
                {Skill.THOI_MIEN, 7, 40000}},//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Bojack có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!",
                "|-1|Ta mà lại thua được sao?",
                "|-1|Aaaa, ngươi coi thường ta sao!"
            }, //text chat 3
            AppearType.APPEAR_WITH_ANOTHER //type appear
    );

    public static final BossData SUPER_BOJACK = new BossData(
            "Super Bojack 5 Củ", //name
            ConstPlayer.XAYDA, //gender
            new short[]{326, 327, 328, -1, -1, -1}, //outfit
            30000, //dame
            new double[]{35_000_000_000l}, //hp
            new int[]{-1}, //map join
            new int[][]{
                {Skill.SOCOLA, 7, 100000},
                {Skill.LIEN_HOAN, 7, 1000},
                {Skill.KAMEJOKO, 7, 10000},
                {Skill.KHIEN_NANG_LUONG, 7, 130000},},//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
                "|-1|Chán", "|-1|Đại ca Bojack có nhầm không nhỉ",
                "|-1|Một mình tao chấp hết tụi bây",
                "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
            }, //text chat 2
            new String[]{"|-1|Cay quá!"
            }, //text chat 3
            AppearType.ANOTHER_LEVEL //type appear
    );


    public static final BossData TAP_SU_0 = new BossData(
            "Tập sự-0", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{526, 525, 524, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10000, // dame
            new double[]{350_000}, // hp
            new int[]{131}, // map join
            new int[][]{
                {Skill.DRAGON, 1, 1000},
                {Skill.DEMON, 1, 1000},
                {Skill.GALICK, 1, 1000},
                {Skill.MASENKO, 1, 500},
                {Skill.ANTOMIC, 1, 500},
                {Skill.KAMEJOKO, 1, 500},
                {Skill.LIEN_HOAN, 1, 1000},
                {Skill.BIEN_KHI, 1, 3000000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, // skill
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER // type appear
    );

    public static final BossData TAP_SU_1 = new BossData(
            "Tập sự-1", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{526, 525, 524, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10000, // dame
            new double[]{350_000}, // hp
            new int[]{131}, // map join
            new int[][]{
                {Skill.DRAGON, 1, 1000},
                {Skill.DEMON, 1, 1000},
                {Skill.GALICK, 1, 1000},
                {Skill.MASENKO, 1, 1000},
                {Skill.ANTOMIC, 1, 1000},
                {Skill.KAMEJOKO, 1, 1000},
                {Skill.LIEN_HOAN, 1, 1000},
                {Skill.BIEN_KHI, 2, 3000000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, // skill
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER // type appear
    );

    public static final BossData TAP_SU_2 = new BossData(
            "Tập sự-2", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{526, 525, 524, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10000, // dame
            new double[]{350_000}, // hp
            new int[]{131}, // map join
            new int[][]{
                {Skill.DRAGON, 1, 1000},
                {Skill.DEMON, 1, 1000},
                {Skill.GALICK, 1, 1000},
                {Skill.MASENKO, 1, 1000},
                {Skill.ANTOMIC, 1, 1000},
                {Skill.KAMEJOKO, 1, 1000},
                {Skill.LIEN_HOAN, 1, 1000},
                {Skill.BIEN_KHI, 3, 3000000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, // skill
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER // type appear
    );

    public static final BossData TAP_SU_3 = new BossData(
            "Tập sự-3", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{526, 525, 524, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10000, // dame
            new double[]{350_000}, // hp
            new int[]{131}, // map join
            new int[][]{
                {Skill.DRAGON, 1, 1000},
                {Skill.DEMON, 1, 1000},
                {Skill.GALICK, 1, 1000},
                {Skill.MASENKO, 1, 1000},
                {Skill.ANTOMIC, 1, 1000},
                {Skill.KAMEJOKO, 1, 1000},
                {Skill.LIEN_HOAN, 1, 1000},
                {Skill.BIEN_KHI, 4, 3000000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, // skill
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER // type appear
    );

    public static final BossData TAP_SU_4 = new BossData(
            "Tập sự-4", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{526, 525, 524, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10000, // dame
            new double[]{350_000}, // hp
            new int[]{131}, // map join
            new int[][]{
                {Skill.DRAGON, 1, 1000},
                {Skill.DEMON, 1, 1000},
                {Skill.GALICK, 1, 1000},
                {Skill.MASENKO, 1, 1000},
                {Skill.ANTOMIC, 1, 1000},
                {Skill.KAMEJOKO, 1, 1000},
                {Skill.LIEN_HOAN, 1, 1000},
                {Skill.BIEN_KHI, 5, 3000000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, // skill
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER // type appear
    );

//    public static final BossData TAN_BINH_5 = new BossData(
//            "Tân binh-5", // name
//            ConstPlayer.TRAI_DAT, // gender
//            new short[]{527, 525, 524, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
//            10000, // dame
//            new double[]{450_000}, // hp
//            new int[]{131}, // map join
//            new int[][]{
//                {Skill.DRAGON, 1, 1000},
//                {Skill.DEMON, 1, 1000},
//                {Skill.GALICK, 1, 1000},
//                {Skill.MASENKO, 1, 1000},
//                {Skill.ANTOMIC, 1, 1000},
//                {Skill.KAMEJOKO, 1, 1000},
//                {Skill.LIEN_HOAN, 1, 1000},
//                {Skill.BIEN_KHI, 6, 3000000},
//                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, // skill
//            new String[]{}, // text chat 1
//            new String[]{"|-1|Khí công pháo"}, // text chat 2
//            new String[]{}, // text chat 3
//            REST_1_S,
//            new int[]{BossID.TAP_SU_0, BossID.TAP_SU_1, BossID.TAP_SU_2, BossID.TAP_SU_3,
//                BossID.TAP_SU_4} // type appear
//    );
    public static final BossData TAN_BINH_0 = new BossData(
            "Tân binh-0", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{527, 525, 524, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10000, // dame
            new double[]{450_000}, // hp
            new int[]{132}, // map join
            new int[][]{
                {Skill.DRAGON, 1, 1000},
                {Skill.DEMON, 1, 1000},
                {Skill.GALICK, 1, 1000},
                {Skill.MASENKO, 1, 1000},
                {Skill.ANTOMIC, 1, 1000},
                {Skill.KAMEJOKO, 1, 1000},
                {Skill.LIEN_HOAN, 1, 1000},
                {Skill.BIEN_KHI, 1, 3000000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, // skill
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER // type appear
    );

    public static final BossData TAN_BINH_1 = new BossData(
            "Tân binh-1", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{527, 525, 524, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10000, // dame
            new double[]{450_000}, // hp
            new int[]{132}, // map join
            new int[][]{
                {Skill.DRAGON, 1, 1000},
                {Skill.DEMON, 1, 1000},
                {Skill.GALICK, 1, 1000},
                {Skill.MASENKO, 1, 1000},
                {Skill.ANTOMIC, 1, 1000},
                {Skill.KAMEJOKO, 1, 1000},
                {Skill.LIEN_HOAN, 1, 1000},
                {Skill.BIEN_KHI, 2, 3000000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, // skill
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER // type appear
    );

    public static final BossData TAN_BINH_2 = new BossData(
            "Tân binh-2", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{527, 525, 524, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10000, // dame
            new double[]{450_000}, // hp
            new int[]{132}, // map join
            new int[][]{
                {Skill.DRAGON, 1, 1000},
                {Skill.DEMON, 1, 1000},
                {Skill.GALICK, 1, 1000},
                {Skill.MASENKO, 1, 1000},
                {Skill.ANTOMIC, 1, 1000},
                {Skill.KAMEJOKO, 1, 1000},
                {Skill.LIEN_HOAN, 1, 1000},
                {Skill.BIEN_KHI, 3, 3000000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, // skill
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER // type appear
    );

    public static final BossData TAN_BINH_3 = new BossData(
            "Tân binh-3", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{527, 525, 524, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10000, // dame
            new double[]{450_000}, // hp
            new int[]{132}, // map join
            new int[][]{
                {Skill.DRAGON, 1, 1000},
                {Skill.DEMON, 1, 1000},
                {Skill.GALICK, 1, 1000},
                {Skill.MASENKO, 1, 1000},
                {Skill.ANTOMIC, 1, 1000},
                {Skill.KAMEJOKO, 1, 1000},
                {Skill.LIEN_HOAN, 1, 1000},
                {Skill.BIEN_KHI, 4, 3000000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, // skill
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER // type appear
    );

    public static final BossData TAN_BINH_4 = new BossData(
            "Tân binh-4", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{527, 525, 524, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10000, // dame
            new double[]{450_000}, // hp
            new int[]{132}, // map join
            new int[][]{
                {Skill.DRAGON, 1, 1000},
                {Skill.DEMON, 1, 1000},
                {Skill.GALICK, 1, 1000},
                {Skill.MASENKO, 1, 1000},
                {Skill.ANTOMIC, 1, 1000},
                {Skill.KAMEJOKO, 1, 1000},
                {Skill.LIEN_HOAN, 1, 1000},
                {Skill.BIEN_KHI, 5, 3000000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, // skill
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER // type appear
    );

//    public static final BossData CHIEN_BINH_5 = new BossData(
//            "Chiến binh-5", // name
//            ConstPlayer.TRAI_DAT, // gender
//            new short[]{528, 525, 524, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
//            10000, // dame
//            new double[]{500_000}, // hp
//            new int[]{132}, // map join
//            new int[][]{
//                {Skill.DRAGON, 1, 1000},
//                {Skill.DEMON, 1, 1000},
//                {Skill.GALICK, 1, 1000},
//                {Skill.MASENKO, 1, 1000},
//                {Skill.ANTOMIC, 1, 1000},
//                {Skill.KAMEJOKO, 1, 1000},
//                {Skill.LIEN_HOAN, 1, 1000},
//                {Skill.BIEN_KHI, 6, 3000000},
//                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, // skill
//            new String[]{}, // text chat 1
//            new String[]{"|-1|Khí công pháo"}, // text chat 2
//            new String[]{}, // text chat 3
//            REST_1_S,
//            new int[]{BossID.TAN_BINH_0, BossID.TAN_BINH_1, BossID.TAN_BINH_2, BossID.TAN_BINH_3,
//                BossID.TAN_BINH_4} // type appear
//    );
    public static final BossData CHIEN_BINH_0 = new BossData(
            "Chiến binh-0", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{528, 525, 524, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10000, // dame
            new double[]{500_000}, // hp
            new int[]{133}, // map join
            new int[][]{
                {Skill.DRAGON, 1, 1000},
                {Skill.DEMON, 1, 1000},
                {Skill.GALICK, 1, 1000},
                {Skill.MASENKO, 1, 1000},
                {Skill.ANTOMIC, 1, 1000},
                {Skill.KAMEJOKO, 1, 1000},
                {Skill.LIEN_HOAN, 1, 1000},
                {Skill.BIEN_KHI, 2, 3000000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, // skill
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER // type appear
    );

    public static final BossData CHIEN_BINH_1 = new BossData(
            "Chiến binh-1", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{528, 525, 524, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10000, // dame
            new double[]{500_000}, // hp
            new int[]{133}, // map join
            new int[][]{
                {Skill.DRAGON, 1, 1000},
                {Skill.DEMON, 1, 1000},
                {Skill.GALICK, 1, 1000},
                {Skill.MASENKO, 1, 1000},
                {Skill.ANTOMIC, 1, 1000},
                {Skill.KAMEJOKO, 1, 1000},
                {Skill.LIEN_HOAN, 1, 1000},
                {Skill.BIEN_KHI, 3, 3000000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, // skill
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER // type appear
    );

    public static final BossData CHIEN_BINH_2 = new BossData(
            "Chiến binh-2", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{528, 525, 524, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10000, // dame
            new double[]{500_000}, // hp
            new int[]{133}, // map join
            new int[][]{
                {Skill.DRAGON, 1, 1000},
                {Skill.DEMON, 1, 1000},
                {Skill.GALICK, 1, 1000},
                {Skill.MASENKO, 1, 1000},
                {Skill.ANTOMIC, 1, 1000},
                {Skill.KAMEJOKO, 1, 1000},
                {Skill.LIEN_HOAN, 1, 1000},
                {Skill.BIEN_KHI, 4, 3000000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, // skill
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER // type appear
    );

    public static final BossData CHIEN_BINH_3 = new BossData(
            "Chiến binh-3", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{528, 525, 524, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10000, // dame
            new double[]{500_000}, // hp
            new int[]{133}, // map join
            new int[][]{
                {Skill.DRAGON, 1, 1000},
                {Skill.DEMON, 1, 1000},
                {Skill.GALICK, 1, 1000},
                {Skill.MASENKO, 1, 1000},
                {Skill.ANTOMIC, 1, 1000},
                {Skill.KAMEJOKO, 1, 1000},
                {Skill.LIEN_HOAN, 1, 1000},
                {Skill.BIEN_KHI, 5, 3000000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, // skill
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER // type appear
    );

    public static final BossData CHIEN_BINH_4 = new BossData(
            "Chiến binh-4", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{528, 525, 524, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10000, // dame
            new double[]{500_000}, // hp
            new int[]{133}, // map join
            new int[][]{
                {Skill.DRAGON, 1, 1000},
                {Skill.DEMON, 1, 1000},
                {Skill.GALICK, 1, 1000},
                {Skill.MASENKO, 1, 1000},
                {Skill.ANTOMIC, 1, 1000},
                {Skill.KAMEJOKO, 1, 1000},
                {Skill.LIEN_HOAN, 1, 1000},
                {Skill.BIEN_KHI, 6, 3000000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, // skill
            new String[]{}, // text chat 1
            new String[]{"|-1|Khí công pháo"}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER // type appear
    );

//    public static final BossData DOI_TRUONG_5 = new BossData(
//            "Đội trưởng-5", // name
//            ConstPlayer.TRAI_DAT, // gender
//            new short[]{529, 525, 524, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
//            10000, // dame
//            new double[]{1_000_000}, // hp
//            new int[]{133}, // map join
//            new int[][]{
//                {Skill.DRAGON, 1, 1000},
//                {Skill.DEMON, 1, 1000},
//                {Skill.GALICK, 1, 1000},
//                {Skill.MASENKO, 1, 1000},
//                {Skill.ANTOMIC, 1, 1000},
//                {Skill.KAMEJOKO, 1, 1000},
//                {Skill.LIEN_HOAN, 1, 1000},
//                {Skill.BIEN_KHI, 7, 3000000},
//                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},}, // skill
//            new String[]{}, // text chat 1
//            new String[]{"|-1|Khí công pháo"}, // text chat 2
//            new String[]{}, // text chat 3
//            REST_1_S,
//            new int[]{BossID.CHIEN_BINH_0, BossID.CHIEN_BINH_1, BossID.CHIEN_BINH_2, BossID.CHIEN_BINH_3,
//                BossID.CHIEN_BINH_4} // type appear
//    );
    public static final BossData KHIDOT = new BossData(
            "Khỉ đột", // name
            ConstPlayer.XAYDA, // gender
            new short[]{198, 193, 194, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            100000, // dame
            new double[]{100_000_000}, // hp
            new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20}, // map
            // join
            new int[][]{
                {Skill.GALICK, 7, 1000},
                {Skill.ANTOMIC, 7, 3000},
                {Skill.BIEN_KHI, 7, 60000},}, // skill
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            REST_15_M // second rest
    );

    public static final BossData NGUYETTHAN = new BossData(
            "Nguyệt thần", // name
            ConstPlayer.XAYDA, // gender
            new short[]{2058, 2059, 2060, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            1000, // dame
            new double[]{50_000_000}, // hp
            new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20}, // map
            // join
            new int[][]{
                {Skill.GALICK, 1, 2000},
                {Skill.ANTOMIC, 1, 6000},},
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            REST_15_M, // second rest
            new int[]{BossID.NHATTHAN});

    public static final BossData NHATTTHAN = new BossData(
            "Nhật thần", // name
            ConstPlayer.XAYDA, // gender
            new short[]{2065, 2066, 2067, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            1000, // dame
            new double[]{50_000_000}, // hp
            new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20}, // map
            // join
            new int[][]{
                {Skill.GALICK, 1, 2000},
                {Skill.ANTOMIC, 1, 6000},},
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER);

    public static final BossData DRACULA = new BossData(
            "Đracula", // name
            ConstPlayer.XAYDA, // gender
            new short[]{353, 354, 355, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            1000000000, // dame
            new double[]{1000000000}, // hp
            new int[]{112}, // map join
            new int[][]{
                {Skill.TAI_TAO_NANG_LUONG, 1, 60000},},
            new String[]{}, // text chat 1
            new String[]{"|-1|He he he",
                "|-1|Ta sẽ xé xác ngươi ra thành trăm mảnh",
                "|-1|Xem các ngươi mạnh đến đâu"}, // text chat 2
            new String[]{}, // text chat 3
            REST_1_S);

    public static final BossData NGUOI_VO_HINH = new BossData(
            "Người vô hình", // name
            ConstPlayer.XAYDA, // gender
            new short[]{377, 378, 379, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            1000000000, // dame
            new double[]{1_000_000_000}, // hp
            new int[]{112}, // map join
            new int[][]{
                {Skill.TAI_TAO_NANG_LUONG, 1, 60000},},
            new String[]{}, // text chat 1
            new String[]{"|-1|He he he",
                "|-1|Ta sẽ xé xác ngươi ra thành trăm mảnh",
                "|-1|Xem các ngươi mạnh đến đâu"}, // text chat 2
            new String[]{}, // text chat 3
            REST_1_S);

    public static final BossData BONG_BANG = new BossData(
            "Bông băng", // name
            ConstPlayer.XAYDA, // gender
            new short[]{350, 351, 352, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            1000000000, // dame
            new double[]{1_000_000_000}, // hp
            new int[]{112}, // map join
            new int[][]{
                {Skill.TAI_TAO_NANG_LUONG, 1, 60000},},
            new String[]{}, // text chat 1
            new String[]{"|-1|He he he",
                "|-1|Ta sẽ xé xác ngươi ra thành trăm mảnh",
                "|-1|Xem các ngươi mạnh đến đâu"}, // text chat 2
            new String[]{}, // text chat 3
            REST_1_S);

    public static final BossData VUA_QUY_SA_TANG = new BossData(
            "Vua Quỷ Sa tăng", // name
            ConstPlayer.XAYDA, // gender
            new short[]{344, 345, 346, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            1000000000, // dame
            new double[]{1_000_000_000}, // hp
            new int[]{112}, // map join
            new int[][]{
                {Skill.TAI_TAO_NANG_LUONG, 1, 60000},},
            new String[]{}, // text chat 1
            new String[]{"|-1|He he he",
                "|-1|Ta sẽ xé xác ngươi ra thành trăm mảnh",
                "|-1|Xem các ngươi mạnh đến đâu"}, // text chat 2
            new String[]{}, // text chat 3
            REST_1_S);

    public static final BossData THO_DAU_BAC = new BossData(
            "Thỏ Đầu Bạc", // name
            ConstPlayer.XAYDA, // gender
            new short[]{347, 348, 349, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            1000000000, // dame
            new double[]{1_000_000_000}, // hp
            new int[]{112}, // map join
            new int[][]{
                {Skill.TAI_TAO_NANG_LUONG, 1, 60000},},
            new String[]{}, // text chat 1
            new String[]{"|-1|He he he",
                "|-1|Ta sẽ xé xác ngươi ra thành trăm mảnh",
                "|-1|Xem các ngươi mạnh đến đâu"}, // text chat 2
            new String[]{}, // text chat 3
            REST_1_S);

    public static final BossData MA_TROI = new BossData(
            "Ma trơi", // name
            ConstPlayer.XAYDA, // gender
            new short[]{651, 652, 653, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            100, // dame
            new double[]{500_000}, // hp
            new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 24, 25,
                26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 63, 64, 65, 66, 67, 68, 69, 70,
                71, 72, 73, 74, 75, 76, 77, 79, 80, 81, 82, 83, 84, 92, 93, 94, 96, 97, 98, 99,
                100, 102, 103, 104, 105, 106, 107, 108, 109, 110}, // map join
            new int[][]{
                {Skill.GALICK, 7, Util.nextInt(5000, 10000)}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Khà khà"}, // text chat 2
            new String[]{}, // text chat 3
            REST_10_M);

    public static final BossData DOI = new BossData(
            "Dơi", // name
            ConstPlayer.XAYDA, // gender
            new short[]{654, 655, 656, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            100, // dame
            new double[]{500_000}, // hp
            new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 24, 25,
                26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 63, 64, 65, 66, 67, 68, 69, 70,
                71, 72, 73, 74, 75, 76, 77, 79, 80, 81, 82, 83, 84, 92, 93, 94, 96, 97, 98, 99,
                100, 102, 103, 104, 105, 106, 107, 108, 109, 110}, // map join
            new int[][]{
                {Skill.GALICK, 7, Util.nextInt(5000, 10000)}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Khà khà"}, // text chat 2
            new String[]{}, // text chat 3
            REST_10_M);

    public static final BossData BI_MA = new BossData(
            "Bí ma", // name
            ConstPlayer.XAYDA, // gender
            new short[]{760, 761, 762, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            100, // dame
            new double[]{500_000}, // hp
            new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 24, 25,
                26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 63, 64, 65, 66, 67, 68, 69, 70,
                71, 72, 73, 74, 75, 76, 77, 79, 80, 81, 82, 83, 84, 92, 93, 94, 96, 97, 98, 99,
                100, 102, 103, 104, 105, 106, 107, 108, 109, 110}, // map join
            new int[][]{
                {Skill.GALICK, 7, Util.nextInt(5000, 10000)}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Khà khà"}, // text chat 2
            new String[]{}, // text chat 3
            REST_10_M);

    public static final BossData COOLER_1 = new BossData(
            "Cooler", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{496, 497, 498, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            700000, //dame
            new double[]{50_000_000_000l}, //hp
            new int[]{106, 107, 108, 109}, //map join
            new int[][]{
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 10000},
                {Skill.DE_TRUNG, 7, 1000},
                {Skill.LIEN_HOAN, 1, 400},
                {Skill.MASENKO, 2, 300},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Các ngươi thật là yếu ớt",
                "|-1|Chán quá!",
                "|-1|Không có ai đủ mạnh để đấu với ta sao?",
                "|-1|Ta sẽ cho ngươi cái nịt!",}, //text chat 2
            new String[]{"|-1|Biến hình !!!!!!!!!!!!"}, //text chat 3
            REST_15_M
    );
    public static final BossData COOLER_2 = new BossData(
            "Cooler 2", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{499, 500, 501, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            700000, //dame
            new double[]{150_250_000_000l}, //hp
            new int[]{106, 107, 108, 109}, //map join
            new int[][]{
                {Skill.DE_TRUNG, 7, 1000},
                {Skill.KHIEN_NANG_LUONG, 7, 100000},
                {Skill.KAMEJOKO, 1, 400},
                {Skill.LIEN_HOAN, 2, 300},},//skill//skill
            new String[]{}, //text chat 1
            new String[]{"|-1|Các ngươi thật là yếu ớt",
                "|-1|Ta sẽ phá hủy hành tinh này",
                "|-1|Chán quá!",
                "|-1|Không có ai đủ mạnh để đấu với ta sao?",
                "|-1|Ta nghèo lắm!Đừng săn ta nữa",}, //text chat 2
            new String[]{"|-1|Đen lắm em trai !"}, //text chat 3
            AppearType.APPEAR_WITH_ANOTHER
    );
    public static final BossData CUU_VI = new BossData(
            "Cửu vĩ", // name
            ConstPlayer.XAYDA, // gender
            new short[]{1476, 1477, 1478, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            2000, // dame
            new double[]{100_000_000}, // hp
            new int[]{5, 6, 13, 19, 20, 29, 30, 33, 34, 37}, // map join
            (int[][]) Util.addArray(FULL_ANTOMIC, FULL_KAMEJOKO, FULL_MASENKO, FULL_GALICK), // skill
            new String[]{"|-1|Cửu vĩ tựa mây hồng"
            }, // text chat 1
            new String[]{"|-1|Ta sẽ hủy diệt làng lá Aaaa",
                "|-1|Bom vĩ thú",
                "|-1|Nên giải quyết con sâu nào trước đây",
                "|-1|HA HA HA",
                "|-1|Bố mày là cửu vĩ hồ",
                "|-1|Vào hết đây mấy con lợn",
                "|-1|Ta là bất khả chiến bại",}, // text chat 2
            new String[]{"|-1|Không thể nào",
                "|-1|Hãy trả thù cho ta Obito"}, // text chat 3
            REST_15_M, // second rest
            new int[]{BossID.OBITO}
    );
    public static final BossData OBITO = new BossData(
            "OBITO", // name
            ConstPlayer.XAYDA, // gender
            new short[]{1755, 1756, 1757, 171, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            5000, // dame
            new double[]{300_000_000}, // hp
            new int[]{11, 12, 13, 18, 19, 20, 27, 28, 29, 30, 31, 33, 34, 35, 36, 37, 38}, // map join
            new int[][]{
                {Skill.GALICK, 7, 1000},
                {Skill.KAMEJOKO, 7, 3000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 120_000},},
            new String[]{"|-1|Ta là OBITO đại nhân"
            }, // text chat 1
            new String[]{"|-1|Các ngươi nghĩ có thể cản được ta sao",
                "|-1|Kamui",
                "|-1|Mang đệ tứ đến đây",
                "|-1|Cửu vĩ đâu hãy tiêu diệt bọn chúng",
                "|-1|Sức mạnh của ta là tuyệt đối HAHAHA",
                "|-1|Bộc la thiên tinh",
                "|-1|Ta là bất khả chiến bại",}, // text chat 2
            new String[]{"|-1|Nãy giờ ta chưa thèm tung hết sức đâu",
                "|-1|Hóa Lục Đạo Yahhhhhhh"}, // text chat 3
            REST_30_M, // second rest,
            AppearType.APPEAR_WITH_ANOTHER,
            new int[]{BossID.OBITO_LUC_DAO}
    );
    public static final BossData OBITO_LUC_DAO = new BossData(
            "OBITO Lục Đạo", // name
            ConstPlayer.XAYDA, // gender
            new short[]{1758, 1759, 1760, 140, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            12000, // dame
            new double[]{500_000_000L}, // hp
            new int[]{110}, // map join
            // skill
            new int[][]{
                {Skill.GALICK, 7, 500},
                {Skill.KAMEJOKO, 7, 2000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.DICH_CHUYEN_TUC_THOI, 7, 30000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 120_000},
                {Skill.QUA_CAU_KENH_KHI, 7, 15_000}
            },
            new String[]{"|-1|Ta là OBITO đại nhân"
            }, // text chat 1
            new String[]{"|-1|Các ngươi nghĩ có thể cản được ta sao",
                "|-1|Kamui",
                "|-1|Mang đệ tứ đến đây",
                "|-1|Cửu vĩ đâu hãy tiêu diệt bọn chúng",
                "|-1|Sức mạnh của ta là tuyệt đối HAHAHA",
                "|-1|Bộc la thiên tinh",
                "|-1|Ta là bất khả chiến bại",}, // text chat 2
            new String[]{"|-1|Đáng ghét, không thể như thế được",
                "|-1|Ước mơ hủy diệt thế giới của ta!"}, // text chat 3
            REST_30_M,
            AppearType.CALL_BY_ANOTHER
    );
    public static final BossData HAI_KHUYEN = new BossData(
            "Hải Khuyển", // name
            ConstPlayer.XAYDA, // gender
            new short[]{1593, 1594, 1595, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            2000, // dame
            new double[]{350_000_000}, // hp
            new int[]{62, 63, 64, 65, 66, 67, 68, 71, 72, 76, 77}, // map join
            (int[][]) Util.addArray(FULL_ANTOMIC, FULL_KAMEJOKO, FULL_MASENKO, FULL_GALICK), // skill
            new String[]{"|-1|Nổi dậy nào anh em"
            }, // text chat 1
            new String[]{"|-1|Húuuuuuu",
                "|-1|Cú đớp của Hải Khuyển",
                "|-1|Muốn chết thì cứ bơi vào đây",
                "|-1|Go Go Go",
                "|-1|Đại Hồng Thủy ",
                "|-1|Ta sẽ nhấn chìm các ngươi hahaha",
                "|-1|Khôn hồn thì mau biến đi",}, // text chat 2
            new String[]{"|-1|Ta không thể bị đánh bại",
                "|-1|Hãy đợi đó ta sẽ quay trở lại"}, // text chat 3
            REST_15_M // second rest
    );
    public static final BossData BACH_LANG = new BossData(
            "Bạch Lang", // name
            ConstPlayer.XAYDA, // gender
            new short[]{1596, 1597, 1598, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            2000, // dame
            new double[]{350_000_000}, // hp
            new int[]{62, 63, 64, 65, 66, 67, 68, 71, 72, 76, 77}, // map join
            (int[][]) Util.addArray(FULL_ANTOMIC, FULL_KAMEJOKO, FULL_MASENKO, FULL_GALICK), // skill
            new String[]{"|-1|Nổi dậy nào anh em"
            }, // text chat 1
            new String[]{"|-1|Húuuuuuu",
                "|-1|Móng vuốt Bạch Lang",
                "|-1|Muốn chết thì cứ bơi vào đây",
                "|-1|Hôm nay trăng tròn thật Hahaha",
                "|-1|Ta sẽ xé xác các ngươi hahaha",
                "|-1|Khôn hồn thì mau biến đi",}, // text chat 2
            new String[]{"|-1|Ta không thể bị đánh bại",
                "|-1|Hãy đợi đó ta sẽ quay trở lại"}, // text chat 3
            REST_15_M // second rest
    );
    public static final BossData THIET_MA = new BossData(
            "Thiết Mã", // name
            ConstPlayer.XAYDA, // gender
            new short[]{1599, 1600, 1601, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            2000, // dame
            new double[]{350_000_000}, // hp
            new int[]{62, 63, 64, 65, 66, 67, 68, 71, 72, 76, 77}, // map join
            (int[][]) Util.addArray(FULL_ANTOMIC, FULL_KAMEJOKO, FULL_MASENKO, FULL_GALICK), // skill
            new String[]{"|-1|Nổi dậy nào anh em"
            }, // text chat 1
            new String[]{"|-1|Ố Ố Ố",
                "|-1|Có con Ngựa Ô Ngựa Ô",
                "|-1|Ngàn năm thương nhớ",
                "|-1|Đất Nước",
                "|-1|Có bao bài ca ngàn năm thương nhớ",
                "|-1|Nhớ con Ngựa Ô Ngựa Ô",
                "|-1|Để anh đón nàng",}, // text chat 2
            new String[]{"|-1|Ta không thể bị đánh bại",
                "|-1|Hãy đợi đó ta sẽ quay trở lại"}, // text chat 3
            REST_15_M // second rest
    );
    public static final BossData VUA_BACH_THU = new BossData(
            "Vua Bách Thú", // name
            ConstPlayer.XAYDA, // gender
            new short[]{1285, 1286, 1287, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            2000, // dame
            new double[]{1_000_000_000L}, // hp
            new int[]{62, 63, 64, 65, 66, 67, 68, 71, 72, 76, 77}, // map join
            (int[][]) Util.addArray(FULL_ANTOMIC, FULL_KAMEJOKO, FULL_MASENKO, FULL_GALICK, FULL_LIENHOAN), // skill
            new String[]{"|-1|Nổi dậy nào anh em"
            }, // text chat 1
            new String[]{"|-1|Vạn vật đều phải đầu hàng dưới chùy của ta",
                "|-1|Hừm hết rượu rồi sao",
                "|-1|Mang rượu tới đây cho ta",
                "|-1|Ta sẽ tiêu diệt hết đám bọn bay",
                "|-1|Chùy Bạch Ngân 1 Chùy cân tất",}, // text chat 2
            new String[]{"|-1|Các ngươi nghĩ đã hạ được ta sao",
                "|-1|Rồi có ngày ta sẽ quay trở lại"}, // text chat 3
            REST_15_M // second rest
    );
    public static final BossData ONG_GIA_NOEL = new BossData(
            "Ông già Noel", // name
            ConstPlayer.XAYDA, // gender
            new short[]{657, 658, 659, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            5_000_000, // dame
            new double[]{500}, // hp
            new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 24, 25,
                26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 63, 64, 65, 66, 67, 68, 69, 70,
                71, 72, 73, 74, 75, 76, 77, 79, 80, 81, 82, 83, 84, 92, 93, 94, 96, 97, 98, 99,
                100, 102, 103, 104, 105, 106, 107, 108, 109, 110}, // map join
            new int[][]{
                {Skill.TAI_TAO_NANG_LUONG, 7, Util.nextInt(5000, 10000)}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Mé ri chịch mệt",
                "|-1|Hô hô hô",
                "|-1|Giáng sinh vui vẻ!"}, // text chat 2
            new String[]{"|-1|Giáng sinh vui vẻ!"}, // text chat 3
            REST_1_M // type appear
    );

    public static final BossData THUY_TINH = new BossData(
            "Thủy Tinh", // name
            ConstPlayer.XAYDA, // gender
            new short[]{311, 312, 313, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            1000, // dame
            new double[]{50_000_000}, // hp
            new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 24, 25,
                26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 63, 64, 65, 66, 67, 68, 69, 70,
                71, 72, 73, 74, 75, 76, 77, 79, 80, 81, 82, 83, 84, 92, 93, 94, 96, 97, 98, 99,
                100, 102, 103, 104, 105, 106, 107, 108, 109, 110}, // map join
            new int[][]{
                {Skill.GALICK, 1, 2000},
                {Skill.ANTOMIC, 1, 6000},},
            new String[]{}, // text chat 1
            new String[]{"|-1|Trả Mị Nương lại cho ta",
                "|-1|Ta cho nước dâng chìm cả lũ bây giờ"}, // text chat 2
            new String[]{}, // text chat 3
            REST_15_M, // second rest
            new int[]{BossID.SON_TINH});

    public static final BossData SON_TINH = new BossData(
            "Sơn Tinh", // name
            ConstPlayer.XAYDA, // gender
            new short[]{314, 315, 316, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            1000, // dame
            new double[]{50_000_000}, // hp
            new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 24, 25,
                26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 63, 64, 65, 66, 67, 68, 69, 70,
                71, 72, 73, 74, 75, 76, 77, 79, 80, 81, 82, 83, 84, 92, 93, 94, 96, 97, 98, 99,
                100, 102, 103, 104, 105, 106, 107, 108, 109, 110}, // map join
            new int[][]{
                {Skill.GALICK, 1, 2000},
                {Skill.ANTOMIC, 1, 6000},},
            new String[]{}, // text chat 1
            new String[]{"|-1|Còn lâu á, chậm chân ráng chịu đi cưng",
                "|-1|Ta thách, chiêu này quá quen rồi"}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.APPEAR_WITH_ANOTHER);

    public static final BossData KARIN = new BossData(
            "Karin", // name
            ConstPlayer.XAYDA, // gender
            new short[]{89, 90, 91, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            50, // dame
            new double[]{10000000}, // hp
            new int[]{46}, // map join
            new int[][]{
                {Skill.GALICK, 7, 100}},
            new String[]{"|-1|Ta sẽ dạy ngươi vài chiêu",
                "|-1|Ta sẽ đánh hết sức, ngươi cẩn thận nhé"}, // text chat 1
            new String[]{"|-1|Haizzzzz",
                "|-1|Hahaha",
                "|-1|Xem đây"}, // text chat 2
            new String[]{"|-1|OK ta chịu thua"}, // text chat 3
            REST_1_S);

    public static final BossData TAUPAYPAY = new BossData(
            "Tàu Pảy Pảy", // name
            ConstPlayer.XAYDA, // gender
            new short[]{92, 93, 94, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            100, // dame
            new double[]{10_000_000}, // hp
            new int[]{47}, // map join
            new int[][]{
                {Skill.DRAGON, 1, 1000},
                {Skill.DRAGON, 2, 2000},
                {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.KAMEJOKO, 7, 2000},
                {Skill.KAMEJOKO, 6, 1800},
                {Skill.KAMEJOKO, 4, 1500},
                {Skill.KAMEJOKO, 2, 1000}
            },
            new String[]{"|-1|Ta sẽ dạy ngươi vài chiêu",
                "|-1|Ta sẽ đánh hết sức, ngươi cẩn thận nhé"}, // text chat 1
            new String[]{"|-1|Haizzzzz",
                "|-1|Hahaha",
                "|-1|Xem đây"}, // text chat 2
            new String[]{"|-1|OK ta chịu thua"}, // text chat 3
            REST_1_S);

    public static final BossData YAJIRO = new BossData(
            "Yajirô", // name
            ConstPlayer.XAYDA, // gender
            new short[]{77, 79, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            1100, // dame
            new double[]{1100}, // hp
            new int[]{46}, // map join
            new int[][]{
                {Skill.GALICK, 1, 1000}},
            new String[]{"|-1|Ngon nhào vô",
                "|-1|Cho mi biết sự lợi hại của ta"}, // text chat 1
            new String[]{"|-1|Haizzzzz",
                "|-1|Hahaha",
                "|-1|Xem đây"}, // text chat 2
            new String[]{"|-1|Ngươi thật lợi hại"}, // text chat 3
            REST_1_S);

    public static final BossData MRPOPO = new BossData(
            "Mr.PôPô", // name
            ConstPlayer.XAYDA, // gender
            new short[]{77, 79, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            1100, // dame
            new double[]{5100}, // hp
            new int[]{46}, // map join
            new int[][]{{Skill.GALICK, 1, 30000},
            {Skill.KAMEJOKO, 1, 30000},
            {Skill.THAI_DUONG_HA_SAN, 3, 30000},},
            new String[]{"|-1|Đánh trúng ta 1 cái coi như ngươi thắng",
                "|-1|Đánh trúng ta 3 cái coi như ngươi thắng"}, // text chat 1
            new String[]{"|-1|Haizzzzz",
                "|-1|Hahaha",
                "|-1|Xem đây",
                "|-1|Phù.."}, // text chat 2
            new String[]{"|-1|Thua thì thua"}, // text chat 3
            REST_1_S);

    public static final BossData THUONG_DE = new BossData(
            "Thượng đế", // name
            ConstPlayer.XAYDA, // gender
            new short[]{86, 87, 88, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10000, // dame
            new double[]{10_000}, // hp
            new int[]{49}, // map join
            new int[][]{
                {Skill.DRAGON, 1, 1000},},
            new String[]{"|-1|Ta sẽ dạy võ cho con trong phòng tập thời gian này",
                "|-1|Con hãy đánh hết sức nhé, ta sẽ không nương tay đâu"}, // text chat 1
            new String[]{"|-1|Haizzzzz",
                "|-1|Hahaha",
                "|-1|Xem đây"}, // text chat 2
            new String[]{"|-1|Ta rất tự hào về con"}, // text chat 3
            REST_1_S);

    public static final BossData KHI_BUBBLES = new BossData(
            "Khỉ Bubbles", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{95, 96, 97, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            30000, // dame
            new double[]{30_000}, // hp
            new int[]{48}, // map join
            new int[][]{
                {Skill.GALICK, 7, 1000}}, // skill
            new String[]{"|-1|Ù ù khẹt khẹt",
                "|-1|Ù ù khẹt khẹt"}, // text chat 1
            new String[]{"|-1|Ù ù khẹt khẹt",
                "|-1|khẹt khẹt",
                "|-1|ù ù khẹc khẹc",
                "|-1|khẹc khẹc",
                "|-1|éc éc",}, // text chat 2
            new String[]{"|-1|Éc Éc Éc Éc!"}, // text chat 3
            REST_1_S // second rest
    );

    public static final BossData THAN_VU_TRU = new BossData(
            "Thần Vũ Trụ", // name
            ConstPlayer.XAYDA, // gender
            new short[]{98, 99, 100, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            45000, // dame
            new double[]{45_000}, // hp
            new int[]{48}, // map join
            new int[][]{
                {Skill.GALICK, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 20000},},
            new String[]{"|-1|Ta sẽ dạy ngươi chiêu kaio-ken",
                "|-1|Ngươi cũng to gan lắm"}, // text chat 1
            new String[]{"|-1|Haizzzzz",
                "|-1|Hahaha",
                "|-1|Xem đây"}, // text chat 2
            new String[]{"|-1|Tại hôm nay ta...ta hơi bị đau bụng"}, // text chat 3
            REST_1_S);

    public static final BossData TO_SU_KAIO = new BossData(
            "Tổ sư Kaio", // name
            ConstPlayer.XAYDA, // gender
            new short[]{448, 449, 450, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            45000, // dame
            new double[]{45000}, // hp
            new int[]{50}, // map join
            new int[][]{
                {Skill.GALICK, 1, 60000},},
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            REST_1_S);

    public static final BossData WHIS = new BossData(
            "Whis", // name
            ConstPlayer.TRAI_DAT, // gender
            new short[]{838, 839, 840, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            10000, // dame
            new double[]{550_000}, // hp
            new int[]{154}, // map join
            new int[][]{
                {Skill.DRAGON, 7, 500}}, // skill
            new String[]{"|-1|Ta sẽ dạy ngươi vài chiêu"}, // text chat 1
            new String[]{"|-1|Xem đây",
                "|-1|Haizzzzz",
                "|-1|Hahaha", "|-1|AAAAAAAAAA"}, // text chat 2
            new String[]{}, // text chat 3
            REST_1_M);

    public static final BossData GOLDEN_FRIEZA = new BossData(
            "Fide Vàng", // name
            ConstPlayer.XAYDA, // gender
            new short[]{502, 503, 504, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            1000000, // dame
            new double[]{1_000_000_000}, // hp
            new int[]{6}, // map join
            new int[][]{
                {Skill.TAI_TAO_NANG_LUONG, 1, 120000}, {Skill.GALICK, 7, 1000},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1000},
                {Skill.KAMEJOKO, 3, 1000}, {Skill.KAMEJOKO, 4, 1000},
                {Skill.KAMEJOKO, 5, 1000}, {Skill.KAMEJOKO, 6, 1000},
                {Skill.KAMEJOKO, 7, 1000},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1000},
                {Skill.MASENKO, 3, 1000}, {Skill.MASENKO, 4, 1000},
                {Skill.MASENKO, 5, 1000}, {Skill.MASENKO, 6, 1000},
                {Skill.MASENKO, 7, 1000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1000},
                {Skill.ANTOMIC, 3, 1000}, {Skill.ANTOMIC, 4, 1000},
                {Skill.ANTOMIC, 5, 1000}, {Skill.ANTOMIC, 6, 1000},
                {Skill.ANTOMIC, 7, 1000},}, // skill
            new String[]{}, // text chat 1
            new String[]{"|-1|He he he",
                "|-1|Ta sẽ xé xác ngươi ra thành trăm mảnh",
                "|-1|Xem các ngươi mạnh đến đâu"}, // text chat 2
            new String[]{}, // text chat 3
            REST_5_M,
            new int[]{BossID.DEATH_BEAM_1, BossID.DEATH_BEAM_2, BossID.DEATH_BEAM_3, BossID.DEATH_BEAM_4,
                BossID.DEATH_BEAM_5});

    public static final BossData DEATH_BEAM = new BossData(
            "$", // name
            ConstPlayer.XAYDA, // gender
            new short[]{609, 610, 611, -1, 1, -1}, // outfit {head, body, leg, bag, aura, eff}
            1_000_000_000, // dame
            new double[]{500}, // hp
            new int[]{6}, // map join
            new int[][]{},
            new String[]{}, // text chat 1
            new String[]{}, // text chat 2
            new String[]{}, // text chat 3
            AppearType.CALL_BY_ANOTHER);

    public static final BossData LAN_CON = new BossData(
            "Lân con", // name
            ConstPlayer.XAYDA, // gender
            new short[]{763, 764, 765, -1, -1, -1}, // outfit {head, body, leg, bag, aura, eff}
            5000000, // dame
            new double[]{5_000_000}, // hp
            new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 24, 25,
                26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 63, 64, 65, 66, 67, 68, 69, 70,
                71, 72, 73, 74, 75, 76, 77, 79, 80, 81, 82, 83, 84, 92, 93, 94, 96, 97, 98, 99,
                100, 102, 103, 104, 105, 106, 107, 108, 109, 110}, // map join
            new int[][]{
                {Skill.TAI_TAO_NANG_LUONG, 7, Util.nextInt(5000, 10000)}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Tùng tùng xèng xèng"}, // text chat 2
            new String[]{}, // text chat 3
            REST_1_M // type appear
    );
    // ************************************************************************** Boss The Gioi (bat tu, tinh dame top 10 - cau hinh HP/map o worldboss.properties)
    public static final BossData WORLD_BOSS = new BossData(
            "Boss Thế Giới", // name
            ConstPlayer.XAYDA, // gender
            new short[]{502, 503, 504, -1, 1, -1}, // outfit Fide Vang cho oai
            1, // dame goc = 1 (khong danh chet nguoi, chi lam bao cat; muon manh hon sua tren panel WorldBoss)
            new double[]{9.0e14}, // hp goc 900k ty ~ bat tu; HP that lay tu WorldBossTuning.HP luc spawn
            new int[]{5}, // map join mac dinh = 5 (admin doi tren panel WorldBoss -> MAP_ID)
            new int[][]{
                {Skill.GALICK, 1, 5000},
                {Skill.TAI_TAO_NANG_LUONG, 1, 60000}},
            new String[]{}, // text chat 1
            new String[]{"|-1|Ta là Boss Thế Giới, bất tử! Hãy thể hiện sức mạnh của ngươi!",
                "|-1|Đánh càng đau, quà càng to! Top 1 nhận quà đặc biệt!",
                "|-1|Ta không bao giờ gục ngã!"}, // text chat 2
            new String[]{}, // text chat 3
            REST_1_M // second rest
    );
}
