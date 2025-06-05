package ru.untitled_devs.core.routers.filters;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.regex.Pattern;

public class RegexFilter implements Filter {

    private final Pattern pattern;

    public RegexFilter(String pattern) {
        this.pattern = Pattern.compile(pattern, Pattern.UNICODE_CHARACTER_CLASS);
    }

    @Override
    public boolean check(Update update) {
        String text = extractTextFromUpdate(update);

        if (text == null) return false;

        return this.pattern.matcher(text).find();
    }

    private String extractTextFromUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return update.getMessage().getText();
        }
        if (update.hasEditedMessage() && update.getEditedMessage().hasText()) {
            return update.getEditedMessage().getText();
        }
        if (update.hasChannelPost() && update.getChannelPost().hasText()) {
            return update.getChannelPost().getText();
        }
        if (update.hasEditedChannelPost() && update.getEditedChannelPost().hasText()) {
            return update.getEditedChannelPost().getText();
        }
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getData();
        }
        if (update.hasInlineQuery()) {
            return update.getInlineQuery().getQuery();
        }
        return null;
    }

}
