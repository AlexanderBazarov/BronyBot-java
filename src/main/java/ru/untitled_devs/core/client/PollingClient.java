package ru.untitled_devs.core.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.untitled_devs.core.dispatcher.Dispatcher;

import java.io.ByteArrayInputStream;
import java.time.Instant;

import static ru.untitled_devs.core.utils.FileUtils.getImageFileNameWithExtension;

public class PollingClient extends TelegramLongPollingBot implements BotClient {
    private final String botUsername;
    private final Logger logger = LoggerFactory.getLogger(PollingClient.class);
	private final Dispatcher dispatcher;

    public PollingClient(String botToken, String botUsername, Dispatcher dispatcher) {
        super(botToken);
        this.botUsername = botUsername;
		this.dispatcher = dispatcher;
	}

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

	@Override
	public Dispatcher getDispatcher() {
		return dispatcher;
	}

	public Message sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            return execute(message);
        } catch (TelegramApiException e) {
            this.logger.error(e.getMessage());
			return null;
        }
    }

    public Message sendMessage(long chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(replyKeyboard);

        try {
            return execute(message);
        } catch (TelegramApiException e) {
            this.logger.error(e.getMessage());
			return null;
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
    public Message sendPhoto(long chatId, String caption, byte[] photo) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(caption);

        InputFile file = new InputFile(new ByteArrayInputStream(photo), getImageFileNameWithExtension(photo));
        sendPhoto.setPhoto(file);

        try {
            return execute(sendPhoto);
        } catch (TelegramApiException e) {
            this.logger.error(e.getMessage());
			return null;
        }
    }

	@Override
	public Message sendPhoto(long chatId, String caption, byte[] photo, ReplyKeyboard replyKeyboard) {
		SendPhoto sendPhoto = new SendPhoto();
		sendPhoto.setChatId(chatId);
		sendPhoto.setCaption(caption);
		sendPhoto.setReplyMarkup(replyKeyboard);

		InputFile file = new InputFile(new ByteArrayInputStream(photo), getImageFileNameWithExtension(photo));
		sendPhoto.setPhoto(file);

		try {
			return execute(sendPhoto);
		} catch (TelegramApiException e) {
			this.logger.error(e.getMessage());
			return null;
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
	public File getFile(String fileId) {
		GetFile getFile = new GetFile();
		getFile.setFileId(fileId);

		try {
			return execute(getFile);
		} catch (TelegramApiException e) {
			this.logger.error(e.getMessage());
			return null;
		}
	}

    @Override
    public void onUpdateReceived(Update update) {
        dispatcher.feedUpdate(update);
    }
}
