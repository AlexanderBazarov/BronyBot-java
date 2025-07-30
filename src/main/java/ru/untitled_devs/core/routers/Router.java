package ru.untitled_devs.core.routers;

import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.FSMContext;
import ru.untitled_devs.core.fsm.states.State;
import ru.untitled_devs.core.routers.handlers.Handler;

import java.util.List;
import java.util.Map;

public interface Router {
	void addHandler(State state, Handler handler);

	Map<State, List<Handler>> getHandlers();

	boolean routeUpdate(UpdateContext update, FSMContext ctx);
}
