package ru.untitled_devs.bot.features.registration.handlers;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.untitled_devs.bot.features.registration.RegistrationStates;
import ru.untitled_devs.bot.shared.geocoder.Coordinates;
import ru.untitled_devs.bot.shared.geocoder.Geocoder;
import ru.untitled_devs.bot.shared.localisation.ButtonKey;
import ru.untitled_devs.bot.shared.localisation.BtnLocService;
import ru.untitled_devs.bot.shared.localisation.MessageKey;
import ru.untitled_devs.bot.shared.localisation.MsgLocService;
import ru.untitled_devs.bot.shared.models.Profile;
import ru.untitled_devs.core.client.BotClient;
import ru.untitled_devs.core.fsm.FSMContext;
import ru.untitled_devs.core.routers.filters.Filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RegLocationHandler extends RegStepHandler{
	private static int MAX_LOCATION_LENGTH = 100;
	private final Geocoder geocoder;

	public RegLocationHandler(BotClient bot, Geocoder geocoder, Filter... filters) {
		super(bot, filters);

		this.geocoder = geocoder;
	}

	@Override
	void action(Message message, FSMContext ctx) {
		Locale loc = ctx.getData(langKey);

		String location;
		Coordinates coords;

		try {
			coords = extractCoordinates(message);
			location = resolveLocation(message, coords, message.hasLocation());
		} catch (IllegalArgumentException e) {
			bot.sendMessage(message.getChatId(),
				MsgLocService.getLocal(MessageKey.CANT_FIND_LOCATION, loc));
			return;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (location.length() > MAX_LOCATION_LENGTH) {
			bot.sendMessage(message.getChatId(),
				MsgLocService.getLocal(MessageKey.TOO_BIG_LOCATION, loc));
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
			BtnLocService.getLocal(ButtonKey.SKIP_WORD, loc))
		);

		List<KeyboardRow> rows = new ArrayList<>();
		rows.add(row);
		markup.setKeyboard(rows);

		bot.sendMessage(message.getChatId(),
			MsgLocService.getLocal(MessageKey.ASK_DESCRIPTION, loc), markup);
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
}
