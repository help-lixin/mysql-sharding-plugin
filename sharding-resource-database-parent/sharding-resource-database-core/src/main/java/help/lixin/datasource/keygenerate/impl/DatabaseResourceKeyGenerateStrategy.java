package help.lixin.datasource.keygenerate.impl;

import help.lixin.datasource.keygenerate.AbstractKeyGenerateStrategy;
import help.lixin.datasource.keygenerate.IKeyGenerateStrategy;
import help.lixin.datasource.model.DatabaseResource;
import help.lixin.resource.ResourceType;

public class DatabaseResourceKeyGenerateStrategy extends AbstractKeyGenerateStrategy implements IKeyGenerateStrategy {
    @Override
    public String generate(Object object) {
        if (null == object || !(object instanceof DatabaseResource)) {
            throw new IllegalArgumentException("生成数据源主键失败,期望请求参数为:DBResourceContextInfo");
        } else {
            DatabaseResource databaseResource = (DatabaseResource) object;
            String instanceName = databaseResource.getInstanceName();
            String dataSourceName = databaseResource.getResourceName();
            ResourceType mode = databaseResource.getResourceType();
            String format = generate0(instanceName, dataSourceName, mode.name());
            return format;
        }
    }
}
