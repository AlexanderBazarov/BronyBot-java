package ru.untitled_devs.bot.features.localisation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.context.UpdateContextFactory;
import ru.untitled_devs.core.exceptions.StopMiddlewareException;
import ru.untitled_devs.core.exceptions.StopRoutingException;
import ru.untitled_devs.core.fsm.DataKey;
import ru.untitled_devs.core.fsm.FSMContext;
import ru.untitled_devs.core.routers.scenes.SceneManager;

import java.util.Locale;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class LocalisationMiddlewareTest {
	DataKey<Locale> langKey = DataKey.of("lang", Locale.class);
	LocalisationMiddleware middleware;

	FSMContext context;
	Update update;
	SceneManager sceneManager;

	@BeforeEach
	void setup() {
		context = mock(FSMContext.class);
		update = mock(Update.class);
		sceneManager = mock(SceneManager.class);
		middleware = new LocalisationMiddleware(sceneManager);

		Message message = mock(Message.class);
		when(message.getChatId()).thenReturn(124512L);
		when(message.hasText()).thenReturn(true);
		when(message.getText()).thenReturn("Test");
		when(update.hasMessage()).thenReturn(true);
		when(update.getMessage()).thenReturn(message);
	}

	@Test
	void preHandleSwitchToLocalisationScene() {
		when(context.getData(langKey)).thenReturn(null);

		UpdateContext updateContext = UpdateContextFactory.create(update);
		assertThrows(StopRoutingException.class,
			() -> middleware.preHandle(updateContext, context));
		verify(sceneManager).enterScene(any(Long.class), eq("lang"), eq(context));
	}

	@Test
	void preHandleSkipMiddlewareChainScene() {
		when(context.getState()).thenReturn(LocalisationStates.START);

		UpdateContext updateContext = UpdateContextFactory.create(update);
		assertThrows(StopMiddlewareException.class,
			() -> middleware.preHandle(updateContext, context));
	}

	@Test
	void preHandleSkipLocalisationScene() {
		when(context.getData(langKey)).thenReturn(Locale.of("ru_RU"));

		UpdateContext updateContext = UpdateContextFactory.create(update);
		assertDoesNotThrow(() -> middleware.preHandle(updateContext, context));
	}
}
