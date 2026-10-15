namespace Game1
{
	using System;

	public class ShowBoss
	{
		public string nameBoss;

		public string mapName;

		public int mapID;

		public DateTime time;

		public ShowBoss(string a)
		{
			a = a.Replace(a.Substring(0, 5), "|");
			a = a.Replace(" vừa xuất hiện tại", "|");
			a = a.Replace(" khu vực", "|");
			string[] array = a.Split('|');
			nameBoss = array[1].Trim();
			mapName = array[2].Trim();
			mapID = VuDang.GetIDMap(mapName);
			time = DateTime.Now;
		}

		public void paintBoss(mGraphics a, int b, int c, int d)
		{
			TimeSpan timeSpan = DateTime.Now.Subtract(time);
			int num = (int)timeSpan.TotalSeconds;
			mFont mFont2 = mFont.tahoma_7b_yellowSmall2;
			if (TileMap.mapName.Trim().ToLower() == mapName.Trim().ToLower())
			{
				mFont2 = mFont.tahoma_7_red;
				for (int i = 0; i < GameScr.vCharInMap.size(); i++)
				{
					if (((Char)GameScr.vCharInMap.elementAt(i)).cName == nameBoss)
					{
						mFont2 = mFont.tahoma_7b_red;
						break;
					}
				}
			}
			mFont2.drawString(a, nameBoss + " - " + mapName + " - " + ((num < 60) ? (num + "s") : (timeSpan.Minutes + "p")) + " <<<", b, c, d);
		}
	}
}