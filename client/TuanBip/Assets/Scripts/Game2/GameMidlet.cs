namespace Game2
{
	using System;
	using UnityEngine;

	// Token: 0x020000CD RID: 205
	public class GameMidlet
	{
		// Token: 0x06000A91 RID: 2705 RVA: 0x000A0D04 File Offset: 0x0009F104
		public GameMidlet()
		{
			this.initGame();
		}

		// Token: 0x06000A92 RID: 2706 RVA: 0x000A0D14 File Offset: 0x0009F114
		public void initGame()
		{
			GameMidlet.instance = this;
			MotherCanvas.instance = new MotherCanvas();
			Session_ME.gI().setHandler(Controller.gI());
			Session_ME2.gI().setHandler(Controller.gI());
			Session_ME2.isMainSession = false;
			GameMidlet.instance = this;
			GameMidlet.gameCanvas = new GameCanvas();
			GameMidlet.gameCanvas.start();
			SplashScr.loadImg();
			SplashScr.loadSplashScr();
			GameCanvas.currentScreen = new SplashScr();
		}

		// Token: 0x06000A93 RID: 2707 RVA: 0x000A0D83 File Offset: 0x0009F183
		public void exit()
		{
			if (Main.typeClient == 6)
			{
				mSystem.exitWP();
			}
			else
			{
				GameCanvas.bRun = false;
				mSystem.gcc();
				this.notifyDestroyed();
			}
		}

		// Token: 0x06000A94 RID: 2708 RVA: 0x000A0DAB File Offset: 0x0009F1AB
		public static void sendSMS(string data, string to, Command successAction, Command failAction)
		{
			Cout.println("SEND SMS");
		}

		// Token: 0x06000A95 RID: 2709 RVA: 0x000A0DB7 File Offset: 0x0009F1B7
		public static void flatForm(string url)
		{
			Cout.LogWarning("PLATFORM REQUEST: " + url);
			Application.OpenURL(url);
		}

		// Token: 0x06000A96 RID: 2710 RVA: 0x000A0DCF File Offset: 0x0009F1CF
		public void notifyDestroyed()
		{
			Main.exit();
		}

		// Token: 0x06000A97 RID: 2711 RVA: 0x000A0DD6 File Offset: 0x0009F1D6
		public void platformRequest(string url)
		{
			Cout.LogWarning("PLATFORM REQUEST: " + url);
			Application.OpenURL(url);
		}

		// Token: 0x040013DD RID: 5085
		public static string IP = "103.231.248.134";

		// Token: 0x040013DE RID: 5086
		public static int PORT = 14445;

		// Token: 0x040013DF RID: 5087
		public static string IP2;

		// Token: 0x040013E0 RID: 5088
		public static int PORT2;

		// Token: 0x040013E1 RID: 5089
		public static sbyte PROVIDER;

		// Token: 0x040013E2 RID: 5090
		public static int LANGUAGE;

		// Token: 0x040013E3 RID: 5091
		public static string VERSION = "2.3.7";

		public static string VERSION2 = "5.0.0";

		// Token: 0x040013E4 RID: 5092
		public static int intVERSION = 237;

		// Token: 0x040013E5 RID: 5093
		public static GameCanvas gameCanvas;

		// Token: 0x040013E6 RID: 5094
		public static GameMidlet instance;

		// Token: 0x040013E7 RID: 5095
		public static bool isConnect2;

		// Token: 0x040013E8 RID: 5096
		public static bool isBackWindowsPhone;
	}
}