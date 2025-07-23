package ru.untitled_devs.bot.features.registration;

import dev.morphia.Datastore;
import ru.untitled_devs.bot.shared.models.Profile;
import ru.untitled_devs.bot.shared.models.User;
import ru.untitled_devs.bot.shared.repositories.ProfileRepo;
import ru.untitled_devs.bot.shared.repositories.UserRepo;

import java.util.Optional;

public final class RegistrationService {

	private final UserRepo userRepo;
	private final ProfileRepo profileRepo;

	public RegistrationService(Datastore datastore) {
		userRepo = new UserRepo(datastore);
		profileRepo = new ProfileRepo(datastore);
	}

	public void registerUser(User user, Profile profile) {
		Profile savedProfile = profileRepo.save(profile);
		user.setProfile(savedProfile);
		userRepo.save(user);
	}

	public Optional<User> getByChatId(long chatId) {
		return userRepo.getByTelegramId(chatId);
	}

	public boolean isUseRegistered(long chatId) {
		return getByChatId(chatId).isPresent();
	}

}
