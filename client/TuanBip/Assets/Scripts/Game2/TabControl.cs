namespace Game2
{
    using System.Linq;
    using UnityEngine.SceneManagement;

    public class TabControll : mScreen
    {
        private static TabControll _Instance;
        public static TabControll Instance => _Instance ?? (_Instance = new TabControll());

        public TabControll()
        {
            initCommand();
        }

        private static bool _selectTab;

        public static bool selectTab
        {
            get => _selectTab;
            set => _selectTab = value;
        }

        private static bool _isShow = true;

        public static bool isShow
        {
            get => _isShow;
            set => _isShow = value;
        }

        private static sbyte tabIndex = 0;

        private static TabCommand firstCommand;

        private static TabCommand[] TransferTab = Enumerable.Range(0, 3)
              .Select(i => new TabCommand((i + 1).ToString(), () => TransferTabIndex((sbyte)(i - 1))))
              .ToArray();

        private static string[] SceneNames = new string[]
        {
            "NROL",
            "NRO2",
            "NRO3"
        };
        private static TabType[] tabTypes = new TabType[]
        {
            TabType.Tab1,
            TabType.Tab2, TabType.Tab3
        };
        private static void initCommand()
        {
            int x_menu = 160 + (GameScr.imgModMenu.getWidth() + 5);
            firstCommand = new TabCommand("Tab 2", () => showTabSelect());
            firstCommand.x = x_menu; //(GameCanvas.w / 4) * 3
            firstCommand.y = 0;
            for (int i = 0; i < TransferTab.Length; i++)
            {
                TransferTab[i].x = x_menu + (TabCommand.menu.getWidth() + 2) + i * (TabCommand.menu.getWidth() + 2);
                TransferTab[i].y = 0;
            }
        }
        public override void paint(mGraphics g)
        {
            if (!isShow) return;
            firstCommand.paint(g);
            paintTab(g);
            //int currentTabIndex = -1;
            //string currentScene = UnityEngine.SceneManagement.SceneManager.GetActiveScene().name;
            //for (int i = 0; i < SceneNames.Length; i++)
            //{
            //    if (SceneNames[i] == currentScene)
            //    {
            //        currentTabIndex = i;
            //        break;
            //    }
            //}
            //mFont.tahoma_7b_red.drawString(g, "Tab " + (currentTabIndex + 1), firstCommand.x + 10, firstCommand.y + 25, 3);
            base.paint(g);
        }
        private void paintTab(mGraphics g)
        {
            if (!selectTab) return;
            foreach (var cmd in TransferTab)
            {
                cmd.paint(g);
            }
        }
        private static void TransferTabIndex(sbyte index)
        {
            tabIndex = (sbyte)(index + 1);

            SceneManager.LoadScene(SceneNames[index + 1]);
            TabManagement.tab = (global::TabType)tabTypes[index + 1];
            _selectTab = false;
        }
        private static void showTabSelect()
        {
            _selectTab = !_selectTab;
        }
        public bool isPointerHoldInTab()
        {
            if (!isShow)
                return false;
            if (firstCommand.isPointerInside())
            {
                firstCommand.Invoke();
                return true;
            }
            if (selectTab)
            {
                foreach (var cmd in TransferTab)
                {
                    if (cmd.isPointerInside())
                    {
                        cmd.Invoke();
                        return true;
                    }
                }
            }
            return false;
        }
        public override void updateKey()
        {
            base.updateKey();
        }
    }
}
