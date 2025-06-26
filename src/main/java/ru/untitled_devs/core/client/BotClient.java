package ru.untitled_devs.core.client;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.untitled_devs.core.dispatcher.Dispatcher;


public interface BotClient {
    void sendMessage(long chatId, String text);
    void banChatMember(long chatId, long userId, int duration);
    void editMessageText(long chatId, int messageId, String newText);
    void editMessageReplyMarkup(long chatId, int messageId, InlineKeyboardMarkup replyKeyboard);
    void deleteMessage(long chatId, int messageId);
    void sendPhoto(long chatId, String caption, byte[] photo);
    void answerCallbackQuery(String callbackQueryId, String text, boolean showAlert);

	Dispatcher getDispatcher();
    }
