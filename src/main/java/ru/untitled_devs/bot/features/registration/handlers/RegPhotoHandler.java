package ru.untitled_devs.bot.features.registration.handlers;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.untitled_devs.bot.features.registration.PhotoProcessor;
import ru.untitled_devs.bot.features.registration.ProfilePreviewService;
import ru.untitled_devs.bot.features.registration.RegistrationStates;
import ru.untitled_devs.bot.shared.image.ImageService;
import ru.untitled_devs.bot.shared.localisation.ButtonKey;
import ru.untitled_devs.bot.shared.localisation.BtnLocService;
import ru.untitled_devs.bot.shared.localisation.MessageKey;
import ru.untitled_devs.bot.shared.localisation.MsgLocService;
import ru.untitled_devs.bot.shared.models.Image;
import ru.untitled_devs.bot.shared.models.Profile;
import ru.untitled_devs.core.client.BotClient;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.filters.Filter;

import java.util.Locale;
import java.util.Objects;

public class RegPhotoHandler extends RegStepHandler {
	private final ImageService imageService;
	private final ProfilePreviewService previewService;
	private final PhotoProcessor processor;

	private static int MAX_FILE_SIZE = 5 * 1024 * 1024;

	public RegPhotoHandler(BotClient bot, ImageService imageService, Filter... filters) {
		super(bot, filters);

		this.imageService = imageService;
		previewService = new ProfilePreviewService(bot, imageService);
		processor = new PhotoProcessor(bot, imageService, MAX_FILE_SIZE);
	}

	@Override
	void action(Message message, FSMContext ctx) {
		Locale loc = ctx.getData(langKey);
		String skipWord = BtnLocService.getLocal(ButtonKey.SKIP_WORD, loc);
		Profile profileData = ctx.getData(profileKey);

		if (message.hasPhoto()) {
			Image image;
			image = processor.processPhoto(message, loc);
			profileData.setImage(image);
			ctx.setData(profileKey, profileData);
			ctx.setState(RegistrationStates.FINISH);
			previewService.sendPreview(profileData, loc, message.getChatId());
			return;
		}

		if (Objects.equals(message.getText(), skipWord)) {
			profileData.setImage(null);
			ctx.setData(profileKey, profileData);
			ctx.setState(RegistrationStates.FINISH);
			previewService.sendPreview(profileData, loc, message.getChatId());
			return;
		}

		bot.sendMessage(message.getChatId(),
			MsgLocService.getLocal(MessageKey.INCORRECT_INPUT, loc));
	}
}
