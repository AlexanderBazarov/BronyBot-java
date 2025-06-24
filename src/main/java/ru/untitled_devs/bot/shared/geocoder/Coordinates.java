package ru.untitled_devs.bot.shared.geocoder;

public class Coordinates {
	private final double latitude;
	private final double longitude;

	public Coordinates(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return latitude + "," + longitude;
	}
}
