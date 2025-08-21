package ru.untitled_devs.bot.features.registration;

import com.google.inject.Inject;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.FSMContext;
import ru.untitled_devs.core.fsm.states.State;
import ru.untitled_devs.core.fsm.states.StatesGroup;
import ru.untitled_devs.core.middlewares.Middleware;
import ru.untitled_devs.core.routers.scenes.SceneManager;

public class LoginMiddleware extends Middleware {
	private final SceneManager sceneManager;
	private final RegistrationService regService;

	@Inject
	public LoginMiddleware(SceneManager sceneManager, RegistrationService regService) {
		this.sceneManager = sceneManager;
		this.regService = regService;
	}

	@Override
	public void preHandle(UpdateContext update, FSMContext ctx) {
		if (regService.isUseRegistered(update.getChatId()))
			return;

		State state = ctx.getState();
		if (! StatesGroup.contains(RegistrationStates.class, state)) {
			sceneManager.enterScene(update.getChatId(), "register", ctx);
			stopRouting();
		}
	}
}
