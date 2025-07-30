package ru.untitled_devs.core.dispatcher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.exceptions.StopMiddlewareException;
import ru.untitled_devs.core.exceptions.StopRoutingException;
import ru.untitled_devs.core.fsm.FSMContext;
import ru.untitled_devs.core.fsm.storage.Storage;
import ru.untitled_devs.core.fsm.storage.StorageKey;
import ru.untitled_devs.core.middlewares.Middleware;
import ru.untitled_devs.core.routers.UpdateRouter;
import ru.untitled_devs.core.routers.scenes.Scene;
import ru.untitled_devs.core.routers.scenes.SceneManager;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DispatcherTest {
	static Storage storage;
	static UpdateRouter router;
	static Dispatcher dispatcher;
	static SceneManager sceneManager;

	@BeforeEach
	void setup() {
		storage = mock(Storage.class);
		router = mock(UpdateRouter.class);
		sceneManager = new SceneManager();
		dispatcher = new Dispatcher(storage, sceneManager);
		dispatcher.addRouter(router);
	}

	@Test
	void onUpdateMessageReceivedRoutesUpdateToAllRouters() {
		Update update = mock(Update.class);
		Message message = mock(Message.class);
		User user = mock(User.class);

		when(update.hasMessage()).thenReturn(true);
		when(update.hasChannelPost()).thenReturn(false);
		when(update.hasCallbackQuery()).thenReturn(false);
		when(update.hasEditedMessage()).thenReturn(false);
		when(update.hasEditedChannelPost()).thenReturn(false);

		when(update.getMessage()).thenReturn(message);
		when(message.getChatId()).thenReturn(12345L);
		when(message.getFrom()).thenReturn(user);
		when(user.getId()).thenReturn(67890L);

		FSMContext context = mock(FSMContext.class);
		when(storage.getOrCreateContext(any(StorageKey.class))).thenReturn(context);

		dispatcher.feedUpdate(update);

		ArgumentCaptor<UpdateContext> updateCtxCaptor = ArgumentCaptor.forClass(UpdateContext.class);

		await().atMost(3, TimeUnit.SECONDS)
			.pollInterval(10, TimeUnit.MILLISECONDS)
			.untilAsserted(() ->
			verify(router, times(1)).routeUpdate(updateCtxCaptor.capture(), eq(context))
		);
	}

	@Test
	void onUpdateCallbackQueryReceivedRoutesUpdateToAllRouters() {
		Update update = mock(Update.class);
		CallbackQuery callbackQuery = mock(CallbackQuery.class);
		User user = mock(User.class);
		Message message = mock(Message.class);

		when(update.hasCallbackQuery()).thenReturn(true);
		when(update.hasMessage()).thenReturn(false);
		when(update.hasChannelPost()).thenReturn(false);
		when(update.hasEditedMessage()).thenReturn(false);
		when(update.hasEditedChannelPost()).thenReturn(false);

		when(update.getCallbackQuery()).thenReturn(callbackQuery);
		when(callbackQuery.getMessage()).thenReturn(message);
		when(callbackQuery.getMessage().getChatId()).thenReturn(12345L);
		when(callbackQuery.getFrom()).thenReturn(user);
		when(user.getId()).thenReturn(67890L);

		FSMContext context = mock(FSMContext.class);
		when(storage.getOrCreateContext(any(StorageKey.class))).thenReturn(context);

		dispatcher.feedUpdate(update);

		ArgumentCaptor<UpdateContext> updateCtxCaptor = ArgumentCaptor.forClass(UpdateContext.class);

		await().atMost(3, TimeUnit.SECONDS)
			.pollInterval(10, TimeUnit.MILLISECONDS)
			.untilAsserted(() ->
				verify(router, times(1)).routeUpdate(updateCtxCaptor.capture(), eq(context))
			);
	}

	@Test
	void onUpdateMessageReceivedRoutesUpdateToScene() {
		Update update = mock(Update.class);
		Message message = mock(Message.class);
		User user = mock(User.class);
		Scene testScene = mock(Scene.class);

		when(update.hasMessage()).thenReturn(true);
		when(update.hasChannelPost()).thenReturn(false);
		when(update.hasCallbackQuery()).thenReturn(false);
		when(update.hasEditedMessage()).thenReturn(false);
		when(update.hasEditedChannelPost()).thenReturn(false);

		when(update.getMessage()).thenReturn(message);
		when(message.getChatId()).thenReturn(12345L);
		when(message.getFrom()).thenReturn(user);
		when(user.getId()).thenReturn(67890L);

		FSMContext context = mock(FSMContext.class);
		when(storage.getOrCreateContext(any(StorageKey.class))).thenReturn(context);
		when(context.getSceneId()).thenReturn("testScene");

		sceneManager.register("testScene", testScene);

		dispatcher.feedUpdate(update);

		ArgumentCaptor<UpdateContext> updateCtxCaptor = ArgumentCaptor.forClass(UpdateContext.class);

		await().atMost(3, TimeUnit.SECONDS)
			.pollInterval(10, TimeUnit.MILLISECONDS)
			.untilAsserted(() -> {
					verify(testScene, times(1)).routeUpdate(updateCtxCaptor.capture(), eq(context));
					verify(router, never()).routeUpdate(updateCtxCaptor.capture(), eq(context));
				}
			);
	}

	@Test
	void onUpdateCallbackQueryReceivedRoutesUpdateToScene() {
		Update update = mock(Update.class);
		CallbackQuery callbackQuery = mock(CallbackQuery.class);
		User user = mock(User.class);
		Message message = mock(Message.class);
		Scene testScene = mock(Scene.class);

		when(update.hasCallbackQuery()).thenReturn(true);
		when(update.hasMessage()).thenReturn(false);
		when(update.hasChannelPost()).thenReturn(false);
		when(update.hasEditedMessage()).thenReturn(false);
		when(update.hasEditedChannelPost()).thenReturn(false);

		when(update.getCallbackQuery()).thenReturn(callbackQuery);
		when(callbackQuery.getMessage()).thenReturn(message);
		when(callbackQuery.getMessage().getChatId()).thenReturn(12345L);
		when(callbackQuery.getFrom()).thenReturn(user);
		when(user.getId()).thenReturn(67890L);

		FSMContext context = mock(FSMContext.class);
		when(storage.getOrCreateContext(any(StorageKey.class))).thenReturn(context);
		when(context.getSceneId()).thenReturn("testScene");

		sceneManager.register("testScene", testScene);

		dispatcher.feedUpdate(update);

		ArgumentCaptor<UpdateContext> updateCtxCaptor = ArgumentCaptor.forClass(UpdateContext.class);

		await().atMost(3, TimeUnit.SECONDS)
			.pollInterval(10, TimeUnit.MILLISECONDS)
			.untilAsserted(() -> {
					verify(testScene, times(1)).routeUpdate(updateCtxCaptor.capture(), eq(context));
					verify(router, never()).routeUpdate(updateCtxCaptor.capture(), eq(context));
				}
			);
	}

	@Test
	void onUpdateThrowsStopMiddlewareExceptions() {
		Middleware stopMiddleware = mock(Middleware.class);
		Middleware neverCalledMiddleware = mock(Middleware.class);
		Update update = mock(Update.class);
		Message message = mock(Message.class);
		User user = mock(User.class);

		when(update.hasMessage()).thenReturn(true);
		when(update.hasChannelPost()).thenReturn(false);
		when(update.hasCallbackQuery()).thenReturn(false);
		when(update.hasEditedMessage()).thenReturn(false);
		when(update.hasEditedChannelPost()).thenReturn(false);

		when(update.getMessage()).thenReturn(message);
		when(message.getChatId()).thenReturn(12345L);
		when(message.getFrom()).thenReturn(user);
		when(user.getId()).thenReturn(67890L);

		FSMContext context = mock(FSMContext.class);
		when(storage.getOrCreateContext(any(StorageKey.class))).thenReturn(context);

		doThrow(new StopMiddlewareException()).when(stopMiddleware).preHandle(any(), any());

		dispatcher.feedUpdate(update);

		dispatcher.addMiddleware(stopMiddleware);
		dispatcher.addMiddleware(neverCalledMiddleware);

		ArgumentCaptor<UpdateContext> updateCtxCaptor = ArgumentCaptor.forClass(UpdateContext.class);

		await().atMost(3, TimeUnit.SECONDS)
			.pollInterval(10, TimeUnit.MILLISECONDS)
			.untilAsserted(() -> {
				verify(router, times(1)).routeUpdate(updateCtxCaptor.capture(), eq(context));
				verify(neverCalledMiddleware, never()).preHandle(any(), any());
				}
			);
	}

	@Test
	void onUpdateThrowsStopRoutingExceptions() {
		Middleware stopMiddleware = mock(Middleware.class);
		Update update = mock(Update.class);
		Message message = mock(Message.class);
		User user = mock(User.class);

		when(update.hasMessage()).thenReturn(true);
		when(update.hasChannelPost()).thenReturn(false);
		when(update.hasCallbackQuery()).thenReturn(false);
		when(update.hasEditedMessage()).thenReturn(false);
		when(update.hasEditedChannelPost()).thenReturn(false);

		when(update.getMessage()).thenReturn(message);
		when(message.getChatId()).thenReturn(12345L);
		when(message.getFrom()).thenReturn(user);
		when(user.getId()).thenReturn(67890L);

		FSMContext context = mock(FSMContext.class);
		when(storage.getOrCreateContext(any(StorageKey.class))).thenReturn(context);

		doThrow(new StopRoutingException()).when(stopMiddleware).preHandle(any(), any());

		dispatcher.feedUpdate(update);

		dispatcher.addMiddleware(stopMiddleware);

		ArgumentCaptor<UpdateContext> updateCtxCaptor = ArgumentCaptor.forClass(UpdateContext.class);

		await().atMost(3, TimeUnit.SECONDS)
			.pollInterval(10, TimeUnit.MILLISECONDS)
			.untilAsserted(() -> {
					verify(router, never()).routeUpdate(updateCtxCaptor.capture(), eq(context));
				}
			);
	}



}
