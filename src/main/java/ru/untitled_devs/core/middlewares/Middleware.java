package ru.untitled_devs.core.middlewares;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.fsm.context.FSMContext;

public interface Middleware {
    boolean preHandle(Update update, FSMContext ctx);
}
