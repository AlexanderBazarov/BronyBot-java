package ru.untitled_devs.bot.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.morphia.Datastore;
import ru.untitled_devs.bot.config.GeocodingConfig;
import ru.untitled_devs.bot.config.S3Config;
import ru.untitled_devs.bot.shared.geocoder.Geocoder;
import ru.untitled_devs.bot.shared.geocoder.yandex.YandexGeocoder;
import ru.untitled_devs.bot.shared.image.ImageService;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;

import java.net.URI;

public class SharedModule extends AbstractModule {
	@Override
	protected void configure() {

	}

	@Provides @Singleton
	Geocoder geocoder(GeocodingConfig cfg) {
		return new YandexGeocoder(cfg.getApiUrl(), cfg.getApiKey());
	}

	@Provides @Singleton
	AwsCredentials awsCredentials(S3Config cfg) {
		return AwsBasicCredentials.create(cfg.getAccessKey(), cfg.getSecretKey());
	}

	@Provides @Singleton
	ImageService imageService(S3Config cfg, AwsCredentials creds, Datastore datastore) {
		URI endpoint = URI.create(cfg.getEndpoint());
		Region region = Region.US_EAST_1;
		return new ImageService(endpoint, creds, region, cfg.getBucketName(), datastore);
	}
}
