package ru.untitled_devs.core.client;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotClient {
    void sendMessage(long chatId, String text);
    void banChatMember(long chatId, long userId, int duration);
    void editMessageText(long chatId, int messageId, String newText);
    void deleteMessage(long chatId, int messageId);
    void sendTypingAction(long chatId);
    void sendPhoto(long chatId, String caption, byte[] photo);
    void answerCallbackQuery(String callbackQueryId, String text, boolean showAlert);
    void onUpdateReceived(Update update);
}