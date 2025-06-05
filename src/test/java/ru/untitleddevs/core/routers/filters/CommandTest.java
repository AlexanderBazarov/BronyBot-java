package ru.untitleddevs.core.routers.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.routers.filters.Command;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandTest {

    private Update updateMock;
    private Message messageMock;

    @BeforeEach
    void setUp() {
        updateMock = mock(Update.class);
        messageMock = mock(Message.class);
        when(updateMock.getMessage()).thenReturn(messageMock);
        when(updateMock.hasMessage()).thenReturn(true);
    }

    @Test
    void checkReturnsTrueWhenCommandMatches() {
        Command command = new Command("test");
        when(messageMock.getText()).thenReturn("/test");
        when(updateMock.hasMessage()).thenReturn(true);

        boolean result = command.check(updateMock);
        assertTrue(result, "check() should return true when the text contains the command");

    }

    @Test
    void checkReturnsTrueWhenMultiwordCommandMatched() {
        Command command = new Command("test");
        when(messageMock.getText()).thenReturn("/test arg1 arg2");
        when(updateMock.hasMessage()).thenReturn(true);

        boolean result = command.check(updateMock);
        assertTrue(result, "check() should return true when the text contains the command");
    }

    @Test
    void checkReturnsTrueWhenCommandNotMatches() {
        Command command = new Command("start");
        when(messageMock.getText()).thenReturn("/test");
        when(updateMock.hasMessage()).thenReturn(true);

        boolean result = command.check(updateMock);
        assertFalse(result, "check() should return false when the text not contains the command");

    }

    @Test
    void checkReturnsTrueWhenMultiwordCommandNotMatched() {
        Command command = new Command("start");
        when(messageMock.getText()).thenReturn("/test arg1 arg2");
        when(updateMock.hasMessage()).thenReturn(true);

        boolean result = command.check(updateMock);
        assertFalse(result, "check() should return false when the text not contains the command");
    }

}
