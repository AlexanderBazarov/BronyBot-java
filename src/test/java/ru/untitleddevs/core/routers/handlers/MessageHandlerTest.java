package ru.untitleddevs.core.routers.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.untitled_devs.core.fsm.State;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.filters.Filter;
import ru.untitled_devs.core.routers.handlers.MessageHandler;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MessageHandlerTest {
    private FSMContext ctxMock;
    private Update updateMock;
    private Message messageMock;
    private State handlerState;
    private State differentState;

    @BeforeEach
    void setup() {
        ctxMock = mock(FSMContext.class);
        updateMock = mock(Update.class);
        messageMock = mock(Message.class);

        handlerState = new State("EXPECTED_STATE");
        differentState = new State("OTHER_STATE");
    }

    @Test
    void canHandleReturnsFalseWhenStateDiffers() {
        when(ctxMock.getState()).thenReturn(differentState);
        MessageHandler handler = new MessageHandler((msg, ctx) -> {}, handlerState);

        boolean result = handler.canHandle(updateMock, ctxMock);

        assertFalse(result, "canHandle() should return false when context state does not match handler state");
    }

    @Test
    void canHandleReturnsFalseWhenNoMessage() {
        when(ctxMock.getState()).thenReturn(handlerState);
        when(updateMock.hasMessage()).thenReturn(false);
        MessageHandler handler = new MessageHandler((msg, ctx) -> {}, handlerState);

        boolean result = handler.canHandle(updateMock, ctxMock);

        assertFalse(result, "canHandle() should return false when update.hasMessage() is false");
    }

    @Test
    void canHandleReturnsFalseWhenFilterFails() {
        when(ctxMock.getState()).thenReturn(handlerState);
        when(updateMock.hasMessage()).thenReturn(true);
        Filter failingFilter = mock(Filter.class);
        when(failingFilter.check(updateMock)).thenReturn(false);

        MessageHandler handler = new MessageHandler((msg, ctx) -> {}, handlerState, failingFilter);

        boolean result = handler.canHandle(updateMock, ctxMock);

        assertFalse(result, "canHandle() should return false if any filter.check(update) returns false");
    }

    @Test
    void canHandleReturnsTrueWhenStateAndFiltersPass() {
        when(ctxMock.getState()).thenReturn(handlerState);
        when(updateMock.hasMessage()).thenReturn(true);
        Filter filter1 = mock(Filter.class);
        Filter filter2 = mock(Filter.class);
        when(filter1.check(updateMock)).thenReturn(true);
        when(filter2.check(updateMock)).thenReturn(true);

        MessageHandler handler = new MessageHandler((msg, ctx) -> {}, handlerState, filter1, filter2);

        boolean result = handler.canHandle(updateMock, ctxMock);

        assertTrue(result, "canHandle() should return true when state matches, update.hasMessage() is true, and all filters pass");
    }

    @Test
    void handleUpdateInvokesActionWithCorrectParams() {
        @SuppressWarnings("unchecked")
        BiConsumer<Message, FSMContext> actionMock = mock(BiConsumer.class);
        MessageHandler handler = new MessageHandler(actionMock, handlerState);
        when(ctxMock.getState()).thenReturn(handlerState);
        when(updateMock.getMessage()).thenReturn(messageMock);

        handler.handleUpdate(updateMock, ctxMock);

        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        ArgumentCaptor<FSMContext> ctxCaptor = ArgumentCaptor.forClass(FSMContext.class);
        verify(actionMock, times(1)).accept(messageCaptor.capture(), ctxCaptor.capture());
        assertSame(messageMock, messageCaptor.getValue(), "handleUpdate() should pass the update's message to action.accept()");
        assertSame(ctxMock, ctxCaptor.getValue(), "handleUpdate() should pass the same FSMContext instance to action.accept()");
    }

    @Test
    void addFilterAffectsCanHandle() {
        when(ctxMock.getState()).thenReturn(handlerState);
        when(updateMock.hasMessage()).thenReturn(true);
        when(updateMock.getMessage()).thenReturn(messageMock);

        MessageHandler handler = new MessageHandler((msg, ctx) -> {}, handlerState);
        assertTrue(handler.canHandle(updateMock, ctxMock),
                "Initially, with no filters, canHandle() should return true when state matches and update.hasMessage() is true");

        Filter falseFilter = mock(Filter.class);
        when(falseFilter.check(updateMock)).thenReturn(false);
        handler.addFilter(falseFilter);

        boolean resultAfterAdding = handler.canHandle(updateMock, ctxMock);

        assertFalse(resultAfterAdding,
                "After adding a filter that returns false, canHandle() should return false");
    }
}
