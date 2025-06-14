package ru.untitled_devs.bot.shared.models;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

@Entity("Accounts")
public final class User implements Model{
    @Id
    private ObjectId id;

    private int telegramId;
    private String lang;
    private boolean admin;
    private boolean banned;

    @Reference
    private Profile profile;
}
