package help.lixin.datasource.keygen.impl;

import help.lixin.datasource.context.DBResourceContext;
import help.lixin.datasource.keygen.AbstractKeyGenerateService;
import help.lixin.datasource.keygen.IKeyGenerateService;
import help.lixin.resource.ResourceMode;

/**
 * 基于上下文生成key
 */
public class ContextKeyGenerateService extends AbstractKeyGenerateService implements IKeyGenerateService {
    @Override
    public String generate(Object obj) {
        if (null == obj || !(obj instanceof DBResourceContext)) {
            throw new IllegalArgumentException("生成数据源主键失败,期望请求参数为:DBResourceContextInfo");
        } else {
            DBResourceContext ctx = (DBResourceContext) obj;
            // 租户ID
            String tenantId = ctx.getTenantId();
            // 数据库实例(192.168.1.100:3306)
            String instanceName = ctx.getInstanceName();
            // 数据源的名称
            String dataSourceName = ctx.getDataSourceName();
            // 是否为只读
            boolean readOnly = ctx.isReadOnly();
            // 模式
            ResourceMode mode = readOnly ? ResourceMode.R : ResourceMode.RW;
            String format = generate0(instanceName);
            return format;
        }
    }
}
