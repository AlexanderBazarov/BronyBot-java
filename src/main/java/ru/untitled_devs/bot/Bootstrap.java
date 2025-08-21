package ru.untitled_devs.bot;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import ru.untitled_devs.bot.features.localisation.LocalisationMiddleware;
import ru.untitled_devs.bot.features.localisation.LocalisationScene;
import ru.untitled_devs.bot.features.menu.MainMenuScene;
import ru.untitled_devs.bot.features.prfile.ProfileScene;
import ru.untitled_devs.bot.features.registration.LoginMiddleware;
import ru.untitled_devs.bot.features.registration.RegistrationScene;
import ru.untitled_devs.bot.features.start.StartMiddleware;
import ru.untitled_devs.core.client.PollingClient;
import ru.untitled_devs.core.dispatcher.Dispatcher;
import ru.untitled_devs.core.routers.scenes.SceneManager;

public class Bootstrap {
	private final Logger logger;
	private final TelegramBotsApi botsApi;
	private final PollingClient bot;
	private final Dispatcher dispatcher;
	private final SceneManager scenes;

	private final RegistrationScene registrationScene;
	private final LocalisationScene localisationScene;
	private final MainMenuScene mainMenuScene;
	private final ProfileScene profileScene;

	private final LocalisationMiddleware localisationMiddleware;
	private final LoginMiddleware loginMiddleware;
	private final StartMiddleware startMiddleware;

	@Inject
	public Bootstrap(
		Logger logger,
		TelegramBotsApi botsApi,
		PollingClient bot,
		Dispatcher dispatcher,
		SceneManager scenes,
		RegistrationScene registrationScene,
		LocalisationScene localisationScene,
		MainMenuScene mainMenuScene,
		ProfileScene profileScene,
		LocalisationMiddleware localisationMiddleware,
		LoginMiddleware loginMiddleware,
		StartMiddleware startMiddleware
	) {
		this.logger = logger;
		this.botsApi = botsApi;
		this.bot = bot;
		this.dispatcher = dispatcher;
		this.scenes = scenes;
		this.registrationScene = registrationScene;
		this.localisationScene = localisationScene;
		this.mainMenuScene = mainMenuScene;
		this.profileScene = profileScene;
		this.localisationMiddleware = localisationMiddleware;
		this.loginMiddleware = loginMiddleware;
		this.startMiddleware = startMiddleware;
	}

	public void start() {
		try {
			scenes.register("register", registrationScene);
			scenes.register("lang", localisationScene);
			scenes.register("menu", mainMenuScene);
			scenes.register("profile", profileScene);

			dispatcher.addMiddleware(localisationMiddleware);
			dispatcher.addMiddleware(loginMiddleware);
			dispatcher.addMiddleware(startMiddleware);

			botsApi.registerBot(bot);

			logger.info("Bot up.");
		} catch (Exception e) {
			logger.error("Boot error: {}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
}
