package ru.untitled_devs.bot.features.start;

import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.FSMContext;
import ru.untitled_devs.core.middlewares.Middleware;
import ru.untitled_devs.core.routers.scenes.SceneManager;

import java.util.Objects;

public class StartMiddleware extends Middleware {
	private final SceneManager sceneManager;
	public StartMiddleware(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
	}
	@Override
	public void preHandle(UpdateContext update, FSMContext ctx) {
		if (update.hasMessage() && Objects.equals(update.getText(), "/start")) {
			sceneManager.enterScene(update.getChatId(), "menu", ctx);
		}
	}
}
