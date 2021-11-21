package help.lixin.resource.publisher;

import help.lixin.resource.event.Event;
import help.lixin.resource.listener.IEventListener;

/**
 * 事件发布管理
 */
public interface IEventPublisher {

    /**
     * 订阅
     *
     * @param eventListener
     */
    boolean subscribe(IEventListener eventListener);

    /**
     * 取消订阅
     *
     * @param eventListener
     */
    boolean unsubscribe(IEventListener eventListener);

    /**
     * 发布事件
     *
     * @param event
     */
    void publish(Event event);
}
