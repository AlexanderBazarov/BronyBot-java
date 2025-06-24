package ru.untitled_devs.bot.shared.repositories;

import dev.morphia.Datastore;
import ru.untitled_devs.bot.shared.models.Profile;

public class ProfileRepo extends MongoRepo<Profile> {

	public ProfileRepo(Datastore datastore) {
		super(datastore, Profile.class);
	}
}
