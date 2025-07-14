package ru.untitled_devs.bot.shared.repositories;

import dev.morphia.Datastore;
import ru.untitled_devs.bot.shared.models.Image;

import static dev.morphia.query.filters.Filters.eq;

public class ImageRepo extends MongoRepo<Image> {
	private final Datastore datastore;
	public ImageRepo(Datastore datastore) {
		super(datastore, Image.class);
		this.datastore = datastore;
	}

	public Image findByFileId(String fileId) {
		return datastore.find(Image.class).filter(eq("fileId", fileId)).first();
	}
}
