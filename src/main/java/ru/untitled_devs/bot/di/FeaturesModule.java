package ru.untitled_devs.bot.di;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import ru.untitled_devs.bot.features.localisation.LocalisationMiddleware;
import ru.untitled_devs.bot.features.localisation.LocalisationScene;
import ru.untitled_devs.bot.features.menu.MainMenuScene;
import ru.untitled_devs.bot.features.prfile.ProfileScene;
import ru.untitled_devs.bot.features.registration.LoginMiddleware;
import ru.untitled_devs.bot.features.registration.ProfilePreviewService;
import ru.untitled_devs.bot.features.registration.RegistrationScene;
import ru.untitled_devs.bot.features.registration.RegistrationService;
import ru.untitled_devs.bot.features.start.StartMiddleware;

public class FeaturesModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(RegistrationService.class).in(Singleton.class);
		bind(ProfilePreviewService.class).in(Singleton.class);

		bind(RegistrationScene.class).in(Singleton.class);
		bind(LocalisationScene.class).in(Singleton.class);
		bind(MainMenuScene.class).in(Singleton.class);
		bind(ProfileScene.class).in(Singleton.class);

		bind(LocalisationMiddleware.class).in(Singleton.class);
		bind(LoginMiddleware.class).in(Singleton.class);

		bind(StartMiddleware.class).in(Singleton.class);
	}

}
