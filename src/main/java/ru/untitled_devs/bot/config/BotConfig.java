package ru.untitled_devs.bot.config;

public final class BotConfig {
	private String botToken;
	private String botName;

	public BotConfig (){loadVariables();}

	private void loadVariables() {
		botToken = System.getenv("BOT_TOKEN");
		botName = System.getenv("BOT_NAME");
	}

	public String getBotToken() {
		return botToken;
	}

	public String getBotName() {
		return botName;
	}
}
