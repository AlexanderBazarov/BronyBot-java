package ru.untitled_devs.bot.shared.localisation;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public  class LocalisationService {
	private static final String BUNDLE_BASE = "messages.messages";

	public static ResourceBundle bundle(Locale locale) {
		return ResourceBundle.getBundle(BUNDLE_BASE, locale);
	}

	public static String getMessage(MessageKey key, Locale locale, Object... args) {
		String pattern = bundle(locale).getString(key.key());
		return pattern != null ? MessageFormat.format(pattern, args) : "???";
	}

	public static String getMessage(MessageKey key, Locale locale) {
		return getMessage(key, locale, (Object[]) null);
	}
}
