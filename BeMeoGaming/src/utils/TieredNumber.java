package utils;

import java.math.BigInteger;

// Lop so phan tang: mantissa * (STEP ^ tier), STEP = 1 ty.
// Dung de bieu dien chi so vuot tran long ma van gon nhe.
public class TieredNumber {

    // He so nhay tier, 1 tier = gap 1 ty lan.
    public static final long STEP = 1000000000L;
    // Chan tren so tier de tranh tran va lap vo han.
    public static final int MAX_TIER = 40;
    // Nguong cap cho packet cu (client cu chi hien thi toi da ~9E15).
    private static final double CAP_DOUBLE = 9.0E15;

    // Phan dinh luong trong tier hien tai, luon < STEP khi da chuan hoa.
    public long mantissa;
    // Bac tier, cang cao gia tri cang lon.
    public int tier;

    // Ham dung mac dinh, gia tri 0|0.
    public TieredNumber() {
        try {
            this.mantissa = 0L;
            this.tier = 0;
        } catch (Exception e) {
            this.mantissa = 0L;
            this.tier = 0;
        }
    }

    // Ham dung voi gia tri tho, khong tu chuan hoa sau.
    public TieredNumber(long mantissa, int tier) {
        try {
            this.mantissa = mantissa;
            this.tier = tier;
        } catch (Exception e) {
            this.mantissa = 0L;
            this.tier = 0;
        }
    }

    // Tao moi va chuan hoa: mantissa >= STEP thi chia va tang tier.
    public static TieredNumber of(long m, int t) {
        try {
            // Tri am quy ve 0 de tranh so lieu loi.
            if (m < 0L) {
                m = 0L;
            }
            // Tier am quy ve 0.
            if (t < 0) {
                t = 0;
            }
            // Tier vuot tran thi cat tran.
            if (t > MAX_TIER) {
                t = MAX_TIER;
            }
            long mant = m;
            int tr = t;
            // Lap chuan hoa cho toi khi mantissa < STEP hoac dat tran tier.
            while (mant >= STEP && tr < MAX_TIER) {
                try {
                    mant = mant / STEP;
                    tr++;
                } catch (Exception e) {
                    break;
                }
            }
            // Neu van tran STEP o tier toi da thi cat tran.
            if (tr >= MAX_TIER && mant >= STEP) {
                mant = STEP - 1L;
                tr = MAX_TIER;
            }
            TieredNumber r = new TieredNumber();
            r.mantissa = mant;
            r.tier = tr;
            return r;
        } catch (Exception e) {
            TieredNumber r = new TieredNumber();
            r.mantissa = 0L;
            r.tier = 0;
            return r;
        }
    }

    // Cong theo tier: bang nhau cong mantissa, khac nhau giu so lon.
    public TieredNumber add(TieredNumber o) {
        try {
            // Cong voi null thi tra ve ban sao so hien tai.
            if (o == null) {
                return TieredNumber.of(this.mantissa, this.tier);
            }
            long m1 = this.mantissa;
            int t1 = this.tier;
            long m2 = o.mantissa;
            int t2 = o.tier;
            // Chuan hoa so am ve 0 truoc khi cong.
            if (m1 < 0L) {
                m1 = 0L;
            }
            if (m2 < 0L) {
                m2 = 0L;
            }
            if (t1 < 0) {
                t1 = 0;
            }
            if (t2 < 0) {
                t2 = 0;
            }
            if (t1 > MAX_TIER) {
                t1 = MAX_TIER;
            }
            if (t2 > MAX_TIER) {
                t2 = MAX_TIER;
            }
            // Cung tier thi cong truc tiep mantissa, co cap tran long.
            if (t1 == t2) {
                long sum;
                try {
                    if (m2 > 0L && m1 > Long.MAX_VALUE - m2) {
                        sum = Long.MAX_VALUE;
                    } else {
                        sum = m1 + m2;
                    }
                } catch (Exception e) {
                    sum = Long.MAX_VALUE;
                }
                return TieredNumber.of(sum, t1);
            }
            // Khac tier thi giu so lon, cong phan nho da quy doi.
            int bigTier;
            long bigMant;
            long smallMant;
            int diff;
            try {
                if (t1 > t2) {
                    bigTier = t1;
                    bigMant = m1;
                    smallMant = m2;
                    diff = t1 - t2;
                } else {
                    bigTier = t2;
                    bigMant = m2;
                    smallMant = m1;
                    diff = t2 - t1;
                }
            } catch (Exception e) {
                return TieredNumber.of(m1, t1);
            }
            // Quy doi so nho ve cung tier bang cach chia STEP nhieu lan.
            long addPart = smallMant;
            try {
                for (int i = 0; i < diff; i++) {
                    addPart = addPart / STEP;
                    if (addPart <= 0L) {
                        break;
                    }
                }
            } catch (Exception e) {
                addPart = 0L;
            }
            // Cong co cap tran long.
            long sum;
            try {
                if (addPart > 0L && bigMant > Long.MAX_VALUE - addPart) {
                    sum = Long.MAX_VALUE;
                } else {
                    sum = bigMant + addPart;
                }
            } catch (Exception e) {
                sum = bigMant;
            }
            return TieredNumber.of(sum, bigTier);
        } catch (Exception e) {
            try {
                return TieredNumber.of(this.mantissa, this.tier);
            } catch (Exception ex) {
                return new TieredNumber();
            }
        }
    }

    // Nhan theo phan tram thuc, vi du pctReal=10 la +10 phan tram.
    public TieredNumber mulPercent(double pctReal) {
        try {
            long m = this.mantissa;
            int t = this.tier;
            if (m < 0L) {
                m = 0L;
            }
            if (t < 0) {
                t = 0;
            }
            if (t > MAX_TIER) {
                t = MAX_TIER;
            }
            // He so nhan, cho phep am de tru chi so.
            double factor;
            try {
                factor = (100.0d + pctReal) / 100.0d;
            } catch (Exception e) {
                return TieredNumber.of(m, t);
            }
            // He so <= 0 thi ket qua ve 0.
            if (Double.isNaN(factor) || Double.isInfinite(factor) || factor <= 0.0d) {
                return TieredNumber.of(0L, t);
            }
            // Dung BigInteger noi bo de tranh tran long khi nhan.
            long newM;
            try {
                long num = Math.round(factor * 1000000.0d);
                BigInteger bi = BigInteger.valueOf(m);
                bi = bi.multiply(BigInteger.valueOf(num));
                bi = bi.divide(BigInteger.valueOf(1000000L));
                try {
                    newM = bi.longValueExact();
                } catch (ArithmeticException ae) {
                    newM = Long.MAX_VALUE;
                }
            } catch (Exception e) {
                return TieredNumber.of(m, t);
            }
            return TieredNumber.of(newM, t);
        } catch (Exception e) {
            try {
                return TieredNumber.of(this.mantissa, this.tier);
            } catch (Exception ex) {
                return new TieredNumber();
            }
        }
    }

    // Chuoi hien thi: tier 0 kieu so thuong, tier >= 1 kieu "65 X3Ti".
    public String toDisplayString() {
        try {
            long m = this.mantissa;
            int t = this.tier;
            if (m < 0L) {
                m = 0L;
            }
            if (t < 0) {
                t = 0;
            }
            // Tier >= 1 thi hien thi dang mantissa + X + tier + Ti.
            if (t >= 1) {
                return m + " X" + t + "Ti";
            }
            // Tier 0 thi hien thi kieu Nghin / Trieu / Ty thuong.
            if (m >= 1000000000L) {
                return (m / 1000000000L) + " Ty";
            }
            if (m >= 1000000L) {
                return (m / 1000000L) + " Trieu";
            }
            if (m >= 1000L) {
                return (m / 1000L) + " Nghin";
            }
            return String.valueOf(m);
        } catch (Exception e) {
            return "0";
        }
    }

    // Quy doi ve double co cap 9E15 de tuong thich packet cu.
    public double toCappedDouble() {
        try {
            long m = this.mantissa;
            int t = this.tier;
            if (m <= 0L || t < 0) {
                if (m <= 0L) {
                    return 0.0d;
                }
                t = 0;
            }
            if (t > MAX_TIER) {
                t = MAX_TIER;
            }
            double v = (double) m;
            try {
                for (int i = 0; i < t; i++) {
                    v = v * (double) STEP;
                    // Break som de tranh Infinity khi vuot nguong.
                    if (v > CAP_DOUBLE) {
                        break;
                    }
                }
            } catch (Exception e) {
                return CAP_DOUBLE;
            }
            if (v > CAP_DOUBLE) {
                return CAP_DOUBLE;
            }
            return v;
        } catch (Exception e) {
            return 0.0d;
        }
    }

    // Doc tu chuoi dang "m|t", vi du "65000|3".
    public static TieredNumber parse(String s) {
        try {
            if (s == null) {
                return TieredNumber.of(0L, 0);
            }
            String str = s.trim();
            if (str.length() == 0) {
                return TieredNumber.of(0L, 0);
            }
            int sep = str.indexOf('|');
            // Khong co dau | thi hieu la so tier 0.
            if (sep < 0) {
                long m = Long.parseLong(str.trim());
                return TieredNumber.of(m, 0);
            }
            String sm = str.substring(0, sep).trim();
            String st = str.substring(sep + 1).trim();
            long m = Long.parseLong(sm);
            int t = Integer.parseInt(st);
            return TieredNumber.of(m, t);
        } catch (Exception e) {
            return TieredNumber.of(0L, 0);
        }
    }

    // Chuoi luu tru dang "m|t" de ghi file hoac db.
    @Override
    public String toString() {
        try {
            return this.mantissa + "|" + this.tier;
        } catch (Exception e) {
            return "0|0";
        }
    }
}
