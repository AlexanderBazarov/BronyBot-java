package ru.untitled_devs.bot.features.registration.handlers;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.untitled_devs.bot.features.registration.RegistrationStates;
import ru.untitled_devs.bot.shared.localisation.BtnLocService;
import ru.untitled_devs.bot.shared.localisation.ButtonKey;
import ru.untitled_devs.bot.shared.localisation.MessageKey;
import ru.untitled_devs.bot.shared.localisation.MsgLocService;
import ru.untitled_devs.bot.shared.models.Gender;
import ru.untitled_devs.bot.shared.models.Profile;
import ru.untitled_devs.core.client.BotClient;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.filters.Filter;

import java.util.Locale;
import java.util.Map;

public class RegGenderHandler extends RegStepHandler {
	public RegGenderHandler(BotClient bot, Filter... filters) {
		super(bot, filters);
	}

	@Override
	void action(Message message, FSMContext ctx) {
		Locale loc = ctx.getData(langKey);
		Profile profileData = ctx.getData(profileKey);

		String text = message.getText();

		String maleButton = BtnLocService.getLocal(ButtonKey.GENDER_MALE, loc);
		String femaleButton = BtnLocService.getLocal(ButtonKey.GENDER_FEMALE, loc);
		String noneGenderButton = BtnLocService.getLocal(ButtonKey.GENDER_NONE, loc);

		Map<String, Gender> gendersMap = Map.of(
			maleButton, Gender.MALE,
			femaleButton, Gender.FEMALE,
			noneGenderButton, Gender.NONE
		);

		Gender selectedGender = gendersMap.get(text);

		if (selectedGender != null) {
			profileData.setGender(selectedGender);
			ctx.setState(RegistrationStates.LOCATION);
			bot.sendMessage(message.getChatId(), MsgLocService.getLocal(MessageKey.ASK_LOCATION, loc));
		} else {
			bot.sendMessage(message.getChatId(), MsgLocService.getLocal(MessageKey.INCORRECT_INPUT, loc));
			throw new IllegalArgumentException();
		}

	}
}
