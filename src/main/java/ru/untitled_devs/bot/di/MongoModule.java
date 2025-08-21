package ru.untitled_devs.bot.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import ru.untitled_devs.bot.config.MongoConfig;

public class MongoModule extends AbstractModule {
	@Override
	protected void configure() {

	}

	@Provides @Singleton
	MongoClient mongoClient(MongoConfig cfg) {
		return MongoClients.create(cfg.getMongoString());
	}

	@Provides @Singleton
	Datastore datastore(MongoClient client, MongoConfig cfg) {
		return Morphia.createDatastore(client, cfg.getMongoDBName());
	}


}
