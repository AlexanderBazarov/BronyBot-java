package ru.untitled_devs.bot.shared.localisation;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class MsgLocService {
	private static final String BUNDLE_BASE = "messages.messages";

	public static ResourceBundle messageBundle(Locale locale) {
		return ResourceBundle.getBundle(BUNDLE_BASE, locale);
	}

	public static String getLocal(MessageKey key, Locale locale, Object... args) {
		String pattern = messageBundle(locale).getString(key.key());
		return pattern != null ? MessageFormat.format(pattern, args) : "???";
	}

	public static String getLocal(MessageKey key, Locale locale) {
		return getLocal(key, locale, (Object[]) null);
	}

}
