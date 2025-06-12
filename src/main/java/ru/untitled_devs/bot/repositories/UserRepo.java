package ru.untitled_devs.bot.repositories;

import dev.morphia.Datastore;

public class UserRepo {
    private final Datastore datastore;

    public UserRepo(Datastore datastore) {
        this.datastore = datastore;
    }

}
