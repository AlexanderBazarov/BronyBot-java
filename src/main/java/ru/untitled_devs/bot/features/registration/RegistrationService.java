package ru.untitled_devs.bot.features.registration;

import dev.morphia.Datastore;
import ru.untitled_devs.bot.shared.models.Profile;
import ru.untitled_devs.bot.shared.models.User;
import ru.untitled_devs.bot.shared.repositories.ProfileRepo;
import ru.untitled_devs.bot.shared.repositories.UserRepo;

public final class RegistrationService {
	private final Datastore datastore;

	private final UserRepo userRepo;
	private final ProfileRepo profileRepo;

	public RegistrationService(Datastore datastore) {
		this.datastore = datastore;
		userRepo = new UserRepo(datastore);
		profileRepo = new ProfileRepo(datastore);
	}

	void registerUser(User user, Profile profile) {
		Profile savedProfile = profileRepo.save(profile);
		user.setProfile(savedProfile);
		userRepo.save(user);
	}

}
