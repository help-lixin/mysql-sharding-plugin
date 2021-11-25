package help.lixin.datasource.properties;

import help.lixin.datasource.OverrideSQLMode;
import help.lixin.datasource.model.DatabaseResource;
import help.lixin.resource.constants.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.*;

@ConfigurationProperties(prefix = Constants.SHARDING_RESOURCE_KEY)
public class ShardingResourceProperties {
    private boolean enabled = false;

    // 所属环境(比如:XXX公司),多个之间用逗号分隔
    private String env;
    private List<String> envs = new ArrayList(0);
    // 重写SQL的切入点期望在哪?为什么要对MyBatis进行扩展?因为重写后的SQL通过mybatis日志是看不出来的,主要是不要让控制台的SQL误导了开发人员.
    private String overrideMode = OverrideSQLMode.MYBATIS.name();
    // 微服务名称
    @Value("${spring.application.name:}")
    private String microServiceName;
    // db资源
    private List<DatabaseResource> databases = new ArrayList<>(0);

    public void setEnvs(List<String> envs) {
        this.envs = envs;
    }

    public List<String> getEnvs() {
        return envs;
    }

    public void setOverrideMode(String overrideMode) {
        this.overrideMode = overrideMode;
    }

    public String getOverrideMode() {
        return overrideMode;
    }

    public void setEnv(String env) {
        this.env = env;
        if (null != env) {
            String[] strings = env.split(",");
            List<String> stringList = Arrays.asList(strings);
            envs.addAll(stringList);
        }
    }

    public String getEnv() {
        return env;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getMicroServiceName() {
        return microServiceName;
    }

    public void setMicroServiceName(String microServiceName) {
        this.microServiceName = microServiceName;
    }

    public List<DatabaseResource> getDatabases() {
        return databases;
    }

    public void setDatabases(List<DatabaseResource> databases) {
        this.databases = databases;
    }
}
