using System;
using System.Collections.Generic;
using System.Text;

namespace Game1.AssemblyCSharp.Mod.PickMob
{

	public class Pk9rPickMob
	{
		private const int ID_ITEM_GEM = 77;

		private const int ID_ITEM_GEM_LOCK = 861;

		private const int DEFAULT_HP_BUFF = 20;

		private const int DEFAULT_MP_BUFF = 20;

		private static readonly sbyte[] IdSkillsBase = new sbyte[5] { 0, 2, 17, 4, 13 };

		public static readonly short[] IdItemBlockBase = new short[10] { 225, 353, 354, 355, 356, 357, 358, 359, 360, 362 };

		public static bool IsTanSat = false;

		public static bool IsNeSieuQuai = false;

		public static bool IsVuotDiaHinh = true;

		public static List<int> IdMobsTanSat = new List<int>();

		public static List<int> TypeMobsTanSat = new List<int>();

		public static List<sbyte> IdSkillsTanSat = new List<sbyte>(IdSkillsBase);

		public static bool IsAutoPickItems;

		public static bool IsItemMe = true;

		public static bool IsLimitTimesPickItem = true;

		public static int TimesAutoPickItemMax = 10;

		public static List<short> IdItemPicks = new List<short>();

		public static List<short> IdItemBlocks = new List<short>(IdItemBlockBase);

		public static List<sbyte> TypeItemPicks = new List<sbyte>();

		public static List<sbyte> TypeItemBlock = new List<sbyte>();

		public static int HpBuff = 0;

		public static int MpBuff = 0;

		public static bool Chat(string text)
		{
			switch (text)
			{
				case "add":
					{
						Mob mobFocus2 = Char.myCharz().mobFocus;
						ItemMap itemFocus3 = Char.myCharz().itemFocus;
						if (mobFocus2 != null)
						{
							if (IdMobsTanSat.Contains(mobFocus2.mobId))
							{
								IdMobsTanSat.Remove(mobFocus2.mobId);
								GameScr.info1.addInfo("Đã xoá mob: " + mobFocus2.mobId, 0);
							}
							else
							{
								IdMobsTanSat.Add(mobFocus2.mobId);
								GameScr.info1.addInfo("Đã thêm mob: " + mobFocus2.mobId, 0);
							}
						}
						else if (itemFocus3 != null)
						{
							if (IdItemPicks.Contains(itemFocus3.template.id))
							{
								IdItemPicks.Remove(itemFocus3.template.id);
								GameScr.info1.addInfo($"Đã xoá khỏi danh sách chỉ tự động nhặt item: {itemFocus3.template.name}[{itemFocus3.template.id}]", 0);
							}
							else
							{
								IdItemPicks.Add(itemFocus3.template.id);
								GameScr.info1.addInfo($"Đã thêm vào danh sách chỉ tự động nhặt item: {itemFocus3.template.name}[{itemFocus3.template.id}]", 0);
							}
						}
						else
						{
							GameScr.info1.addInfo("Cần trỏ vào quái hay vật phẩm cần thêm vào danh sách", 0);
						}
						break;
					}
				case "addd":
					{
						Mob mobFocus = Char.myCharz().mobFocus;
						ItemMap itemFocus2 = Char.myCharz().itemFocus;
						if (mobFocus != null)
						{
							if (TypeMobsTanSat.Contains(mobFocus.templateId))
							{
								TypeMobsTanSat.Remove(mobFocus.templateId);
								GameScr.info1.addInfo($"Đã xoá loại mob: {mobFocus.getTemplate().name}[{mobFocus.templateId}]", 0);
							}
							else
							{
								TypeMobsTanSat.Add(mobFocus.templateId);
								GameScr.info1.addInfo($"Đã thêm loại mob: {mobFocus.getTemplate().name}[{mobFocus.templateId}]", 0);
							}
						}
						else if (itemFocus2 != null)
						{
							if (TypeItemPicks.Contains(itemFocus2.template.type))
							{
								TypeItemPicks.Remove(itemFocus2.template.type);
								GameScr.info1.addInfo("Đã xoá khỏi danh sách chỉ tự động nhặt loại item:" + itemFocus2.template.type, 0);
							}
							else
							{
								TypeItemPicks.Add(itemFocus2.template.type);
								GameScr.info1.addInfo("Đã thêm vào danh sách chỉ tự động nhặt loại item:" + itemFocus2.template.type, 0);
							}
						}
						else
						{
							GameScr.info1.addInfo("Cần trỏ vào quái hay vật phẩm cần thêm vào danh sách", 0);
						}
						break;
					}
				case "nhat":
					IsAutoPickItems = !IsAutoPickItems;
					GameScr.info1.addInfo("Auto nhặt của tàn sát: " + (IsAutoPickItems ? "Bật" : "Tắt"), 0);
					break;
				case "itm":
					IsItemMe = !IsItemMe;
					GameScr.info1.addInfo("Lọc không nhặt vật phẩm của người khác: " + (IsItemMe ? "Bật" : "Tắt"), 0);
					break;
				case "sln":
					{
						IsLimitTimesPickItem = !IsLimitTimesPickItem;
						StringBuilder stringBuilder = new StringBuilder();
						stringBuilder.Append("Giới hạn số lần nhặt là ");
						stringBuilder.Append(TimesAutoPickItemMax);
						stringBuilder.Append(IsLimitTimesPickItem ? ": Bật" : ": Tắt");
						GameScr.info1.addInfo(stringBuilder.ToString(), 0);
						break;
					}
				default:
					if (IsGetInfoChat<int>(text, "sln"))
					{
						TimesAutoPickItemMax = GetInfoChat<int>(text, "sln");
						GameScr.info1.addInfo("Số lần nhặt giới hạn là: " + TimesAutoPickItemMax, 0);
						break;
					}
					if (IsGetInfoChat<short>(text, "addi"))
					{
						short infoChat = GetInfoChat<short>(text, "addi");
						if (IdItemPicks.Contains(infoChat))
						{
							IdItemPicks.Remove(infoChat);
							GameScr.info1.addInfo($"Đã xoá khỏi danh sách chỉ tự động nhặt item: {ItemTemplates.get(infoChat).name}[{infoChat}]", 0);
						}
						else
						{
							IdItemPicks.Add(infoChat);
							GameScr.info1.addInfo($"Đã thêm vào danh sách chỉ tự động nhặt item: {ItemTemplates.get(infoChat).name}[{infoChat}]", 0);
						}
						break;
					}
					if (text == "blocki")
					{
						ItemMap itemFocus = Char.myCharz().itemFocus;
						if (itemFocus != null)
						{
							if (IdItemBlocks.Contains(itemFocus.template.id))
							{
								IdItemBlocks.Remove(itemFocus.template.id);
								GameScr.info1.addInfo($"Đã xoá khỏi danh sách không tự động nhặt item: {itemFocus.template.name}[{itemFocus.template.id}]", 0);
							}
							else
							{
								IdItemBlocks.Add(itemFocus.template.id);
								GameScr.info1.addInfo($"Đã thêm vào danh sách không tự động nhặt item: {itemFocus.template.name}[{itemFocus.template.id}]", 0);
							}
						}
						else
						{
							GameScr.info1.addInfo("Cần trỏ vào vật phẩm cần chặn khi auto nhặt", 0);
						}
						break;
					}
					if (IsGetInfoChat<short>(text, "blocki"))
					{
						short infoChat2 = GetInfoChat<short>(text, "blocki");
						if (IdItemBlocks.Contains(infoChat2))
						{
							IdItemBlocks.Remove(infoChat2);
							GameScr.info1.addInfo($"Đã thêm vào danh sách không tự động nhặt item: {ItemTemplates.get(infoChat2).name}[{infoChat2}]", 0);
						}
						else
						{
							IdItemBlocks.Add(infoChat2);
							GameScr.info1.addInfo($"Đã xoá khỏi danh sách không tự động nhặt item: {ItemTemplates.get(infoChat2).name}[{infoChat2}]", 0);
						}
						break;
					}
					if (IsGetInfoChat<sbyte>(text, "addti"))
					{
						sbyte infoChat3 = GetInfoChat<sbyte>(text, "addti");
						if (TypeItemPicks.Contains(infoChat3))
						{
							TypeItemPicks.Remove(infoChat3);
							GameScr.info1.addInfo("Đã xoá khỏi danh sách chỉ tự động nhặt loại item: " + infoChat3, 0);
						}
						else
						{
							TypeItemPicks.Add(infoChat3);
							GameScr.info1.addInfo("Đã thêm vào danh sách chỉ tự động nhặt loại item: " + infoChat3, 0);
						}
						break;
					}
					if (IsGetInfoChat<sbyte>(text, "blockti"))
					{
						sbyte infoChat4 = GetInfoChat<sbyte>(text, "blockti");
						if (TypeItemBlock.Contains(infoChat4))
						{
							TypeItemBlock.Remove(infoChat4);
							GameScr.info1.addInfo("Đã xoá khỏi danh sách không tự động nhặt loại item: " + infoChat4, 0);
						}
						else
						{
							TypeItemBlock.Add(infoChat4);
							GameScr.info1.addInfo("Đã thêm vào danh sách không tự động nhặt loại item: " + infoChat4, 0);
						}
						break;
					}
					switch (text)
					{
						case "clri":
							IdItemPicks.Clear();
							TypeItemPicks.Clear();
							TypeItemBlock.Clear();
							IdItemBlocks.Clear();
							IdItemBlocks.AddRange(IdItemBlockBase);
							GameScr.info1.addInfo("Danh sách lọc item đã được đặt lại mặc định", 0);
							break;
						case "cnn":
							IdItemPicks.Clear();
							TypeItemPicks.Clear();
							TypeItemBlock.Clear();
							IdItemBlocks.Clear();
							IdItemBlocks.AddRange(IdItemBlockBase);
							IdItemPicks.Add(77);
							IdItemPicks.Add(861);
							GameScr.info1.addInfo("Đã cài đặt chỉ nhặt ngọc (của tàn sát)", 0);
							break;
						case "nsq":
							IsNeSieuQuai = !IsNeSieuQuai;
							GameScr.info1.addInfo("Tàn sát né siêu quái: " + (IsNeSieuQuai ? "Bật" : "Tắt"), 0);
							break;
						default:
							if (IsGetInfoChat<int>(text, "addm"))
							{
								int infoChat5 = GetInfoChat<int>(text, "addm");
								if (IdMobsTanSat.Contains(infoChat5))
								{
									IdMobsTanSat.Remove(infoChat5);
									GameScr.info1.addInfo("Đã xoá mob: " + infoChat5, 0);
								}
								else
								{
									IdMobsTanSat.Add(infoChat5);
									GameScr.info1.addInfo("Đã thêm mob: " + infoChat5, 0);
								}
							}
							else if (IsGetInfoChat<int>(text, "addtm"))
							{
								int infoChat6 = GetInfoChat<int>(text, "addtm");
								if (TypeMobsTanSat.Contains(infoChat6))
								{
									TypeMobsTanSat.Remove(infoChat6);
									GameScr.info1.addInfo($"Đã xoá loại mob: {Mob.arrMobTemplate[infoChat6].name}[{infoChat6}]", 0);
								}
								else
								{
									TypeMobsTanSat.Add(infoChat6);
									GameScr.info1.addInfo($"Đã thêm loại mob: {Mob.arrMobTemplate[infoChat6].name}[{infoChat6}]", 0);
								}
							}
							else if (text == "clrm")
							{
								IdMobsTanSat.Clear();
								TypeMobsTanSat.Clear();
								GameScr.info1.addInfo("Đã xoá danh sách đánh quái", 0);
							}
							else if (text == "skill")
							{
								SkillTemplate template = Char.myCharz().myskill.template;
								if (IdSkillsTanSat.Contains(template.id))
								{
									IdSkillsTanSat.Remove(template.id);
									GameScr.info1.addInfo($"Đã xoá khỏi danh sách skill sử dụng tự động đánh quái skill: {template.name}[{template.id}]", 0);
								}
								else
								{
									IdSkillsTanSat.Add(template.id);
									GameScr.info1.addInfo($"Đã thêm vào danh sách skill sử dụng tự động đánh quái skill: {template.name}[{template.id}]", 0);
								}
							}
							else if (IsGetInfoChat<sbyte>(text, "skillid"))
							{
								sbyte infoChat7 = GetInfoChat<sbyte>(text, "skillid");
								if (IdSkillsTanSat.Contains(infoChat7))
								{
									IdSkillsTanSat.Remove(infoChat7);
									GameScr.info1.addInfo("Đã xoá khỏi danh sách skill sử dụng tự động đánh quái skill: " + infoChat7, 0);
								}
								else
								{
									IdSkillsTanSat.Add(infoChat7);
									GameScr.info1.addInfo("Đã thêm vào danh sách skill sử dụng tự động đánh quái skill: " + infoChat7, 0);
								}
							}
							else if (text == "clrs")
							{
								IdSkillsTanSat.Clear();
								IdSkillsTanSat.AddRange(IdSkillsBase);
								GameScr.info1.addInfo("Đã đặt danh sách skill sử dụng tự động đánh quái về mặc định", 0);
							}
							else if (text == "abf")
							{
								if (HpBuff == 0 && MpBuff == 0)
								{
									GameScr.info1.addInfo("Tự động sử dụng đậu thần: Tắt", 0);
									break;
								}
								HpBuff = 20;
								MpBuff = 20;
								GameScr.info1.addInfo($"Tự động sử dụng đậu thần khi HP dưới {HpBuff}%, MP dưới {MpBuff}%", 0);
							}
							else if (IsGetInfoChat<int>(text, "abf"))
							{
								HpBuff = GetInfoChat<int>(text, "abf");
								MpBuff = 0;
								GameScr.info1.addInfo($"Tự động sử dụng đậu thần khi HP dưới {HpBuff}%", 0);
							}
							else if (IsGetInfoChat<int>(text, "abf", 2))
							{
								int[] infoChat8 = GetInfoChat<int>(text, "abf", 2);
								HpBuff = infoChat8[0];
								MpBuff = infoChat8[1];
								GameScr.info1.addInfo($"Tự động sử dụng đậu thần khi HP dưới {HpBuff}%, MP dưới {MpBuff}%", 0);
							}
							else
							{
								if (!(text == "vdh"))
								{
									return false;
								}
								IsVuotDiaHinh = !IsVuotDiaHinh;
								GameScr.info1.addInfo("Tự động đánh quái vượt địa hình: " + (IsVuotDiaHinh ? "Bật" : "Tắt"), 0);
							}
							break;
					}
					break;
			}
			return true;
		}

		public static bool HotKeys()
		{
			switch (GameCanvas.keyAsciiPress)
			{
				case 97:
					Chat("add");
					return true;
				case 98:
					Chat("abf");
					return true;
				case 110:
					Chat("nhat");
					return true;
				case 116:
					Chat("ts1");
					return true;
				default:
					return false;
			}
		}

		public static void Update()
		{
			PickMobController.Update();
		}

		public static void MobStartDie(object obj)
		{
			Mob mob = (Mob)obj;
			if (mob.status != 1 && mob.status != 0)
			{
				mob.timeLastDie = mSystem.currentTimeMillis();
				mob.countDie++;
				if (mob.countDie > 10)
				{
					mob.countDie = 0;
				}
			}
		}

		public static void UpdateCountDieMob(Mob mob)
		{
			if (mob.levelBoss != 0)
			{
				mob.countDie = 0;
			}
		}

		private static bool IsGetInfoChat<T>(string text, string s)
		{
			if (!text.StartsWith(s))
			{
				return false;
			}
			try
			{
				Convert.ChangeType(text.Substring(s.Length), typeof(T));
			}
			catch
			{
				return false;
			}
			return true;
		}

		private static T GetInfoChat<T>(string text, string s)
		{
			return (T)Convert.ChangeType(text.Substring(s.Length), typeof(T));
		}

		private static bool IsGetInfoChat<T>(string text, string s, int n)
		{
			if (!text.StartsWith(s))
			{
				return false;
			}
			try
			{
				string[] array = text.Substring(s.Length).Split(' ');
				for (int i = 0; i < n; i++)
				{
					Convert.ChangeType(array[i], typeof(T));
				}
			}
			catch
			{
				return false;
			}
			return true;
		}

		private static T[] GetInfoChat<T>(string text, string s, int n)
		{
			T[] array = new T[n];
			string[] array2 = text.Substring(s.Length).Split(' ');
			for (int i = 0; i < n; i++)
			{
				array[i] = (T)Convert.ChangeType(array2[i], typeof(T));
			}
			return array;
		}
	}
}
