package ru.untitled_devs.bot.features.start;

import com.google.inject.Inject;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.untitled_devs.core.client.PollingClient;
import ru.untitled_devs.core.fsm.FSMContext;
import ru.untitled_devs.core.fsm.states.DefaultStates;
import ru.untitled_devs.core.routers.UpdateRouter;
import ru.untitled_devs.core.routers.filters.CommandStart;
import ru.untitled_devs.core.routers.handlers.MessageHandler;
import ru.untitled_devs.core.routers.scenes.SceneManager;

public final class StartRouter extends UpdateRouter {
    private final PollingClient bot;
	private final SceneManager sceneManager;

	@Inject
    public StartRouter(PollingClient bot, SceneManager sceneManager) {
        this.bot = bot;
		this.sceneManager = sceneManager;
        this.registerHandlers();
    }

    private void registerHandlers() {
        this.addHandler(DefaultStates.ANY,
                new MessageHandler(this::start,
                                    new CommandStart()));
    }

	private void start(Message message, FSMContext ctx) {
		sceneManager.enterScene(message.getChatId(), "menu", ctx);
    }
}
