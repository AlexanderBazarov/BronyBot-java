package ru.untitled_devs.bot;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.untitled_devs.bot.config.Config;
import ru.untitled_devs.bot.features.localisation.LocalisationScene;
import ru.untitled_devs.bot.features.localisation.midlewares.LocalisationMiddleware;
import ru.untitled_devs.bot.features.registration.RegistrationScene;
import ru.untitled_devs.bot.features.registration.middlewares.LoginMiddleware;
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
		Logger logger = LogManager.getLogger();
		SceneManager sceneManager = new SceneManager();
		Dispatcher dispatcher = new Dispatcher(storage, logger, sceneManager);

		MongoClient client = MongoClients.create(Config.getMongoConfig().getMongoString());
		Datastore datastore = Morphia.createDatastore(client, Config.getMongoConfig().getMongoDBName());

		Geocoder geocoder =
			new YandexGeocoder(Config.getGeocodingConfig().getApiUrl(),
				Config.getGeocodingConfig().getApiKey());
		ImageService imageService = new ImageService(Config.getImagesConfig().getImagesPath(), datastore);

		try {
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            PollingClient bot = new PollingClient(Config.getBotConfig().getBotToken(),
				Config.getBotConfig().getBotName(), dispatcher, logger);

			sceneManager.register(new RegistrationScene(bot, datastore, geocoder, imageService));
			sceneManager.register(new LocalisationScene(bot));

			dispatcher.addMiddleware(new LocalisationMiddleware(sceneManager));
			dispatcher.addMiddleware(new LoginMiddleware(sceneManager));

			dispatcher.addRouter(new StartRouter(bot));
			dispatcher.addRouter(new LocalisationScene(bot));

            botsApi.registerBot(bot);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
