package ru.untitled_devs.bot.features.registration;

import dev.morphia.Datastore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.bot.shared.geocoder.Geocoder;
import ru.untitled_devs.bot.shared.models.Profile;
import ru.untitled_devs.core.client.PollingClient;
import ru.untitled_devs.core.fsm.context.DataKey;
import ru.untitled_devs.core.fsm.context.FSMContext;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegistrationRouterTest {
	RegistrationRouter router;
	FSMContext ctx;

	@BeforeEach
	public void getRegistrationRouter(){
		Datastore datastore = mock(Datastore.class);
		PollingClient bot = mock(PollingClient.class);
		Geocoder geocoder = mock(Geocoder.class);
		router =  new RegistrationRouter(bot, datastore, geocoder);
		ctx = mock(FSMContext.class);
		DataKey<Locale> langKey = DataKey.of("lang", Locale.class);
		when(ctx.getData(langKey)).thenReturn(Locale.forLanguageTag("en-US"));
	}

	@Test
	void getAgeGotDigitString() {
		Message message = mock(Message.class);
		when(message.getText()).thenReturn("15");

		Update update = mock(Update.class);
		when(update.hasMessage()).thenReturn(true);
		when(update.getMessage()).thenReturn(message);


		when(ctx.getState()).thenReturn(RegistrationStates.AGE);

		Profile profile = new Profile();
		DataKey<Profile> key = DataKey.of("RegistrationData", Profile.class);
		when(ctx.getData(key)).thenReturn(profile);

		assertDoesNotThrow(() -> router.routeUpdate(update, ctx),
			"Get age must do not throw any exceptions when got valid age type");

		assertEquals(15, profile.getAge(), "Age must be changed");
	}

	@Test
	void getAgeGotNotDigitString() {

		Message message = mock(Message.class);
		when(message.getText()).thenReturn("abcd");

		Update update = mock(Update.class);
		when(update.hasMessage()).thenReturn(true);
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.AGE);

		assertThrows(IllegalArgumentException.class,
			() -> router.routeUpdate(update, ctx),
			"Get age must throw exception when got invalid age type");
	}

	@Test
	void getAgeGoTooHugeAge() {

		Message message = mock(Message.class);
		when(message.getText()).thenReturn("120");

		Update update = mock(Update.class);
		when(update.hasMessage()).thenReturn(true);
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.AGE);

		assertThrows(IllegalArgumentException.class,
			() -> router.routeUpdate(update, ctx),
			"Get age must do not throw exception when got invalid age value");
	}

	@Test
	void getAgeGoTooSmallAge() {

		Message message = mock(Message.class);
		when(message.getText()).thenReturn("2");

		Update update = mock(Update.class);
		when(update.hasMessage()).thenReturn(true);
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.AGE);

		assertThrows(IllegalArgumentException.class,
			() -> router.routeUpdate(update, ctx),
			"Get age must do not throw exception when got invalid age value");
	}
}
