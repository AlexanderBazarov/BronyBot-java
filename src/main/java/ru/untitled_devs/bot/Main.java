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
import ru.untitled_devs.bot.features.localisation.LocalisationRouter;
import ru.untitled_devs.bot.features.localisation.midlewares.LocalisationMiddleware;
import ru.untitled_devs.bot.features.registration.RegistrationRouter;
import ru.untitled_devs.bot.features.registration.middlewares.LoginMiddleware;
import ru.untitled_devs.bot.features.start.StartRouter;
import ru.untitled_devs.bot.shared.geocoder.Geocoder;
import ru.untitled_devs.bot.shared.geocoder.yandex.YandexGeocoder;
import ru.untitled_devs.core.client.PollingClient;
import ru.untitled_devs.core.fsm.storage.InMemoryStorage;

public class Main {
    static InMemoryStorage storage = new InMemoryStorage();
    protected static final Logger logger = LogManager.getLogger();
	private static final Geocoder geocoder =
		new YandexGeocoder(Config.getGeocodingConfig().getApiUrl(),
			Config.getGeocodingConfig().getApiKey());


    public static void main(String[] args) {
        try {
			MongoClient client = MongoClients.create(Config.getMongoConfig().getMongoString());
			Datastore datastore = Morphia.createDatastore(client, Config.getMongoConfig().getMongoDBName());

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            PollingClient bot = new PollingClient(Config.getBotConfig().getBotToken(),
				Config.getBotConfig().getBotName(), storage,logger);

			bot.addMiddleware(new LocalisationMiddleware());
			bot.addMiddleware(new LoginMiddleware());

            bot.addRouter(new StartRouter(bot));
			bot.addRouter(new RegistrationRouter(bot, datastore, geocoder));
			bot.addRouter(new LocalisationRouter(bot));

            botsApi.registerBot(bot);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
