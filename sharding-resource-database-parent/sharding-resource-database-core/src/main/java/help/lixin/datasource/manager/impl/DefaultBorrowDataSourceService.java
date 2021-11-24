package help.lixin.datasource.manager.impl;

import help.lixin.datasource.context.DBResourceContext;
import help.lixin.datasource.WrapperDataSourceMeta;
import help.lixin.datasource.keygen.IKeyGenerateService;
import help.lixin.datasource.manager.IBorrowDataSourceService;
import help.lixin.datasource.store.IDataSourceStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * 总体思想是这样的:<br/>
 * 1. 应用启动时做以下几件事情:<br/>
 * &nbsp;&nbsp; 1.1 读取配置信息(本地/网络),转换成业务模型(DatabaseResource),并根据DatabaseResource创建DataSource.<br/>
 * &nbsp;&nbsp; 1.2 启动一个后台线程,监听配置的变化,当配置有变化时.更新DataSource信息,注意:仅仅做局部更新(或新增),禁止销毁所有的连接. <br/>
 */
public class DefaultBorrowDataSourceService implements IBorrowDataSourceService {

    private Logger logger = LoggerFactory.getLogger(DefaultBorrowDataSourceService.class);

    // key生成策略
    private IKeyGenerateService contextKeyGenerateService;

    // 数据源存储中介中心
    private IDataSourceStoreService dataSourceStore;

    public DefaultBorrowDataSourceService(
            IKeyGenerateService contextKeyGenerateService,
            IDataSourceStoreService dataSourceStore) {
        this.dataSourceStore = dataSourceStore;
        this.contextKeyGenerateService = contextKeyGenerateService;
    }

    @Override
    public Optional<DataSource> getDataSource(DBResourceContext ctx) throws SQLException {
        String key = contextKeyGenerateService.generate(ctx);
        List<WrapperDataSourceMeta> dataSources = dataSourceStore.getDataSources(key);
        // 至少要有一个数据源
        if (null != dataSources && !dataSources.isEmpty() && dataSources.size() > 0) {
            // 是否为只读
            boolean readOnly = ctx.isReadOnly();
            if (!readOnly) { // 读写模式
                return Optional.of(dataSources.get(0));
            } else { // 只读模式
                // TODO lixin 此处应该要扔出一个策略模式接口出去,后续再做扩展.
                Random random = new Random();
                int index = random.nextInt(dataSources.size());
                return Optional.of(dataSources.get(index));
            }
        }
        return Optional.empty();
    }
}