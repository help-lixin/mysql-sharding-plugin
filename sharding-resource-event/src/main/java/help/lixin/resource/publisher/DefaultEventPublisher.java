package help.lixin.resource.publisher;

import help.lixin.resource.event.Event;
import help.lixin.resource.listener.IEventListener;

import java.util.HashSet;
import java.util.Set;

/**
 * 事件管理的默认实现
 */
public class DefaultEventPublisher implements IEventPublisher {

    private final Set<IEventListener> eventListeners = new HashSet<>();

    @Override
    public boolean subscribe(IEventListener eventListener) {
        return eventListeners.add(eventListener);
    }

    @Override
    public boolean unSubscribe(IEventListener eventListener) {
        return eventListeners.remove(eventListener);
    }

    @Override
    public void publish(Event event) {
        eventListeners.forEach(listener -> {
            listener.onEvent(event);
        });
    }
}
