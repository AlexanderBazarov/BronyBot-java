package ru.untitled_devs.core.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.untitled_devs.core.middlewares.Middleware;
import ru.untitled_devs.core.routers.Router;

import java.util.ArrayList;
import java.util.List;

public class TestClient implements BotClient {
    private final List<Router> routers = new ArrayList<>();
    private final List<Middleware> middlewares = new ArrayList<>();

    protected static final Logger logger = LogManager.getLogger();

    @Override
    public void addMiddleware(Middleware middleware) {
        this.middlewares.add(middleware);
    }

    @Override
    public void addRouter(Router router) {
        this.routers.add(router);
    }

    @Override
    public void sendMessage(long chatId, String text) {
        logger.info("Sent message in chat {} with text '{}'", chatId, text);
    }

    @Override
    public void banChatMember(long chatId, long userId, int duration) {
        logger.info("Banned user {} in chat {} for {} seconds", userId, chatId, duration);
    }

    @Override
    public void editMessageText(long chatId, int messageId, String newText) {
        logger.info("Edited message {} in chat {} with new text '{}'", messageId, chatId, newText);
    }

    @Override
    public void editMessageReplyMarkup(long chatId, int messageId, InlineKeyboardMarkup replyKeyboard) {
        logger.info("Edited reply markup of message {} in chat {}", messageId, chatId);
    }

    @Override
    public void deleteMessage(long chatId, int messageId) {
        logger.info("Deleted message {} in chat {}", messageId, chatId);
    }

    @Override
    public void sendPhoto(long chatId, String caption, byte[] photo) {
        logger.info("Sent photo in chat {} with caption '{}'", chatId, caption);
    }

    @Override
    public void answerCallbackQuery(String callbackQueryId, String text, boolean showAlert) {
        logger.info("Answered callback query {} with text '{}' and showAlert={}", callbackQueryId, text, showAlert);
    }

    void simulateUserMessage(Message message) {

    }

}
