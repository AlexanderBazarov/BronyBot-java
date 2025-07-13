package ru.untitled_devs.core.routers.scenes;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.context.FSMContext;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {
	private final Map<String, Scene> scenes = new HashMap<>();

	public void register(Scene scene) {
		scenes.put(scene.getId(), scene);
	}

	public void enterScene(String sceneId, long chatId, FSMContext ctx) {
		Scene scene = scenes.get(sceneId);
		if (scene == null) return;

		ctx.setSceneId(sceneId);
		scene.enter(chatId, ctx);
	}

	public void leaveCurrentScene(long chatId, FSMContext ctx) {
		String sceneId = ctx.getSceneId();
		Scene scene = scenes.get(sceneId);
		if (scene != null) {
			scene.leave(chatId, ctx);
		}
		ctx.clearScene();
	}

	public boolean handle(UpdateContext update, FSMContext ctx) {
		String id = ctx.getSceneId();
		if (id == null) return false;
		Scene scene = scenes.get(id);
		if (scene == null) return false;

		return scene.routeUpdate(update, ctx);
	}
}
