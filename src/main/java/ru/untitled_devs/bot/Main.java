package ru.untitled_devs.bot;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.untitled_devs.core.client.Bot;
import ru.untitled_devs.core.fsm.storage.InMemoryStorage;

public class Main {
    static Config config = new Config();
    static InMemoryStorage storage = new InMemoryStorage();
    protected static final Logger logger = LogManager.getLogger();


    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            Bot bot = new Bot(config.getBotToken(), config.getBotName(), storage,logger);
            botsApi.registerBot(bot);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}