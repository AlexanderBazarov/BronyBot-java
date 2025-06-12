package ru.untitled_devs.bot.registration.routes;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.untitled_devs.bot.registration.fsm.RegistrationStates;
import ru.untitled_devs.core.client.Bot;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.Router;
import ru.untitled_devs.core.routers.filters.Command;
import ru.untitled_devs.core.routers.handlers.MessageHandler;

public class RegistraitonRouter extends Router {
    private final Bot bot;

    public RegistraitonRouter(Bot bot) {
        this.bot = bot;
    }

    private void registerHandlers() {
        this.addHandler(RegistrationStates.START, new MessageHandler(this::startRegistration, new Command("start")));
    }

    private void startRegistration(Message message, FSMContext ctx) {

    }
}
