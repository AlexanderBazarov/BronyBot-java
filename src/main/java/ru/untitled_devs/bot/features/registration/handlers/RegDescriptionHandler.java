package ru.untitled_devs.bot.features.registration.handlers;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.untitled_devs.bot.features.registration.RegistrationStates;
import ru.untitled_devs.bot.shared.localisation.*;
import ru.untitled_devs.bot.shared.models.Profile;
import ru.untitled_devs.core.client.BotClient;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.filters.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RegDescriptionHandler extends RegStepHandler {
	private static int MAX_DESCRIPTION_LENGTH = 500;

	public RegDescriptionHandler(BotClient bot, Filter... filters) {
		super(bot, filters);
	}

	@Override
	void action(Message message, FSMContext ctx) {
		Locale loc = ctx.getData(langKey);
		String skipWord = BtnLocService.getLocal(ButtonKey.SKIP_WORD, loc);
		String text = message.getText();

		String description;
		if (Objects.equals(text, skipWord)) {
			description = TextLocalisationService.getLocal(
				TextKey.NO_DESCRIPTION, loc
			);
		} else {
			description = message.getText();
		}

		if (description.length() > MAX_DESCRIPTION_LENGTH) {
			bot.sendMessage(message.getChatId(),
				MsgLocService.getLocal(MessageKey.TOO_BIG_DESCRIPTION, loc));
			throw new IllegalArgumentException("Description length must be not bigger than 500 characters.");
		}

		Profile profileData = ctx.getData(profileKey);

		profileData.setDescription(description);

		ctx.setData(profileKey, profileData);
		ctx.setState(RegistrationStates.PHOTO);

		ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
		markup.setResizeKeyboard(true);
		markup.setOneTimeKeyboard(true);
		markup.setSelective(false);

		KeyboardRow row = new KeyboardRow();
		row.add(new KeyboardButton(
			BtnLocService.getLocal(ButtonKey.SKIP_WORD, loc))
		);

		List<KeyboardRow> rows = new ArrayList<>();
		rows.add(row);
		markup.setKeyboard(rows);

		bot.sendMessage(message.getChatId(),
			MsgLocService.getLocal(MessageKey.ASK_PHOTO, loc), markup);
	}
}
