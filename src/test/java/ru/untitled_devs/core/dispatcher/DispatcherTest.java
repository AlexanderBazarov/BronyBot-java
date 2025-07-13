package ru.untitled_devs.core.dispatcher;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.fsm.storage.Storage;
import ru.untitled_devs.core.fsm.storage.StorageKey;
import ru.untitled_devs.core.routers.UpdateRouter;
import ru.untitled_devs.core.routers.scenes.SceneManager;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DispatcherTest {
	static Storage storage;
	static UpdateRouter router;
	static Logger logger;
	static Dispatcher dispatcher;
	static SceneManager sceneManager;

	@BeforeAll
	static void setup() {
		storage = mock(Storage.class);
		router = mock(UpdateRouter.class);
		logger = mock(Logger.class);
		sceneManager = mock(SceneManager.class);
		dispatcher = new Dispatcher(storage, logger, sceneManager);
		dispatcher.addRouter(router);
	}

	@Test
	void onUpdateMessageReceivedRoutesUpdateToAllRouters() throws ExecutionException, InterruptedException, TimeoutException {
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
	void onUpdateCallbackQueryReceivedRoutesUpdateToAllRouters() throws ExecutionException, InterruptedException, TimeoutException {
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
	void onUpdateEditedMessageReceivedRoutesUpdateToAllRouters() throws ExecutionException, InterruptedException, TimeoutException {
		Update update = mock(Update.class);
		Message editedMessage = mock(Message.class);
		User user = mock(User.class);

		when(update.hasEditedMessage()).thenReturn(true);
		when(update.hasMessage()).thenReturn(false);
		when(update.hasChannelPost()).thenReturn(false);
		when(update.hasCallbackQuery()).thenReturn(false);
		when(update.hasEditedChannelPost()).thenReturn(false);

		when(update.getEditedMessage()).thenReturn(editedMessage);
		when(editedMessage.getChatId()).thenReturn(12345L);
		when(editedMessage.getFrom()).thenReturn(user);
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
	void onUpdateChanelPostReceivedRoutesUpdateToAllRouters() throws ExecutionException, InterruptedException, TimeoutException {
		Update update = mock(Update.class);
		Message postMessage = mock(Message.class);
		User user = mock(User.class);

		when(update.hasChannelPost()).thenReturn(true);
		when(update.hasMessage()).thenReturn(false);
		when(update.hasCallbackQuery()).thenReturn(false);
		when(update.hasEditedMessage()).thenReturn(false);
		when(update.hasEditedChannelPost()).thenReturn(false);

		when(update.getChannelPost()).thenReturn(postMessage);
		when(postMessage.getFrom()).thenReturn(user);
		when(postMessage.getChatId()).thenReturn(12345L);
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
	void onUpdateEditedChanelPostReceivedRoutesUpdateToAllRouters() throws ExecutionException, InterruptedException, TimeoutException {
		Update update = mock(Update.class);
		Message editedPostMessage = mock(Message.class);
		User user = mock(User.class);

		when(update.hasEditedChannelPost()).thenReturn(true);
		when(update.hasMessage()).thenReturn(false);
		when(update.hasChannelPost()).thenReturn(false);
		when(update.hasCallbackQuery()).thenReturn(false);
		when(update.hasEditedMessage()).thenReturn(false);

		when(update.getEditedChannelPost()).thenReturn(editedPostMessage);
		when(editedPostMessage.getChatId()).thenReturn(12345L);
		when(editedPostMessage.getFrom()).thenReturn(user);
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

}
