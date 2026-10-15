namespace Game3
{
	using System;
	using UnityEngine;
	// Token: 0x020000C6 RID: 198
	public class SplashScr : mScreen
	{
		public static int maxVideoLength2;
		public static bool IsVideoPlaying { get; private set; }
		public static void SetIsVideoPlaying(bool isPlaying)
		{
			IsVideoPlaying = isPlaying;
		}

		// Token: 0x060009FA RID: 2554 RVA: 0x0009826A File Offset: 0x0009666A
		public SplashScr()
		{
			SplashScr.instance = this;
		}

		// Token: 0x060009FB RID: 2555 RVA: 0x00098280 File Offset: 0x00096680
		public static void loadSplashScr()
		{
			SplashScr.splashScrStat = 0;
		}


		// Token: 0x060009FC RID: 2556 RVA: 0x00098288 File Offset: 0x00096688
		public override void update()
		{

			if (!this.isCheckConnect && IsVideoPlaying == false)
			{
				isCheckConnect = true;
				if (Rms.loadRMSInt("serverchat") != -1)
				{
					GameScr.isPaintChatVip = ((Rms.loadRMSInt("serverchat") == 0) ? true : false);
				}
				if (Rms.loadRMSInt("isPlaySound") != -1)
				{
					GameCanvas.isPlaySound = Rms.loadRMSInt("isPlaySound") == 1;
				}
				if (GameCanvas.isPlaySound)
				{
					SoundMn.gI().loadSound(TileMap.mapID);
				}
				SoundMn.gI().getStrOption();
				// if (Rms.loadRMSInt("svselect") == -1)
				// {
				// 	string linkDefault = ServerListScreen.linkDefault;
				// 	string[] array = Res.split(linkDefault.Trim(), ",", 0);
				// 	mResources.loadLanguague(sbyte.Parse(array[array.Length - 2]));
				// 	ServerListScreen.nameServer = new string[array.Length - 2];
				// 	ServerListScreen.address = new string[array.Length - 2];
				// 	ServerListScreen.port = new short[array.Length - 2];
				// 	ServerListScreen.language = new sbyte[array.Length - 2];
				// 	ServerListScreen.hasConnected = new bool[2];
				// 	for (int i = 0; i < array.Length - 2; i++)
				// 	{
				// 		string[] array2 = Res.split(array[i].Trim(), ":", 0);
				// 		ServerListScreen.nameServer[i] = array2[0];
				// 		ServerListScreen.address[i] = array2[1];
				// 		ServerListScreen.port[i] = short.Parse(array2[2]);
				// 		ServerListScreen.language[i] = sbyte.Parse(array2[3].Trim());
				// 	}
				// 	GameCanvas.serverScr.switchToMe();
				// }
				// else
				// {
				ServerListScreen.loadIP();
				// }
			}

			ServerListScreen.updateDeleteData();
			if (IsVideoPlaying == false)
			{
				Res.outz("cho man hinh nay qa lau");
				if (Session_ME.gI().isConnected())
				{
					ServerListScreen.loadScreen = true;
					GameCanvas.serverScreen.switchToMe();
				}
				else
				{
					mSystem.onDisconnected();
				}
			}
		}

		// Token: 0x060009FD RID: 2557 RVA: 0x00098478 File Offset: 0x00096878
		public static void loadIP()
		{
			if (Rms.loadRMSInt("svselect") == -1)
			{
				int num = 0;
				if ((int)mResources.language > 0)
				{
					for (int i = 0; i < (int)mResources.language; i++)
					{
						num += ServerListScreen.lengthServer[i];
					}
				}
				if ((int)ServerListScreen.serverPriority == -1)
				{
					ServerListScreen.ipSelect = num + Res.random(0, ServerListScreen.lengthServer[(int)mResources.language]);
				}
				else
				{
					ServerListScreen.ipSelect = (int)ServerListScreen.serverPriority;
				}
				Rms.saveRMSInt("svselect", ServerListScreen.ipSelect);
				GameMidlet.IP = ServerListScreen.address[ServerListScreen.ipSelect];
				GameMidlet.PORT = (int)ServerListScreen.port[ServerListScreen.ipSelect];
				mResources.loadLanguague(ServerListScreen.language[ServerListScreen.ipSelect]);
				LoginScr.serverName = ServerListScreen.nameServer[ServerListScreen.ipSelect];
				GameCanvas.connect();
			}
			else
			{
				ServerListScreen.ipSelect = Rms.loadRMSInt("svselect");
				if (ServerListScreen.ipSelect > ServerListScreen.nameServer.Length - 1)
				{
					ServerListScreen.ipSelect = (int)ServerListScreen.serverPriority;
					Rms.saveRMSInt("svselect", ServerListScreen.ipSelect);
				}
				GameMidlet.IP = ServerListScreen.address[ServerListScreen.ipSelect];
				GameMidlet.PORT = (int)ServerListScreen.port[ServerListScreen.ipSelect];
				mResources.loadLanguague(ServerListScreen.language[ServerListScreen.ipSelect]);
				LoginScr.serverName = ServerListScreen.nameServer[ServerListScreen.ipSelect];
				GameCanvas.connect();
			}
		}

		// Token: 0x060009FE RID: 2558 RVA: 0x000985D4 File Offset: 0x000969D4
		public override void paint(mGraphics g)
		{
			if (imgLogo != null)
			{
				// g.setColor(0);
				// g.fillRect(0, 0, GameCanvas.w, GameCanvas.h);
				// g.drawImage(imgLogo, GameCanvas.w / 2, GameCanvas.h / 2, 3);
			}
			if (nData != -1)
			{
				g.setColor(0);
				g.fillRect(0, 0, GameCanvas.w, GameCanvas.h);
				g.drawImage(LoginScr.imgTitle, GameCanvas.w / 2, GameCanvas.h / 2 - 24, StaticObj.BOTTOM_HCENTER);
				GameCanvas.paintShukiren(GameCanvas.hw, GameCanvas.h / 2 + 24, g);
				mFont.tahoma_7b_white.drawString(g, mResources.downloading_data + nData * 100 / maxData + "%", GameCanvas.w / 2, GameCanvas.h / 2, 2);
			}
			else if (IsVideoPlaying == false)
			{
				g.setColor(0);
				g.fillRect(0, 0, GameCanvas.w, GameCanvas.h);
				GameCanvas.paintShukiren(GameCanvas.hw, GameCanvas.hh, g);
				if (ServerListScreen.cmdDeleteRMS != null)
				{
					mFont.tahoma_7_white.drawString(g, mResources.xoadulieu, GameCanvas.w - 2, GameCanvas.h - 15, 1, mFont.tahoma_7_grey);

				}
			}
		}

		// Token: 0x060009FF RID: 2559 RVA: 0x0009872D File Offset: 0x00096B2D
		public static void loadImg()
		{
			SplashScr.imgLogo = GameCanvas.loadImage("/gamelogo.png");
		}

		// Token: 0x04001260 RID: 4704
		public static int splashScrStat;

		// Token: 0x04001261 RID: 4705
		private bool isCheckConnect;

		// Token: 0x04001262 RID: 4706
		private bool isSwitchToLogin;

		// Token: 0x04001263 RID: 4707
		public static int nData = -1;

		// Token: 0x04001264 RID: 4708
		public static int maxData = -1;

		// Token: 0x04001265 RID: 4709
		public static SplashScr instance;

		// Token: 0x04001266 RID: 4710
		public static Image imgLogo;

		// Token: 0x04001267 RID: 4711
		private int timeLoading = 10;

		// Token: 0x04001268 RID: 4712
		public long TIMEOUT;
	}
}