package ru.untitled_devs.core.context;

import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.Locale;

public abstract class UpdateContext {
	protected final Update update;

	public UpdateContext(Update update) {
		this.update = update;
	}

	public abstract Long getChatId();
	public abstract Long getUserId();
	public abstract String getUsername();
	public abstract String getText();
	public abstract Locale getLocale();

	public boolean isCommand() {
		return getText() != null && getText().startsWith("/");
	}

	public String getCommand() {
		if (!isCommand()) return null;
		return getText().split(" ")[0];
	}

	public Update getUpdate() {
		return update;
	}

	public boolean hasMessage() {
		return false;
	}

	public boolean hasCallbackQuery() {
		return false;
	}
}
