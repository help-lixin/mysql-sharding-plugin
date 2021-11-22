package help.lixin.datasource.config;

import help.lixin.datasource.core.IVirtuaDataSourceDelegator;
import help.lixin.datasource.core.VirtualDataSource;
import help.lixin.datasource.core.impl.VirtuaDataSourceDelegator;
import help.lixin.datasource.customizer.IDataSourceCustomizer;
import help.lixin.datasource.customizer.impl.DruidDataSourceCustomizer;
import help.lixin.datasource.customizer.impl.HikariCPDataSourceCustomizer;
import help.lixin.datasource.manager.IDataSourceController;
import help.lixin.datasource.manager.impl.DefaultDataSourceController;
import help.lixin.datasource.meta.IDataSourceMetaService;
import help.lixin.datasource.meta.impl.CacheDataSourceMetaService;
import help.lixin.datasource.meta.impl.EnvironmentDataSourceMetaService;
import help.lixin.datasource.properties.ShardingResourceProperties;
import help.lixin.resource.event.Event;
import help.lixin.resource.listener.IEventListener;
import help.lixin.resource.publisher.DefaultEventPublisher;
import help.lixin.resource.publisher.IEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Configuration
public class ShardingResourceConfig {

    private Logger logger = LoggerFactory.getLogger(ShardingResourceConfig.class);

    @Bean
    @ConditionalOnMissingBean(name = "druidDataSourceCustomizer")
    public IDataSourceCustomizer druidDataSourceCustomizer() {
        return new DruidDataSourceCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean(name = "hikariCPDataSourceCustomizer")
    public IDataSourceCustomizer hikariCPDataSourceCustomizer() {
        return new HikariCPDataSourceCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean(name = "cacheDataSourceMetaService")
    @Primary
    public IDataSourceMetaService cacheDataSourceMetaService(IDataSourceMetaService environmentDataSourceMetaService) {
        return new CacheDataSourceMetaService(environmentDataSourceMetaService);
    }

    @Bean
    @ConditionalOnMissingBean(name = "environmentDataSourceMetaService")
    public IDataSourceMetaService environmentDataSourceMetaService(ShardingResourceProperties shardingResourceProperties) {
        return new EnvironmentDataSourceMetaService(shardingResourceProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public IDataSourceController dataSourceController(
            IDataSourceMetaService dataSourceMetaService,
            ObjectProvider<List<IDataSourceCustomizer>> dataSourceCustomizers
    ) {
        return new DefaultDataSourceController(dataSourceMetaService, dataSourceCustomizers.getIfAvailable());
    }

    @Bean
    @ConditionalOnMissingBean(name = "virtuaDataSourceDelegator")
    public IVirtuaDataSourceDelegator virtuaDataSourceDelegator() {
        return new VirtuaDataSourceDelegator();
    }

    @Bean
    @ConditionalOnMissingBean(name = "dataSource", value = {DataSource.class})
    public DataSource dataSource(IVirtuaDataSourceDelegator virtuaDataSourceDelegator) {
        return new VirtualDataSource(virtuaDataSourceDelegator);
    }

    @Bean
    @ConditionalOnMissingBean
    public IEventPublisher eventPublisher() {
        return new DefaultEventPublisher();
    }

    @Bean
    @ConditionalOnMissingBean(name = "loggerEventListener")
    public IEventListener loggerEventListener() {
        IEventListener listener = new IEventListener() {
            @Override
            public void onEvent(Event event) {
                logger.info("trace event [{}]", event);
            }
        };
        return listener;
    }

    @Configuration
    public class ConfigurationListener {
        @Autowired
        private IEventPublisher eventPublisher;

        @Autowired(required = false)
        private Optional<List<IEventListener>> listeners = Optional.empty();

        @PostConstruct
        public void init() {
            listeners.ifPresent(consumer -> {
                for (IEventListener listener : consumer) {
                    eventPublisher.subscribe(listener);
                }
            });

        }

        @PreDestroy
        public void destory() {
            listeners.ifPresent(consumer -> {
                for (IEventListener listener : consumer) {
                    eventPublisher.unSubscribe(listener);
                }
            });
        }
    }
}
