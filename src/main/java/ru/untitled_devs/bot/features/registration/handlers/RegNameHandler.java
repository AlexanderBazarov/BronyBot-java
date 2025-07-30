package ru.untitled_devs.bot.features.registration.handlers;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.untitled_devs.bot.features.registration.RegistrationStates;
import ru.untitled_devs.bot.shared.localisation.MessageKey;
import ru.untitled_devs.bot.shared.localisation.MsgLocService;
import ru.untitled_devs.bot.shared.models.Profile;
import ru.untitled_devs.core.client.BotClient;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.filters.Filter;

import java.util.Locale;

public class RegNameHandler extends RegStepHandler {
	private static int MAX_NAME_LENGTH = 100;

	public RegNameHandler(BotClient bot, Filter... filters) {
		super(bot, filters);
	}

	@Override
	void action(Message message, FSMContext ctx) {
		Locale loc = ctx.getData(langKey);
		String name = message.getText().strip();
		Profile profileData = ctx.getData(profileKey);

		if (name.length() > MAX_NAME_LENGTH) {
			bot.sendMessage(message.getChatId(),
				MsgLocService.getLocal(
					MessageKey.TOO_BIG_NAME,
					loc
				)
			);
			return;
		}

		profileData.setName(name);

		ctx.setData(profileKey, profileData);
		ctx.setState(RegistrationStates.AGE);

		bot.sendMessage(message.getChatId(),
			MsgLocService.getLocal(MessageKey.ASK_AGE, loc));
	}
}
