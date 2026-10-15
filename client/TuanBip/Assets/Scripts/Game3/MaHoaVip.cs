using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UnityEngine;

namespace Game3
{
    public class MaHoaVip
    {
        // Tạo một private static instance để lưu trữ thể hiện duy nhất của lớp.
        private static MaHoaVip instance;

        // Tạo private constructor để ngăn việc tạo thể hiện từ bên ngoài.
        private MaHoaVip()
        {
        }

        // Tạo một public static phương thức để truy xuất hoặc tạo thể hiện duy nhất.
        public static MaHoaVip gI()
        {
            if (instance == null)
            {
                instance = new MaHoaVip();
            }
            return instance;
        }
        public const long MOD = 8941489;

        public const long e = 127;

        public const long PRK = 5134843;
        long sqr(long x)
        {
            return x * x;
        }
        public long powMod(long a, long b)
        {
            if (b == 0) return 1 % MOD;
            else
         if (b % 2 == 0)
                return sqr(powMod(a, b / 2)) % MOD;
            else
                return a * (sqr(powMod(a, b / 2)) % MOD) % MOD;
        }
        public string longToBin(long number)
        {
            string binary = "";
            long mask = 1L << 63; // Bắt đầu với bit cao nhất (bit thứ 63)

            for (int i = 0; i < 64; i++) // Long có 64 bit
            {
                binary += ((number & mask) == 0) ? "0" : "1";
                number <<= 1; // Dịch số sang trái 1 bit
            }

            return binary;
        }
        public long binToLong(string binary)
        {
            long result = 0;
            int index = binary.Length - 1; // Bắt đầu từ bit thấp nhất (bên phải)

            while (index >= 0)
            {
                if (binary[index] == '1')
                {
                    result |= (1L << (binary.Length - 1 - index));
                }

                index--;
            }

            return result;
        }
        public List<string> encode(string s)
        {
            List<string> ans = new List<string>();
            foreach (char x in s)
            {
                long z = (byte)x;
                z = powMod(z, e);
                string k = longToBin(z);
                ans.Add(k);
            }
            return ans;
        }

        public string decode(string input)
        {
            string[] arr = input.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
            List<string> x = new List<string>(arr);
            string ans = "";
            foreach (string z in x)
            {
                long k = binToLong(z);
                long t = powMod(k, PRK);
                ans += (char)t;
            }
            return ans;
        }
        private static readonly string IPSERVER_UNICODE = "\\x00\\x54\\x00\\x68\\x00\\xe1\\x00\\xba\\x00\\xbf\\x00\\x20\\x00\\x47\\x00\\x69\\x00\\xe1\\x00\\xbb\\x00\\x9b\\x00\\x69\\x00\\x20\\x00\\x4e\\x00\\x67\\x00\\xe1\\x00\\xbb\\x00\\x8d\\x00\\x63\\x00\\x20\\x00\\x52\\x00\\xe1\\x00\\xbb\\x00\\x93\\x00\\x6e\\x00\\x67\\x00\\x3a\\x00\\x74\\x00\\x68\\x00\\x65\\x00\\x67\\x00\\x69\\x00\\x6f\\x00\\x69\\x00\\x6e\\x00\\x67\\x00\\x6f\\x00\\x63\\x00\\x72\\x00\\x6f\\x00\\x6e\\x00\\x67\\x00\\x2e\\x00\\x6f\\x00\\x6e\\x00\\x6c\\x00\\x69\\x00\\x6e\\x00\\x65\\x00\\x3a\\x00\\x31\\x00\\x34\\x00\\x34\\x00\\x34\\x00\\x35\\x00\\x3a\\x00\\x30\\x00\\x2c\\x00\\x30\\x00\\x2c\\x00\\x30";


        public static string giaimaUnicode()
        {
            // Chuyển chuỗi hex thành mảng byte
            byte[] encodedBytes = ConvertHexStringToByteArray(IPSERVER_UNICODE.Replace("\\x", ""));
            if (encodedBytes == null)
            {
                return null;
            }
            Debug.Log("encodedBytes    " + encodedBytes);
            // Giải mã thành chuỗi UTF-8
            return DecodeUnicode(encodedBytes);
        }

        public static byte[] ConvertHexStringToByteArray(string hexString)
        {
            if (hexString.Length % 2 != 0)
            {
                return null; // Chuỗi không hợp lệ
            }

            int length = hexString.Length / 2;
            byte[] byteArray = new byte[length];

            try
            {
                for (int i = 0; i < hexString.Length; i += 2)
                {
                    string hexValue = hexString.Substring(i, 2);
                    byteArray[i / 2] = Convert.ToByte(hexValue, 16);
                }
            }
            catch (FormatException e)
            {
                Console.WriteLine("Lỗi định dạng hex: " + e.Message);
                return null;
            }

            return byteArray;
        }

        public static string DecodeUnicode(byte[] encodedBytes)
        {
            byte[] utf8Bytes = new byte[encodedBytes.Length / 2];
            int utf8Index = 0;

            for (int i = 0; i < encodedBytes.Length; i += 2)
            {
                if (encodedBytes[i] == 0)
                {
                    utf8Bytes[utf8Index++] = encodedBytes[i + 1];
                }
                else
                {
                    return null; // Chuỗi không hợp lệ
                }
            }

            try
            {
                return Encoding.UTF8.GetString(utf8Bytes, 0, utf8Index);
            }
            catch (Exception e)
            {
                Console.WriteLine("Lỗi giải mã: " + e.Message);
                return null;
            }
        }
    }
}
