package ru.untitled_devs.bot.shared.geocoder.yandex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YandexResponse {

	@JsonProperty("response")
	private Response response;

	public Response getResponse() {
		return response;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Response {
		@JsonProperty("GeoObjectCollection")
		private GeoObjectCollection geoObjectCollection;

		public GeoObjectCollection getGeoObjectCollection() {
			return geoObjectCollection;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class GeoObjectCollection {
		@JsonProperty("metaDataProperty")
		private MetaDataProperty metaDataProperty;

		@JsonProperty("featureMember")
		private List<FeatureMember> featureMember;

		public MetaDataProperty getMetaDataProperty() {
			return metaDataProperty;
		}

		public List<FeatureMember> getFeatureMember() {
			return featureMember;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class MetaDataProperty {
		@JsonProperty("GeocoderResponseMetaData")
		private GeocoderResponseMetaData geocoderResponseMetaData;

		@JsonProperty("GeocoderMetaData")
		private GeocoderMetaData geocoderMetaData;

		@JsonProperty("ReferencesMetaData")
		private ReferencesMetaData referencesMetaData;

		public GeocoderResponseMetaData getGeocoderResponseMetaData() {
			return geocoderResponseMetaData;
		}

		public GeocoderMetaData getGeocoderMetaData() {
			return geocoderMetaData;
		}

		public ReferencesMetaData getReferencesMetaData() {
			return referencesMetaData;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class GeocoderResponseMetaData {
		@JsonProperty("request")
		private String request;
		@JsonProperty("results")
		private String results;
		@JsonProperty("found")
		private String found;

		public String getRequest() {
			return request;
		}

		public String getResults() {
			return results;
		}

		public String getFound() {
			return found;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class GeocoderMetaData {
		@JsonProperty("precision")
		private String precision;
		@JsonProperty("text")
		private String text;
		@JsonProperty("kind")
		private String kind;

		@JsonProperty("Address")
		private Address address;

		@JsonProperty("AddressDetails")
		private AddressDetails addressDetails;

		public String getPrecision() {
			return precision;
		}

		public String getText() {
			return text;
		}

		public String getKind() {
			return kind;
		}

		public Address getAddress() {
			return address;
		}

		public AddressDetails getAddressDetails() {
			return addressDetails;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class FeatureMember {
		@JsonProperty("GeoObject")
		private GeoObject geoObject;

		public GeoObject getGeoObject() {
			return geoObject;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class GeoObject {
		@JsonProperty("metaDataProperty")
		private MetaDataProperty metaDataProperty;

		@JsonProperty("name")
		private String name;

		@JsonProperty("description")
		private String description;

		@JsonProperty("boundedBy")
		private BoundedBy boundedBy;

		@JsonProperty("uri")
		private String uri;

		@JsonProperty("Point")
		private Point point;

		public MetaDataProperty getMetaDataProperty() {
			return metaDataProperty;
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public BoundedBy getBoundedBy() {
			return boundedBy;
		}

		public String getUri() {
			return uri;
		}

		public Point getPoint() {
			return point;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Point {
		@JsonProperty("pos")
		private String pos;

		public String getPos() {
			return pos;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Address {
		@JsonProperty("country_code")
		private String countryCode;

		@JsonProperty("formatted")
		private String formatted;

		@JsonProperty("Components")
		private List<Component> components;

		public String getCountryCode() {
			return countryCode;
		}

		public String getFormatted() {
			return formatted;
		}

		public List<Component> getComponents() {
			return components;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class AddressDetails {
		@JsonProperty("Country")
		private Country country;

		public Country getCountry() {
			return country;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Country {
		@JsonProperty("AddressLine")
		private String addressLine;

		@JsonProperty("CountryNameCode")
		private String countryNameCode;

		@JsonProperty("CountryName")
		private String countryName;

		@JsonProperty("AdministrativeArea")
		private AdministrativeArea administrativeArea;

		public String getAddressLine() {
			return addressLine;
		}

		public String getCountryNameCode() {
			return countryNameCode;
		}

		public String getCountryName() {
			return countryName;
		}

		public AdministrativeArea getAdministrativeArea() {
			return administrativeArea;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class AdministrativeArea {
		@JsonProperty("AdministrativeAreaName")
		private String administrativeAreaName;

		@JsonProperty("SubAdministrativeArea")
		private SubAdministrativeArea subAdministrativeArea;

		public String getAdministrativeAreaName() {
			return administrativeAreaName;
		}

		public SubAdministrativeArea getSubAdministrativeArea() {
			return subAdministrativeArea;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SubAdministrativeArea {
		@JsonProperty("SubAdministrativeAreaName")
		private String subAdministrativeAreaName;

		@JsonProperty("Locality")
		private Locality locality;

		public String getSubAdministrativeAreaName() {
			return subAdministrativeAreaName;
		}

		public Locality getLocality() {
			return locality;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Locality {
		@JsonProperty("LocalityName")
		private String localityName;

		@JsonProperty("Thoroughfare")
		private Thoroughfare thoroughfare;

		public String getLocalityName() {
			return localityName;
		}

		public Thoroughfare getThoroughfare() {
			return thoroughfare;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Thoroughfare {
		@JsonProperty("ThoroughfareName")
		private String thoroughfareName;

		public String getThoroughfareName() {
			return thoroughfareName;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ReferencesMetaData {
		@JsonProperty("references")
		private List<Reference> references;

		public List<Reference> getReferences() {
			return references;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Reference {
		@JsonProperty("id")
		private String id;

		@JsonProperty("scope")
		private String scope;

		public String getId() {
			return id;
		}

		public String getScope() {
			return scope;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class BoundedBy {
		@JsonProperty("Envelope")
		private Envelope envelope;

		public Envelope getEnvelope() {
			return envelope;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Envelope {
		@JsonProperty("lowerCorner")
		private String lowerCorner;

		@JsonProperty("upperCorner")
		private String upperCorner;

		public String getLowerCorner() {
			return lowerCorner;
		}

		public String getUpperCorner() {
			return upperCorner;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Component {
		@JsonProperty("kind")
		private String kind;

		@JsonProperty("name")
		private String name;

		public String getKind() {
			return kind;
		}

		public String getName() {
			return name;
		}
	}
}
