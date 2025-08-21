package ru.untitled_devs.bot.features.registration;

import com.google.inject.Inject;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.untitled_devs.bot.shared.image.ImageService;
import ru.untitled_devs.bot.shared.localisation.ButtonKey;
import ru.untitled_devs.bot.shared.localisation.BtnLocService;
import ru.untitled_devs.bot.shared.localisation.MessageKey;
import ru.untitled_devs.bot.shared.localisation.MsgLocService;
import ru.untitled_devs.bot.shared.models.Profile;
import ru.untitled_devs.core.client.BotClient;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class ProfilePreviewService {
	private final ImageService imageService;
	private final BotClient bot;

	@Inject
	public ProfilePreviewService(BotClient bot, ImageService imageService) {
		this.bot = bot;
		this.imageService = imageService;
	}

	public void sendPreview(Profile profile, Locale loc, long chatId) {
		String text = MsgLocService.getLocal(MessageKey.PROFILE_MESSAGE, loc,
			profile.getName(), profile.getAge(), profile.getLocation(), profile.getDescription()
		);

		ReplyKeyboardMarkup markup = buildKeyboard(loc);

		if (profile.hasImage()) {
			URL imageUrl = imageService.getImageURL(profile.getImage());
			bot.sendPhoto(chatId, text, imageUrl, markup);
		} else {
			bot.sendMessage(chatId, text, markup);
		}
	}

	private ReplyKeyboardMarkup buildKeyboard(Locale loc) {
		KeyboardRow row = new KeyboardRow();
		row.add(new KeyboardButton(BtnLocService.getLocal(ButtonKey.CREATE_PROFILE, loc)));
		row.add(new KeyboardButton(BtnLocService.getLocal(ButtonKey.REWRITE_PROFILE, loc)));

		List<KeyboardRow> rows = List.of(row);
		ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
		markup.setKeyboard(rows);
		markup.setResizeKeyboard(true);
		markup.setOneTimeKeyboard(true);
		markup.setSelective(false);
		return markup;
	}
}
