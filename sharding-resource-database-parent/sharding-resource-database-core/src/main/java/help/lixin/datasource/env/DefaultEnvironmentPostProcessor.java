package help.lixin.datasource.env;

import help.lixin.datasource.VirtualDataSource;
import help.lixin.resource.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * 添加默认的环境变量信息,否则,Spring容器启动时,会报错.
 * spring:
 * datasource:
 * type: help.lixin.datasource.VirtualDataSource
 * url: jdbc:virtdatasource://localhost:3306
 * driver-class-name: com.mysql.jdbc.Driver
 */
public class DefaultEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private Logger logger = LoggerFactory.getLogger(DefaultEnvironmentPostProcessor.class);

    private static final String DATA_SOURCE_TYPE_KEY = "spring.datasource.type";
    private static final String DATA_SOURCE_TYPE_VALUE = VirtualDataSource.class.getName();

    private static final String DATA_SOURCE_URL_KEY = "spring.datasource.url";
    private static final String DATA_SOURCE_URL_VALUE = "jdbc:virtdatasource://localhost:3306";

    private static final String DATA_SOURCE_DRIVER_KEY = "spring.datasource.driver-class-name";
    // 此处写成mysql,后期要做修改 TODO lixin
    private static final String DATA_SOURCE_DRIVER_VALUE = "com.mysql.jdbc.Driver";

    private final String name = "_shardingResourceEnvironment";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Boolean isEnabledRoute = environment.getProperty(Constants.SHARDING_RESOURCE_KEY + "." + Constants.ENABLED, Boolean.class);
        if (null != isEnabledRoute && isEnabledRoute) {
            Map<String, Object> shardingResourceEnvironment = new HashMap<>();
            shardingResourceEnvironment.put(DATA_SOURCE_TYPE_KEY, DATA_SOURCE_TYPE_VALUE);
            shardingResourceEnvironment.put(DATA_SOURCE_URL_KEY, DATA_SOURCE_URL_VALUE);
            shardingResourceEnvironment.put(DATA_SOURCE_DRIVER_KEY, DATA_SOURCE_DRIVER_VALUE);
            PropertySource<Map<String, Object>> propertySource = new MapPropertySource(name, shardingResourceEnvironment);
            environment.getPropertySources().addFirst(propertySource);
        }
    }
}
