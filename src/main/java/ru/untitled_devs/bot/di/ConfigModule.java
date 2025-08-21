package ru.untitled_devs.bot.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import ru.untitled_devs.bot.config.*;

public class ConfigModule extends AbstractModule {
	@Override
	protected void configure() {

	}

	@Provides @Singleton BotConfig botConfig() { return new BotConfig(); }
	@Provides @Singleton MongoConfig mongoConfig() { return new MongoConfig(); }
	@Provides @Singleton GeocodingConfig geocodingConfig() { return new GeocodingConfig(); }
	@Provides @Singleton S3Config s3Config() { return new S3Config(); }
}
