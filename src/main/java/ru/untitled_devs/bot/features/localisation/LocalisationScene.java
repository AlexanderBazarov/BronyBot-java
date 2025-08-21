package ru.untitled_devs.bot.features.localisation;

import com.google.inject.Inject;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.untitled_devs.core.client.PollingClient;
import ru.untitled_devs.core.fsm.DataKey;
import ru.untitled_devs.core.fsm.FSMContext;
import ru.untitled_devs.core.fsm.states.DefaultStates;
import ru.untitled_devs.core.fsm.states.State;
import ru.untitled_devs.core.routers.handlers.CallbackQueryHandler;
import ru.untitled_devs.core.routers.handlers.MessageHandler;
import ru.untitled_devs.core.routers.scenes.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class LocalisationScene extends Scene {
	private final PollingClient bot;

	@Inject
	public LocalisationScene(PollingClient bot) {
		this.bot = bot;

		this.registerHandlers();
	}

	private void registerHandlers() {
		addHandler(LocalisationStates.GETLANG, new CallbackQueryHandler(this::getLang));
		addHandler(DefaultStates.ANY, new MessageHandler(this::incorrectInput));
	}

	@Override
	public void leave(long chatId, FSMContext ctx) {

	}

	@Override
	public void enter(long chatId, FSMContext ctx) {
		InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

		InlineKeyboardButton ruButton = InlineKeyboardButton.builder()
			.text("Рус")
			.callbackData("ru-RU")
			.build();

		InlineKeyboardButton enButton = InlineKeyboardButton.builder()
			.text("Eng")
			.callbackData("en-US")
			.build();

		List<InlineKeyboardButton> row = new ArrayList<>();
		row.add(ruButton);
		row.add(enButton);

		List<List<InlineKeyboardButton>> rows = new ArrayList<>();
		rows.add(row);

		markup.setKeyboard(rows);

		Message sentMessage = bot.sendMessage(chatId, "Please, setup Your Language", markup);

		DataKey<Integer> messageKey = DataKey.of("getLangMessageId", Integer.class);

		ctx.setData(messageKey, sentMessage.getMessageId());
		ctx.setState(LocalisationStates.GETLANG);
	}

	private void getLang(CallbackQuery callback, FSMContext ctx) {
		DataKey<Locale> key = DataKey.of("lang", Locale.class);
		ctx.setData(key, Locale.forLanguageTag(callback.getData()));

		DataKey<Update> updateKey = DataKey.of("register:Update", Update.class);
		DataKey<State> stateKey = DataKey.of("register:State", State.class);
		DataKey<String> sceneIdKey = DataKey.of("register:Scene", String.class);

		Update update = ctx.getData(updateKey);
		State state = ctx.getData(stateKey);
		String sceneId = ctx.getData(sceneIdKey);

		DataKey<Integer> messageKey = DataKey.of("getLangMessageId", Integer.class);
		int langMessageId = ctx.getData(messageKey);

		bot.deleteMessage(callback.getMessage().getChatId(), langMessageId);

		ctx.setState(state);
		ctx.setSceneId(sceneId);
		bot.getDispatcher().processUpdate(update);
	}

	private void incorrectInput(Message message, FSMContext ctx) {
		enter(message.getChatId(), ctx);
	}

}

