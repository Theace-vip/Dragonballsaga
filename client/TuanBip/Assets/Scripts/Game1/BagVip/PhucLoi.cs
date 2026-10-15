using Game1.Assets.src.e;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Text;
using UnityEngine;
using UnityEngine.UIElements;

namespace Game1.BagVip
{
    public class PhucLoi : IActionListener
    {
        private Image bgSize;
        private Image btnItem;
        private Image[] indexBag = new Image[2];
        private Image[] nutnhan = new Image[2];
        private Image imgX;
        private Image imgChamhoi;
        private Image imgLogoRuong;
        private Image imgDaNhan;
        private Image imgKhungItem;

        private static PhucLoi instance;
        public bool isShow = false;
        private Scroll scrollBag = new Scroll(), scrollInfo = new Scroll();
        private Scroll scrollList = new Scroll(), scrollTab = new Scroll();
        private int X, Y, W, H;
        private Cell[] cellsBag;
        private Item itemDetails;
        private bool isShowInfo, isItemInfo;
        private int numCol2;
        private int Length = 0;

        public int currIndex = 0;
        public static int START;
        public static PhucLoi gI()
        {
            if (instance == null)
            {
                instance = new PhucLoi();
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
                g.drawImage(PhucLoi.gI().btnItem, x, y);
            }
        }
        public PhucLoi()
        {
            if (bgSize == null)
            {
                bgSize = GameCanvas.loadImage("/phucloi/backbg.png");
            }
            X = GameCanvas.w / 2 - bgSize.getWidth() / 2;
            Y = GameCanvas.h / 2 - bgSize.getHeight() / 2;
            W = bgSize.getWidth() - 10;
            H = bgSize.getHeight() - 20;
            //X = 30;
            //Y = 30;
            //W = GameCanvas.w - 60;
            //H = GameCanvas.h - 60;
            btnItem = GameCanvas.loadImage("/phucloi/btnItem.png");
            imgKhungItem = GameCanvas.loadImage("/phucloi/khungitem.png");
            for (int i = 0; i < indexBag.Length; i++)
            {
                if (indexBag[i] == null)
                {
                    indexBag[i] = GameCanvas.loadImage($"/phucloi/index{i}.png");
                }
            }
            Service.gI().loadPhucLoi();
            InitScroll();
            InitScrollTab();
            InitScrollList();
            InitScrollInfo();
            imgX = GameCanvas.loadImage("/phucloi/iconx.png");
            imgChamhoi = GameCanvas.loadImage("/phucloi/chamhoi.png");
            imgLogoRuong = GameCanvas.loadImage("/phucloi/logoruong.png");
            imgDaNhan = GameCanvas.loadImage("/phucloi/danhan.png");
            for (int i = 0; i < nutnhan.Length; i++)
            {
                if (nutnhan[i] == null)
                {
                    nutnhan[i] = GameCanvas.loadImage($"/phucloi/nutnhan{i}.png");
                }
            }
        }
        public void update()
        {
            if (!isShow) return;

            updateScroll(scrollBag);
            if (isItemInfo == false)
            {
                updateScroll(scrollTab);
                updateScroll(scrollList);
            }
            else
            {
                updateScroll(scrollInfo);
            }
        }
        public void updateScroll(Scroll sc)
        {
            if (sc.cmyLim > 0 && !GameCanvas.isPointerClick)
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
            else if (sc.cmxLim > 0 && !GameCanvas.isPointerClick)
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

        private void InitScroll()
        {
            try
            {
                int x1 = X + 1, y1 = Y, w1 = W - 2, h1 = H - 2;
                int x2 = x1 + 2, y2 = y1 + 2, w2 = w1 - 4, h2 = h1 - 1;
                int x3 = x2 + 2, y3 = y2, w3 = w2 - 4, h3 = h2 - 4;
                int x4 = x3 + 3, y4 = y3 + 2, w4 = w3 - 4, h4 = h3 - 4;
                int x5 = x4 + 4, y5 = y4 + 25, w5 = w4 - 8, h5 = h4 - 30;
                int x6 = x5 + 1, y6 = y5 + 1, w6 = w5 - 2, h6 = h5 - 2;
                int x7 = x6 + 4, y7 = y6 + 8, w7 = w6 / 2 + 2, h7 = h6 - 16;
                int x10 = x7 + w7 + 10;
                int y10 = y7 + 25;
                int w10 = w7 - 16 - btnItem.getWidth() * 2;
                int h10 = h7 - 20;
                Length = 0;
                var arrAll = PhucLoiList.arrPhucLoi;
                if (arrAll != null && currIndex >= 0 && currIndex < arrAll.Length && arrAll[currIndex] != null
                    && arrAll[currIndex].arrPhucLoiTemplates != null && arrAll[currIndex].arrPhucLoiTemplates.Length != 0)
                {
                    var arrTpl = arrAll[currIndex].arrPhucLoiTemplates;
                    for (int i = 0; i < arrTpl.Length; i++)
                    {
                        var tpl = arrTpl[i];
                        if (tpl == null || tpl.arrItemPhucLoi == null) continue;
                        if (tpl.arrItemPhucLoi.Length > Length)
                        {
                            Length = tpl.arrItemPhucLoi.Length;
                        }
                    }
                    numCol2 = UnityEngine.Mathf.Max(3, UnityEngine.Mathf.CeilToInt(Length));
                    scrollBag = new Scroll();
                    scrollBag.setStyle(numCol2, btnItem.getWidth() + 5, x10, y10 - btnItem.getWidth() / 2, w10, h10, false, Length);
                }
                GameCanvas.clearAllPointerEvent();
            }
            catch (Exception e)
            {
                Debug.LogError(e.ToString());
            }
        }
        private void InitScrollInfo()
        {
            try
            {
                int x1 = X + 1, y1 = Y, w1 = W - 2, h1 = H - 2;
                int x2 = x1 + 2, y2 = y1 + 2, w2 = w1 - 4, h2 = h1 - 1;
                int x3 = x2 + 2, y3 = y2, w3 = w2 - 4, h3 = h2 - 4;
                int x4 = x3 + 3, y4 = y3 + 2, w4 = w3 - 4, h4 = h3 - 4;
                int x5 = x4 + 4, y5 = y4 + 25, w5 = w4 - 8, h5 = h4 - 30;
                int x6 = x5 + 1, y6 = y5 + 1, w6 = w5 - 2, h6 = h5 - 2;
                int x7 = x6 + 4, y7 = y6 + 8, w7 = w6 / 2 + 2, h7 = h6 - 16;
                int x10 = x7 + w7 + 5;
                int y10 = y7 + 20;
                int w10 = w7 - 16;
                int h10 = h7 - 20;
                GameCanvas.clearAllPointerEvent();
                scrollInfo = new Scroll();
                scrollInfo.clear();
                if (itemDetails != null && itemDetails.itemOption != null)
                {
                    List<string> lines = BreakStringIntoLines(itemDetails.template.description, w7 - 12, mFont.tahoma_7_grey);

                    scrollInfo.setStyle(itemDetails.itemOption.Length, mFont.tahoma_7_green.getHeight() + itemDetails.itemOption.Length + lines.Count
                        , x7 + 5, y7 + 40, (w7 - 10) / 2, h7 - 80, true, 1);
                }
            }
            catch (Exception e)
            {
                Debug.LogError(e.ToString());
            }
        }
        private void InitScrollList()
        {
            try
            {
                int x1 = X + 1, y1 = Y, w1 = W - 2, h1 = H - 2;
                int x2 = x1 + 2, y2 = y1 + 2, w2 = w1 - 4, h2 = h1 - 1;
                int x3 = x2 + 2, y3 = y2, w3 = w2 - 4, h3 = h2 - 4;
                int x4 = x3 + 3, y4 = y3 + 2, w4 = w3 - 4, h4 = h3 - 4;
                int x5 = x4 + 4, y5 = y4 + 25, w5 = w4 - 8, h5 = h4 - 30;
                int x6 = x5 + 1, y6 = y5 + 1, w6 = w5 - 2, h6 = h5 - 2;
                int x7 = x6 + 4, y7 = y6 + 8, w7 = w6 / 2 + 2, h7 = h6 - 16;
                int x10 = x7 + w7 + 10;
                int y10 = y7 + 25;
                int w11 = indexBag[0].getWidth() + 2;

                int xList = X + 23 + w11 + 10;                                              // x_khungitem
                int yList = Y + 60;                                                          // y_khungitem
                int wList = imgKhungItem.getWidth();                                        // w_khungitem
                int hList = bgSize.getHeight() - yList + Y - imgKhungItem.getHeight() / 2;   // h_khungitem
                var arrList = PhucLoiList.arrPhucLoi;
                if (arrList != null && arrList.Length != 0 && currIndex >= 0 && currIndex < arrList.Length
                    && arrList[currIndex] != null && arrList[currIndex].arrPhucLoiTemplates != null)
                {
                    if (arrList[currIndex].arrPhucLoiTemplates.Length != 0)
                    {
                        scrollList = new Scroll();
                        // vùng bắt chuột để kéo phải trùng khung danh sách đang vẽ, lệch là kéo không ăn
                        scrollList.setStyle(PhucLoiList.arrPhucLoi[currIndex].arrPhucLoiTemplates.Length, imgKhungItem.getHeight() + 5
                        , xList, yList, wList, hList, true, 1);
                        GameCanvas.clearAllPointerEvent();
                    }
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
                int x1 = X + 1, y1 = Y, w1 = W - 2, h1 = H - 2;
                int x2 = x1 + 2, y2 = y1 + 2, w2 = w1 - 4, h2 = h1 - 1;
                int x3 = x2 + 2, y3 = y2, w3 = w2 - 4, h3 = h2 - 4;
                int x4 = x3 + 3, y4 = y3 + 2, w4 = w3 - 4, h4 = h3 - 4;
                int x5 = x4 + 4, y5 = y4 + 25, w5 = w4 - 8, h5 = h4 - 30;
                int x6 = x5 + 1, y6 = y5 + 1, w6 = w5 - 2, h6 = h5 - 2;
                int x7 = x6 + 4, y7 = y6 + 8, w7 = w6 / 2 + 2, h7 = h6 - 16;
                var arrTab = PhucLoiList.arrPhucLoi;
                if (arrTab != null && arrTab.Length != 0)
                {
                    scrollTab = new Scroll();
                    scrollTab.setStyle(PhucLoiList.arrPhucLoi.Length, (indexBag[0].getHeight() + 5) * PhucLoiList.arrPhucLoi.Length
                    , x7, y7 - indexBag[0].getHeight() / 2, indexBag[0].getWidth(), (indexBag[0].getHeight() + 3) * 8, true, PhucLoiList.arrPhucLoi.Length);
                }
                GameCanvas.clearAllPointerEvent();
            }
            catch (Exception e)
            {
                Debug.LogError(e.ToString());
            }
        }
        public void Paint(mGraphics g)
        {
            try
            {
                if (!isShow) return;

                int x_bg = X;
                int y_bg = Y;
                g.drawImage(bgSize, x_bg, y_bg, 0);

                int x_close = x_bg + bgSize.getWidth() - imgX.getWidth();
                int y_close = y_bg + imgX.getWidth() / 2;

                g.drawImage(imgX, x_close, y_close, 0);
                if (GameCanvas.isPointerHoldIn(x_close, y_close, imgX.getWidth(), imgX.getHeight()))
                {
                    if (GameCanvas.isPointerJustRelease && GameCanvas.isPointerClick)
                    {
                        itemDetails = null;
                        isItemInfo = false;
                        isShow = false;
                        GameCanvas.clearAllPointerEvent();
                    }
                }

                int x_tab = X + 23;
                int y_tab = Y + 60;
                int w_tab = indexBag[0].getWidth() + 2;
                int h_tab = bgSize.getHeight() - y_tab + Y - imgKhungItem.getHeight() / 2;
                g.reset();
                if (PhucLoiList.arrPhucLoi.Length != 0)
                {
                    int numk = 0;
                    for (int num = 0; num < PhucLoiList.arrPhucLoi.Length; num++)
                    {
                        if (PhucLoiList.arrPhucLoi[num].action >= 1)
                        {
                            g.setClip(x_tab, y_tab, w_tab, h_tab);
                            g.translate(0, -scrollTab.cmy);
                            g.drawImage((currIndex == num) ? indexBag[1] : indexBag[0], x_tab, y_tab + (indexBag[0].getHeight() + 2) * numk);

                            mFont.tahoma_7b_dark.drawString(g, PhucLoiList.arrPhucLoi[num].TabName
                                , x_tab + indexBag[0].getWidth() / 2, y_tab + 5 + (indexBag[0].getHeight() + 2) * numk
                                , mGraphics.VCENTER | mGraphics.HCENTER);

                            if (GameCanvas.isPointerHoldIn(x_tab, y_tab + (indexBag[0].getHeight() + 2) * numk - scrollTab.cmy, indexBag[0].getWidth(), indexBag[0].getHeight())
                                && GameCanvas.isPointerHoldIn(x_tab, y_tab, w_tab, h_tab))
                            {
                                if (GameCanvas.isPointerJustRelease && GameCanvas.isPointerClick)
                                {
                                    currIndex = num;
                                    InitScroll();
                                    InitScrollList();
                                    InitScrollInfo();
                                    GameCanvas.clearAllPointerEvent();
                                }
                            }
                            numk++;
                        }
                        g.reset();
                    }
                }
                else
                {
                    mFont.tahoma_7b_dark.drawString(g, "Chưa có thông tin", x_tab + indexBag[0].getHeight() / 2, y_tab + 10, mGraphics.VCENTER | mGraphics.HCENTER);
                }
                g.reset();
                #region ITEM_PHUC_LOI
                int x_tichluy = x_tab + w_tab + 10 + imgKhungItem.getWidth() / 2;
                int y_tichluy = y_tab - mFont.tahoma_7b_dark.getHeight() - 7;
                if (PhucLoiList.arrPhucLoi[currIndex].action == 2)
                {
                    mFont.tahoma_7b_red.drawString(g, "Chăm chỉ Online hằng ngày để nhận quà nào!!!"
                    , x_tichluy, y_tichluy, mGraphics.VCENTER | mGraphics.HCENTER);
                }
                else
                {
                    mFont.tahoma_7b_red.drawString(g, "Tích lũy: " + NinjaUtil.getMoneys(Char.myCharz().count_phucloi[currIndex]) + " " + PhucLoiList.arrPhucLoi[currIndex].tichLuy
                    , x_tichluy, y_tichluy, mGraphics.VCENTER | mGraphics.HCENTER);
                }

                int x_khungitem = x_tab + w_tab + 10;
                int y_khungitem = y_tab;
                int w_khungitem = imgKhungItem.getWidth();
                int h_khungitem = bgSize.getHeight() - y_tab + Y - imgKhungItem.getHeight() / 2;

                if (PhucLoiList.arrPhucLoi.Length != 0)
                {
                    if (PhucLoiList.arrPhucLoi[currIndex].arrPhucLoiTemplates.Length != 0 && PhucLoiList.arrPhucLoi[currIndex].action >= 1)
                    {
                        for (int t = 0; t < PhucLoiList.arrPhucLoi[currIndex].arrPhucLoiTemplates.Length; t++)
                        {
                            g.setClip(x_khungitem, y_khungitem, w_khungitem, h_khungitem);
                            g.translate(0, -scrollList.cmy);
                            g.drawImage(imgKhungItem, x_khungitem, y_khungitem + (imgKhungItem.getHeight() + 5) * t);
                            g.reset();
                        }

                        int x_item = x_tab + w_tab + 15;
                        int y_item = y_tab + 15;
                        int w_item = imgKhungItem.getWidth() - imgDaNhan.getWidth() * 2 + btnItem.getWidth();
                        int h_item = imgKhungItem.getHeight();
                        for (int t = 0; t < PhucLoiList.arrPhucLoi[currIndex].arrPhucLoiTemplates.Length; t++)
                        {

                            numCol2 = PhucLoiList.arrPhucLoi[currIndex].arrPhucLoiTemplates[t].arrItemPhucLoi.Length;
                            g.setClip(x_khungitem, y_khungitem, w_item, h_khungitem);
                            cellsBag = new Cell[numCol2];
                            g.translate(0, -scrollList.cmy);
                            g.translate(-scrollBag.cmx, -scrollBag.cmy);

                            for (int col = 0; col < numCol2; col++)
                            {
                                int x = x_item + (col * (btnItem.getWidth() + 5));
                                int y = y_item + (imgKhungItem.getHeight() + 5) * (t);
                                int index = col;
                                if (cellsBag[index] == null || cellsBag.Length != numCol2)
                                {
                                    cellsBag[index] = new Cell(x, y);
                                }
                                cellsBag[index].paint(g);
                                Item[] items = PhucLoiList.arrPhucLoi[currIndex].arrPhucLoiTemplates[t].arrItemPhucLoi;
                                if (items[index] != null)
                                {
                                    SmallImage.drawSmallImage(g, items[index].template.iconID, x + btnItem.getWidth() / 2, y + btnItem.getHeight() / 2, 0, mGraphics.VCENTER | mGraphics.HCENTER);
                                    mFont.tahoma_7_yellow.drawString(g, $"{items[index].quantity}", x + btnItem.getWidth() / 2 + 2, y + btnItem.getHeight() / 2 + 2, mGraphics.VCENTER | mGraphics.HCENTER, mFont.tahoma_7b_dark);
                                    paintOptSlotItem(g, items[index].template.type, 6, x, y, btnItem.getWidth(), btnItem.getHeight());
                                    if (GameCanvas.isPointerHoldIn(cellsBag[index].x - scrollBag.cmx, cellsBag[index].y - scrollList.cmy, btnItem.getWidth(), btnItem.getHeight())
                                        && GameCanvas.isPointerHoldIn(x_khungitem, y_khungitem, w_item, h_khungitem))
                                    {
                                        if (GameCanvas.isPointerJustRelease && GameCanvas.isPointerClick)
                                        {
                                            itemDetails = items[index];
                                            isItemInfo = true;
                                            InitScrollInfo();
                                            GameCanvas.clearAllPointerEvent();
                                        }

                                    }
                                }
                            }
                            g.reset();
                        }
                        g.reset();

                        int w_nut = imgDaNhan.getWidth() * 2;
                        int h_nut = h_khungitem;
                        int x_nut = x_khungitem + imgKhungItem.getWidth() - imgDaNhan.getWidth() - 3;
                        int y_nut = y_tab;
                        for (int t = 0; t < PhucLoiList.arrPhucLoi[currIndex].arrPhucLoiTemplates.Length; t++)
                        {
                            g.setClip(x_khungitem, y_nut, w_khungitem, h_nut);
                            g.translate(0, -scrollList.cmy);
                            mFont.tahoma_7b_white.drawString(g, PhucLoiList.arrPhucLoi[currIndex].arrPhucLoiTemplates[t].info
                                , x_item, y_item - mFont.tahoma_7b_white.getHeight() - 3 + (imgKhungItem.getHeight() + 5) * t, 0);
                            if (PhucLoiList.arrPhucLoi[currIndex].arrPhucLoiTemplates[t].active == 0 && PhucLoiList.arrPhucLoi[currIndex].action != 2)
                            {
                                if (Char.myCharz().active_phucloi[PhucLoiList.arrPhucLoi[currIndex].arrPhucLoiTemplates[t].Id] == 0)
                                {
                                    g.drawImage((Char.myCharz().count_phucloi[currIndex] >= PhucLoiList.arrPhucLoi[currIndex].arrPhucLoiTemplates[t].max_count) ? nutnhan[0] : nutnhan[1]
                                        , x_nut, y_nut + 15 + (imgKhungItem.getHeight() + 5) * t);
                                    mFont.tahoma_7b_dark.drawString(g, "Nhận", x_nut + nutnhan[0].getWidth() / 2, y_nut + 15 + nutnhan[0].getHeight() / 3 + (imgKhungItem.getHeight() + 5) * t, mGraphics.VCENTER | mGraphics.HCENTER);
                                }
                                else
                                {
                                    //vẽ nút đã nhận
                                    g.drawImage(imgDaNhan, x_nut, y_nut + 15 + (imgKhungItem.getHeight() + 5) * t);
                                }
                            }
                            else if (PhucLoiList.arrPhucLoi[currIndex].arrPhucLoiTemplates[t].active == 0 && PhucLoiList.arrPhucLoi[currIndex].action == 2)
                            {
                                if (Char.myCharz().active_phucloi[PhucLoiList.arrPhucLoi[currIndex].arrPhucLoiTemplates[t].Id] == 0)
                                {
                                    g.drawImage((Char.myCharz().count_phucloi[currIndex] == PhucLoiList.arrPhucLoi[currIndex].arrPhucLoiTemplates[t].max_count) ? nutnhan[0] : nutnhan[1]
                                        , x_nut, y_nut + 15 + (imgKhungItem.getHeight() + 5) * t);
                                    mFont.tahoma_7b_dark.drawString(g, "Nhận", x_nut + nutnhan[0].getWidth() / 2, y_nut + 15 + nutnhan[0].getHeight() / 3 + (imgKhungItem.getHeight() + 5) * t, mGraphics.VCENTER | mGraphics.HCENTER);
                                }
                                else
                                {
                                    //vẽ nút đã nhận
                                    g.drawImage(imgDaNhan, x_nut, y_nut + 15 + (imgKhungItem.getHeight() + 5) * t);
                                }
                            }
                            else
                            {
                                g.drawImage((Char.myCharz().count_phucloi[currIndex] >= PhucLoiList.arrPhucLoi[currIndex].arrPhucLoiTemplates[t].max_count) ? nutnhan[0] : nutnhan[1]
                                    , x_nut, y_nut + 15 + (imgKhungItem.getHeight() + 5) * t);
                                mFont.tahoma_7b_dark.drawString(g, "Mua", x_nut + nutnhan[0].getWidth() / 2, y_nut + 15 + nutnhan[0].getHeight() / 3 + (imgKhungItem.getHeight() + 5) * t, mGraphics.VCENTER | mGraphics.HCENTER);
                            }

                            if (GameCanvas.isPointerHoldIn(x_nut, y_nut + 15 + (imgKhungItem.getHeight() + 5) * t - scrollList.cmy, nutnhan[0].getWidth(), nutnhan[0].getHeight())
                                && GameCanvas.isPointerClick && GameCanvas.isPointerJustRelease
                                && GameCanvas.isPointerHoldIn(x_nut, y_nut, w_nut, h_nut))
                            {
                                Service.gI().ActivePhucLoi(PhucLoiList.arrPhucLoi[currIndex].arrPhucLoiTemplates[t].Id);
                                GameCanvas.clearAllPointerEvent();
                            }
                            g.reset();
                        }
                        g.translate(scrollBag.cmx, 0);
                        g.reset();
                    }
                    else
                    {
                        mFont.tahoma_7b_dark.drawString(g, "Chưa có thông tin", x_khungitem + imgKhungItem.getWidth() / 2, y_khungitem + 20
                            , mGraphics.VCENTER | mGraphics.HCENTER);
                    }
                }
                else
                {
                    mFont.tahoma_7b_dark.drawString(g, "Chưa có thông tin", x_khungitem + imgKhungItem.getWidth() / 2, y_khungitem + 20
                        , mGraphics.VCENTER | mGraphics.HCENTER);
                }
                //vẽ dấu chấm hỏi
                g.drawImage(imgChamhoi, x_tab + indexBag[0].getWidth() / 2 - imgChamhoi.getWidth() / 2, y_tab - imgChamhoi.getHeight() - 5, 0);
                if (GameCanvas.isPointerHoldIn(x_tab + indexBag[0].getWidth() / 2 - imgChamhoi.getWidth() / 2, y_tab - imgChamhoi.getHeight() - 5, imgChamhoi.getWidth(), imgChamhoi.getHeight()))
                {
                    if (GameCanvas.isPointerJustRelease && GameCanvas.isPointerClick)
                    {
                        isItemInfo = false;
                        isShowInfo = true;
                        GameCanvas.clearAllPointerEvent();
                    }
                }
                paintShowInfo(g, x_tab + indexBag[0].getWidth() / 2 - imgChamhoi.getWidth() / 2, y_tab - imgChamhoi.getHeight() - 25
                    , bgSize.getWidth() / 2, bgSize.getHeight() - 30);
                paintItemDetails(g, x_tab + indexBag[0].getWidth() / 2 - imgChamhoi.getWidth() / 2, y_tab - imgChamhoi.getHeight() - 25
                    , bgSize.getWidth() / 2, bgSize.getHeight() - 30);
                #endregion
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
                if (isItemInfo)
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
                            InitScrollTab();
                            GameCanvas.clearAllPointerEvent();
                        }
                    }
                    if (isItemInfo)
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
                        g.setClip(x + 5, y + 39, w - 10, h - 80);
                        g.setColor(0);
                        g.fillRect(x + w - 6, y + 40, 4, h - 80, 10);
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
                                //if (itemDetails.itemOption[i].optionTemplate.id != 107 && itemDetails.itemOption[i].optionTemplate.id != 102)
                                //{
                                //    mFont.tahoma_7b_green.drawString(g, itemDetails.itemOption[i].getOptionString(), x + 6, yPaint, 0);
                                //    yPaint += 12;
                                //}
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

        private void paintShowInfo(mGraphics g, int x, int y, int w, int h)
        {
            try
            {
                if (isShowInfo)
                {
                    g.fillRect(x, y, w, h, 10);
                    g.setColor(2233091, 1f);

                    g.setColor(UnityEngine.Color.white);
                    g.fillRect(x + 1, y + 1, w - 2, h - 2, 10);
                    g.drawImage(imgX, x + w - 5 - imgX.getWidth(), y + 3, 0);
                    g.drawImage(imgLogoRuong, x + w / 2 - imgLogoRuong.getWidth() / 2, y + 3, 0);
                    if (GameCanvas.isPointerHoldIn(x + w - 5 - imgX.getWidth(), y + 3, imgX.getWidth(), imgX.getHeight()))
                    {
                        if (GameCanvas.isPointerJustRelease && GameCanvas.isPointerClick)
                        {
                            isShowInfo = false;
                            InitScrollTab();
                            GameCanvas.clearAllPointerEvent();
                        }
                    }
                    g.setClip(x, y, w, h);
                    if (PhucLoiList.arrPhucLoi.Length != 0)
                    {
                        if (PhucLoiList.arrPhucLoi[currIndex].infoTab.Length > 0)
                        {
                            List<string> lines = BreakStringIntoLines(PhucLoiList.arrPhucLoi[currIndex].infoTab, w - 12, mFont.tahoma_7_grey); // 12 là lề trái và phải
                            int lineHeight = mFont.tahoma_7_grey.getHeight();

                            for (int i = 0; i < lines.Count; i++)
                            {
                                mFont.tahoma_7_grey.drawString(g, lines[i], x + 6, y + 10 + imgLogoRuong.getHeight() + i * lineHeight, 0);
                            }
                        }
                        g.reset();
                    }
                    else
                    {
                        mFont.tahoma_7b_dark.drawString(g, "Chưa có thông tin", x + w / 2, y + h / 2, mGraphics.VCENTER | mGraphics.HCENTER);
                    }

                }
            }
            catch (Exception e)
            {
                Debug.LogError(e.ToString());
            }
        }
        public void paintOptSlotItem(mGraphics g, int idOpt, int param, int x, int y, int w, int h)
        {
            if (idOpt == 11 || idOpt == 5 || idOpt == 23 || idOpt == 24 || idOpt == 75 || idOpt == 72 || idOpt == 21)
            {
                sbyte color_Item_Upgrade = GetColor_Item_Upgrade(param);
                int nline = param;
                Panel.paintUpgradeEffect(x, y, w, h, nline, color_Item_Upgrade, g);
            }
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
                    Service.gI().loadPhucLoi();
                    for (int num = 0; num < PhucLoiList.arrPhucLoi.Length; num++)
                    {
                        if (PhucLoiList.arrPhucLoi[num].action >= 1)
                        {
                            currIndex = num;
                            break;
                        }
                    }
                    isShow = true;
                    break;
            }
        }
    }
}