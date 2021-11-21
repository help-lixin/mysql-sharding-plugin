package help.lixin.datasource.model;

import help.lixin.datasource.DriverType;
import help.lixin.resource.ResourceType;
import help.lixin.resource.model.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 数据库资源(唯的标识为: resourceName )
 */
public class DatabaseResource implements Resource {
    // 实例名称(比如:192.168.1.100:3306),仅用于标记
    private String instanceName;
    // 资源名称(user-service)
    private String resourceName;
    // 资源属于master还是slave
    private ResourceType resourceType = ResourceType.MASTER;
    // 驱动程序工厂
    private DriverType driverType = DriverType.DRUID;
    // 驱动程序信息
    private String driver;
    private String url;
    private String username;
    private String password;
    private Map<String, String> properties = new HashMap<>();

    @Override
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String getResourceName() {
        return resourceName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public DriverType getDriverType() {
        return driverType;
    }

    public void setDriverType(DriverType driverType) {
        this.driverType = driverType;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "DatabaseResource{" +
                "instanceName='" + instanceName + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", resourceType=" + resourceType +
                ", driverType=" + driverType +
                ", driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", properties=" + properties +
                '}';
    }
}