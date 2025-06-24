package ru.untitled_devs.bot.shared;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import ru.untitled_devs.bot.shared.localisation.LocalisationService;
import ru.untitled_devs.bot.shared.localisation.MessageKey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalisationServiceTest {

	@Test
	void getMessageReturnsCorrectMessageWithoutArgs() {
		Locale testLocale = Locale.ENGLISH;
		String testKey = "test.key";
		String testPattern = "Hello!";

		MessageKey key = mock(MessageKey.class);
		when(key.key()).thenReturn(testKey);

		ResourceBundle fakeBundle = mock(ResourceBundle.class);
		when(fakeBundle.getString(testKey)).thenReturn(testPattern);

		try (MockedStatic<ResourceBundle> bundleMock = mockStatic(ResourceBundle.class)) {
			bundleMock.when(() -> ResourceBundle.getBundle(anyString(), eq(testLocale))).thenReturn(fakeBundle);

			String message = LocalisationService.getMessage(key, testLocale);
			assertEquals("Hello!", message);
		}
	}

	@Test
	void getMessageReturnsCorrectMessageWithArgs() {
		Locale testLocale = Locale.ENGLISH;
		String testKey = "test.key";
		String testPattern = "Hello, {0}!";

		MessageKey key = mock(MessageKey.class);
		when(key.key()).thenReturn(testKey);

		ResourceBundle fakeBundle = mock(ResourceBundle.class);
		when(fakeBundle.getString(testKey)).thenReturn(testPattern);

		try (MockedStatic<ResourceBundle> bundleMock = mockStatic(ResourceBundle.class)) {
			bundleMock.when(() -> ResourceBundle.getBundle(anyString(), eq(testLocale))).thenReturn(fakeBundle);

			String message = LocalisationService.getMessage(key, testLocale, "World");
			assertEquals("Hello, World!", message);
		}
	}

	@Test
	void getMessageReturnsQuestionMarksWhenGotMissingKey() {
		Locale testLocale = Locale.ENGLISH;
		String invalidKey = "test.hello";
		String testKey = "test.key";
		String TestPattern = "Hello!";

		MessageKey key = mock(MessageKey.class);
		when(key.key()).thenReturn(invalidKey);

		ResourceBundle fakeBundle = mock(ResourceBundle.class);
		when(fakeBundle.getString(testKey)).thenReturn(TestPattern);

		try (MockedStatic<ResourceBundle> bundleMock = mockStatic(ResourceBundle.class)) {
			bundleMock.when(() -> ResourceBundle.getBundle(anyString(), eq(testLocale))).thenReturn(fakeBundle);

			String message = LocalisationService.getMessage(key, testLocale);
			assertEquals("???", message);
		}
	}


	@Test
	void getMessageReturnsQuestionMarksWhenGotMissingKeyWithArgs() {
		Locale testLocale = Locale.ENGLISH;
		String invalidKey = "test.hello";
		String testKey = "test.key";
		String TestPattern = "Hello!";

		MessageKey key = mock(MessageKey.class);
		when(key.key()).thenReturn(invalidKey);

		ResourceBundle fakeBundle = mock(ResourceBundle.class);
		when(fakeBundle.getString(testKey)).thenReturn(TestPattern);

		try (MockedStatic<ResourceBundle> bundleMock = mockStatic(ResourceBundle.class)) {
			bundleMock.when(() -> ResourceBundle.getBundle(anyString(), eq(testLocale))).thenReturn(fakeBundle);

			String message = LocalisationService.getMessage(key, testLocale, "Test");
			assertEquals("???", message);
		}
	}
}
