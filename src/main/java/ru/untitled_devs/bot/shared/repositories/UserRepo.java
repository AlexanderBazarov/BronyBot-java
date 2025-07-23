package ru.untitled_devs.bot.shared.repositories;

import dev.morphia.Datastore;
import ru.untitled_devs.bot.shared.models.User;

import java.util.Optional;

import static dev.morphia.query.filters.Filters.eq;

public final class UserRepo extends MongoRepo<User>{
	private final Datastore datastore;

	public UserRepo(Datastore datastore) {
		super(datastore, User.class);

		this.datastore = datastore;
	}

	public Optional<User> getByTelegramId(long telegramId) {
		return Optional.ofNullable(datastore.find(User.class).filter(eq("telegramId", telegramId)).first());
	}


}
