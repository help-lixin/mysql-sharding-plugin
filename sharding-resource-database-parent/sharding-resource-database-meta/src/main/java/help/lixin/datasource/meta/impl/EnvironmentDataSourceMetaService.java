package help.lixin.datasource.meta.impl;

import help.lixin.datasource.meta.IDataSourceMetaService;
import help.lixin.datasource.model.DatabaseResource;
import help.lixin.resource.constants.Constants;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.List;

// TODO lixin 站在分层的架构上一说:在这个时候就已经依赖了Spring了,总觉得不太友好,后期,再想办法进行解藕.
public class EnvironmentDataSourceMetaService implements IDataSourceMetaService, EnvironmentAware {

    private Environment environment;

    @Override
    public List<DatabaseResource> getMeta() {
        List<DatabaseResource> resources = Binder.get(environment)
                .bind(Constants.SHARDING_RESOURCE_DATABASES, Bindable.listOf(DatabaseResource.class))
                .get();
        return resources;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
