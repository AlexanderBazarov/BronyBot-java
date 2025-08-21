package ru.untitled_devs.bot.shared.geocoder.yandex;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import ru.untitled_devs.bot.shared.geocoder.Coordinates;
import ru.untitled_devs.bot.shared.geocoder.Geocoder;
import ru.untitled_devs.bot.shared.utils.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;

public final class YandexGeocoder extends Geocoder {
	private static final ObjectMapper mapper = new ObjectMapper()
		.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

	@Inject
	public YandexGeocoder(String apiUrl, String apiKey) {
		super(apiUrl, apiKey);
	}

	@Override
	public Coordinates getPlaceCoords(String name) throws IOException, IllegalArgumentException {
		URIBuilder builder = new URIBuilder(apiUrl);
		builder.addParam("apikey", apiKey);
		builder.addParam("geocode", name);
		builder.addParam("format", "json");

		URI uri = builder.toUri();

		YandexResponse yandexResponse = makeRequest(uri);

		if (yandexResponse.getResponse().getGeoObjectCollection().getFeatureMember().isEmpty()) {
			throw new IllegalArgumentException();
		}

		YandexResponse.GeoObject geo = yandexResponse
			.getResponse()
			.getGeoObjectCollection()
			.getFeatureMember()
			.getFirst().getGeoObject();

		String pos = geo.getPoint().getPos();
		String[] parts = pos.split(" ");

		double longitude = Double.parseDouble(parts[0]);
		double latitude  = Double.parseDouble(parts[1]);

		return new Coordinates(latitude, longitude);
	}

	@Override
	public String getPlaceName(Coordinates coords) throws IOException, IllegalArgumentException {
		URIBuilder builder = new URIBuilder(apiUrl);
		builder.addParam("apikey", apiKey);
		builder.addParam("geocode", coords.toString());
		builder.addParam("format", "json");

		URI uri = builder.toUri();

		YandexResponse yandexResponse = makeRequest(uri);

		List<YandexResponse.FeatureMember> featureMember =
			yandexResponse.getResponse().getGeoObjectCollection().getFeatureMember();

		if (featureMember.isEmpty()) {
			throw new IllegalArgumentException();
		}

		YandexResponse.GeoObject geoObject =
			featureMember
			.getFirst()
			.getGeoObject();

		YandexResponse.Address address = geoObject
			.getMetaDataProperty()
			.getGeocoderMetaData()
			.getAddress();

		String name = address
			.getComponents()
			.stream()
			.filter((component -> Objects.equals(component.getKind(), "locality")))
			.toList()
			.getFirst()
			.getName();


		return name;
	}

	private YandexResponse makeRequest(URI uri) throws IOException {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(uri).build();

		HttpResponse<String> response;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (InterruptedException e) {
			throw new IOException("Interrupted while calling Yandex Geocoder", e);
		}

		if (response.statusCode() != 200) {
			throw new IOException("Yandex Geocoder API error: " + response.statusCode());
		}

		String body = response.body();
		return mapper.readValue(body, YandexResponse.class);
	}

}
