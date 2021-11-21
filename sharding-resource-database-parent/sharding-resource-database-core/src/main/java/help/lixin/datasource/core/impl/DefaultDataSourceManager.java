package help.lixin.datasource.core.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import help.lixin.datasource.context.DBResourceContextInfo;
import help.lixin.datasource.core.IDataSourceManager;
import help.lixin.datasource.meta.IDataSourceMetaService;
import help.lixin.resource.event.Event;
import help.lixin.resource.listener.IEventListener;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class DefaultDataSourceManager implements IDataSourceManager, IEventListener {

    private IDataSourceMetaService dataSourceMetaService;

    private final Cache<String, List<DataSource>> cache = CacheBuilder
            .newBuilder()
            .build(new CacheLoader<String, List<DataSource>>() {
                @Override
                public List<DataSource> load(String s) throws Exception {

                    return null;
                }
            });

    public DefaultDataSourceManager(IDataSourceMetaService dataSourceMetaService) {
        this.dataSourceMetaService = dataSourceMetaService;
    }

    @Override
    public DataSource getDataSource(DBResourceContextInfo ctx) throws SQLException {
        // 租户ID
        String tenantId = ctx.getTenantId();
        // 数据源的名称
        String dataSourceName = ctx.getDataSourceName();

        return null;
    }

    @Override
    public void onEvent(Event event) {
        // 清空所有的缓存信息
        cache.invalidateAll();
    }
}
