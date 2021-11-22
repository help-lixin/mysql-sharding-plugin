package help.lixin.resource.bindings;

import help.lixin.datasource.context.DBResourceContextInfo;
import help.lixin.resource.context.ResourceContext;
import help.lixin.resource.context.ResourceContextInfo;

import java.util.Map;

public class MyBatisBindingsVariable implements IBindingsVariable {
    @Override
    public void bindings(Map<String, Object> bindings) {
        // 增加从线程上下文获得变量信息
        // lixin 2021/11/21
        ResourceContextInfo tmp = ResourceContext.get();
        if (null != tmp && tmp instanceof DBResourceContextInfo
                && !bindings.containsKey("database")
                && !bindings.containsKey("tablePrefix")
        ) {
            DBResourceContextInfo ctx = (DBResourceContextInfo) tmp;
            String tenantId = ctx.getTenantId();
            String database = ctx.getDatabase();
            String tablePrefix = ctx.getTablePrefix();
            String microServiceName = ctx.getMicroServiceName();
            Map<String, String> properties = ctx.getProperties();

            bindings.put("tenantId", tenantId);
            bindings.put("database", database);
            bindings.put("tablePrefix", tablePrefix);
            bindings.put("microServiceName", microServiceName);

            properties.forEach((key, value) -> {
                bindings.put(key, value);
            });
        }
    }
}
