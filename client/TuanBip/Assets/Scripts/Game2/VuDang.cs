namespace Game2
{
	using System;
	using System.Collections.Generic;
	using System.IO;
	using System.Text;
	using System.Threading;
	using AssemblyCSharp.Mod.PickMob;
	using AssemblyCSharp.Mod.Xmap;
	using UnityEngine;
	using static GameScr;

	internal class VuDang
	{
		public struct setDo1
		{
			public string info;

			public int type;

			public int id;

			public string name;

			public string option;

			public long soSao;

			public setDo1(int type, string info, int id, string name, string option, long soSao)
			{
				this.type = type;
				this.info = info;
				this.id = id;
				this.name = name;
				this.option = option;
				this.soSao = soSao;
			}
		}

		public struct setDo2
		{
			public string info;

			public int type;

			public int id;

			public string name;

			public string option;

			public long soSao;

			public setDo2(int type, string info, int id, string name, string option, long soSao)
			{
				this.type = type;
				this.info = info;
				this.id = id;
				this.name = name;
				this.option = option;
				this.soSao = soSao;
			}
		}

		public struct ItemAuto
		{
			public int iconID;

			public int id;

			public ItemAuto(int iconID, int id)
			{
				this.iconID = iconID;
				this.id = id;
			}
		}

		public static bool isbanvang;

		public static int speed;

		private static string ListMap;

		public static string[] MapNames;

		public static bool dichChuyenPem;

		public static bool isAK;

		private static long currUpdateKhu;

		public static long[] currTimeAK;

		public static bool isNhanVang;

		public static bool isDapDo;

		public static Item doDeDap;

		public static int soSaoCanDap;

		private static long saoHienTai;

		public static bool dangBanVang;

		public static bool isVaoKhu;

		public static Char[] chars;

		public static bool nvat;

		public static long currChangeZone;

		public static bool achat;

		private static long currAutoChat;

		public static string textAutoChat;

		public static bool isAutoCTG;

		private static long currAutoCTG;

		public static string textAutoChatTG;

		public static bool isAutoNhatXa;

		public static int xNhatXa;

		public static int yNhatXa;

		private static long currNhatXa;

		private static long currAutoBT;

		public static sbyte petStatus;

		public static bool isAutoBT;

		public static int timeBT;

		public static bool isGMT;

		public static Char charMT;

		public static bool xoamap;

		public static bool XoaBackground;

		private static string backgroundColor;

		public static bool giamDungLuong;

		public static bool isPKM;

		public static bool isBossM;

		public static bool trangThai;

		public static bool thongBaoBoss = true;

		public static MyVector bossVip;

		public static bool lineboss;

		public static int dichLenXuongTraiPhai;

		public static int ghimX;

		public static int ghimY;

		public static bool isKhoaViTri;

		private static long currKhoaViTri;

		public static bool xoaHieuUngHopThe;

		public static bool xoaTauBay;

		public static bool isThongTinDeTu;

		public static bool isThongTinSuPhu;

		public static int countBuyItem;

		public static Item itemBuy;

		private static long currBuyItem;

		public static bool isKSBoss;

		public static int HPKSBoss;

		public static bool isKSBossBangSkill5;

		private static long currHoiSinh;

		public static bool hoiSinhNgoc;

		public static int ngocDuocDungDeHoiSinh;

		public static int ngocHienTai;

		public static string tennoitaicanmo;

		public static bool isNoitai;

		public static int ntMin;

		public static int ntNow;

		public static bool doBoss;

		public static int zoneMacDinh;

		public static string bossCanDo;

		private static long currDoBoss;

		public static bool isUpdateKhu;

		private static long currFocusBoss;

		public static bool focusBoss;

		public static bool isThuongDeThuong;

		//public static bool isThuongDeVip;

		private static long currThuongDe;

		public static bool isPaintCrackBall;

		public static bool dangNhanAll;

		private static bool dangMoMenu;

		public static bool khoamap;

		public static bool khoakhu;

		public static long canChangeZone;

		public static List<setDo1> ListSet1;

		public static List<setDo2> ListSet2;

		public static List<ItemAuto> listItemAuto;

		public static bool dangLogin;

		public static bool isAutoLogin;

		public static bool isAutoVeKhu;

		private static long currVeKhuCu;

		public static int khuVeLai;

		public static int csHP;

		public static int csKI;

		public static bool aBuffDau;

		private static long currBuffDau;

		public static bool aDauDeTu;

		private static long currDauDeTu;

		public static int csHPDeTu;

		public static int csKIDeTu;

		public static bool isAutoTTNL;

		private static long currAutoTTNL;

		public static string dau;

		public static string doHoa;

		public static string hoTroUpDe;

		public static string upYardrat;

		public static string hoTroSanBoss;

		public static string chucNangKhac;

		public static string tdLT;

		public static string autoSkill;

		public static List<Skill> listSkillsAuto;

		public static bool xindau;

		public static bool thudau;

		private static long currThuDau;

		public static bool chodau;

		private static long currXinDau;

		private static long currChoDau;

		private static long delayChoDau;

		public static bool isAutoNeBoss;

		private static long currNeBoss;

		private static long currAutoFlag;

		public static bool isAutoCo;

		public static bool isKOK;

		private static long currKOK;

		private static long lastTimeCheckLag;

		public static int currentCheckLag;

		public static bool isCheckLag;

		public static bool autoHoiSinh;

		private static long currAutoHoiSinh;

		public static bool isAutoAnNho;

		private static long currAnNho;

		public static int xGB;

		public static int yGB;

		public static bool IsGoBack;

		public static int IdmapGB;

		public static int ZoneGB;

		public static bool canUpdate;

		public static int tocdochay;

		public static bool clearMob;

		public static bool doBossNguoc;

		private static short idMap;

		private static short idNpc;

		public static int zoneMacDinhNguoc;

		public static bool iskepboss;

		private int zoneMatDinhNguoc = 11;

		public static long currkepboss;

		public static bool isbom;

		public static bool hideAll;

		public static bool chiso;

		public static bool isautonhat;

		public static int HPauto;

		public static int KIauto;

		public static int SDauto;

		public static int DEFauto;

		public static void GotoNpc(int npcID)
		{
			for (int i = 0; i < GameScr.vNpc.size(); i++)
			{
				Npc npc = (Npc)GameScr.vNpc.elementAt(i);
				if (npc.template.npcTemplateId == npcID && Math.abs(npc.cx - Char.myCharz().cx) >= 50)
				{
					GotoXY(npc.cx, npc.cy - 1);
					Char.myCharz().focusManualTo(npc);
					break;
				}
			}
		}

		public static void GotoXY(int x, int y)
		{
			Char.myCharz().cx = x;
			Char.myCharz().cy = y;
			Service.gI().charMove();
		}

		public static int FindYardrat()
		{
			for (int i = 0; i < Char.myCharz().arrItemBag.Length; i++)
			{
				if (Char.myCharz().arrItemBag[i] != null && Char.myCharz().arrItemBag[i].template.name.Contains("Yardrat"))
				{
					return i;
				}
			}
			return -1;
		}

		public static void TeleVip(int CharID)
		{
			Item[] arrItemBody = Char.myCharz().arrItemBody;
			if (arrItemBody[5] == null)
			{
				Service.gI().getItem(4, (sbyte)FindYardrat());
				Service.gI().gotoPlayer(CharID);
				Service.gI().getItem(5, 5);
			}
			if (arrItemBody[5].template.name.Contains("Yardrat"))
			{
				Service.gI().gotoPlayer(CharID);
			}
			else if (!arrItemBody[5].template.name.Contains("Yardrat"))
			{
				Service.gI().getItem(4, (sbyte)FindYardrat());
				Service.gI().gotoPlayer(CharID);
				Service.gI().getItem(4, (sbyte)FindYardrat());
			}
		}

		public static int FindIndexItem(int idItem)
		{
			for (int i = 0; i < Char.myCharz().arrItemBag.Length; i++)
			{
				if (Char.myCharz().arrItemBag[i] != null && Char.myCharz().arrItemBag[i].template.id == idItem)
				{
					return Char.myCharz().arrItemBag[i].indexUI;
				}
			}
			return -1;
		}

		public static void PhimTat(int key)
		{
			if (key != 0 && GameScr.gI().mobCapcha == null && TField.isQwerty && key == 120)
			{
				GameScr.gI().onChatFromMe("xmp", "xmp");
			}
		}

		public static void update()
		{
			//Time.timeScale = speed;
			Char.myCharz().cspeed = tocdochay;
			AutoTTNL();
			AutoHoiSinh();
			xd();
			UseSkillAuto();
			autoFocusBoss();
			KSBoss();
			KSBossBangSkill5();
			BuyItems();
			khoaViTri();
			gmt();
			AutoBT();
			AutoCTG();
			AutoNhatXa();
			if (!Pk9rXmap.IsXmapRunning)
			{
				Pk9rPickMob.Update();
			}
			AutoNhatXa();
			if (Pk9rXmap.IsXmapRunning && GameCanvas.isPointerClick && GameCanvas.isPointerJustRelease)
			{
				Pk9rXmap.IsXmapRunning = false;
			}
			if (isAutoAnNho && Char.myCharz().cStamina <= 5 && mSystem.currentTimeMillis() - currAnNho >= 1000)
			{
				AnNho();
				currAnNho = mSystem.currentTimeMillis();
			}
			if (mSystem.currentTimeMillis() - delayChoDau >= 31000)
			{
				for (int i = 0; i < 5; i++)
				{
					cd();
				}
				delayChoDau = mSystem.currentTimeMillis();
			}
			if (isCheckLag)
			{
				CheckLag();
			}
			if (isKOK && mSystem.currentTimeMillis() - currKOK >= 500)
			{
				currKOK = mSystem.currentTimeMillis();
				if (Char.myCharz().isCharge || Char.myCharz().isFlyAndCharge || Char.myCharz().isStandAndCharge || Char.myCharz().statusMe == 14 || Char.myCharz().cHP <= 0)
				{
					return;
				}
				Char.myCharz().cy--;
				Service.gI().charMove();
				Char.myCharz().cy++;
				Service.gI().charMove();
			}
			if (isAutoCo && mSystem.currentTimeMillis() - currAutoFlag >= 500)
			{
				if (!FlagInMap() && Char.myCharz().cFlag == 0)
				{
					Service.gI().getFlag(1, 8);
				}
				if (FlagInMap() && Char.myCharz().cFlag == 8)
				{
					Service.gI().getFlag(1, 0);
				}
				currAutoFlag = mSystem.currentTimeMillis();
			}
			if (isAutoNeBoss && mSystem.currentTimeMillis() - currNeBoss >= 17000)
			{
				NeBoss();
				currNeBoss = mSystem.currentTimeMillis();
			}
			if (thudau && mSystem.currentTimeMillis() - currThuDau >= 500)
			{
				td();
				currThuDau = mSystem.currentTimeMillis();
			}
			if (aDauDeTu && mSystem.currentTimeMillis() - currDauDeTu >= 1000)
			{
				if ((csHPDeTu > Char.myPetz().cHP || csKIDeTu > Char.myPetz().cMP) && Char.myPetz().cHP > 0 && myPetInMap() != null)
				{
					GameScr.gI().doUseHP();
				}
				currDauDeTu = mSystem.currentTimeMillis();
			}
			if (aBuffDau && mSystem.currentTimeMillis() - currBuffDau >= 1000)
			{
				if ((csHP > Char.myCharz().cHP || csKI > Char.myCharz().cMP) && Char.myCharz().cHP > 0)
				{
					GameScr.gI().doUseHP();
				}
				currBuffDau = mSystem.currentTimeMillis();
			}
			if (isAutoVeKhu && mSystem.currentTimeMillis() - currVeKhuCu >= 20000)
			{
				currVeKhuCu = mSystem.currentTimeMillis();
				khuVeLai = TileMap.zoneID;
			}
			if (doBoss && mSystem.currentTimeMillis() - currDoBoss > 11000)
			{
				DoBoss();
				currDoBoss = mSystem.currentTimeMillis();
			}
			else if (doBossNguoc && mSystem.currentTimeMillis() - currDoBoss > 11000)
			{
				DoBossNguoc();
				currDoBoss = mSystem.currentTimeMillis();
			}
			if (isNoitai)
			{
				ntNow = int.Parse(CutString(Panel.specialInfo.Substring(0, Panel.specialInfo.IndexOf('%')).LastIndexOf(' ') + 1, Panel.specialInfo.IndexOf('%'), Panel.specialInfo));
			}
			if (hoiSinhNgoc && mSystem.currentTimeMillis() - currHoiSinh >= 1000)
			{
				HoiSinhTheoNgocChiDinh();
				currHoiSinh = mSystem.currentTimeMillis();
			}
			if (achat && mSystem.currentTimeMillis() - currAutoChat >= 2000)
			{
				AutoChat();
				currAutoChat = mSystem.currentTimeMillis();
			}
			if ((isThuongDeThuong) && mSystem.currentTimeMillis() - currThuongDe >= 1000)// || isThuongDeVip
			{
				quayThuongDe();
				currThuongDe = mSystem.currentTimeMillis();
			}
			if (mSystem.currentTimeMillis() - currUpdateKhu >= 200)
			{
				currUpdateKhu = mSystem.currentTimeMillis();
				useItem();
				if (isUpdateKhu && !TileMap.mapName.Contains("Nhà") && !(TileMap.mapName == "Cổng phi thuyền") && !(TileMap.mapName == "Phòng chờ") && !(TileMap.mapName == "Thánh địa Kaio") && !(TileMap.mapName == "Thần điện") && !(TileMap.mapName == "Rừng Karin") && !(TileMap.mapName == "Tháp Karin") && !(TileMap.mapName == "Phòng chỉ huy") && !(TileMap.mapName == "Đấu trường") && !TileMap.mapName.Contains("Cửa Ải") && !TileMap.mapName.Contains("Trại độc nhãn") && !TileMap.mapName.Contains("Tường thành") && !TileMap.mapName.Contains("Tầng") && !(TileMap.mapName == "Hành tinh Bill") && !(TileMap.mapName == "Hành tinh Kaio") && !(TileMap.mapName == "Lãnh địa bang hội"))
				{
					Service.gI().openUIZone();
				}
				if (Char.myCharz().havePet)
				{
					Service.gI().petInfo();
					Service.gI().petInfo2();
				}
			}
			if (isAK)
			{
				Ak();
			}
			if (isDapDo && doDeDap != null)
			{
				saoHienTai = soSao(findItemBagWithIndexUI(doDeDap.indexUI));
			}
			else
			{
				saoHienTai = -1;
			}
			if (!isPKM || isGMT || (Char.myCharz().charFocus != null && (Char.myCharz().charFocus == null || Char.myCharz().isMeCanAttackOtherPlayer(Char.myCharz().charFocus))))
			{
				return;
			}
			for (int j = 0; j < GameScr.vCharInMap.size(); j++)
			{
				Char @char = (Char)GameScr.vCharInMap.elementAt(j);
				if (@char != null && Char.myCharz().isMeCanAttackOtherPlayer(@char) && !@char.isPet && !@char.isMiniPet && !@char.cName.StartsWith("$") && !@char.cName.StartsWith("#") && @char.charID >= 0)
				{
					Char.myCharz().focusManualTo(@char);
					break;
				}
			}
		}

		public static void Paint(mGraphics g)
		{
			if (hideAll)
			{
				return;
			}
			int num = 10;
			int num2 = 185;
			int vitri = 10;
			if (VuDang.isAutoLogin)
			{
				mFont.tahoma_7.drawString(g, "Auto Login: ON", 20, (GameCanvas.h / 3) + vitri, mFont.LEFT);
				vitri += 10;
			}
			if (VuDang.isAK)
			{
				mFont.tahoma_7.drawString(g, "Tự Động Đánh: ON", 20, (GameCanvas.h / 3) + vitri, mFont.LEFT);
				vitri += 10;
			}
			if (Pk9rPickMob.IsTanSat)
			{
				mFont.tahoma_7.drawString(g, "Tàn sát quái: ON", 20, (GameCanvas.h / 3) + vitri, mFont.LEFT);
				vitri += 10;
			}
			if (GameScr.aNhat)
			{
				mFont.tahoma_7.drawString(g, "Auto Nhặt: ON", 20, (GameCanvas.h / 3) + vitri, mFont.LEFT);
				vitri += 10;
			}
			if (GameScr.autoHoiSinh)
			{
				mFont.tahoma_7.drawString(g, "Auto Hồi Sinh: ON", 20, (GameCanvas.h / 3) + vitri, mFont.LEFT);
				vitri += 10;
			}
			num2 -= 10;
			mFont.tahoma_7b_yellowSmall2.drawString(g, NinjaUtil.NumberText(Char.myCharz().cHP), 85, 5, mFont.LEFT);
			mFont.tahoma_7b_yellowSmall2.drawString(g, NinjaUtil.NumberText(Char.myCharz().cMP), 85, 17, mFont.LEFT);

			mFont.tahoma_7_white.drawString(g, string.Concat(new object[]
			{
				TileMap.mapName,
				"(",
				TileMap.mapID,
				")",
				" [Khu ",
				TileMap.zoneID,
				"]"
			}), 85, 30, mFont.LEFT, mFont.tahoma_7b_dark);

			if (isThongTinSuPhu)
			{
				mFont.tahoma_7b_red.drawString(g, "Sư Phụ:", num, GameCanvas.h - 160, mFont.LEFT);
				mFont.tahoma_7.drawString(g, "Sức Mạnh: " + NinjaUtil.getMoneys(Char.myCharz().cPower), num, GameCanvas.h - 150, mFont.LEFT);
				mFont.tahoma_7.drawString(g, "Tiềm Năng: " + NinjaUtil.getMoneys(Char.myCharz().cTiemNang), num, GameCanvas.h - 140, mFont.LEFT);
				mFont.tahoma_7.drawString(g, string.Concat(new object[2]
				{
				"Sức Đánh: ",
				NinjaUtil.getMoneys(Char.myCharz().cDamFull)
				}), num, GameCanvas.h - 130, mFont.LEFT);
				mFont.tahoma_7.drawString(g, "HP: " + NinjaUtil.getMoneys(Char.myCharz().cHPFull), num, GameCanvas.h - 120, mFont.LEFT);
				mFont.tahoma_7.drawString(g, "MP: " + NinjaUtil.getMoneys(Char.myCharz().cMPFull), num, GameCanvas.h - 110, mFont.LEFT);
				num += GameCanvas.w / 4;
			}
			if (isThongTinDeTu)
			{
				mFont.tahoma_7b_red.drawString(g, "Đệ Tử:", num, GameCanvas.h - 160, mFont.LEFT);
				mFont.tahoma_7.drawString(g, "Sức Mạnh: " + NinjaUtil.getMoneys(Char.myPetz().cPower), num, GameCanvas.h - 150, mFont.LEFT);
				mFont.tahoma_7.drawString(g, "Tiềm Năng: " + NinjaUtil.getMoneys(Char.myPetz().cTiemNang), num, GameCanvas.h - 140, mFont.LEFT);
				mFont.tahoma_7.drawString(g, string.Concat(new object[2]
				{
				"Sức Đánh: ",
				NinjaUtil.getMoneys(Char.myPetz().cDamFull)
				}), num, GameCanvas.h - 130, mFont.LEFT);
				mFont.tahoma_7.drawString(g, "HP: " + NinjaUtil.getMoneys(Char.myPetz().cHP), num, GameCanvas.h - 120, mFont.LEFT);
				mFont.tahoma_7.drawString(g, "MP: " + NinjaUtil.getMoneys(Char.myPetz().cMP), num, GameCanvas.h - 110, mFont.LEFT);
				num += GameCanvas.w / 4;
			}
			if (thongBaoBoss)
			{
				int num3 = 35;
				for (int i = 0; i < bossVip.size(); i++)
				{
					g.setColor(2721889, 0.5f);
					g.fillRect(GameCanvas.w - 18, num3 + 2, 20, 9);
					((ShowBoss)bossVip.elementAt(i)).paintBoss(g, GameCanvas.w - 2, num3, mFont.RIGHT);
					num3 += 10;
				}
			}
			if (trangThai)
			{
				for (int j = 0; j < GameScr.vCharInMap.size(); j++)
				{
					int num4 = 72;
					Char @char = (Char)GameScr.vCharInMap.elementAt(j);
					if (@char != null && @char == Char.myCharz().charFocus)
					{
						mFont.tahoma_7b_red.drawString(g, string.Concat(new object[4]
						{
						@char.cName,
						" [",
						NinjaUtil.getMoneys(@char.cHP),
						" ]"
						}), 10, GameCanvas.h - 250, mFont.LEFT);
						num4 += 10;
						if (@char.isFreez)
						{
							mFont.tahoma_7b_red.drawString(g, "Bị TDHS: " + @char.freezSeconds, 10, GameCanvas.h - 235, mFont.LEFT);
							num4 += 10;
						}
					}
				}
			}
			if (isBossM)
			{
				int num5 = 80;
				for (int k = 0; k < GameScr.vCharInMap.size(); k++)
				{
					Char char2 = (Char)GameScr.vCharInMap.elementAt(k);
					if (char2 != null && char2.cTypePk == 5 && !char2.isMiniPet)
					{
						mFont.tahoma_7b_red.drawString(g, char2.cName + " [" + NinjaUtil.getMoneys(char2.cHP).ToString() + "/" + NinjaUtil.NumberText(char2.cHPFull).ToString() + "]", GameCanvas.w / 2, num5, mFont.CENTER);
						num5 -= 10;
					}
				}
			}
			if (isPKM)
			{
				mFont.tahoma_8b.drawString(g, "Địch trong khu:", GameCanvas.w / 2, 72, mFont.CENTER);
				int num6 = 82;
				Char char3 = null;
				for (int l = 0; l < GameScr.vCharInMap.size(); l++)
				{
					Char char4 = (Char)GameScr.vCharInMap.elementAt(l);
					if (char4 != null && Char.myCharz().isMeCanAttackOtherPlayer(char4))
					{
						if (Char.myCharz().charFocus != null && Char.myCharz().charFocus == char4)
						{
							char3 = char4;
						}
						g.setColor(Color.red);
						g.drawLine(Char.myCharz().cx - GameScr.cmx, Char.myCharz().cy - GameScr.cmy, char4.cx - GameScr.cmx, char4.cy - GameScr.cmy);
						mFont.tahoma_7b_red.drawString(g, l + " - " + (char4.isPet ? "$" : "") + (char4.isMiniPet ? "#" : "") + char4.cName + "[" + NinjaUtil.getMoneys(char4.cHP).ToString() + " / " + NinjaUtil.getMoneys(char4.cHPFull).ToString() + " ] [ " + hanhTinhNhanVat(char4) + " ]", GameCanvas.w / 2, num6, mFont.CENTER);
						num6 += 10;
					}
				}
				if (char3 != null)
				{
					g.setColor(Color.green);
					g.drawLine(Char.myCharz().cx - GameScr.cmx, Char.myCharz().cy - GameScr.cmy, char3.cx - GameScr.cmx, char3.cy - GameScr.cmy);
				}
			}
			if (doBoss)
			{
				if (GameCanvas.isPointerClick && GameCanvas.isPointerJustRelease)
				{
					GameScr.info1.addInfo("Đã tắt auto dò boss", 0);
					doBoss = false;
					doBossNguoc = false;
				}
				else if (TileMap.zoneID == 14)
				{
					GameScr.info1.addInfo("Đã hết dò hết khu", 0);
					doBoss = false;
				}
			}
			else if (doBossNguoc)
			{
				if (GameCanvas.isPointerClick && GameCanvas.isPointerJustRelease)
				{
					GameScr.info1.addInfo("Đã tắt auto dò boss", 0);
					doBoss = false;
					doBossNguoc = false;
				}
				else if (TileMap.zoneID == 1)
				{
					GameScr.info1.addInfo("Đã hết dò hết khu", 0);
					doBossNguoc = false;
				}
			}
			if (isDapDo)
			{
				mFont.tahoma_7b_dark.drawString(g, (doDeDap != null) ? doDeDap.template.name : "Chưa Có", GameCanvas.w / 2, 72, mFont.CENTER);
				mFont.tahoma_7b_dark.drawString(g, (doDeDap != null) ? ("Số Sao: " + saoHienTai) : "Số Sao: -1", GameCanvas.w / 2, 82, mFont.CENTER);
				mFont.tahoma_7b_dark.drawString(g, "Số Sao Cần Đập: " + soSaoCanDap + " Sao", GameCanvas.w / 2, 92, mFont.CENTER);
			}
			if (isDapDo || isThuongDeThuong)// || isThuongDeVip
			{
				mFont.tahoma_7b_dark.drawString(g, "Ngọc Xanh: " + NinjaUtil.getMoneys(Char.myCharz().luong) + " Ngọc Hồng: " + NinjaUtil.getMoneys(Char.myCharz().luongKhoa), GameCanvas.w / 2, 102, mFont.CENTER);
				mFont.tahoma_7b_dark.drawString(g, "Vàng: " + NinjaUtil.getMoneys(Char.myCharz().xu) + " Thỏi Vàng: " + thoiVang(), GameCanvas.w / 2, 112, mFont.CENTER);
			}
			if (nvat)
			{
				int num7 = 90;
				for (int m = 0; m < chars.Length; m++)
				{
					chars[m] = null;
				}
				int num8 = 0;
				int stt = 1;
				for (int n = 0; n < GameScr.vCharInMap.size(); n++)
				{
					Char char5 = (Char)GameScr.vCharInMap.elementAt(n);
					if (stt < 11 && char5 != null && !char5.isMiniPet && !char5.isPet)
					{
						g.fillRect(GameCanvas.w - 155, num7 + 2, 120, 8, 12566453, 90);
						if (char5 == Char.myCharz().charFocus && char5.cTypePk != 5)
						{
							mFont.tahoma_7b_red.drawString(g, stt + ". " + char5.cName + " [" + NinjaUtil.NumberText(char5.cHP).ToString() + " - " + hanhTinhNhanVat(char5) + "]", GameCanvas.w - 150, num7, mFont.LEFT);
						}
						else if (char5 == Char.myCharz().charFocus && char5.cTypePk == 5)
						{
							mFont.tahoma_7b_red.drawString(g, stt + ". " + char5.cName + " [" + NinjaUtil.NumberText(char5.cHP).ToString() + " - " + hanhTinhNhanVat(char5) + "]", GameCanvas.w - 150, num7, mFont.LEFT);
						}
						else if (char5.cTypePk == 5 || (char5.charID < 0 && char5.charID > -1000 && char5.charID != -114))
						{
							mFont.tahoma_7b_yellowSmall.drawString(g, stt + ". " + char5.cName + " [" + NinjaUtil.NumberText(char5.cHP).ToString() + " - " + hanhTinhNhanVat(char5) + "]", GameCanvas.w - 150, num7, mFont.LEFT);
						}
						else if (char5.clanID == Char.myCharz().clanID)
						{
							mFont.tahoma_7_blue1.drawString(g, stt + "." + char5.cName + " [" + NinjaUtil.NumberText(char5.cHP).ToString() + " - " + hanhTinhNhanVat(char5) + "]", GameCanvas.w - 150, num7, mFont.LEFT);
						}
						else
						{
							mFont.tahoma_7.drawString(g, stt + ". " + char5.cName + " [" + NinjaUtil.NumberText(char5.cHP).ToString() + " - " + hanhTinhNhanVat(char5) + "]", GameCanvas.w - 150, num7, mFont.LEFT);
						}
						stt++;
						num8++;
						chars[n] = char5;
						num7 += 8;
					}
				}
			}
			if (!lineboss)
			{
				return;
			}
			for (int num9 = 0; num9 < GameScr.vCharInMap.size(); num9++)
			{
				Char char6 = (Char)GameScr.vCharInMap.elementAt(num9);
				if (char6.cTypePk == 5)
				{
					if (Char.myCharz().charFocus == char6)
					{
						g.setColor(Color.green);
						g.drawLine(Char.myCharz().cx - GameScr.cmx, Char.myCharz().cy - GameScr.cmy, char6.cx - GameScr.cmx, char6.cy - GameScr.cmy);
					}
					else
					{
						g.setColor(Color.red);
						g.drawLine(Char.myCharz().cx - GameScr.cmx, Char.myCharz().cy - GameScr.cmy, char6.cx - GameScr.cmx, char6.cy - GameScr.cmy);
					}
				}
			}
		}

		public static void autocs()
		{
			int num = HPauto;
			int num2 = KIauto;
			int num3 = SDauto;
			int num4 = DEFauto;
			while (chiso)
			{
				if (GameCanvas.isPointerClick && GameCanvas.isPointerJustRelease)
				{
					GameScr.info1.addInfo("Đã tắt auto cộng chỉ số", 0);
					chiso = false;
				}
				else if (Char.myCharz().cHPGoc < num - 2000)
				{
					if (Char.myCharz().cTiemNang >= Char.myCharz().cHPGoc * 1000 + 199000)
					{
						Service.gI().upPotential(0, 100);
					}
					else
					{
						GameScr.info1.addInfo("Hết tiềm năng", 0);
						chiso = false;
					}
				}
				else if (Char.myCharz().cMPGoc < num2 - 2000)
				{
					if (Char.myCharz().cTiemNang >= Char.myCharz().cMPGoc * 1000 + 199000)
					{
						Service.gI().upPotential(1, 100);
					}
					else
					{
						GameScr.info1.addInfo("Hết tiềm năng", 0);
						chiso = false;
					}
				}
				else if (Char.myCharz().cDamGoc < num3 - 100)
				{
					if (Char.myCharz().cTiemNang >= Char.myCharz().cDamGoc * 10000 + 495000)
					{
						Service.gI().upPotential(2, 100);
					}
					else
					{
						GameScr.info1.addInfo("Hết tiềm năng", 0);
						chiso = false;
					}
				}
				else if (Char.myCharz().cDefGoc < num4 - 1)
				{
					Service.gI().upPotential(3, 100);
				}
				else
				{
					GameScr.info1.addInfo("Đã dừng Auto cộng chỉ số", 0);
					chiso = false;
				}
				Thread.Sleep(200);
			}
			GameScr.info1.addInfo("Đã cộng xong", 0);
			chiso = false;
		}

		public static void Ak()
		{
			if (clearMob)
			{
				if (!Char.myCharz().stone && !Char.isLoadingMap && !Char.myCharz().meDead && Char.myCharz().statusMe != 14 && Char.myCharz().statusMe != 5 && Char.myCharz().myskill.template.type != 3 && Char.myCharz().myskill.template.id != 10 && Char.myCharz().myskill.template.id != 11 && !Char.myCharz().myskill.paintCanNotUseSkill)
				{
					getSkill();
					if (GameScr.gI().isMeCanAttackMob(Char.myCharz().mobFocus) && (double)Res.abs(Char.myCharz().mobFocus.xFirst - Char.myCharz().cx) < (double)Char.myCharz().myskill.dx * 1.5)
					{
						AkMob();
					}
				}
			}
			else
			{
				if (Char.myCharz().stone || Char.isLoadingMap || Char.myCharz().meDead || Char.myCharz().statusMe == 14 || Char.myCharz().statusMe == 5 || Char.myCharz().myskill.template.type == 3 || Char.myCharz().myskill.template.id == 10 || Char.myCharz().myskill.template.id == 11 || Char.myCharz().myskill.paintCanNotUseSkill)
				{
					return;
				}
				int skill = getSkill();
				if (mSystem.currentTimeMillis() - currTimeAK[skill] > getTimeSkill(Char.myCharz().myskill))
				{
					if (GameScr.gI().isMeCanAttackMob(Char.myCharz().mobFocus) && (double)Res.abs(Char.myCharz().mobFocus.xFirst - Char.myCharz().cx) < (double)Char.myCharz().myskill.dx * 1.5)
					{
						Char.myCharz().myskill.lastTimeUseThisSkill = mSystem.currentTimeMillis();
						AkMob();
						currTimeAK[skill] = mSystem.currentTimeMillis();
					}
					else if (Char.myCharz().charFocus != null && Char.myCharz().isMeCanAttackOtherPlayer(Char.myCharz().charFocus) && (double)Res.abs(Char.myCharz().charFocus.cx - Char.myCharz().cx) < (double)Char.myCharz().myskill.dx * 1.5)
					{
						Char.myCharz().myskill.lastTimeUseThisSkill = mSystem.currentTimeMillis();
						AkChar();
						currTimeAK[skill] = mSystem.currentTimeMillis();
					}
				}
			}
		}

		public static void AkChar()
		{
			try
			{
				MyVector myVector = new MyVector();
				myVector.addElement(Char.myCharz().charFocus);
				Service.gI().sendPlayerAttack(new MyVector(), myVector, 2);
				Char.myCharz().cMP -= Char.myCharz().myskill.manaUse;
			}
			catch
			{
			}
		}

		public static void AutoAKForDT()
		{
			try
			{
				Char.myCharz().focusManualTo(GameScr.findMobInMap(0));
				MyVector myVector = new MyVector();
				myVector.addElement(Char.myCharz().mobFocus);
				Service.gI().sendPlayerAttack(myVector, new MyVector(), 1);
				Char.myCharz().cMP -= Char.myCharz().myskill.manaUse;
			}
			catch
			{
			}
		}

		public static void AkMob()
		{
			try
			{
				MyVector myVector = new MyVector();
				myVector.addElement(Char.myCharz().mobFocus);
				Service.gI().sendPlayerAttack(myVector, new MyVector(), 1);
				Char.myCharz().cMP -= Char.myCharz().myskill.manaUse;
			}
			catch
			{
			}
		}

		private static int getSkill()
		{
			for (int i = 0; i < GameScr.keySkill.Length; i++)
			{
				if (GameScr.keySkill[i] == Char.myCharz().myskill)
				{
					return i;
				}
			}
			return 0;
		}

		public static long getTimeSkill(Skill s)
		{
			if (s.template.id == 29 || s.template.id == 22 || s.template.id == 7 || s.template.id == 18 || s.template.id == 23)
			{
				return (long)s.coolDown + 500L;
			}
			long num = (long)((double)s.coolDown * 1.2);
			if (num < 406)
			{
				return 406L;
			}
			return num;
		}

		public static void LoadGame()
		{
			petStatus = 1;
			listSkillsAuto.Clear();
			listItemAuto.Clear();
		}

		public static void AutoDapDo()
		{
			while (isDapDo)
			{
				if (soSaoCanDap != -1 && GameCanvas.isPointerClick && GameCanvas.isPointerJustRelease)
				{
					GameScr.info1.addInfo("Đồ để đập đã reset hãy add đồ", 0);
					soSaoCanDap = -1;
					doDeDap = null;
				}
				if (TileMap.mapID != 5 && !Pk9rXmap.IsXmapRunning && Char.myCharz().xu > 120000000 && Char.myCharz().luong > 50)
				{
					XmapController.StartRunToMapId(5);
				}
				while (TileMap.mapID != 5)
				{
					Thread.Sleep(100);
				}
				if (saoHienTai >= soSaoCanDap && doDeDap != null && saoHienTai >= 0 && soSaoCanDap > 0)
				{
					GameScr.info1.addInfo("Đồ Cần Đập Đã Đạt Số Sao Yêu Cầu", 0);
					soSaoCanDap = -1;
					doDeDap = null;
				}
				if (Char.myCharz().xu > 300000000 && Char.myCharz().luong > 50)
				{
					GotoNpc(21);
					if (doDeDap != null && soSaoCanDap > 0)
					{
						while (!GameCanvas.menu.showMenu)
						{
							Service.gI().combine(1, GameCanvas.panel.vItemCombine);
							Thread.Sleep(100);
						}
						Service.gI().confirmMenu(21, 0);
						GameCanvas.menu.doCloseMenu();
						GameCanvas.panel.currItem = null;
						GameCanvas.panel.chatTField.isShow = false;
					}
				}
				//else if (isbanvang)
				//{
				//	isDapDo = false;
				//	BanVang();
				//	isDapDo = true;
				//}
				else
				{
					isDapDo = false;
					GameScr.info1.addInfo("Hết vàng hoặc ngọc", 0);
					soSaoCanDap = -1;
					doDeDap = null;
				}
				Thread.Sleep(100);
			}
		}

		public static void BanVang()
		{
			dangBanVang = true;
			if (TileMap.mapID != 5)
			{
				XmapController.StartRunToMapId(5);
				Thread.Sleep(1000);
			}
			while (TileMap.mapID != 5)
			{
				Thread.Sleep(500);
			}
			if (GameCanvas.isPointerClick && GameCanvas.isPointerJustRelease)
			{
				GameScr.info1.addInfo("Dừng bán vàng", 0);
				dangBanVang = false;
				return;
			}
			while (dangBanVang == true && Char.myCharz().xu < 2000000000 && !GameCanvas.isPointerClick && !GameCanvas.isPointerJustRelease)
			{
				for (int i = 0; i < Char.myCharz().arrItemBag.Length; i++)
				{
					Item item = Char.myCharz().arrItemBag[i];
					if (item != null && item.template.id == 457)
					{
						Service.gI().saleItem(1, 1, (short)FindIndexItem(457));
						Thread.Sleep(200);
						continue;
					}
					if (item == null || item.template.id != 457)
					{
						GameScr.info1.addInfo("Hết vàng", 0);
						if (isDapDo)
						{
							isDapDo = false;
							GameScr.info1.addInfo("Đập đồ đã tắt do bạn quá nghèo :v", 0);
						}
						dangBanVang = false;
						return;
					}
					Service.gI().saleItem(1, 1, (short)FindIndexItem(457));
					Thread.Sleep(200);
				}
			}
			GameScr.info1.addInfo("Đã bán xong", 0);
			Thread.Sleep(200);
			dangBanVang = false;
		}

		public static void autonhat()
		{
			while (isautonhat)
			{
				Char.myCharz().searchItem();
				if (Char.myCharz().itemFocus != null)
				{
					GameScr.gI().pickItem();
				}
				Thread.Sleep(200);
			}
		}

		public static long soSao(Item item)
		{
			for (int i = 0; i < item.itemOption.Length; i++)
			{
				if (item.itemOption[i].optionTemplate.id == 107)
				{
					return item.itemOption[i].param;
				}
			}
			return 0;
		}

		public static int thoiVang()
		{
			int num = 0;
			for (int i = 0; i < Char.myCharz().arrItemBag.Length; i++)
			{
				Item item = Char.myCharz().arrItemBag[i];
				if (item != null && item.template.id == 457)
				{
					num += item.quantity;
				}
			}
			return num;
		}

		public static string saoTrongBalo(Item item)
		{
			if ((item != null && item.template.type <= 5) || item.template.type == 32)
			{
				return soSao(item) + " Sao";
			}
			return "";
		}

		public static Item findItemBagWithIndexUI(int index)
		{
			Item[] arrItemBag = Char.myCharz().arrItemBag;
			Item[] array = arrItemBag;
			foreach (Item item in array)
			{
				if (item != null && item.indexUI == index)
				{
					return item;
				}
			}
			return null;
		}

		public static void VaoKhu(object okhu)
		{
			try
			{
				isVaoKhu = true;
				int zoneID = TileMap.zoneID;
				int mapID = TileMap.mapID;
				int num = (int)okhu;
				if (isVaoKhu)
				{
					GameScr.info1.addInfo("Vào Khu: " + num, 0);
				}
				while (TileMap.zoneID == zoneID && TileMap.mapID == mapID && TileMap.zoneID != num)
				{
					if (GameCanvas.isPointerClick && GameCanvas.isPointerJustRelease)
					{
						GameScr.info1.addInfo("Đã dừng auto vào khu", 0);
						isVaoKhu = false;
						return;
					}
					if (!isVaoKhu)
					{
						return;
					}
					if (GameScr.gI().numPlayer[num] < GameScr.gI().maxPlayer[num])
					{
						Service.gI().requestChangeZone(num, -1);
					}
					Thread.Sleep(1);
				}
				isVaoKhu = false;
			}
			catch (Exception)
			{
				isVaoKhu = false;
			}
		}

		public static string hanhTinhNhanVat(Char @char)
		{
			if (@char.cTypePk == 5)
			{
				return "BOSS";
			}
			if (@char.cgender == 0)
			{
				return "TD";
			}
			if (@char.cgender == 1)
			{
				return "NM";
			}
			if (@char.cgender == 2)
			{
				return "XD";
			}
			return "";
		}
		public static void quayThuongDe()
		{
			if (dangBanVang || dangNhanAll || dangMoMenu)
			{
				return;
			}
			if (!isPaintCrackBall && TileMap.mapID == 45)
			{
				new Thread(OpenMenuThuongDe).Start();
			}
			else if (TileMap.mapID != 45 && !Pk9rXmap.IsXmapRunning)
			{
				XmapController.StartRunToMapId(45);
			}
			else if (TileMap.mapID == 45)
			{
				if (Input.GetKey("q") || GameCanvas.isPointerClick && GameCanvas.isPointerJustRelease)
				{
					GameScr.info1.addInfo("Auto đã tắt", 0);
					//isThuongDeVip = false;
					isThuongDeThuong = false;
				}
				else if (Char.myCharz().xu <= 175000000 && isThuongDeThuong)
				{
					new Thread(BanVang).Start();
				}
				else
				{
					GotoNpc(19);
					Service.gI().openMenu(19);
					Service.gI().SendCrackBall(2, 7);
				}
			}
		}
		public static void NhanAllThuongDe()
		{
			dangNhanAll = true;
			Service.gI().openMenu(19);
			Service.gI().confirmMenu(19, 2);
			Service.gI().confirmMenu(19, 1);
			Service.gI().buyItem(2, 0, 0);
			dangNhanAll = false;
		}

		private static void OpenMenuThuongDe()
		{
			dangMoMenu = true;
			Service.gI().openMenu(19);
			Service.gI().confirmMenu(19, 2);
			if (isThuongDeThuong)
			{
				Service.gI().confirmMenu(19, 0);
			}
			//if (isThuongDeVip)
			//{
			//	Service.gI().confirmMenu(19, 1);
			//}
			dangMoMenu = false;
		}

		public static void AutoChat()
		{
			if (!string.IsNullOrEmpty(textAutoChat))
			{
				Service.gI().chat(textAutoChat);
			}
			else
			{
				GameScr.info1.addInfo("Chưa cài nội dung auto chat", 0);
			}
		}

		public static void AutoCTG()
		{
			if (isAutoCTG && mSystem.currentTimeMillis() - currAutoCTG >= 5000)
			{
				currAutoCTG = mSystem.currentTimeMillis();
				if (!string.IsNullOrEmpty(textAutoChatTG))
				{
					Service.gI().chatGlobal(textAutoChatTG);
				}
				else
				{
					GameScr.info1.addInfo("Chưa cài nội dung auto chat thế giới", 0);
				}
			}
		}

		public static void AutoNhatXa()
		{
			if (!isAutoNhatXa || mSystem.currentTimeMillis() - currNhatXa < 2000)
			{
				return;
			}
			currNhatXa = mSystem.currentTimeMillis();
			for (int i = 0; i < GameScr.vItemMap.size(); i++)
			{
				ItemMap itemMap = (ItemMap)GameScr.vItemMap.elementAt(i);
				if (itemMap != null && itemMap.itemMapID == Char.myCharz().charID)
				{
					GotoXY(itemMap.x, itemMap.y);
					Service.gI().pickItem(itemMap.itemMapID);
					GotoXY(xNhatXa, yNhatXa);
					break;
				}
				if (itemMap != null)
				{
					GotoXY(itemMap.x, itemMap.y);
					Service.gI().pickItem(itemMap.itemMapID);
					GotoXY(xNhatXa, yNhatXa);
					break;
				}
			}
		}

		public static void BongTai()
		{
			for (int i = 0; i < Char.myCharz().arrItemBag.Length; i++)
            {
                if (Char.myCharz().arrItemBag[i] != null && (Char.myCharz().arrItemBag[i].template.id == 921
                    || Char.myCharz().arrItemBag[i].template.id == 454 || Char.myCharz().arrItemBag[i].template.id == 1165
                    || Char.myCharz().arrItemBag[i].template.id == 1129 || Char.myCharz().arrItemBag[i].template.id == 1999
                    || Char.myCharz().arrItemBag[i].template.id == 1550 || Char.myCharz().arrItemBag[i].template.id == 1944
                     || Char.myCharz().arrItemBag[i].template.id == 1945 || Char.myCharz().arrItemBag[i].template.id == 1946))
                {
					Service.gI().useItem(0, 1, (sbyte)i, -1);
					Service.gI().petStatus(petStatus);
					return;
				}
			}
			GameScr.info1.addInfo("Đéo có bông tai", 0);
		}

		public static void AutoBT()
		{
			if (isAutoBT && mSystem.currentTimeMillis() - currAutoBT >= timeBT * 1000)
			{
				currAutoBT = mSystem.currentTimeMillis();
				BongTai();
				if (Char.myCharz().isNhapThe)
				{
					BongTai();
				}
			}
		}

		public static void gmt()
		{
			if (isGMT && GameScr.findCharInMap(charMT.charID) != null)
			{
				Char.myCharz().focusManualTo(GameScr.findCharInMap(charMT.charID));
			}
		}

		public static Color GetColor()
		{
			string[] array = backgroundColor.Split(' ');
			return new Color(float.Parse(array[0]), float.Parse(array[1]), float.Parse(array[2]));
		}

		public static bool MapNRD()
		{
			if (TileMap.mapID >= 85)
			{
				return TileMap.mapID <= 91;
			}
			return false;
		}

		public static int GetIDMap(string mapName)
		{
			int result = -1;
			for (int i = 0; i < MapNames.Length; i++)
			{
				if (MapNames[i].Trim().ToLower().Equals(mapName.Trim().ToLower()))
				{
					result = i;
				}
			}
			return result;
		}

		public static int MapID(string a)
		{
			for (int i = 0; i < TileMap.mapNames.Length; i++)
			{
				if (TileMap.mapNames[i].Trim().ToLower() == a.Trim().ToLower())
				{
					return i;
				}
			}
			return -1;
		}

		public static void khoaViTri()
		{
			if (isKhoaViTri && mSystem.currentTimeMillis() - currKhoaViTri >= 600 && Char.myCharz().statusMe != 14 && Char.myCharz().cHP > 0)
			{
				currKhoaViTri = mSystem.currentTimeMillis();
				Service.gI().charMove();
			}
		}

		public static sbyte typeBuyItem()
		{
			if (GameCanvas.panel.currItem.buyCoin > 0)
			{
				return 0;
			}
			if (GameCanvas.panel.currItem.buyGold > 0)
			{
				return 1;
			}
			if (GameCanvas.panel.currItem.buySpec > 0)
			{
				return 3;
			}
			return -1;
		}

		public static void BuyItems()
		{
			if (itemBuy == null || countBuyItem <= 0 || mSystem.currentTimeMillis() - currBuyItem < 100)
			{
				return;
			}
			currBuyItem = mSystem.currentTimeMillis();
			if (GameCanvas.isPointerClick && GameCanvas.isPointerJustRelease)
			{
				countBuyItem = 0;
				itemBuy = null;
				GameScr.info1.addInfo("Dừng Mua Item", 0);
				return;
			}
			Service.gI().buyItem(typeBuyItem(), itemBuy.template.id, 0);
			countBuyItem--;
			if (countBuyItem == 0)
			{
				GameScr.info1.addInfo("Đã Mua Xong !", 0);
				itemBuy = null;
			}
		}

		public static void KSBoss()
		{
			if (!isKSBoss)
			{
				return;
			}
			for (int i = 0; i < GameScr.vCharInMap.size(); i++)
			{
				Char @char = (Char)GameScr.vCharInMap.elementAt(i);
				if (HPKSBoss == 0)
				{
					isKSBoss = false;
					GameScr.info1.addInfo("HP Boss = 0 thì ks sao ba =))", 0);
					GameScr.info1.addInfo("KS Boss đã tắt", 0);
					break;
				}
				if (@char != null && @char.charID < 0 && @char.cTypePk == 5 && !@char.cName.StartsWith("Đ") && @char.cHP <= HPKSBoss && @char.cHP > 0)
				{
					GetSkillByIconID(539);
					if (Math.abs(@char.cx - Char.myCharz().cx) >= 25)
					{
						GotoXY(@char.cx, @char.cy - 1);
					}
					Char.myCharz().focusManualTo(@char);
					Ak();
				}
			}
		}

		private static sbyte ulti()
		{
			if (Char.myCharz().cgender == 0)
			{
				return 10;
			}
			if (Char.myCharz().cgender == 1)
			{
				return 11;
			}
			return 14;
		}

		public static void KSBossBangSkill5()
		{
			if (!isKSBossBangSkill5)
			{
				return;
			}
			Char @char = BossInMap();
			if ((Char.myCharz().isStandAndCharge || Char.myCharz().isCharge || Char.myCharz().isFlyAndCharge || GetCoolDownSkill(GetSkillByID(ulti())) > 0) && GetSkillByID(ulti()) == null && @char == null)
			{
				return;
			}
			if (HPKSBoss == 0)
			{
				isKSBossBangSkill5 = false;
				GameScr.info1.addInfo("HP Boss = 0 thì ks sao ba =))", 0);
				GameScr.info1.addInfo("KS Boss đã tắt", 0);
			}
			else if ((@char.cHP <= HPKSBoss || (DameToBoss() + Char.myCharz().cHPFull) / 2 >= @char.cHP) && GetSkillByID(ulti()) != null)
			{
				if (Math.abs(@char.cx - Char.myCharz().cx) >= 500)
				{
					GotoXY(@char.cx, @char.cy - 1);
				}
				Char.myCharz().focusManualTo(@char);
				UseSkill(GetSkillByID(ulti()));
			}
		}

		public static Char BossInMap()
		{
			for (int i = 0; i < GameScr.vCharInMap.size(); i++)
			{
				Char @char = (Char)GameScr.vCharInMap.elementAt(i);
				if (@char.cTypePk == 5 && @char != null && @char.cHP > 0 && Char.myCharz().isMeCanAttackOtherPlayer(@char))
				{
					return @char;
				}
			}
			return null;
		}

		private static double DameToBoss()
		{
			double num = -Char.myCharz().cHPFull;
			for (int i = 0; i < GameScr.vCharInMap.size(); i++)
			{
				Char @char = (Char)GameScr.vCharInMap.elementAt(i);
				if (@char != null && @char.isStandAndCharge && Char.myCharz().cgender == 2 && BossInMap() != null)
				{
					if (num < 0)
					{
						num = 0;
					}
					num += @char.cHPFull;
				}
			}
			return num;
		}

		public static void UseSkill(Skill sk)
		{
			if (Char.myCharz().myskill != sk)
			{
				GameScr.gI().doSelectSkill(sk, isShortcut: true);
				GameScr.gI().doSelectSkill(sk, isShortcut: true);
			}
			else
			{
				GameScr.gI().doSelectSkill(sk, isShortcut: true);
			}
		}

		public static Skill GetSkillByID(sbyte idSkill)
		{
			for (int i = 0; i < GameScr.keySkill.Length; i++)
			{
				if (GameScr.keySkill[i] != null && GameScr.keySkill[i].template.id == idSkill)
				{
					return GameScr.keySkill[i];
				}
			}
			return null;
		}

		public static Skill GetSkillByIconID(int iconID)
		{
			for (int i = 0; i < GameScr.keySkill.Length; i++)
			{
				if (GameScr.keySkill[i] != null && GameScr.keySkill[i].template.iconId == iconID)
				{
					return GameScr.keySkill[i];
				}
			}
			return null;
		}

		public static int GetCoolDownSkill(Skill skill)
		{
			return (int)(skill.coolDown - mSystem.currentTimeMillis() + skill.lastTimeUseThisSkill);
		}

		public static void HoiSinhTheoNgocChiDinh()
		{
			if (ngocHienTai == 0 || Char.myCharz().luongKhoa + Char.myCharz().luong == 0 || ngocDuocDungDeHoiSinh == 0)
			{
				GameScr.info1.addInfo("Không còn ngọc để hồi sinh hoặc chưa set up số ngọc được phép sử dụng hoặc số ngọc được hồi sinh đã dùng hết", 0);
				GameScr.info1.addInfo("Đã tắt tự hồi sinh với số ngọc chỉ định", 0);
				hoiSinhNgoc = false;
			}
			if (ngocDuocDungDeHoiSinh > 0 && Char.myCharz().cHP <= 0 && Char.myCharz().statusMe == 14)
			{
				Service.gI().wakeUpFromDead();
				ngocDuocDungDeHoiSinh--;
			}
		}

		public static void AutoNoiTai()
		{
			while (isNoitai)
			{
				if (ntMin == 0)
				{
					GameScr.info1.addInfo("Chưa set up chỉ số nội tại", 0);
					isNoitai = false;
					break;
				}
				if (Panel.specialInfo.Contains(tennoitaicanmo) && ntMin <= ntNow)
				{
					GameScr.info1.addInfo("Đã Ra Nội Tại Cần Mở", 0);
					isNoitai = false;
					break;
				}
				if (GameCanvas.isPointerClick && GameCanvas.isPointerJustRelease)
				{
					GameScr.info1.addInfo("Auto Mở Nội Tại Đã Tắt", 0);
					isNoitai = false;
					break;
				}
				Service.gI().speacialSkill(0);
				Service.gI().confirmMenu(5, 2);
				Service.gI().confirmMenu(5, 0);
				Thread.Sleep(500);
			}
		}

		public static string CutString(int start, int end, string s)
		{
			string text = "";
			for (int i = start; i < end; i++)
			{
				text += s[i];
			}
			return text;
		}

		public static void DoBoss()
		{
			Service.gI().requestChangeZone(zoneMacDinh, -1);
			zoneMacDinh++;
		}

		public static int getX(sbyte type)
		{
			for (int i = 0; i < TileMap.vGo.size(); i++)
			{
				Waypoint waypoint = (Waypoint)TileMap.vGo.elementAt(i);
				if (waypoint.maxX < 60 && type == 0)
				{
					return 15;
				}
				if (waypoint.minX <= TileMap.pxw - 60 && waypoint.maxX >= 60 && type == 1)
				{
					return (waypoint.minX + waypoint.maxX) / 2;
				}
				if (waypoint.minX <= TileMap.pxw - 60 || type != 2)
				{
					if (type == 3)
					{
						if (waypoint.maxX < 60)
						{
							return 15;
						}
						if (waypoint.minX > TileMap.pxw - 60)
						{
							return TileMap.pxw - 15;
						}
					}
					continue;
				}
				return TileMap.pxw - 15;
			}
			return 0;
		}

		public static int getY(sbyte type)
		{
			for (int i = 0; i < TileMap.vGo.size(); i++)
			{
				Waypoint waypoint = (Waypoint)TileMap.vGo.elementAt(i);
				if (waypoint.maxX < 60 && type == 0)
				{
					return waypoint.maxY;
				}
				if (waypoint.minX <= TileMap.pxw - 60 && waypoint.maxX >= 60 && type == 1)
				{
					return waypoint.maxY;
				}
				if (waypoint.minX <= TileMap.pxw - 60 || type != 2)
				{
					if (type == 3 && waypoint.maxY != Char.myCharz().cy)
					{
						if (waypoint.maxX < 60)
						{
							return waypoint.maxY;
						}
						if (waypoint.minX > TileMap.pxw - 60)
						{
							return waypoint.maxY;
						}
					}
					continue;
				}
				return waypoint.maxY;
			}
			return 0;
		}

		public static void autoFocusBoss()
		{
			if (!focusBoss || mSystem.currentTimeMillis() - currFocusBoss < 500)
			{
				return;
			}
			currFocusBoss = mSystem.currentTimeMillis();
			for (int i = 0; i < GameScr.vCharInMap.size(); i++)
			{
				Char @char = (Char)GameScr.vCharInMap.elementAt(i);
				if (@char != null && @char.cTypePk == 5 && !@char.cName.StartsWith("Đ"))
				{
					Char.myCharz().focusManualTo(@char);
					break;
				}
			}
		}

		public static void addSet1(Item item)
		{
			foreach (setDo1 item2 in ListSet1)
			{
				if (item2.type == item.template.type && item2.info == item.info && item2.id == item.template.id && item2.name == item.template.name && item2.option == item.VuDangItemOption() && item2.soSao == item.VuDangSoSao())
				{
					ListSet1.Remove(item2);
					GameCanvas.startOKDlg("Đã xóa " + item.template.name + " khỏi set 1");
				}
			}
			ListSet1.Add(new setDo1(item.template.type, item.info, item.template.id, item.template.name, item.VuDangItemOption(), item.VuDangSoSao()));
			GameCanvas.startOKDlg("Đã thêm " + item.template.name + " vào set 1");
		}

		public static void macSet1()
		{
			foreach (setDo1 item in ListSet1)
			{
				Item[] arrItemBag = Char.myCharz().arrItemBag;
				try
				{
					for (int i = 0; i < arrItemBag.Length; i++)
					{
						if (arrItemBag[i].template.type == item.type && arrItemBag[i].info == item.info && arrItemBag[i].template.id == item.id && arrItemBag[i].template.name == item.name && arrItemBag[i].VuDangItemOption() == item.option && arrItemBag[i].VuDangSoSao() == item.soSao)
						{
							Service.gI().getItem(4, (sbyte)i);
							Thread.Sleep(500);
						}
					}
				}
				catch
				{
				}
			}
			GameScr.info1.addInfo("Đã mặc set 1", 0);
		}

		public static void addSet2(Item item)
		{
			foreach (setDo2 item2 in ListSet2)
			{
				if (item2.name == item.template.name && item2.type == item.template.type && item2.info == item.info && item2.id == item.template.id && item2.option == item.VuDangItemOption() && item2.soSao == item.VuDangSoSao())
				{
					ListSet2.Remove(item2);
					GameCanvas.startOKDlg("Đã xóa " + item.template.name + " khỏi set 2");
				}
			}
			ListSet2.Add(new setDo2(item.template.type, item.info, item.template.id, item.template.name, item.VuDangItemOption(), item.VuDangSoSao()));
			GameCanvas.startOKDlg("Đã thêm " + item.template.name + " vào set 2");
		}

		public static void macSet2()
		{
			foreach (setDo2 item in ListSet2)
			{
				Item[] arrItemBag = Char.myCharz().arrItemBag;
				try
				{
					for (int i = 0; i < arrItemBag.Length; i++)
					{
						if (arrItemBag[i].template.type == item.type && arrItemBag[i].info == item.info && arrItemBag[i].template.id == item.id && arrItemBag[i].template.name == item.name && arrItemBag[i].VuDangItemOption() == item.option && arrItemBag[i].VuDangSoSao() == item.soSao)
						{
							Service.gI().getItem(4, (sbyte)i);
							Thread.Sleep(500);
						}
					}
				}
				catch
				{
				}
			}
			GameScr.info1.addInfo("Đã mặc set 2", 0);
		}

		public static void AddItem(Item item)
		{
			foreach (ItemAuto item2 in listItemAuto)
			{
				if (item2.iconID == item.template.iconID && item2.id == item.template.id)
				{
					listItemAuto.Remove(item2);
					GameCanvas.startOKDlg("Đã xóa " + item.template.name + " khỏi list item auto");
					return;
				}
			}
			listItemAuto.Add(new ItemAuto(item.template.iconID, item.template.id));
			GameCanvas.startOKDlg("Đã thêm " + item.template.name + " vào list item auto");
		}

		public static void useItem()
		{
			if (listItemAuto.Count <= 0)
			{
				return;
			}
			if (GameCanvas.isPointerClick && GameCanvas.isPointerJustRelease)
			{
				listItemAuto.Clear();
				GameScr.info1.addInfo("Đã dừng auto sử dụng Item", 0);
			}
			for (int i = 0; i < Char.myCharz().arrItemBag.Length; i++)
			{
				Item item = Char.myCharz().arrItemBag[i];
				foreach (ItemAuto item2 in listItemAuto)
				{
					if (item != null && item.template.iconID == item2.iconID && item.template.id == item2.id && !ItemTime.isExistItem(item.template.iconID))
					{
						Service.gI().useItem(0, 1, (sbyte)FindIndexItem(item.template.id), -1);
						break;
					}
				}
			}
		}

		public static void AutoLogin()
		{
			dangLogin = true;
			Thread.Sleep(1000);
			GameCanvas.startOKDlg("Vui Lòng Thử Lại...");
			Thread.Sleep(2000);
			while (ServerListScreen.testConnect != 2)
			{
				GameCanvas.serverScreen.switchToMe();
				Thread.Sleep(1000);
			}
			if (GameCanvas.loginScr == null)
			{
				GameCanvas.loginScr = new LoginScr();
			}
			Thread.Sleep(1000);
			GameCanvas.loginScr.switchToMe();
			GameCanvas.loginScr.doLogin();
			dangLogin = false;
		}

		public static void VeKhu()
		{
			if (isAutoVeKhu)
			{
				int num = -1;
				try
				{
					num = khuVeLai;
				}
				catch
				{
				}
				while (num >= 0 && TileMap.zoneID != num)
				{
					Service.gI().requestChangeZone(num, -1);
				}
			}
		}

		public static void AutoTTNL()
		{
			if (!isAutoTTNL || mSystem.currentTimeMillis() - currAutoTTNL < 1000)
			{
				return;
			}
			currAutoTTNL = mSystem.currentTimeMillis();
			if (Char.myCharz().cgender != 2)
			{
				GameScr.info1.addInfo("Hành tinh acc này không phải xayda", 0);
				GameScr.info1.addInfo("Auto ttnl đã tắt", 0);
				isAutoTTNL = false;
			}
			else if (!Char.myCharz().isStandAndCharge && !Char.myCharz().isCharge && !Char.myCharz().isFlyAndCharge && !Char.myCharz().stone && Char.myCharz().holdEffID == 0 && !Char.myCharz().blindEff && !Char.myCharz().sleepEff && Char.myCharz().cHP > 0 && Char.myCharz().statusMe != 14)
			{
				if ((Char.myCharz().cHP < Char.myCharz().cHPFull || Char.myCharz().cMP < Char.myCharz().cMPFull) && GetCoolDownSkill(GetSkillByIconID(720)) <= 0)
				{
					UseSkill(GetSkillByIconID(720));
				}
				else if (Char.myCharz().myskill != GetSkillByIconID(539))
				{
					GameScr.gI().doSelectSkill(GetSkillByIconID(539), isShortcut: true);
				}
			}
		}

		public static Char myPetInMap()
		{
			for (int i = 0; i < GameScr.vCharInMap.size(); i++)
			{
				Char @char = (Char)GameScr.vCharInMap.elementAt(i);
				if (@char != null && @char.charID == -Char.myCharz().charID)
				{
					return @char;
				}
			}
			return null;
		}

		private static void UseSkillAuto()
		{
			if (GameCanvas.isPointerClick && GameCanvas.isPointerJustRelease && listSkillsAuto.Count > 0)
			{
				listSkillsAuto.Clear();
				GameScr.info1.addInfo("Đã reset skill auto", 0);
				return;
			}
			foreach (Skill item in listSkillsAuto)
			{
				if (Char.myCharz().isStandAndCharge || Char.myCharz().isCharge || Char.myCharz().isFlyAndCharge || Char.myCharz().stone || Char.myCharz().holdEffID != 0 || Char.myCharz().blindEff || Char.myCharz().sleepEff || Char.myCharz().cHP <= 0 || Char.myCharz().statusMe == 14 || TileMap.mapID == Char.myCharz().cgender + 21)
				{
					break;
				}
				if ((GetCoolDownSkill(item) <= 0 && item.template.type == 3) || item.lastTimeUseThisSkill == 0)
				{
					UseSkill(item);
				}
			}
		}

		public static void AddRemoveSkill(int indexSkill)
		{
			if (listSkillsAuto.Contains(GameScr.keySkill[indexSkill]))
			{
				listSkillsAuto.Remove(GameScr.keySkill[indexSkill]);
				GameScr.info1.addInfo("Đã xóa " + GameScr.keySkill[indexSkill].template.name + " khỏi list skill auto", 0);
				GameScr.VuDangMenuMod();
			}
			else
			{
				listSkillsAuto.Add(GameScr.keySkill[indexSkill]);
				GameScr.info1.addInfo("Đã thêm " + GameScr.keySkill[indexSkill].template.name + " vào list skill auto", 0);
				GameScr.VuDangMenuMod();
			}
		}

		public static void xd()
		{
			if (xindau && mSystem.currentTimeMillis() - currXinDau >= 35000)
			{
				currXinDau = mSystem.currentTimeMillis();
				Service.gI().clanMessage(1, "", -1);
			}
		}

		public static void td()
		{
			if (TileMap.mapID != 21 && TileMap.mapID != 22 && TileMap.mapID != 23)
			{
				return;
			}
			int num = 0;
			for (int i = 0; i < Char.myCharz().arrItemBox.Length; i++)
			{
				Item item = Char.myCharz().arrItemBox[i];
				if (item != null && item.template.type == 6)
				{
					num += item.quantity;
				}
			}
			if (num < 20 && GameCanvas.gameTick % 200 == 0)
			{
				for (int j = 0; j < Char.myCharz().arrItemBag.Length; j++)
				{
					Item item2 = Char.myCharz().arrItemBag[j];
					if (item2 != null && item2.template.type == 6)
					{
						Service.gI().getItem(1, (sbyte)j);
					}
				}
			}
			if ((GameScr.gI().magicTree.currPeas > 0 && GameScr.hpPotion < 10) || (num < 20 && GameCanvas.gameTick % 100 == 0))
			{
				Service.gI().openMenu(4);
				Service.gI().confirmMenu(4, 0);
			}
		}

		public static void cd()
		{
			if (!chodau || mSystem.currentTimeMillis() - currChoDau < 500)
			{
				return;
			}
			currChoDau = mSystem.currentTimeMillis();
			for (int i = 0; i < ClanMessage.vMessage.size(); i++)
			{
				ClanMessage clanMessage = (ClanMessage)ClanMessage.vMessage.elementAt(i);
				if (clanMessage.maxCap != 0 && clanMessage.playerName != Char.myCharz().cName && clanMessage.recieve != clanMessage.maxCap)
				{
					Service.gI().clanDonate(clanMessage.id);
					break;
				}
			}
		}

		public static void NeBoss()
		{
			for (int i = 0; i < GameScr.vCharInMap.size(); i++)
			{
				Char @char = (Char)GameScr.vCharInMap.elementAt(i);
				char c = char.Parse(@char.cName.Substring(0, 1));
				if (@char != null && c >= 'A' && c <= 'Z' && !@char.cName.StartsWith("Đệ tử") && @char.cTypePk == 5 && !@char.isMiniPet)
				{
					Service.gI().requestChangeZone(Res.random(0, GameScr.gI().zones.Length), -1);
					break;
				}
			}
		}

		public static bool FlagInMap()
		{
			for (int i = 0; i < GameScr.vCharInMap.size(); i++)
			{
				Char @char = (Char)GameScr.vCharInMap.elementAt(i);
				if (@char != null && @char.cFlag != 0 && @char.charID > 0 && ((Math.abs(@char.cx - Char.myCharz().cx) <= 500 && Math.abs(@char.cy - Char.myCharz().cy) <= 500) || (Math.abs(@char.cx - myPetInMap().cx) <= 500 && Math.abs(@char.cy - myPetInMap().cy) <= 500)))
				{
					return true;
				}
			}
			return false;
		}

		public static void CheckLag()
		{
			if (mSystem.currentTimeMillis() - lastTimeCheckLag >= 1000 && GameCanvas.currentScreen == GameScr.instance)
			{
				currentCheckLag--;
				if (currentCheckLag <= 0)
				{
					Session_ME.gI().close();
					Session_ME2.gI().close();
					GameCanvas.gI().onDisconnected();
					currentCheckLag = 30;
				}
				lastTimeCheckLag = mSystem.currentTimeMillis();
			}
		}

		private static void AutoHoiSinh()
		{
			if (autoHoiSinh && (Char.myCharz().cHP <= 0 || Char.myCharz().meDead) && mSystem.currentTimeMillis() - currAutoHoiSinh >= 300)
			{
				currAutoHoiSinh = mSystem.currentTimeMillis();
				Service.gI().wakeUpFromDead();
			}
		}

		public static void AnNho()
		{
			try
			{
				for (int i = 0; i < Char.myCharz().arrItemBag.Length; i++)
				{
					if (Char.myCharz().arrItemBag[i] != null && Char.myCharz().arrItemBag[i].template.id == 212)
					{
						Service.gI().useItem(0, 1, (sbyte)i, -1);
						break;
					}
				}
			}
			catch
			{
				try
				{
					for (int j = 0; j < Char.myCharz().arrItemBag.Length; j++)
					{
						if (Char.myCharz().arrItemBag[j] != null && Char.myCharz().arrItemBag[j].template.id == 211)
						{
							Service.gI().useItem(0, 1, (sbyte)j, -1);
							break;
						}
					}
				}
				catch
				{
					GameScr.info1.addInfo("Không có nho trong balo", 0);
				}
			}
		}

		public static void GoBack()
		{
			Thread.Sleep(5000);
			if (!GameScr.gI().magicTree.isUpdate && GameScr.gI().magicTree.currPeas > 0 && TileMap.mapID == Char.myCharz().cgender + 21)
			{
				Service.gI().magicTree(1);
				Thread.Sleep(500);
				GameCanvas.gI().keyPressedz(-5);
				Thread.Sleep(1000);
			}
			for (int i = 0; i < GameScr.vItemMap.size(); i++)
			{
				ItemMap itemMap = (ItemMap)GameScr.vItemMap.elementAt(i);
				Char.myCharz().cx = itemMap.x;
				Service.gI().charMove();
				Thread.Sleep(1000);
				Service.gI().pickItem(itemMap.itemMapID);
				Thread.Sleep(1000);
			}
			XmapController.StartRunToMapId(IdmapGB);
			while (TileMap.mapID != IdmapGB)
			{
				Thread.Sleep(100);
			}
			new Thread(DoiLaiKhu).Start();
			GameScr.isAutoPlay = true;
			IsGoBack = false;
		}

		public static void DoiLaiKhu()
		{
			while (TileMap.zoneID != ZoneGB)
			{
				Thread.Sleep(1000);
				Service.gI().requestChangeZone(ZoneGB, -1);
			}
			Thread.Sleep(2000);
			GotoXY(xGB, yGB);
			GameScr.isAutoPlay = true;
		}

		static VuDang()
		{
			//isbanvang = false;
			//speed = 2;
			isPKM = false;
			isBossM = true;
			isAutoLogin = true;
			hideAll = false;
			ListMap = "Làng Aru,Đồi hoa cúc,Thung lũng tre,Rừng nấm,Rừng xương,Đảo Kamê,Đông Karin,Làng Mori,Đồi nấm tím,Thị trấn Moori,Thung lũng Namếc,Thung lũng Maima,Vực maima,Đảo Guru,Làng Kakarot,Đồi hoang,Làng Plant,Rừng nguyên sinh,Rừng thông Xayda,Thành phố Vegeta,Vách núi đen,Nhà Gôhan,Nhà Moori,Nhà Broly,Trạm tàu vũ trụ,Trạm tàu vũ trụ,Trạm tàu vũ trụ,Rừng Bamboo,Rừng dương xỉ,Nam Kamê,Đảo Bulông,Núi hoa vàng,Núi hoa tím,Nam Guru,Đông Nam Guru,Rừng cọ,Rừng đá,Thung lũng đen,Bờ vực đen,Vách núi Aru,Vách núi Moori,Vực Plant,Vách núi Aru,Vách núi Moori,Vách núi Kakarot,Thần điện,Tháp Karin,Rừng Karin,Hành tinh Kaio,Phòng tập thời gian,Thánh địa Kaio,Đấu trường,Đại hội võ thuật,Tường thành 1,Tầng 3,Tầng 1,Tầng 2,Tầng 4,Tường thành 2,Tường thành 3,Trại độc nhãn 1,Trại độc nhãn 2,Trại độc nhãn 3,Trại lính Fide,Núi dây leo,Núi cây quỷ,Trại qủy già,Vực chết,Thung lũng Nappa,Vực cấm,Núi Appule,Căn cứ Raspberry,Thung lũng Raspberry,Thung lũng chết,Đồi cây Fide,Khe núi tử thần,Núi đá,Rừng đá,Lãnh  địa Fize,Núi khỉ đỏ,Núi khỉ vàng,Hang quỷ chim,Núi khỉ đen,Hang khỉ đen,Siêu Thị,Hành tinh M-2,Hành tinh Polaris,Hành tinh Cretaceous,Hành tinh Monmaasu,Hành tinh Rudeeze,Hành tinh Gelbo,Hành tinh Tigere,Thành phố phía đông,Thành phố phía nam,Đảo Balê,95,Cao nguyên,Thành phố phía bắc,Ngọn núi phía bắc,Thung lũng phía bắc,Thị trấn Ginder,101,Nhà Bunma,Võ đài Xên bọ hung,Sân sau siêu thị,Cánh đồng tuyết,Rừng tuyết,Núi tuyết,Dòng sông băng,Rừng băng,Hang băng,Đông Nam Karin,Võ đài Hạt Mít,Đại hội võ thuật,Cổng phi thuyền,Phòng chờ,Thánh địa Kaio,Cửa Ải 1,Cửa Ải 2,Cửa Ải 3,Phòng chỉ huy,Đấu trường,Ngũ Hành Sơn,Ngũ Hành Sơn,Ngũ Hành Sơn,Võ đài Bang,Thành phố Santa,Cổng phi thuyền,Bụng Mabư,Đại hội võ thuật,Đại hội võ thuật Vũ Trụ,Hành Tinh Yardart,Hành Tinh Yardart 2,Hành Tinh Yardart 3,Đại hội võ thuật Vũ Trụ 6-7,Động hải tặc,Hang Bạch Tuộc,Động kho báu,Cảng hải tặc,Hành tinh Potaufeu,Hang động Potaufeu,Con đường rắn độc,Con đường rắn độc,Con đường rắn độc,Hoang mạc,Võ Đài Siêu Cấp,Tây Karin,Sa mạc,Lâu đài Lychee,Thành phố Santa,Lôi Đài,Hành tinh bóng tối,Vùng đất băng giá,Lãnh địa bang hội,Hành tinh Bill,Hành tinh ngục tù,Tây thánh địa,Đông thánh Địa,Bắc thánh địa,Nam thánh Địa,Khu hang động,Bìa rừng nguyên thủy,Rừng nguyên thủy,Làng Plant nguyên thủy,Tranh ngọc Namếc";
			MapNames = ListMap.Split(',');
			dichChuyenPem = true;
			currTimeAK = new long[8];
			isDapDo = false;
			soSaoCanDap = -1;
			saoHienTai = -1;
			chars = new Char[50];
			nvat = true;
			textAutoChat = string.Empty;
			textAutoChatTG = string.Empty;
			timeBT = 1;
			XoaBackground = false;
			backgroundColor = "0.6 0.8 0.9";
			trangThai = false;
			thongBaoBoss = true;
			bossVip = new MyVector();
			lineboss = true;
			isThongTinDeTu = false;
			isThongTinSuPhu = false;
			itemBuy = null;
			currBuyItem = 0L;
			HPKSBoss = 0;
			ngocHienTai = 0;
			doBoss = false;
			isUpdateKhu = true;
			ListSet1 = new List<setDo1>();
			ListSet2 = new List<setDo2>();
			listItemAuto = new List<ItemAuto>();
			dau = "Đậu";
			doHoa = "Đồ Họa";
			hoTroUpDe = "Up đệ";
			upYardrat = "Up Yardrat";
			hoTroSanBoss = "Boss";
			chucNangKhac = "Khác";
			tdLT = "TĐLT";
			autoSkill = "Auto Skill";
			listSkillsAuto = new List<Skill>();
			currentCheckLag = 30;
			isCheckLag = false;
			isbom = false;
			canUpdate = false;
			tocdochay = 6;
			doBossNguoc = false;
		}

		public static void Capsule()
		{
			for (int i = 0; i < Char.myCharz().arrItemBag.Length; i++)
			{
				if (Char.myCharz().arrItemBag[i] != null && (Char.myCharz().arrItemBag[i].template.id == 193 || Char.myCharz().arrItemBag[i].template.id == 194))
				{
					Service.gI().useItem(0, 1, (sbyte)i, -1);
					return;
				}
			}
			GameScr.info1.addInfo("Không có Capsule", 0);
		}

		public static void Akbk()
		{
			if (!Char.myCharz().stone && !Char.isLoadingMap && !Char.myCharz().meDead && Char.myCharz().statusMe != 14 && Char.myCharz().statusMe != 5 && Char.myCharz().myskill.template.type != 3 && Char.myCharz().myskill.template.id != 10 && Char.myCharz().myskill.template.id != 11 && !Char.myCharz().myskill.paintCanNotUseSkill)
			{
				getSkill();
				if (GameScr.gI().isMeCanAttackMob(Char.myCharz().mobFocus) && (double)Res.abs(Char.myCharz().mobFocus.xFirst - Char.myCharz().cx) < (double)Char.myCharz().myskill.dx * 1.5)
				{
					AkMob();
				}
			}
		}

		public static void DoBossNguoc()
		{
			Service.gI().requestChangeZone(zoneMacDinhNguoc, -1);
			zoneMacDinhNguoc--;
		}

		public static void kepBoss()
		{
			Char @char = (Char)GameScr.vCharInMap.elementAt(0);
			Char.myCharz().cx = @char.cx;
			Char.myCharz().cy = @char.cy;
		}
	}
}