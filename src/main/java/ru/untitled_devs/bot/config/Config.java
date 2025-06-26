package ru.untitled_devs.bot.config;

public final class Config {
	private static BotConfig botConfig = new BotConfig();
	private static MongoConfig mongoConfig = new MongoConfig();
	private static GeocodingConfig geocodingConfig = new GeocodingConfig();

	public static BotConfig getBotConfig() {
		return botConfig;
	}

	public static MongoConfig getMongoConfig() {
		return mongoConfig;
	}

	public static GeocodingConfig getGeocodingConfig() {
		return geocodingConfig;
	}
}
