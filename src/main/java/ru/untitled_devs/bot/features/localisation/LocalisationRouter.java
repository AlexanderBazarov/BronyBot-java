package ru.untitled_devs.bot.features.localisation;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.untitled_devs.core.client.PollingClient;
import ru.untitled_devs.core.fsm.context.DataKey;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.fsm.states.DefaultStates;
import ru.untitled_devs.core.fsm.states.State;
import ru.untitled_devs.core.routers.Router;
import ru.untitled_devs.core.routers.handlers.CallbackQueryHandler;
import ru.untitled_devs.core.routers.handlers.MessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class LocalisationRouter extends Router {
	private final PollingClient bot;

	public LocalisationRouter(PollingClient bot) {
		this.bot = bot;

		this.registerHandlers();
	}

	private void registerHandlers() {
		addHandler(LocalisationStates.START, new MessageHandler(this::askLang));
		addHandler(LocalisationStates.GETLANG, new CallbackQueryHandler(this::getLang));
	}

	private void askLang(Message message, FSMContext ctx) {
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

		bot.sendMessage(message.getChatId(), "Please, setup Your Language", markup);

		ctx.setState(LocalisationStates.GETLANG);
	}

	private void askLangCallback(CallbackQuery callback, FSMContext ctx) {

	}

	private void getLang(CallbackQuery callback, FSMContext ctx) {
		DataKey<Locale> key = DataKey.of("lang", Locale.class);
		ctx.setData(key, Locale.forLanguageTag(callback.getData()));

		DataKey<Update> updateKey = DataKey.of("register:Update", Update.class);
		DataKey<State> stateKey = DataKey.of("register:Update", State.class);

		Update update = ctx.getData(updateKey);
		State state = ctx.getData(stateKey);

		ctx.setState(state);
		bot.getDispatcher().processUpdate(update);
	}

}

