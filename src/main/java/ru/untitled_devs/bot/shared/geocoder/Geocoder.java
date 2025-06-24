package ru.untitled_devs.bot.shared.geocoder;

import java.io.IOException;
import java.net.http.HttpClient;

public abstract class Geocoder {
	protected final String apiUrl;
	protected final String apiKey;
	protected final HttpClient client;

	public Geocoder(String apiUrl, String apiKey) {
		this.apiUrl = apiUrl;
		this.apiKey = apiKey;

		client = HttpClient.newHttpClient();
	}

	public abstract Coordinates getPlaceCoords(String name) throws IllegalArgumentException, IOException;

	public abstract String getPlaceName(Coordinates coords) throws IOException, IllegalArgumentException;
}
