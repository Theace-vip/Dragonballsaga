namespace Game2
{
	internal class Setup : mScreen, IActionListener, IChatable
	{
		public static Setup instance;

		public static Setup gI()
		{
			if (instance == null)
			{
				return instance = new Setup();
			}
			return instance;
		}

		public void onChatFromMe(string text, string to)
		{
			string strChat = ChatTextField.gI().strChat;
			ChatTextField.gI().isShow = false;
			ChatTextField.gI().tfChat.setIputType(0);
			ChatTextField.gI().parentScreen = GameScr.gI();
			ChatTextField.gI().strChat = "Chat ";
			ChatTextField.gI().tfChat.name = "chat ";
		}

		public void perform(int idAction, object p)
		{
			switch (idAction)
			{
				case 1:
					{
						string[] array7 = (string[])p;
						ChatTextField.gI().VuDangStartChat(gI(), array7[0], array7[1]);
						break;
					}
				case 2:
					{
						string[] array6 = (string[])p;
						ChatTextField.gI().VuDangStartChat(gI(), array6[0], array6[1]);
						break;
					}
				case 3:
					{
						string[] array5 = (string[])p;
						ChatTextField.gI().VuDangStartChat(gI(), array5[0], array5[1]);
						break;
					}
				case 4:
					{
						string[] array4 = (string[])p;
						ChatTextField.gI().VuDangStartChat(gI(), array4[0], array4[1]);
						break;
					}
				case 5:
					{
						VuDang.itemBuy = GameCanvas.panel.currItem;
						string[] array3 = (string[])p;
						GameCanvas.panel.VuDangChatTextField(array3[0], array3[1]);
						break;
					}
				case 6:
					{
						string[] array2 = (string[])p;
						ChatTextField.gI().VuDangStartChat(gI(), array2[0], array2[1]);
						break;
					}
				case 7:
					{
						string[] array = (string[])p;
						GameCanvas.panel.VuDangChatTextField(array[0], array[1]);
						break;
					}
				case 8:
					{
						string[] array8 = (string[])p;
						GameCanvas.panel.VuDangChatTextField(array8[0], array8[1]);
						break;
					}
				case 9:
					{
						string[] array9 = (string[])p;
						GameCanvas.panel.VuDangChatTextField(array9[0], array9[1]);
						break;
					}
				case 10:
					{
						string[] array10 = (string[])p;
						GameCanvas.panel.VuDangChatTextField(array10[0], array10[1]);
						break;
					}
				case 11:
					{
						string[] array11 = (string[])p;
						GameCanvas.panel.VuDangChatTextField(array11[0], array11[1]);
						break;
					}
			}
		}

		public void onCancelChat()
		{
			ChatTextField.gI().center = null;
		}

		public override void paint(mGraphics g)
		{
			ChatTextField.gI().paint(g);
		}

		public override void update()
		{
			ChatTextField.gI().update();
		}
	}
}