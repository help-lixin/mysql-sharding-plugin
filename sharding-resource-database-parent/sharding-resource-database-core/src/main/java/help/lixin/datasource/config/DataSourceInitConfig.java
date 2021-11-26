package help.lixin.datasource.config;

import help.lixin.datasource.keygen.IKeyGenerateService;
import help.lixin.datasource.keygen.impl.DatabaseResourceKeyGenerateService;
import help.lixin.datasource.meta.IDataSourceMetaService;
import help.lixin.datasource.meta.impl.CacheDataSourceMetaService;
import help.lixin.datasource.meta.impl.EnvironmentDataSourceMetaService;
import help.lixin.datasource.properties.ShardingResourceProperties;
import help.lixin.datasource.service.init.IDataSourceInitService;
import help.lixin.datasource.service.init.customizer.IDataSourceCustomizer;
import help.lixin.datasource.service.init.customizer.impl.DruidDataSourceCustomizer;
import help.lixin.datasource.service.init.customizer.impl.HikariCPDataSourceCustomizer;
import help.lixin.datasource.service.init.impl.DataSourceInitService;
import help.lixin.datasource.service.store.IDataSourceStoreService;
import help.lixin.datasource.service.store.impl.DefaultDataSourceStoreService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 数据源初始化时配置
 */
@Configuration
public class DataSourceInitConfig {

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
     * 解析环境变量转换成业务模型为:元数据
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
     * 数据源的初始化处理
     *
     * @param dataSourceMetaService
     * @param databaseResourceKeyGenerateService
     * @param dataSourceStoreService
     * @param dataSourceCustomizers
     * @return
     */
    @Bean(initMethod = "initDataSources")
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
}
