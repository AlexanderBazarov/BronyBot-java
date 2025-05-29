package ru.untitled_devs.core.routers.filters;

import org.telegram.telegrambots.meta.api.objects.Update;

@FunctionalInterface
public interface Filter {
    boolean check(Update update);
}
