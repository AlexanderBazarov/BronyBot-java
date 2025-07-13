package ru.untitled_devs.core.routers;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.fsm.states.DefaultStates;
import ru.untitled_devs.core.fsm.states.State;
import ru.untitled_devs.core.routers.handlers.Handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UpdateRouter implements Router {
    private final ConcurrentHashMap<State, List<Handler>> handlers = new ConcurrentHashMap<>();

	@Override
    public void addHandler(State state, Handler handler) {
        this.handlers.computeIfAbsent(state, (k) -> new ArrayList<>()).add(handler);
    }

	@Override
    public Map<State, List<Handler>> getHandlers() {
        return Collections.unmodifiableMap(this.handlers);
    }

	@Override
    public boolean routeUpdate(UpdateContext update, FSMContext ctx) {
        List<Handler> stateHandlers = handlers.getOrDefault(ctx.getState(), List.of());
        for (Handler handler : stateHandlers) {
            if (handler.canHandle(update, ctx)) {
                handler.handleUpdate(update, ctx);
				return true;
            }
        }
		return false;
    }
}
