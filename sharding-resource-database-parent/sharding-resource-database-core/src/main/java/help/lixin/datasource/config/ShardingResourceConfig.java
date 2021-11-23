package help.lixin.datasource.config;

import help.lixin.datasource.core.IVirtuaDataSourceDelegator;
import help.lixin.datasource.core.VirtualDataSource;
import help.lixin.datasource.core.impl.VirtuaDataSourceDelegator;
import help.lixin.datasource.customizer.IDataSourceCustomizer;
import help.lixin.datasource.customizer.impl.DruidDataSourceCustomizer;
import help.lixin.datasource.customizer.impl.HikariCPDataSourceCustomizer;
import help.lixin.datasource.keygenerate.IKeyGenerateStrategy;
import help.lixin.datasource.keygenerate.impl.ContextKeyGenerateStrategy;
import help.lixin.datasource.keygenerate.impl.DatabaseResourceKeyGenerateStrategy;
import help.lixin.datasource.manager.IDataSourceController;
import help.lixin.datasource.manager.IDataSourceInitController;
import help.lixin.datasource.manager.impl.DataSourceInitController;
import help.lixin.datasource.manager.impl.DefaultDataSourceController;
import help.lixin.datasource.manager.store.DefaultDataSourceStore;
import help.lixin.datasource.manager.store.IDataSourceStore;
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
import org.springframework.beans.factory.annotation.Qualifier;
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

    /**
     * 基于:DatabaseResource对象,生成key
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "databaseResourceKeyGenerateStrategy")
    public IKeyGenerateStrategy databaseResourceKeyGenerateStrategy() {
        return new DatabaseResourceKeyGenerateStrategy();
    }

    /**
     * 基于:Context对象,生成key
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "contextKeyGenerateStrategy")
    public IKeyGenerateStrategy contextKeyGenerateStrategy() {
        return new ContextKeyGenerateStrategy();
    }


    /**
     * 对HikariCPDataSource进行自定义
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "hikariCPDataSourceCustomizer")
    public IDataSourceCustomizer hikariCPDataSourceCustomizer() {
        return new HikariCPDataSourceCustomizer();
    }

    /**
     * 对DruidDataSource进行定义
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "druidDataSourceCustomizer")
    public IDataSourceCustomizer druidDataSourceCustomizer() {
        return new DruidDataSourceCustomizer();
    }


    /**
     * 所有的数据源最终存储的介质(Map)
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public IDataSourceStore dataSourceStore() {
        return new DefaultDataSourceStore();
    }

    /**
     * 元数据服务缓存对象
     *
     * @param environmentDataSourceMetaService
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "cacheDataSourceMetaService")
    @Primary
    public IDataSourceMetaService cacheDataSourceMetaService(IDataSourceMetaService environmentDataSourceMetaService) {
        return new CacheDataSourceMetaService(environmentDataSourceMetaService);
    }

    /**
     * 解析环境变量到元数据服务中心
     *
     * @param shardingResourceProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "environmentDataSourceMetaService")
    public IDataSourceMetaService environmentDataSourceMetaService(ShardingResourceProperties shardingResourceProperties) {
        return new EnvironmentDataSourceMetaService(shardingResourceProperties);
    }

    /**
     * 数据源的初始化处理
     *
     * @param dataSourceMetaService
     * @param databaseResourceKeyGenerateStrategy
     * @param dataSourceStore
     * @param dataSourceCustomizers
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public IDataSourceInitController dataSourceInitController(
            IDataSourceMetaService dataSourceMetaService,
            @Autowired
            @Qualifier("databaseResourceKeyGenerateStrategy")
                    IKeyGenerateStrategy databaseResourceKeyGenerateStrategy,
            IDataSourceStore dataSourceStore,
            ObjectProvider<List<IDataSourceCustomizer>> dataSourceCustomizers) {
        return new DataSourceInitController(dataSourceMetaService, databaseResourceKeyGenerateStrategy, dataSourceStore, dataSourceCustomizers.getIfAvailable());
    }

    /**
     * 数据源的获取控制
     *
     * @param contextKeyGenerateStrategy
     * @param dataSourceStore
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public IDataSourceController dataSourceController(
            @Autowired
            @Qualifier("contextKeyGenerateStrategy")
                    IKeyGenerateStrategy contextKeyGenerateStrategy,
            IDataSourceStore dataSourceStore) {
        return new DefaultDataSourceController(contextKeyGenerateStrategy, dataSourceStore);
    }

    /**
     * 虚拟数据源代理
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "virtuaDataSourceDelegator")
    public IVirtuaDataSourceDelegator virtuaDataSourceDelegator(IDataSourceController dataSourceController) {
        return new VirtuaDataSourceDelegator(dataSourceController);
    }

    /**
     * 数据源代理
     *
     * @param virtuaDataSourceDelegator
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "dataSource", value = {DataSource.class})
    public DataSource dataSource(IVirtuaDataSourceDelegator virtuaDataSourceDelegator) {
        return new VirtualDataSource(virtuaDataSourceDelegator);
    }

    /**
     * 事件发布者
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public IEventPublisher eventPublisher() {
        return new DefaultEventPublisher();
    }

    /**
     * 事件监听者
     *
     * @return
     */
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

    /**
     * 配置系统启动时,初始化所有的数据源
     */
    @Configuration
    public class ConfigurationDataSourceInit {
        @Autowired
        private IDataSourceInitController dataSourceInitController;

        @PostConstruct
        public void init() {
            dataSourceInitController.initDataSources();
        }
    }

    /**
     * 配置监听者与发布者的关系
     */
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
