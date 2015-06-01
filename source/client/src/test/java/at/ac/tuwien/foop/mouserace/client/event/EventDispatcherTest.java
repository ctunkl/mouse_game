package at.ac.tuwien.foop.mouserace.client.event;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EventDispatcherTest {

    private EventDispatcher dispatcher;

    @Before
    public void setUp() throws Exception {
        dispatcher = new EventDispatcher();
    }

    @Test
    public void testNotificationOnce() throws Exception {
        Subscriber mock = mock(Subscriber.class);

        dispatcher.subscribeTo("event", mock);
        dispatcher.publishEvent("event");

        verify(mock, times(1)).eventOccurred();
    }
    @Test
    public void testNoNotificationTwice() throws Exception {
        Subscriber mock = mock(Subscriber.class);

        dispatcher.subscribeTo("event", mock);
        dispatcher.publishEvent("event");
        dispatcher.publishEvent("event");

        verify(mock, times(2)).eventOccurred();
    }

    @Test
    public void testNoNotification() throws Exception {
        Subscriber mock = mock(Subscriber.class);

        dispatcher.subscribeTo("event", mock);
        dispatcher.publishEvent("event1");

        verify(mock, never()).eventOccurred();
    }
}