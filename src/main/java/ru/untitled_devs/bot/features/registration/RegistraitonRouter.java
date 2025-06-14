package ru.untitled_devs.bot.features.registration;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.untitled_devs.bot.shared.localisation.LocalisationService;
import ru.untitled_devs.bot.shared.localisation.MessageKey;
import ru.untitled_devs.core.client.Bot;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.Router;
import ru.untitled_devs.core.routers.filters.Command;
import ru.untitled_devs.core.routers.handlers.MessageHandler;

import java.util.Locale;

public final class RegistraitonRouter extends Router {
    private final Bot bot;

    public RegistraitonRouter(Bot bot) {
        this.bot = bot;
    }

    private void registerHandlers() {
        this.addHandler(RegistrationStates.START, new MessageHandler(this::startRegistration, new Command("start")));
    }

	Locale loc = Locale.forLanguageTag("ru-RU");

    private void startRegistration(Message message, FSMContext ctx) {
		bot.sendMessage(message.getChatId(),
			LocalisationService.getMessage(MessageKey.WHATS_YOUR_NAME, loc));
    }
}
