package ru.untitled_devs.core.routers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.untitled_devs.core.context.UpdateContext;
import ru.untitled_devs.core.exceptions.StopRoutingException;
import ru.untitled_devs.core.fsm.FSMContext;
import ru.untitled_devs.core.fsm.states.State;
import ru.untitled_devs.core.routers.handlers.Handler;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RouterTest {

    private UpdateRouter router;
    private FSMContext ctx;
    private UpdateContext update;
    private State state;

    @BeforeEach
    void setup() {
        router = new UpdateRouter();
        ctx = mock(FSMContext.class);
        update = mock(UpdateContext.class);
        state = new State("TEST");
        when(ctx.getState()).thenReturn(state);
    }

    @Test
    void addHandlerStoresHandlerUnderCorrectState() {
        Handler handler = mock(Handler.class);
        router.addHandler(state, handler);

        Map<State, List<Handler>> handlers = router.getHandlers();
        assertTrue(handlers.containsKey(state));
        assertEquals(1, handlers.get(state).size());
        assertSame(handler, handlers.get(state).getFirst());
    }

    @Test
    void getHandlersReturnsUnmodifiableMap() {
        router.addHandler(state, mock(Handler.class));
        Map<State, List<Handler>> handlers = router.getHandlers();

        assertThrows(UnsupportedOperationException.class, () -> handlers.put(new State("Other"), List.of()));
    }

    @Test
    void routeUpdateCallsOnlyHandlersThatCanHandle() {
        Handler handlerTrue = mock(Handler.class);
        Handler handlerFalse = mock(Handler.class);

        when(handlerTrue.canHandle(update, ctx)).thenReturn(true);
        when(handlerFalse.canHandle(update, ctx)).thenReturn(false);

        router.addHandler(state, handlerTrue);
        router.addHandler(state, handlerFalse);

		try {
			router.routeUpdate(update, ctx);
		} catch (StopRoutingException e) {}

        verify(handlerTrue, times(1)).handleUpdate(update, ctx);
        verify(handlerFalse, never()).handleUpdate(any(), any());
    }

    @Test
    void routeUpdateDoesNothingIfNoHandlersForState() {
        FSMContext ctxWithoutState = mock(FSMContext.class);
        when(ctxWithoutState.getState()).thenReturn(new State("UNREGISTERED"));

        assertDoesNotThrow(() -> router.routeUpdate(update, ctxWithoutState));
    }
}
