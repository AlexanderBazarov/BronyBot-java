package ru.untitled_devs.bot.features.registration;

import dev.morphia.Datastore;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.bot.shared.geocoder.Geocoder;
import ru.untitled_devs.bot.shared.models.Profile;
import ru.untitled_devs.core.client.PollingClient;
import ru.untitled_devs.core.fsm.context.DataKey;
import ru.untitled_devs.core.fsm.context.FSMContext;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

class RegistrationRouterTest {
	private RegistrationRouter getRegistrationRouter(){
		Datastore datastore = mock(Datastore.class);
		PollingClient bot = mock(PollingClient.class);
		Geocoder geocoder = mock(Geocoder.class);
		return new RegistrationRouter(bot, datastore, geocoder);
	}

	@Test
	void getAgeGotDigitString() {
		RegistrationRouter router = getRegistrationRouter();

		Message message = mock(Message.class);
		when(message.getText()).thenReturn("15");

		Update update = mock(Update.class);
		when(update.hasMessage()).thenReturn(true);
		when(update.getMessage()).thenReturn(message);

		FSMContext ctx = mock(FSMContext.class);
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
		RegistrationRouter router = getRegistrationRouter();

		Message message = mock(Message.class);
		when(message.getText()).thenReturn("abcd");

		Update update = mock(Update.class);
		when(update.hasMessage()).thenReturn(true);
		when(update.getMessage()).thenReturn(message);

		FSMContext ctx = mock(FSMContext.class);
		when(ctx.getState()).thenReturn(RegistrationStates.AGE);

		assertThrows(IllegalArgumentException.class,
			() -> router.routeUpdate(update, ctx),
			"Get age must throw exception when got invalid age type");
	}

	@Test
	void getAgeGoTooHugeAge() {
		RegistrationRouter router = getRegistrationRouter();

		Message message = mock(Message.class);
		when(message.getText()).thenReturn("120");

		Update update = mock(Update.class);
		when(update.hasMessage()).thenReturn(true);
		when(update.getMessage()).thenReturn(message);

		FSMContext ctx = mock(FSMContext.class);
		when(ctx.getState()).thenReturn(RegistrationStates.AGE);

		assertThrows(IllegalArgumentException.class,
			() -> router.routeUpdate(update, ctx),
			"Get age must do not throw exception when got invalid age value");
	}

	@Test
	void getAgeGoTooSmallAge() {
		RegistrationRouter router = getRegistrationRouter();

		Message message = mock(Message.class);
		when(message.getText()).thenReturn("2");

		Update update = mock(Update.class);
		when(update.hasMessage()).thenReturn(true);
		when(update.getMessage()).thenReturn(message);

		FSMContext ctx = mock(FSMContext.class);
		when(ctx.getState()).thenReturn(RegistrationStates.AGE);

		assertThrows(IllegalArgumentException.class,
			() -> router.routeUpdate(update, ctx),
			"Get age must do not throw exception when got invalid age value");
	}
}
