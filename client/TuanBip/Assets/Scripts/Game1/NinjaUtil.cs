namespace Game1
{
    using System;
    using System.Numerics;

    public class NinjaUtil
    {
        public static void onLoadMapComplete()
        {
            GameCanvas.endDlg();
        }

        public void onLoading()
        {
            GameCanvas.startWaitDlg(mResources.downloading_data);
        }

        public static int randomNumber(int max)
        {
            MyRandom myRandom = new MyRandom();
            return myRandom.nextInt(max);
        }

        public static sbyte[] readByteArray(Message msg)
        {
            try
            {
                int num = msg.reader().readInt();
                sbyte[] data = new sbyte[num];
                msg.reader().read(ref data);
                return data;
            }
            catch (Exception)
            {
            }
            return null;
        }

        public static sbyte[] readByteArray(myReader dos)
        {
            try
            {
                int num = dos.readInt();
                sbyte[] data = new sbyte[num];
                dos.read(ref data);
                return data;
            }
            catch (Exception)
            {
                Cout.LogError("LOI DOC readByteArray dos  NINJAUTIL");
            }
            return null;
        }

        public static string replace(string text, string regex, string replacement)
        {
            return text.Replace(regex, replacement);
        }

        public static string numberTostring(string number)
        {
            string text = string.Empty;
            string text2 = string.Empty;
            if (number.Equals(string.Empty))
            {
                return text;
            }
            if (number[0] == '-')
            {
                text2 = "-";
                number = number.Substring(1);
            }
            for (int num = number.Length - 1; num >= 0; num--)
            {
                text = (((number.Length - 1 - num) % 3 != 0 || number.Length - 1 - num <= 0) ? (number[num] + text) : (number[num] + "." + text));
            }
            return text2 + text;
        }

        public static string getDate(int second)
        {
            long num = (long)second * 1000L;
            DateTime dateTime = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc).Add(new TimeSpan(num * 10000)).ToUniversalTime();
            int hour = dateTime.Hour;
            int minute = dateTime.Minute;
            int day = dateTime.Day;
            int month = dateTime.Month;
            int year = dateTime.Year;
            return day + "/" + month + "/" + year + " " + hour + "h";
        }

        public static string getDate2(long second)
        {
            long num = second + 25200000;
            DateTime dateTime = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc).Add(new TimeSpan(num * 10000)).ToUniversalTime();
            int hour = dateTime.Hour;
            int minute = dateTime.Minute;
            return hour + "h" + minute + "m";
        }

        public static string getTime(int timeRemainS)
        {
            int num = 0;
            if (timeRemainS > 60)
            {
                num = timeRemainS / 60;
                timeRemainS %= 60;
            }
            int num2 = 0;
            if (num > 60)
            {
                num2 = num / 60;
                num %= 60;
            }
            int num3 = 0;
            if (num2 > 24)
            {
                num3 = num2 / 24;
                num2 %= 24;
            }
            string empty = string.Empty;
            if (num3 > 0)
            {
                empty += num3;
                empty += "d";
                return empty + num2 + "h";
            }
            if (num2 > 0)
            {
                empty += num2;
                empty += "h";
                return empty + num + "'";
            }
            empty = ((num <= 9) ? (empty + "0" + num) : (empty + num));
            empty += ":";
            if (timeRemainS > 9)
            {
                return empty + timeRemainS;
            }
            return empty + "0" + timeRemainS;
        }

        public static string getMoneys(double m)
        {
            if (double.IsNaN(m) || double.IsInfinity(m) || m > 7.9e28)
            {
                return NumberText(m);
            }
            BigInteger integerPart = (BigInteger)m;

            return integerPart.ToString("#,##0");
        }

        public static string NumberText(double m)
        {
            string text = string.Empty;
            if (double.IsNaN(m) || double.IsInfinity(m))
            {
                return "0";
            }
            if (m < 0)
            {
                return "-" + NumberText(0.0 - m);
            }

            if (m <= 99_999_999_999D)
            {
                BigInteger num = (BigInteger)m;
                while (num >= 1000)
                {
                    BigInteger num2 = num % 1000;
                    text = num2.ToString("D3") + text;
                    if (num >= 1000) text = "." + text;
                    num /= 1000;
                }
                text = num + text;
                return text;
            }
            // Số lớn: gom mỗi 3 chữ số thành 1 đơn vị (Nghìn, Triệu, Tỷ, Tỷ.Tỷ, Tỷ.Tỷ.Tỷ...)
            double formattedNumber = m;
            int soLanChia = 0;
            while (formattedNumber >= 1000)
            {
                formattedNumber /= 1000;
                soLanChia++;
            }

            string formattedString = formattedNumber.ToString("N2").Replace(".", ",");
            if (formattedString.EndsWith(",00"))
            {
                formattedString = formattedString.Substring(0, formattedString.Length - 3);
            }

            return formattedString + DonViLon(soLanChia);
        }

        // 1 = Nghìn (K), 2 = Triệu (Tr), 3 = Tỷ, 4 = K.Tỷ, 6 = Tỷ.Tỷ, 7 = K.Tỷ.Tỷ, ...
        // quá 4 từ thì rút gọn thành " Tỷ×n" (n tầng tỷ)
        public static string DonViLon(int soLanChia)
        {
            if (soLanChia <= 0)
            {
                return string.Empty;
            }
            string donVi = ((soLanChia % 3 == 1) ? "K" : ((soLanChia % 3 == 2) ? "Tr" : "Tỷ"));
            int soTangTys = (soLanChia - 1) / 3;
            if (soTangTys <= 3)
            {
                string ketQua = " " + donVi;
                for (int i = 0; i < soTangTys; i++)
                {
                    ketQua += ".Tỷ";
                }
                return ketQua;
            }
            // Quá 4 từ thì rút gọn lại: "Tỷ×34" = (10^9)^34
            return (donVi == "Tỷ") ? (" Tỷ×" + (soTangTys + 1)) : (" " + donVi + ".Tỷ×" + soTangTys);
        }

        public static string getTimeAgo(int timeRemainS)
        {
            int num = 0;
            if (timeRemainS > 60)
            {
                num = timeRemainS / 60;
                timeRemainS %= 60;
            }
            int num2 = 0;
            if (num > 60)
            {
                num2 = num / 60;
                num %= 60;
            }
            int num3 = 0;
            if (num2 > 24)
            {
                num3 = num2 / 24;
                num2 %= 24;
            }
            string empty = string.Empty;
            if (num3 > 0)
            {
                empty += num3;
                empty += "d";
                return empty + num2 + "h";
            }
            if (num2 > 0)
            {
                empty += num2;
                empty += "h";
                return empty + num + "'";
            }
            if (num == 0)
            {
                num = 1;
            }
            empty += num;
            return empty + "ph";
        }

        public static string[] split(string original, string separator)
        {
            MyVector myVector = new MyVector();
            for (int num = original.IndexOf(separator); num >= 0; num = original.IndexOf(separator))
            {
                myVector.addElement(original.Substring(0, num));
                original = original.Substring(num + separator.Length);
            }
            myVector.addElement(original);
            string[] array = new string[myVector.size()];
            if (myVector.size() > 0)
            {
                for (int i = 0; i < myVector.size(); i++)
                {
                    array[i] = (string)myVector.elementAt(i);
                }
            }
            return array;
        }
    }
}