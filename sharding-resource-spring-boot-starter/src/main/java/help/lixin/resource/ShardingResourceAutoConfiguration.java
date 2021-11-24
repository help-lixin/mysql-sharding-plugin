package help.lixin.resource;

import help.lixin.datasource.config.ShardingResourceConfig;
import help.lixin.resource.constants.Constants;
import help.lixin.datasource.properties.ShardingResourceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ShardingResourceProperties.class)
@ImportAutoConfiguration({ShardingResourceConfig.class})
@ConditionalOnProperty(prefix = Constants.SHARDING_RESOURCE_KEY, name = Constants.ENABLED, havingValue = "true", matchIfMissing = false)
public class ShardingResourceAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(ShardingResourceAutoConfiguration.class);

    private ShardingResourceProperties shardingResourceProperties;

    public ShardingResourceAutoConfiguration(ShardingResourceProperties shardingResourceProperties) {
        this.shardingResourceProperties = shardingResourceProperties;
    }

    {
        if (logger.isDebugEnabled()) {
            logger.debug("enabled Module [{}] SUCCESS.", ShardingResourceAutoConfiguration.class.getSimpleName());
        }
    }

}
