package ru.untitled_devs.core.context;

import org.telegram.telegrambots.meta.api.objects.Update;

public class UpdateContextFactory {
	public static UpdateContext create(Update update) {
		if (update.hasMessage()) {
			return new MessageContext(update.getMessage(), update);
		} else if (update.hasCallbackQuery()) {
			return new CallbackQueryContext(update.getCallbackQuery(), update);
		} else if (update.hasEditedMessage()) {
			return new MessageContext(update.getEditedMessage(), update);
		} else if (update.hasChannelPost()) {
			return new MessageContext(update.getChannelPost(), update);
		} else if (update.hasEditedChannelPost()) {
			return new MessageContext(update.getEditedChannelPost(), update);
		}

		throw new IllegalArgumentException("Unsupported update: " + update);
	}
}
