package ru.untitled_devs.bot.features.registration;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import dev.morphia.Datastore;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.untitled_devs.bot.shared.geocoder.Coordinates;
import ru.untitled_devs.bot.shared.geocoder.Geocoder;
import ru.untitled_devs.bot.shared.image.ImageService;
import ru.untitled_devs.bot.shared.localisation.*;
import ru.untitled_devs.bot.shared.models.Image;
import ru.untitled_devs.bot.shared.models.Profile;
import ru.untitled_devs.bot.shared.models.User;
import ru.untitled_devs.core.client.BotClient;
import ru.untitled_devs.core.fsm.context.DataKey;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.handlers.MessageHandler;
import ru.untitled_devs.core.routers.scenes.Scene;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class RegistrationScene extends Scene {
	private final BotClient bot;
	private final RegistrationService regService;
	private final Geocoder geocoder;
	private final ImageService imageService;

	private final static DataKey<Locale> langKey = DataKey.of("lang", Locale.class);
	private final static DataKey<Profile> profileKey = DataKey.of("RegistrationData", Profile.class);

	private static int MAX_NAME_LENGTH = 100;
	private static int MIN_AGE = 5;
	private static int MAX_AGE = 120;
	private static int MAX_LOCATION_LENGTH = 100;
	private static int MAX_DESCRIPTION_LENGTH = 500;
	private static int MAX_FILE_SIZE = 5 * 1024 * 1024;

	public RegistrationScene(BotClient bot, Datastore datastore, Geocoder geocoder, ImageService imageService) {
		this.bot = bot;
		regService = new RegistrationService(datastore);
		this.geocoder = geocoder;
		this.imageService = imageService;

		this.registerHandlers();
	}

	private void registerHandlers() {
		addHandler(RegistrationStates.NAME, new MessageHandler(this::getName));
		addHandler(RegistrationStates.AGE, new MessageHandler(this::getAge));
		addHandler(RegistrationStates.LOCATION, new MessageHandler(this::getLocation));
		addHandler(RegistrationStates.PHOTO, new MessageHandler(this::getPhoto));
		addHandler(RegistrationStates.DESCRIPTION, new MessageHandler(this::getDescription));
		addHandler(RegistrationStates.FINISH, new MessageHandler(this::finishRegistration));
	}

	@Override
	public String getId() {
		return "register";
	}

	public void enter(long chatId, FSMContext ctx) {
		DataKey<Locale> langKey = DataKey.of("lang", Locale.class);
		Locale loc = ctx.getData(langKey);

		bot.sendMessage(chatId,
			MessagesLocalisationService.getLocal(MessageKey.NOT_REGISTERED, loc));

		Profile profileData = new Profile();
		DataKey<Profile> key = DataKey.of("RegistrationData", Profile.class);

		ctx.setData(key, profileData);
		ctx.setState(RegistrationStates.NAME);

		bot.sendMessage(chatId,
			MessagesLocalisationService.getLocal(MessageKey.WHATS_YOUR_NAME, loc));
	}

	@Override
	public void leave(long chatId, FSMContext ctx) {

	}

	private void getName(Message message, FSMContext ctx) {
		Locale loc = ctx.getData(langKey);
		String name = message.getText().strip();
		Profile profileData = ctx.getData(profileKey);

		if (name.length() > MAX_NAME_LENGTH) {
			bot.sendMessage(message.getChatId(),
				MessagesLocalisationService.getLocal(
					MessageKey.TOO_BIG_NAME,
					loc
				)
			);
			return;
		}

		profileData.setName(name);

		ctx.setData(profileKey, profileData);
		ctx.setState(RegistrationStates.AGE);

		bot.sendMessage(message.getChatId(),
			MessagesLocalisationService.getLocal(MessageKey.ASK_AGE, loc));
	}

	private void getAge(Message message, FSMContext ctx) {
		Locale loc = ctx.getData(langKey);

		int age;

		try {
			age = Integer.parseInt(message.getText());
		} catch (NumberFormatException e) {
			bot.sendMessage(message.getChatId(),
				MessagesLocalisationService.getLocal(MessageKey.AGE_ERROR, loc));
			throw new IllegalArgumentException("Message must contain only digits");
		}

		if (age < MIN_AGE) {
			bot.sendMessage(message.getChatId(),
				MessagesLocalisationService.getLocal(MessageKey.TOO_SMALL_AGE, loc));
			throw new IllegalArgumentException("Age must be in interval from 5 to 100");
		}

		if (age > MAX_AGE) {
			bot.sendMessage(message.getChatId(),
				MessagesLocalisationService.getLocal(MessageKey.TOO_BIG_AGE, loc));
			throw new IllegalArgumentException("Age must be in interval from 5 to 100");
		}

		Profile profileData = ctx.getData(profileKey);

		profileData.setAge(age);

		ctx.setData(profileKey, profileData);
		ctx.setState(RegistrationStates.LOCATION);

		bot.sendMessage(message.getChatId(),
			MessagesLocalisationService.getLocal(MessageKey.ASK_LOCATION, loc));
	}

	private void getLocation(Message message, FSMContext ctx) {
		Locale loc = ctx.getData(langKey);

		String location;
		Coordinates coords;

		try {
			coords = extractCoordinates(message);
			location = resolveLocation(message, coords, message.hasLocation());
		} catch (IllegalArgumentException e) {
			bot.sendMessage(message.getChatId(),
				MessagesLocalisationService.getLocal(MessageKey.CANT_FIND_LOCATION, loc));
			return;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (location.length() > MAX_LOCATION_LENGTH) {
			bot.sendMessage(message.getChatId(),
				MessagesLocalisationService.getLocal(
					MessageKey.TOO_BIG_LOCATION,
					loc
				)
			);
			throw new IllegalArgumentException("Location length must be not bigger than 150 characters.");
		}

		Position position = new Position(coords.getLongitude(), coords.getLatitude());
		Point point = new Point(position);

		Profile profileData = ctx.getData(profileKey);

		profileData.setLocation(location);
		profileData.setCoordinates(point);

		ctx.setData(profileKey, profileData);
		ctx.setState(RegistrationStates.DESCRIPTION);

		ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
		markup.setResizeKeyboard(true);
		markup.setOneTimeKeyboard(true);
		markup.setSelective(false);

		KeyboardRow row = new KeyboardRow();
		row.add(new KeyboardButton(
			ButtonsLocalisationService.getLocal(ButtonKey.SKIP_WORD, loc))
		);

		List<KeyboardRow> rows = new ArrayList<>();
		rows.add(row);
		markup.setKeyboard(rows);

		bot.sendMessage(message.getChatId(),
			MessagesLocalisationService.getLocal(MessageKey.ASK_DESCRIPTION, loc), markup);
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
		Locale loc = ctx.getData(langKey);
		String skipWord = ButtonsLocalisationService.getLocal(ButtonKey.SKIP_WORD, loc);

		String description;
		if (Objects.equals(message.getText(), skipWord)) {
			description = TextLocalisationService.getLocal(
				TextKey.NO_DESCRIPTION, loc
			);
		} else {
			description = message.getText();
		}

		if (description.length() > MAX_DESCRIPTION_LENGTH) {
			bot.sendMessage(message.getChatId(),
				MessagesLocalisationService.getLocal(
					MessageKey.TOO_BIG_DESCRIPTION,
					loc
				)
			);
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
			ButtonsLocalisationService.getLocal(ButtonKey.SKIP_WORD, loc))
		);

		List<KeyboardRow> rows = new ArrayList<>();
		rows.add(row);
		markup.setKeyboard(rows);

		bot.sendMessage(message.getChatId(),
			MessagesLocalisationService.getLocal(MessageKey.ASK_PHOTO, loc),
			markup
		);
	}

	private void getPhoto(Message message, FSMContext ctx) {
		Locale loc = ctx.getData(langKey);
		String skipWord = ButtonsLocalisationService.getLocal(ButtonKey.SKIP_WORD, loc);
		Profile profileData = ctx.getData(profileKey);

		if (message.hasPhoto()) {
			Image image;
			image = processPhoto(message, loc);
			profileData.setImage(image);
			ctx.setData(profileKey, profileData);
			ctx.setState(RegistrationStates.FINISH);
			sendPreview(message.getChatId(), profileData, loc);
			return;
		}

		if (Objects.equals(message.getText(), skipWord)) {
			profileData.setImage(null);
			ctx.setData(profileKey, profileData);
			ctx.setState(RegistrationStates.FINISH);
			sendPreview(message.getChatId(), profileData, loc);
			return;
		}

		bot.sendMessage(message.getChatId(),
			MessagesLocalisationService.getLocal(
				MessageKey.INCORRECT_INPUT,
				loc
			)
		);
	}

	private Image processPhoto(Message message, Locale loc) {
		PhotoSize largest = imageService.getLargestPhotoSize(message.getPhoto());
		String fileId = largest.getFileId();
		int fileSize = largest.getFileSize();

		if (fileSize > MAX_FILE_SIZE) {
			bot.sendMessage(message.getChatId(),
				MessagesLocalisationService.getLocal(MessageKey.TOO_BIG_PHOTO_SIZE, loc));
			throw new IllegalArgumentException("File size must be no bigger than 5 MB");
		}

		String filePath = bot.getFile(fileId).getFilePath();
		try (InputStream stream = bot.downloadFileAsStream(filePath)) {
			return imageService.saveImage(stream, fileId);
		} catch (IOException | TelegramApiException e) {
			bot.sendMessage(message.getChatId(),
				MessagesLocalisationService.getLocal(
					MessageKey.FILE_DOWNLOADING_ERROR,
					loc
				)
			);
			throw new RuntimeException(e);
		}
	}

	private void sendPreview(long chatId, Profile profile, Locale loc) {
		String text = MessagesLocalisationService.getLocal(
			MessageKey.PROFILE_MESSAGE,
			loc,
			profile.getName(), profile.getAge(), profile.getLocation(), profile.getDescription()
		);

		ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
		markup.setResizeKeyboard(true);
		markup.setOneTimeKeyboard(true);
		markup.setSelective(false);

		KeyboardRow row = new KeyboardRow();
		row.add(new KeyboardButton(
			ButtonsLocalisationService.getLocal(ButtonKey.CREATE_PROFILE, loc))
		);
		row.add(new KeyboardButton(
			ButtonsLocalisationService.getLocal(ButtonKey.REWRITE_PROFILE, loc))
		);

		List<KeyboardRow> rows = new ArrayList<>();
		rows.add(row);
		markup.setKeyboard(rows);

		if (profile.hasImage()) {
			sendProfilePreviewWithPhoto(chatId, loc, profile, text, markup);
			return;
		}

		sendProfilePreview(chatId, loc, profile, text, markup);
	}

	private void sendProfilePreviewWithPhoto(long chatId, Locale loc, Profile profile, String text, ReplyKeyboardMarkup markup) {
		try {
			byte[] image = imageService.loadImage(profile.getImage());
			bot.sendPhoto(chatId, text, image, markup);
		} catch (IOException e) {
			bot.sendMessage(
				chatId,
				MessagesLocalisationService.getLocal(
					MessageKey.FILE_DOWNLOADING_ERROR,
					loc
				)
			);
		}
	}

	private void sendProfilePreview(long chatId, Locale loc, Profile profile, String text, ReplyKeyboardMarkup markup) {
		bot.sendMessage(chatId, text, markup);
	}

	private void finishRegistration(Message message, FSMContext ctx) {
		Locale loc = ctx.getData(langKey);
		long chatId = message.getChatId();

		String createProfileButton = ButtonsLocalisationService.getLocal(
			ButtonKey.CREATE_PROFILE,
			loc
		);
		String rewriteProfileButton = ButtonsLocalisationService.getLocal(
			ButtonKey.REWRITE_PROFILE,
			loc
		);

		if (Objects.equals(message.getText(), rewriteProfileButton)) {
			ctx.setData(profileKey, new Profile());
			ctx.setState(RegistrationStates.NAME);

			bot.sendMessage(chatId,
				MessagesLocalisationService.getLocal(
					MessageKey.WHATS_YOUR_NAME,
					loc
				)
			);
			return;
		}

		if (Objects.equals(message.getText(), createProfileButton)) {
			Profile profileData = ctx.getData(profileKey);

			User userData = new User();
			userData.setTelegramId(chatId);
			userData.setLang(loc.toLanguageTag());

			regService.registerUser(userData, profileData);
			bot.sendMessage(chatId,
				MessagesLocalisationService.getLocal(
					MessageKey.SUCCESSFUL_REGISTRATION,
					loc
				)
			);
			return;
		}

		bot.sendMessage(chatId,
			MessagesLocalisationService.getLocal(
				MessageKey.INCORRECT_INPUT,
				loc
			)
		);

	}
}
