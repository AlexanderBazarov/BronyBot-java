package ru.untitled_devs.bot.features.registration.handlers;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.untitled_devs.bot.features.registration.RegistrationService;
import ru.untitled_devs.bot.features.registration.RegistrationStates;
import ru.untitled_devs.bot.shared.localisation.ButtonKey;
import ru.untitled_devs.bot.shared.localisation.BtnLocService;
import ru.untitled_devs.bot.shared.localisation.MessageKey;
import ru.untitled_devs.bot.shared.localisation.MsgLocService;
import ru.untitled_devs.bot.shared.models.Profile;
import ru.untitled_devs.bot.shared.models.User;
import ru.untitled_devs.core.client.BotClient;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.filters.Filter;
import ru.untitled_devs.core.routers.scenes.SceneManager;

import java.util.Locale;
import java.util.Objects;

public class RegFinishHandler extends RegStepHandler {
	private final RegistrationService regService;
	public final SceneManager sceneManager;

	public RegFinishHandler(BotClient bot, RegistrationService regService, SceneManager sceneManager, Filter... filters) {
		super(bot, filters);
		this.regService = regService;
		this.sceneManager = sceneManager;
	}

	@Override
	void action(Message message, FSMContext ctx) {
		Locale loc = ctx.getData(langKey);
		long chatId = message.getChatId();

		String createProfileButton = BtnLocService.getLocal(
			ButtonKey.CREATE_PROFILE,
			loc
		);
		String rewriteProfileButton = BtnLocService.getLocal(
			ButtonKey.REWRITE_PROFILE,
			loc
		);

		if (Objects.equals(message.getText(), rewriteProfileButton)) {
			ctx.setData(profileKey, new Profile());
			ctx.setState(RegistrationStates.NAME);

			bot.sendMessage(chatId,
				MsgLocService.getLocal(MessageKey.WHATS_YOUR_NAME, loc));
			return;
		}

		if (Objects.equals(message.getText(), createProfileButton)) {
			Profile profileData = ctx.getData(profileKey);

			User userData = new User();
			userData.setTelegramId(chatId);
			userData.setLang(loc.toLanguageTag());

			regService.registerUser(userData, profileData);
			bot.sendMessage(chatId, MsgLocService.getLocal(MessageKey.SUCCESSFUL_REGISTRATION, loc));
			sceneManager.enterScene("menu", chatId, ctx);
			return;
		}

		bot.sendMessage(chatId,
			MsgLocService.getLocal(MessageKey.INCORRECT_INPUT, loc));
	}
}
