package ru.untitled_devs.core;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.untitled_devs.core.routers.Router;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    private final String botToken;
    private final String botUsername;

    private List<Router> routers = new ArrayList<>();

    public Bot(String botToken, String botUsername) {
        super(botToken);
        this.botToken = botToken;
        this.botUsername = botUsername;
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }


    public void addRouter(Router router) {
        this.routers.add(router);
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
    public void onUpdateReceived(Update update) {
       for (Router router : this.routers) {
           router.routeUpdate(update);
       }
    }


}
