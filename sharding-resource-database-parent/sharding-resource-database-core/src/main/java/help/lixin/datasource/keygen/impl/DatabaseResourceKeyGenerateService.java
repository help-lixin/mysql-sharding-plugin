package help.lixin.datasource.keygen.impl;

import help.lixin.datasource.keygen.AbstractKeyGenerateService;
import help.lixin.datasource.keygen.IKeyGenerateService;
import help.lixin.datasource.model.DatabaseResource;
import help.lixin.resource.MasterSlave;

public class DatabaseResourceKeyGenerateService extends AbstractKeyGenerateService implements IKeyGenerateService {
    @Override
    public String generate(Object object) {
        if (null == object || !(object instanceof DatabaseResource)) {
            throw new IllegalArgumentException("生成数据源主键失败,期望请求参数为:DBResourceContextInfo");
        } else {
            DatabaseResource databaseResource = (DatabaseResource) object;
            String instanceName = databaseResource.getInstanceName();
            String dataSourceName = databaseResource.getResourceName();
            MasterSlave mode = databaseResource.getMode();
            String format = generate0(instanceName);
            return format;
        }
    }
}
