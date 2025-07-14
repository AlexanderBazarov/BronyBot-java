package ru.untitled_devs.bot.shared.geocoder;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Coordinates that = (Coordinates) o;
		return Double.compare(latitude, that.latitude) == 0 && Double.compare(longitude, that.longitude) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(latitude, longitude);
	}
}
