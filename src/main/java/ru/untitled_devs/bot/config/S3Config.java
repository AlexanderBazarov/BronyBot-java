package ru.untitled_devs.bot.config;

public class S3Config {
	private String accessKey;
	private String secretKey;
	private String bucketName;
	private String endpoint;

	public S3Config() {
		loadVariables();
	}

	void loadVariables() {
		accessKey = System.getenv("ACCESS_KEY");
		secretKey = System.getenv("SECRET_KEY");
		bucketName = System.getenv("BUCKET_NAME");
		endpoint = System.getenv("S3_ENDPOINT");
	}

	public String getAccessKey() {
		return accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public String getBucketName() {
		return bucketName;
	}

	public String getEndpoint() {
		return endpoint;
	}
}
