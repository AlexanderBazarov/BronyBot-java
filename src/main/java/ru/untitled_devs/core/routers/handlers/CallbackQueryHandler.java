package ru.untitled_devs.core.routers.handlers;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.fsm.FSMContext;
import ru.untitled_devs.core.routers.filters.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class CallbackQueryHandler implements Handler {

    private final List<Filter> filters = new ArrayList<>();
    private final BiConsumer<CallbackQuery, FSMContext> action;

    public CallbackQueryHandler(BiConsumer<CallbackQuery, FSMContext> action, Filter... filters) {
        this.filters.addAll(Arrays.asList(filters));
        this.action = action;
    }

    @Override
    public boolean canHandle(UpdateContext update, FSMContext ctx) {
        return update.hasCallbackQuery() &&
                filters.stream().allMatch(filter -> filter.check(update)) ;
    }

    @Override
    public void handleUpdate(UpdateContext update, FSMContext ctx) {
        action.accept(update.getUpdate().getCallbackQuery(), ctx);
    }

    public void addFilter(Filter filter) {
        this.filters.add(filter);
    }

}
