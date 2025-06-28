package ru.untitled_devs.bot.shared.localisation;

public enum TextKey {
	NO_DESCRIPTION("no_description");

	private final String key;

	TextKey(String key) {
		this.key = key;
	}

	public String key() {
		return key;
	}
}
