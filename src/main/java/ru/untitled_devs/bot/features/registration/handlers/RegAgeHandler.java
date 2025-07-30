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

public class RegAgeHandler extends RegStepHandler {
	private static int MIN_AGE = 5;
	private static int MAX_AGE = 120;

	public RegAgeHandler(BotClient bot, Filter... filters) {
		super(bot, filters);
	}

	@Override
	void action(Message message, FSMContext ctx) {
		Locale loc = ctx.getData(langKey);

		int age;

		try {
			age = Integer.parseInt(message.getText());
		} catch (NumberFormatException e) {
			bot.sendMessage(message.getChatId(),
				MsgLocService.getLocal(MessageKey.AGE_ERROR, loc));
			throw new IllegalArgumentException("Message must contain only digits");
		}

		if (age < MIN_AGE) {
			bot.sendMessage(message.getChatId(),
				MsgLocService.getLocal(MessageKey.TOO_SMALL_AGE, loc));
			throw new IllegalArgumentException("Age must be in interval from 5 to 100");
		}

		if (age > MAX_AGE) {
			bot.sendMessage(message.getChatId(),
				MsgLocService.getLocal(MessageKey.TOO_BIG_AGE, loc));
			throw new IllegalArgumentException("Age must be in interval from 5 to 100");
		}

		Profile profileData = ctx.getData(profileKey);

		profileData.setAge(age);

		ctx.setData(profileKey, profileData);
		ctx.setState(RegistrationStates.LOCATION);

		bot.sendMessage(message.getChatId(),
			MsgLocService.getLocal(MessageKey.ASK_LOCATION, loc));
	}
}
