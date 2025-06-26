package ru.untitled_devs.core.dispatcher;


import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.fsm.storage.Storage;
import ru.untitled_devs.core.fsm.storage.StorageKey;
import ru.untitled_devs.core.middlewares.Middleware;
import ru.untitled_devs.core.routers.Router;
import ru.untitled_devs.core.utils.NamedThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Dispatcher {
	NamedThreadFactory namedThreadFactory = new NamedThreadFactory("ExecutorThread-");
	ExecutorService excutor = Executors.newFixedThreadPool(10, namedThreadFactory);

	private final List<Router> routers = new ArrayList<>();
	private final Storage storage;
	private final List<Middleware> middlewares = new ArrayList<>();
	private final Logger logger;


	public Dispatcher(Storage storage, Logger logger) {
		this.storage = storage;
		this.logger = logger;
	}

	public void addRouter(Router router) {
		this.routers.add(router);
	}

	public void addMiddleware(Middleware middleware) {
		this.middlewares.add(middleware);
	}

	public Future<?> feedUpdate(Update update) {
		return CompletableFuture
			.runAsync(() -> processUpdate(update), excutor)
			.whenComplete((v, ex) -> {
				if (ex != null) logger.error("Update crashed", ex);
				else logger.debug("Update {} processed", update.getUpdateId());
			});
	}

	public void processUpdate(Update update) {
		UpdateContext updateContext = new UpdateContext(update);
		if (updateContext.getChatId() == null || updateContext.getUserId() == null) {
			return;
		}

		StorageKey key = new StorageKey(updateContext.getChatId(), updateContext.getUserId());

		FSMContext context = this.storage.getOrCreateContext(key);

		for (Middleware middleware : this.middlewares) {
			try {
				if (!middleware.preHandle(update, context)){
					logger.debug("Middleware prevented handling update: {}", update);
					break;
				}
			} catch (Exception e) {
				logger.error("Exception in middleware: ", e);
				return;
			}
		}

		for (Router router : this.routers) {
			try {
				router.routeUpdate(update, context);
			} catch (Exception e) {
				logger.error("Exception in router {} while handling update: {}", router.getClass().getSimpleName(), update, e);
			}
		}
	}
}
