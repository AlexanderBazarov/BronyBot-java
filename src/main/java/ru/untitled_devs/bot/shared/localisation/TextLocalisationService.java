package ru.untitled_devs.bot.shared.localisation;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class TextLocalisationService {
	private static final String BUNDLE_BASE = "texts.texts";

	public static ResourceBundle buttonBundle(Locale locale) {
		return ResourceBundle.getBundle(BUNDLE_BASE, locale);
	}

	public static String getLocal(TextKey key, Locale locale, Object... args) {
		String pattern = buttonBundle(locale).getString(key.key());
		return pattern != null ? MessageFormat.format(pattern, args) : "???";
	}

	public static String getLocal(TextKey key, Locale locale) {
		return getLocal(key, locale, (Object[]) null);
	}
}
