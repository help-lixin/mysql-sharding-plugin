package help.lixin.datasource.meta.impl;

import help.lixin.datasource.meta.IDataSourceMetaService;
import help.lixin.datasource.model.DatabaseResource;
import help.lixin.datasource.properties.ShardingResourceProperties;
import help.lixin.resource.constants.Constants;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.List;

public class EnvironmentDataSourceMetaService extends AbstractDataSourceMetaService implements IDataSourceMetaService, EnvironmentAware {

    private Environment environment;

    private ShardingResourceProperties shardingResourceProperties;

    public EnvironmentDataSourceMetaService(ShardingResourceProperties shardingResourceProperties) {
        this.shardingResourceProperties = shardingResourceProperties;
    }

    @Override
    public List<DatabaseResource> getMeta() {
        List<DatabaseResource> resources = Binder.get(environment)
                .bind(Constants.SHARDING_RESOURCE_DATABASES, Bindable.listOf(DatabaseResource.class))
                .get();
        // 重新对业务模型进行赋值和填充.
        shardingResourceProperties.setDatabases(resources);
        return resources;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
