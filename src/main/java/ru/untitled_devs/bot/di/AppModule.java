package ru.untitled_devs.bot.di;

import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {
	@Override protected void configure() {
		install(new ConfigModule());
		install(new CoreModule());
		install(new MongoModule());
		install(new SharedModule());
		install(new FeaturesModule());
		install(new TelegramModule());
	}
}
