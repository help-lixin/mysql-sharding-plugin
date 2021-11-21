package help.lixin.resource.model;

import java.util.Map;

/**
 * 定义所有(MQ/Redis)的资源接口
 */
public interface Resource {
    /**
     * 设置资源名称
     */
    public void setResourceName(String resourceName);

    /**
     * 获取资源名称
     *
     * @return
     */
    public String getResourceName();

    /**
     * 设置其它的变量信息
     *
     * @return
     */
    public Map<String, String> getProperties();

    /**
     * 获取其它变量信息
     *
     * @param vars
     */
    public void setProperties(Map<String, String> vars);
}
