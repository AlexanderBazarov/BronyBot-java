package ru.untitled_devs.bot.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.untitled_devs.core.dispatcher.Dispatcher;
import ru.untitled_devs.core.fsm.storage.InMemoryStorage;
import ru.untitled_devs.core.fsm.storage.Storage;
import ru.untitled_devs.core.routers.scenes.SceneManager;

public class CoreModule extends AbstractModule {
	@Override protected void configure() {}

	@Provides @Singleton
	Logger logger() {
		return LoggerFactory.getLogger("bot");
	}

	@Provides @Singleton
	Storage storage() {
		return new InMemoryStorage();
	}

	@Provides @Singleton
	SceneManager sceneManager() {
		return new SceneManager();
	}

	@Provides @Singleton
	Dispatcher dispatcher(Storage storage, SceneManager sceneManager) {
		return new Dispatcher(storage, sceneManager);
	}
}
