package ru.untitled_devs.core.routers.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Handler {
    public boolean canHandle(Update update);

    public void handleUpdate(Update update);
}
