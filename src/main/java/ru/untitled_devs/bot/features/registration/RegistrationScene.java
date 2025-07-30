package ru.untitled_devs.bot.features.registration;

import ru.untitled_devs.bot.features.registration.handlers.*;
import ru.untitled_devs.bot.shared.geocoder.Geocoder;
import ru.untitled_devs.bot.shared.image.ImageService;
import ru.untitled_devs.bot.shared.localisation.MessageKey;
import ru.untitled_devs.bot.shared.localisation.MsgLocService;
import ru.untitled_devs.bot.shared.models.Profile;
import ru.untitled_devs.core.client.BotClient;
import ru.untitled_devs.core.fsm.context.DataKey;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.scenes.Scene;
import ru.untitled_devs.core.routers.scenes.SceneManager;

import java.util.Locale;

public final class RegistrationScene extends Scene {
	private final BotClient bot;
	private final RegistrationService regService;
	private final Geocoder geocoder;
	private final ImageService imageService;
	private final SceneManager sceneManager;

	public RegistrationScene(BotClient bot, RegistrationService regService,
							 Geocoder geocoder, ImageService imageService, SceneManager sceneManager) {
		this.bot = bot;
		this.regService = regService;
		this.geocoder = geocoder;
		this.imageService = imageService;
		this.sceneManager = sceneManager;

		registerHandlers();
	}

	private void registerHandlers() {
		addHandler(RegistrationStates.NAME, new RegNameHandler(bot));
		addHandler(RegistrationStates.AGE, new RegAgeHandler(bot));
		addHandler(RegistrationStates.GENDER, new RegGenderHandler(bot));
		addHandler(RegistrationStates.LOCATION, new RegLocationHandler(bot, geocoder));
		addHandler(RegistrationStates.DESCRIPTION, new RegDescriptionHandler(bot));
		addHandler(RegistrationStates.PHOTO, new RegPhotoHandler(bot, imageService));
		addHandler(RegistrationStates.FINISH, new RegFinishHandler(bot, regService, sceneManager));
	}

	public void enter(long chatId, FSMContext ctx) {
		DataKey<Locale> langKey = DataKey.of("lang", Locale.class);
		Locale loc = ctx.getData(langKey);

		bot.sendMessage(chatId,
			MsgLocService.getLocal(MessageKey.NOT_REGISTERED, loc));

		Profile profileData = new Profile();
		DataKey<Profile> key = DataKey.of("RegistrationData", Profile.class);

		ctx.setData(key, profileData);
		ctx.setState(RegistrationStates.NAME);

		bot.sendMessage(chatId,
			MsgLocService.getLocal(MessageKey.WHATS_YOUR_NAME, loc));
	}

	@Override
	public void leave(long chatId, FSMContext ctx) {

	}
}
