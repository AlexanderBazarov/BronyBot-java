package ru.untitled_devs.core.routers.filters;

import org.telegram.telegrambots.meta.api.objects.Update;

public  class MessageFilter implements Filter {

    @Override
    public boolean check(Update update) {
        return false;
    }
}
