package ru.untitled_devs.core.routers;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.handlers.Handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Router {
    private final List<Handler> handlers = new ArrayList<>();

    public void addHandler(Handler handler) {
        this.handlers.add(handler);
    }

    public List<Handler> getHandler() {
        return Collections.unmodifiableList(this.handlers);
    }

    public void routeUpdate(Update update, FSMContext ctx) {
        for (Handler handler : this.handlers) {
            if (handler.canHandle(update, ctx)) {
                handler.handleUpdate(update, ctx);
            }
        }
    }
}
