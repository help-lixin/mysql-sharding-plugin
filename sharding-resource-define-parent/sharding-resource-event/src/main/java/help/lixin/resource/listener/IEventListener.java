package help.lixin.resource.listener;

import help.lixin.resource.event.Event;

/**
 * 事件监听者
 */
public interface IEventListener {
    void onEvent(Event event);
}
