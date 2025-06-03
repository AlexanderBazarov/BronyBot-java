package ru.untitled_devs.core.client;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.storage.StorageKey;
import ru.untitled_devs.core.fsm.storage.Storage;
import ru.untitled_devs.core.middlewares.Middleware;
import ru.untitled_devs.core.routers.Router;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static ru.untitled_devs.core.utils.FileUtils.getImageFileNameWithExtension;

public class Bot extends TelegramLongPollingBot implements BotClient {
    private final String botUsername;
    private final List<Router> routers = new ArrayList<>();
    private final Storage storage;
    private final List<Middleware> middlewares = new ArrayList<>();

    public Bot(String botToken, String botUsername, Storage storage) {
        super(botToken);
        this.botUsername = botUsername;
        this.storage = storage;
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    public void addRouter(Router router) {
        this.routers.add(router);
    }

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
            System.err.println(e.getMessage());
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
    public void editMessageText(long chatId, int messageId, String newText) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId(messageId);
        messageText.setText(newText);

        try {
            execute(messageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendTypingAction(long chatId) {

    }

    @Override
    public void sendPhoto(long chatId, String caption, byte[] photo) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(caption);

        InputFile file = new InputFile(new ByteArrayInputStream(photo), getImageFileNameWithExtension(photo));
        sendPhoto.setPhoto(file);
    }

    @Override
    public void answerCallbackQuery(String callbackQueryId, String text, boolean showAlert) {

    }

    @Override
    public void onUpdateReceived(Update update) {
        UpdateContext updateContext = new UpdateContext(update);
        if (updateContext.getChatId() == null || updateContext.getUserId() == null) {
            return;
        }
        StorageKey key = new StorageKey(updateContext.getChatId(), updateContext.getUserId());

        for (Middleware middleware : this.middlewares) {
            if (!middleware.preHandle(update)){
                return;
            }
        }

       for (Router router : this.routers) {
           router.routeUpdate(update, this.storage.getOrCreateContext(key));
       }
    }

}
