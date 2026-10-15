using System.Collections.Generic;


namespace Game3.AssemblyCSharp.Mod.Xmap
{

	public class XmapController : IActionListener
	{
		public static int VuDangMapNext;

		private static int step;

		private static readonly XmapController _Instance = new XmapController();

		public static int IdMapEnd;

		private static List<int> WayXmap;

		private static int IndexWay;

		private static bool IsNextMapFailed;

		private static bool IsWait;

		private static long TimeStartWait;

		private static long TimeWait;

		private static bool IsWaitNextMap;

		public static void Update()
		{
			if (IsWaiting() || XmapData.Instance().IsLoading)
			{
				return;
			}
			if (IsWaitNextMap)
			{
				Wait(200);
				IsWaitNextMap = false;
				return;
			}
			if (IsNextMapFailed)
			{
				XmapData.Instance().MyLinkMaps = null;
				WayXmap = null;
				IsNextMapFailed = false;
				return;
			}
			if (WayXmap == null)
			{
				if (XmapData.Instance().MyLinkMaps == null)
				{
					XmapData.Instance().LoadLinkMaps();
					return;
				}
				WayXmap = XmapAlgorithm.FindWay(TileMap.mapID, IdMapEnd);
				IndexWay = 0;
				if (WayXmap == null)
				{
					GameScr.info1.addInfo("Không thể tìm thấy đường đi", 0);
					FinishXmap();
					return;
				}
			}
			if (TileMap.mapID == WayXmap[WayXmap.Count - 1] && !XmapData.IsMyCharDie())
			{
				GameScr.info1.addInfo("Đã đến: " + get_map_names(TileMap.mapID), 0);
				FinishXmap();
			}
			else if (TileMap.mapID == WayXmap[IndexWay])
			{
				if (XmapData.IsMyCharDie())
				{
					Service.gI().returnTownFromDead();
					IsWaitNextMap = (IsNextMapFailed = true);
				}
				else if (XmapData.CanNextMap())
				{
					NextMap(WayXmap[IndexWay + 1]);
					IsWaitNextMap = true;
				}
				Wait(500);
			}
			else if (TileMap.mapID == WayXmap[IndexWay + 1])
			{
				IndexWay++;
			}
			else
			{
				IsNextMapFailed = true;
			}
		}

		public void perform(int idAction, object p)
		{
			if (idAction == 1)
			{
				ShowPanelXmap((List<int>)p);
			}
		}

		private static void Wait(int time)
		{
			IsWait = true;
			TimeStartWait = mSystem.currentTimeMillis();
			TimeWait = time;
		}

		private static bool IsWaiting()
		{
			if (IsWait && mSystem.currentTimeMillis() - TimeStartWait >= TimeWait)
			{
				IsWait = false;
			}
			return IsWait;
		}


		public static void ShowXmapMenu()
		{
			string groupMapsData = "Trái đất:42 21 0 1 2 3 4 5 6 27 28 29 30 47 24" +
                ",Namec:43 22 7 8 9 11 12 13 10 31 32 33 34 25" +
                ",Xayda:44 23 14 15 16 17 18 20 19 35 36 37 38 26 52 84" +
                ",Nappa:68 69 70 71 72 64 65 63 66 67 73 74 75 76 77 81 82 83 79 80 131 132 133" +
                ",Tương lai:102 92 93 94 96 97 98 99 100 103" +
                ",Cold:109 108 107 110 106 105";

			XmapData.Instance().LoadGroupMapsFromString(groupMapsData);
			MyVector myVector = new MyVector();
			foreach (GroupMap groupMap in XmapData.Instance().GroupMaps)
			{
				myVector.addElement(new Command(groupMap.NameGroup, _Instance, 1, groupMap.IdMaps));
			}
			GameCanvas.menu.startAt(myVector, 3);
		}

		public static string get_map_names(int id)
		{
			string result = TileMap.mapName;
			if (id >= 0 && id <= VuDang.MapNames.Length && VuDang.MapNames[id] != null && VuDang.MapNames[id] != string.Empty)
			{
				result = VuDang.MapNames[id];
			}
			return result;
		}

		public static void ShowPanelXmap(List<int> idMaps)
		{
			Pk9rXmap.IsMapTransAsXmap = true;
			int count = idMaps.Count;
			GameCanvas.panel.mapNames = new string[count];
			GameCanvas.panel.planetNames = new string[count];
			for (int i = 0; i < count; i++)
			{
				string text = get_map_names(idMaps[i]);
				GameCanvas.panel.mapNames[i] = idMaps[i] + ": " + text;
				GameCanvas.panel.planetNames[i] = "";
			}
			GameCanvas.panel.setTypeMapTrans();
			GameCanvas.panel.show();
		}

		public static void StartRunToMapId(int idMap)
		{
			IdMapEnd = idMap;
			Pk9rXmap.IsXmapRunning = true;
		}

		public static void FinishXmap()
		{
			Pk9rXmap.IsXmapRunning = false;
			IsNextMapFailed = false;
			XmapData.Instance().MyLinkMaps = null;
			WayXmap = null;
			GameCanvas.panel.hide();
		}

		public static void SaveIdMapCapsuleReturn()
		{
			Pk9rXmap.IdMapCapsuleReturn = TileMap.mapID;
		}

		private static void NextMap(int idMapNext)
		{
			VuDangMapNext = idMapNext;
			List<MapNext> mapNexts = XmapData.Instance().GetMapNexts(TileMap.mapID);
			if (mapNexts != null)
			{
				foreach (MapNext item in mapNexts)
				{
					if (item.MapID == idMapNext)
					{
						NextMap(item);
						return;
					}
				}
			}
			GameScr.info1.addInfo("Lỗi tại dữ liệu", 0);
		}

		private static void NextMap(MapNext mapNext)
		{
			switch (mapNext.Type)
			{
				case TypeMapNext.AutoWaypoint:
					NextMapAutoWaypoint(mapNext);
					break;
				case TypeMapNext.NpcMenu:
					NextMapNpcMenu(mapNext);
					break;
				case TypeMapNext.NpcPanel:
					NextMapNpcPanel(mapNext);
					break;
				case TypeMapNext.Position:
					NextMapPosition(mapNext);
					break;
				case TypeMapNext.Capsule:
					NextMapCapsule(mapNext);
					break;
			}
		}

		public static int TeleMore(int x)
		{
			if (x < TileMap.pxw - TileMap.pxw + 20)
			{
				return 100;
			}
			return -100;
		}

		private static void NextMapAutoWaypoint(MapNext mapNext)
		{
			Waypoint waypoint = XmapData.FindWaypoint(mapNext.MapID);
			if (waypoint != null)
			{
				int posWaypointX = XmapData.GetPosWaypointX(waypoint);
				int posWaypointY = XmapData.GetPosWaypointY(waypoint);
				MoveMyChar(posWaypointX, posWaypointY);
				Char.myCharz().currentMovePoint = new MovePoint(posWaypointX, posWaypointY);
				RequestChangeMap(waypoint);
			}
		}

		private static void NextMapNpcMenu(MapNext mapNext)
		{
			int num = mapNext.Info[0];
			if (GameScr.findNPCInMap((short)num) == null)
			{
				fixtl();
				return;
			}
			VuDang.GotoNpc(num);
			Service.gI().openMenu(num);
			for (int i = 0; i < GameCanvas.menu.menuItems.size(); i++)
			{
				if (((Command)GameCanvas.menu.menuItems.elementAt(i)).caption.Trim().ToLower().Contains("tương lai") && VuDangMapNext >= 92 && VuDangMapNext <= 103)
				{
					Service.gI().confirmMenu((short)num, (sbyte)i);
					return;
				}
				if (((Command)GameCanvas.menu.menuItems.elementAt(i)).caption.Trim().ToLower().Contains("yardart") && VuDangMapNext >= 131 && VuDangMapNext <= 133)
				{
					Service.gI().confirmMenu((short)num, (sbyte)i);
					return;
				}
				if (VuDangMapNext >= 161 && VuDangMapNext <= 164)
				{
					if (((Command)GameCanvas.menu.menuItems.elementAt(i)).caption.Trim().ToLower().Contains("thực vật"))
					{
						Service.gI().confirmMenu((short)num, (sbyte)i);
					}
					else
					{
						Service.gI().useItem(0, 1, (sbyte)VuDang.FindIndexItem(992), -1);
					}
					return;
				}
			}
			for (int j = 1; j < mapNext.Info.Length; j++)
			{
				int num2 = mapNext.Info[j];
				Service.gI().confirmMenu((short)num, (sbyte)num2);
			}
		}

		private static void fixtl()
		{
			if (TileMap.mapID == 27)
			{
				NextMap(28);
				IsWaitNextMap = true;
				step = 0;
			}
			else if (TileMap.mapID == 29)
			{
				NextMap(28);
				IsWaitNextMap = true;
				step = 1;
			}
			else if (step == 0)
			{
				NextMap(29);
				IsWaitNextMap = true;
			}
			else if (step == 1)
			{
				NextMap(27);
				IsWaitNextMap = true;
			}
		}

		private static void NextMapNpcPanel(MapNext mapNext)
		{
			int num = mapNext.Info[0];
			int num2 = mapNext.Info[1];
			int selected = mapNext.Info[2];
			VuDang.GotoNpc(num);
			Service.gI().openMenu(num);
			Service.gI().confirmMenu((short)num, (sbyte)num2);
			Service.gI().requestMapSelect(selected);
		}

		private static void NextMapPosition(MapNext mapNext)
		{
			int x = mapNext.Info[0];
			int y = mapNext.Info[1];
			MoveMyChar(x, y);
			Service.gI().requestChangeMap();
			Service.gI().getMapOffline();
		}

		private static void NextMapCapsule(MapNext mapNext)
		{
			SaveIdMapCapsuleReturn();
			int selected = mapNext.Info[0];
			Service.gI().requestMapSelect(selected);
		}

		public static void UseCapsuleNormal()
		{
			Pk9rXmap.IsShowPanelMapTrans = false;
			Service.gI().useItem(0, 1, (sbyte)VuDang.FindIndexItem(193), -1);
		}

		public static void UseCapsuleVip()
		{
			Pk9rXmap.IsShowPanelMapTrans = false;
			Service.gI().useItem(0, 1, (sbyte)VuDang.FindIndexItem(194), -1);
		}

		public static void HideInfoDlg()
		{
			InfoDlg.hide();
		}

		private static void MoveMyChar(int x, int y)
		{
			VuDang.GotoXY(x, y);
		}

		private static void RequestChangeMap(Waypoint waypoint)
		{
			waypoint.popup.command.performAction();
			if (waypoint.isOffline)
			{
				Service.gI().getMapOffline();
			}
			else
			{
				Service.gI().requestChangeMap();
			}
		}
	}
}
