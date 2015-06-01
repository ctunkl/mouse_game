package at.ac.tuwien.foop.mouserace.client.event;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class EventDispatcher {
    private final ConcurrentHashMap<Object, List<Subscriber>> map;

    public EventDispatcher() {
        this.map = new ConcurrentHashMap<>();
    }

    public void publishEvent(Object identifier) {
        map.computeIfPresent(identifier,
                (s, subscribers) -> {
                    subscribers.forEach(Subscriber::eventOccurred);
                    return subscribers;
                }
        );
    }


    public void subscribeTo(Object identifier, Subscriber subscriber) {
        List<Subscriber> previousSubscribers = map.putIfAbsent(identifier,
                Collections.synchronizedList(Collections.singletonList(subscriber)));
        if (previousSubscribers != null) {
            previousSubscribers.add(subscriber);
        }
    }
}
