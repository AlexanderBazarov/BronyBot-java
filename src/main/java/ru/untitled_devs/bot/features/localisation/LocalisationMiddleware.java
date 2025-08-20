package ru.untitled_devs.bot.features.localisation;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.DataKey;
import ru.untitled_devs.core.fsm.FSMContext;
import ru.untitled_devs.core.fsm.states.State;
import ru.untitled_devs.core.fsm.states.StatesGroup;
import ru.untitled_devs.core.middlewares.Middleware;
import ru.untitled_devs.core.routers.scenes.SceneManager;

import java.util.Locale;

public class LocalisationMiddleware extends Middleware {
	private final SceneManager sceneManager;
	public LocalisationMiddleware(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
	}

	@Override
	public void preHandle(UpdateContext update, FSMContext ctx) {
		DataKey<Locale> langKey = DataKey.of("lang", Locale.class);
		Locale loc = ctx.getData(langKey);

		if (StatesGroup.contains(LocalisationStates.class, ctx.getState()))
			stopMiddlewareChain();

		if (loc == null) {
			DataKey<Update> updateKey = DataKey.of("register:Update", Update.class);
			DataKey<State> stateKey = DataKey.of("register:State", State.class);
			DataKey<String> sceneIdKey = DataKey.of("register:Scene", String.class);

			ctx.setData(updateKey, update.getUpdate());
			ctx.setData(stateKey, ctx.getState());
			ctx.setData(sceneIdKey, ctx.getSceneId());

			sceneManager.enterScene(update.getChatId(), "lang", ctx);
			stopRouting();
		}

	}
}
