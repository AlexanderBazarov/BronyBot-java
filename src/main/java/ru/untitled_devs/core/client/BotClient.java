package ru.untitled_devs.core.client;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.untitled_devs.core.dispatcher.Dispatcher;

import java.io.File;
import java.io.InputStream;


public interface BotClient {
    Message sendMessage(long chatId, String text);
	Message sendMessage(long chatId, String text, ReplyKeyboard replyKeyboard);
    void banChatMember(long chatId, long userId, int duration);
    void editMessageText(long chatId, int messageId, String newText);
    void editMessageReplyMarkup(long chatId, int messageId, InlineKeyboardMarkup replyKeyboard);
    void deleteMessage(long chatId, int messageId);
    Message sendPhoto(long chatId, String caption, byte[] photo);
	Message sendPhoto(long chatId, String caption, byte[] photo, ReplyKeyboard replyKeyboard);
    void answerCallbackQuery(String callbackQueryId, String text, boolean showAlert);
	File downloadFile(String filePath) throws TelegramApiException; //must be not depended on library exceptions
	org.telegram.telegrambots.meta.api.objects.File getFile(String fileId);
	InputStream downloadFileAsStream(org.telegram.telegrambots.meta.api.objects.File file) throws TelegramApiException;
	InputStream downloadFileAsStream(String filePath) throws TelegramApiException;


	Dispatcher getDispatcher();
    }
