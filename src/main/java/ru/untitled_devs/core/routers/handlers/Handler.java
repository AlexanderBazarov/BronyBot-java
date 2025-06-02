package ru.untitled_devs.core.routers.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.fsm.context.FSMContext;

public interface Handler {
    boolean canHandle(Update update, FSMContext ctx);

    void handleUpdate(Update update, FSMContext ctx);
}
