package ru.untitled_devs.bot.config;

public final class Config {
	private static final BotConfig botConfig = new BotConfig();
	private static final MongoConfig mongoConfig = new MongoConfig();
	private static final GeocodingConfig geocodingConfig = new GeocodingConfig();
	private static final ImagesConfig imagesConfig = new ImagesConfig();

	public static BotConfig getBotConfig() {
		return botConfig;
	}

	public static MongoConfig getMongoConfig() {
		return mongoConfig;
	}

	public static GeocodingConfig getGeocodingConfig() {
		return geocodingConfig;
	}

	public static ImagesConfig getImagesConfig() {
		return imagesConfig;
	}
}
