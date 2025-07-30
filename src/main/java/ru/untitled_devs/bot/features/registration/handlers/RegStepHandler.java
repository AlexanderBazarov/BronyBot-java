package ru.untitled_devs.bot.features.registration.handlers;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.untitled_devs.bot.shared.models.Profile;
import ru.untitled_devs.core.client.BotClient;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.DataKey;
import ru.untitled_devs.core.fsm.FSMContext;
import ru.untitled_devs.core.routers.filters.Filter;
import ru.untitled_devs.core.routers.handlers.Handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public abstract class RegStepHandler implements Handler {
	protected final static DataKey<Locale> langKey = DataKey.of("lang", Locale.class);
	protected final static DataKey<Profile> profileKey = DataKey.of("RegistrationData", Profile.class);

	private final List<Filter> filters = new ArrayList<>();
	protected BotClient bot;

	public RegStepHandler(BotClient bot, Filter... filters) {
		this.filters.addAll(Arrays.asList(filters));
		this.bot = bot;
	}

	abstract void action(Message message, FSMContext ctx);

	@Override
	public boolean canHandle(UpdateContext update, FSMContext ctx) {
		return update.hasMessage() &&
			filters.stream().allMatch(filter -> filter.check(update));
	}

	@Override
	public void handleUpdate(UpdateContext update, FSMContext ctx) {
		action(update.getUpdate().getMessage(), ctx);
	}

	@Override
	public void addFilter(Filter filter) {
		this.filters.add(filter);
	}
}
