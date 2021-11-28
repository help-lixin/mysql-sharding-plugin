package help.lixin.datasource.service.loadbalancer.impl;

import help.lixin.datasource.WrapperDataSourceMeta;
import help.lixin.datasource.context.DBResourceContext;
import help.lixin.datasource.keygen.IKeyGenerateService;
import help.lixin.datasource.service.loadbalancer.IRuleService;
import help.lixin.datasource.service.store.IDataSourceStoreService;
import help.lixin.resource.MasterSlave;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 默认选择数据源的规则是:master只允许有一个.slave是轮询选择.
 */
public class DefaultRuleService implements IRuleService {
    // key生成
    private IKeyGenerateService contextKeyGenerateService;

    // 数据源存储中介中心
    private IDataSourceStoreService dataSourceStore;

    private final AtomicInteger atomicCount = new AtomicInteger(0);

    public DefaultRuleService(
            IKeyGenerateService contextKeyGenerateService,
            IDataSourceStoreService dataSourceStore) {
        this.dataSourceStore = dataSourceStore;
        this.contextKeyGenerateService = contextKeyGenerateService;
    }

    @Override
    public Optional<DataSource> choose(DBResourceContext ctx) {
        String key = contextKeyGenerateService.generate(ctx);
        List<WrapperDataSourceMeta> dataSources = dataSourceStore.getDataSources(key);
        // 至少要有一个数据源
        if (null != dataSources && !dataSources.isEmpty() && dataSources.size() > 0) {
            // 只配了一个数据源,就不要去判断读写分离的数据源了
            if (dataSources.size() == 1) {
                return Optional.of(dataSources.get(0));
            }

            // 是否为只读(slave模式)
            boolean readOnly = ctx.isReadOnly();
            // 过滤出所有的master数据源
            List<WrapperDataSourceMeta> masters = dataSources.stream()
                    .filter(ds -> ds.getMetaDatabaseResource().getMode().name().equalsIgnoreCase(MasterSlave.MASTER.name()))
                    .collect(Collectors.toList());
            // 过滤出所有的slave数据源
            List<WrapperDataSourceMeta> slaves = dataSources.stream()
                    .filter(ds -> ds.getMetaDatabaseResource().getMode().name().equalsIgnoreCase(MasterSlave.SLAVE.name()))
                    .collect(Collectors.toList());
            if (!readOnly) { // 读写模式
                return Optional.of(masters.get(0));
            } else { // 只读模式
                // 没有配置slave的情况下,退而其次选择master
                if (slaves.size() > 0) {
                    int count = atomicCount.getAndIncrement();
                    int index = count % slaves.size();
                    return Optional.of(slaves.get(index));
                } else {
                    return Optional.of(masters.get(0));
                }
            }
        }
        return Optional.empty();
    }
}
