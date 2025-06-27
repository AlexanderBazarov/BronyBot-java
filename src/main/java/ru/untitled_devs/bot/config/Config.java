package ru.untitled_devs.bot.config;

public final class Config {
	private static final BotConfig botConfig = new BotConfig();
	private static final MongoConfig mongoConfig = new MongoConfig();
	private static final GeocodingConfig geocodingConfig = new GeocodingConfig();

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
