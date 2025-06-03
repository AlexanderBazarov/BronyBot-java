package ru.untitled_devs.core.routers.filters;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

public class Command implements Filter{
    String command;

    public Command(String command) {
        this.command = command;
    }

    @Override
    public boolean check(Update update) {
        if (!update.hasMessage() || update.getMessage().getText() == null ||
                update.getMessage().getText().startsWith("/")) {
            return false;
        }

        String text = update.getMessage().getText().trim().replaceFirst("/", "");
        int spaceIndex = text.indexOf(' ');
        String enteredCommand = spaceIndex == -1 ? text : text.substring(0, spaceIndex);

        return enteredCommand.equals(command);
    }
}
