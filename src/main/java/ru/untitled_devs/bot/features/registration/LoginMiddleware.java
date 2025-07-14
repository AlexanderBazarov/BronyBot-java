package ru.untitled_devs.bot.features.registration;

import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.fsm.states.State;
import ru.untitled_devs.core.fsm.states.StatesGroup;
import ru.untitled_devs.core.middlewares.Middleware;
import ru.untitled_devs.core.routers.scenes.SceneManager;

public class LoginMiddleware extends Middleware {
	private final SceneManager sceneManager;

	public LoginMiddleware(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
	}

	@Override
	public void preHandle(UpdateContext update, FSMContext ctx) {
		State state = ctx.getState();
		if (! StatesGroup.contains(RegistrationStates.class, state)) {
			sceneManager.enterScene("register", update.getChatId(), ctx);
			stopRouting();
		}
	}
}
