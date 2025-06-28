package ru.untitled_devs.core.dispatcher;

import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.executor.UserQueueExecutor;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.fsm.storage.Storage;
import ru.untitled_devs.core.fsm.storage.StorageKey;
import ru.untitled_devs.core.middlewares.Middleware;
import ru.untitled_devs.core.routers.Router;

import java.util.ArrayList;
import java.util.List;

public class Dispatcher {
	private final UserQueueExecutor userQueueExecutor;
	private final List<Router> routers = new ArrayList<>();
	private final Storage storage;
	private final List<Middleware> middlewares = new ArrayList<>();
	private final Logger logger;


	public Dispatcher(Storage storage, Logger logger) {
		this.storage = storage;
		this.logger = logger;

		userQueueExecutor = new UserQueueExecutor(10, logger);
	}

	public void addRouter(Router router) {
		this.routers.add(router);
	}

	public void addMiddleware(Middleware middleware) {
		this.middlewares.add(middleware);
	}

	public void feedUpdate(Update update) {
		UpdateContext updateContext = new UpdateContext(update);
		long chatId = updateContext.getChatId();

		userQueueExecutor.submit(chatId, () -> {
			try {
				processUpdate(update);
				logger.info("Update {} processed", update.getUpdateId());
			} catch (Exception ex) {
				logger.error("Update crashed", ex);
			}
		});
	}

	public void processUpdate(Update update) {
		UpdateContext updateContext = new UpdateContext(update);
		if (updateContext.getChatId() == null) {
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
