package ru.untitled_devs.bot.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.untitled_devs.bot.config.BotConfig;
import ru.untitled_devs.core.client.BotClient;
import ru.untitled_devs.core.client.PollingClient;
import ru.untitled_devs.core.dispatcher.Dispatcher;

public class TelegramModule extends AbstractModule {
	@Override protected void configure() {
	}

	@Provides @Singleton
	TelegramBotsApi botsApi() throws Exception {
		return new TelegramBotsApi(DefaultBotSession.class);
	}

	@Provides @Singleton
	PollingClient pollingClient(BotConfig cfg, Dispatcher dispatcher) {
		return new PollingClient(cfg.getBotToken(), cfg.getBotName(), dispatcher);
	}

	@Provides @Singleton
	BotClient botClient(PollingClient client) {
		return client;
	}
}
