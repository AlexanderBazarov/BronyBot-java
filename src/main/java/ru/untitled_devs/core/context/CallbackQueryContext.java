package ru.untitled_devs.core.context;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;

public class CallbackQueryContext extends UpdateContext {
	private final CallbackQuery callbackQuery;

	public CallbackQueryContext(CallbackQuery callback, Update update) {
		super(update);
		this.callbackQuery = callback;
	}

	@Override public Long getChatId() { return callbackQuery.getMessage().getChatId(); }
	@Override public Long getUserId() { return callbackQuery.getFrom().getId(); }
	@Override public String getUsername() { return callbackQuery.getFrom().getUserName(); }
	@Override public String getText() { return callbackQuery.getData(); }
	@Override public Locale getLocale() {
		String lang = callbackQuery.getFrom().getLanguageCode();
		return lang != null ? Locale.forLanguageTag(lang.replace("_", "-")) : Locale.ENGLISH;
	}

	public CallbackQuery getCallbackQuery() { return callbackQuery; }

	@Override
	public boolean hasCallbackQuery() {
		return callbackQuery != null;
	}
}
