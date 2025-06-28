package ru.untitled_devs.bot.shared.localisation;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ButtonsLocalisationServiceTest {
	@Test
	void getLocalReturnsCorrectMessageWithoutArgs() {
		Locale testLocale = Locale.ENGLISH;
		String testKey = "test.key";
		String testPattern = "Hello!";

		ButtonKey key = mock(ButtonKey.class);
		when(key.key()).thenReturn(testKey);

		ResourceBundle fakeBundle = mock(ResourceBundle.class);
		when(fakeBundle.getString(testKey)).thenReturn(testPattern);

		try (MockedStatic<ResourceBundle> bundleMock = mockStatic(ResourceBundle.class)) {
			bundleMock.when(() -> ResourceBundle.getBundle(anyString(), eq(testLocale))).thenReturn(fakeBundle);

			String message = ButtonsLocalisationService.getLocal(key, testLocale);
			assertEquals("Hello!", message);
		}
	}

	@Test
	void getLocalReturnsCorrectMessageWithArgs() {
		Locale testLocale = Locale.ENGLISH;
		String testKey = "test.key";
		String testPattern = "Hello, {0}!";

		ButtonKey key = mock(ButtonKey.class);
		when(key.key()).thenReturn(testKey);

		ResourceBundle fakeBundle = mock(ResourceBundle.class);
		when(fakeBundle.getString(testKey)).thenReturn(testPattern);

		try (MockedStatic<ResourceBundle> bundleMock = mockStatic(ResourceBundle.class)) {
			bundleMock.when(() -> ResourceBundle.getBundle(anyString(), eq(testLocale))).thenReturn(fakeBundle);

			String message = ButtonsLocalisationService.getLocal(key, testLocale, "World");
			assertEquals("Hello, World!", message);
		}
	}

	@Test
	void getLocalReturnsQuestionMarksWhenGotMissingKey() {
		Locale testLocale = Locale.ENGLISH;
		String invalidKey = "test.hello";
		String testKey = "test.key";
		String TestPattern = "Hello!";

		ButtonKey key = mock(ButtonKey.class);
		when(key.key()).thenReturn(invalidKey);

		ResourceBundle fakeBundle = mock(ResourceBundle.class);
		when(fakeBundle.getString(testKey)).thenReturn(TestPattern);

		try (MockedStatic<ResourceBundle> bundleMock = mockStatic(ResourceBundle.class)) {
			bundleMock.when(() -> ResourceBundle.getBundle(anyString(), eq(testLocale))).thenReturn(fakeBundle);

			String message = ButtonsLocalisationService.getLocal(key, testLocale);
			assertEquals("???", message);
		}
	}

	@Test
	void getLocalReturnsQuestionMarksWhenGotMissingKeyWithArgs() {
		Locale testLocale = Locale.ENGLISH;
		String invalidKey = "test.hello";
		String testKey = "test.key";
		String TestPattern = "Hello!";

		ButtonKey key = mock(ButtonKey.class);
		when(key.key()).thenReturn(invalidKey);

		ResourceBundle fakeBundle = mock(ResourceBundle.class);
		when(fakeBundle.getString(testKey)).thenReturn(TestPattern);

		try (MockedStatic<ResourceBundle> bundleMock = mockStatic(ResourceBundle.class)) {
			bundleMock.when(() -> ResourceBundle.getBundle(anyString(), eq(testLocale))).thenReturn(fakeBundle);

			String message = ButtonsLocalisationService.getLocal(key, testLocale, "Test");
			assertEquals("???", message);
		}
	}

}
