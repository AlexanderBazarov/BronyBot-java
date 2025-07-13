package ru.untitled_devs.core.routers.filters;

import ru.untitled_devs.core.context.UpdateContext;

public class MessageFilter implements Filter {

    @Override
    public boolean check(UpdateContext update) {
        return update.hasMessage();
    }
}
