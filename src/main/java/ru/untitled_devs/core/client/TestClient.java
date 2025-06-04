package ru.untitled_devs.core.client;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class TestClient implements BotClient {

    @Override
    public void sendMessage(long chatId, String text) {

    }

    @Override
    public void banChatMember(long chatId, long userId, int duration) {

    }

    @Override
    public void editMessageText(long chatId, int messageId, String newText) {

    }

    @Override
    public void editMessageReplyMarkup(long chatId, int messageId, InlineKeyboardMarkup replyKeyboard) {

    }

    @Override
    public void deleteMessage(long chatId, int messageId) {

    }

    @Override
    public void sendPhoto(long chatId, String caption, byte[] photo) {

    }

    @Override
    public void answerCallbackQuery(String callbackQueryId, String text, boolean showAlert) {

    }

    @Override
    public void onUpdateReceived(Update update) {

    }
}
