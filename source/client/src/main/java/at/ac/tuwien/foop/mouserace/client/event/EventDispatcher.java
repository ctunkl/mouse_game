package at.ac.tuwien.foop.mouserace.client.event;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class EventDispatcher<T> {
    private final ConcurrentHashMap<T, List<Subscriber>> map;

    public EventDispatcher() {
        this.map = new ConcurrentHashMap<>();
    }

    public void publishEvent(T identifier) {
        map.computeIfPresent(identifier,
                (s, subscribers) -> {
                    subscribers.forEach(subscriber -> subscriber.eventOccurred(null));
                    return subscribers;
                }
        );
    }

    public void publishEvent(T identifier, Object eventObject) {
        map.computeIfPresent(identifier,
                (s, subscribers) -> {
                    subscribers.forEach(subscriber -> subscriber.eventOccurred(eventObject));
                    return subscribers;
                }
        );
    }


    public void subscribeTo(T identifier, Subscriber subscriber) {
        List<Subscriber> previousSubscribers = map.putIfAbsent(identifier,
                Collections.synchronizedList(Collections.singletonList(subscriber)));
        if (previousSubscribers != null) {
            previousSubscribers.add(subscriber);
        }
    }
}
