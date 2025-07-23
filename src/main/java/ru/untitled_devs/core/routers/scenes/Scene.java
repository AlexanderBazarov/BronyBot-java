package ru.untitled_devs.core.routers.scenes;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.UpdateRouter;

public abstract class Scene extends UpdateRouter {
	public abstract void enter(long chatId, FSMContext ctx);

	public abstract void leave(long chatId, FSMContext ctx);
}
