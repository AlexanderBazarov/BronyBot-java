package ru.untitled_devs.core.test;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;

public class UpdateFactory {

    public static Update fromCallbackQuery(CallbackQuery callbackQuery) {
        Update update = new Update();
        update.setCallbackQuery(callbackQuery);
        return update;
    }

    public static Update fromInlineQuery(InlineQuery inlineQuery) {
        Update update = new Update();
        update.setInlineQuery(inlineQuery);
        return update;
    }

    public static Update fromEditedMessage(Message editedMessage) {
        Update update = new Update();
        update.setEditedMessage(editedMessage);
        return update;
    }

    public static Update fromChannelPost(Message channelPost) {
        Update update = new Update();
        update.setChannelPost(channelPost);
        return update;
    }

    public static Update fromEditedChannelPost(Message editedChannelPost) {
        Update update = new Update();
        update.setEditedChannelPost(editedChannelPost);
        return update;
    }
}