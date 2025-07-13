package ru.untitled_devs.bot.shared.models;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

@Entity("Accounts")
public final class User {
    @Id
    private ObjectId id;

    private long telegramId;
    private String lang;
    private boolean admin = false;
    private boolean banned = false;

    @Reference
    private Profile profile;

	public ObjectId getId() {
		return id;
	}

	public long getTelegramId() {
		return telegramId;
	}

	public void setTelegramId(long telegramId) {
		this.telegramId = telegramId;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isBanned() {
		return banned;
	}

	public void setBanned(boolean banned) {
		this.banned = banned;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
}
