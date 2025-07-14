package ru.untitled_devs.bot.shared.image;

import dev.morphia.Datastore;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import ru.untitled_devs.bot.shared.models.Image;
import ru.untitled_devs.bot.shared.repositories.ImageRepo;
import ru.untitled_devs.core.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;

public class ImageService {
	private final Path imagesPath;
	private final ImageRepo imageRepo;

	public ImageService(Path imagesPath, Datastore datastore) {
		this.imagesPath = imagesPath;
		this.imageRepo = new ImageRepo(datastore);
	}

	public Image saveImage(InputStream in, String fileId) throws IOException {
		byte[] bytes = in.readAllBytes();

		String ext = FileUtils.getImageFileExtension(bytes);

		String fileName = fileId + ext;
		Path target = imagesPath.resolve(fileName);

		Files.createDirectories(target.getParent());

		Files.createDirectories(target.getParent());
		Files.write(target, bytes, StandardOpenOption.CREATE);

		Files.write(target, bytes, StandardOpenOption.CREATE);

		Image img = new Image();
		img.setFileId(fileId);
		img.setPath(fileName);
		return imageRepo.save(img);
	}

	public Image getImage(String fileId) {
		return imageRepo.findByFileId(fileId);
	}

	public byte[] loadImage(Image image) throws IOException {
		Path path = imagesPath.resolve(image.getPath());
		return Files.readAllBytes(path);
	}

	public PhotoSize getLargestPhotoSize(List<PhotoSize> photos) {
		return photos.stream()
			.max(Comparator.comparing(PhotoSize::getFileSize))
			.orElseThrow(() -> new IllegalStateException("Фото не найдено"));
	}
}
