package ru.untitled_devs.bot.features.registration.middlewares;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.bot.features.registration.RegistrationStates;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.fsm.states.State;
import ru.untitled_devs.core.fsm.states.StatesGroup;
import ru.untitled_devs.core.middlewares.Middleware;

public class LoginMiddleware implements Middleware {
	@Override
	public boolean preHandle(Update update, FSMContext ctx) {
		State state = ctx.getState();
		if (! StatesGroup.contains(RegistrationStates.class, state)) {
			ctx.setState(RegistrationStates.START);
		}
		return true;
	}
}
