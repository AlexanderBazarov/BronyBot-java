package ru.untitled_devs.core.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.context.UpdateContextFactory;
import ru.untitled_devs.core.exceptions.StopMiddlewareException;
import ru.untitled_devs.core.exceptions.StopRoutingException;
import ru.untitled_devs.core.executor.UserQueueExecutor;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.fsm.storage.Storage;
import ru.untitled_devs.core.fsm.storage.StorageKey;
import ru.untitled_devs.core.middlewares.Middleware;
import ru.untitled_devs.core.routers.UpdateRouter;
import ru.untitled_devs.core.routers.scenes.Scene;
import ru.untitled_devs.core.routers.scenes.SceneManager;

import java.util.ArrayList;
import java.util.List;

public class Dispatcher {
	private final UserQueueExecutor userQueueExecutor;
	private final List<UpdateRouter> routers = new ArrayList<>();
	private final Storage storage;
	private final List<Middleware> middlewares = new ArrayList<>();
	private final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
	private final SceneManager sceneManager;

	public Dispatcher(Storage storage, SceneManager sceneManager) {
		this.sceneManager = sceneManager;
		this.storage = storage;

		userQueueExecutor = new UserQueueExecutor(10);
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

		FSMContext fsmContext = this.storage.getOrCreateContext(key);

		try {
			runMiddlewares(updateContext, fsmContext);
			handleScene(updateContext, fsmContext);

			handleRouters(updateContext, fsmContext);
		} catch (StopRoutingException e) {
			return;
		}
	}

	private void runMiddlewares(UpdateContext ctx, FSMContext fsmCtx) throws StopRoutingException {
		for (Middleware middleware : middlewares) {
			try {
				middleware.preHandle(ctx, fsmCtx);
			} catch (StopMiddlewareException e) {
				logger.debug("Middleware chain stopped: {}", e.getMessage());
				break;
			}
		}
	}

	private void handleScene(UpdateContext ctx, FSMContext fsmCtx) throws StopRoutingException {
		if (fsmCtx.getSceneId() != null) {
			Scene scene = sceneManager.getScene(fsmCtx.getSceneId());
			if (scene == null) throw new IllegalArgumentException();
			try {
				scene.routeUpdate(ctx, fsmCtx);
				logger.debug("Scene handled update: {}", fsmCtx.getSceneId());
			} catch (Exception e) {
				logger.error("Scene {} failed: {}",
					sceneManager.getScene(fsmCtx.getSceneId()).getClass().getSimpleName(), ctx.getUpdate(), e);
			}

			throw new StopRoutingException();
		}
	}

	private void handleRouters(UpdateContext ctx, FSMContext fsmCtx) throws StopRoutingException {
		for (UpdateRouter router : routers) {
			try {
				router.routeUpdate(ctx, fsmCtx);
			} catch (Exception e) {
				logger.error("Router {} failed: {}", router.getClass().getSimpleName(), ctx.getUpdate(), e);
			}
		}
	}

}
