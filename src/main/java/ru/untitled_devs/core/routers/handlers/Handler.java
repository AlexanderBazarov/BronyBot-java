package ru.untitled_devs.core.routers.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.fsm.context.FSMContext;

public interface Handler {
    public boolean canHandle(Update update, FSMContext ctx);

    public void handleUpdate(Update update, FSMContext ctx);
}
