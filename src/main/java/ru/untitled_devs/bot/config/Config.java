package ru.untitled_devs.bot.config;

public final class Config {
    private String botToken;
    private String botName;

    public Config() {
        loadVariables();
    }

    private void loadVariables() {
        this.botToken = System.getenv("BOT_TOKEN");
        this.botName = System.getenv("BOT_NAME");
    }

    public String getBotToken() {
        return this.botToken;
    }

    public String getBotName() {
        return this.botName;
    }
}
