package ru.untitled_devs.core.routers;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.routers.handlers.Handler;

import java.util.ArrayList;
import java.util.List;

public class Router {
    private List<Handler> handlers = new ArrayList<>();

    public void addHandler(Handler handler) {
        this.handlers.add(handler);
    }

    public void routeUpdate(Update update) {
        for (Handler handler : this.handlers) {
            if (handler.canHandle(update)) {
                handler.handleUpdate(update);
            }
        }
    }
}
