package ru.untitled_devs.core.client;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.untitled_devs.core.dispatcher.Dispatcher;
import ru.untitled_devs.core.fsm.storage.Storage;
import ru.untitled_devs.core.routers.Router;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BotTest {

    private PollingClient bot;
    private Storage storage;
    private Router router;
	private Dispatcher dispatcher;

    @BeforeEach
    void setUp() {
        storage = mock(Storage.class);
        router = mock(Router.class);
		dispatcher = mock(Dispatcher.class);
        Logger logger = mock(Logger.class);
        bot = new PollingClient("testToken", "testUsername", dispatcher, logger);
    }

    @Test
    void sendMessageSuccessfullySendsMessage() throws TelegramApiException {
        bot = spy(bot);
        doReturn(null).when(bot).execute(any(SendMessage.class));

        assertDoesNotThrow(
                () -> bot.sendMessage(12345L, "Hello, World!"),
                "sendMessage should not throw any exception when execute(...) returns null"
        );

        verify(bot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    void sendMessageSuccessfullySendsMessageWithMarkup() throws TelegramApiException {
        bot = spy(bot);
        doReturn(null).when(bot).execute(any(SendMessage.class));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        assertDoesNotThrow(
                () -> bot.sendMessage(12345L, "Hello, World!", markup),
                "sendMessage should not throw any exception when execute(...) returns null"
        );

        verify(bot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    void sendMessageHandlesTelegramApiException() throws TelegramApiException {
        bot = spy(bot);
        doThrow(new TelegramApiException("API error")).when(bot).execute(any(SendMessage.class));

        assertDoesNotThrow(
                () -> bot.sendMessage(12345L, "Hello, World!"),
                "sendMessage should not propagate TelegramApiException"
        );

        verify(bot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    void banChatMemberSuccessfullyBansUser() throws TelegramApiException {
        bot = spy(bot);
        doReturn(null).when(bot).execute(any(BanChatMember.class));

        assertDoesNotThrow(
                () -> bot.banChatMember(12345L, 67890L, 3600),
                "banChatMember should not throw any exception when execute(...) returns null"
        );

        verify(bot, times(1)).execute(any(BanChatMember.class));
    }

    @Test
    void banChatMemberHandlesTelegramApiException() throws TelegramApiException {
        bot = spy(bot);
        doThrow(new TelegramApiException("API error")).when(bot).execute(any(BanChatMember.class));

        assertDoesNotThrow(
                () -> bot.banChatMember(12345L, 67890L, 3600),
                "banChatMember should not propagate TelegramApiException"
        );

        verify(bot, times(1)).execute(any(BanChatMember.class));
    }

    @Test
    void seditMessageSuccessfullyEditMessageText() throws TelegramApiException {
        bot = spy(bot);
        doReturn(null).when(bot).execute(any(EditMessageText.class));

        assertDoesNotThrow(
                () -> bot.editMessageText(12345L, 12415125,"Hello, World!"),
                "sendMessage should not throw any exception when execute(...) returns null"
        );

        verify(bot, times(1)).execute(any(EditMessageText.class));
    }

    @Test
    void EditMessageMarkupSuccessfullyEditMessageMarkup() throws TelegramApiException {
        bot = spy(bot);
        doReturn(null).when(bot).execute(any(EditMessageReplyMarkup.class));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        assertDoesNotThrow(
                () -> bot.editMessageReplyMarkup(12345L, 12415125, markup),
                "sendMessage should not throw any exception when execute(...) returns null"
        );

        verify(bot, times(1)).execute(any(EditMessageReplyMarkup.class));
    }

    @Test
    void deleteMessageSuccessfullyDeletesMessage() throws TelegramApiException {
        bot = spy(bot);
        doReturn(null).when(bot).execute(any(DeleteMessage.class));

        assertDoesNotThrow(
                () -> bot.deleteMessage(12345L, 12415125),
                "sendMessage should not throw any exception when execute(...) returns null"
        );

        verify(bot, times(1)).execute(any(DeleteMessage.class));
    }

    @Test
    void sendPhotoSuccessfullySendsPhoto() throws TelegramApiException {
        bot = spy(bot);
        doReturn(null).when(bot).execute(any(SendPhoto.class));

        byte[] photo = Base64.getDecoder().decode(
                "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVQI12NgYAAAAAMAASsJTYQAAAAASUVORK5CYII="
        );

        assertDoesNotThrow(
                () -> bot.sendPhoto(12345L, "12415125", photo),
                "sendMessage should not throw any exception when execute(...) returns null"
        );

        verify(bot, times(1)).execute(any(SendPhoto.class));
    }

	@Test
	void answerCallbackQuerySuccessfullyAnswersCallback() throws TelegramApiException {
		bot = spy(bot);
		doReturn(null).when(bot).execute(any(AnswerCallbackQuery.class));

		assertDoesNotThrow(
			() -> bot.answerCallbackQuery("21323", "12415125", false),
			"sendMessage should not throw any exception when execute(...) returns null"
		);

		verify(bot, times(1)).execute(any(AnswerCallbackQuery.class));
	}

	@Test
	void onUpdateReceivedHandlesNullMessageGracefully() {
		Update update = mock(Update.class);
		when(update.getMessage()).thenReturn(null);
		when(update.hasMessage()).thenReturn(false);

		assertDoesNotThrow(
			() -> bot.onUpdateReceived(update),
			"onUpdateReceived should throw NullPointerException when update.getMessage() is null"
		);
	}
}
