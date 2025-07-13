package ru.untitled_devs.core.routers.handlers;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.filters.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class MessageHandler implements Handler {

    private final List<Filter> filters = new ArrayList<>();
    private final BiConsumer<Message, FSMContext> action;

    public MessageHandler(BiConsumer<Message, FSMContext> action, Filter... filters) {
        this.filters.addAll(Arrays.asList(filters));
        this.action = action;
    }

    @Override
    public boolean canHandle(UpdateContext update, FSMContext ctx) {
        return update.hasMessage() &&
                filters.stream().allMatch(filter -> filter.check(update));
    }

    @Override
    public void handleUpdate(UpdateContext update, FSMContext ctx) {
		Message message = update.getUpdate().getMessage();
        action.accept(update.getUpdate().getMessage(), ctx);
    }

    @Override
    public void addFilter(Filter filter) {
        this.filters.add(filter);
    }

}
