package ru.untitled_devs.bot.shared.geocoder;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.untitled_devs.bot.shared.geocoder.yandex.YandexGeocoder;

import static org.junit.jupiter.api.Assertions.*;

class YandexGeocoderTest {
	static String url = "https://geocode-maps.yandex.ru/v1/";
	static String key;

	@BeforeAll
	static void checkKey() {
		Assumptions.assumeTrue(System.getenv("YANDEX_API_KEY") != null,
			"Skipping – no YANDEX_API_KEY");
		key = System.getenv("YANDEX_API_KEY");
	}

	@Test
	void getPlaceCoordsGotValidPlace() {
		YandexGeocoder geocoder = new YandexGeocoder(url, key);
		Coordinates coords = assertDoesNotThrow(() -> geocoder.getPlaceCoords("Москва"));
		assertNotNull(coords);
		assertEquals(55.755864, coords.getLatitude());
		assertEquals(37.617698, coords.getLongitude());
	}

	@Test
	void getPlaceCoordsGotGrammarMistake() {
		YandexGeocoder geocoder = new YandexGeocoder(url, key);
		Coordinates coords = assertDoesNotThrow(() -> geocoder.getPlaceCoords("Масква"));
		assertNotNull(coords);
		assertEquals(55.755864, coords.getLatitude(), 1e-4);
		assertEquals(37.617698, coords.getLongitude(), 1e-4);
	}

	@Test
	void getPlaceCoordsGotInvalidPlace() {
		YandexGeocoder geocoder = new YandexGeocoder(url, key);
		assertThrows(IllegalArgumentException.class,
			() -> geocoder.getPlaceCoords("Кантерлот"));
	}

	@Test
	void getNameGotValidCoords() {
		YandexGeocoder geocoder = new YandexGeocoder(url, key);

		Coordinates coords = new Coordinates(37.617698, 55.755864);
		assertDoesNotThrow(() -> geocoder.getPlaceName(coords));
	}

	@Test
	void getNameGotInvalidCoords() {
		YandexGeocoder geocoder = new YandexGeocoder(url, key);

		Coordinates coords = new Coordinates(64.967478, 112.285109);
		assertThrows(IllegalArgumentException.class,
			() -> geocoder.getPlaceName(coords));
	}
}
