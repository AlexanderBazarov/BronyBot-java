package ru.untitleddevs.core.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.untitled_devs.core.client.Bot;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.fsm.storage.StorageKey;
import ru.untitled_devs.core.fsm.storage.Storage;
import ru.untitled_devs.core.routers.Router;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BotTest {

    private Bot bot;
    private Storage storage;
    private Router router;

    @BeforeEach
    void setUp() {
        storage = mock(Storage.class);
        router = mock(Router.class);
        bot = new Bot("testToken", "testUsername", storage);
        bot.addRouter(router);
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
    void onUpdateMessageReceivedRoutesUpdateToAllRouters() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        User user = mock(User.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.hasChannelPost()).thenReturn(false);
        when(update.hasCallbackQuery()).thenReturn(false);
        when(update.hasEditedMessage()).thenReturn(false);
        when(update.hasEditedChannelPost()).thenReturn(false);

        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(12345L);
        when(message.getFrom()).thenReturn(user);
        when(user.getId()).thenReturn(67890L);

        FSMContext context = mock(FSMContext.class);
        when(storage.getOrCreateContext(any(StorageKey.class))).thenReturn(context);

        assertDoesNotThrow(
                () -> bot.onUpdateReceived(update),
                "onUpdateReceived should not throw when message and context are valid"
        );

        verify(router, times(1)).routeUpdate(update, context);
    }

    @Test
    void onUpdateCallbackQueryReceivedRoutesUpdateToAllRouters() {
        Update update = mock(Update.class);
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        User user = mock(User.class);
        Message message = mock(Message.class);

        when(update.hasCallbackQuery()).thenReturn(true);
        when(update.hasMessage()).thenReturn(false);
        when(update.hasChannelPost()).thenReturn(false);
        when(update.hasEditedMessage()).thenReturn(false);
        when(update.hasEditedChannelPost()).thenReturn(false);

        when(update.getCallbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.getMessage()).thenReturn(message);
        when(callbackQuery.getMessage().getChatId()).thenReturn(12345L);
        when(callbackQuery.getFrom()).thenReturn(user);
        when(user.getId()).thenReturn(67890L);

        FSMContext context = mock(FSMContext.class);
        when(storage.getOrCreateContext(any(StorageKey.class))).thenReturn(context);

        assertDoesNotThrow(
                () -> bot.onUpdateReceived(update),
                "onUpdateReceived should not throw when message and context are valid"
        );

        verify(router, times(1)).routeUpdate(update, context);
    }

    @Test
    void onUpdateEditedMessageReceivedRoutesUpdateToAllRouters() {
        Update update = mock(Update.class);
        Message editedMessage = mock(Message.class);
        User user = mock(User.class);

        when(update.hasEditedMessage()).thenReturn(true);
        when(update.hasMessage()).thenReturn(false);
        when(update.hasChannelPost()).thenReturn(false);
        when(update.hasCallbackQuery()).thenReturn(false);
        when(update.hasEditedChannelPost()).thenReturn(false);

        when(update.getEditedMessage()).thenReturn(editedMessage);
        when(editedMessage.getChatId()).thenReturn(12345L);
        when(editedMessage.getFrom()).thenReturn(user);
        when(user.getId()).thenReturn(67890L);


        FSMContext context = mock(FSMContext.class);
        when(storage.getOrCreateContext(any(StorageKey.class))).thenReturn(context);

        assertDoesNotThrow(
                () -> bot.onUpdateReceived(update),
                "onUpdateReceived should not throw when message and context are valid"
        );

        verify(router, times(1)).routeUpdate(update, context);
    }

    @Test
    void onUpdateChanelPostReceivedRoutesUpdateToAllRouters() {
        Update update = mock(Update.class);
        Message postMessage = mock(Message.class);
        User user = mock(User.class);

        when(update.hasChannelPost()).thenReturn(true);
        when(update.hasMessage()).thenReturn(false);
        when(update.hasCallbackQuery()).thenReturn(false);
        when(update.hasEditedMessage()).thenReturn(false);
        when(update.hasEditedChannelPost()).thenReturn(false);

        when(update.getChannelPost()).thenReturn(postMessage);
        when(postMessage.getFrom()).thenReturn(user);
        when(postMessage.getChatId()).thenReturn(12345L);
        when(user.getId()).thenReturn(67890L);


        FSMContext context = mock(FSMContext.class);
        when(storage.getOrCreateContext(any(StorageKey.class))).thenReturn(context);

        assertDoesNotThrow(
                () -> bot.onUpdateReceived(update),
                "onUpdateReceived should not throw when message and context are valid"
        );

        verify(router, times(1)).routeUpdate(update, context);
    }

    @Test
    void onUpdateEditedChanelPostReceivedRoutesUpdateToAllRouters() {
        Update update = mock(Update.class);
        Message editedPostMessage = mock(Message.class);
        User user = mock(User.class);

        when(update.hasEditedChannelPost()).thenReturn(true);
        when(update.hasMessage()).thenReturn(false);
        when(update.hasChannelPost()).thenReturn(false);
        when(update.hasCallbackQuery()).thenReturn(false);
        when(update.hasEditedMessage()).thenReturn(false);

        when(update.getEditedChannelPost()).thenReturn(editedPostMessage);
        when(editedPostMessage.getChatId()).thenReturn(12345L);
        when(editedPostMessage.getFrom()).thenReturn(user);
        when(user.getId()).thenReturn(67890L);


        FSMContext context = mock(FSMContext.class);
        when(storage.getOrCreateContext(any(StorageKey.class))).thenReturn(context);

        assertDoesNotThrow(
                () -> bot.onUpdateReceived(update),
                "onUpdateReceived should not throw when message and context are valid"
        );

        verify(router, times(1)).routeUpdate(update, context);
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
