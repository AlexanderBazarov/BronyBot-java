package ru.untitled_devs.bot.config;

import java.nio.file.Path;

public class ImagesConfig {
	private Path imagesPath;

	public ImagesConfig() {
		loadVariables();
	}

	void loadVariables() {
		imagesPath = Path.of(System.getenv("IMAGES_PATH"));
	}

	public Path getImagesPath() {
		return imagesPath;
	}
}
