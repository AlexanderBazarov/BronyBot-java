package ru.untitled_devs.bot;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.untitled_devs.bot.config.Config;
import ru.untitled_devs.bot.features.localisation.LocalisationScene;
import ru.untitled_devs.bot.features.localisation.LocalisationMiddleware;
import ru.untitled_devs.bot.features.menu.MainMenuScene;
import ru.untitled_devs.bot.features.registration.RegistrationScene;
import ru.untitled_devs.bot.features.registration.LoginMiddleware;
import ru.untitled_devs.bot.features.registration.RegistrationService;
import ru.untitled_devs.bot.features.start.StartRouter;
import ru.untitled_devs.bot.shared.geocoder.Geocoder;
import ru.untitled_devs.bot.shared.geocoder.yandex.YandexGeocoder;
import ru.untitled_devs.bot.shared.image.ImageService;
import ru.untitled_devs.core.client.PollingClient;
import ru.untitled_devs.core.dispatcher.Dispatcher;
import ru.untitled_devs.core.fsm.storage.InMemoryStorage;
import ru.untitled_devs.core.routers.scenes.SceneManager;

public class Main {
    public static void main(String[] args) {
		InMemoryStorage storage = new InMemoryStorage();
		SceneManager sceneManager = new SceneManager();
		Logger logger = LoggerFactory.getLogger(Main.class);
		Dispatcher dispatcher = new Dispatcher(storage, sceneManager);

		MongoClient client = MongoClients.create(Config.getMongoConfig().getMongoString());
		Datastore datastore = Morphia.createDatastore(client, Config.getMongoConfig().getMongoDBName());

		Geocoder geocoder =
			new YandexGeocoder(Config.getGeocodingConfig().getApiUrl(),
				Config.getGeocodingConfig().getApiKey());
		ImageService imageService = new ImageService(Config.getImagesConfig().getImagesPath(), datastore);
		RegistrationService regService = new RegistrationService(datastore);

		try {
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            PollingClient bot = new PollingClient(Config.getBotConfig().getBotToken(),
				Config.getBotConfig().getBotName(), dispatcher);

			sceneManager.register("register", new RegistrationScene(bot, regService, geocoder, imageService, sceneManager));
			sceneManager.register("lang", new LocalisationScene(bot));
			sceneManager.register("menu", new MainMenuScene(bot, sceneManager));

			dispatcher.addMiddleware(new LocalisationMiddleware(sceneManager));
			dispatcher.addMiddleware(new LoginMiddleware(sceneManager, regService));

			dispatcher.addRouter(new StartRouter(bot));
			dispatcher.addRouter(new LocalisationScene(bot));

            botsApi.registerBot(bot);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
