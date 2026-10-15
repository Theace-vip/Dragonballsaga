namespace Game2
{
	public class ItemOption
	{
		public long param;

		public sbyte active;

		public sbyte activeCard;

		public ItemOptionTemplate optionTemplate;

		public ItemOption()
		{
		}

		public ItemOption(int optionTemplateId, long param)
		{
			if (optionTemplateId == 22)
			{
				optionTemplateId = 6;
				param *= 1000;
			}
			if (optionTemplateId == 23)
			{
				optionTemplateId = 7;
				param *= 1000;
			}
			this.param = param;
			optionTemplate = GameScr.gI().iOptionTemplates[optionTemplateId];
		}

		public string getOptionString()
		{
			return NinjaUtil.replace(optionTemplate.name, "#", NinjaUtil.getMoneys(param) + string.Empty);
		}

		public string getOptionName()
		{
			return NinjaUtil.replace(optionTemplate.name, "+#", string.Empty);
		}

		public string getOptiongColor()
		{
			return NinjaUtil.replace(optionTemplate.name, "$", string.Empty);
		}
	}
}