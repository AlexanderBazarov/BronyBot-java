package ru.untitled_devs.core.routers.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegexFilterTest {

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
    void checkShouldReturnTrueWhenPatternMatchesText() {
        RegexFilter filter = new RegexFilter("hello");
        when(updateMock.hasMessage()).thenReturn(true);
        when(messageMock.hasText()).thenReturn(true);
        when(messageMock.getText()).thenReturn("say hello to the world");

        boolean result = filter.check(updateMock);

        assertTrue(result, "check() should return true when the text contains the pattern");
    }

    @Test
    void checkShouldReturnFalseWhenPatternDoesNotMatchText() {
        RegexFilter filter = new RegexFilter("\\d+");
        when(messageMock.getText()).thenReturn("no digits here");

        boolean result = filter.check(updateMock);

        assertFalse(result, "check() should return false when the text does not contain the pattern");
    }

    @Test
    void checkShouldHonorUnicodeFlagWhenMatchingUnicodeCharacters() {
        RegexFilter filter = new RegexFilter("\\p{L}+");
        when(updateMock.hasMessage()).thenReturn(true);
        when(messageMock.hasText()).thenReturn(true);
        when(messageMock.getText()).thenReturn("Скажи привет, пожалуйста");

        boolean result = filter.check(updateMock);

        assertTrue(result, "check() should return true when the text contains the Unicode pattern");
    }

    @Test
    void checkShouldReturnFalseOnEmptyText() {
        RegexFilter filter = new RegexFilter(".+");
        when(messageMock.getText()).thenReturn("");

        boolean result = filter.check(updateMock);

        assertFalse(result, "check() should return false when the message text is empty");
    }

    @Test
    void checkShouldThrowExceptionWhenMessageIsNull() {
        RegexFilter filter = new RegexFilter("test");
        when(updateMock.getMessage()).thenReturn(null);

        assertThrows(NullPointerException.class,
                () -> filter.check(updateMock),
                "check() should throw NullPointerException when update.getMessage() is null");
    }

    @Test
    void checkShouldNotThrowExceptionWhenTextIsNull() {
        RegexFilter filter = new RegexFilter("test");
        when(messageMock.getText()).thenReturn(null);

        assertDoesNotThrow(() -> filter.check(updateMock),
                "check() should throw NullPointerException when message.getText() is null");
    }
}
