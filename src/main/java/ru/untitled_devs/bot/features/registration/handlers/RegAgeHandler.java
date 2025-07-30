package ru.untitled_devs.bot.features.registration.handlers;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.untitled_devs.bot.features.registration.RegistrationStates;
import ru.untitled_devs.bot.shared.localisation.BtnLocService;
import ru.untitled_devs.bot.shared.localisation.ButtonKey;
import ru.untitled_devs.bot.shared.localisation.MessageKey;
import ru.untitled_devs.bot.shared.localisation.MsgLocService;
import ru.untitled_devs.bot.shared.models.Profile;
import ru.untitled_devs.core.client.BotClient;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.filters.Filter;

import java.util.ArrayList;
import java.util.List;
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
		ctx.setState(RegistrationStates.GENDER);

		ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
		markup.setResizeKeyboard(true);
		markup.setOneTimeKeyboard(true);
		markup.setSelective(false);

		KeyboardRow row = new KeyboardRow();
		row.add(new KeyboardButton(
			BtnLocService.getLocal(ButtonKey.GENDER_MALE, loc))
		);
		row.add(new KeyboardButton(
			BtnLocService.getLocal(ButtonKey.GENDER_FEMALE, loc))
		);
		row.add(new KeyboardButton(
			BtnLocService.getLocal(ButtonKey.GENDER_NONE, loc))
		);

		List<KeyboardRow> rows = new ArrayList<>();
		rows.add(row);
		markup.setKeyboard(rows);

		bot.sendMessage(message.getChatId(),
			MsgLocService.getLocal(MessageKey.ASK_SEX, loc), markup);
	}
}
