package help.lixin.datasource.config;

import help.lixin.datasource.VirtualDataSource;
import help.lixin.datasource.build.context.DefaultBuildResourceContextService;
import help.lixin.datasource.build.context.impl.TransactionalResourceContextCustomizer;
import help.lixin.datasource.core.IVirtuaDataSourceDelegator;
import help.lixin.datasource.core.impl.VirtuaDataSourceDelegator;
import help.lixin.datasource.keygen.IKeyGenerateService;
import help.lixin.datasource.keygen.impl.ContextKeyGenerateService;
import help.lixin.datasource.properties.ShardingResourceProperties;
import help.lixin.datasource.service.loadbalancer.ILoadBalancerDataSourceService;
import help.lixin.datasource.service.loadbalancer.IRuleService;
import help.lixin.datasource.service.loadbalancer.impl.DefaultLoadBalancerDataSourceService;
import help.lixin.datasource.service.loadbalancer.impl.DefaultRuleService;
import help.lixin.datasource.service.store.IDataSourceStoreService;
import help.lixin.resource.route.IBuildResourceContextService;
import help.lixin.resource.route.IResourceContextCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

/**
 * 对数据源进行负载均衡配置
 */
@Configuration
public class DataSourceLoadBalancerConfig {

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
}
