package ru.untitled_devs.bot.features.registration;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import dev.morphia.Datastore;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.untitled_devs.bot.shared.geocoder.Coordinates;
import ru.untitled_devs.bot.shared.geocoder.Geocoder;
import ru.untitled_devs.bot.shared.localisation.LocalisationService;
import ru.untitled_devs.bot.shared.localisation.MessageKey;
import ru.untitled_devs.bot.shared.models.Profile;
import ru.untitled_devs.bot.shared.models.User;
import ru.untitled_devs.core.client.BotClient;
import ru.untitled_devs.core.fsm.context.DataKey;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.Router;
import ru.untitled_devs.core.routers.filters.MessageFilter;
import ru.untitled_devs.core.routers.handlers.MessageHandler;

import java.io.IOException;
import java.util.Locale;

public final class RegistrationRouter extends Router {
    private final BotClient bot;
	private final RegistrationService regService;
	private final Geocoder geocoder;

    public RegistrationRouter(BotClient bot, Datastore datastore, Geocoder geocoder) {
        this.bot = bot;
		regService = new RegistrationService(datastore);
		this.geocoder = geocoder;

		this.registerHandlers();
    }

    private void registerHandlers() {
        this.addHandler(RegistrationStates.START, new MessageHandler(this::startRegistration));
		this.addHandler(RegistrationStates.NAME, new MessageHandler(this::getName));
		this.addHandler(RegistrationStates.AGE, new MessageHandler(this::getAge));
		this.addHandler(RegistrationStates.LOCATION, new MessageHandler(this::getLocation));
		this.addHandler(RegistrationStates.DESCRIPTION, new MessageHandler(this::getDescription));
		this.addHandler(RegistrationStates.FINISH, new MessageHandler(this::finishRegistration));
    }

	Locale loc = Locale.forLanguageTag("ru-RU");

    private void startRegistration(Message message, FSMContext ctx) {
		this.bot.sendMessage(message.getChatId(),
			LocalisationService.getMessage(MessageKey.NOT_REGISTERED, loc));

		Profile profileData = new Profile();
		DataKey<Profile> key = DataKey.of("RegistrationData", Profile.class);

		ctx.setData(key, profileData);
		ctx.setState(RegistrationStates.NAME);

		this.bot.sendMessage(message.getChatId(),
			LocalisationService.getMessage(MessageKey.WHATS_YOUR_NAME, loc));
    }

	private void getName(Message message, FSMContext ctx) {
		String name = message.getText().strip();

		DataKey<Profile> key = DataKey.of("RegistrationData", Profile.class);
		Profile profileData = ctx.getData(key);

		profileData.setName(name);

		ctx.setData(key, profileData);
		ctx.setState(RegistrationStates.AGE);

		this.bot.sendMessage(message.getChatId(),
			LocalisationService.getMessage(MessageKey.ASK_AGE, loc));
	}

	private void getAge(Message message, FSMContext ctx) {
		int age;

		try {
			age = Integer.parseInt(message.getText());
		} catch (NumberFormatException e) {
			bot.sendMessage(message.getChatId(),
				LocalisationService.getMessage(MessageKey.AGE_ERROR, loc));
			throw new IllegalArgumentException("Message must contain only digits");
		}

		int minAge = 5;
		int maxAge = 100;

		if (age < minAge || age > maxAge) {
			bot.sendMessage(message.getChatId(),
				LocalisationService.getMessage(MessageKey.AGE_ERROR, loc));
			throw new IllegalArgumentException("Age must be in interval from 5 to 100");
		}


		DataKey<Profile> key = DataKey.of("RegistrationData", Profile.class);
		Profile profileData = ctx.getData(key);

		profileData.setAge(age);

		ctx.setData(key, profileData);
		ctx.setState(RegistrationStates.LOCATION);

		this.bot.sendMessage(message.getChatId(),
			LocalisationService.getMessage(MessageKey.ASK_LOCATION, loc));
	}

	private void getLocation(Message message, FSMContext ctx) {
		String location;
		Coordinates coords;

		try {
			coords = extractCoordinates(message);
			location = resolveLocation(message, coords, message.hasLocation());
		} catch (IllegalArgumentException e) {
			bot.sendMessage(message.getChatId(),
				LocalisationService.getMessage(MessageKey.CANT_FIND_LOCATION, loc));
			return;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Position position = new Position(coords.getLongitude(), coords.getLatitude());
		Point point = new Point(position);

		DataKey<Profile> key = DataKey.of("RegistrationData", Profile.class);
		Profile profileData = ctx.getData(key);

		profileData.setLocation(location);
		profileData.setCoordinates(point);

		ctx.setData(key, profileData);
		ctx.setState(RegistrationStates.DESCRIPTION);

		bot.sendMessage(message.getChatId(),
			LocalisationService.getMessage(MessageKey.ASK_DESCRIPTION, loc));
	}

	private Coordinates extractCoordinates(Message message) throws IOException {
		if (message.hasLocation()) {
			Location loc = message.getLocation();
			return new Coordinates(loc.getLatitude(), loc.getLongitude());
		}

		try {
			String text = message.getText().strip();
			return geocoder.getPlaceCoords(text);
		} catch (IllegalArgumentException e) {
			return new Coordinates(0, 0);
		}
	}

	private String resolveLocation(Message message, Coordinates coords, boolean wasLocation) throws IOException {
		if (wasLocation) {
			return geocoder.getPlaceName(coords);
		}
		return message.getText().strip();
	}

	private void getDescription(Message message, FSMContext ctx) {
		String description = message.getText();

		DataKey<Profile> key = DataKey.of("RegistrationData", Profile.class);
		Profile profileData = ctx.getData(key);

		profileData.setDescription(description);

		ctx.setData(key, profileData);
		ctx.setState(RegistrationStates.FINISH);
	}

	private void finishRegistration(Message message, FSMContext ctx) {
		long telegramId = message.getChatId();

		DataKey<Profile> key = DataKey.of("RegistrationData", Profile.class);
		Profile profileData = ctx.getData(key);

		User userData = new User();
		userData.setTelegramId(telegramId);
		userData.setLang(loc.toLanguageTag());

		regService.registerUser(userData, profileData);
	}

}
