package ru.untitled_devs.core.routers.handlers;

import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.filters.Filter;

public interface Handler {
    boolean canHandle(UpdateContext update, FSMContext ctx);

    void handleUpdate(UpdateContext update, FSMContext ctx);

    void addFilter(Filter filter);
}
