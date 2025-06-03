package ru.untitled_devs.core.middlewares;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Middleware {
    boolean preHandle(Update update);
}
