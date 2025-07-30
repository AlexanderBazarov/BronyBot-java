package ru.untitled_devs.core.routers.scenes;

import ru.untitled_devs.core.fsm.FSMContext;
import ru.untitled_devs.core.routers.UpdateRouter;

public abstract class Scene extends UpdateRouter {
	public abstract void enter(long chatId, FSMContext ctx);

	public abstract void leave(long chatId, FSMContext ctx);
}
