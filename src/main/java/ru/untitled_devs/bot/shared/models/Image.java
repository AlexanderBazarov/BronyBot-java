package ru.untitled_devs.bot.shared.models;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

@Entity("Images")
public final class Image {
	@Id
	private ObjectId id;

	private String fileId;
	private String path;

	public ObjectId getId() {
		return id;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
