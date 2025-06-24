package ru.untitled_devs.bot.config;

public final class GeocodingConfig {
	private String apiUrl;
	private String apiKey;

	public GeocodingConfig() {
		loadVariables();
	}

	private void loadVariables() {
		apiUrl = System.getenv("YANDEX_API_URL");
		apiKey = System.getenv("YANDEX_API_KEY");
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public String getApiKey() {
		return apiKey;
	}
}
