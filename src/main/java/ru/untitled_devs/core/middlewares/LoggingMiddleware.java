package ru.untitled_devs.core.middlewares;

import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.*;

public class LoggingMiddleware implements Middleware {

    private final Logger logger;

    public LoggingMiddleware(Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean preHandle(Update update) {
        if (update.hasMessage()) {
            Message msg = update.getMessage();
            logger.info("[MESSAGE] From: {} (ID: {}), Chat: {}, Text: {}",
                    msg.getFrom().getUserName(),
                    msg.getFrom().getId(),
                    msg.getChatId(),
                    msg.getText());
        } else if (update.hasCallbackQuery()) {
            CallbackQuery cb = update.getCallbackQuery();
            logger.info("[CALLBACK] From: {} (ID: {}), Chat: {}, Data: {}",
                    cb.getFrom().getUserName(),
                    cb.getFrom().getId(),
                    cb.getMessage().getChatId(),
                    cb.getData());
        } else if (update.hasEditedMessage()) {
            Message msg = update.getEditedMessage();
            logger.info("[EDITED_MESSAGE] From: {} (ID: {}), Chat: {}, Text: {}",
                    msg.getFrom().getUserName(),
                    msg.getFrom().getId(),
                    msg.getChatId(),
                    msg.getText());
        }
        else {
            logger.info("[UNKNOWN] Update type: {}", update);
        }

        return true;
    }
}