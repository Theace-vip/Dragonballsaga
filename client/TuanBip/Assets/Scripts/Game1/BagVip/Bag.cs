using Game1.Assets.src.e;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Text;
using UnityEngine;
using UnityEngine.UIElements;

namespace Game1.BagVip
{
    public class Bag : IActionListener
    {
        private enum TypePaint
        {
            typeBody,
            typeBag,
            typeBox
        }
        private static Bag instance;
        public bool isShow = false;
        private TypePaint typePaint;
        private Image bgSize;
        private Image btnItem;
        private Image btnItem2;
        private Image[] imgBox = new Image[5];
        private Image[] indexBag = new Image[2];
        private int currIndex = 0, currTab = 0;
        private Scroll scrollBag, scrollInfo;
        private int numCol = 5, numRow = 5;
        private int X, Y, W, H;
        private Cell[] cellsBag;
        private Item itemDetails;
        private bool isBody = true, isItemInfo;
        private int selected;
        private Tab[] tabs;
        private Image imgX;
        private Image[] imgTab = new Image[2];
        private int numCol2, numRow2;
        public static Bag gI()
        {
            if (instance == null)
            {
                instance = new Bag();
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
                g.drawImage(Bag.gI().btnItem2, x, y);
            }
        }
        public Bag()
        {
            if (bgSize == null)
            {
                bgSize = GameCanvas.loadImage("/bag/bgSize.png");
            }
            X = GameCanvas.w / 2 - bgSize.getWidth() / 2;
            Y = GameCanvas.h / 2 - bgSize.getHeight() / 2;
            W = bgSize.getWidth();
            H = bgSize.getHeight();
            for (int i = 0; i < imgBox.Length; i++)
            {
                if (imgBox[i] == null)
                {
                    imgBox[i] = GameCanvas.loadImage("/bag/textbox.png");
                }
            }
            btnItem = GameCanvas.loadImage("/bag/btnItem.png");
            btnItem2 = GameCanvas.loadImage("/bag/btnItem2.png");
            for (int i = 0; i < indexBag.Length; i++)
            {
                if (indexBag[i] == null)
                {
                    indexBag[i] = GameCanvas.loadImage($"/bag/index{i}.png");
                }
            }
            InitScroll();
            tabs = new Tab[]
            {
                new Tab("Lấy Ra", this, 1 , null),
                new Tab("Dùng", this, 2, null),
                new Tab("Vứt Ra", this, 3, null),
                new Tab("Cất Vào", this, 4, null),
                new Tab("Cho Đệ", this, 5, null)
            };
            imgX = GameCanvas.loadImage("/mainImage/myTexture2dbtx.png");
            for (int i = 0; i < imgTab.Length; i++)
            {
                if (imgTab[i] == null)
                {
                    imgTab[i] = GameCanvas.loadImage($"/bag/tab{i}.png");
                }
            }
        }
        public void update()
        {
            if (!isShow) return;

            updateScroll(scrollBag);
            updateScroll(scrollInfo);
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
        }
        //public void updateKey()
        //{
        //    if (!isShow) return;
        //    if (scrollBag != null)
        //    {
        //        scrollBag.updateKey();
        //    }
        //    if (scrollInfo != null)
        //    {
        //        scrollInfo.updateKey();
        //    }
        //}

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
            Item[] items = currIndex == 0 ? Char.myCharz().arrItemBag : Char.myCharz().arrItemBox;
            numCol2 = 5;
            numRow2 = UnityEngine.Mathf.Max(5, UnityEngine.Mathf.CeilToInt(items.Length / 5f));
            scrollBag = new Scroll();
            scrollBag.setStyle(numCol2 * numRow2, btnItem2.getWidth() + 5, x10, y10 + btnItem2.getHeight(), (btnItem2.getWidth() + 5) * 5, h10, true, 5);
        }
        private void InitScrollInfo()
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
            List<string> lines = BreakStringIntoLines(itemDetails.template.description, w7 - 12, mFont.tahoma_7_grey);
            if (itemDetails != null && itemDetails.itemOption != null)
                scrollInfo.setStyle(itemDetails.itemOption.Length, mFont.tahoma_7b_green.getHeight() + itemDetails.itemOption.Length + lines.Count
                    , x7 + 5, y7 + 40, w7 - 10, h7 - 80, true, 1);

        }
        public void Paint(mGraphics g)
        {
            if (!isShow) return;
            g.setColor(15839327);
            // viền ngoài 
            g.fillRect(X, Y, W, H, 10);
            int x1 = X + 1, y1 = Y, w1 = W - 2, h1 = H - 2;
            g.setColor(2233091, 1f);
            // viền ngoài 
            g.fillRect(x1, y1, w1, h1, 10);
            int x2 = x1 + 2, y2 = y1 + 2, w2 = w1 - 4, h2 = h1 - 1;
            g.setColor(3678729);
            // viền ngoài 
            g.fillRect(x2, y2, w2, h2, 8);
            int x3 = x2 + 2, y3 = y2, w3 = w2 - 4, h3 = h2 - 4;
            g.setColor(7228184, 2f);
            // viền ngoài 
            g.fillRect(x3, y3, w3, h3, 4);
            int x4 = x3 + 3, y4 = y3 + 2, w4 = w3 - 4, h4 = h3 - 4;
            g.setColor(11562007, 2f);
            // viền ngoài 
            g.fillRect(x4, y4, w4, h4, 4);
            //mFont.bigNumber_red.drawString(g, "HÀNH TRANG", GameCanvas.w / 2, y4, mFont.LEFT, mFont.tahoma_7);
            g.drawImage(imgX, GameCanvas.w - x4 - 10 - imgX.getWidth(), y4, 0);
            if (GameCanvas.isPointerHoldIn(GameCanvas.w - x4 - 10 - imgX.getWidth(), y4, imgX.getWidth(), imgX.getHeight()))
            {
                if (GameCanvas.isPointerJustRelease && GameCanvas.isPointerClick)
                {
                    isItemInfo = false;
                    isBody = true;
                    isShow = false;
                    GameCanvas.clearAllPointerEvent();
                }
            }
            g.drawImage((currTab == 0) ? imgTab[1] : imgTab[0], x4 + imgTab[0].getWidth() / 2 + 5, y4 + imgTab[0].getHeight() / 2 + 5, mGraphics.VCENTER | mGraphics.HCENTER);
            mFont.tahoma_7_grey.drawString(g, "Bản Thân", x4 + imgTab[0].getWidth() / 2 + 5, y4 + imgTab[0].getHeight() / 2, mGraphics.VCENTER | mGraphics.HCENTER);
            //vẽ vàng
            g.drawImage(Panel.imgXu, GameCanvas.w / 2 - 60, y4 + imgTab[0].getHeight() / 2 + 5, 3);
            mFont.tahoma_7_yellow.drawString(g, Char.myCharz().xuStr + string.Empty, GameCanvas.w / 2 - 50, y4 + imgTab[0].getHeight() / 2 - 2, mFont.LEFT, mFont.tahoma_7_grey);
            //vẽ ngọc xanh
            g.drawImage(Panel.imgLuong, GameCanvas.w / 2, y4 + imgTab[0].getHeight() / 2 + 5, 3);
            mFont.tahoma_7_yellow.drawString(g, Char.myCharz().luongStr + string.Empty, GameCanvas.w / 2 + 10, y4 + imgTab[0].getHeight() / 2 - 2, mFont.LEFT, mFont.tahoma_7_grey);
            //vẽ hồng ngọc
            g.drawImage(Panel.imgLuongKhoa, GameCanvas.w / 2 + 60, y4 + imgTab[0].getHeight() / 2 + 5, 3);
            mFont.tahoma_7_yellow.drawString(g, Char.myCharz().luongKhoaStr + string.Empty, GameCanvas.w / 2 + 70, y4 + imgTab[0].getHeight() / 2 - 2, mFont.LEFT, mFont.tahoma_7_grey);
            //vẽ thỏi vàng
            g.drawImage(Panel.imgSlThoiVang, GameCanvas.w / 2 + 120, y4 + imgTab[0].getHeight() / 2 + 5, 3);
            int thoiVangQuantity = GetItemQuantity(457);
            mFont.tahoma_7_yellow.drawString(g, thoiVangQuantity.ToString(), GameCanvas.w / 2 + 130, y4 + imgTab[0].getHeight() / 2 - 2, mFont.LEFT, mFont.tahoma_7_grey);

            // g.drawImage((currTab == 1) ? imgTab[1] : imgTab[0], x4 + imgTab[0].getWidth() / 2 + 50, y4 + imgTab[0].getHeight() / 2 + 5, mGraphics.VCENTER | mGraphics.HCENTER);
            // mFont.tahoma_7_yellow.drawString(g, "Đệ tử", x4 + imgTab[0].getWidth() / 2 + 50, y4 + imgTab[0].getHeight() / 2, mGraphics.VCENTER | mGraphics.HCENTER, mFont.tahoma_7b_dark);

            int x5 = x4 + 4, y5 = y4 + 25, w5 = w4 - 8, h5 = h4 - 30;
            g.setColor(0, 1f);
            // viền ngoài kế 2 nút bản thân và đệ tử
            g.fillRect(x5, y5, w5, h5, 4);
            int x6 = x5 + 1, y6 = y5 + 1, w6 = w5 - 2, h6 = h5 - 2;
            g.setColor(8545082, 3f);
            // viền ngoài kế tiếp
            g.fillRect(x6, y6, w6, h6, 4);
            int x7 = x6 + 4, y7 = y6 + 8, w7 = w6 / 2 + 2, h7 = h6 - 16;
            g.setColor(13153676);
            // nền  nhân vật 
            if (isBody)
            {

                g.fillRect(x7, y7, w7, h7, 4);
                mFont.tahoma_7b_dark.drawString(g, "Power: ", x7 + w7 / 2 - 20, y7 + 2, mGraphics.VCENTER | mGraphics.HCENTER, mFont.tahoma_7b_white);
                mFont.tahoma_7b_dark.drawString(g, NinjaUtil.NumberText(Char.myCharz().cPower), x7 + w7 / 2 - 5, y7 + 2, mFont.LEFT, mFont.tahoma_7b_white);

                g.drawImage(TileMap.bong, x7 + w7 / 2, y7 + h7 / 2, mGraphics.VCENTER | mGraphics.HCENTER);
                int cx = x7 + w7 / 2;
                int cy = y7 + h7 / 2 - TileMap.bong.getHeight() / 2;
                #region EFFECT & MYCHARZ
                Char.myCharz().paintCharBody(g, cx, cy + 2, 1, Char.myCharz().cf, true);
                for (int i = 0; i < Char.myCharz().vEffChar.size(); i++)
                {
                    Effect effect = (Effect)Char.myCharz().vEffChar.elementAt(i);
                    if (effect.layer == 1)
                    {
                        bool flag = true;
                        if (effect.isStand == 0)
                        {
                            flag = ((Char.myCharz().statusMe == 1 || Char.myCharz().statusMe == 6) ? true : false);
                        }
                        if (flag)
                        {
                            effect.x = cx;
                            effect.y = cy + 20;
                            effect.paint(g);
                        }
                    }
                }
                #endregion
                #region BODY
                int x8 = x7 + 1;
                int y8 = y7 + h7 / 2 + 35;
                int w8 = w7 - 2;
                int h8 = h7 / 4 + 15;
                var c = Char.myCharz();
                g.setColor(11562007, 1f);
                // thanh thông tin ki sức mạnh 
                g.fillRect(x8, y8, w8, h8, 4);
                //Guild
                mFont.bigNumber_While.drawString(g, "Guild: ", x8 + 7, y8 + 4, mFont.LEFT);
                mFont.tahoma_7_white.drawString(g, (c.clan != null) ? c.clan.name : "Chưa vào bang", x8 + 35, y8 + 5, mFont.LEFT, mFont.tahoma_7b_dark);
                //Def
                mFont.tahoma_7_yellow.drawString(g, "DEF: ", x8 + 7, y8 + 20, mFont.LEFT, mFont.tahoma_7b_dark);
                mFont.tahoma_7_white.drawString(g, NinjaUtil.getMoneys(c.cDefull), x8 + 35, y8 + 20, mFont.LEFT, mFont.tahoma_7b_dark);
                //Crit
                mFont.tahoma_7_yellow.drawString(g, "CRIT: ", x8 + 7, y8 + 36, mFont.LEFT, mFont.tahoma_7b_dark);
                mFont.tahoma_7_white.drawString(g, NinjaUtil.getMoneys(c.cCriticalFull) + "%", x8 + 35, y8 + 36, mFont.LEFT, mFont.tahoma_7b_dark);
                //HP
                int x9 = x8 + w8 / 2 + 20;
                mFont.tahoma_7_yellow.drawString(g, "HP", x9, y8 + 5, mFont.RIGHT, mFont.tahoma_7b_dark);
                g.drawImage(imgBox[0], x9 + imgBox[0].getWidth() / 2 + 5, y8 + 5, mFont.RIGHT);
                int clipHP = (int)(c.cHP * imgBox[0].getWidth() / c.cHPFull);
                g.setColor(16713995);
                g.fillRect(x9 + 6, y8 + 6, clipHP - 2, imgBox[0].getHeight() - 2);
                mFont.tahoma_7_grey.drawString(g, NinjaUtil.getMoneys(c.cHP), x9 + 7, y8 + 4, mFont.LEFT);
                //KI
                mFont.tahoma_7_yellow.drawString(g, "MP", x9, y8 + 20, mFont.RIGHT, mFont.tahoma_7b_dark);
                g.drawImage(imgBox[0], x9 + imgBox[0].getWidth() / 2 + 5, y8 + 10 + imgBox[0].getHeight(), mFont.RIGHT);
                int clipKI = (int)(c.cMP * imgBox[0].getWidth() / c.cMPFull);
                g.setColor(2486262);
                g.fillRect(x9 + 6, y8 + imgBox[0].getHeight() + 11, clipKI - 2, imgBox[0].getHeight() - 2);
                mFont.tahoma_7_grey.drawString(g, NinjaUtil.getMoneys(c.cMP), x9 + 7, y8 + 19, mFont.LEFT);
                //DMG
                mFont.tahoma_7_yellow.drawString(g, "DMG", x9, y8 + 35, mFont.RIGHT, mFont.tahoma_7b_dark);
                mFont.tahoma_7_white.drawString(g, NinjaUtil.getMoneys(c.cDamFull), x9 + 7, y8 + 35, mFont.LEFT, mFont.tahoma_7b_dark);
                #endregion
                #region ITEMBODY
                for (int row = 0; row < numRow; row++)
                {
                    for (int col = 0; col < numCol; col++)
                    {
                        int x = x7 + 4 + col * (btnItem.getWidth() + 17);
                        int y = y7 + 4 + row * (btnItem.getHeight() + 2);
                        if (row < 5 && (col == 0 || col == numCol - 1))
                        {
                            g.drawImage(btnItem, x, y, 0);
                            Item item = null;
                            int index = -1;
                            if (col == 0 && row <= 5)
                            {
                                if (Char.myCharz().arrItemBody != null && row < Char.myCharz().arrItemBody.Length && Char.myCharz().arrItemBody[row] != null)
                                {
                                    item = Char.myCharz().arrItemBody[row];
                                }
                                index = row;
                            }
                            else if (col == numCol - 1 && row <= 5)
                            {
                                if (Char.myCharz().arrItemBody != null && row + 5 < Char.myCharz().arrItemBody.Length && Char.myCharz().arrItemBody[row + 5] != null)
                                {
                                    item = Char.myCharz().arrItemBody[row + 5];
                                }
                                index = row + 5;
                            }
                            if (item != null)
                            {
                                SmallImage.drawSmallImage(g, item.template.iconID, x + btnItem.getWidth() / 2, y + btnItem.getHeight() / 2, 0, mGraphics.VCENTER | mGraphics.HCENTER);
                                g.reset();
                                if (GameCanvas.isPointerHoldIn(x, y, btnItem.getWidth(), btnItem.getHeight()) && (GameCanvas.isPointerClick || GameCanvas.isPointerJustDown))
                                {
                                    if (GameCanvas.isPointerJustRelease)
                                    {
                                        isBody = false;
                                        itemDetails = item;
                                        isItemInfo = true;
                                        typePaint = TypePaint.typeBody;
                                        selected = (sbyte)index;
                                        InitScrollInfo();
                                        GameCanvas.clearAllPointerEvent();
                                    }
                                }
                            }
                        }
                        else if (row >= 4)
                        {
                            g.drawImage(btnItem, x, y);
                            int index = (row - 4) * numCol + (col + 9);
                            Item item = null;
                            if (Char.myCharz().arrItemBody != null && index < Char.myCharz().arrItemBody.Length && Char.myCharz().arrItemBody[index] != null)
                            {
                                item = Char.myCharz().arrItemBody[index];
                            }
                            if (item != null)
                            {
                                SmallImage.drawSmallImage(g, item.template.iconID, x + btnItem.getWidth() / 2, y + btnItem.getHeight() / 2, 0, mGraphics.VCENTER | mGraphics.HCENTER);
                                if (GameCanvas.isPointerHoldIn(x, y, btnItem.getWidth(), btnItem.getHeight()) && (GameCanvas.isPointerClick || GameCanvas.isPointerJustDown))
                                {
                                    if (GameCanvas.isPointerJustRelease)
                                    {
                                        isBody = false;
                                        itemDetails = item;
                                        isItemInfo = true;
                                        typePaint = TypePaint.typeBody;
                                        selected = (sbyte)index;
                                        InitScrollInfo();
                                        GameCanvas.clearAllPointerEvent();
                                    }
                                }
                            }
                        }
                    }
                }
                #endregion
            }
            #region ITEMBAG
            int x10 = x7 + w7 + 5;
            int y10 = y7 + 20;
            int w10 = w7 - 16;
            int h10 = h7 - 20;
            g.setColor(13153676);
            g.fillRect(x10, y10, w10, h10, 4);
            g.drawImage((currIndex == 0) ? indexBag[1] : indexBag[0], x10 + w10 / 2 - indexBag[0].getWidth(), y10 - indexBag[0].getHeight() - 5);
            mFont.tahoma_7_yellow.drawString(g, "Túi đồ", x10 + w10 / 2 - indexBag[0].getWidth() + 12, y10 - indexBag[0].getHeight(), 0, mFont.tahoma_7b_dark);
            g.drawImage((currIndex == 1) ? indexBag[1] : indexBag[0], x10 + w10 / 2 + 5, y10 - indexBag[0].getHeight() - 5);
            mFont.tahoma_7_yellow.drawString(g, "Rương Đồ", x10 + w10 / 2 + 12, y10 - indexBag[0].getHeight(), 0, mFont.tahoma_7b_dark);
            if (GameCanvas.isPointerHoldIn(x10 + w10 / 2 - indexBag[0].getWidth(), y10 - indexBag[0].getHeight() - 5, indexBag[0].getWidth(), indexBag[0].getHeight()))
            {
                if (GameCanvas.isPointerJustRelease && GameCanvas.isPointerClick)
                {
                    currIndex = 0;
                    GameCanvas.clearAllPointerEvent();
                }
            }
            if (GameCanvas.isPointerHoldIn(x10 + w10 / 2 + 5, y10 - indexBag[0].getHeight() - 5, indexBag[0].getWidth(), indexBag[0].getHeight()))
            {
                if (GameCanvas.isPointerJustRelease && GameCanvas.isPointerClick)
                {
                    currIndex = 1;
                    GameCanvas.clearAllPointerEvent();
                }
            }
            if (cellsBag == null || cellsBag.Length != (numCol2 * numRow2))
                cellsBag = new Cell[(numCol2 * numRow2)];
            g.setClip(x10, y10, w10, h10);
            g.translate(0, -scrollBag.cmy);
            for (int row = 0; row < numRow2; row++)
            {
                for (int col = 0; col < numCol2; col++)
                {
                    int x = x10 + 6 + (col * (btnItem2.getWidth() + 5));
                    int y = y10 + 5 + (row * (btnItem2.getHeight() + 5));
                    int index = row * numCol2 + col;
                    if (cellsBag[index] == null)
                    {
                        cellsBag[index] = new Cell(x, y);
                    }
                    cellsBag[index].paint(g);
                    Item[] items = (currIndex == 0) ? Char.myCharz().arrItemBag : Char.myCharz().arrItemBox;
                    if (items != null && index < items.Length && items[index] != null)
                    {
                        SmallImage.drawSmallImage(g, items[index].template.iconID, x + btnItem2.getWidth() / 2, y + btnItem2.getHeight() / 2, 0, mGraphics.VCENTER | mGraphics.HCENTER);
                        mFont.tahoma_7_yellow.drawString(g, $"{items[index].quantity}", x + btnItem2.getWidth() / 2 + 2, y + btnItem2.getHeight() / 2 + 2, mGraphics.VCENTER | mGraphics.HCENTER, mFont.tahoma_7b_dark);
                        if (GameCanvas.isPointerHoldIn(cellsBag[index].x, cellsBag[index].y - scrollBag.cmy, btnItem2.getWidth(), btnItem2.getHeight())
                            && GameCanvas.isPointerHoldIn(x10, y10, w10, h10))
                        {
                            if (GameCanvas.isPointerJustRelease && GameCanvas.isPointerClick)
                            {
                                isBody = false;
                                itemDetails = items[index];
                                isItemInfo = true;
                                typePaint = (currIndex == 0) ? TypePaint.typeBag : TypePaint.typeBox;
                                selected = (sbyte)index;
                                InitScrollInfo();
                                GameCanvas.clearAllPointerEvent();
                            }

                        }
                    }
                }
            }
            g.translate(0, scrollBag.cmy);
            g.reset();
            paintItemDetails(g, x7, y7, w7, h7);
            #endregion
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
                            isBody = true;
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
                            int yPaint = y + 51;
                            for (int i = 0; i < itemDetails.itemOption.Length; i++)
                            {
                                if (itemDetails.itemOption[i].optionTemplate.id != 107 && itemDetails.itemOption[i].optionTemplate.id != 102)
                                {
                                    mFont.tahoma_7b_green.drawString(g, itemDetails.itemOption[i].getOptionString(), x + 6, yPaint, 0);
                                    yPaint += 12;
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
                        switch (typePaint)
                        {
                            case TypePaint.typeBody:
                            case TypePaint.typeBox:
                                if (tabs[0] != null)
                                {
                                    tabs[0].x = x + 5;
                                    tabs[0].y = y + h - 25 - tabs[1].tabs[0].getHeight();
                                    tabs[0].paint(g);
                                    if (tabs[0].Pressed())
                                    {
                                        tabs[0].actionPerform();
                                    }
                                }
                                break;
                            case TypePaint.typeBag:
                                if (tabs[1] != null)
                                {
                                    tabs[1].x = x + 5;
                                    tabs[1].y = y + h - 25 - tabs[1].tabs[0].getHeight();
                                    tabs[1].paint(g);
                                    if (tabs[1].Pressed())
                                    {
                                        tabs[1].actionPerform();
                                    }
                                }
                                if (tabs[2] != null)
                                {
                                    tabs[2].x = x + 15 + tabs[1].tabs[0].getWidth() * 2;
                                    tabs[2].y = y + h - 25 - tabs[1].tabs[0].getHeight();
                                    tabs[2].paint(g);
                                    if (tabs[2].Pressed())
                                    {
                                        tabs[2].actionPerform();
                                    }
                                }
                                if (tabs[3] != null)
                                {
                                    tabs[3].x = x + 10 + tabs[1].tabs[0].getWidth();
                                    tabs[3].y = y + h - 25 - tabs[1].tabs[0].getHeight();
                                    tabs[3].paint(g);
                                    if (tabs[3].Pressed())
                                    {
                                        tabs[3].actionPerform();
                                    }
                                }
                                if (tabs[4] != null)
                                {
                                    tabs[4].x = x + 5;
                                    tabs[4].y = y + h - 20 - tabs[1].tabs[0].getHeight() + tabs[4].tabs[0].getHeight();
                                    tabs[4].paint(g);
                                    if (tabs[4].Pressed())
                                    {
                                        tabs[4].actionPerform();
                                    }
                                }
                                break;
                        }
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

        public void perform(int idAction, object p)
        {
            switch (idAction)
            {
                case 1:
                    if (GameScr.gI().isBagFull())
                    {
                        GameScr.info1.addInfo("|2|Hành Trang Đã Đầy!", 0);
                    }
                    else
                    {
                        if (typePaint == TypePaint.typeBody)
                        {
                            Service.gI().getItem(Panel.BODY_BAG, (sbyte)selected);
                        }
                        else
                        {
                            Service.gI().getItem(Panel.BOX_BAG, (sbyte)selected);
                        }
                    }
                    isItemInfo = false;
                    isBody = true;
                    break;
                case 2:
                    if (!itemDetails.isTypeBody())
                    {
                        Service.gI().useItem(0, 1, (sbyte)itemDetails.indexUI, -1);
                    }
                    else
                    {
                        Service.gI().getItem(Panel.BAG_BODY, (sbyte)selected);
                    }
                    isItemInfo = false;
                    isBody = true;
                    break;
                case 3:
                    Service.gI().useItem(2, 1, (sbyte)itemDetails.indexUI, -1);
                    isItemInfo = false;
                    isBody = true;
                    break;
                case 4:
                    if (GameScr.gI().isBoxFull())
                    {
                        GameScr.info1.addInfo("|2|Rương Đồ Đã Đầy!", 0);
                    }
                    else
                    {
                        Service.gI().getItem(Panel.BAG_BOX, (sbyte)selected);
                    }
                    isItemInfo = false;
                    isBody = true;
                    break;
                case 5:
                    Service.gI().getItem(Panel.BAG_PET, (sbyte)selected);
                    isItemInfo = false;
                    isBody = true;
                    break;
                case 1000:
                    GameCanvas.panel.setTypeMain();
                    GameCanvas.panel.show();
                    break;
                case 999:
                    isShow = true;
                    break;
            }
        }
    }
}