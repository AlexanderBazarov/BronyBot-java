package ru.untitled_devs.core.routers.filters;

import ru.untitled_devs.core.context.UpdateContext;

public class Command implements Filter{
    String command;

    public Command(String command) {
        this.command = command;
    }

    @Override
    public boolean check(UpdateContext update) {
        if (!update.hasMessage() || update.getUpdate().getMessage().getText() == null ||
                !update.getUpdate().getMessage().getText().startsWith("/")) {
            return false;
        }

        String text = update.getUpdate().getMessage().getText().trim().replaceFirst("/", "");
        int spaceIndex = text.indexOf(' ');
        String enteredCommand = spaceIndex == -1 ? text : text.substring(0, spaceIndex);

        return enteredCommand.equals(command);
    }
}
