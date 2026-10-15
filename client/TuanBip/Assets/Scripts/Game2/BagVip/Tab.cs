namespace Game2.BagVip
{
    public class Tab
    {
        public string caption;
        public IActionListener listener;
        public int idAction;
        public object p;
        public Image[] tabs = new Image[3];
        public int x, y;
        public bool isFocus;
        public Tab(string caption, IActionListener listener, int idAction, object p)
        {
            this.caption = caption;
            this.listener = listener;
            this.idAction = idAction;
            this.p = p;
            for (int i = 0; i < tabs.Length; i++)
            {   
                if (tabs[i] == null)
                {
                    tabs[i] = GameCanvas.loadImage($"/bag/cmd{i}.png");
                }
            }
        }
        public void paint(mGraphics g)
        {
            if (isFocus)
            {
                g.drawImage(tabs[1], x, y);
            }
            else if (GameCanvas.isMouseFocus(x, y, tabs[0].getWidth(), tabs[0].getHeight()))
            {
                g.drawImage(tabs[2], x, y);
            }
            else
            {
                g.drawImage(tabs[0], x, y);
            }
            if (caption != "")
            {
                mFont.tahoma_7_white.drawString(g, caption, x + tabs[0].getWidth() / 2, y + 3, mGraphics.VCENTER | mGraphics.HCENTER, mFont.tahoma_7b_dark);
            }
        }
        public void actionPerform()
        {
            if (listener != null)
            {
                listener.perform(idAction, p);
            }
            else
            {
                GameScr.gI().actionPerform(idAction, p);
            }
        }
        public bool Pressed()
        {
            isFocus = false;
            if (GameCanvas.isPointerHoldIn(x, y, tabs[0].getWidth(), tabs[0].getHeight()))
            {
                if (GameCanvas.isPointerDown)
                {
                    isFocus = true;
                }
                if (GameCanvas.isPointerJustRelease && GameCanvas.isPointerClick)
                {
                    return true;
                }
            }
            return false;
        }
    }
}