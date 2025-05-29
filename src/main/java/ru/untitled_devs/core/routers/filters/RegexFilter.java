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
        String text = update.getMessage().getText();

        return this.pattern.matcher(text).find();
    }
}
