package help.lixin.datasource.config;

import help.lixin.datasource.aop.BindResourceContextAspect;
import help.lixin.datasource.core.IVirtuaDataSourceDelegator;
import help.lixin.datasource.VirtualDataSource;
import help.lixin.datasource.core.impl.VirtuaDataSourceDelegator;
import help.lixin.datasource.service.init.customizer.IDataSourceCustomizer;
import help.lixin.datasource.service.init.customizer.impl.DruidDataSourceCustomizer;
import help.lixin.datasource.service.init.customizer.impl.HikariCPDataSourceCustomizer;
import help.lixin.datasource.keygen.IKeyGenerateService;
import help.lixin.datasource.keygen.impl.ContextKeyGenerateService;
import help.lixin.datasource.keygen.impl.DatabaseResourceKeyGenerateService;
import help.lixin.datasource.service.loadbalancer.ILoadBalancerDataSourceService;
import help.lixin.datasource.service.init.IDataSourceInitService;
import help.lixin.datasource.service.init.impl.DataSourceInitService;
import help.lixin.datasource.service.loadbalancer.IRuleService;
import help.lixin.datasource.service.loadbalancer.impl.DefaultLoadBalancerDataSourceService;
import help.lixin.datasource.mybatis.MyBatisConfigurationCustomizer;
import help.lixin.datasource.service.loadbalancer.impl.DefaultRuleService;
import help.lixin.datasource.service.store.impl.DefaultDataSourceStoreService;
import help.lixin.datasource.service.store.IDataSourceStoreService;
import help.lixin.datasource.meta.IDataSourceMetaService;
import help.lixin.datasource.meta.impl.CacheDataSourceMetaService;
import help.lixin.datasource.meta.impl.EnvironmentDataSourceMetaService;
import help.lixin.datasource.properties.ShardingResourceProperties;
import help.lixin.datasource.build.context.DefaultBuildResourceContextService;
import help.lixin.datasource.build.context.impl.TransactionalResourceContextCustomizer;
import help.lixin.resource.event.Event;
import help.lixin.resource.listener.IEventListener;
import help.lixin.resource.publisher.DefaultEventPublisher;
import help.lixin.resource.publisher.IEventPublisher;
import help.lixin.resource.route.IResourceContextCustomizer;
import help.lixin.resource.route.IBuildResourceContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;

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
    @ConditionalOnMissingBean(name = "databaseResourceKeyGenerateService")
    public IKeyGenerateService databaseResourceKeyGenerateService() {
        return new DatabaseResourceKeyGenerateService();
    }

    /**
     * 基于:Context对象,生成key
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "contextKeyGenerateService")
    public IKeyGenerateService contextKeyGenerateService() {
        return new ContextKeyGenerateService();
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
    public IDataSourceStoreService dataSourceStoreService() {
        return new DefaultDataSourceStoreService();
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
     * @param databaseResourceKeyGenerateService
     * @param dataSourceStoreService
     * @param dataSourceCustomizers
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public IDataSourceInitService dataSourceInitService(
            IDataSourceMetaService dataSourceMetaService,
            @Autowired
            @Qualifier("databaseResourceKeyGenerateService")
                    IKeyGenerateService databaseResourceKeyGenerateService,
            IDataSourceStoreService dataSourceStoreService,
            ObjectProvider<List<IDataSourceCustomizer>> dataSourceCustomizers) {
        return new DataSourceInitService(dataSourceMetaService, databaseResourceKeyGenerateService, dataSourceStoreService, dataSourceCustomizers.getIfAvailable());
    }


    @Bean
    @ConditionalOnMissingBean(name = "defaultRuleService")
    public IRuleService ruleService(
            @Autowired
            @Qualifier("contextKeyGenerateService")
                    IKeyGenerateService contextKeyGenerateService,
            IDataSourceStoreService dataSourceStore) {
        return new DefaultRuleService(contextKeyGenerateService, dataSourceStore);
    }

    /**
     * 数据源负载均衡
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public ILoadBalancerDataSourceService loadBalancerDataSourceService(IRuleService ruleService) {
        return new DefaultLoadBalancerDataSourceService(ruleService);
    }

    /**
     * 虚拟数据源代理
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "virtuaDataSourceDelegator")
    public IVirtuaDataSourceDelegator virtuaDataSourceDelegator(ILoadBalancerDataSourceService loadBalancerDataSourceService) {
        return new VirtuaDataSourceDelegator(loadBalancerDataSourceService);
    }

    /**
     * 数据源代理
     *
     * @param virtuaDataSourceDelegator
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "dataSource", value = {DataSource.class})
    public DataSource dataSource(IVirtuaDataSourceDelegator virtuaDataSourceDelegator, ShardingResourceProperties shardingResourceProperties) {
        return new VirtualDataSource(virtuaDataSourceDelegator, shardingResourceProperties);
    }

    @Bean
    @ConditionalOnMissingBean(name = "buildResourceContextService")
    public IBuildResourceContextService buildResourceContextService(ObjectProvider<List<IResourceContextCustomizer>> customizers) {
        return new DefaultBuildResourceContextService(customizers.getIfAvailable());
    }

    /**
     * 资源上下文自定义
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "transactionalResourceContextCustomizer")
    public IResourceContextCustomizer transactionalResourceContextCustomizer() {
        return new TransactionalResourceContextCustomizer();
    }


    /**
     * 绑定上下文切面
     *
     * @return
     */
    @Bean
    public BindResourceContextAspect bindResourceContextAspect() {
        return new BindResourceContextAspect();
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

    @Bean
    public MyBatisConfigurationCustomizer myBatisConfigurationCustomizer() {
        return new MyBatisConfigurationCustomizer();
    }

    /**
     * 配置系统启动时,初始化所有的数据源
     */
    @Configuration
    public class ConfigurationDataSourceInit {
        @Autowired
        private IDataSourceInitService dataSourceInitService;

        @PostConstruct
        public void init() {
            dataSourceInitService.initDataSources();
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
