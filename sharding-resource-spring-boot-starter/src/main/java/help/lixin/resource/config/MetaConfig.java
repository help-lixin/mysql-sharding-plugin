package help.lixin.resource.config;

import help.lixin.datasource.meta.IDataSourceMetaService;
import help.lixin.datasource.meta.impl.CacheDataSourceMetaService;
import help.lixin.datasource.meta.impl.EnvironmentDataSourceMetaService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MetaConfig {

    @Bean
    @ConditionalOnBean
    public IDataSourceMetaService environmentDataSourceMetaService() {
        IDataSourceMetaService dataSourceMetaService = new EnvironmentDataSourceMetaService();
        return dataSourceMetaService;
    }

    @Bean
    @ConditionalOnBean
    @Primary
    public IDataSourceMetaService cacheDataSourceMetaService(IDataSourceMetaService environmentDataSourceMetaService) {
        IDataSourceMetaService dataSourceMetaService = new CacheDataSourceMetaService(environmentDataSourceMetaService);
        return dataSourceMetaService;
    }
}
