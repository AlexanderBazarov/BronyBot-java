package ru.untitled_devs.bot.features.localisation.midlewares;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.bot.features.localisation.LocalisationStates;
import ru.untitled_devs.core.fsm.context.DataKey;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.fsm.states.StatesGroup;
import ru.untitled_devs.core.middlewares.Middleware;

import java.util.Locale;

public class LocalisationMiddleware implements Middleware {
	@Override
	public boolean preHandle(Update update, FSMContext ctx) {
		DataKey<Locale> key = DataKey.of("Lang", Locale.class);
		Locale loc = ctx.getData(key);

		if (StatesGroup.contains(LocalisationStates.class, ctx.getState()))
			return false;

		if (loc == null) {
			ctx.setState(LocalisationStates.START);
			return false;
		}

		return true;
	}
}
