package ru.untitled_devs.bot.config;

import java.util.Objects;

public final class MongoConfig {
	private String mongoHost;
	private int mongoPort;
	private String mongoUser;
	private String mongoPass;
	private String mongoDBName;
	private boolean mongoDirectConnection;

	public MongoConfig() {
		loadVariables();
	}

	private void loadVariables() {
		mongoHost = System.getenv("MONGO_HOST");
		mongoPort = Integer.parseInt(System.getenv("MONGO_PORT"));
		mongoUser = System.getenv("MONGO_USER");
		mongoPass = System.getenv("MONGO_PASS");
		mongoDBName = System.getenv("MONGO_DBNAME");
		mongoDirectConnection = Objects.equals(System.getenv("MONGO_DIRECT_CONNECTION"), "true");
	}

	public String getMongoHost() {
		return mongoHost;
	}

	public int getMongoPort() {
		return mongoPort;
	}

	public String getMongoUser() {
		return mongoUser;
	}

	public String getMongoPass() {
		return mongoPass;
	}

	public String getMongoDBName() {
		return mongoDBName;
	}

	public String getMongoString() {
		return String.format("mongodb://%s:%s@%s:%d/?directConnection=%s",
			mongoUser, mongoPass, mongoHost, mongoPort, mongoDirectConnection ? "true" : "false");
	}
}
