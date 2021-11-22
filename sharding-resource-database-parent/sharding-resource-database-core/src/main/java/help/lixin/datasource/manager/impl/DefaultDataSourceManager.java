package help.lixin.datasource.manager.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import help.lixin.datasource.context.DBResourceContextInfo;
import help.lixin.datasource.manager.IDataSourceManager;
import help.lixin.datasource.meta.IDataSourceMetaService;
import help.lixin.resource.event.Event;
import help.lixin.resource.listener.IEventListener;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * 总体思想是这样的:<br/>
 * 1. 应用启动时做以下几件事情:<br/>
 * &nbsp;&nbsp; 1.1 读取配置信息(本地/网络),转换成业务模型(DatabaseResource),并根据DatabaseResource创建DataSource.<br/>
 * &nbsp;&nbsp; 1.2 启动一个后台线程,监听配置的变化,当配置有变化时.更新DataSource信息,注意:仅仅做局部更新(或新增),禁止销毁所有的连接. <br/>
 */
public class DefaultDataSourceManager implements IDataSourceManager, IEventListener {

    private IDataSourceMetaService dataSourceMetaService;

    /**
     * key:   instanceName +  dataSourceName <br/>
     * value: List<DataSource> <br/>
     */
    private final Cache<String, List<DataSource>> cache = CacheBuilder
            .newBuilder()
            .build();

    public DefaultDataSourceManager(IDataSourceMetaService dataSourceMetaService) {
        this.dataSourceMetaService = dataSourceMetaService;
    }

    @Override
    public DataSource getDataSource(DBResourceContextInfo ctx) throws SQLException {
        // 数据库实例
        String instanceName = ctx.getInstanceName();
        // 租户ID
        String tenantId = ctx.getTenantId();
        // 数据源的名称
        String dataSourceName = ctx.getDataSourceName();


        return null;
    }

    @Override
    public void onEvent(Event event) {

    }
}