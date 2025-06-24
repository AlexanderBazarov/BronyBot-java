package ru.untitled_devs.bot.shared.repositories;

import dev.morphia.Datastore;
import ru.untitled_devs.bot.shared.models.User;

public final class UserRepo extends MongoRepo<User>{
	public UserRepo(Datastore datastore) {
		super(datastore, User.class);
	}
}
