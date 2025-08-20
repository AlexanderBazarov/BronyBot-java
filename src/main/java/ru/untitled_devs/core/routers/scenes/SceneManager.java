package ru.untitled_devs.core.routers.scenes;

import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.FSMContext;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {
	private final Map<String, Scene> scenes = new HashMap<>();

	public void register(String sceneId, Scene scene) {
		scenes.put(sceneId, scene);
	}

	public Scene getScene(String sceneId) {
		return scenes.get(sceneId);
	}

	public void enterScene(long chatId, String sceneId, FSMContext ctx) {
		leaveCurrentScene(chatId, ctx);

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

	public void handle(UpdateContext update, FSMContext ctx) throws IllegalArgumentException{
		String id = ctx.getSceneId();
		if (id == null) throw new IllegalArgumentException();
		Scene scene = scenes.get(id);
		if (scene == null) throw new IllegalArgumentException();

		scene.routeUpdate(update, ctx);
	}
}
