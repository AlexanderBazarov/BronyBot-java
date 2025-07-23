package ru.untitled_devs.core.routers.filters;

import ru.untitled_devs.core.context.UpdateContext;

import java.util.regex.Pattern;

public class RegexFilter implements Filter {

    private final Pattern pattern;

    public RegexFilter(String pattern) {
        this.pattern = Pattern.compile(pattern, Pattern.UNICODE_CHARACTER_CLASS);
    }

    @Override
    public boolean check(UpdateContext update) {
        String text = extractTextFromUpdate(update);

        if (text == null) return false;

        return this.pattern.matcher(text).find();
    }

    private String extractTextFromUpdate(UpdateContext update) {
        if (update.hasMessage() && update.getUpdate().getMessage().hasText()) {
            return update.getUpdate().getMessage().getText();
        }
        if (update.hasCallbackQuery()) {
            return update.getUpdate().getCallbackQuery().getData();
        }
        return null;
    }

}
