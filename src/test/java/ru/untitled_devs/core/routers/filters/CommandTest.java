package ru.untitled_devs.core.routers.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.context.UpdateContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandTest {

    private UpdateContext updateContextMock;
    private Message messageMock;
	private Update updateMock;

    @BeforeEach
    void setUp() {
        updateContextMock = mock(UpdateContext.class);
        messageMock = mock(Message.class);
		updateMock = mock(Update.class);

        when(updateContextMock.getUpdate()).thenReturn(updateMock);
        when(updateContextMock.hasMessage()).thenReturn(true);
		when(updateMock.getMessage()).thenReturn(messageMock);
    }

    @Test
    void checkReturnsTrueWhenCommandMatches() {
        Command command = new Command("test");
        when(messageMock.getText()).thenReturn("/test");
        when(updateContextMock.hasMessage()).thenReturn(true);

        boolean result = command.check(updateContextMock);
        assertTrue(result, "check() should return true when the text contains the command");

    }

    @Test
    void checkReturnsTrueWhenMultiwordCommandMatched() {
        Command command = new Command("test");
        when(messageMock.getText()).thenReturn("/test arg1 arg2");
        when(updateContextMock.hasMessage()).thenReturn(true);

        boolean result = command.check(updateContextMock);
        assertTrue(result, "check() should return true when the text contains the command");
    }

    @Test
    void checkReturnsTrueWhenCommandNotMatches() {
        Command command = new Command("start");
        when(messageMock.getText()).thenReturn("/test");
        when(updateContextMock.hasMessage()).thenReturn(true);

        boolean result = command.check(updateContextMock);
        assertFalse(result, "check() should return false when the text not contains the command");

    }

    @Test
    void checkReturnsTrueWhenMultiwordCommandNotMatched() {
        Command command = new Command("start");
        when(messageMock.getText()).thenReturn("/test arg1 arg2");
        when(updateContextMock.hasMessage()).thenReturn(true);

        boolean result = command.check(updateContextMock);
        assertFalse(result, "check() should return false when the text not contains the command");
    }

}
