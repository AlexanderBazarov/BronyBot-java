package ru.untitled_devs.bot.features.registration.middlewares;

import ru.untitled_devs.bot.features.registration.RegistrationStates;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.fsm.states.State;
import ru.untitled_devs.core.fsm.states.StatesGroup;
import ru.untitled_devs.core.middlewares.Middleware;
import ru.untitled_devs.core.routers.scenes.SceneManager;

public class LoginMiddleware implements Middleware {
	private final SceneManager sceneManager;

	public LoginMiddleware(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
	}

	@Override
	public boolean preHandle(UpdateContext update, FSMContext ctx) {
		State state = ctx.getState();
		if (! StatesGroup.contains(RegistrationStates.class, state)) {
			sceneManager.enterScene("register", update.getChatId(), ctx);
			return false;
		}
		return true;
	}
}
