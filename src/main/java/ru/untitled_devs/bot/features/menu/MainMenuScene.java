package ru.untitled_devs.bot.features.menu;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.untitled_devs.bot.shared.localisation.ButtonKey;
import ru.untitled_devs.bot.shared.localisation.ButtonsLocalisationService;
import ru.untitled_devs.bot.shared.localisation.MessageKey;
import ru.untitled_devs.bot.shared.localisation.MessagesLocalisationService;
import ru.untitled_devs.core.client.BotClient;
import ru.untitled_devs.core.fsm.context.DataKey;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.scenes.Scene;
import ru.untitled_devs.core.routers.scenes.SceneManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainMenuScene extends Scene {
	private final BotClient bot;
	private final SceneManager sceneManager;

	private final static DataKey<Locale> langKey = DataKey.of("lang", Locale.class);

	public MainMenuScene(BotClient bot, SceneManager sceneManager) {
		this.bot = bot;
		this.sceneManager = sceneManager;
	}

	@Override
	public void enter(long chatId, FSMContext ctx) {
		Locale loc = ctx.getData(langKey);
		bot.sendMessage(chatId,
			MessagesLocalisationService.getLocal(
				MessageKey.MAIN_MENU,
				loc
			),
			getMainMenuMarkup(loc)
		);
		ctx.setState(MainMenuStates.MENU);
	}

	@Override
	public void leave(long chatId, FSMContext ctx) {

	}

	private ReplyKeyboardMarkup getMainMenuMarkup(Locale loc) {
		ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
		markup.setResizeKeyboard(true);
		markup.setOneTimeKeyboard(true);
		markup.setSelective(false);

		KeyboardRow row = new KeyboardRow();
		row.add(new KeyboardButton(
			ButtonsLocalisationService.getLocal(ButtonKey.MY_PROFILE, loc))
		);
		row.add(new KeyboardButton(
			ButtonsLocalisationService.getLocal(ButtonKey.VIEW_PROFILES, loc))
		);
		row.add(new KeyboardButton(
			ButtonsLocalisationService.getLocal(ButtonKey.VIEW_LIKES, loc))
		);

		List<KeyboardRow> rows = new ArrayList<>();
		rows.add(row);
		markup.setKeyboard(rows);
		return markup;
	}

	private void getMainMenuButton(Message message, FSMContext ctx) {
		Locale loc = ctx.getData(langKey);
		String text = message.getText().strip();
		long chatId = message.getChatId();

		final String myProfileButton = ButtonsLocalisationService.getLocal(ButtonKey.MY_PROFILE, loc);
		final String viewProfilesButton = ButtonsLocalisationService.getLocal(ButtonKey.VIEW_PROFILES, loc);
		final String viewLikesButton = ButtonsLocalisationService.getLocal(ButtonKey.VIEW_LIKES, loc);

		if (text.equals(myProfileButton))
			openViewLikesScene(chatId, ctx);
		else if (text.equals(viewProfilesButton))
			openViewProfilesScene(chatId, ctx);
		else if (text.equals(viewLikesButton))
			openViewLikesScene(chatId, ctx);
	}
	
	private void openMyProfileScene(long chatId, FSMContext ctx) {
		sceneManager.enterScene("myProfile", chatId, ctx);
	}
	private void openViewProfilesScene(long chatId, FSMContext ctx) {
		sceneManager.enterScene("viewProfiles", chatId, ctx);
	}
	private void openViewLikesScene(long chatId, FSMContext ctx) {
		sceneManager.enterScene("viewLikes", chatId, ctx);
	}

}
