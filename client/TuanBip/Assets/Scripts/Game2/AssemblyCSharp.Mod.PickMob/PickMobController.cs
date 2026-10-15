using System.Collections.Generic;
using System.Linq;

namespace Game2.AssemblyCSharp.Mod.PickMob
{

	public class PickMobController
	{
		private enum TpyePickItem
		{
			CanNotPickItem,
			PickItemNormal,
			PickItemTDLT,
			PickItemTanSat
		}

		private const int TIME_REPICKITEM = 500;

		private const int TIME_DELAY_TANSAT = 0;

		private const int ID_ICON_ITEM_TDLT = 4387;

		private static readonly sbyte[] IdSkillsMelee = new sbyte[5] { 0, 9, 2, 17, 4 };

		private static readonly sbyte[] IdSkillsCanNotAttack = new sbyte[8] { 10, 11, 14, 23, 7, 24, 25, 26};

		private static readonly PickMobController _Instance = new PickMobController();

		public static bool IsPickingItems;

		private static bool IsWait;

		private static long TimeStartWait;

		private static long TimeWait;

		public static List<ItemMap> ItemPicks = new List<ItemMap>();

		private static int IndexItemPick = 0;

		public static void Update()
		{
			if (IsWaiting())
			{
				return;
			}
			Char @char = Char.myCharz();
			if (@char.statusMe == 14 || @char.cHP <= 0)
			{
				return;
			}
			if (GameScr.hpPotion >= 1 && (@char.cHP <= @char.cHPFull * Pk9rPickMob.HpBuff / 100 || @char.cMP <= @char.cMPFull * Pk9rPickMob.MpBuff / 100))
			{
				GameScr.gI().doUseHP();
			}
			bool flag = ItemTime.isExistItem(4387);
			bool flag2 = Pk9rPickMob.IsTanSat && flag;
			if (Pk9rPickMob.IsAutoPickItems && !flag2)
			{
				if (IsPickingItems)
				{
					if (IndexItemPick >= ItemPicks.Count)
					{
						IsPickingItems = false;
						return;
					}
					ItemMap itemMap = ItemPicks[IndexItemPick];
					switch (GetTpyePickItem(itemMap))
					{
						case TpyePickItem.CanNotPickItem:
							IndexItemPick++;
							return;
						case TpyePickItem.PickItemNormal:
							Service.gI().pickItem(itemMap.itemMapID);
							if (Pk9rPickMob.IsTanSat)
							{
								itemMap.countAutoPick++;
								IndexItemPick++;
							}
							Wait((Pk9rPickMob.TimesAutoPickItemMax <= 1) ? 1 : 500);
							return;
						case TpyePickItem.PickItemTDLT:
							Service.gI().pickItem(itemMap.itemMapID);
							if (Pk9rPickMob.IsTanSat)
							{
								itemMap.countAutoPick++;
								IndexItemPick++;
							}
							Wait((Pk9rPickMob.TimesAutoPickItemMax <= 1) ? 1 : 500);
							return;
						case TpyePickItem.PickItemTanSat:
							Service.gI().pickItem(itemMap.itemMapID);
							Wait((Pk9rPickMob.TimesAutoPickItemMax <= 1) ? 1 : 500);
							return;
					}
				}
				ItemPicks.Clear();
				IndexItemPick = 0;
				for (int i = 0; i < GameScr.vItemMap.size(); i++)
				{
					ItemMap itemMap2 = (ItemMap)GameScr.vItemMap.elementAt(i);
					if (GetTpyePickItem(itemMap2) != 0)
					{
						ItemPicks.Add(itemMap2);
					}
				}
				if (ItemPicks.Count > 0)
				{
					IsPickingItems = true;
					return;
				}
			}
			if (!Pk9rPickMob.IsTanSat)
			{
				return;
			}
			if (Char.myCharz().cHP <= Char.myCharz().cHPFull * 10 / 100 || Char.myCharz().cMP <= Char.myCharz().cMPFull * 10 / 100)
			{
				GameScr.gI().doUseHP();
			}
			if (@char.isCharge)
			{
				Wait(Pk9rPickMob.IsTanSat ? 1 : 500);
				return;
			}
			@char.clearFocus(0);
			if (@char.mobFocus != null && !IsMobTanSat(@char.mobFocus))
			{
				@char.mobFocus = null;
			}
			if (@char.mobFocus == null)
			{
				@char.mobFocus = GetMobTanSat();
				if (flag && @char.mobFocus != null)
				{
					if (VuDang.dichChuyenPem)
					{
						if (Math.abs(@char.mobFocus.xFirst - @char.cx) >= 20 || Math.abs(@char.mobFocus.yFirst - @char.cy) >= 20)
						{
							VuDang.GotoXY(@char.mobFocus.xFirst, @char.mobFocus.yFirst);
						}
						return;
					}
					@char.cx = @char.mobFocus.xFirst;
					@char.cy = @char.mobFocus.yFirst;
					Service.gI().charMove();
				}
			}
			if (@char.mobFocus != null)
			{
				if (@char.skillInfoPaint() == null)
				{
					Skill skillAttack = GetSkillAttack();
					if (skillAttack != null && !skillAttack.paintCanNotUseSkill)
					{
						Mob mobFocus = @char.mobFocus;
						mobFocus.x = mobFocus.xFirst;
						mobFocus.y = mobFocus.yFirst;
						if (Char.myCharz().myskill != skillAttack)
						{
							GameScr.gI().doSelectSkill(skillAttack, isShortcut: true);
						}
						if (Res.distance(mobFocus.xFirst, mobFocus.yFirst, @char.cx, @char.cy) <= 48)
						{
							if (GameCanvas.gameTick % 50 == 0 && Mob.arrMobTemplate[Char.myCharz().mobFocus.templateId].type == 4)
							{
								VuDang.GotoXY(mobFocus.xFirst, mobFocus.yFirst + 1);
							}
							if (Char.myCharz().myskill.template.iconId == 539 || Char.myCharz().myskill.template.name.ToLower().Contains("liên hoàn") || Char.myCharz().myskill.template.iconId == 540)
							{
								VuDang.Ak();
								return;
							}
							GameScr.gI().doDoubleClickToObj(mobFocus);
						}
						else
						{
							if (VuDang.dichChuyenPem)
							{
								if (Math.abs(mobFocus.xFirst - @char.cx) >= 20 || Math.abs(mobFocus.yFirst - @char.cy) >= 20)
								{
									VuDang.GotoXY(mobFocus.xFirst, mobFocus.yFirst);
								}
								return;
							}
							Move(mobFocus.xFirst, mobFocus.yFirst);
						}
					}
				}
			}
			else if (!flag)
			{
				Mob mobNext = GetMobNext();
				if (mobNext != null)
				{
					Char.myCharz().focusManualTo(mobNext);
					if (VuDang.dichChuyenPem)
					{
						if (Math.abs(mobNext.xFirst - @char.cx) >= 20 || Math.abs(mobNext.yFirst - @char.cy) >= 20)
						{
							VuDang.GotoXY(mobNext.xFirst, mobNext.yFirst);
						}
						return;
					}
					Move(mobNext.xFirst, mobNext.yFirst);
				}
			}
			Wait(Pk9rPickMob.IsTanSat ? 1 : 500);
		}

		public static void Move(int x, int y)
		{
			Char @char = Char.myCharz();
			if (!Pk9rPickMob.IsVuotDiaHinh)
			{
				@char.currentMovePoint = new MovePoint(x, y);
				return;
			}
			int[] pointYsdMax = GetPointYsdMax(@char.cx, x);
			if (pointYsdMax[1] >= y || (pointYsdMax[1] >= @char.cy && (@char.statusMe == 2 || @char.statusMe == 1)))
			{
				pointYsdMax[0] = x;
				pointYsdMax[1] = y;
			}
			@char.currentMovePoint = new MovePoint(pointYsdMax[0], pointYsdMax[1]);
		}

		private static TpyePickItem GetTpyePickItem(ItemMap itemMap)
		{
			Char @char = Char.myCharz();
			bool flag = itemMap.playerId == @char.charID || itemMap.playerId == -1;
			if (Pk9rPickMob.IsItemMe && !flag)
			{
				return TpyePickItem.CanNotPickItem;
			}
			if (Pk9rPickMob.IsLimitTimesPickItem && itemMap.countAutoPick > Pk9rPickMob.TimesAutoPickItemMax)
			{
				return TpyePickItem.CanNotPickItem;
			}
			if (!FilterItemPick(itemMap))
			{
				return TpyePickItem.CanNotPickItem;
			}
			if (Res.abs(@char.cx - itemMap.xEnd) < 60 && Res.abs(@char.cy - itemMap.yEnd) < 60)
			{
				return TpyePickItem.PickItemNormal;
			}
			if (ItemTime.isExistItem(4387))
			{
				return TpyePickItem.PickItemTDLT;
			}
			if (Pk9rPickMob.IsTanSat)
			{
				return TpyePickItem.PickItemTanSat;
			}
			return TpyePickItem.CanNotPickItem;
		}

		private static bool FilterItemPick(ItemMap itemMap)
		{
			return (Pk9rPickMob.IdItemPicks.Count == 0 || Pk9rPickMob.IdItemPicks.Contains(itemMap.template.id)) && (Pk9rPickMob.IdItemBlocks.Count == 0 || !Pk9rPickMob.IdItemBlocks.Contains(itemMap.template.id)) && (Pk9rPickMob.TypeItemPicks.Count == 0 || Pk9rPickMob.TypeItemPicks.Contains(itemMap.template.type)) && (Pk9rPickMob.TypeItemBlock.Count == 0 || !Pk9rPickMob.TypeItemBlock.Contains(itemMap.template.type));
		}

		private static Mob GetMobTanSat()
		{
			Mob result = null;
			int num = int.MaxValue;
			Char @char = Char.myCharz();
			for (int i = 0; i < GameScr.vMob.size(); i++)
			{
				Mob mob = (Mob)GameScr.vMob.elementAt(i);
				int num2 = (mob.xFirst - @char.cx) * (mob.xFirst - @char.cx) + (mob.yFirst - @char.cy) * (mob.yFirst - @char.cy);
				if (IsMobTanSat(mob) && num2 < num)
				{
					result = mob;
					num = num2;
				}
			}
			return result;
		}

		private static Mob GetMobNext()
		{
			Mob result = null;
			long num = mSystem.currentTimeMillis();
			for (int i = 0; i < GameScr.vMob.size(); i++)
			{
				Mob mob = (Mob)GameScr.vMob.elementAt(i);
				if (IsMobNext(mob) && mob.timeLastDie < num)
				{
					result = mob;
					num = mob.timeLastDie;
				}
			}
			return result;
		}

		private static bool IsMobTanSat(Mob mob)
		{
			if (mob.status == 0 || mob.status == 1 || mob.hp <= 0 || mob.isMobMe)
			{
				return false;
			}
			bool flag = Pk9rPickMob.IsNeSieuQuai && !ItemTime.isExistItem(4387);
			return (mob.levelBoss == 0 || !flag) && FilterMobTanSat(mob);
		}

		private static bool IsMobNext(Mob mob)
		{
			if (mob.isMobMe)
			{
				return false;
			}
			if (!FilterMobTanSat(mob))
			{
				return false;
			}
			if (Pk9rPickMob.IsNeSieuQuai && !ItemTime.isExistItem(4387) && mob.getTemplate().hp >= 3000)
			{
				if (mob.levelBoss != 0)
				{
					Mob mob2 = null;
					bool flag = false;
					for (int i = 0; i < GameScr.vMob.size(); i++)
					{
						mob2 = (Mob)GameScr.vMob.elementAt(i);
						if (mob2.countDie == 10 && (mob2.status == 0 || mob2.status == 1))
						{
							flag = true;
							break;
						}
					}
					if (!flag)
					{
						return false;
					}
					mob.timeLastDie = mob2.timeLastDie;
				}
				else if (mob.countDie == 10 && (mob.status == 0 || mob.status == 1))
				{
					return false;
				}
			}
			return true;
		}

		private static bool FilterMobTanSat(Mob mob)
		{
			return (Pk9rPickMob.IdMobsTanSat.Count == 0 || Pk9rPickMob.IdMobsTanSat.Contains(mob.mobId)) && (Pk9rPickMob.TypeMobsTanSat.Count == 0 || Pk9rPickMob.TypeMobsTanSat.Contains(mob.templateId));
		}

		private static Skill GetSkillAttack()
		{
			Skill skill = null;
			SkillTemplate skillTemplate = new SkillTemplate();
			foreach (sbyte item in Pk9rPickMob.IdSkillsTanSat)
			{
				sbyte b = (skillTemplate.id = item);
				Skill skill2 = Char.myCharz().getSkill(skillTemplate);
				if (IsSkillBetter(skill2, skill))
				{
					skill = skill2;
				}
			}
			return skill;
		}

		private static bool IsSkillBetter(Skill SkillBetter, Skill skill)
		{
			if (SkillBetter == null)
			{
				return false;
			}
			if (!CanUseSkill(SkillBetter))
			{
				return false;
			}
			bool flag = (SkillBetter.template.id == 17 && skill.template.id == 2);
			return skill == null || skill.coolDown < SkillBetter.coolDown || flag;
		}

		private static bool CanUseSkill(Skill skill)
		{
			if (mSystem.currentTimeMillis() - skill.lastTimeUseThisSkill > (long)skill.coolDown + 25L)
			{
				skill.paintCanNotUseSkill = false;
			}
			return (!skill.paintCanNotUseSkill || IdSkillsMelee.Contains(skill.template.id)) && !IdSkillsCanNotAttack.Contains(skill.template.id) && Char.myCharz().cMP >= GetManaUseSkill(skill);
		}

		private static double GetManaUseSkill(Skill skill)
		{
			if (skill.template.manaUseType == 2)
			{
				return 1;
			}
			if (skill.template.manaUseType == 1)
			{
				return skill.manaUse * Char.myCharz().cMPFull / 100;
			}
			return skill.manaUse;
		}

		public static int GetYsd(int xsd)
		{
			Char @char = Char.myCharz();
			int num = TileMap.pxh;
			int result = -1;
			for (int i = 24; i < TileMap.pxh; i += 24)
			{
				if (TileMap.tileTypeAt(xsd, i, 2))
				{
					int num2 = Res.abs(i - @char.cy);
					if (num2 < num)
					{
						num = num2;
						result = i;
					}
				}
			}
			return result;
		}

		private static int[] GetPointYsdMax(int xStart, int xEnd)
		{
			int num = TileMap.pxh;
			int num2 = -1;
			if (xStart > xEnd)
			{
				for (int i = xEnd; i < xStart; i += 24)
				{
					int ysd = GetYsd(i);
					if (ysd < num)
					{
						num = ysd;
						num2 = i;
					}
				}
			}
			else
			{
				for (int num3 = xEnd; num3 > xStart; num3 -= 24)
				{
					int ysd2 = GetYsd(num3);
					if (ysd2 < num)
					{
						num = ysd2;
						num2 = num3;
					}
				}
			}
			return new int[2] { num2, num };
		}

		public static void Wait(int time)
		{
			IsWait = true;
			TimeStartWait = mSystem.currentTimeMillis();
			TimeWait = time;
		}

		public static bool IsWaiting()
		{
			if (IsWait && mSystem.currentTimeMillis() - TimeStartWait >= TimeWait)
			{
				IsWait = false;
			}
			return IsWait;
		}
	}
}
