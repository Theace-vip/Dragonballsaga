namespace Game1
{
    using System;
    
    public class TabCommand : BaseCommand
    {
        public static Image menu, menu1;
        public TabCommand(string caption, Action action) : base(caption, action)
        {
            this.caption = caption;
            this.action = action;
            this.w = menu.getWidth();
            this.h = menu.getHeight();
        }
        public static void loadBG()
        {
            menu = GameCanvas.loadImage("/mainImage/myTexture2dnut.png");
            menu1 = GameCanvas.loadImage("/mainImage/myTexture2dnutF.png");
        }
        public override void paint(mGraphics g)
        {
            g.drawImage(isFocus ? menu1 : menu, x, y);
            mFont.tahoma_7b_dark.drawString(g, caption, x + w / 2 + 1, y + h / 2 - mFont.tahoma_7b_dark.getHeight() / 2, 3);
        }
    
        public override bool isPointerInside()
        {
            isFocus = false;
            if (GameCanvas.isPointerHoldIn(x, y, w, h))
            {
                if (GameCanvas.isPointerDown)
                {
                    isFocus = true;
                }
                if(GameCanvas.isPointerClick && GameCanvas.isPointerJustRelease)
                {
                    return true;
                }
            }
            return false;
        }
        public override void Invoke()
        {
            GameCanvas.clearAllPointerEvent();
            action?.Invoke();
        }
    }
}
