package ru.untitled_devs.core.context;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;

public class MessageContext extends UpdateContext {
	private final Message message;

	public MessageContext(Message message, Update update) {
		super(update);
		this.message = message;
	}

	@Override public Long getChatId() { return message.getChatId(); }
	@Override public Long getUserId() { return message.getFrom().getId(); }
	@Override public String getUsername() { return message.getFrom().getUserName(); }
	@Override public String getText() { return message.getText(); }
	@Override public Locale getLocale() {
		String lang = message.getFrom().getLanguageCode();
		return lang != null ? Locale.forLanguageTag(lang.replace("_", "-")) : Locale.ENGLISH;
	}

	public Message getMessage() { return message; }

	@Override
	public boolean hasMessage() {
		return message != null;
	}
}
