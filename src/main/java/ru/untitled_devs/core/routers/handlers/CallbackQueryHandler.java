package ru.untitled_devs.core.routers.handlers;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.fsm.context.FSMContext;
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
    public boolean canHandle(Update update, FSMContext ctx) {
        return update.hasCallbackQuery() &&
                filters.stream().allMatch(filter -> filter.check(update)) ;
    }

    @Override
    public void handleUpdate(Update update, FSMContext ctx) {
        action.accept(update.getCallbackQuery(), ctx);
    }

    public void addFilter(Filter filter) {
        this.filters.add(filter);
    }

}
