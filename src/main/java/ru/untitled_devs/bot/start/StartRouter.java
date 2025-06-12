package ru.untitled_devs.bot.start;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.untitled_devs.core.client.Bot;
import ru.untitled_devs.core.fsm.states.DefaultStates;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.Router;
import ru.untitled_devs.core.routers.filters.CommandStart;
import ru.untitled_devs.core.routers.handlers.MessageHandler;

public class StartRouter extends Router {
    private final Bot bot;
    public StartRouter(Bot bot) {
        this.bot = bot;
        this.registerHandlers();
    }

    private void registerHandlers() {
        this.addHandler(DefaultStates.DEFAULT,
                new MessageHandler(this::start,
                                    new CommandStart()));
    }

    private void start(Message message, FSMContext ctx) {
        this.bot.sendMessage(message.getChatId(), "Привет! Это стратовое сообщение, лол!!!!");
    }
}
