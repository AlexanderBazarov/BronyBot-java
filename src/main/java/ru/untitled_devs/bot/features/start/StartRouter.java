package ru.untitled_devs.bot.features.start;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.untitled_devs.core.client.PollingClient;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.fsm.states.DefaultStates;
import ru.untitled_devs.core.routers.UpdateRouter;
import ru.untitled_devs.core.routers.filters.CommandStart;
import ru.untitled_devs.core.routers.handlers.MessageHandler;


public final class StartRouter extends UpdateRouter {
    private final PollingClient bot;
    public StartRouter(PollingClient bot) {
        this.bot = bot;
        this.registerHandlers();
    }

    private void registerHandlers() {
        this.addHandler(DefaultStates.DEFAULT,
                new MessageHandler(this::start,
                                    new CommandStart()));
    }

	private void start(Message message, FSMContext ctx) {

    }
}
