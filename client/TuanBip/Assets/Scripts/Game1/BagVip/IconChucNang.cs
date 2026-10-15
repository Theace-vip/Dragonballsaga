using System;
using UnityEngine;

namespace Game1.BagVip
{
    public class IconChucNang : IActionListener
    {
        private static IconChucNang instance;
        public bool isShow = false;
        private Image bgSize;
        private Image imgX;

        public static Image[] imgIcon = new Image[4];

        public static string[] namePNG = new string[4] { "menuauto", "tuido", "phucloi", "iconvongquay" };

        public static string[] nameChucNang = new string[4] { "Menu Auto", "Túi đồ", "Phúc lợi", "Vòng quay" };

        private Scroll scrollIcon;
        private int X, Y, W, H;
        public static IconChucNang gI()
        {
            if (instance == null)
            {
                instance = new IconChucNang();
            }
            return instance;
        }
        public IconChucNang()
        {
            if (bgSize == null)
            {
                bgSize = GameCanvas.loadImage("/iconchucnang/bgshowicon.png");
            }
            imgX = GameCanvas.loadImage("/iconchucnang/imgx.png");

            for (int i = 0; i < imgIcon.Length; i++)
            {
                if (imgIcon[i] == null)
                {
                    imgIcon[i] = GameCanvas.loadImage("/iconchucnang/" + namePNG[i] + ".png");
                }
            }

            X = GameCanvas.w / 2 - bgSize.getWidth() / 2 + 5;
            Y = GameCanvas.h / 2 - bgSize.getHeight() / 2 + 5;
            W = bgSize.getWidth();
            H = bgSize.getHeight();
            InitScrollIcon();
        }
        public void update()
        {
            if (!isShow) return;
            updateScroll(scrollIcon);
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

        private void InitScrollIcon()
        {
            try
            {
                int x = X + 5;
                int y = Y + 5;
                int w = W - 10;
                int h = H - 10;

                int khoangCachY = 38;
                int h_cachnhau = imgIcon[0].getHeight() + khoangCachY;

                GameCanvas.clearAllPointerEvent();
                scrollIcon = new Scroll();
                scrollIcon.clear();
                if (imgIcon.Length > 0)
                {
                    int size = imgIcon.Length / 4 + (imgIcon.Length % 4 == 0 ? 0 : 1);
                    scrollIcon.setStyle(size, size * h_cachnhau
                        , x, y, w, h, true, size);
                }
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

                int x_close = x_bg + bgSize.getWidth() - imgX.getWidth() / 2;
                int y_close = y_bg - imgX.getHeight() / 2;

                g.drawImage(imgX, x_close, y_close, 0);
                if (GameCanvas.isPointerHoldIn(x_close, y_close, imgX.getWidth(), imgX.getHeight()))
                {
                    if (GameCanvas.isPointerJustRelease && GameCanvas.isPointerClick)
                    {
                        isShow = false;
                        GameCanvas.clearAllPointerEvent();
                    }
                }
                //vẽ icon
                int x_icon = X + 15;
                int y_icon = Y + 15;
                int khoangCachX = 35;
                int khoangCachY = 38;
                int cot = 0;
                int xuongDong = 0;

                g.setClip(X + 5, Y + 5, W - 10, H - 10);
                g.translate(scrollIcon.cmx, -scrollIcon.cmy);

                for (int i = 0; i < imgIcon.Length; i++)
                {
                    if (i != 0 && i % 4 == 0)
                    {
                        xuongDong++;
                        cot = 0;
                    }
                    //vẽ icon Chức năng
                    g.drawImage(imgIcon[i], x_icon + (imgIcon[i].getWidth() + khoangCachX) * cot, y_icon + xuongDong * (imgIcon[i].getHeight() + khoangCachY), 0);
                    //vẽ tên Chức năng
                    mFont.tahoma_7_white.drawString(g, nameChucNang[i],
                        x_icon + (imgIcon[i].getWidth() + khoangCachX) * cot + imgIcon[i].getWidth() / 2
                        , y_icon + xuongDong * (imgIcon[i].getHeight() + khoangCachY) + (imgIcon[i].getHeight() + 3)
                        , mGraphics.VCENTER | mGraphics.HCENTER);

                    if (!GameCanvas.isPointerHoldIn(X, Y, W, H))
                    {
                        if (GameCanvas.isPointerJustRelease && GameCanvas.isPointerClick)
                        {
                            isShow = false;
                            GameCanvas.clearAllPointerEvent();
                        }
                    }
                    if (GameCanvas.isPointerHoldIn(x_icon + (imgIcon[i].getWidth() + khoangCachX) * cot,
                        y_icon + xuongDong * (imgIcon[i].getHeight() + khoangCachY) - scrollIcon.cmy, imgIcon[i].getWidth(), imgIcon[i].getHeight())
                        && GameCanvas.isPointerHoldIn(X, Y, W, H))
                    {
                        if (GameCanvas.isPointerJustRelease && GameCanvas.isPointerClick)
                        {
                            if (i == 0)
                            {
                                isShow = false;
                                GameScr.menumod();
                                GameCanvas.clearAllPointerEvent();
                            }
                            if (i == 1)
                            {
                                isShow = false;
                                Bag.gI().perform(999, null);
                                GameCanvas.clearAllPointerEvent();
                            }
                            if (i == 2)
                            {
                                if (PhucLoi.START == 1)
                                {
                                    isShow = false;
                                    PhucLoi.gI().perform(999, null);
                                    GameCanvas.clearAllPointerEvent();
                                }
                                else
                                {
                                    GameScr.info1.addInfo("Chức năng đang đóng", 0);
                                }
                            }
                            if (i == 3)
                            {
                                if (TamBao.START == 1)
                                {
                                    isShow = false;
                                    TamBao.gI().perform(999, null);
                                    GameCanvas.clearAllPointerEvent();
                                }
                                else
                                {
                                    GameScr.info1.addInfo("Chức năng đang đóng", 0);
                                }
                            }
                        }
                    }
                    cot++;
                }
            }
            catch (Exception e)
            {
                Debug.LogError(e.ToString());
            }
        }

        public bool isChucNangShow()
        {
            if (isShow || Bag.gI().isShow
                || PhucLoi.gI().isShow || TamBao.gI().isShow)
            {
                return true;
            }
            return false;
        }

        public bool isNotShow()
        {
            if (!Bag.gI().isShow && !PhucLoi.gI().isShow && !TamBao.gI().isShow)
            {
                return true;
            }
            return false;
        }

        public void UpdateChucNang()
        {
            Bag.gI().update();
            if (IconChucNang.gI().isShow)
            {
                IconChucNang.gI().update();
            }
            if (PhucLoi.START == 1)
            {
                PhucLoi.gI().update();
            }
            if (TamBao.START == 1)
            {
                TamBao.gI().update();
            }
        }

        public void PaintChucNang(mGraphics g)
        {
            Bag.gI().Paint(g);
            if (IconChucNang.gI().isShow)
            {
                IconChucNang.gI().Paint(g);
            }
            if (PhucLoi.START == 1)
            {
                PhucLoi.gI().Paint(g);
            }
            if (TamBao.START == 1)
            {
                TamBao.gI().Paint(g);
            }
        }

        public void perform(int idAction, object p)
        {
            switch (idAction)
            {
                case 999:
                    isShow = true;
                    InitScrollIcon();
                    break;
            }
        }
    }
}