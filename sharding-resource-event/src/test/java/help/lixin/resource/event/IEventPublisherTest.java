package help.lixin.resource.event;

import help.lixin.resource.listener.IEventListener;
import help.lixin.resource.publisher.DefaultEventPublisher;
import help.lixin.resource.publisher.IEventPublisher;
import org.junit.Test;

public class IEventPublisherTest {

    @Test
    public void testPublish() {
        // 1. 定义两个监听器
        IEventListener listener1 = new IEventListener() {
            @Override
            public void onEvent(Event event) {
                System.out.println(event);
            }
        };

        IEventListener listener2 = new IEventListener() {
            @Override
            public void onEvent(Event event) {
                System.out.println(event);
            }
        };

        // 2. 创建发布者,并关联发布者
        IEventPublisher publisher = new DefaultEventPublisher();
        publisher.subscribe(listener1);
        publisher.subscribe(listener2);

        TestEvent event = new TestEvent();
        event.setName("张三");

        // 3. 发布事件
        publisher.publish(event);
    }
}

class TestEvent implements Event {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "TestEvent{" +
                "name='" + name + '\'' +
                '}';
    }
}
