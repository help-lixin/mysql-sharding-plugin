package help.lixin.resource.bindings;

import help.lixin.datasource.context.DBResourceContext;
import help.lixin.resource.context.ResourceContextHolder;
import help.lixin.resource.context.ResourceContext;

import java.util.Map;

public class MyBatisBindingsVariable implements IBindingsVariable {
    @Override
    public void bindings(Map<String, Object> bindings) {
        // 增加从线程上下文获得变量信息
        // lixin 2021/11/21
        ResourceContext tmp = ResourceContextHolder.get();
        if (null != tmp && tmp instanceof DBResourceContext
                && !bindings.containsKey("database")
                && !bindings.containsKey("tablePrefix")
        ) {
            DBResourceContext ctx = (DBResourceContext) tmp;
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
