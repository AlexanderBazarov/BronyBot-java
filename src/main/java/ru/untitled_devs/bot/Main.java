package ru.untitled_devs.bot;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.untitled_devs.bot.di.AppModule;

public class Main {
    public static void main(String[] args) {
		Injector injector = Guice.createInjector(new AppModule());
		injector.getInstance(Bootstrap.class).start();
	}
}
