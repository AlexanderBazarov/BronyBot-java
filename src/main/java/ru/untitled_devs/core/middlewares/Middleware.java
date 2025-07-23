package ru.untitled_devs.core.middlewares;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.exceptions.StopMiddlewareException;
import ru.untitled_devs.core.exceptions.StopRoutingException;
import ru.untitled_devs.core.fsm.context.FSMContext;

public abstract class Middleware {
    public abstract void preHandle(UpdateContext update, FSMContext ctx);

	protected void stopMiddlewareChain() {
		throw new StopMiddlewareException("Stopped in middleware");
	}

	protected void stopMiddlewareChain(String message) {
		throw new StopMiddlewareException(message);
	}

	protected void stopRouting() {
		throw new StopRoutingException("Stopped in middleware");
	}

	protected void stopRouting(String message) {
		throw new StopRoutingException(message);
	}

}
