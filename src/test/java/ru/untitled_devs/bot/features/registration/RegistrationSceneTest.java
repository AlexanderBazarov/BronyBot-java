package ru.untitled_devs.bot.features.registration;

import dev.morphia.Datastore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.untitled_devs.bot.shared.geocoder.Coordinates;
import ru.untitled_devs.bot.shared.geocoder.Geocoder;
import ru.untitled_devs.bot.shared.image.ImageService;
import ru.untitled_devs.bot.shared.localisation.BtnLocService;
import ru.untitled_devs.bot.shared.localisation.ButtonKey;
import ru.untitled_devs.bot.shared.models.Gender;
import ru.untitled_devs.bot.shared.models.Image;
import ru.untitled_devs.bot.shared.models.Profile;
import ru.untitled_devs.core.client.PollingClient;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.context.DataKey;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.scenes.SceneManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegistrationSceneTest {
	RegistrationScene router;
	FSMContext ctx;
	Geocoder geocoder;
	ImageService imageService;
	PhotoSize photoSize;
	PollingClient bot;
	Update update;
	UpdateContext updateContext;
	RegistrationService registrationService;
	SceneManager sceneManager;

	@BeforeEach
	public void getRegistrationRouter() throws IOException {
		Datastore datastore = mock(Datastore.class);

		geocoder = mock(Geocoder.class);
		when(geocoder.getPlaceCoords("Екатеринбург")).thenReturn(new Coordinates(123, 123));
		when(geocoder.getPlaceName(new Coordinates(123.0, 123.0))).thenReturn("Екатеринбург");
		when(geocoder.getPlaceName(new Coordinates(125.0, 125.0))).thenThrow(new IllegalArgumentException());
		imageService = mock(ImageService.class);

		sceneManager = mock(SceneManager.class);

		photoSize = mock(PhotoSize.class);

		bot = mock(PollingClient.class);

		update = mock(Update.class);
		updateContext = mock(UpdateContext.class);
		when(updateContext.getUpdate()).thenReturn(update);

		registrationService = new RegistrationService(datastore);

		router =  new RegistrationScene(bot, registrationService, geocoder, imageService, sceneManager);
		ctx = mock(FSMContext.class);
		DataKey<Locale> langKey = DataKey.of("lang", Locale.class);
		when(ctx.getData(langKey)).thenReturn(Locale.forLanguageTag("en-US"));
	}

	@Test
	void getNameGotValidName() {
		Message message = mock(Message.class);
		when(message.getText()).thenReturn("Саня");

		when(updateContext.hasMessage()).thenReturn(true);
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.NAME);

		Profile profile = new Profile();
		DataKey<Profile> key = DataKey.of("RegistrationData", Profile.class);
		when(ctx.getData(key)).thenReturn(profile);

		assertDoesNotThrow(() -> router.routeUpdate(updateContext, ctx));
		assertEquals("Саня", profile.getName());
	}

	@Test
	void getNameGotInvalidName() {
		Message message = mock(Message.class);
		when(message.getText()).thenReturn("A".repeat(101));


		when(updateContext.hasMessage()).thenReturn(true);
		
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.NAME);

		Profile profile = new Profile();
		DataKey<Profile> key = DataKey.of("RegistrationData", Profile.class);
		when(ctx.getData(key)).thenReturn(profile);

		assertDoesNotThrow(() -> router.routeUpdate(updateContext, ctx));
		assertNull(profile.getName());
	}

	@Test
	void getAgeGotDigitString() {
		Message message = mock(Message.class);
		when(message.getText()).thenReturn("15");


		when(updateContext.hasMessage()).thenReturn(true);
		
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.AGE);

		Profile profile = new Profile();
		DataKey<Profile> key = DataKey.of("RegistrationData", Profile.class);
		when(ctx.getData(key)).thenReturn(profile);

		assertDoesNotThrow(() -> router.routeUpdate(updateContext, ctx),
			"Get age must do not throw any exceptions when got valid age type");

		assertEquals(15, profile.getAge(), "Age must be changed");
	}

	@Test
	void getAgeGotNotDigitString() {
		Message message = mock(Message.class);
		when(message.getText()).thenReturn("abcd");


		when(updateContext.hasMessage()).thenReturn(true);
		
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.AGE);

		assertThrows(IllegalArgumentException.class,
			() -> router.routeUpdate(updateContext, ctx),
			"Get age must throw exception when got invalid age type");
	}

	@Test
	void getAgeGoTooHugeAge() {
		Message message = mock(Message.class);
		when(message.getText()).thenReturn("121");


		when(updateContext.hasMessage()).thenReturn(true);
		
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.AGE);

		assertThrows(IllegalArgumentException.class,
			() -> router.routeUpdate(updateContext, ctx),
			"Get age must do not throw exception when got invalid age value");
	}

	@Test
	void getAgeGoTooSmallAge() {
		Message message = mock(Message.class);
		when(message.getText()).thenReturn("2");


		when(updateContext.hasMessage()).thenReturn(true);
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.AGE);

		assertThrows(IllegalArgumentException.class,
			() -> router.routeUpdate(updateContext, ctx),
			"Get age must do not throw exception when got invalid age value");
	}

	@Test
	void getGenderGotValidGender() {
		Message message = mock(Message.class);
		String genderButton = BtnLocService.getLocal(ButtonKey.GENDER_FEMALE, Locale.forLanguageTag("en-US"));
		when(message.getText()).thenReturn(genderButton);

		when(updateContext.hasMessage()).thenReturn(true);
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.GENDER);

		Profile profile = new Profile();
		DataKey<Profile> key = DataKey.of("RegistrationData", Profile.class);
		when(ctx.getData(key)).thenReturn(profile);

		assertDoesNotThrow(() -> router.routeUpdate(updateContext, ctx));
		assertEquals(Gender.FEMALE, profile.getGender());
	}

	@Test
	void getGenderGotInvalidGender() {
		Message message = mock(Message.class);
		String genderButton = "awbfgaowgbpaioug";
		when(message.getText()).thenReturn(genderButton);

		when(updateContext.hasMessage()).thenReturn(true);
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.GENDER);

		Profile profile = new Profile();
		DataKey<Profile> key = DataKey.of("RegistrationData", Profile.class);
		when(ctx.getData(key)).thenReturn(profile);

		assertThrows(IllegalArgumentException.class,
			() -> router.routeUpdate(updateContext, ctx));
	}

	@Test
	void getLocationGotValidLocation() {
		DataKey<Profile> profileKey = DataKey.of("RegistrationData", Profile.class);

		Message message = mock(Message.class);
		when(message.getText()).thenReturn("Екатеринбург");

		Profile profile = new Profile();
		when(ctx.getData(profileKey)).thenReturn(profile);


		when(updateContext.hasMessage()).thenReturn(true);
		
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.LOCATION);

		assertDoesNotThrow(
			() -> router.routeUpdate(updateContext, ctx)
		);

		assertEquals("Екатеринбург", profile.getLocation());
		assertEquals(Arrays.asList(123.0, 123.0), profile.getCoordinates().getCoordinates().getValues());
	}

	@Test
	void getLocationGotToLongLocation() {
		DataKey<Profile> profileKey = DataKey.of("RegistrationData", Profile.class);

		Message message = mock(Message.class);
		when(message.getText()).thenReturn("A".repeat(151));

		Profile profile = new Profile();
		when(ctx.getData(profileKey)).thenReturn(profile);


		when(updateContext.hasMessage()).thenReturn(true);
		
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.LOCATION);

		assertThrows(IllegalArgumentException.class,
			() -> router.routeUpdate(updateContext, ctx)
		);

	}

	@Test
	void getLocationGotValidCoordinates() {
		DataKey<Profile> profileKey = DataKey.of("RegistrationData", Profile.class);

		Message message = mock(Message.class);
		when(message.hasLocation()).thenReturn(true);
		when(message.getLocation()).thenReturn(new Location(123.0, 123.0, 0.0, 0, 0, 0));

		Profile profile = new Profile();
		when(ctx.getData(profileKey)).thenReturn(profile);


		when(updateContext.hasMessage()).thenReturn(true);
		
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.LOCATION);

		assertDoesNotThrow(
			() -> router.routeUpdate(updateContext, ctx)
		);

		assertEquals("Екатеринбург", profile.getLocation());
		assertEquals(Arrays.asList(123.0, 123.0), profile.getCoordinates().getCoordinates().getValues());
	}

	@Test
	void getLocationGotInvalidCoordinates() {
		DataKey<Profile> profileKey = DataKey.of("RegistrationData", Profile.class);

		Message message = mock(Message.class);
		when(message.hasLocation()).thenReturn(true);
		when(message.getLocation()).thenReturn(new Location(125.0, 125.0, 0.0, 0, 0, 0));

		Profile profile = new Profile();
		when(ctx.getData(profileKey)).thenReturn(profile);


		when(updateContext.hasMessage()).thenReturn(true);
		
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.LOCATION);

		assertDoesNotThrow(
			() -> router.routeUpdate(updateContext, ctx)
		);

		assertNull(profile.getLocation());
		assertNull(profile.getCoordinates());
	}

	@Test
	void getDescriptionGotValidText() {
		DataKey<Profile> profileKey = DataKey.of("RegistrationData", Profile.class);

		Message message = mock(Message.class);
		when(message.hasText()).thenReturn(true);
		when(message.getText()).thenReturn("Test description");

		Profile profile = new Profile();
		when(ctx.getData(profileKey)).thenReturn(profile);


		when(updateContext.hasMessage()).thenReturn(true);
		
		when(update.getMessage()).thenReturn(message);
		when(ctx.getState()).thenReturn(RegistrationStates.DESCRIPTION);

		assertDoesNotThrow(
			() -> router.routeUpdate(updateContext, ctx)
		);

		assertEquals("Test description", profile.getDescription());
	}

	@Test
	void getDescriptionGotInvalidText() {
		DataKey<Profile> profileKey = DataKey.of("RegistrationData", Profile.class);

		Message message = mock(Message.class);
		when(message.hasText()).thenReturn(true);
		when(message.getText()).thenReturn("A".repeat(501));

		Profile profile = new Profile();
		when(ctx.getData(profileKey)).thenReturn(profile);


		when(updateContext.hasMessage()).thenReturn(true);
		
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.DESCRIPTION);

		assertThrows(IllegalArgumentException.class,
			() -> router.routeUpdate(updateContext, ctx)
		);
	}

	@Test
	void getDescriptionGotSkipText() {
		DataKey<Profile> profileKey = DataKey.of("RegistrationData", Profile.class);

		Message message = mock(Message.class);
		when(message.hasText()).thenReturn(true);
		when(message.getText()).thenReturn(BtnLocService.getLocal(ButtonKey.SKIP_WORD, Locale.forLanguageTag("en-US")));

		Profile profile = new Profile();
		when(ctx.getData(profileKey)).thenReturn(profile);


		when(updateContext.hasMessage()).thenReturn(true);
		
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.DESCRIPTION);

		assertDoesNotThrow(
			() -> router.routeUpdate(updateContext, ctx)
		);

		assertEquals("No description.", profile.getDescription());
	}

	@Test
	void getPhotoGotValidPhoto() throws TelegramApiException, IOException {
		DataKey<Profile> profileKey = DataKey.of("RegistrationData", Profile.class);

		Message message = mock(Message.class);

		when(message.hasPhoto()).thenReturn(true);
		when(photoSize.getFileId()).thenReturn("file123");
		when(photoSize.getFileSize()).thenReturn(1024);

		when(message.getPhoto()).thenReturn(List.of(photoSize));
		when(imageService.getLargestPhotoSize(any())).thenReturn(photoSize);

		Image fakeImage = new Image();
		File file =
			new File();

		file.setFileId("file123");

		when(bot.getFile("file123")).thenReturn(file);
		when(bot.downloadFileAsStream((String) any())).thenReturn(new ByteArrayInputStream(new byte[]{1, 2, 3}));
		when(imageService.saveImage(any(), eq("file123"))).thenReturn(fakeImage);

		Profile profile = new Profile();
		when(ctx.getData(profileKey)).thenReturn(profile);


		when(updateContext.hasMessage()).thenReturn(true);
		
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.PHOTO);

		assertDoesNotThrow(
			() -> router.routeUpdate(updateContext, ctx)
		);
	}

	@Test
	void getPhotoGotTooBigPhoto() throws TelegramApiException, IOException {
		DataKey<Profile> profileKey = DataKey.of("RegistrationData", Profile.class);

		Message message = mock(Message.class);

		when(message.hasPhoto()).thenReturn(true);
		when(photoSize.getFileId()).thenReturn("file123");
		when(photoSize.getFileSize()).thenReturn(1024 * 1024 * 6);

		when(message.getPhoto()).thenReturn(List.of(photoSize));
		when(imageService.getLargestPhotoSize(any())).thenReturn(photoSize);

		Image fakeImage = new Image();
		File file =
			new File();

		file.setFileId("file123");

		when(bot.getFile("file123")).thenReturn(file);
		when(bot.downloadFileAsStream((String) any())).thenReturn(new ByteArrayInputStream(new byte[]{1, 2, 3}));
		when(imageService.saveImage(any(), eq("file123"))).thenReturn(fakeImage);

		Profile profile = new Profile();
		when(ctx.getData(profileKey)).thenReturn(profile);


		when(updateContext.hasMessage()).thenReturn(true);
		when(update.getMessage()).thenReturn(message);

		when(ctx.getState()).thenReturn(RegistrationStates.PHOTO);

		assertThrows(IllegalArgumentException.class,
			() -> router.routeUpdate(updateContext, ctx)
		);
	}

}
