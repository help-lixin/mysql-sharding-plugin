package help.lixin.resource.event;

/**
 * 配置刷新事件
 */
public class ResourceRefreshEvent implements Event {
    private String name;

    public ResourceRefreshEvent(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ResourceConfigRefreshEvent{" +
                "name='" + name + '\'' +
                '}';
    }
}
