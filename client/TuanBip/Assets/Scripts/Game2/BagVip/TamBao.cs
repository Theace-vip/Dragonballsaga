using Game2.Assets.src.e;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Text;
using UnityEngine;
using UnityEngine.UIElements;

namespace Game2.BagVip
{
    public class TamBao : IActionListener
    {
        private Image bgSize;
        private Image bgItem;
        private Image[] nutQuay = new Image[2];
        private Image[] nutNhan = new Image[2];
        private Image[] khungItemMoc = new Image[2];
        private Image imgX;
        private Image khungKey;
        private Image imgDanhan;
        private Image imgVip;

        public static MocTamBao[] Moc_TamBao;
        private static TamBao instance;
        public bool isShow = false;
        private Scroll scrollInfo;
        private Scroll scrollTab;
        private int X, Y, W, H;
        private Item itemDetails;
        private bool isItemInfo;
        public static int idItemKey;
        public static int iconKey;
        public static int[] active_vip;

        public int currIndex = 0;
        public static int START;
        public static TamBao gI()
        {
            if (instance == null)
            {
                instance = new TamBao();
            }
            return instance;
        }
        private class Cell
        {
            public int x;
            public int y;
            public Cell(int x, int y)
            {
                this.x = x;
                this.y = y;
            }
            public void paint(mGraphics g)
            {
                g.drawImage(TamBao.gI().bgItem, x, y);
            }
        }
        public TamBao()
        {
            bgSize = GameCanvas.loadImage("/tambao/bgtambao.png");
            bgItem = GameCanvas.loadImage("/tambao/bgItem.png");
            X = GameCanvas.w / 2 - bgSize.getWidth() / 2 + 5;
            Y = GameCanvas.h / 2 - bgSize.getHeight() / 2 - 35;
            W = bgSize.getWidth() - 10;
            H = bgSize.getHeight() - 80;

            imgX = GameCanvas.loadImage("/tambao/imgclose.png");
            khungKey = GameCanvas.loadImage("/tambao/khungkey.png");
            imgDanhan = GameCanvas.loadImage("/tambao/danhan.png");
            imgVip = GameCanvas.loadImage("/tambao/vip.png");
            for (int i = 0; i < nutQuay.Length; i++)
            {
                if (nutQuay[i] == null)
                {
                    nutQuay[i] = GameCanvas.loadImage($"/tambao/nutquay{i}.png");
                }
            }
            for (int i = 0; i < khungItemMoc.Length; i++)
            {
                if (khungItemMoc[i] == null)
                {
                    khungItemMoc[i] = GameCanvas.loadImage($"/tambao/moc{i}.png");
                }
            }
            for (int i = 0; i < nutNhan.Length; i++)
            {
                if (nutNhan[i] == null)
                {
                    nutNhan[i] = GameCanvas.loadImage($"/tambao/nutnhan{i}.png");
                }
            }
            InitScrollTab();
        }
        public void update()
        {
            if (!isShow) return;

            if (isItemInfo == false)
            {
                updateScroll(scrollTab);
            }
            else
            {
                updateScroll(scrollInfo);
            }
        }

        private void InitScrollInfo()
        {
            try
            {
                GameCanvas.clearAllPointerEvent();
                scrollInfo = new Scroll();
                scrollInfo.clear();
                if (itemDetails != null && itemDetails.itemOption != null)
                {
                    List<string> lines = BreakStringIntoLines(itemDetails.template.description, bgSize.getWidth() / 3 + 30 - 12, mFont.tahoma_7_grey);
                    scrollInfo.setStyle(itemDetails.itemOption.Length, mFont.tahoma_7_green.getHeight() + itemDetails.itemOption.Length + lines.Count,
                        X + 65, Y + 110, (bgSize.getWidth() / 3 + 20), bgSize.getHeight() / 2 - 80, true, 1);
                }
            }
            catch (Exception e)
            {
                Debug.LogError(e.ToString());
            }
        }
        private void InitScrollTab()
        {
            try
            {
                int x_qua = X + 10;
                int y_qua = Y + 80;
                int w_qua = W / 3 - 20;
                int h_qua = H;
                if (Moc_TamBao != null && Moc_TamBao.Length != 0)
                {
                    scrollTab = new Scroll();
                    scrollTab.setStyle(Moc_TamBao.Length, (bgItem.getHeight() + 5) * Moc_TamBao.Length
                    , x_qua, y_qua - bgItem.getHeight() / 2, w_qua, h_qua, true, Moc_TamBao.Length);
                }
                GameCanvas.clearAllPointerEvent();
            }
            catch (Exception e)
            {
                Debug.LogError(e.ToString());
            }
        }
        public void updateScroll(Scroll sc)
        {
            if (sc != null && sc.cmyLim > 0 && !GameCanvas.isPointerClick)
            {
                ScrollResult s = sc.updateKey();
                if (sc.cmy < 0 && !s.isDowning)
                {
                    sc.cmy -= sc.cmy / 2;
                }
                else if (sc.cmy > sc.cmyLim && !s.isDowning)
                {
                    sc.cmy -= (sc.cmy - sc.cmyLim + 6) / 2;
                }
                if (s.isDowning) GameCanvas.isPointerMove = false;
                GameCanvas.clearKeyHold();
            }
            else if (sc != null && sc.cmxLim > 0 && !GameCanvas.isPointerClick)
            {
                ScrollResult sh = sc.updateKey();
                if (sc.cmx < 0 && !sh.isDowning)
                {
                    sc.cmx -= sc.cmx / 2;
                }
                else if (sc.cmx > sc.cmxLim && !sh.isDowning)
                {
                    sc.cmx -= (sc.cmx - sc.cmxLim + 6) / 2;
                }
                if (sh.isDowning) GameCanvas.isPointerMove = false;
                GameCanvas.clearKeyHold();
            }
        }

        public void Paint(mGraphics g)
        {
            try
            {
                if (!isShow) return;
                //vẽ khung tổng thể
                int x_khung = GameCanvas.w / 2 - bgSize.getWidth() / 2;
                int y_khung = GameCanvas.h / 2 - bgSize.getHeight() / 2;
                g.drawImage(bgSize, x_khung, y_khung, 0);

                //vẽ khung chứa key
                int x_khungKey = X + bgSize.getWidth() - khungKey.getWidth() - 20;
                int y_khungKey = Y + 80;
                g.drawImage(khungKey, x_khungKey, y_khungKey, 0);
                SmallImage.drawSmallImage(g, iconKey, x_khungKey + 12, y_khungKey + 12, 0, mGraphics.VCENTER | mGraphics.HCENTER);
                int Soluong_key = GetItemQuantity(idItemKey);
                mFont.tahoma_7b_dark.drawString(g, Soluong_key.ToString(), x_khungKey + 27, y_khungKey + 8, mFont.LEFT);

                //vẽ khung tổng lượt
                int x_daquay = X + bgSize.getWidth() / 3;
                int y_daquay = Y + 80;
                g.drawImage(khungKey, x_daquay, y_daquay, 0);
                mFont.tahoma_7_grey.drawString(g, "Điểm: " + Char.myCharz().diem_quay, x_daquay + 5, y_daquay + 8, mFont.LEFT);

                //vẽ nút tắt X
                int x_imgX = GameCanvas.w / 2 + bgSize.getWidth() / 2 - imgX.getWidth() + 2;
                int y_imgX = GameCanvas.h / 2 - bgSize.getHeight() / 2 + imgX.getHeight() + 2;
                g.drawImage(imgX, x_imgX, y_imgX, 0);
                if (GameCanvas.isPointerHoldIn(x_imgX, y_imgX, imgX.getWidth(), imgX.getHeight()))
                {
                    if (GameCanvas.isPointerJustRelease && GameCanvas.isPointerClick)
                    {
                        isItemInfo = false;
                        isShow = false;
                        GameCanvas.clearAllPointerEvent();
                    }
                }

                //vẽ nút quay
                int x_quay = X + bgSize.getWidth() / 3;
                int y_quay = Y + bgSize.getHeight() - 10;
                g.drawImage((GetItemQuantity(idItemKey) > 0) ? nutQuay[0] : nutQuay[1], x_quay + nutQuay[0].getWidth() / 2, y_quay, 0);
                mFont.tahoma_7b_dark.drawString(g, "Quay x1", x_quay + nutQuay[0].getWidth(), y_quay + nutQuay[0].getHeight() / 3, mGraphics.VCENTER | mGraphics.HCENTER);
                g.drawImage((GetItemQuantity(idItemKey) >= 10) ? nutQuay[0] : nutQuay[1], x_quay - nutQuay[0].getWidth() / 2 + 2 * (nutQuay[0].getWidth() + 20), y_quay, 0);
                mFont.tahoma_7b_dark.drawString(g, "Quay x10", x_quay + 2 * (nutQuay[0].getWidth() + 20), y_quay + nutQuay[0].getHeight() / 3, mGraphics.VCENTER | mGraphics.HCENTER);

                if (GameCanvas.isPointerHoldIn(x_quay + nutQuay[0].getWidth() / 2, y_quay, nutQuay[0].getWidth(), nutQuay[0].getHeight())
                    && GameCanvas.isPointerClick && GameCanvas.isPointerJustRelease)
                {
                    Service.gI().QuayTamBao(1);
                    GameCanvas.clearAllPointerEvent();
                }
                if (GameCanvas.isPointerHoldIn(x_quay - nutQuay[0].getWidth() / 2 + 2 * (nutQuay[0].getWidth() + 20), y_quay, nutQuay[0].getWidth(), nutQuay[0].getHeight())
                    && GameCanvas.isPointerClick && GameCanvas.isPointerJustRelease)
                {
                    Service.gI().QuayTamBao(10);
                    GameCanvas.clearAllPointerEvent();
                }
                //vẽ Item Quay
                int x_item = X + bgSize.getWidth() / 3;
                int y_item = Y + 120;
                for (int i = 0; i < 14; i++)
                {
                    Item currentItem = TamBao_ListItem.ListItemTamBao[i];
                    int x_offset = 0;
                    int y_offset = 0;

                    if (i < 6)
                    {
                        x_offset = i;
                    }
                    else if (i < 8)
                    {
                        x_offset = 5;
                        y_offset = i - 5;
                    }
                    else if (i < 13)
                    {
                        x_offset = 12 - i;
                        y_offset = 2;
                    }
                    else
                    {
                        x_offset = 0;
                        y_offset = 1;
                    }
                    int x_position = x_item + x_offset * (bgItem.getWidth() + 10);
                    int y_position = y_item + y_offset * (bgItem.getHeight() + 10);

                    g.drawImage((Char.myCharz().id_nhan[i] == 0) ? bgItem : khungItemMoc[0], x_position, y_position, 0);
                    SmallImage.drawSmallImage(g, currentItem.template.iconID, x_position + bgItem.getWidth() / 2, y_position + bgItem.getHeight() / 2, 0, mGraphics.VCENTER | mGraphics.HCENTER);
                    if (active_vip[i] == 1)
                    {
                        g.drawImage(imgVip, x_position + bgItem.getWidth() / 2, y_position + bgItem.getHeight(), 3);
                    }
                    //mFont.tahoma_7_yellow.drawString(g, $"{currentItem.quantity}", x_position + 2 + bgItem.getWidth() / 2, y_position + 2 + bgItem.getHeight() / 2, mGraphics.VCENTER | mGraphics.HCENTER, mFont.tahoma_7b_dark); 
                    if (GameCanvas.isPointerHoldIn(x_position, y_position, bgItem.getWidth(), bgItem.getHeight()))
                    {
                        if (GameCanvas.isPointerJustRelease && GameCanvas.isPointerClick)
                        {
                            itemDetails = currentItem;
                            isItemInfo = true;
                            InitScrollInfo();
                            GameCanvas.clearAllPointerEvent();
                        }
                    }
                }
                g.reset();
                //vẽ item mốc quay
                int x_qua = X + 10;
                int y_qua = Y + 80;
                int w_qua = W / 3 - 20;
                int h_qua = H;
                if (Moc_TamBao.Length != 0)
                {
                    for (int num = 0; num < Moc_TamBao.Length; num++)
                    {
                        MocTamBao itemMoc = Moc_TamBao[num];
                        g.setClip(x_qua, y_qua, w_qua, h_qua);
                        g.translate(0, -scrollTab.cmy);
                        if (Char.myCharz().active_moc_quay[num] == 0 && Char.myCharz().diem_quay >= itemMoc.max_count)
                        {
                            g.drawImage(khungItemMoc[0], x_qua, y_qua + (5 + khungItemMoc[0].getHeight()) * num);
                            g.drawImage(nutNhan[0], x_qua + khungItemMoc[0].getWidth() + 5, y_qua + 3 + (5 + khungItemMoc[0].getHeight()) * num);
                            paintOptSlotItem(g, 11, x_qua, y_qua + (5 + khungItemMoc[0].getHeight()) * num, khungItemMoc[0].getWidth(), khungItemMoc[0].getHeight());
                            mFont.tahoma_7b_dark.drawString(g, "Nhận thưởng",
                                x_qua + khungItemMoc[0].getWidth() + 5 + nutNhan[0].getWidth() / 2, y_qua + 3 + (5 + khungItemMoc[0].getHeight()) * num + nutNhan[0].getHeight() / 3, mGraphics.VCENTER | mGraphics.HCENTER);
                        }
                        else if (Char.myCharz().active_moc_quay[num] == 0 && Char.myCharz().diem_quay < itemMoc.max_count)
                        {
                            g.drawImage(khungItemMoc[1], x_qua, y_qua + (5 + khungItemMoc[0].getHeight()) * num);
                            g.drawImage(nutNhan[1], x_qua + khungItemMoc[0].getWidth() + 5, y_qua + 3 + (5 + khungItemMoc[0].getHeight()) * num);
                            paintOptSlotItem(g, 11, x_qua, y_qua + (5 + khungItemMoc[0].getHeight()) * num, khungItemMoc[0].getWidth(), khungItemMoc[0].getHeight());
                            mFont.tahoma_7b_dark.drawString(g, itemMoc.max_count + " Điểm",
                                x_qua + khungItemMoc[0].getWidth() + 5 + nutNhan[0].getWidth() / 2, y_qua + 3 + (5 + khungItemMoc[0].getHeight()) * num + nutNhan[0].getHeight() / 3, mGraphics.VCENTER | mGraphics.HCENTER);
                        }
                        else
                        {
                            g.drawImage(khungItemMoc[1], x_qua, y_qua + (5 + khungItemMoc[0].getHeight()) * num);
                            g.drawImage(imgDanhan, x_qua + khungItemMoc[0].getWidth() + 5, y_qua + 3 + (5 + khungItemMoc[0].getHeight()) * num);
                        }
                        SmallImage.drawSmallImage(g, itemMoc.template.iconID, x_qua + khungItemMoc[0].getWidth() / 2, y_qua + khungItemMoc[0].getHeight() / 2 + (5 + khungItemMoc[0].getHeight()) * num, 0, mGraphics.VCENTER | mGraphics.HCENTER);

                        if (GameCanvas.isPointerHoldIn(x_qua, y_qua + (5 + khungItemMoc[0].getHeight()) * num - scrollTab.cmy, khungItemMoc[0].getWidth(), khungItemMoc[0].getHeight())
                            && GameCanvas.isPointerHoldIn(x_qua, y_qua, w_qua, h_qua))
                        {
                            if (GameCanvas.isPointerJustRelease && GameCanvas.isPointerClick)
                            {
                                itemDetails = itemMoc;
                                isItemInfo = true;
                                InitScrollInfo();
                                GameCanvas.clearAllPointerEvent();
                            }
                        }
                        if (GameCanvas.isPointerHoldIn(x_qua + khungItemMoc[0].getWidth() + 5, y_qua + 3 + (5 + khungItemMoc[0].getHeight()) * num - scrollTab.cmy, nutNhan[0].getWidth(), nutNhan[0].getHeight())
                            && GameCanvas.isPointerClick && GameCanvas.isPointerJustRelease
                            && GameCanvas.isPointerHoldIn(x_qua, y_qua, w_qua, h_qua))
                        {
                            Service.gI().NhanQuaTamBao(Moc_TamBao[num].id_moc);
                            GameCanvas.clearAllPointerEvent();
                        }
                        g.reset();
                    }
                }
                else
                {
                    mFont.tahoma_7b_dark.drawString(g, "Chưa có thông tin", x_qua, y_qua, mGraphics.VCENTER | mGraphics.HCENTER);
                }
                paintItemDetails(g, X + 60, Y + 70, bgSize.getWidth() / 3 + 30, bgSize.getHeight() / 2);
            }
            catch (Exception e)
            {
                Debug.LogError(e.ToString());
            }
        }

        private void paintItemDetails(mGraphics g, int x, int y, int w, int h)
        {
            try
            {
                if (isItemInfo && itemDetails != null)
                {
                    g.setColor(16777215);
                    g.fillRect(x, y, w, h, 10);
                    SmallImage.drawSmallImage(g, itemDetails.template.iconID, x + 3, y + 3, 0, 0);
                    g.drawImage(imgX, x + w - 5 - imgX.getWidth(), y + 3, 0);
                    if (GameCanvas.isPointerHoldIn(x + w - 5 - imgX.getWidth(), y + 3, imgX.getWidth(), imgX.getHeight()))
                    {
                        if (GameCanvas.isPointerJustRelease && GameCanvas.isPointerClick)
                        {
                            isItemInfo = false;
                            itemDetails = null;
                            GameCanvas.clearAllPointerEvent();
                        }
                    }
                    if (isItemInfo && itemDetails != null)
                    {
                        mFont.tahoma_7b_blue.drawString(g, itemDetails.template.name, x + 32, y + 10, 0);
                        g.setColor(UnityEngine.Color.black);
                        g.fillRect(x + 5, y + 35, w - 10, 2, 10);
                        GameCanvas.panel.addItemDetail(itemDetails);
                        var cp = GameCanvas.panel.cp;
                        if (cp.maxStarSlot > 0)
                        {
                            for (int j = 0; j < cp.maxStarSlot; j++)
                            {
                                g.drawImage(Panel.imgMaxStar, x + 10 + j * 10 + Panel.imgMaxStar.getWidth(), y + 23);
                            }
                        }
                        if (cp.starSlot > 0)
                        {
                            for (int k = 0; k < cp.starSlot; k++)
                            {
                                g.drawImage(Panel.imgStar, x + 10 + k * 10 + Panel.imgStar.getWidth(), y + 23);
                            }
                        }
                        g.setClip(x + 5, y + 39, w - 10, h - 50);
                        g.setColor(0);
                        g.fillRect(x + w - 6, y + 40, 4, h - 50, 10);
                        g.setColor(UnityEngine.Color.white);
                        g.fillRect(x + w - 6, y + 41 + scrollInfo.cmy, 2, 20, 10);
                        g.translate(scrollInfo.cmx, -scrollInfo.cmy);
                        if (itemDetails.quantity > 0)
                        {
                            mFont.tahoma_7b_red.drawString(g, $"Số Lượng: {itemDetails.quantity}", x + 6, y + 41, 0);
                        }
                        if (itemDetails.itemOption.Length > 0)
                        {
                            string text = string.Empty;
                            int yPaint = y + 53;
                            for (int i = 0; i < itemDetails.itemOption.Length; i++)
                            {
                                List<string> linesOP = BreakStringIntoLines(itemDetails.itemOption[i].getOptionString(), w - 12, mFont.tahoma_7_green); // 12 là lề trái và phải
                                int lineHeightOP = mFont.tahoma_7_green.getHeight();
                                if (itemDetails.itemOption[i].optionTemplate.id != 107 && itemDetails.itemOption[i].optionTemplate.id != 102)
                                {
                                    for (int f = 0; f < linesOP.Count; f++)
                                    {
                                        mFont.tahoma_7_green.drawString(g, linesOP[f], x + 6, yPaint, 0);
                                        yPaint += 12;
                                    }
                                    //mFont.tahoma_7b_green.drawString(g, itemDetails.itemOption[i].getOptionString(), x + 6, yPaint, 0);
                                    //yPaint += 12;
                                }
                            }
                            mFont.tahoma_7_grey.drawString(g, "-------", x + 6, yPaint, 0);
                            yPaint += 12;
                            List<string> lines = BreakStringIntoLines(itemDetails.template.description, w - 12, mFont.tahoma_7_grey); // 12 là lề trái và phải
                            int lineHeight = mFont.tahoma_7_grey.getHeight();

                            for (int k = 0; k < lines.Count; k++)
                            {
                                mFont.tahoma_7_grey.drawString(g, lines[k], x + 6, yPaint + k * lineHeight, 0);
                            }
                        }
                        g.reset();
                    }
                }
            }
            catch (Exception e)
            {
                Debug.LogError(e.ToString());
            }
        }

        private List<string> BreakStringIntoLines(string input, int maxWidth, mFont font)
        {
            if (string.IsNullOrEmpty(input)) return new List<string>();
            if (maxWidth <= 0 || font == null) throw new ArgumentException("Invalid maxWidth or font");

            List<string> lines = new List<string>();
            StringBuilder currentLine = new StringBuilder();
            string[] words = input.Split(new[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);

            foreach (string word in words)
            {
                // Xử lý các ký tự xuống dòng trong từ
                string[] wordParts = word.Split(new[] { "\r\n", "\n", "\r" }, StringSplitOptions.None);
                for (int i = 0; i < wordParts.Length; i++)
                {
                    string part = wordParts[i];
                    // Tạo chuỗi kiểm tra: nếu currentLine rỗng, chỉ dùng part; nếu không, thêm dấu cách
                    string testLine = currentLine.Length == 0 ? part : currentLine.ToString() + " " + part;

                    // Đo chiều rộng văn bản
                    float textWidth = font.getWidth(testLine);

                    // Kiểm tra xem thêm phần từ có vượt maxWidth không
                    if (textWidth <= maxWidth)
                    {
                        // Thêm dấu cách trước khi thêm phần mới (trừ khi currentLine rỗng)
                        if (currentLine.Length > 0 && i == 0) currentLine.Append(' ');
                        currentLine.Append(part);
                    }
                    else
                    {
                        // Lưu dòng hiện tại (nếu có) và bắt đầu dòng mới
                        if (currentLine.Length > 0)
                        {
                            lines.Add(currentLine.ToString().TrimEnd());
                            currentLine.Clear();
                        }
                        currentLine.Append(part);
                    }

                    // Nếu không phải phần cuối của từ chứa xuống dòng, lưu dòng hiện tại và đặt lại
                    if (i < wordParts.Length - 1)
                    {
                        if (currentLine.Length > 0)
                        {
                            lines.Add(currentLine.ToString().TrimEnd());
                            currentLine.Clear();
                        }
                    }
                }
            }

            // Thêm dòng cuối nếu còn
            if (currentLine.Length > 0)
            {
                lines.Add(currentLine.ToString().TrimEnd());
            }

            return lines;
        }

        public void paintOptSlotItem(mGraphics g, int param, int x, int y, int w, int h)
        {
            sbyte color_Item_Upgrade = GetColor_Item_Upgrade(param);
            int nline = param;
            Panel.paintUpgradeEffect(x, y, w, h, nline, color_Item_Upgrade, g);
        }
        public static int GetItemQuantity(int templateId)
        {
            int totalQuantity = 0;

            for (int i = 0; i < Char.myCharz().arrItemBag.Length; i++)
            {
                Item item = Char.myCharz().arrItemBag[i];
                if (item != null && (int)item.template.id == templateId)
                {
                    totalQuantity += item.quantity;
                }
            }

            return totalQuantity;
        }

        public static sbyte GetColor_Item_Upgrade(int lv)
        {
            if (lv < 0)
            {
                return 0;
            }
            switch (lv)
            {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    return 4;
                case 9:
                    return 4;
                case 10:
                    return 1;
                case 11:
                    return 5;
                case 12:
                    return 3;
                case 13:
                    return 2;
                default:
                    return 6;
            }
        }
        public void perform(int idAction, object p)
        {
            switch (idAction)
            {
                case 1000:
                    GameCanvas.panel.setTypeMain();
                    GameCanvas.panel.show();
                    break;
                case 999:
                    Service.gI().loadMocTamBao();
                    isShow = true;
                    InitScrollTab();
                    break;
            }
        }
    }
}