package help.lixin.datasource.model;

import help.lixin.resource.MasterSlave;
import help.lixin.resource.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库资源(唯的标识为: resourceName )
 */
public class DatabaseResource implements Resource {
    // 实例名称(比如:192.168.1.100:3306),仅用于标记
    private String instanceName;
    // 数据源名称(user-service)
    private String resourceName;
    // 资源属于:读写/读
    private MasterSlave mode = MasterSlave.MASTER;
    // 连接池类型(比如:com.alibaba.druid.pool.DruidDataSource)
    private String type;
    // 驱动程序信息(比如:com.mysql.jdbc.Driver)
    private String driver;
    private String url;
    private String username;
    private String password;
    private Map<String, Object> properties = new HashMap<>();
    // 多个slave
    private List<DatabaseResource> slaves = new ArrayList<>(0);

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

    public void setMode(MasterSlave mode) {
        this.mode = mode;
    }

    public MasterSlave getMode() {
        return mode;
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

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setSlaves(List<DatabaseResource> slaves) {
        if (null != slaves) {
            this.slaves = slaves;
        }
    }

    public List<DatabaseResource> getSlaves() {
        return slaves;
    }

    @Override
    public String toString() {
        return "DatabaseResource{" +
                "instanceName='" + instanceName + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", resourceMode=" + mode +
                ", type='" + type + '\'' +
                ", driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", properties=" + properties +
                '}';
    }
}