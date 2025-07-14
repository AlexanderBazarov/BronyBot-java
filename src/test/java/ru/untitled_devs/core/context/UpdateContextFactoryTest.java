package ru.untitled_devs.core.context;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpdateContextFactoryTest {
	Update update;
	User user;

	@BeforeEach
	void setup() {
		update = mock(Update.class);
		user = mock(User.class);

		when(user.getId()).thenReturn(67890L);
		when(user.getUserName()).thenReturn("test_username");
	}

	@Test
	void createGotValidMessageUpdate() {
		Message message = mock(Message.class);

		when(update.hasMessage()).thenReturn(true);
		when(update.hasChannelPost()).thenReturn(false);
		when(update.hasCallbackQuery()).thenReturn(false);
		when(update.hasEditedMessage()).thenReturn(false);
		when(update.hasEditedChannelPost()).thenReturn(false);

		when(update.getMessage()).thenReturn(message);
		when(message.getChatId()).thenReturn(12345L);
		when(message.getText()).thenReturn("test_text");
		when(message.getFrom()).thenReturn(user);

		UpdateContext context = UpdateContextFactory.create(update);

		assertInstanceOf(MessageContext.class, context);
		assertEquals(12345L, context.getChatId());
		assertEquals(67890L, context.getUserId());
		assertEquals("test_username", context.getUsername());
		assertEquals("test_text", context.getText());
		assertEquals(Locale.ENGLISH, context.getLocale());
	}

	@Test
	void createGotValidCallbackQueryUpdate() {
		CallbackQuery callbackQuery = mock(CallbackQuery.class);
		Message message = mock(Message.class);

		when(update.hasCallbackQuery()).thenReturn(true);
		when(update.hasMessage()).thenReturn(false);
		when(update.hasChannelPost()).thenReturn(false);
		when(update.hasEditedMessage()).thenReturn(false);
		when(update.hasEditedChannelPost()).thenReturn(false);

		when(update.getCallbackQuery()).thenReturn(callbackQuery);
		when(callbackQuery.getMessage()).thenReturn(message);
		when(callbackQuery.getMessage().getChatId()).thenReturn(12345L);
		when(callbackQuery.getData()).thenReturn("test_text");
		when(callbackQuery.getFrom()).thenReturn(user);

		UpdateContext context = UpdateContextFactory.create(update);

		assertInstanceOf(CallbackQueryContext.class, context);
		assertEquals(12345L, context.getChatId());
		assertEquals(67890L, context.getUserId());
		assertEquals("test_username", context.getUsername());
		assertEquals("test_text", context.getText());
		assertEquals(Locale.ENGLISH, context.getLocale());
	}

	@Test
	void createGotValidEditedMessageUpdate() {
		Message editedMessage = mock(Message.class);

		when(update.hasEditedMessage()).thenReturn(true);
		when(update.hasMessage()).thenReturn(false);
		when(update.hasChannelPost()).thenReturn(false);
		when(update.hasCallbackQuery()).thenReturn(false);
		when(update.hasEditedChannelPost()).thenReturn(false);

		when(update.getEditedMessage()).thenReturn(editedMessage);
		when(editedMessage.getChatId()).thenReturn(12345L);
		when(editedMessage.getText()).thenReturn("test_text");
		when(editedMessage.getFrom()).thenReturn(user);

		UpdateContext context = UpdateContextFactory.create(update);

		assertInstanceOf(MessageContext.class, context);
		assertEquals(12345L, context.getChatId());
		assertEquals(67890L, context.getUserId());
		assertEquals("test_username", context.getUsername());
		assertEquals("test_text", context.getText());
		assertEquals(Locale.ENGLISH, context.getLocale());
	}

	@Test
	void createGotValidChannelPostUpdate() {
		Message postMessage = mock(Message.class);

		when(update.hasChannelPost()).thenReturn(true);
		when(update.hasMessage()).thenReturn(false);
		when(update.hasCallbackQuery()).thenReturn(false);
		when(update.hasEditedMessage()).thenReturn(false);
		when(update.hasEditedChannelPost()).thenReturn(false);

		when(update.getChannelPost()).thenReturn(postMessage);
		when(postMessage.getFrom()).thenReturn(user);
		when(postMessage.getText()).thenReturn("test_text");
		when(postMessage.getChatId()).thenReturn(12345L);
		when(user.getId()).thenReturn(67890L);

		UpdateContext context = UpdateContextFactory.create(update);

		assertInstanceOf(MessageContext.class, context);
		assertEquals(12345L, context.getChatId());
		assertEquals(67890L, context.getUserId());
		assertEquals("test_username", context.getUsername());
		assertEquals("test_text", context.getText());
		assertEquals(Locale.ENGLISH, context.getLocale());
	}

	@Test
	void createGotValidEditedChannelPostUpdate() {
		Message editedPostMessage = mock(Message.class);

		when(update.hasEditedChannelPost()).thenReturn(true);
		when(update.hasMessage()).thenReturn(false);
		when(update.hasChannelPost()).thenReturn(false);
		when(update.hasCallbackQuery()).thenReturn(false);
		when(update.hasEditedMessage()).thenReturn(false);

		when(update.getEditedChannelPost()).thenReturn(editedPostMessage);
		when(editedPostMessage.getFrom()).thenReturn(user);
		when(editedPostMessage.getText()).thenReturn("test_text");
		when(editedPostMessage.getChatId()).thenReturn(12345L);
		when(user.getId()).thenReturn(67890L);

		UpdateContext context = UpdateContextFactory.create(update);

		assertInstanceOf(MessageContext.class, context);
		assertEquals(12345L, context.getChatId());
		assertEquals(67890L, context.getUserId());
		assertEquals("test_username", context.getUsername());
		assertEquals("test_text", context.getText());
		assertEquals(Locale.ENGLISH, context.getLocale());
	}
}
