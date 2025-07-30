package ru.untitled_devs.bot.features.registration;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.untitled_devs.bot.shared.image.ImageService;
import ru.untitled_devs.bot.shared.localisation.MessageKey;
import ru.untitled_devs.bot.shared.localisation.MsgLocService;
import ru.untitled_devs.bot.shared.models.Image;
import ru.untitled_devs.core.client.BotClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class PhotoProcessor {
	private final BotClient bot;
	public final ImageService imageService;
	private final int MAX_FILE_SIZE;

	public PhotoProcessor(BotClient bot, ImageService imageService, int maxFileSize) {
		this.bot = bot;
		this.imageService = imageService;
		MAX_FILE_SIZE = maxFileSize;
	}

	public Image processPhoto(Message message, Locale loc) {
		PhotoSize largest = imageService.getLargestPhotoSize(message.getPhoto());
		String fileId = largest.getFileId();
		int fileSize = largest.getFileSize();

		if (fileSize > MAX_FILE_SIZE) {
			bot.sendMessage(message.getChatId(),
				MsgLocService.getLocal(MessageKey.TOO_BIG_PHOTO_SIZE, loc));
			throw new IllegalArgumentException("File size must be no bigger than 5 MB");
		}

		String filePath = bot.getFile(fileId).getFilePath();
		try (InputStream stream = bot.downloadFileAsStream(filePath)) {
			return imageService.saveImage(stream, fileId);
		} catch (IOException | TelegramApiException e) {
			bot.sendMessage(message.getChatId(),
				MsgLocService.getLocal(MessageKey.FILE_DOWNLOADING_ERROR, loc));
			throw new RuntimeException(e);
		}
	}
}
