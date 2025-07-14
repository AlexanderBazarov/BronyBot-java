package ru.untitled_devs.bot.features.localisation.midlewares;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.bot.features.localisation.LocalisationStates;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.context.DataKey;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.fsm.states.State;
import ru.untitled_devs.core.fsm.states.StatesGroup;
import ru.untitled_devs.core.middlewares.Middleware;
import ru.untitled_devs.core.routers.scenes.SceneManager;

import java.util.Locale;

public class LocalisationMiddleware implements Middleware {
	private final SceneManager sceneManager;
	public LocalisationMiddleware(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
	}

	@Override
	public boolean preHandle(UpdateContext update, FSMContext ctx) {
		DataKey<Locale> langKey = DataKey.of("lang", Locale.class);
		Locale loc = ctx.getData(langKey);

		if (StatesGroup.contains(LocalisationStates.class, ctx.getState()))
			return false;

		if (loc == null) {
			DataKey<Update> updateKey = DataKey.of("register:Update", Update.class);
			DataKey<State> stateKey = DataKey.of("register:Update", State.class);

			ctx.setData(updateKey, update.getUpdate());
			ctx.setData(stateKey, ctx.getState());

			sceneManager.enterScene("lang", update.getChatId(), ctx);
			return false;
		}

		return true;
	}
}
