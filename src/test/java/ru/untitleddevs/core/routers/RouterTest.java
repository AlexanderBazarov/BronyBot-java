package ru.untitleddevs.core.routers;

import org.junit.jupiter.api.Test;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.routers.Router;
import ru.untitled_devs.core.routers.handlers.Handler;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RouterTest {

    @Test
    void addHandlerAndGetHandler() {
        Router router = new Router();

        Handler handler1 = mock(Handler.class);
        Handler handler2 = mock(Handler.class);

        assertTrue(router.getHandler().isEmpty(), "Handler list should be empty initially");

        router.addHandler(handler1);
        List<Handler> handlersAfterOne = router.getHandler();
        assertEquals(1, handlersAfterOne.size(), "After adding one handler, list size should be 1");
        assertSame(handler1, handlersAfterOne.getFirst(), "First element should be handler1");

        router.addHandler(handler2);
        List<Handler> handlersAfterTwo = router.getHandler();
        assertEquals(2, handlersAfterTwo.size(), "After adding second handler, list size should be 2");
        assertSame(handler1, handlersAfterTwo.get(0), "First element should still be handler1");
        assertSame(handler2, handlersAfterTwo.get(1), "Second element should be handler2");
    }

    @Test
    void getHandlerReturnsUnmodifiableList() {
        Router router = new Router();
        Handler handler = mock(Handler.class);
        router.addHandler(handler);

        List<Handler> handlersView = router.getHandler();

        assertThrows(
                UnsupportedOperationException.class,
                () -> handlersView.add(mock(Handler.class)),
                "Returned handler list should be unmodifiable"
        );
    }

    @Test
    void routeUpdateInvokesHandleWhenCanHandleTrue() {
        Update update = mock(Update.class);
        FSMContext ctx = mock(FSMContext.class);

        Router router = new Router();
        Handler handler = mock(Handler.class);

        when(handler.canHandle(update, ctx)).thenReturn(true);

        router.addHandler(handler);
        router.routeUpdate(update, ctx);

        verify(handler, times(1)).handleUpdate(update, ctx);
    }

    @Test
    void routeUpdateDoesNotInvokeHandleWhenCanHandleFalse() {
        Update update = mock(Update.class);
        FSMContext ctx = mock(FSMContext.class);

        Router router = new Router();
        Handler handler = mock(Handler.class);

        when(handler.canHandle(update, ctx)).thenReturn(false);

        router.addHandler(handler);
        router.routeUpdate(update, ctx);

        verify(handler, never()).handleUpdate(any(Update.class), any(FSMContext.class));
    }

    @Test
    void routeUpdateInvokesOnlyThoseHandlersThatCanHandle() {
        Update update = mock(Update.class);
        FSMContext ctx = mock(FSMContext.class);

        Router router = new Router();

        Handler handlerTrue = mock(Handler.class);
        Handler handlerFalse = mock(Handler.class);

        when(handlerTrue.canHandle(update, ctx)).thenReturn(true);
        when(handlerFalse.canHandle(update, ctx)).thenReturn(false);

        router.addHandler(handlerTrue);
        router.addHandler(handlerFalse);

        router.routeUpdate(update, ctx);

        verify(handlerTrue, times(1)).handleUpdate(update, ctx);
        verify(handlerFalse, never()).handleUpdate(any(Update.class), any(FSMContext.class));
    }
}
