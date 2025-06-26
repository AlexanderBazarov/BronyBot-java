package ru.untitled_devs.core.client;


import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.fsm.storage.StorageKey;
import ru.untitled_devs.core.fsm.storage.Storage;
import ru.untitled_devs.core.middlewares.Middleware;
import ru.untitled_devs.core.routers.Router;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


import static ru.untitled_devs.core.utils.FileUtils.getImageFileNameWithExtension;

public class PollingClient extends TelegramLongPollingBot implements BotClient {
    private final String botUsername;
    private final List<Router> routers = new ArrayList<>();
    private final Storage storage;
    private final List<Middleware> middlewares = new ArrayList<>();
    private final Logger logger;

    public PollingClient(String botToken, String botUsername, Storage storage, Logger logger) {
        super(botToken);
        this.botUsername = botUsername;
        this.storage = storage;
        this.logger = logger;
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public void addRouter(Router router) {
        this.routers.add(router);
    }

    @Override
    public void addMiddleware(Middleware middleware) {
        this.middlewares.add(middleware);
    }

    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            this.logger.error(e.getMessage());
        }
    }

    public void sendMessage(long chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(replyKeyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            this.logger.error(e.getMessage());
        }
    }

    @Override
    public void editMessageText(long chatId, int messageId, String newText) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId(messageId);
        messageText.setText(newText);

        try {
            execute(messageText);
        } catch (TelegramApiException e) {
            this.logger.error(e.getMessage());
        }

    }

    @Override
    public void editMessageReplyMarkup(long chatId, int messageId, InlineKeyboardMarkup replyKeyboard) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(chatId);
        editMessageReplyMarkup.setMessageId(messageId);
        editMessageReplyMarkup.setReplyMarkup(replyKeyboard);

        try {
            execute(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            this.logger.error(e.getMessage());
        }
    }

    @Override
    public void deleteMessage(long chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);

        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            this.logger.error(e.getMessage());
        }
    }

    @Override
    public void sendPhoto(long chatId, String caption, byte[] photo) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(caption);

        InputFile file = new InputFile(new ByteArrayInputStream(photo), getImageFileNameWithExtension(photo));
        sendPhoto.setPhoto(file);

        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            this.logger.error(e.getMessage());
        }
    }

    @Override
    public void answerCallbackQuery(String callbackQueryId, String text, boolean showAlert) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQueryId);
        answerCallbackQuery.setText(text);
        answerCallbackQuery.setShowAlert(showAlert);

        try {
            execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            this.logger.error(e.getMessage());
        }
    }

    public void banChatMember(long chatId, long userId, int duration) {
        BanChatMember banChatMember = new BanChatMember();
        banChatMember.setChatId(chatId);
        banChatMember.setUserId(userId);

        if (duration > 0) {
            long untilDate = Instant.now().getEpochSecond() + duration;
            banChatMember.setUntilDate((int) untilDate);
        }

        try {
            execute(banChatMember);
        } catch (TelegramApiException e) {
            System.err.println(e.getMessage());
        }

    }

    @Override
    public void onUpdateReceived(Update update) {
        this.handleUpdateAsync(update);
    }

    public Future<?> handleUpdateAsync(Update update) {
        return CompletableFuture
                .runAsync(() -> processUpdate(update))
                .whenComplete((v, ex) -> {
                    if (ex != null) logger.error("Update crashed", ex);
                    else logger.debug("Update {} processed", update.getUpdateId());
                });
    }

    private void processUpdate(Update update) {
        UpdateContext updateContext = new UpdateContext(update);
        if (updateContext.getChatId() == null || updateContext.getUserId() == null) {
            return;
        }

        StorageKey key = new StorageKey(updateContext.getChatId(), updateContext.getUserId());

		FSMContext context = this.storage.getOrCreateContext(key);

        for (Middleware middleware : this.middlewares) {
            try {
                if (!middleware.preHandle(update, context)){
                    logger.debug("Middleware prevented handling update: {}", update);
                    break;
                }
            } catch (Exception e) {
                logger.error("Exception in middleware: ", e);
                return;
            }
        }

        for (Router router : this.routers) {
            try {
                if (router.routeUpdate(update, context)) {
					return;
				}
            } catch (Exception e) {
                logger.error("Exception in router {} while handling update: {}", router.getClass().getSimpleName(), update, e);
            }
        }
    }

}
