package ru.untitled_devs.core.dispatcher;

import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.context.UpdateContextFactory;
import ru.untitled_devs.core.executor.UserQueueExecutor;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.fsm.storage.Storage;
import ru.untitled_devs.core.fsm.storage.StorageKey;
import ru.untitled_devs.core.middlewares.Middleware;
import ru.untitled_devs.core.routers.UpdateRouter;
import ru.untitled_devs.core.routers.scenes.SceneManager;

import java.util.ArrayList;
import java.util.List;

public class Dispatcher {
	private final UserQueueExecutor userQueueExecutor;
	private final List<UpdateRouter> routers = new ArrayList<>();
	private final Storage storage;
	private final List<Middleware> middlewares = new ArrayList<>();
	private final Logger logger;
	private final SceneManager sceneManager;

	public Dispatcher(Storage storage, Logger logger, SceneManager sceneManager) {
		this.sceneManager = sceneManager;
		this.storage = storage;
		this.logger = logger;

		userQueueExecutor = new UserQueueExecutor(10, logger);
	}

	public void addRouter(UpdateRouter router) {
		this.routers.add(router);
	}

	public void addMiddleware(Middleware middleware) {
		this.middlewares.add(middleware);
	}

	public void feedUpdate(Update update) {
		UpdateContext updateContext = UpdateContextFactory.create(update);
		long chatId = updateContext.getChatId();

		userQueueExecutor.submit(chatId, () -> {
			try {
				processUpdate(update);
				logger.info("Update {} processed", update.getUpdateId());
			} catch (Exception e) {
				logger.error("Update crashed", e);
			}
		});
	}

	public void processUpdate(Update update) {
		UpdateContext updateContext = UpdateContextFactory.create(update);
		if (updateContext.getChatId() == null) {
			return;
		}

		StorageKey key = new StorageKey(updateContext.getChatId(), updateContext.getUserId());

		FSMContext context = this.storage.getOrCreateContext(key);

		for (Middleware middleware : middlewares) {
			try {
				if (!middleware.preHandle(updateContext, context)){
					logger.debug("Middleware prevented handling update: {}", update);
					return;
				}
			} catch (Exception e) {
				logger.error("Exception in middleware: ", e);
				return;
			}
		}

		if (context.getSceneId() != null) {
			boolean handled = sceneManager.handle(updateContext, context);
			if (handled) return;
		}

		for (UpdateRouter router : routers) {
			try {
				router.routeUpdate(updateContext, context);
			} catch (Exception e) {
				logger.error("Exception in router {} while handling update: {}", router.getClass().getSimpleName(), update, e);
			}
		}
	}
}
