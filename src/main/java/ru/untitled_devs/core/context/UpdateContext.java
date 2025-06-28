package ru.untitled_devs.core.context;

import org.telegram.telegrambots.meta.api.objects.Update;

public class UpdateContext {
    private final Update update;
    private final Long chatId;
    private final Long userId;

    public UpdateContext(Update update) {
        this.update = update;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            userId = update.getMessage().getFrom().getId();
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            userId = update.getCallbackQuery().getFrom().getId();
        } else if (update.hasEditedMessage()) {
            chatId = update.getEditedMessage().getChatId();
            userId = update.getEditedMessage().getFrom().getId();
        } else if (update.hasChannelPost()) {
            chatId = update.getChannelPost().getChatId();
            userId = update.getChannelPost().getFrom().getId();
        } else if (update.hasEditedChannelPost()) {
            chatId = update.getEditedChannelPost().getChatId();
            userId = update.getEditedChannelPost().getFrom().getId();
        } else {
			throw new IllegalArgumentException();
        }
    }

    public Long getChatId() {
        return chatId;
    }

    public Long getUserId() {
        return userId;
    }

    public Update getUpdate() {
        return update;
    }
}
