package consts;

public class ConstPlayer {

    public static final int[] HEADMONKEY = {192, 195, 196, 199, 197, 200, 198};

    //--------------------------------------------------------------------------
    /*
     * {380, 381, 382}: ht lưỡng long nhất thể xayda trái đất
     * {383, 384, 385}: ht porata xayda trái đất
     * {391, 392, 393}: ht namếc
     * {870, 871, 872}: ht c2 trái đất
     * {873, 874, 875}: ht c2 namếc
     * {867, 878, 869}: ht c2 xayda
     */
    public static final short[][][] OUTFIT_FUSION = {
        //[gender {0,1,2}][typefusion {luonglong,cap1,cap2,s,ss,sss}][outfit {head,body,leg}]
        {/*td*/{380, 381, 382},/*cap1*/ {1264, 1265, 1266},/*cap2*/ {557, 558, 559},/*s*/ {380, 381, 382},/*ss*/{2063, 2064, 2065},/*sss*/ {1249, 1250, 1251}},
        {/*nm*/{391, 392, 393},/*cap1*/ {1270, 1271, 1272},/*cap2*/ {1003, 1004, 1005},/*s*/ {391, 392, 393},/*ss*/{2063, 2064, 2065},/*sss*/ {2099, 2100, 2101}},
        {/*xd*/{380, 381, 382},/*cap1*/ {1267, 1268, 1269},/*cap2*/ {1006, 1007, 1008},/*s*/ {380, 381, 382},/*ss*/{2063, 2064, 2065},/*sss*/ {2102, 2103, 2104}},
    };

    public static final byte TRAI_DAT = 0;
    public static final byte NAMEC = 1;
    public static final byte XAYDA = 2;

    //type pk
    public static final byte NON_PK = 0;
    public static final byte PK_PVP = 3;
    public static final byte PK_PVP_2 = 4;
    public static final byte PK_ALL = 5;

    //type fushion
    public static final byte NON_FUSION = 0;
    public static final byte LUONG_LONG_NHAT_THE = 1;
    public static final byte HOP_THE_PORATA = 2;
    public static final byte HOP_THE_PORATA2 = 3;
    public static final byte HOP_THE_PORATA_S = 4;
    public static final byte HOP_THE_PORATA_SS = 5;
    public static final byte HOP_THE_PORATA_SSS = 6;
}
